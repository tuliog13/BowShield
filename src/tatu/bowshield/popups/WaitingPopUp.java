package tatu.bowshield.popups;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import android.view.KeyEvent;

import tatu.bowshield.component.PopUp;
import tatu.bowshield.component.PopUpLayout;
import tatu.bowshield.sprites.GameSprite;

public class WaitingPopUp extends PopUpLayout {

	private Text mWatingText;
	// private Text mConnectingText;
	private Font mTextFont;
	private Scene mScene;
	public boolean attached = false;

	public WaitingPopUp(Scene scene) {
		WIDTH = 700;
		HEIGHT = 380;

		BitmapTextureAtlas mTextFontTextureAtlas = new BitmapTextureAtlas(
				GameSprite.getGameReference().getTextureManager(), 512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTextFont = FontFactory.create(GameSprite.getGameReference()
				.getFontManager(), mTextFontTextureAtlas, 32);

		GameSprite.getGameReference().getEngine().getFontManager()
				.loadFont(mTextFont);

		GameSprite.getGameReference().getEngine().getTextureManager()
				.loadTexture(mTextFontTextureAtlas);

		mWatingText = new Text(100, 200, mTextFont,
				"Jogo criado, aguardando oponente.", GameSprite
						.getGameReference().getVertexBufferObjectManager());

		// mConnectingText = new Text(300, 300, mTextFont,
		// "Alguem parece estar chegando...", GameSprite
		// .getGameReference().getVertexBufferObjectManager());
		mScene = scene;
	}

	@Override
	public void onDraw() {
		if (!attached) {
			try {
				mScene.attachChild(mWatingText);
				attached = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void TouchEvent(org.andengine.input.touch.TouchEvent event) {
		// TODO Auto-generated method stub

	}

	public void setConnecting() {
		try {
			mScene.detachChild(mWatingText);
			// mScene.attachChild(mConnectingText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void Destroy() {
		try {
			mScene.detachChild(mWatingText);
			// mScene.detachChild(mConnectingText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		PopUp.hidePopUp();
	}

}
