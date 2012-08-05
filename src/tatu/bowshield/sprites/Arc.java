package tatu.bowshield.sprites;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.GamePhysicalData;
import android.view.MotionEvent;

public class Arc extends GameSprite{

	public Arc(final String filepath,float X, float Y) {
		super(filepath,X,Y);
	}

	public void Move(float angulo, float força,int direcao) {
		if(direcao == 1)
		{
			this.pSprite.setRotation(angulo);
		}
		else
		{
			this.pSprite.setRotation(-angulo);
		}
		
		this.pSprite.setRotationCenterX(pSprite.getWidth()/2);
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