package tatu.bowshield.control;

import java.util.EventListener;

import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.bluetooth.BluetoothService;
import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.screens.Game;
import tatu.bowshield.sprites.GameSprite;
import tatu.bowshield.util.DebugLog;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class GamePhysicalData implements EventListener {

    private float                    mForce;
    private float                    mAngle;
    public float                     mDistance;
    public int                       mDirecao;

    private GameSprite               mTouch;

    public static final int          SERVER_TYPE = 0, CLIENT_TYPE = 1;
    public static OnDirectionChanged sListener;

    public static int                GAME_TYPE;

    public boolean                   sShoted;
    BowShieldGameActivity            mReference;

    public GamePhysicalData(BowShieldGameActivity reference) {
        mReference = reference;
        sShoted = false;
        mTouch = new GameSprite(reference, "gfx/touch.png", 0, 0);
        mTouch.getSprite().setVisible(false);
    }

    public GameSprite getmTouch() {
        return mTouch;
    }

    public void setmTouch(GameSprite mTouch) {
        this.mTouch = mTouch;
    }

    public float X = 0;
    public float Y = 0;

    public boolean calculateTouch(TouchEvent pSceneTouchEvent, Game gameReference, boolean send) {

        int myEventAction = pSceneTouchEvent.getAction();

        float currentX = pSceneTouchEvent.getX();
        float currentY = pSceneTouchEvent.getY();
        float angle = 0;

        switch (myEventAction) {

            case MotionEvent.ACTION_DOWN:
                X = pSceneTouchEvent.getX();
                Y = pSceneTouchEvent.getY();
                sShoted = false;
                PlayersController.getMyPlayer().getmArrow().getSprite().setVisible(false);
                break;

            case MotionEvent.ACTION_MOVE:

                mTouch.setPosition(X - mTouch.getSprite().getWidth() / 2, Y - mTouch.getSprite().getHeight() / 2);
                mTouch.getSprite().setVisible(true);
                PlayersController.getMyPlayer().flipHorizontal(mDirecao);
                PlayersController.getMyPlayer().setState(PlayersController.getMyPlayer().STATE_AIMING);

                mDistance = (float) Math.sqrt(Math.pow((X - currentX), 2) + Math.pow((Y - currentY), 2));
                float distance = X - currentX;

                if (distance > 0) {
                    if (mDirecao != 1) {
                        mDirecao = 1;
                        sListener.onDirectionChanged();
                    }
                    angle = ((currentY - Y) / (Math.abs(distance) / 2)) * 30;

                } else {
                    if (mDirecao != 2) {
                        mDirecao = 2;
                        sListener.onDirectionChanged();
                    }
                    angle = ((currentY - Y) / (Math.abs(distance) / 2)) * 30;

                }

                // if (send) {
                // gameReference.sendMessage(BluetoothChatService.AIMING_UPDATE,
                // currentX + "@" + currentY + "#");
                // }

                break;

            case MotionEvent.ACTION_UP:
                DebugLog.log("UP");
                mTouch.getSprite().setVisible(false);
                if (PlayersController.getMyPlayer().getState() == PlayersController.getMyPlayer().STATE_AIMING) {
                    mDistance = 0;
                    mForce = 0;
                    float ang;

                    if (mDirecao == 1) {
                        ang = mAngle;
                    } else {
                        ang = -mAngle;
                    }

                    if (send) {
                        gameReference.sendMessage(BluetoothService.SHOT, ang + "@" + mForce + "@"
                                + mDirecao + "#");
                    }
                    PlayersController.getMyPlayer().getmArrow().getSprite().setVisible(true);
                    sShoted = true;
                    PlayersController.getMyPlayer().setState(PlayersController.getMyPlayer().STATE_SHOTED);
                }
                break;
        }

        if (mForce <= 2.5f)// Limite de força
        {
            mForce = (mDistance / 100) / 1.5f;
        } else {
            if ((mDistance / 100) / 1.5f <= 2.5f) {
                mForce = (mDistance / 100) / 1.5f;
            }
        }

        if (angle <= 90 && angle >= -90) {
            mAngle = -angle;
        } else {
            if (angle > 90) {
                mAngle = -90;
            } else {
                mAngle = 90;
            }
        }

        // if (send && myEventAction == TouchEvent.ACTION_MOVE) {
        // gameReference.sendMessage(BluetoothChatService.SHOT, mAngle + "@"
        // + getProporcionalForce(mForce) + "@" + mDirecao + "#");
        // }

        return true;
    }

    public float getInicialX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getInicialY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public boolean issShoted() {
        return sShoted;
    }

    public void setsShoted(boolean newShoted) {
        sShoted = newShoted;
    }

    public static void setOnDirectionChangedListener(OnDirectionChanged listener) {
        sListener = listener;
    }

    public float getForca() {
        return mForce;
    }

    public float getDistance() {
        return mDistance;
    }

    public float getAngulo() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    public void setForce(float force) {
        mForce = force;
    }
}