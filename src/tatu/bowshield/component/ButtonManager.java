package tatu.bowshield.component;

import java.util.ArrayList;
import java.util.List;

import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.sprites.GameSprite;

public class ButtonManager {

	List<Button> buttons;
	IOnButtonTouch listener;

	public ButtonManager(IOnButtonTouch listener) {
		this.listener = listener;
		this.buttons = new ArrayList<Button>();
	}

	public void addButton(Button button){
		buttons.add(button);
	}
	
	public void updateButtons(TouchEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		switch (event.getAction()) {
		case TouchEvent.ACTION_DOWN:
			break;

		case TouchEvent.ACTION_MOVE:
			break;
		case TouchEvent.ACTION_UP:
			
			for (Button button : buttons) {
				if((x >= button.getX() && x <= button.getX() + button.getWidth()) &&
						(y >= button.getY() && y <= button.getY() + button.getHeight())){
					listener.onButtonTouch(button.getId());
				}
			}
			
			break;
		}
	}
	
	public void drawButtons(){
		for (Button button : buttons) {
			button.Draw();
		}		
	}
	
	public void detach(){
		for (Button button : buttons) {
			GameSprite.getGameReference().getScene().detachChild(button.getSprite());
		}
	}
}
