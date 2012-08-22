package tatu.bowshield.component;

import java.util.EventListener;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.OnPopupResult;
import tatu.bowshield.control.TextureLoader;
import tatu.bowshield.sprites.GameSprite;
import android.view.KeyEvent;

public class PopUp implements EventListener {

    private static final String  PATH_POPUP_SCAND = "gfx/telas/scan.png";

    private static float         alpha;
    private static boolean       isShowing;
    private static PopUpLayout   mPopupLayout;
    private static Rectangle     rec;

    private static Rectangle     recCover;
    protected static Sprite      pSpriteCover;

    private static OnPopupResult sListener;

    private static boolean       popupLoadDone;
    public static boolean        popupUnloadDone;

    private static TextureRegion mRegion;
    private static Texture       mTexture;

    protected static Sprite      pSprite;

    private static int           currentWidth;
    private static int           currentHeigth;

    private static int           requiredWidth;
    private static int           requiredHeight;

    private static int           requiredX;
    private static int           requiredY;

    private static int           currentX;
    private static int           currentY;

    static BowShieldGameActivity mReference;

    public static void Inicialize(BowShieldGameActivity reference) {

        mReference = reference;
        alpha = 251;
        rec = new Rectangle(0, 0, 800, 480, mReference.getVertexBufferObjectManager());
        rec.setColor(0f, 0f, 0f);

        recCover = new Rectangle(0, 0, 800, 480, mReference.getVertexBufferObjectManager());
        recCover.setColor(0f, 0f, 0f);

        mReference.getScene().attachChild(rec);
        rec.setVisible(false);

        TextureLoader loader = mReference.getTextureLoader();

        mTexture = loader.load(PATH_POPUP_SCAND);
        mTexture.load();

        mRegion = TextureRegionFactory.extractFromTexture(mTexture);

        pSprite = new Sprite(0, 0, mRegion, mReference.getVertexBufferObjectManager());
        mReference.getTextureManager().loadTexture(mTexture);

        pSpriteCover = new Sprite(0, 0, mRegion, mReference.getVertexBufferObjectManager());
        mReference.getTextureManager().loadTexture(mTexture);

        mReference.getScene().attachChild(pSprite);
        pSprite.setVisible(false);
        pSprite.setWidth(0);
        pSprite.setHeight(0);

        popupLoadDone = false;
        popupUnloadDone = true;
    }

    public static void setListener(OnPopupResult listener) {
        sListener = listener;
    }

    public static boolean isShowing() {
        return isShowing;
    }

    public static void TouchEvent(TouchEvent event) {
        if (popupLoadDone) {
            mPopupLayout.TouchEvent(event);
        }
    }

    public static void sendResult(int result) {
        sListener.onResultReceived(result);
    }

    public static void setBackground(String path) {
        TextureLoader loader = mReference.getTextureLoader();

        mTexture = loader.load(path);
        mTexture.load();

    }

    public static void showPopUp(PopUpLayout popupLayout) {

        mPopupLayout = popupLayout;

        isShowing = true;
        rec.setVisible(true);

        currentHeigth = 0;
        currentWidth = 0;
        currentX = 400;
        currentY = 240;

        requiredX = (800 - popupLayout.WIDTH) / 2;
        requiredY = (480 - popupLayout.HEIGHT) / 2;

        pSprite.setPosition(currentX, currentY);

        requiredWidth = popupLayout.WIDTH;
        requiredHeight = popupLayout.HEIGHT;

        pSprite.setVisible(true);

    }

    public static void hidePopUp() {
        isShowing = false;
        mPopupLayout.Destroy();
        popupLoadDone = false;
        popupUnloadDone = false;
    }

    public static void forceClose() {
        isShowing = false;
        mPopupLayout.Destroy();
        popupLoadDone = false;
        popupUnloadDone = false;
        pSprite.setVisible(false);
        rec.setVisible(false);
        popupUnloadDone = true;
        recCover.detachSelf();
        pSpriteCover.detachSelf();
    }

    public static void UpdatePopUp() {
        if (isShowing) {
            mPopupLayout.Update();

            rec.setAlpha(alpha);
            if (alpha > 130) {
                alpha -= 5;
            } else // Popup completamente carregado ..
            {
                mPopupLayout.onDraw();
                popupLoadDone = true;
            }

            if (pSprite.getWidth() < requiredWidth) {
                pSprite.setWidth(currentWidth);
                currentWidth += 20;
            }
            if (pSprite.getHeight() < requiredHeight) {
                pSprite.setHeight(currentHeigth);
                currentHeigth += 20;
            }

            if (pSprite.getX() > requiredX) {
                pSprite.setX(currentX);
                currentX -= 10;
            }
            if (pSprite.getY() > requiredY) {
                pSprite.setY(currentY);
                currentY -= 10;
            }
        } else {
            rec.setAlpha(alpha);
            if (alpha < 251) {
                alpha += 5;
            } else {
                pSprite.setVisible(false);
                popupUnloadDone = true;
                recCover.detachSelf();
                pSpriteCover.detachSelf();
            }

            if (pSprite.getWidth() > 0) {
                pSprite.setWidth(currentWidth);
                currentWidth -= 20;
            }

            if (pSprite.getHeight() > 0) {
                pSprite.setHeight(currentHeigth);
                currentHeigth -= 20;
            }

            if (pSprite.getX() < 400) {
                pSprite.setX(currentX);
                currentX += 10;
            }
            if (pSprite.getY() < 240) {
                pSprite.setY(currentY);
                currentY += 10;
            }

        }
        recCover.setVisible(rec.isVisible());
        recCover.setAlpha(rec.getAlpha());

        pSpriteCover.setVisible(pSprite.isVisible());
        pSpriteCover.setX(pSprite.getX());
        pSpriteCover.setY(pSprite.getY());
        pSpriteCover.setWidth(pSprite.getWidth());
        pSpriteCover.setHeight(pSprite.getHeight());
    }

    public static void bringToFront() {
        // TODO Auto-generated method stub

        mReference.getScene().attachChild(recCover);
        mReference.getScene().attachChild(pSpriteCover);

    }

    public static void onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        mPopupLayout.onKeyDown(keyCode, event);
    }

}
