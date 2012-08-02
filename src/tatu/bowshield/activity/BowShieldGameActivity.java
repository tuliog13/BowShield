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
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.TextureLoader;
import tatu.bowshield.sprites.Arc;
import tatu.bowshield.sprites.Arrow;
import tatu.bowshield.sprites.Player;
import tatu.bowshield.sprites.Rope;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class BowShieldGameActivity extends SimpleBaseGameActivity implements OnMessageReceivedListener, IUpdateHandler,
        IOnSceneTouchListener {

    private String               PATH_BACKGROUND = "gfx/bg.png";
    private String               PATH_PLAYER1    = "gfx/arq.png";
    private String               PATH_PLAYER2    = "gfx/pers.png";
    private String               PATH_ARC        = "gfx/arco.png";
    private String               PATH_ARROW      = "gfx/flecha1.png";
    private String               PATH_ROPE       = "gfx/corda.png";

    public static float          CAMERA_WIDTH    = 800;
    public static float          CAMERA_HEIGHT   = 480;

    public static float          SPRITE_X_SCALE;
    public static float          SPRITE_Y_SCALE;

    private Texture              mBackgroundTexture;
    private ITextureRegion       mBackgroundRegion;

    private Player               mPlayer, mEnemy;
    private Arc                  mArc;
    private Arrow                mArrow;
    private Rope                 mRope;
    private Scene                myScene;

    private BluetoothChatService mChatService;
    private TextureLoader        mLoader;

    @Override
    public EngineOptions onCreateEngineOptions() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mChatService = BluetoothChatService.getInstance();
        mChatService.setOnMenssageReceivedListener(this);

        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);

    }

    @Override
    public void onCreateResources() {

        mLoader = new TextureLoader(this, getAssets());

        mBackgroundTexture = mLoader.load(PATH_BACKGROUND);
        mBackgroundTexture.load();
        mBackgroundRegion = TextureRegionFactory.extractFromTexture(mBackgroundTexture);

        mPlayer = new Player(this, PATH_PLAYER1, 10, 310);
        mEnemy = new Player(this, PATH_PLAYER2, 700, 310);
        mArc = new Arc(this, PATH_ARC, mPlayer.getX() + 105, mPlayer.getY());
        mArrow = new Arrow(this, PATH_ARROW, mArc.getX() + 160, mArc.getY() + 160);
        mRope = new Rope(this, PATH_ROPE, 150, 145);
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
        myScene.setBackground(new SpriteBackground(new Sprite(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, mBackgroundRegion,
                getVertexBufferObjectManager())));
        myScene.registerUpdateHandler(this);

        mArrow.Move(GamePhysicalData.sShoted, GamePhysicalData.getAngulo(), GamePhysicalData.getForca());

        Draw(myScene);

        return myScene;
    }

    public void Draw(Scene s) {
        
        mPlayer.Draw();
        mEnemy.Draw();
        mArc.Draw();
        mArrow.Draw();
        mRope.Draw();
        
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
        mChatService.write(message);
    }

    @Override
    public void onMessageReceived(String message) {
        DebugLog.log("Message received: " + message);
        
        
        float angle, force;
        int i;
        
        for(i = 0; i < message.length() && message.charAt(i) != ','; i++);
        
        String s = message.substring(0, i);
        angle = Float.parseFloat(s);
        
        s = message.substring(i + 1);
        force = Float.parseFloat(s);
        
        mArrow.configPreLaunch(angle, force);
        
        GamePhysicalData.sShoted = true;
        GamePhysicalData.mDistance = 0;
        
        GamePhysicalData.setAngle(angle);
        GamePhysicalData.setForce(force);
        
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        mArrow.Move(GamePhysicalData.sShoted, GamePhysicalData.getAngulo(), GamePhysicalData.getForca());
        mRope.Move(GamePhysicalData.getAngulo(), GamePhysicalData.getDistance());
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        
        GamePhysicalData.calculateTouch(pSceneTouchEvent, getGameReference());
        mArc.Move(GamePhysicalData.getAngulo(), GamePhysicalData.getForca());
        mRope.onTouchEvent(pSceneTouchEvent);

        return false;
    }

}
