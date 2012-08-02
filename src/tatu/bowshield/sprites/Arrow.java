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

	public void Move(boolean canMove, float angulo, float força) {
		if (canMove) {

			novoX += velocidadeX;

			velocidadeX -= 0;

			novoY += velocidadeY;

			velocidadeY += 0.04;
			pSprite.setPosition(novoX, novoY);
		} else {
			configPreLaunch(angulo, força);
		}
	}
	
	public void configPreLaunch(float angulo, float força){
	    pSprite.setPosition(150, 140);
        float radians = (float) (angulo * Math.PI / 180);
        velocidadeX = (float) (Math.cos(radians) * 7 * força);
        velocidadeY = (float) (Math.sin(radians) * 7 * força);
        novoX = pSprite.getX();
        novoY = pSprite.getY();
	}

}