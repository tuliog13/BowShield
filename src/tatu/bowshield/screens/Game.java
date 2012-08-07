package tatu.bowshield.screens;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.bluetooth.BluetoothChatService;
import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.bluetooth.OnMessageReceivedListener;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.PlayersController;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.sprites.Arrow;
import tatu.bowshield.sprites.GameSprite;
import tatu.bowshield.sprites.Player;
import android.bluetooth.BluetoothAdapter;
import android.graphics.PointF;
import android.util.Log;
import android.view.KeyEvent;

public class Game extends Screen implements OnDirectionChanged,
		OnMessageReceivedListener {

	// Game
	public static String PATH_BACKGROUND = "gfx/bg.png";
	public static String PATH_PLAYER1 = "gfx/arq.png";
	public static String PATH_PLAYER2 = "gfx/pers.png";
	public static String PATH_ARC = "gfx/arco.png";
	public static String PATH_ARROW = "gfx/flecha1.png";
	public static String PATH_ROPE = "gfx/corda.png";

	private Texture mBackgroundTexture;
	private ITextureRegion mBackgroundRegion;


	public static Player mPlayerOne;
    private Player mPlayerTwo;

	private BluetoothChatService mChatService;



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
		
		if(GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE){
		
			mPlayerOne = new Player(PATH_PLAYER1, 0, 310);
			mPlayerTwo = new Player(PATH_PLAYER1, 1500, 310);
		
		}
		else
		{
			mPlayerOne = new Player(PATH_PLAYER1, -800, 310);
			mPlayerTwo = new Player(PATH_PLAYER1, 700, 310);
		}
		
		PlayersController.set_PlayerOne(mPlayerOne);
		PlayersController.set_PlayerTwo(mPlayerTwo);
		
		PlayersController.configureGamePlayers();
		
		getScene().setBackground(
				new SpriteBackground(new Sprite(0, 0, Constants.CAMERA_WIDTH,
						Constants.CAMERA_HEIGHT, mBackgroundRegion, GameSprite
								.getGameReference()
								.getVertexBufferObjectManager())));

		GamePhysicalData.setOnDirectionChangedListener(this);
		PlayersController.get_PlayerOne().getmArrow().setOnDirectionChangedListener(this);
		PlayersController.get_PlayerTwo().getmArrow().setOnDirectionChangedListener(this);
		
		GameSprite.getGameReference().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mChatService = BluetoothChatService
						.getInstance(BluetoothAdapter.getDefaultAdapter());
				mChatService.setOnMenssageReceivedListener(Game.this);
			}
		});

		DebugLog.log("Initialize called!");
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

		// PlayersController.Update();
		if (PlayersController.getMyPlayer() != null) {
			PlayersController.Update();
		} else {
			DebugLog.log("Player null update");
		}

	}

	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
			PlayersController.get_PlayerOne().getGameData().calculateTouch(pSceneTouchEvent, this);
			PlayersController.get_PlayerTwo().getGameData().calculateTouch(pSceneTouchEvent, this);
		return false;
	}

	@Override
	public void Draw() {
		// TODO Auto-generated method stub
		super.Draw();

		// PlayersController.Draw();
		if (PlayersController.getMyPlayer() != null) {
			PlayersController.Draw();
		} else {
			DebugLog.log("Player null draw");
		}
	}

	@Override
	public void Destroy() {
		// TODO Auto-generated method stub
		super.Destroy();

		GameSprite.getGameReference().runOnUpdateThread(new Runnable() {

			@Override
			public void run() {

				mPlayerOne.Destroy(getScene());
				// mPlayerTwo.Destroy(getScene());
			}

		});

		mBackgroundTexture = null;
		mBackgroundRegion = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		ScreenManager.changeScreen(getId() - 1);
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onDirectionChanged() {
		//mPlayerOne.flipHorizontal1(GamePhysicalData.mDirecao);
	}

	@Override
	public void onTurnDone() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String message) {
		DebugLog.log("Message sended: " + message);
		mChatService.write(message);
	}

	@Override
	public void onMessageReceived(String message) {
		DebugLog.log("Message received: " + message);

		float angle, force, direction;
		int i, j;

		for (i = 0; i < message.length() && message.charAt(i) != '#'; i++)
			;
		
		message = message.substring(0, i);
		DebugLog.log("Message: " + message);
		
		for (i = 0; i < message.length() && message.charAt(i) != '@'; i++)
			;

		String s = message.substring(0, i);
		DebugLog.log("Angle: " + s);
		angle = Float.parseFloat(s);

		j = i;
		i += 2;

		for (; i < message.length() && message.charAt(i) != '@'; i++)
			;

		s = message.substring(j + 1, i);
		DebugLog.log("Force: " + s);
		force = Float.parseFloat(s);

		s = message.substring(i + 1);
		DebugLog.log("Direction: " + s);
		direction = Integer.parseInt(s);
		
		if(GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE){
			PlayersController.get_PlayerTwo().getmArrow().configPreLaunch(angle, force);

			PlayersController.get_PlayerTwo().getGameData().sShoted = true;
			PlayersController.get_PlayerTwo().getGameData().mDistance = 0;
			PlayersController.get_PlayerTwo().getGameData().mDirecao = (int) direction;
			PlayersController.get_PlayerTwo().flipHorizontal((int) direction);

			PlayersController.get_PlayerTwo().getGameData().setAngle(angle);
			PlayersController.get_PlayerTwo().getGameData().setForce(force);
		}
		else
		{
			PlayersController.get_PlayerOne().getmArrow().configPreLaunch(angle, force);

			PlayersController.get_PlayerOne().getGameData().sShoted = true;
			PlayersController.get_PlayerOne().getGameData().mDistance = 0;
			PlayersController.get_PlayerOne().getGameData().mDirecao = (int) direction;
			PlayersController.get_PlayerOne().flipHorizontal((int) direction);

			PlayersController.get_PlayerOne().getGameData().setAngle(angle);
			PlayersController.get_PlayerOne().getGameData().setForce(force);
		}

	}

	@Override
	public void onArrowOutofScreen(int type) {
		// TODO Auto-generated method stub
		if(type == 1){
			//PlayersController.get_PlayerOne().getGameData().sShoted = false;
		}
		else
		{
			//PlayersController.get_PlayerTwo().getGameData().sShoted = false;
		}
	}
}