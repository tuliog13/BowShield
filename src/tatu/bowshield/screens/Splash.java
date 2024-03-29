package tatu.bowshield.screens;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.OnPopupResult;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.popups.ConfirmDialog;
import tatu.bowshield.sprites.GameSprite;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Splash extends Screen implements OnPopupResult {

    private String         PATH_BACKGROUND = "gfx/splash.png";
    private Texture        mBackgroundTexture;
    private ITextureRegion mBackgroundRegion;

    public Splash(BowShieldGameActivity reference, int id) {
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

        PopUp.setListener(this);

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

        int myEventAction = pSceneTouchEvent.getAction();

        switch (myEventAction) {

            case MotionEvent.ACTION_UP:

                ScreenManager.changeScreen(1);
                break;

        }

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            PopUp.showPopUp(new ConfirmDialog(mReference, 500, 350));
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void Draw() {
        // TODO Auto-generated method stub
        

        getScene().setBackground(
                new SpriteBackground(new Sprite(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT,
                        mBackgroundRegion, mReference.getVertexBufferObjectManager())));
        super.Draw();
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        super.Destroy();

        mBackgroundTexture = null;
        mBackgroundRegion = null;
    }

    @Override
    public void onResultReceived(int result) {
        // TODO Auto-generated method stub

        switch (result) {
            case Constants.RESULT_YES:
                System.gc();
                mReference.finish();
                break;

            case Constants.RESULT_NO:
                PopUp.hidePopUp();
                break;
        }

    }

}
