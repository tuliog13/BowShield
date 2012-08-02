package tatu.bowshield.sprites;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.TextureLoader;
import android.view.MotionEvent;

public class GameSprite {

	// variáveis criadas
	private TextureRegion mRegion;
	private Texture mTexture;

	protected float X;
	protected float Y;

	protected Sprite pSprite;

	private static BowShieldGameActivity mReference;

	public static BowShieldGameActivity getGameReference() {
		return mReference;
	}

	public static void setGameReference(BowShieldGameActivity mReference) {
		GameSprite.mReference = mReference;
	}

	public GameSprite(final String filepath, float X, float Y) {

		this.X = X;
		this.Y = Y;

		TextureLoader loader = mReference.getTextureLoader();

		mTexture = loader.load(filepath);
		mTexture.load();

		mRegion = TextureRegionFactory.extractFromTexture(mTexture);

		pSprite = new Sprite(X, Y, mRegion,
				mReference.getVertexBufferObjectManager());
		mReference.getTextureManager().loadTexture(mTexture);
	}

	public void Update() {

	}

	public void Draw(float percw, float perch) {
		pSprite.setPosition(X, Y);
		mReference.getScene().attachChild(pSprite);
	}
	
	public void setPosition(float x, float y)
	{
		this.X = x;
		this.Y = y;
		pSprite.setPosition(x, y);
	}
	
	public void Draw() {
		
		pSprite.setPosition(X, Y);
		Scene scene = mReference.getScene();

		try {
			scene.attachChild(pSprite);
		} catch (Exception e) {
		}
		
	}

	public void flipHorizontal(int direction)
	{
		if(direction == 2)
		pSprite.setFlippedHorizontal(true);
		else
		{
			pSprite.setFlippedHorizontal(false);
		}
	}
	
	public boolean onTouchEvent(TouchEvent pSceneTouchEvent) {

		int myEventAction = pSceneTouchEvent.getAction();

		switch (myEventAction) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			break;
		}

		return true;
	}

	public float getX() {
		return this.X;
	}

	public float getY() {
		return this.Y;
	}

	public int getWidth() {
		return mTexture.getWidth();
	}

	public int getHeight() {
		return mTexture.getHeight();
	}

	public Sprite getSprite() {
		return this.pSprite;
	}
}