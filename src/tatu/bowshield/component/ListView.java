package tatu.bowshield.component;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.sprites.GameSprite;

public class ListView implements IOnButtonTouch {

	private String PATH_FONT = "gfx/Arial.TTF";
	
	private String PATH_BUTTON = "gfx/buttonn.png";
	private String PATH_BUTTON_PRESSED = "gfx/buttonp.png";

	private ButtonManager manager;
	private int mX;
	private Font mTextFont;
	private int mCount;
	
	OnListItemClickListener itemListener;

	public ListView(int x, int y) {

		BitmapTextureAtlas mTextFontTextureAtlas = new BitmapTextureAtlas(
				GameSprite.getGameReference().getTextureManager(), 512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTextFont = FontFactory.create(GameSprite.getGameReference()
				.getFontManager(), mTextFontTextureAtlas, 32);

		GameSprite.getGameReference().getEngine().getFontManager()
				.loadFont(mTextFont);

		GameSprite.getGameReference().getEngine().getTextureManager()
				.loadTexture(mTextFontTextureAtlas);

		mCount = 0;
		manager = new ButtonManager(this);
		this.mX = x;
	}

	public void addItem(String text, Object container) {
		
		int y = 200 * mCount;
		mCount++;
		
		ListItem newItem = new ListItem(text, PATH_BUTTON, PATH_BUTTON_PRESSED, mX, y, mCount - 1);
		newItem.setContainer(container);

		Text mText = new Text(mX, y, mTextFont, text, GameSprite
				.getGameReference().getVertexBufferObjectManager());
		newItem.setTextSprite(mText);

		manager.addButton(newItem);
	}

	public void clear() {
		detach();
	}

	public void updateItems(TouchEvent event) {

		manager.updateButtons(event);

	}

	public void draw() {
		
		Scene scene = GameSprite.getGameReference().getScene();
		manager.drawButtons();
		
		for(Button b : manager.getButtons()){
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
		Scene scene = GameSprite.getGameReference().getScene();
		for(Button b : manager.getButtons()){
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
