package tatu.bowshield.popups;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.component.Button;
import tatu.bowshield.component.ButtonManager;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.component.PopUpLayout;
import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.screens.Menu;
import tatu.bowshield.sprites.AnimatedGameSprite;
import tatu.bowshield.sprites.GameSprite;
import android.graphics.Color;
import android.view.KeyEvent;

public class WaitingPopUp extends PopUpLayout implements IOnButtonTouch {

    private String             PATH_FONT           = "gfx/lithos.otf";
    private String             PATH_LOADING        = "gfx/loading_new.png";
    private String             PATH_BUTTON         = "gfx/listItem.png";
    private String             PATH_BUTTON_PRESSED = "gfx/listItemPressed.png";

    private final int          OK_BUTTON           = 0;

    private Text               mWatingText;
    private Text               mCancelText;
    private Font               mTextFont;
    Button                     mCancelButton;
    ButtonManager              mButtonManager;

    private AnimatedGameSprite mLoadingSprite;
    private Scene              mScene;
    public boolean             attached            = false;

    public WaitingPopUp(BowShieldGameActivity reference, Menu menuScreen) {

        WIDTH = 700;
        HEIGHT = 380;

        mTextFont = FontFactory.createFromAsset(reference.getFontManager(), reference.getTextureManager(), 300, 200,
                reference.getAssets(), PATH_FONT, 32, true, Color.rgb(84, 56, 20));

        reference.getEngine().getFontManager().loadFont(mTextFont);
        mTextFont.load();

        mWatingText = new Text(190, 85, mTextFont, "Aguardando jogadores...", reference.getVertexBufferObjectManager());
        mCancelText = new Text(480, 357, mTextFont, "Cancel", reference.getVertexBufferObjectManager());
        
        mLoadingSprite = new AnimatedGameSprite(reference, PATH_LOADING, 420, 200, 8, 1);

        mScene = reference.getScene();
        mLoadingSprite.setAnimationSettings(new long[] { 120, 120, 120, 120, 120, 120 }, 1, 6, true);
        mLoadingSprite.animate();

        mButtonManager = new ButtonManager(reference, this);
        mCancelButton = new Button(reference, PATH_BUTTON, PATH_BUTTON_PRESSED, 450, 350, OK_BUTTON);

        mButtonManager.addButton(mCancelButton);

    }

    @Override
    public void onDraw() {

        mButtonManager.drawButtons();

        try {
            mScene.attachChild(mLoadingSprite.getSprite());
            mScene.attachChild(mWatingText);
            mScene.attachChild(mCancelText);
        } catch (Exception e) {
        }
    }

    @Override
    public void Update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void TouchEvent(org.andengine.input.touch.TouchEvent event) {
        // TODO Auto-generated method stub
        mButtonManager.updateButtons(event);
    }

    @Override
    public void Destroy() {
        try {
            mScene.detachChild(mWatingText);
            mScene.detachChild(mCancelText);
            mScene.detachChild(mLoadingSprite.getSprite());
            mButtonManager.detach();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {
        PopUp.hidePopUp();
    }

    @Override
    public void onButtonTouch(int buttonId) {
        PopUp.hidePopUp();
    }

}
