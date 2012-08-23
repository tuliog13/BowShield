package tatu.bowshield.component;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.sprites.GameSprite;

public class ListView implements IOnButtonTouch {

    private String          PATH_BUTTON         = "gfx/listItem.png";
    private String          PATH_BUTTON_PRESSED = "gfx/listItemPressed.png";

    private ButtonManager   manager;
    private int             mX, mY;
    private Font            mTextFont;
    private int             mCount;

    OnListItemClickListener itemListener;
    BowShieldGameActivity   mReference;

    public ListView(BowShieldGameActivity reference, int x, int y) {

        mReference = reference;
        BitmapTextureAtlas mTextFontTextureAtlas = new BitmapTextureAtlas(mReference.getTextureManager(), 512, 512,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mTextFont = FontFactory.create(mReference.getFontManager(), mTextFontTextureAtlas, 32);

        mReference.getEngine().getFontManager().loadFont(mTextFont);

        mReference.getEngine().getTextureManager().loadTexture(mTextFontTextureAtlas);

        mCount = 0;
        manager = new ButtonManager(mReference, this);
        this.mX = x;
        this.mY = y;

    }

    public void addItem(String text, Object container) {

        int y = (50 * mCount) + mY;
        mCount++;

        ListItem newItem = new ListItem(mReference, text, PATH_BUTTON, PATH_BUTTON_PRESSED, mX, y, mCount - 1);
        newItem.setContainer(container);

        try {
            Text mText = new Text(mX, y, mTextFont, text, mReference.getVertexBufferObjectManager());
            newItem.setTextSprite(mText);
        } catch (Exception e) {
            e.printStackTrace();
        }

        manager.addButton(newItem);
    }

    public void clear() {
        detach();
    }

    public void updateItems(TouchEvent event) {

        manager.updateButtons(event);

    }

    public void draw() {

        Scene scene = mReference.getScene();
        manager.drawButtons();

        for (Button b : manager.getButtons()) {
            ListItem item = (ListItem) b;

            try {
                scene.attachChild(item.getTextSprite());
            } catch (Exception e) {
            }
        }

    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        itemListener = listener;
    }

    public void detach() {

        manager.detach();
        Scene scene = mReference.getScene();
        for (Button b : manager.getButtons()) {
            ListItem item = (ListItem) b;
            try {
                scene.detachChild(item.getTextSprite());
            } catch (Exception e) {
            }
        }
        mCount = 0;
        manager.buttons.clear();
    }

    @Override
    public void onButtonTouch(int buttonId) {
        Object container = ((ListItem) manager.getButtons().get(buttonId)).getContainer();
        itemListener.onItemClick(buttonId, container);
    }
}
