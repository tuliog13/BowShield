package tatu.bowshield.screens;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.bluetooth.BluetoothService;
import tatu.bowshield.component.Button;
import tatu.bowshield.component.ButtonManager;
import tatu.bowshield.component.OnListItemClickListener;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.popups.ScanPopUp;
import tatu.bowshield.popups.WaitingPopUp;
import tatu.bowshield.sprites.GameSprite;
import tatu.bowshield.util.DebugLog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

public class Menu extends Screen implements IOnButtonTouch, OnListItemClickListener {

    public static final String  PATH_BUTTON_CREATE         = "gfx/buttons/create_normal.png";
    private static final String PATH_BUTTON_CREATE_PRESSED = "gfx/buttons/create_pressed.png";
    private static final String PATH_BUTTON_JOIN           = "gfx/buttons/join_normal.png";
    private static final String PATH_BUTTON_JOIN_PRESSED   = "gfx/buttons/join_pressed.png";
    private static final String PATH_BUTTON_HELP           = "gfx/buttons/help_normal.png";
    private static final String PATH_BUTTON_HELP_PRESSED   = "gfx/buttons/help_pressed.png";
    public static final String  PATH_BUTTON_ABOUT          = "gfx/buttons/info_normal.png";
    private static final String PATH_BUTTON_ABOUT_PRESSED  = "gfx/buttons/info_pressed.png";
    private static final String PATH_IMAGE_HELP            = "gfx/telas/help.png";
    private static final String PATH_IMAGE_ABOUT           = "gfx/telas/about.png";

    private final int           BTN_CREATE                 = 0;
    private final int           BTN_JOIN                   = 1;
    private final int           BTN_SKIP                   = 2;
    private final int           BTN_HELP                   = 3;
    private final int           BTN_ABOUT                  = 4;

    private String              PATH_BACKGROUND            = "gfx/telas/menu.png";
    private Texture             mBackgroundTexture;
    private ITextureRegion      mBackgroundRegion;

    private Button              mButtonCreate;
    private Button              mButtonJoin;
    private Button              mButtonSkip;

    private Button              mButtonInfo;
    private Button              mButtonHelp;

    public ButtonManager        mButtonManager;

    private BluetoothAdapter    mBtAdapter;
    private BluetoothService    mChatService;

    private Handler             mHandler;

    private SimpleScreen        helpScreen;
    private SimpleScreen        AboutScreen;

    private ScanPopUp           mScanPopUp;
    private WaitingPopUp        mWaitingPopUp;

    public Menu(BowShieldGameActivity reference, int id) {
        super(reference, id);
    }

    @Override
    public void Initialize() {
        // TODO Auto-generated method stub

        mButtonCreate = new Button(mReference, PATH_BUTTON_CREATE, PATH_BUTTON_CREATE_PRESSED, 100, 90, BTN_CREATE);
        mButtonJoin = new Button(mReference, PATH_BUTTON_JOIN, PATH_BUTTON_JOIN_PRESSED, 450, 90, BTN_JOIN);
        mButtonSkip = new Button(mReference, PATH_BUTTON_HELP, PATH_BUTTON_HELP_PRESSED, 350, 350, BTN_SKIP);
        mButtonHelp = new Button(mReference, PATH_BUTTON_HELP, PATH_BUTTON_HELP_PRESSED, 50, 350, BTN_HELP);
        mButtonInfo = new Button(mReference, PATH_BUTTON_ABOUT, PATH_BUTTON_ABOUT_PRESSED, 670, 350, BTN_ABOUT);

        mButtonManager = new ButtonManager(mReference, this);
        mButtonManager.addButton(mButtonCreate);
        mButtonManager.addButton(mButtonJoin);
        mButtonManager.addButton(mButtonSkip);
        mButtonManager.addButton(mButtonHelp);
        mButtonManager.addButton(mButtonInfo);

        helpScreen = new SimpleScreen(mReference, Constants.SIMPLE_SCREEN_HELP, PATH_IMAGE_HELP);
        AboutScreen = new SimpleScreen(mReference, Constants.SIMPLE_SCREEN_ABOUT, PATH_IMAGE_ABOUT);

        addSimpleScreen(helpScreen);
        addSimpleScreen(AboutScreen);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mReference.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mReference.registerReceiver(mReceiver, filter);

        mReference.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                mHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case BluetoothService.STATE_CONNECTED:
                                DebugLog.log("Connected");
                                PopUp.forceClose();
                                ScreenManager.changeScreen(Constants.SCREEN_CUTSCENE);
                                break;

                            case BluetoothService.STATE_CONNECTING:
                                DebugLog.log("Connecting");
                                if (mScanPopUp != null) {
                                    mScanPopUp.setConnecting();
                                }
                                break;

                            case BluetoothService.STATE_LISTEN:
                                DebugLog.log("Waiting Connection");
                                break;

                            case BluetoothService.STATE_LOST:
                                DebugLog.log("Connection Lost");

                                if (mChatService != null) {
                                    mChatService.stop();
                                }
                                
                                if (mScanPopUp != null) {
                                    mScanPopUp.setConnectionLost();
                                    
                                }

                                break;
                        }
                    };
                };

                mBtAdapter = BluetoothAdapter.getDefaultAdapter();

                mChatService = BluetoothService.getInstance(mBtAdapter);
                mChatService.setHandler(mHandler);
            }
        });

        mBackgroundTexture = getLoader().load(PATH_BACKGROUND);
        mBackgroundTexture.load();
        mBackgroundRegion = TextureRegionFactory.extractFromTexture(mBackgroundTexture);

        super.Initialize();

    }

    @Override
    public void Load() {
        // TODO Auto-generated method stub
        super.Load();
    }

    @Override
    public void Update() {
        // TODO Auto-generated method stub
        super.Update();
        // helpScreen.Update();
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        mButtonManager.updateButtons(pSceneTouchEvent);
        return super.onSceneTouchEvent(pSceneTouchEvent);
    }

    @Override
    public void Draw() {

        getScene().setBackground(
                new SpriteBackground(new Sprite(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT,
                        mBackgroundRegion, mReference.getVertexBufferObjectManager())));

        mButtonManager.drawButtons();
        super.Draw();
    }

    @Override
    public void Destroy() {
        mReference.unregisterReceiver(mReceiver);

        mReference.runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                mButtonManager.detach();
            }
        });

        super.Destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        // ScreenManager.changeScreen(getId() - 1);

        PopUp.hidePopUp();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onButtonTouch(int buttonId) {

        switch (buttonId) {

            case BTN_CREATE:

                PopUp.showPopUp(mWaitingPopUp = new WaitingPopUp(mReference));

                makeDiscoverable();
                mChatService.start();
                GamePhysicalData.GAME_TYPE = GamePhysicalData.SERVER_TYPE;
                PopUp.bringToFront();

                DebugLog.log("START CALLED!");
                break;

            case BTN_JOIN:

                PopUp.showPopUp(mScanPopUp = new ScanPopUp(mReference, this));

                ScreenManager.reDraw();
                scanDevices();
                GamePhysicalData.GAME_TYPE = GamePhysicalData.CLIENT_TYPE;
                PopUp.bringToFront();

                DebugLog.log("Searching for devices... ");
                break;

            case BTN_SKIP:
                ScreenManager.changeScreen(Constants.SCREEN_CUTSCENE);
                break;

            case BTN_HELP:
                ScreenManager.showSimpleScreen(Constants.SIMPLE_SCREEN_HELP);
                break;

            case BTN_ABOUT:
                ScreenManager.showSimpleScreen(Constants.SIMPLE_SCREEN_ABOUT);
                break;

        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                                                  @Override
                                                  public void onReceive(Context context, Intent intent) {
                                                      String action = intent.getAction();
                                                      if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                                                          BluetoothDevice device = intent
                                                                  .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                                                          ScreenManager.reDraw();

                                                          if (mScanPopUp != null) {
                                                              mScanPopUp.addBluetooth(device);
                                                          }

                                                          DebugLog.log("Device found: " + device.getName());
                                                      }

                                                      else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                                                              .equals(action)) {
                                                          DebugLog.log("Descover finished!");
                                                          
                                                          if (mScanPopUp != null) {
                                                              mScanPopUp.setDiscoveryEnd();
                                                          }
                                                          
                                                          ScreenManager.reDraw();
                                                          
                                                      }
                                                  }
                                              };

    public void scanDevices() {

        makeDiscoverable();

        mBtAdapter.cancelDiscovery();
        mBtAdapter.startDiscovery();

        Log.v("TESTE", "Discovery started!");
    }

    private void makeDiscoverable() {
        // if (mBtAdapter.getScanMode() !=
        // BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        mReference.startActivity(discoverableIntent);
        // }
    }

    @Override
    public void onItemClick(int position, Object container) {

        mBtAdapter.cancelDiscovery();

        String address = (String) container;

        BluetoothDevice device = mBtAdapter.getRemoteDevice(address);

        mChatService.connect(device);

    }

}
