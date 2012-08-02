package tatu.bowshield.sprites;

import java.util.EventListener;

import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.control.Constants;

public class Arrow extends GameSprite implements EventListener{

	private float velocidadeX;
	private float velocidadeY;

	private float novoX;
	private float novoY;

	private float inicialX;
	private float inicialY;
	
	public static OnDirectionChanged sListener;
	
	public Arrow(final String filepath,float X, float Y) {
		super(filepath,X,Y);

		inicialX = X;
		inicialY = Y;
		
	}

	float decrementoX;
	float decrementoY;

	public void Move(boolean canMove, float angulo, float força, int direcao) {
		if (canMove) {
			if(direcao == 1)
			{
				novoX += velocidadeX;
				novoY += velocidadeY;
	
				velocidadeY += Constants.VALUE_VELOCITY_Y;
				pSprite.setPosition(novoX, novoY);
			}
			else
			{
				novoX -= velocidadeX;
				novoY -= velocidadeY;
	
				velocidadeY -= Constants.VALUE_VELOCITY_Y;
				pSprite.setPosition(novoX, novoY);
			}
			
			if(novoX > 800 || novoX < 0 || novoY > 480 || novoY < 0)
			{
				sListener.onArrowOutofScreen();
				configPreLaunch(angulo, força);
				canMove = false;
			}
			
		} else {
			if(direcao == 1)
			configPreLaunch(angulo, força);
			else
			{
				configPreLaunch(-angulo, força);
			}
		}
	}
	public static void setOnDirectionChangedListener(OnDirectionChanged listener){
    	sListener = listener;
    }
	public void configPreLaunch(float angulo, float força){
	    pSprite.setPosition(inicialX, inicialY);
        float radians = (float) (angulo * Math.PI / 180);
        velocidadeX = (float) (Math.cos(radians) * 7 * força);
        velocidadeY = (float) (Math.sin(radians) * 7 * força);
        novoX = pSprite.getX();
        novoY = pSprite.getY();
	}

}