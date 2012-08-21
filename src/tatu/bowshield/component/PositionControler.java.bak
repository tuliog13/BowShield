package tatu.bowshield.component;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.control.TextureLoader;
import tatu.bowshield.sprites.GameSprite;

public class PositionControler {

    private TextureRegion    mRegion;
    private TextureRegion    mRegion2;

    private Texture          mTexture;
    private Texture          mTexture2;

    protected float          mX;
    protected float          mY;
    private float            mWidth;
    private float            mHeight;

    protected Sprite         pSprite;
    protected Sprite         pSprite2;

    public static final byte STATE_CENTER = 0;
    public static final byte STATE_RIGHT  = 1;
    public static final byte STATE_LEFT   = 2;

    private boolean          mLock;

    public PositionControler(final String filepath1, String filepath2, float X, float Y) {

        this.mX = X;
        this.mY = Y;

        TextureLoader loader = GameSprite.getGameReference().getTextureLoader();

        mTexture = loader.load(filepath1);
        mTexture.load();

        mTexture2 = loader.load(filepath2);
        mTexture2.load();

        mRegion = TextureRegionFactory.extractFromTexture(mTexture);
        mRegion2 = TextureRegionFactory.extractFromTexture(mTexture2);

        pSprite = new Sprite(X, Y, mRegion, GameSprite.getGameReference().getVertexBufferObjectManager());
        GameSprite.getGameReference().getTextureManager().loadTexture(mTexture);

        mWidth = pSprite.getWidth();
        mHeight = pSprite.getHeight();

        pSprite2 = new Sprite(X + 5, Y + 10, mRegion2, GameSprite.getGameReference().getVertexBufferObjectManager());
        GameSprite.getGameReference().getTextureManager().loadTexture(mTexture2);

        mLock = false;
    }

    public void Draw() {

        Scene scene = GameSprite.getGameReference().getScene();

        try {
            scene.attachChild(pSprite);
            scene.attachChild(pSprite2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean update(TouchEvent event) {

        float x = event.getX();
        float y = event.getY();

        if (x > mX && x < mX + mWidth && y > mY && y < mY + mHeight) {
            pSprite2.setPosition(x, mY);
            mLock = true;
        }

        if (event.getAction() == TouchEvent.ACTION_UP) {
            mLock = false;
        }

        return mLock;
    }

}
