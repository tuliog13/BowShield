package tatu.bowshield.screens;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.bluetooth.BluetoothService;
import tatu.bowshield.bluetooth.OnMessageReceivedListener;
import tatu.bowshield.component.Button;
import tatu.bowshield.component.ButtonManager;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.FruitController;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.control.OpponentView;
import tatu.bowshield.control.PlayersController;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.sprites.GameSprite;
import android.bluetooth.BluetoothAdapter;
import android.util.Log;
import android.view.KeyEvent;

public class CutScene extends Screen implements OnMessageReceivedListener, IOnButtonTouch {

    private String           PATH_BACKGROUND     = "gfx/telas/menu.png";
    private String           PATH_READY          = "gfx/imgReady.png";
    private String           PATH_NOTREADY       = "gfx/imgNotReady.png";

    private String           PATH_BUTTON         = "gfx/listItem.png";
    private String           PATH_BUTTON_PRESSED = "gfx/listItemPressed.png";

    private final String     READY               = "0";

    private Texture          mBackgroundTexture;
    private ITextureRegion   mBackgroundRegion;

    private GameSprite       mMyReadySprite;
    private GameSprite       mMyNotReadySprite;

    private GameSprite       mOppReadySprite;
    private GameSprite       mOppNotReadySprite;

    private BluetoothService mBluetoothService;
    Button                   mReadyButton;
    ButtonManager            mButtonManager;

    private boolean          isOppReady          = false;
    private boolean          isMeReady           = false;

    public CutScene(BowShieldGameActivity reference, int id) {
        super(reference, id);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void Initialize() {
        // TODO Auto-generated method stub
        super.Initialize();

        mBackgroundTexture = getLoader().load(PATH_BACKGROUND);
        mBackgroundTexture.load();
        mBackgroundRegion = TextureRegionFactory.extractFromTexture(mBackgroundTexture);

        int buttonX = 0;

        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {
            mMyReadySprite = new GameSprite(mReference, PATH_READY, 100, 140);
            mMyNotReadySprite = new GameSprite(mReference, PATH_NOTREADY, 100, 140);

            mOppReadySprite = new GameSprite(mReference, PATH_READY, 400, 140);
            mOppNotReadySprite = new GameSprite(mReference, PATH_NOTREADY, 400, 140);

            buttonX = 100;
        } else {

            mMyReadySprite = new GameSprite(mReference, PATH_READY, 400, 140);
            mMyNotReadySprite = new GameSprite(mReference, PATH_NOTREADY, 400, 140);

            mOppReadySprite = new GameSprite(mReference, PATH_READY, 100, 140);
            mOppNotReadySprite = new GameSprite(mReference, PATH_NOTREADY, 100, 140);
            buttonX = 400;
        }

        mReference.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mBluetoothService = BluetoothService.getInstance(BluetoothAdapter.getDefaultAdapter());
                mBluetoothService.setOnMenssageReceivedListener(CutScene.this);
            }
        });

        mReadyButton = new Button(mReference, PATH_BUTTON, PATH_BUTTON_PRESSED, buttonX, 250, 0);
        mButtonManager = new ButtonManager(mReference, this);

        mButtonManager.addButton(mReadyButton);

        isOppReady = false;
        isMeReady = false;
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
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {

        mButtonManager.updateButtons(pSceneTouchEvent);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        ScreenManager.changeScreen(Constants.SCREEN_DEVICE);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void Draw() {
        // TODO Auto-generated method stub
        super.Draw();

        getScene().setBackground(
                new SpriteBackground(new Sprite(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT,
                        mBackgroundRegion, mReference.getVertexBufferObjectManager())));

        mMyReadySprite.Draw();
        mMyNotReadySprite.Draw();
        
        mOppReadySprite.Draw();
        mOppNotReadySprite.Draw();
        
        mMyReadySprite.getSprite().setVisible(false);
        mOppReadySprite.getSprite().setVisible(false);

        mButtonManager.drawButtons();

    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        super.Destroy();

        // getScene().setBackground(null);

        mBackgroundTexture = null;
        mBackgroundRegion = null;

        mReference.runOnUpdateThread(new Runnable() {

            @Override
            public void run() {
                getScene().detachChild(mMyReadySprite.getSprite());
                getScene().detachChild(mMyNotReadySprite.getSprite());

                getScene().detachChild(mOppReadySprite.getSprite());
                getScene().detachChild(mOppNotReadySprite.getSprite());
                mButtonManager.detach();
            }

        });


    }

    @Override
    public void sendMessage(byte type, String message) {
        mBluetoothService.write(type, message);
    }

    @Override
    public void onMessageReceived(byte type, String message) {

        if (type == BluetoothService.READY) {
            mOppReadySprite.getSprite().setVisible(true);
            mOppNotReadySprite.getSprite().setVisible(false);
            isOppReady = true;

            if (isMeReady) {
                ScreenManager.changeScreen(Constants.SCREEN_GAME);
            }
        }

        Log.v("TESTE", "Menssage received: " + message);

    }

    @Override
    public void onButtonTouch(int buttonId) {

        mMyReadySprite.getSprite().setVisible(true);
        mMyNotReadySprite.getSprite().setVisible(false);
        sendMessage(BluetoothService.READY, READY);

        if (isOppReady) {
            ScreenManager.changeScreen(Constants.SCREEN_GAME);
        }

        isMeReady = true;

    }

}
