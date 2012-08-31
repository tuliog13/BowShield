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
import tatu.bowshield.sprites.AnimatedGameSprite;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.view.KeyEvent;

public class ScanPopUp extends PopUpLayout implements IOnButtonTouch {

    private String             PATH_FONT           = "gfx/lithos.otf";
    private String             PATH_LOADING        = "gfx/loading.png";
    private String             PATH_BUTTON         = "gfx/listItem.png";
    private String             PATH_BUTTON_PRESSED = "gfx/listItemPressed.png";

    ListView                   mDeviceList;
    private Text               mScanPopUpText;
    private Text               mConnectingText;
    private Font               mTextFont;
    private Menu               mMenu;
    private Text               mCancelText;
    private Text               mRefreshText;
    Button                     mCancelButton;

    Button                     mRefreshButton;
    ButtonManager              mButtonManager;
    private AnimatedGameSprite mLoadingSprite;

    BowShieldGameActivity      mReference;
    Menu                       mMenuScreen;
    public static final int    REFRESH_BUTTON      = 0;
    public static final int    CANCEL_BUTTON       = 1;

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
        mLoadingSprite = new AnimatedGameSprite(reference, PATH_LOADING, 600, 260, 9, 1);

        mMenu = menuScreen;

        mButtonManager = new ButtonManager(mReference, this);

        mRefreshButton = new Button(reference, PATH_BUTTON, PATH_BUTTON_PRESSED, 530, 260, REFRESH_BUTTON);
        mRefreshText = new Text(560, 267, mTextFont, "Refresh", reference.getVertexBufferObjectManager());

        mCancelButton = new Button(reference, PATH_BUTTON, PATH_BUTTON_PRESSED, 530, 350, CANCEL_BUTTON);
        mCancelText = new Text(560, 357, mTextFont, "Cancel", reference.getVertexBufferObjectManager());

        mButtonManager.addButton(mRefreshButton);
        mButtonManager.addButton(mCancelButton);

        mRefreshButton.setVisibility(false);
        mLoadingSprite.setAnimationSettings(new long[] { 120, 120, 120, 120, 120, 120, 120, 120 }, 1, 8, true);
        mLoadingSprite.animate();

    }

    @Override
    public void onDraw() {
        mDeviceList.draw();
        mButtonManager.drawButtons();
        try {
            mReference.getScene().attachChild(mScanPopUpText);
            mReference.getScene().attachChild(mLoadingSprite.getSprite());
            mReference.getScene().attachChild(mCancelText);
            mRefreshButton.setVisibility(false);
            mReference.getScene().detachChild(mRefreshText);
        } catch (Exception e) {
        }
    }

    public void setConnecting() {
        try {
            mReference.getScene().attachChild(mConnectingText);
            mRefreshButton.setVisibility(false);
            mReference.getScene().detachChild(mRefreshText);
        } catch (Exception e) {
        }
    }

    public void setConnectionLost() {
        mDeviceList.clear();
        mRefreshButton.setVisibility(true);
        try {
            mReference.getScene().detachChild(mConnectingText);
            mReference.getScene().attachChild(mRefreshText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDiscoveryEnd() {
        mRefreshButton.setVisibility(true);
        mReference.getScene().detachChild(mLoadingSprite.getSprite());
        mReference.getScene().attachChild(mRefreshText);

    }

    @Override
    public void Update() {

    }

    @Override
    public void TouchEvent(org.andengine.input.touch.TouchEvent event) {
        mDeviceList.updateItems(event);
        mButtonManager.updateButtons(event);
    }

    @Override
    public void Destroy() {
        mDeviceList.clear();
        try {
            mReference.getScene().detachChild(mScanPopUpText);
            mReference.getScene().detachChild(mConnectingText);
            mReference.getScene().detachChild(mCancelText);
            mReference.getScene().detachChild(mRefreshText);
            mReference.getScene().detachChild(mLoadingSprite.getSprite());
            mRefreshButton.setVisibility(false);
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
        PopUp.hidePopUp();
    }

    @Override
    public void onButtonTouch(int buttonId) {
        if (buttonId == REFRESH_BUTTON) {
            mRefreshButton.setVisibility(false);
            mReference.getScene().detachChild(mRefreshText);
            mReference.getScene().attachChild(mLoadingSprite.getSprite());
            mDeviceList.clear();
            mMenuScreen.scanDevices();
        } else {
            PopUp.hidePopUp();
        }
    }
}
