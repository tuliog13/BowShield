package tatu.bowshield.control;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.screens.Screen;
import tatu.bowshield.sprites.GameSprite;

public class ScreenManager {

	private static List<Screen> _screens;
	private static Screen _currentScreen;

	public static void addScreen(Screen screen)
	{
		if(_screens == null)//Initialize
		{
			_screens = new ArrayList<Screen>();
			
		}
		
		_screens.add(screen);
	}
	
	public static void changeScreen(int idScreen)
	{

		if(_currentScreen != null)
		{
			_currentScreen.Destroy();
		}
		
		_currentScreen = getScreen(idScreen);
		_currentScreen.Initialize();
		_currentScreen.Load();
		_currentScreen.Draw();
	}
	
	public static void Update()
	{
		
		_currentScreen.Update();

	}
	public static void reDraw()
	{
		_currentScreen.Draw();
	}
	
	private static Screen getScreen(int id)
	{
		for(Screen screen : _screens)
		{
			if(screen.getId() == id)
			{
				return screen;
			}
		}
		return null;
	}
	
	public static Screen getCurrentScreen()
	{
		return _currentScreen;
	}

	public static void onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub

		_currentScreen.onSceneTouchEvent(pSceneTouchEvent);
	}
	
}
