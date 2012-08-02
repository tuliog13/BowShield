package tatu.bowshield.sprites;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.TextureLoader;
import android.view.MotionEvent;

public class GameSprite {

    // variáveis criadas
    private TextureRegion         mRegion;
    private Texture               mTexture;

    protected float               X;
    protected float               Y;

    protected Sprite              pSprite;

    private BowShieldGameActivity mReference;

    public GameSprite(BowShieldGameActivity reference, final String filepath, float X, float Y) {

        this.X = X;
        this.Y = Y;
        mReference = reference;

        TextureLoader loader = reference.getTextureLoader();

        mTexture = loader.load(filepath);
        mTexture.load();

        mRegion = TextureRegionFactory.extractFromTexture(mTexture);

        pSprite = new Sprite(X, Y, mRegion, mReference.getVertexBufferObjectManager());
        reference.getTextureManager().loadTexture(mTexture);
    }

    public void Update() {

    }

    public void Draw(float percw, float perch) {
        pSprite.setPosition(X, Y);
        mReference.getScene().attachChild(pSprite);
    }

    public void Draw() {
        pSprite.setPosition(X, Y);
        mReference.getScene().attachChild(pSprite);
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

}