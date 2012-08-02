package tatu.bowshield.sprites;

import tatu.bowshield.activity.BowShieldGameActivity;

public class Rope extends GameSprite{

	public Rope(BowShieldGameActivity reference, final String filepath,float X, float Y) {
		super(reference,filepath,X,Y);
		
	}
	
	public void Move(float angulo, float força) {
		this.pSprite.setRotation(angulo);
		this.pSprite.setWidth(-força);
		this.pSprite.setRotationCenterX(0);
		this.pSprite.setRotationCenterY(0);
	}

}
