package tatu.bowshield.activity;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.view.IRendererListener;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import tatu.bowshield.bluetooth.BluetoothService;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.control.TextureLoader;
import tatu.bowshield.screens.CutScene;
import tatu.bowshield.screens.Game;
import tatu.bowshield.screens.Menu;
import tatu.bowshield.screens.Screen;
import tatu.bowshield.screens.Splash;
import tatu.bowshield.sprites.GameSprite;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class BowShieldGameActivity extends SimpleBaseGameActivity implements IUpdateHandler, IOnSceneTouchListener,
        IRendererListener {

    private BluetoothService mChatService;
    private TextureLoader    mLoader;
    private Scene            myScene;

    public static float      DEVICE_WIDTH;
    public static float      DEVICE_HEIGHT;

    @Override
    public EngineOptions onCreateEngineOptions() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        DEVICE_WIDTH = metrics.xdpi;
        DEVICE_HEIGHT = metrics.ydpi;

        final Camera camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);

        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);

    }

    @Override
    public void onCreateResources() {
        mLoader = new TextureLoader(this, getAssets());
        Screen.setLoader(mLoader);

        GameSprite.setGameReference(this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public Scene getScene() {
        return this.myScene;
    }

    @Override
    public Scene onCreateScene() {

        this.mEngine.registerUpdateHandler(new FPSLogger());

        myScene = new Scene();
        myScene.setOnSceneTouchListener(this);
        myScene.registerUpdateHandler(this);

        Screen.setScene(myScene);

        PopUp.Inicialize();

        Screen splash = new Splash(Constants.SCREEN_SPLASH);
        Screen deviceListScreen = new Menu(Constants.SCREEN_DEVICE);
        Screen cutScene = new CutScene(Constants.SCREEN_CUTSCENE);
        Screen gameScreen = new Game(Constants.SCREEN_GAME);

        ScreenManager.addScreen(splash);
        ScreenManager.addScreen(deviceListScreen);
        ScreenManager.addScreen(cutScene);
        ScreenManager.addScreen(gameScreen);

        ScreenManager.changeScreen(Constants.SCREEN_SPLASH);

        return myScene;
    }

    public void Draw() {

    }

    public Engine getEngine() {
        return mEngine;
    }

    public BowShieldGameActivity getGameReference() {
        return this;
    }

    public TextureLoader getTextureLoader() {
        return mLoader;
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {

        if (!PopUp.isShowing()) {
            ScreenManager.Update();
        }

        PopUp.UpdatePopUp();
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (!PopUp.isShowing() && PopUp.popupUnloadDone) {
            ScreenManager.onSceneTouchEvent(pSceneTouchEvent);
        } else {
            PopUp.TouchEvent(pSceneTouchEvent);
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!PopUp.isShowing() && PopUp.popupUnloadDone) {
            ScreenManager.onKeyDown(keyCode, event);
        } else {
            PopUp.onKeyDown(keyCode, event);
        }
        return false;
    }

    public float getScreenWidth() {
        return DEVICE_WIDTH;
    }

    public float getScreenHeight() {
        return DEVICE_HEIGHT;
    }

}
