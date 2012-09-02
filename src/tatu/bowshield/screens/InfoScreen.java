package tatu.bowshield.screens;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.sprites.GameSprite;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class InfoScreen extends Screen {

    private String         PATH_BACKGROUND = "gfx/telas/about.png";
    private Texture        mBackgroundTexture;
    private ITextureRegion mBackgroundRegion;
    
    public InfoScreen(BowShieldGameActivity reference, int id) {
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

        

        return false;
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

    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        super.Destroy();

        // getScene().setBackground(null);

        mBackgroundTexture = null;
        mBackgroundRegion = null;
    }

}