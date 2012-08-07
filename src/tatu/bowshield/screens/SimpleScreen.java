package tatu.bowshield.screens;

import android.view.KeyEvent;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.sprites.GameSprite;

public class SimpleScreen extends Screen{

	private GameSprite backgroundImage;
	private int currentAlpha;
	private int id;
	
	public boolean isShowing() {
		return isShowing;
	}
	public void setShowing(boolean isShowing) {
		this.isShowing = isShowing;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	private boolean isShowing;
	
	public SimpleScreen(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public SimpleScreen(int id, String filepath)
	{
		super(id + 100);
		setId(id);
		
		currentAlpha = 250;
		isShowing = false;
		
		backgroundImage = new GameSprite(filepath, 0, 0);
		backgroundImage.getSprite().setAlpha(currentAlpha);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		ScreenManager.hideSimpleScreen(this.id);
		
		return super.onKeyDown(keyCode, event);
	}
	public void Show()
	{
		isShowing = true;
		GameSprite.getGameReference().getScene().attachChild(backgroundImage.getSprite());
	}
	
	public void Hide()
	{
		isShowing = false;
	}
	
	@Override
	public void Update() {
		// TODO Auto-generated method stub
		super.Update();
		
		if(isShowing)
		{
			if(currentAlpha > 10)
			{
				currentAlpha -= 9;
				backgroundImage.getSprite().setAlpha(currentAlpha);
			}
			else
			{
				backgroundImage.getSprite().setAlpha(currentAlpha);
				currentAlpha = 1; 
			}
		}
		else
		{
			if(currentAlpha < 250)
			{
				currentAlpha += 9;
				backgroundImage.getSprite().setAlpha(currentAlpha);
			}
			else
			{
				GameSprite.getGameReference().getScene().detachChild(backgroundImage.getSprite());
			}
		}
		
	}
}
