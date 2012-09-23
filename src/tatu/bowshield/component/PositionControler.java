package tatu.bowshield.component;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.TextureLoader;
import tatu.bowshield.sprites.GameSprite;

public class PositionControler {

    private TextureRegion    mRegion;
    private TextureRegion    mRegion2;

    private Texture          mTexture;
    private Texture          mTexture2;

    protected float          mBackgroundX;
    protected float          mBackgroundY;

    protected float          mControllerX;
    protected float          mControllerY;

    private float            mWidth;
    private float            mHeight;
    private float            mCenterX;
    private float            mCenterTotal;

    protected Sprite         mBackgroundSprite;
    protected Sprite         mControllerSprite;

    private float            mLEFT, mRIGHT, mCENTER;

    public static final byte STATE_CENTER = 0;
    public static final byte STATE_RIGHT  = 1;
    public static final byte STATE_LEFT   = 2;
    private byte             STATE;

    private boolean          mLock;
    BowShieldGameActivity    mReference;

    public PositionControler(BowShieldGameActivity reference, final String filepath1, String filepath2, float X, float Y) {

        mReference = reference;
        TextureLoader loader = mReference.getTextureLoader();

        mTexture = loader.load(filepath1);
        mTexture.load();

        mTexture2 = loader.load(filepath2);
        mTexture2.load();

        mRegion = TextureRegionFactory.extractFromTexture(mTexture);
        mRegion2 = TextureRegionFactory.extractFromTexture(mTexture2);

        mBackgroundSprite = new Sprite(X, Y, mRegion, mReference.getVertexBufferObjectManager());
        mReference.getTextureManager().loadTexture(mTexture);

        mControllerSprite = new Sprite(X + 5, Y + 5, mRegion2, mReference.getVertexBufferObjectManager());
        mReference.getTextureManager().loadTexture(mTexture2);

        mWidth = mBackgroundSprite.getWidth();
        mHeight = mBackgroundSprite.getHeight();
        mBackgroundX = mBackgroundSprite.getX();
        mBackgroundY = mBackgroundSprite.getY();
        mControllerY = mControllerSprite.getY();

        mLEFT = mBackgroundX;
        mRIGHT = mBackgroundX + ((mBackgroundSprite.getWidth() / 3) * 2);
        mCENTER = mBackgroundX + (mWidth / 2);

        mCenterX = mControllerSprite.getX() + (mControllerSprite.getHeight() / 2);
        mCenterTotal = mCENTER;

        mControllerSprite.setPosition(mCenterTotal - (mControllerSprite.getWidth() / 2), mControllerY);
        mControllerX = mControllerSprite.getX();

        mLock = false;
        STATE = STATE_CENTER;
    }

    public void Draw() {

        Scene scene = mReference.getScene();

        try {
            scene.attachChild(mBackgroundSprite);
            scene.attachChild(mControllerSprite);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void update() {
        if (!mLock) {
            mCenterX = mControllerX + (mControllerSprite.getWidth() / 2);

            if (mCenterX > mCenterTotal) {
                mControllerX -= 3;
                mControllerSprite.setPosition(mControllerX, mControllerY);
            } else if (mCenterX < mCenterTotal) {
                mControllerX += 3;
                mControllerSprite.setPosition(mControllerX, mControllerY);
            }
        }
    }

    public boolean update(TouchEvent event) {

        float x = event.getX() - (mControllerSprite.getWidth() / 2);
        float y = event.getY();
        int toleranceY = 50;
        int toleranceX = 5;

        boolean collisionX = x > (mBackgroundX - toleranceX)
                && (x + mControllerSprite.getWidth()) < mBackgroundX + mWidth + toleranceX;
        boolean collisionY = y > (mBackgroundY - toleranceY) && y < (mBackgroundY + mHeight + toleranceY);

        if ((collisionX && collisionY) || mLock) {

            mControllerX = x;

            if (collisionX) {
                mControllerSprite.setPosition(mControllerX, mControllerY);
            }

            float centerX = mControllerX + (mControllerSprite.getWidth() / 2);

            if (centerX > mCenterTotal) {
                STATE = STATE_RIGHT;
            } else if (centerX < mCenterTotal) {
                STATE = STATE_LEFT;
            }

            mLock = true;

            if (event.getAction() == TouchEvent.ACTION_UP) {
                mLock = false;
                mControllerX = mCENTER;
                mControllerSprite.setPosition(mControllerX, mControllerY);
                STATE = STATE_CENTER;
            }

            return true;
        }

        return false;
    }

    public void setState(byte state) {
        STATE = state;
        switch (STATE) {
            case STATE_RIGHT:
                mControllerX = mRIGHT;
                mLock = true;
                break;

            case STATE_LEFT:
                mControllerX = mLEFT;
                mLock = true;
                break;

            case STATE_CENTER:
                mControllerX = mCENTER;
                break;
        }
        mControllerSprite.setPosition(mControllerX, mControllerY);
    }

    public byte getState() {
        return STATE;
    }

    public void Destroy() {
        mReference.getScene().detachChild(mBackgroundSprite);
        mReference.getScene().detachChild(mControllerSprite);
    }

}
