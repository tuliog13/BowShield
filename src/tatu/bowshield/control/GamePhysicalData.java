package tatu.bowshield.control;

import java.util.EventListener;

import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.bluetooth.BluetoothChatService;
import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.screens.Game;
import tatu.bowshield.sprites.GameSprite;
import android.text.BoringLayout.Metrics;
import android.text.style.MetricAffectingSpan;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class GamePhysicalData implements EventListener {

	private float mForce;
	private float mAngle;
	public float mDistance;
	public int mDirecao;

	public static final int SERVER_TYPE = 0, CLIENT_TYPE = 1;
	public static OnDirectionChanged sListener;

	public static int GAME_TYPE;

	public boolean sShoted;

	public GamePhysicalData() {
		sShoted = false;
	}

	public static float X = 0;
	public static float Y = 0;

	public boolean calculateTouch(TouchEvent pSceneTouchEvent,
			Game gameReference, boolean send) {

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

			PlayersController.getMyPlayer().flipHorizontal1(mDirecao);

			mDistance = (float) Math.sqrt(Math.pow((X - currentX), 2)
					+ Math.pow((Y - currentY), 2));
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

//			if (send) {
//				gameReference.sendMessage(BluetoothChatService.AIMING_UPDATE,
//						 currentX + "@" + currentY + "#");
//			}
			
			break;

		case MotionEvent.ACTION_UP:
			DebugLog.log("UP");

			mDistance = 0;

			float ang;

			if (mDirecao == 1) {
				ang = mAngle;
			} else {
				ang = -mAngle;
			}
			
			if (send) {
				gameReference.sendMessage(BluetoothChatService.SHOT, ang + "@"
						+ getProporcionalForce(mForce) + "@" + mDirecao + "#");
			}

			sShoted = true;
			break;
		}

		mForce = (mDistance / 100); // MUDAr

		if (angle <= 90 && angle >= -90) {
			mAngle = -angle;
		} else {
			if (angle > 90) {
				mAngle = -90;
			} else {
				mAngle = 90;
			}
		}
		
//		if (send && myEventAction == TouchEvent.ACTION_MOVE) {
//			gameReference.sendMessage(BluetoothChatService.SHOT, mAngle + "@"
//					+ getProporcionalForce(mForce) + "@" + mDirecao + "#");
//		}

		return true;
	}

	public float getProporcionalForce(float force) {

		DisplayMetrics metrics = new DisplayMetrics();
		GameSprite.getGameReference().getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);

		float forceResult = force;

		if (metrics.widthPixels == 800) {
			forceResult -= 0.2f;
		}

		return force;
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