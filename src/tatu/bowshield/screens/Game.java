package tatu.bowshield.screens;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.PlayersController;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.sprites.Arc;
import tatu.bowshield.sprites.Arrow;
import tatu.bowshield.sprites.GameSprite;
import tatu.bowshield.sprites.BowMan;
import tatu.bowshield.sprites.Player;
import tatu.bowshield.sprites.Rope;

import android.view.MotionEvent;

public class Game extends Screen implements OnDirectionChanged {

	// Game
	public static String PATH_BACKGROUND = "gfx/bg.png";
	public static String PATH_PLAYER1 = "gfx/arq.png";
	public static String PATH_PLAYER2 = "gfx/pers.png";
	public static String PATH_ARC = "gfx/arco.png";
	public static String PATH_ARROW = "gfx/flecha1.png";
	public static String PATH_ROPE = "gfx/corda.png";

	private Texture mBackgroundTexture;
	private ITextureRegion mBackgroundRegion;
	
	private Player mPlayerOne;
	private Player mPlayerTwo;

	public Game(int id) { 
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Initialize() {
		// TODO Auto-generated method stub
		super.Initialize();

		mBackgroundTexture = getLoader().load(PATH_BACKGROUND);
		mBackgroundTexture.load();
		mBackgroundRegion = TextureRegionFactory
				.extractFromTexture(mBackgroundTexture);

		mPlayerOne = new Player(PATH_PLAYER1, 250, 200);
		mPlayerTwo = new Player(PATH_PLAYER1, 700, 300);
		
		getScene().setBackground(
				new SpriteBackground(new Sprite(0, 0,
						Constants.CAMERA_WIDTH,
						Constants.CAMERA_HEIGHT, mBackgroundRegion,
						GameSprite.getGameReference()
								.getVertexBufferObjectManager())));
		
		GamePhysicalData.setOnDirectionChangedListener(this);
		Arrow.setOnDirectionChangedListener(this);
		GamePhysicalData.mDirecao = 1;
		
		PlayersController.set_PlayerOne(mPlayerOne);
		PlayersController.set_PlayerTwo(mPlayerTwo);
		
		PlayersController.set_currentPlayer(mPlayerOne);
		
		PlayersController.getWatingPlayer().flipHorizontal1(2);
		
	}

	@Override
	public void Load() {
		// TODO Auto-generated method stub
		super.Load();
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		super.Update();

		PlayersController.Update();

	}

	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {

		GamePhysicalData.calculateTouch(pSceneTouchEvent,
				GameSprite.getGameReference());

		return false;
	}
	
	@Override
	public void Draw() {
		// TODO Auto-generated method stub
		super.Draw();
		
		PlayersController.Draw();
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		super.Destroy();

		GameSprite.getGameReference().runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				
				mPlayerOne.Destroy(getScene());
				mPlayerTwo.Destroy(getScene());
			}
			
		});
		
		mBackgroundTexture = null;
		mBackgroundRegion = null;
	}
	
	@Override
	public void onDirectionChanged() {
		PlayersController.get_currentPlayer().flipHorizontal1(GamePhysicalData.mDirecao);
	}

	@Override
	public void onTurnDone() {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void onArrowOutofScreen() {
		// TODO Auto-generated method stub
		PlayersController.changeTurn();
		GamePhysicalData.sShoted = false;
	}
}