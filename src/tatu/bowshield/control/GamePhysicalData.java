package tatu.bowshield.control;

import java.util.EventListener;

import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.bluetooth.OnDirectionChanged;
import android.view.MotionEvent;

public abstract class GamePhysicalData implements EventListener{

    private static float  mForce;
    private static float  mAngle;
    public static float  mDistance;
    public static int  mDirecao;
    
    public static OnDirectionChanged sListener;
    
    public static boolean sShoted;
    
    {
        sShoted = false;
    }

    public static float          X = 0;
    public static float          Y = 0;

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
            	
            	PlayersController.get_currentPlayer().flipHorizontal1(mDirecao);
            	
                // sprite.setRotation((float) (X * 0.5));
                mDistance = (float) Math.sqrt(Math.pow((X - currentX), 2) + Math.pow((Y - currentY), 2));
                float distance = X - currentX;
                
                if(distance > 0)
                {
                	if(mDirecao != 1)
                	{
                		mDirecao = 1;
                		sListener.onDirectionChanged();
                	}
	                angle = ((currentY - Y) / (Math.abs(distance)/ 2)) * 30;

                }
                else
                {
                	if(mDirecao != 2)
                	{
                		mDirecao = 2;
                		sListener.onDirectionChanged();
                	}
	                angle = ((currentY - Y) / (Math.abs(distance)/ 2)) * 30;

                }
                break;
                
            case MotionEvent.ACTION_UP:
            	DebugLog.log("enter");
                sShoted = true;
                mDistance = 0;
                gameReference.sendMessage(mAngle + "," + mForce);
                break;
        }

        mForce = mDistance / 100; // MUDAR

        if (angle <= 90 && angle >= -90) {
            mAngle = -angle;
        }
        else
        {
	       	if(angle > 90)
	      	{
	       		mAngle = -90;
	        }
	        else
	        {
	        	mAngle = 90;
	        }
        }

        return true;
    }
    
    public static void setOnDirectionChangedListener(OnDirectionChanged listener){
    	sListener = listener;
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