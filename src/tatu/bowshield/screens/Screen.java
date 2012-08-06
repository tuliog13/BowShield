package tatu.bowshield.screens;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.TextureLoader;
import tatu.bowshield.sprites.GameSprite;

public abstract class Screen {
	
	private int _id;
	
	protected static TextureLoader _loader;
	protected static Scene _scene;
	
	public Screen(int id)
	{
		this._id = id;
	}
	
	public void Initialize()
	{
		
	}
	public void Load()
	{
		
	}
	public void Update()
	{
		
	}
	
	public void Draw()
	{
		
	}
	
	public void Destroy()
	{
		
	}
	
	public static void resetScene()
	{
		_scene = new Scene();
		_scene.setOnSceneTouchListener(GameSprite.getGameReference());
		_scene.registerUpdateHandler(GameSprite.getGameReference());
	}

	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int getId()
	{
		return this._id;
	}
	
	public static TextureLoader getLoader() {
		return _loader;
	}

	public static void setLoader(TextureLoader _loader) {
		Screen._loader = _loader;
	}

	public static Scene getScene() {
		return _scene;
	}

	public static void setScene(Scene _scene) {
		Screen._scene = _scene;
	}

	
}