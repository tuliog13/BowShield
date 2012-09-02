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
import tatu.bowshield.screens.HelpScreen;
import tatu.bowshield.screens.InfoScreen;
import tatu.bowshield.screens.Menu;
import tatu.bowshield.screens.ResultScreen;
import tatu.bowshield.screens.Results;
import tatu.bowshield.screens.Screen;
import tatu.bowshield.screens.Splash;
import tatu.bowshield.sprites.GameSprite;
import tatu.bowshield.util.DebugLog;
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

        DEVICE_WIDTH = metrics.widthPixels;
        DEVICE_HEIGHT = metrics.heightPixels;
        
        DebugLog.log("" +DEVICE_WIDTH + " x " +DEVICE_HEIGHT );
        
        final Camera camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);

        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);

    }

    @Override
    public void onCreateResources() {
        mLoader = new TextureLoader(this, getAssets());
        Screen.setLoader(mLoader);
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

        PopUp.Inicialize(this);

        Screen splash = new Splash(this, Constants.SCREEN_SPLASH);
        Screen deviceListScreen = new Menu(this, Constants.SCREEN_DEVICE);
        Screen cutScene = new CutScene(this, Constants.SCREEN_CUTSCENE);
        Screen gameScreen = new Game(this, Constants.SCREEN_GAME);
        Screen helpScreen = new HelpScreen(this, Constants.SCREEN_HELP);
        Screen infoScreen = new InfoScreen(this, Constants.SCREEN_INFO);
        Screen results = new ResultScreen(this, Constants.SCREEN_RESULTS);

        ScreenManager.addScreen(splash);
        ScreenManager.addScreen(deviceListScreen);
        ScreenManager.addScreen(cutScene);
        ScreenManager.addScreen(gameScreen);
        ScreenManager.addScreen(helpScreen);
        ScreenManager.addScreen(infoScreen);
        ScreenManager.addScreen(results);

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
