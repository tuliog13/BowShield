package tatu.bowshield.screens;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.component.Button;
import tatu.bowshield.component.ButtonManager;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.control.OnPopupResult;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.popups.ConfirmDialog;
import tatu.bowshield.sprites.GameSprite;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class ResultScreen extends Screen implements OnPopupResult, IOnButtonTouch{

    private String         PATH_BACKGROUND = "gfx/telas/game_over.png";
    private Texture        mBackgroundTexture;
    private ITextureRegion mBackgroundRegion;
    

    private int   id;

    Button        btnPlayAgain;
    Button        btnBackToMenu;

    ButtonManager bManager;

    
    public ResultScreen(BowShieldGameActivity reference, int id) {
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

        btnPlayAgain = new Button(mReference, "gfx/buttons/create_normal.png", "gfx/buttons/create_pressed.png", 170, 288, 0);
        btnBackToMenu = new Button(mReference, "gfx/buttons/create_normal.png", "gfx/buttons/create_pressed.png", 420, 288, 1);

        bManager = new ButtonManager(mReference, this);
        bManager.addButton(btnPlayAgain);
        bManager.addButton(btnBackToMenu);
        
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

        bManager.updateButtons(pSceneTouchEvent);

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        //PopUp.showPopUp(new ConfirmDialog(mReference, 700, 350));
        
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void Draw() {
        // TODO Auto-generated method stub
        super.Draw();

        getScene().setBackground(
                new SpriteBackground(new Sprite(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT,
                        mBackgroundRegion, mReference.getVertexBufferObjectManager())));
        
        bManager.drawButtons();
        
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        super.Destroy();

        // getScene().setBackground(null);
        bManager.detach();
        mBackgroundTexture = null;
        mBackgroundRegion = null;
    }

    @Override
    public void onButtonTouch(int buttonId) {
        // TODO Auto-generated method stub
        
        switch (buttonId) {

            case 0: // play angain

                ScreenManager.changeScreen(Constants.SCREEN_GAME);

                break;

            case 1: // Menu
                ScreenManager.changeScreen(Constants.SCREEN_DEVICE);
                break;

        }
        
    }

    @Override
    public void onResultReceived(int result) {
        // TODO Auto-generated method stub
        
        switch (result) {
            case Constants.RESULT_YES:
                PopUp.forceClose();
                ScreenManager.changeScreen(1);
                break;

            case Constants.RESULT_NO:
                PopUp.hidePopUp();
                break;
        }
        
    }

}