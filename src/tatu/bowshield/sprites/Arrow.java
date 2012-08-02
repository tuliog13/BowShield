package tatu.bowshield.sprites;

import tatu.bowshield.activity.BowShieldGameActivity;

public class Arrow extends GameSprite{

	private float velocidadeX;
	private float velocidadeY;

	private float novoX;
	private float novoY;

	public Arrow(BowShieldGameActivity reference, final String filepath,float X, float Y) {
		super(reference,filepath,X,Y);
	}

	float decrementoX;
	float decrementoY;

	public void Move(boolean canMove, float angulo, float for�a) {
		if (canMove) {

			novoX += velocidadeX;

			velocidadeX -= 0;

			novoY += velocidadeY;

			velocidadeY += 0.04;
			pSprite.setPosition(novoX, novoY);
		} else {
			configPreLaunch(angulo, for�a);
		}
	}
	
	public void configPreLaunch(float angulo, float for�a){
	    pSprite.setPosition(150, 140);
        float radians = (float) (angulo * Math.PI / 180);
        velocidadeX = (float) (Math.cos(radians) * 7 * for�a);
        velocidadeY = (float) (Math.sin(radians) * 7 * for�a);
        novoX = pSprite.getX();
        novoY = pSprite.getY();
	}

}