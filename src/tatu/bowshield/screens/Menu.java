package tatu.bowshield.screens;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.bluetooth.BluetoothChatService;
import tatu.bowshield.component.Button;
import tatu.bowshield.component.ButtonManager;
import tatu.bowshield.component.ListView;
import tatu.bowshield.component.OnListItemClickListener;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.sprites.GameSprite;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

public class Menu extends Screen implements IOnButtonTouch,
		OnListItemClickListener {

	private String PATH_BUTTON = "gfx/buttonn.png";
	private String PATH_BUTTON_PRESSED = "gfx/buttonp.png";
	private String PATH_BUTTON_HELP = "gfx/btn_help.png";
	private String PATH_BUTTON_ABOUT = "gfx/btn_about.png";

	private String PATH_IMAGE_HELP = "gfx/telas/help_screen.png";
	private String PATH_IMAGE_ABOUT = "gfx/telas/about_screen.png";
	
	private final int BTN_CREATE = 0;
	private final int BTN_JOIN = 1;
	private final int BTN_SKIP = 2;
	private final int BTN_HELP = 3;
	private final int BTN_ABOUT = 4;
	

	private String PATH_BACKGROUND = "gfx/telas/deviceBack.jpg";
	private Texture mBackgroundTexture;
	private ITextureRegion mBackgroundRegion;

	Button btnCreateGame;
	Button btnJoinGame;
	Button btnSkip;
	
	Button btnAbout;
	Button btnHelp;

	ButtonManager bManager;
	ListView deviceList;

	private BluetoothAdapter mBtAdapter;
	private BluetoothChatService mChatService;

	private int devicesCount = 0;
	private Handler mHandler;

	private SimpleScreen helpScreen;
	private SimpleScreen AboutScreen;
	
	
	public Menu(int id) {
		super(id);
	}

	@Override
	public void Initialize() {
		// TODO Auto-generated method stub

		btnCreateGame = new Button(PATH_BUTTON,PATH_BUTTON_PRESSED, 0, 0, BTN_CREATE);
		btnJoinGame = new Button(PATH_BUTTON,PATH_BUTTON_PRESSED, 0, 100, BTN_JOIN);
		btnSkip = new Button(PATH_BUTTON,PATH_BUTTON_PRESSED, 0, 200, BTN_SKIP);
		btnHelp = new Button(PATH_BUTTON_HELP, PATH_BUTTON_HELP, 0, 380, BTN_HELP);
		btnAbout = new Button(PATH_BUTTON_ABOUT, PATH_BUTTON_ABOUT, 600, 380, BTN_ABOUT);
		
		
		
		bManager = new ButtonManager(this);
		bManager.addButton(btnCreateGame);
		bManager.addButton(btnJoinGame);
		bManager.addButton(btnSkip);
		bManager.addButton(btnHelp);
		bManager.addButton(btnAbout);
		
		helpScreen = new SimpleScreen(Constants.SIMPLE_SCREEN_HELP,PATH_IMAGE_HELP);
		AboutScreen = new SimpleScreen(Constants.SIMPLE_SCREEN_ABOUT,PATH_IMAGE_ABOUT);
		
		addSimpleScreen(helpScreen);
		addSimpleScreen(AboutScreen);
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		GameSprite.getGameReference().registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		GameSprite.getGameReference().registerReceiver(mReceiver, filter);

		GameSprite.getGameReference().runOnUiThread(new Runnable() {

			@Override
			public void run() {

				mHandler = new Handler() {
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case BluetoothChatService.STATE_CONNECTED:
							DebugLog.log("Connected");
							ScreenManager.changeScreen(2);
							break;

						case BluetoothChatService.STATE_CONNECTING:
							DebugLog.log("Connecting");
							break;

						case BluetoothChatService.STATE_LISTEN:
							DebugLog.log("Waiting Connection");
							break;

						case BluetoothChatService.STATE_NONE:
							DebugLog.log("Connection Lost");
							break;
						}
					};
				};

				mBtAdapter = BluetoothAdapter.getDefaultAdapter();

				mChatService = BluetoothChatService.getInstance(mBtAdapter);
				mChatService.setHandler(mHandler);
			}
		});

		mBackgroundTexture = getLoader().load(PATH_BACKGROUND);
		mBackgroundTexture.load();
		mBackgroundRegion = TextureRegionFactory
				.extractFromTexture(mBackgroundTexture);


		deviceList = new ListView(300, 0);
		deviceList.setOnListItemClickListener(this);

		super.Initialize();

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
		//helpScreen.Update();
	}
	
	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		bManager.updateButtons(pSceneTouchEvent);
		deviceList.updateItems(pSceneTouchEvent);
		
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}

	@Override
	public void Draw() {

		getScene().setBackground(
				new SpriteBackground(new Sprite(0, 0, Constants.CAMERA_WIDTH,
						Constants.CAMERA_HEIGHT, mBackgroundRegion, GameSprite
								.getGameReference()
								.getVertexBufferObjectManager())));
		
		bManager.drawButtons();
		deviceList.draw();
		
		super.Draw();
	}

	@Override
	public void Destroy() {
		GameSprite.getGameReference().unregisterReceiver(mReceiver);

		GameSprite.getGameReference().runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				bManager.detach();
				deviceList.detach();
			}

		});

		super.Destroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		ScreenManager.changeScreen(getId() - 1);
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onButtonTouch(int buttonId) {

		switch (buttonId) {

		case BTN_CREATE:
			makeDiscoverable();
			mChatService.start();
			GamePhysicalData.GAME_TYPE = GamePhysicalData.SERVER_TYPE;
			DebugLog.log("START CALLED!");
			break;

		case BTN_JOIN:
			deviceList.clear();
			ScreenManager.reDraw();
			scanDevices();
			GamePhysicalData.GAME_TYPE = GamePhysicalData.CLIENT_TYPE;
			DebugLog.log("Searching for devices... ");
			break;

		case BTN_SKIP:
			ScreenManager.changeScreen(2);
			break;
			
		case BTN_HELP:
			ScreenManager.showSimpleScreen(Constants.SIMPLE_SCREEN_HELP);
			break;
			
		case BTN_ABOUT:
			ScreenManager.showSimpleScreen(Constants.SIMPLE_SCREEN_ABOUT);
			break;

		}
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {

				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				deviceList.addItem(device.getName(), device.getAddress());
				ScreenManager.reDraw();

				DebugLog.log("Device found: " + device.getName());
			}

			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				DebugLog.log("Descover finished!");
			}
		}
	};

	private void scanDevices() {

		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		mBtAdapter.startDiscovery();
		Log.v("TESTE", "Discovery started!");
	}

	private void makeDiscoverable() {
		if (mBtAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			GameSprite.getGameReference().startActivity(discoverableIntent);
		}
	}

	@Override
	public void onItemClick(int position, Object container) {

		mBtAdapter.cancelDiscovery();

		String address = (String) container;

		BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
		mChatService.connect(device);

	}

}
