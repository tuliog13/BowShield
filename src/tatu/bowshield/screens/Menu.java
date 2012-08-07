package tatu.bowshield.screens;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.activity.R;
import tatu.bowshield.bluetooth.BluetoothChatService;
import tatu.bowshield.component.Button;
import tatu.bowshield.component.ButtonManager;
import tatu.bowshield.component.ListView;
import tatu.bowshield.component.OnListItemClickListener;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.IOnBluetoothConnectListener;
import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.sprites.GameSprite;

public class Menu extends Screen implements IOnButtonTouch,
		OnListItemClickListener {

	private String PATH_BUTTON = "gfx/buttonn.png";
	private String PATH_BUTTON_PRESSED = "gfx/buttonp.png";

	private final int BTN_CREATE = 0;
	private final int BTN_JOIN = 1;

	private String PATH_BACKGROUND = "gfx/deviceBack.jpg";
	private Texture mBackgroundTexture;
	private ITextureRegion mBackgroundRegion;

	Button btnCreateGame;
	Button btnJoinGame;
	Button btnSkip;

	ButtonManager bManager;
	ListView deviceList;

	private BluetoothAdapter mBtAdapter;
	private BluetoothChatService mChatService;

	private int devicesCount = 0;
	private Handler mHandler;

	public Menu(int id) {
		super(id);
	}

	@Override
	public void Initialize() {
		// TODO Auto-generated method stub

		btnCreateGame = new Button(PATH_BUTTON,PATH_BUTTON_PRESSED, 0, 0, BTN_CREATE);
		btnJoinGame = new Button(PATH_BUTTON,PATH_BUTTON_PRESSED, 0, 100, BTN_JOIN);
		btnSkip = new Button(PATH_BUTTON,PATH_BUTTON_PRESSED, 0, 200, 2);

		bManager = new ButtonManager(this);
		bManager.addButton(btnCreateGame);
		bManager.addButton(btnJoinGame);
		bManager.addButton(btnSkip);

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
			ensureDiscoverable();
			mChatService.start();
			GamePhysicalData.GAME_TYPE = GamePhysicalData.SERVER_TYPE;
			DebugLog.log("START CALLED!");
			break;

		case BTN_JOIN:
			deviceList.clear();
			ScreenManager.reDraw();
			doDiscovery();
			GamePhysicalData.GAME_TYPE = GamePhysicalData.CLIENT_TYPE;
			break;

		default:
			ScreenManager.changeScreen(2);
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

	private void doDiscovery() {

		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		mBtAdapter.startDiscovery();
		Log.v("TESTE", "Discovery started!");
	}

	private void ensureDiscoverable() {
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
