package tatu.bowshield.sprites;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.GamePhysicalData;

public class Rope extends GameSprite{

	public Rope(final String filepath,float X, float Y) {
		super(filepath,X,Y);
		
	}
	
	public void Move(float angulo, float for�a, int direcao) {
		if(direcao == 1)
		{
			this.pSprite.setRotation(angulo);
			this.pSprite.setWidth(-for�a);
		}
		else
		{
			this.pSprite.setRotation(-angulo);
			this.pSprite.setWidth(for�a);
		}
		this.pSprite.setRotationCenterX(0);
		this.pSprite.setRotationCenterY(0);
	}

}
