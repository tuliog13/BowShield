package tatu.bowshield.sprites;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.PlayersController;
import tatu.bowshield.control.TextureLoader;
import android.view.MotionEvent;

public class AnimatedGameSprite {

    // variáveis criadas
    private TiledTextureRegion      mRegion;
    private Texture                 mTexture;

    protected float                 X;
    protected float                 Y;

    protected AnimatedSprite        pSprite;
    protected BowShieldGameActivity mReference;

    private long[]                  mFrameDurations;
    private int                     startIndex, endIndex;
    private boolean                 isLooping;
    private boolean                 isAnimating;

    private int                     mColumns, mRows;

    public AnimatedGameSprite(BowShieldGameActivity reference, final String filepath, float X, float Y, int columns,
            int rows) {

        this.X = X;
        this.Y = Y;

        mReference = reference;
        TextureLoader loader = mReference.getTextureLoader();

        // this is in the case of indio animation, otherwise, use setAnimationSettings method
        mFrameDurations = new long[] { 100, 100, 100, 100, 100, 100, 100 };
        startIndex = 1;
        endIndex = 7;
        isLooping = true;

        mColumns = columns;
        mRows = rows;
        mTexture = loader.load(filepath);
        mTexture.load();
        mRegion = TextureRegionFactory.extractTiledFromTexture(mTexture, mColumns, mRows);

        // mRegion = TextureRegionFactory.extractFromTexture(mTexture);
        pSprite = new AnimatedSprite(X, Y, mRegion, mReference.getVertexBufferObjectManager());
        mReference.getTextureManager().loadTexture(mTexture);
        isAnimating = false;

    }

    public void Update() {

    }

    public void setAnimationSettings(long[] frameDuration, int start, int end, boolean loop) {
        mFrameDurations = frameDuration;
        startIndex = start;
        endIndex = end;
        isLooping = loop;
    }

    public void animate() {
        if (!isAnimating) {
            pSprite.animate(mFrameDurations, startIndex, endIndex, true);
            isAnimating = true;
        }
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public void stopAnimation(int frame) {
        pSprite.stopAnimation(frame);
        isAnimating = false;
    }

    public void Draw(float percw, float perch) {
        pSprite.setPosition(X, Y);
        mReference.getScene().attachChild(pSprite);
    }

    public void setPosition(float x, float y) {
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

    public void flipHorizontal(int direction) {
        if (direction == 2)
            pSprite.setFlippedHorizontal(true);
        else {
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
