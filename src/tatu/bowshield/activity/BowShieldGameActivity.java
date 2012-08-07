package tatu.bowshield.activity;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.bluetooth.BluetoothChatService;
import tatu.bowshield.bluetooth.OnMessageReceivedListener;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.component.PopUpLayout;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.control.TextureLoader;
import tatu.bowshield.screens.Menu;
import tatu.bowshield.screens.Game;
import tatu.bowshield.screens.Screen;
import tatu.bowshield.screens.Splash;
import tatu.bowshield.sprites.Arc;
import tatu.bowshield.sprites.Arrow;
import tatu.bowshield.sprites.GameSprite;
import tatu.bowshield.sprites.BowMan;
import tatu.bowshield.sprites.Rope;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class BowShieldGameActivity extends SimpleBaseGameActivity implements OnMessageReceivedListener, IUpdateHandler,
        IOnSceneTouchListener {

    private BluetoothChatService mChatService;
    private TextureLoader        mLoader;
    private Scene                myScene;

    public static int DEVICE_WIDTH;
    public static int DEVICE_HEIGHT;
    
    @Override
    public EngineOptions onCreateEngineOptions() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        DEVICE_WIDTH = metrics.widthPixels;
        DEVICE_HEIGHT = metrics.heightPixels;
        
        final Camera camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
        
        //mChatService = BluetoothChatService.getInstance();
      //  mChatService.setOnMenssageReceivedListener(this);

        

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
        Screen gameScreen = new Game(Constants.SCREEN_GAME);
        
        ScreenManager.addScreen(splash);
        ScreenManager.addScreen(deviceListScreen);
        ScreenManager.addScreen(gameScreen);
        
        ScreenManager.changeScreen(Constants.SCREEN_SPLASH);
        
        
        return myScene;
    }

    public void Draw() {

    }

    public Engine getEngine() {
        return this.mEngine;
    }

    public BowShieldGameActivity getGameReference() {
        return this;
    }

    public TextureLoader getTextureLoader() {
        return mLoader;
    }

    @Override
    public void sendMessage(String message) {
        DebugLog.log("Message sended: " + message);
        //mChatService.write(message);
    }

    @Override
    public void onMessageReceived(String message) {
        DebugLog.log("Message received: " + message);
        
        
//        float angle, force;
//        int i;
//        
//        for(i = 0; i < message.length() && message.charAt(i) != ','; i++);
//        
//        String s = message.substring(0, i);
//        angle = Float.parseFloat(s);
//        
//        s = message.substring(i + 1);
//        force = Float.parseFloat(s);
//        
//        mArrow.configPreLaunch(angle, force);
//        
//        GamePhysicalData.sShoted = true;
//        GamePhysicalData.mDistance = 0;
//        
//        GamePhysicalData.setAngle(angle);
//        GamePhysicalData.setForce(force);
        
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        
    	if(!PopUp.isShowing())
    	{
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
    	if(!PopUp.isShowing() && PopUp.popupUnloadDone)
    	{
    		ScreenManager.onSceneTouchEvent(pSceneTouchEvent);
    	}
    	else
    	{
    		PopUp.TouchEvent(pSceneTouchEvent);
    	}
        return false;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(!PopUp.isShowing() && PopUp.popupUnloadDone)
    	{
           ScreenManager.getCurrentScreen().onKeyDown(keyCode, event);
    	}
        return false;
    }

}
