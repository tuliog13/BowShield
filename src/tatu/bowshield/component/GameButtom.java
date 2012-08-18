package tatu.bowshield.component;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.control.TextureLoader;
import tatu.bowshield.sprites.GameSprite;
import android.view.MotionEvent;

public class GameButtom {

    // variáveis criadas
    private TextureRegion mRegion;
    private TextureRegion mRegion2;

    private Texture       mTexture;
    private Texture       mTexture2;

    protected float       X;
    protected float       Y;

    protected Sprite      pSprite;
    protected Sprite      pSprite2;

    public GameButtom(final String filepath1, String filepath2, float X, float Y) {

        this.X = X;
        this.Y = Y;

        TextureLoader loader = GameSprite.getGameReference().getTextureLoader();

        mTexture = loader.load(filepath1);
        mTexture.load();

        mTexture2 = loader.load(filepath2);
        mTexture2.load();

        mRegion = TextureRegionFactory.extractFromTexture(mTexture);
        mRegion2 = TextureRegionFactory.extractFromTexture(mTexture2);

        pSprite = new Sprite(X, Y, mRegion, GameSprite.getGameReference().getVertexBufferObjectManager());
        GameSprite.getGameReference().getTextureManager().loadTexture(mTexture);

        pSprite2 = new Sprite(X, Y, mRegion2, GameSprite.getGameReference().getVertexBufferObjectManager());
        GameSprite.getGameReference().getTextureManager().loadTexture(mTexture2);
    }

    public void Update() {

    }

    public void Draw(float percw, float perch) {
        pSprite.setPosition(X, Y);
        GameSprite.getGameReference().getScene().attachChild(pSprite);
    }

    public void setPosition(float x, float y) {
        this.X = x;
        this.Y = y;
        pSprite.setPosition(x, y);
    }

    public void Draw() {

        pSprite.setPosition(X, Y);
        pSprite2.setPosition(X, Y);
        Scene scene = GameSprite.getGameReference().getScene();

        try {
            scene.attachChild(pSprite2);
            scene.attachChild(pSprite);
            pSprite2.setVisible(false);
        } catch (Exception e) {
        }

    }

    public void setTouchDown(boolean isTouch) {
        if (isTouch) {
            pSprite.setVisible(false);
            pSprite2.setVisible(true);
        } else {
            pSprite.setVisible(true);
            pSprite2.setVisible(false);
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

    public Sprite getSprite2() {
        return this.pSprite2;
    }
}