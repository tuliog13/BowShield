package tatu.bowshield.control;

import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.activity.BowShieldGameActivity;
import android.view.MotionEvent;

public abstract class GamePhysicalData {

    private static float  mForce;
    private static float  mAngle;
    public static float  mDistance;

    public static boolean sShoted;
    {
        sShoted = false;
    }

    static float          X = 0;
    static float          Y = 0;

    public static boolean calculateTouch(TouchEvent pSceneTouchEvent, BowShieldGameActivity gameReference) {

        int myEventAction = pSceneTouchEvent.getAction();

        float currentX = pSceneTouchEvent.getX();
        float currentY = pSceneTouchEvent.getY();
        float angle = 0;

        switch (myEventAction) {
            
            case MotionEvent.ACTION_DOWN:
                X = pSceneTouchEvent.getX();
                Y = pSceneTouchEvent.getY();
                sShoted = false;
                break;
                
            case MotionEvent.ACTION_MOVE:
                // sprite.setRotation((float) (X * 0.5));
                mDistance = (float) Math.sqrt(Math.pow((X - currentX), 2) + Math.pow((Y - currentY), 2));
                float distance = X - currentX;

                if (Math.abs(distance) / 2 >= 2)
                    angle = ((currentY - Y) / (Math.abs(distance) / 2)) * 30;
                else {
                    angle = (currentY - Y) * 30;
                }
                break;
                
            case MotionEvent.ACTION_UP:
                sShoted = true;
                mDistance = 0;
                gameReference.sendMessage(mAngle + "," + mForce);
                break;
        }

        mForce = mDistance / 100; // MUDAR

        if (angle <= 90 && angle >= -90) {
            mAngle = -angle;
        }

        return true;
    }

    public static float getForca() {
        return mForce;
    }

    public static float getDistance() {
        return mDistance;
    }

    public static float getAngulo() {
        return mAngle;
    }
    
    public static void setAngle(float angle){
        mAngle = angle;
    }

    public static void setForce(float force){
        mForce = force;
    }
}