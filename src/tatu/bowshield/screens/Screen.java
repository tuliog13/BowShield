package tatu.bowshield.screens;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.control.TextureLoader;
import tatu.bowshield.sprites.GameSprite;
import android.view.KeyEvent;

public abstract class Screen {

    private int                    _id;

    protected static TextureLoader _loader;
    protected static Scene         _scene;
    protected List<SimpleScreen>   _simpleScreens;

    public Screen(int id) {
        this._id = id;
    }

    public void Initialize() {

    }

    public void Load() {

    }

    public void Update() {

    }

    public void Draw() {

    }

    public void addSimpleScreen(SimpleScreen screen) {
        if (_simpleScreens == null) {
            _simpleScreens = new ArrayList<SimpleScreen>();
        }

        _simpleScreens.add(screen);
    }

    public boolean hasSimpleScreens() {
        return (_simpleScreens != null);
    }

    public SimpleScreen getActivedSimpleScreen() {
        for (SimpleScreen screen : _simpleScreens) {
            if (screen.isShowing()) {
                return screen;
            }
        }
        return null;
    }

    public List<SimpleScreen> get_simpleScreens() {
        return _simpleScreens;
    }

    public void set_simpleScreens(List<SimpleScreen> _simpleScreens) {
        this._simpleScreens = _simpleScreens;
    }

    public void Destroy() {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return false;
    }

    public static void resetScene() {
        _scene = new Scene();
        _scene.setOnSceneTouchListener(GameSprite.getGameReference());
        _scene.registerUpdateHandler(GameSprite.getGameReference());
    }

    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        // TODO Auto-generated method stub
        return false;
    }

    public int getId() {
        return this._id;
    }

    public static TextureLoader getLoader() {
        return _loader;
    }

    public static void setLoader(TextureLoader _loader) {
        Screen._loader = _loader;
    }

    public static Scene getScene() {
        return _scene;
    }

    public static void setScene(Scene _scene) {
        Screen._scene = _scene;
    }

}
