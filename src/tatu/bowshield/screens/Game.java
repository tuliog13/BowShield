package tatu.bowshield.screens;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.bluetooth.BluetoothChatService;
import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.bluetooth.OnMessageReceivedListener;
import tatu.bowshield.component.Button;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.component.PositionControler;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.FruitController;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.OpponentView;
import tatu.bowshield.control.PlayersController;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.sprites.Arrow;
import tatu.bowshield.sprites.GameSprite;
import tatu.bowshield.sprites.Player;
import android.bluetooth.BluetoothAdapter;
import android.graphics.PointF;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Game extends Screen implements OnDirectionChanged, OnMessageReceivedListener {

    // Game
    public static String         PATH_BACKGROUND = "gfx/telas/gameBg.png";
    public static String         PATH_PLAYER1    = "gfx/arq.png";
    public static String         PATH_PLAYER2    = "gfx/pers.png";
    public static String         PATH_ARC        = "gfx/arco.png";
    public static String         PATH_ARROW      = "gfx/flecha1.png";
    public static String         PATH_ROPE       = "gfx/corda.png";

    public static String         PATH_CONTROLLER = "gfx/control1.png";
    public static String         PATH_CONTROL    = "gfx/control2.png";

    private Texture              mBackgroundTexture;
    private ITextureRegion       mBackgroundRegion;

    public static Player         mPlayerOne;
    private Player               mPlayerTwo;

    private BluetoothChatService mChatService;

    private Results              resultsScreen;
    private float                iX, iY;
    private PositionControler    mPositionController;

    private GamePhysicalData     myPlayerData;
    private GamePhysicalData     opponentPlayerData;

    private Text                 mPointText;
    private Font                 mTextFont;

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
        mBackgroundRegion = TextureRegionFactory.extractFromTexture(mBackgroundTexture);

        myPlayerData = new GamePhysicalData();
        opponentPlayerData = new GamePhysicalData();

        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {

            mPlayerOne = new Player(PATH_PLAYER1, 60, 330, myPlayerData);
           // mPlayerOne = new Player(PATH_PLAYER1, 330, 230, myPlayerData);
            mPlayerTwo = new Player(PATH_PLAYER1, 1500, 330, opponentPlayerData);

        } else {
            mPlayerOne = new Player(PATH_PLAYER1, -740, 330, opponentPlayerData);
            mPlayerTwo = new Player(PATH_PLAYER1, 700, 330, myPlayerData);
        }

        resultsScreen = new Results(0, "gfx/telas/game_over.png");

        addSimpleScreen(resultsScreen);

        PlayersController.set_PlayerOne(mPlayerOne);
        PlayersController.set_PlayerTwo(mPlayerTwo);

        PlayersController.configureGamePlayers();

        GamePhysicalData.setOnDirectionChangedListener(this);
        PlayersController.get_PlayerOne().getmArrow().setOnDirectionChangedListener(this);
        PlayersController.get_PlayerTwo().getmArrow().setOnDirectionChangedListener(this);

        GameSprite.getGameReference().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mChatService = BluetoothChatService.getInstance(BluetoothAdapter.getDefaultAdapter());
                mChatService.setOnMenssageReceivedListener(Game.this);
            }
        });

        DebugLog.log("Initialize called!");

        FruitController.Initialize(5, PlayersController.getOpponentPlayer());
        mPositionController = new PositionControler(PATH_CONTROLLER, PATH_CONTROL, 400, 400);
        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {
            OpponentView.Initialize((int) 1, 2);
        } else {
            OpponentView.Initialize((int) (800 - OpponentView.WIDTH) - 5, 2);
        }

        // font

        BitmapTextureAtlas mTextFontTextureAtlas = new BitmapTextureAtlas(GameSprite.getGameReference()
                .getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mTextFont = FontFactory.create(GameSprite.getGameReference().getFontManager(), mTextFontTextureAtlas, 32);

        GameSprite.getGameReference().getEngine().getFontManager().loadFont(mTextFont);

        GameSprite.getGameReference().getEngine().getTextureManager().loadTexture(mTextFontTextureAtlas);

        mPointText = new Text(200, 0, mTextFont, "Indio Mira " + PlayersController.getMyPlayer().getmCount() + " - "
                + PlayersController.getMyPlayer().getmCount() + " Indio Opponent", GameSprite.getGameReference()
                .getVertexBufferObjectManager());

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
            FruitController.Update();
            OpponentView.Update();
        } else {
            DebugLog.log("Player null update");
        }
        mPointText.setText("Indio Mira " + PlayersController.getMyPlayer().getmCount() + " - "
                + PlayersController.getOpponentPlayer().getmCount() + " Indio Opponent");
    }

    public float X = 0;
    public float Y = 0;

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {

        if (!mPositionController.update(pSceneTouchEvent)) {

            // if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {
            // PlayersController.get_PlayerOne().getGameData()
            // .calculateTouch(pSceneTouchEvent, this, true);
            // PlayersController.get_PlayerTwo().getGameData()
            // .calculateTouch(pSceneTouchEvent, this, false);
            // } else {
            // PlayersController.get_PlayerOne().getGameData()
            // .calculateTouch(pSceneTouchEvent, this, false);
            // PlayersController.get_PlayerTwo().getGameData()
            // .calculateTouch(pSceneTouchEvent, this, true);
            // }
            myPlayerData.calculateTouch(pSceneTouchEvent, this, true);
        }

        if (pSceneTouchEvent.getX() > 780 && pSceneTouchEvent.getY() < 30) {
            ScreenManager.showSimpleScreen(0);
        }

        int myEventAction = pSceneTouchEvent.getAction();

        float currentX = pSceneTouchEvent.getX();
        float currentY = pSceneTouchEvent.getY();

        switch (myEventAction) {

            case MotionEvent.ACTION_DOWN:

                X = OpponentView.getPositionX() - pSceneTouchEvent.getX();
                Y = OpponentView.getPositionY() - pSceneTouchEvent.getY();

                break;

            case MotionEvent.ACTION_MOVE:

                if (currentX > OpponentView.getPositionX()
                        && currentX < OpponentView.getPositionX() + OpponentView.WIDTH
                        && currentY > OpponentView.getPositionY()
                        && currentY < OpponentView.getPositionY() + OpponentView.HEIGTH) {

                    if ((X + currentX) >= 0 && (X + currentX) <= 800 - OpponentView.WIDTH) {
                        // OpponentView.MoveX(X + currentX);
                    }

                    if ((Y + currentY) >= 0 && (Y + currentY) <= 480 - OpponentView.HEIGTH) {
                        // OpponentView.MoveY(Y + currentY);
                    }

                }

                break;
        }

        return false;
    }

    @Override
    public void Draw() {
        // TODO Auto-generated method stub
        super.Draw();

        getScene().setBackground(
                new SpriteBackground(new Sprite(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT,
                        mBackgroundRegion, GameSprite.getGameReference().getVertexBufferObjectManager())));

        // PlayersController.Draw();
        if (PlayersController.getMyPlayer() != null) {
            PlayersController.Draw();
            getScene().attachChild(mPointText);
        } else {
            DebugLog.log("Player null draw");
        }

        mPositionController.Draw();

        FruitController.Draw();
        OpponentView.Draw();
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        super.Destroy();

        GameSprite.getGameReference().runOnUpdateThread(new Runnable() {

            @Override
            public void run() {

                PlayersController.Destroy();
                FruitController.Destroy();
                OpponentView.Destroy();
                mPointText.detachSelf();
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
        // mPlayerOne.flipHorizontal1(GamePhysicalData.mDirecao);
    }

    @Override
    public void onTurnDone() {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMessage(byte type, String message) {
        mChatService.write(type, message);
    }

    @Override
    public void onMessageReceived(byte type, String message) {

        switch (type) {
            case BluetoothChatService.SHOT:
                doShotAction(message);
                break;

            case BluetoothChatService.AIMING_UPDATE:
                DebugLog.log("Move received: " + message);
                break;
        }

    }

    private void doShotAction(String shotInfo) {

        float angle, force, direction;
        int i, j;

        for (i = 0; i < shotInfo.length() && shotInfo.charAt(i) != '#'; i++)
            ;

        shotInfo = shotInfo.substring(0, i);
        DebugLog.log("Message: " + shotInfo);

        for (i = 0; i < shotInfo.length() && shotInfo.charAt(i) != '@'; i++)
            ;

        String s = shotInfo.substring(0, i);
        DebugLog.log("Angle: " + s);
        angle = Float.parseFloat(s);

        j = i;
        i += 2;

        for (; i < shotInfo.length() && shotInfo.charAt(i) != '@'; i++)
            ;

        s = shotInfo.substring(j + 1, i);
        DebugLog.log("Force: " + s);
        force = Float.parseFloat(s);

        s = shotInfo.substring(i + 1);
        DebugLog.log("Direction: " + s);
        direction = Integer.parseInt(s);

        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {
            PlayersController.get_PlayerTwo().getmArrow().configPreLaunch(angle, force);

            PlayersController.get_PlayerTwo().getGameData().sShoted = true;
            PlayersController.get_PlayerTwo().getGameData().mDistance = 0;
            PlayersController.get_PlayerTwo().getGameData().mDirecao = (int) direction;
            PlayersController.get_PlayerTwo().flipHorizontal((int) direction);

            PlayersController.get_PlayerTwo().getGameData().setAngle(angle);
            PlayersController.get_PlayerTwo().getGameData().setForce(force);
        } else {
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
        if (type == 1) {
            // PlayersController.get_PlayerOne().getGameData().sShoted = false;
        } else {
            // PlayersController.get_PlayerTwo().getGameData().sShoted = false;
        }
    }
}