package tatu.bowshield.popups;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import tatu.bowshield.component.Button;
import tatu.bowshield.component.ListView;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.component.PopUpLayout;
import tatu.bowshield.screens.Menu;
import tatu.bowshield.sprites.GameSprite;
import android.bluetooth.BluetoothDevice;
import android.view.KeyEvent;

public class ScanPopUp extends PopUpLayout {

	ListView mDeviceList;
	private Text mErrorMessage;
	private Font mTextFont;
	private Menu mMenu;
	
	Button okButton;
	
	public static final int OK_BUTTON = 5;

	public ScanPopUp(Menu menuScreen) {

		WIDTH = 700;
		HEIGHT = 380;

		mDeviceList = new ListView(300, 100);
		mDeviceList.setOnListItemClickListener(menuScreen);

		BitmapTextureAtlas mTextFontTextureAtlas = new BitmapTextureAtlas(
				GameSprite.getGameReference().getTextureManager(), 512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTextFont = FontFactory.create(GameSprite.getGameReference()
				.getFontManager(), mTextFontTextureAtlas, 32);

		GameSprite.getGameReference().getEngine().getFontManager()
				.loadFont(mTextFont);

		GameSprite.getGameReference().getEngine().getTextureManager()
				.loadTexture(mTextFontTextureAtlas);

		mErrorMessage = new Text(300, 200, mTextFont,
				"Um erro ocorreu!", GameSprite
						.getGameReference().getVertexBufferObjectManager());
		
		mMenu = menuScreen;
		
		okButton = new Button(menuScreen.PATH_BUTTON, menuScreen.PATH_BUTTON_ABOUT, 250, 380,
				OK_BUTTON);
		
		mMenu.bManager.addButton(okButton);
		
	}

	@Override
	public void onDraw() {
		mDeviceList.draw();
	}

	@Override
	public void Update() {
		
	}

	@Override
	public void TouchEvent(org.andengine.input.touch.TouchEvent event) {
		mDeviceList.updateItems(event);
	}

	@Override
	public void Destroy() {
		mDeviceList.clear();
		mMenu.bManager.removeButton(okButton);
	}

	public void addBluetooth(BluetoothDevice device) {
		mDeviceList.addItem(device.getName(), device.getAddress());
	}

	@Override
	public void onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		PopUp.hidePopUp();
	}
	
}
