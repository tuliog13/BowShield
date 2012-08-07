package tatu.bowshield.component;

import org.andengine.input.touch.TouchEvent;

public abstract class PopUpLayout {
	
	protected int WIDTH;
	protected int HEIGHT;
	
	public abstract void onDraw();
	public abstract void Update();
	
	public abstract void TouchEvent(TouchEvent event);
	
	public abstract void Destroy();
}
