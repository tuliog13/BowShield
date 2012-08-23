package tatu.bowshield.popups;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.component.ListView;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.component.PopUpLayout;
import tatu.bowshield.screens.Menu;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.view.KeyEvent;

public class ScanPopUp extends PopUpLayout {

    private String          PATH_FONT = "gfx/lithos.otf";

    ListView                mDeviceList;
    private Text            mScanPopUpText;
    private Font            mTextFont;
    private Menu            mMenu;

    // Button okButton;

    BowShieldGameActivity   mReference;
    public static final int OK_BUTTON = 5;

    public ScanPopUp(BowShieldGameActivity reference, Menu menuScreen) {

        mReference = reference;

        WIDTH = 700;
        HEIGHT = 380;

        mDeviceList = new ListView(reference, 370, 160);
        mDeviceList.setOnListItemClickListener(menuScreen);

//        BitmapTextureAtlas mTextFontTextureAtlas = new BitmapTextureAtlas(reference.getTextureManager(), 512, 512,
//                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        
        mTextFont = FontFactory.createFromAsset(reference.getFontManager(), reference.getTextureManager(), 300, 200,
                reference.getAssets(), PATH_FONT, 32, true, Color.rgb(84, 56, 20));

        reference.getEngine().getFontManager().loadFont(mTextFont);
//        reference.getEngine().getTextureManager().loadTexture(mTextFontTextureAtlas);
        mTextFont.load();

        mScanPopUpText = new Text(190, 85, mTextFont, "Procurando por jogadores...",
                reference.getVertexBufferObjectManager());

        mMenu = menuScreen;

        // okButton = new Button(menuScreen.PATH_BUTTON_CREATE, menuScreen.PATH_BUTTON_ABOUT, 250, 380, OK_BUTTON);
        // mMenu.mButtonManager.addButton(okButton);

    }

    @Override
    public void onDraw() {
        mDeviceList.draw();
        
        try {
            mReference.getScene().attachChild(mScanPopUpText);
        } catch (Exception e) {
        }
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

        try {
            mReference.getScene().detachChild(mScanPopUpText);
        } catch (Exception e) {
        }
        // mMenu.mButtonManager.removeButton(okButton);
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
