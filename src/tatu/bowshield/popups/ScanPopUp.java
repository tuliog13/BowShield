package tatu.bowshield.popups;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.component.Button;
import tatu.bowshield.component.ButtonManager;
import tatu.bowshield.component.ListView;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.component.PopUpLayout;
import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.screens.Menu;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.view.KeyEvent;

public class ScanPopUp extends PopUpLayout implements IOnButtonTouch {

    private String          PATH_FONT = "gfx/lithos.otf";

    ListView                mDeviceList;
    private Text            mScanPopUpText;
    private Text            mConnectingText;
    private Font            mTextFont;
    private Menu            mMenu;

    Button                  mRefreshButton;
    ButtonManager           mButtonManager;

    BowShieldGameActivity   mReference;
    Menu                    mMenuScreen;
    public static final int OK_BUTTON = 5;

    public ScanPopUp(BowShieldGameActivity reference, Menu menuScreen) {

        mReference = reference;
        mMenuScreen = menuScreen;

        WIDTH = 700;
        HEIGHT = 380;

        mDeviceList = new ListView(reference, 330, 160);
        mDeviceList.setOnListItemClickListener(menuScreen);

        mTextFont = FontFactory.createFromAsset(reference.getFontManager(), reference.getTextureManager(), 300, 200,
                reference.getAssets(), PATH_FONT, 32, true, Color.rgb(84, 56, 20));

        mReference.getEngine().getFontManager().loadFont(mTextFont);
        mTextFont.load();

        mScanPopUpText = new Text(190, 85, mTextFont, "Procurando por jogadores...",
                reference.getVertexBufferObjectManager());

        mConnectingText = new Text(400, 380, mTextFont, "Connecting...", reference.getVertexBufferObjectManager());

        mMenu = menuScreen;

        mButtonManager = new ButtonManager(mReference, this);

        mRefreshButton = new Button(reference, mMenuScreen.PATH_BUTTON_CREATE, mMenuScreen.PATH_BUTTON_ABOUT, 500, 160,
                OK_BUTTON);

    }

    @Override
    public void onDraw() {
        mDeviceList.draw();

        try {
            mReference.getScene().attachChild(mScanPopUpText);
        } catch (Exception e) {
        }
    }

    public void setConnecting() {
        try {
            mReference.getScene().attachChild(mConnectingText);
        } catch (Exception e) {
        }
    }

    public void setConnectionLost() {
        mDeviceList.clear();
        try {
            mReference.getScene().detachChild(mConnectingText);
            mButtonManager.addButton(mRefreshButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDiscoveryEnd() {
        mButtonManager.addButton(mRefreshButton);
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
            mReference.getScene().detachChild(mConnectingText);
            mButtonManager.detach();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBluetooth(BluetoothDevice device) {
        mDeviceList.addItem(device.getName(), device.getAddress());
    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        PopUp.hidePopUp();
    }

    @Override
    public void onButtonTouch(int buttonId) {
        mButtonManager.removeButton(mRefreshButton);
        mDeviceList.clear();
        mMenuScreen.scanDevices();
    }

}
