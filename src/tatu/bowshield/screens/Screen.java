package tatu.bowshield.screens;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.TextureLoader;
import tatu.bowshield.sprites.GameSprite;
import android.view.KeyEvent;

public abstract class Screen {

    private int                     mId;

    protected static TextureLoader  mLoader;
    protected static Scene          mScene;
    protected List<SimpleScreen>    mSimpleScreen;
    protected BowShieldGameActivity mReference;
    
    private Rectangle branco;
    private int mAlpha;

    public Screen(BowShieldGameActivity reference, int id) {
        this.mId = id;
        mReference = reference;
        
        branco = new Rectangle(0, 0, 800, 480, mReference.getVertexBufferObjectManager());
        branco.setColor(0f, 0f, 0f);
        mAlpha = 50;
    }

    public void Initialize() {
        
        mAlpha = 50;
        branco.setVisible(true);
        branco.setAlpha(mAlpha);
    }

    public void Load() {

    }

    public void Update() {
        
        
        if(mAlpha <= 250)
        {
            branco.setVisible(true);
            branco.setAlpha(mAlpha);
            mAlpha += 15;
        }
        else
        {
            branco.setVisible(false);
        }
    }

    public void Draw() {
        try
        {
            mReference.getScene().attachChild(branco);
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        branco.setVisible(false);
    }

    public void addSimpleScreen(SimpleScreen screen) {
        if (mSimpleScreen == null) {
            mSimpleScreen = new ArrayList<SimpleScreen>();
        }

        mSimpleScreen.add(screen);
    }

    public boolean hasSimpleScreens() {
        return (mSimpleScreen != null);
    }

    public SimpleScreen getActivedSimpleScreen() {
        for (SimpleScreen screen : mSimpleScreen) {
            if (screen.isShowing()) {
                return screen;
            }
        }
        return null;
    }

    public List<SimpleScreen> get_simpleScreens() {
        return mSimpleScreen;
    }

    public void set_simpleScreens(List<SimpleScreen> _simpleScreens) {
        this.mSimpleScreen = _simpleScreens;
    }

    public void Destroy() {
        branco.detachSelf();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return false;
    }

//    public static void resetScene() {
//        mScene = new Scene();
//        mScene.setOnSceneTouchListener();
//        mScene.registerUpdateHandler(mReference);
//    }

    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        // TODO Auto-generated method stub
        return false;
    }

    public int getId() {
        return this.mId;
    }

    public static TextureLoader getLoader() {
        return mLoader;
    }

    public static void setLoader(TextureLoader _loader) {
        Screen.mLoader = _loader;
    }

    public static Scene getScene() {
        return mScene;
    }

    public static void setScene(Scene _scene) {
        Screen.mScene = _scene;
    }

}
