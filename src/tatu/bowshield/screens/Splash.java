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

import android.view.MotionEvent;

public class Splash extends Screen{

	private String               PATH_BACKGROUND = "gfx/splash.png";
    private Texture              mBackgroundTexture;
    private ITextureRegion       mBackgroundRegion;
	
	
	public Splash(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void Initialize() {
		// TODO Auto-generated method stub
		super.Initialize();
		
		mBackgroundTexture = getLoader().load(PATH_BACKGROUND);
        mBackgroundTexture.load();
        mBackgroundRegion = TextureRegionFactory.extractFromTexture(mBackgroundTexture);

        getScene().setBackground(new SpriteBackground(new Sprite(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT, mBackgroundRegion,
                GameSprite.getGameReference().getVertexBufferObjectManager())));
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
	public void Draw() {
		// TODO Auto-generated method stub
		super.Draw();
	}
	
	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		super.Destroy();

		//getScene().setBackground(null);
		
		mBackgroundTexture = null;
        mBackgroundRegion = null;
	}
	
}
