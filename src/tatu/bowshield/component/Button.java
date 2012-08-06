package tatu.bowshield.component;

import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.sprites.GameSprite;

public class Button extends GameSprite {

	private int mId;
	
	public Button(String filepath, float X, float Y, int id) {
		super(filepath, X, Y);
		this.mId = id;
	}
	
	public void setImageResorce()
	{
		
	}
	
	public boolean onTouchEvent(TouchEvent pSceneTouchEvent) {
		return super.onTouchEvent(pSceneTouchEvent);
	}
	
	public int getId(){
		return mId;
	}
	
}
