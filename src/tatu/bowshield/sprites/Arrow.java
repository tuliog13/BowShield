package tatu.bowshield.sprites;

import java.util.EventListener;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.GamePhysicalData;

public class Arrow extends GameSprite implements EventListener{

	private float velocidadeX;
	private float velocidadeY;

	private float novoX;
	private float novoY;

	private float inicialX;
	private float inicialY;
	
	private int type;
	
	public static OnDirectionChanged sListener;
	
	public Arrow(final String filepath,float X, float Y) {
		super(filepath,X,Y);

		inicialX = X;
		inicialY = Y;
		
		if(inicialX < 400)
		type = 1;
		else
			type = 2;
		
	}

	float decrementoX;
	float decrementoY;

	public void Move(boolean canMove, float angulo, float for�a, int direcao) {
		
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
				//sListener.onArrowOutofScreen(type);
				//configPreLaunch(angulo, for�a);
				//canMove = false;
			}
			
		} else {
			
			if(direcao == 1)
				configPreLaunch(angulo, for�a);
			else
			{
				configPreLaunch(-angulo, for�a);
			}
			
		}
	}
	public void setOnDirectionChangedListener(OnDirectionChanged listener){
    	sListener = listener;
    }
	
	public void configPreLaunch(float angulo, float for�a){
	    pSprite.setPosition(inicialX, inicialY);
        float radians = (float) (angulo * Math.PI / 180);
        velocidadeX = (float) (Math.cos(radians) * 7 * for�a);
        velocidadeY = (float) (Math.sin(radians) * 7 * for�a);
        novoX = pSprite.getX();
        novoY = pSprite.getY();
	}

}