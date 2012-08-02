package tatu.bowshield.sprites;

import tatu.bowshield.activity.BowShieldGameActivity;
import android.view.MotionEvent;

public class Arc extends GameSprite{

	public Arc(BowShieldGameActivity reference, final String filepath,float X, float Y) {
		super(reference,filepath,X,Y);
	}

	public void Move(float angulo, float força) {
		this.pSprite.setRotation(angulo);
		this.pSprite.setRotationCenterX(pSprite.getWidth());
	}

	public boolean onTouchEvent(MotionEvent event) {

		int myEventAction = event.getAction();

		float X = event.getX();

		switch (myEventAction) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			pSprite.setRotation((float) (X * 0.5));
			break;
		case MotionEvent.ACTION_UP:

			break;
		}

		return true;
	}
}