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

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.bluetooth.BluetoothService;
import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.bluetooth.OnMessageReceivedListener;
import tatu.bowshield.component.PositionControler;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.FruitController;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.OpponentView;
import tatu.bowshield.control.PlayersController;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.sprites.Player;
import tatu.bowshield.util.DebugLog;
import android.bluetooth.BluetoothAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Game extends Screen implements OnDirectionChanged, OnMessageReceivedListener {

    // Game
    public static String      PATH_BACKGROUND  = "gfx/telas/gameBg.png";
    public static String      PATH_BACKGROUND1 = "gfx/telas/gameBg1.png";
    public static String      PATH_PLAYER1     = "gfx/New_tirinha.png";
    public static String      PATH_PLAYER2     = "gfx/New_tirinha.png";
    public static String      PATH_ARC         = "gfx/red_arco.png";
    public static String      PATH_ARROW       = "gfx/flecha1.png";
    public static String      PATH_ROPE        = "gfx/force bg.png";
    public static String      PATH_FORCE_BAR   = "gfx/force bar.png";

    public static String      PATH_CONTROLLER  = "gfx/control1.png";
    public static String      PATH_CONTROL     = "gfx/control2.png";

    private Texture           mBackgroundTexture;
    private ITextureRegion    mBackgroundRegion;

    public static Player      mPlayerOne;
    private Player            mPlayerTwo;

    private BluetoothService  mChatService;

    private Results           resultsScreen;
    private float             iX, iY;
    private PositionControler mPositionController;

    private GamePhysicalData  myPlayerData;
    private GamePhysicalData  opponentPlayerData;

    private Text              mPointText;
    private Font              mTextFont;
    private boolean           mCanMovePlayer   = true;
    private boolean           isMovingPlayer   = false;

    public Game(BowShieldGameActivity reference, int id) {
        super(reference, id);
    }

    @Override
    public void Initialize() {
        // TODO Auto-generated method stub
        super.Initialize();

        myPlayerData = new GamePhysicalData(mReference);
        opponentPlayerData = new GamePhysicalData(mReference);

        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {

            mBackgroundTexture = getLoader().load(PATH_BACKGROUND);
            mPositionController = new PositionControler(mReference, PATH_CONTROLLER, PATH_CONTROL, 603, 360);

            mPlayerOne = new Player(mReference, PATH_PLAYER1, 60, 340, myPlayerData);
            // mPlayerOne = new Player(PATH_PLAYER1, 330, 230, myPlayerData);
            mPlayerTwo = new Player(mReference, PATH_PLAYER1, 1400, 340, opponentPlayerData);

        } else {

            mBackgroundTexture = getLoader().load(PATH_BACKGROUND1);
            mPositionController = new PositionControler(mReference, PATH_CONTROLLER, PATH_CONTROL, 10, 360);

            mPlayerOne = new Player(mReference, PATH_PLAYER1, -740, 340, opponentPlayerData);
            mPlayerTwo = new Player(mReference, PATH_PLAYER1, 600, 340, myPlayerData);
        }

        mBackgroundTexture.load();
        mBackgroundRegion = TextureRegionFactory.extractFromTexture(mBackgroundTexture);

        PlayersController.set_PlayerOne(mPlayerOne);
        PlayersController.set_PlayerTwo(mPlayerTwo);

        PlayersController.configureGamePlayers();
        PlayersController.getMyPlayer().stopAnimation();
        PlayersController.getMyPlayer().getmArrow().getSprite().setVisible(false);

        GamePhysicalData.setOnDirectionChangedListener(this);
        PlayersController.get_PlayerOne().getmArrow().setOnDirectionChangedListener(this);
        PlayersController.get_PlayerTwo().getmArrow().setOnDirectionChangedListener(this);

        mReference.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mChatService = BluetoothService.getInstance(BluetoothAdapter.getDefaultAdapter());
                mChatService.setOnMenssageReceivedListener(Game.this);
            }
        });

        DebugLog.log("Initialize called!");

        FruitController.Initialize(mReference, 5, PlayersController.getOpponentPlayer());
        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {
            OpponentView.Initialize(mReference, (int) (800 - OpponentView.WIDTH) - 5, 2);
        } else {
            OpponentView.Initialize(mReference, 1, 2);
        }

        // font
        BitmapTextureAtlas mTextFontTextureAtlas = new BitmapTextureAtlas(mReference.getTextureManager(), 512, 512,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mTextFont = FontFactory.create(mReference.getFontManager(), mTextFontTextureAtlas, 32);

        mReference.getEngine().getFontManager().loadFont(mTextFont);

        mReference.getEngine().getTextureManager().loadTexture(mTextFontTextureAtlas);

        mPointText = new Text(200, 0, mTextFont, "Indio Mira " + PlayersController.getMyPlayer().getmCount() + " - "
                + PlayersController.getMyPlayer().getmCount() + " Indio Opponent",
                mReference.getVertexBufferObjectManager());

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

            PlayersController.Update(isMovingPlayer);
            FruitController.Update();
            OpponentView.Update();

            byte state = mPositionController.getState();

            if (isMovingPlayer) {
                sendMessage(BluetoothService.MOVE_PLAYER, String.valueOf(state));
            }

            Sprite spt = PlayersController.getMyPlayer().getSprite();
            Sprite sptOp = PlayersController.getOpponentPlayer().getSprite();

            float x = spt.getX(), y = spt.getY();
            float xop = sptOp.getX(), yop = sptOp.getY();

            if (mPlayerOne == PlayersController.getMyPlayer()) {

                if (state == PositionControler.STATE_RIGHT && x < 500) {
                    x += Constants.PLAYER_VELOCITY;
                    PlayersController.getMyPlayer().flipHorizontal(1);
                    PlayersController.getMyPlayer().setMyPosition(x, y);
                }

                if (state == PositionControler.STATE_LEFT && x > 60) {
                    x -= Constants.PLAYER_VELOCITY;
                    PlayersController.getMyPlayer().flipHorizontal(2);
                    PlayersController.getMyPlayer().setMyPosition(x, y);
                }

                if (stateOpponent == PositionControler.STATE_RIGHT && xop < 1400) {
                    xop += Constants.PLAYER_VELOCITY;
                    PlayersController.getOpponentPlayer().flipHorizontal(1);
                    PlayersController.getOpponentPlayer().setMyPosition(xop, yop);
                    PlayersController.getOpponentPlayer().setState(Player.STATE_WALKING);
                }

                if (stateOpponent == PositionControler.STATE_LEFT && xop > 960) {
                    xop -= Constants.PLAYER_VELOCITY;
                    PlayersController.getOpponentPlayer().setMyPosition(--xop, yop);
                    PlayersController.getOpponentPlayer().flipHorizontal(2);
                    PlayersController.getOpponentPlayer().setState(Player.STATE_WALKING);
                }

                if (stateOpponent == PositionControler.STATE_CENTER) {
                    PlayersController.getOpponentPlayer().setState(Player.STATE_STOP);
                }

            } else if (mPlayerOne == PlayersController.getOpponentPlayer()) {
                if (state == PositionControler.STATE_RIGHT && x < 600) {
                    x += Constants.PLAYER_VELOCITY;
                    PlayersController.getMyPlayer().flipHorizontal(1);
                    PlayersController.getMyPlayer().setMyPosition(x, y);
                }

                if (state == PositionControler.STATE_LEFT && x > 160) {
                    x -= Constants.PLAYER_VELOCITY;
                    PlayersController.getMyPlayer().flipHorizontal(2);
                    PlayersController.getMyPlayer().setMyPosition(x, y);
                }

                if (stateOpponent == PositionControler.STATE_RIGHT && xop < -300) {
                    xop += Constants.PLAYER_VELOCITY;
                    PlayersController.getOpponentPlayer().flipHorizontal(1);
                    PlayersController.getOpponentPlayer().setMyPosition(xop, yop);
                    PlayersController.getOpponentPlayer().setState(Player.STATE_WALKING);
                }

                if (stateOpponent == PositionControler.STATE_LEFT && xop > -740) {
                    xop -= Constants.PLAYER_VELOCITY;
                    PlayersController.getOpponentPlayer().setMyPosition(xop, yop);
                    PlayersController.getOpponentPlayer().flipHorizontal(2);
                    PlayersController.getOpponentPlayer().setState(Player.STATE_WALKING);
                }

                if (stateOpponent == PositionControler.STATE_CENTER) {
                    PlayersController.getOpponentPlayer().setState(Player.STATE_STOP);
                }

            }

        } else {
            DebugLog.log("Player null update");
        }

        mPointText.setText("Indio Mira " + PlayersController.getMyPlayer().getmCount() + " - "
                + PlayersController.getOpponentPlayer().getmCount() + " Indio Opponent");
        mPositionController.update();
    }

    public float X = 0;
    public float Y = 0;
    private byte stateOpponent;

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {

        if (mCanMovePlayer) {
            if (!mPositionController.update(pSceneTouchEvent)) {
                mCanMovePlayer = false;
            } else {
                isMovingPlayer = true;
            }
        }

        if (!mCanMovePlayer) {
            myPlayerData.calculateTouch(pSceneTouchEvent, this, true);
        }

        if (pSceneTouchEvent.getX() > 780 && pSceneTouchEvent.getY() < 30) {
            ScreenManager.changeScreen(6);
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

            case MotionEvent.ACTION_UP:

                mCanMovePlayer = true;
                isMovingPlayer = false;
                PlayersController.getMyPlayer().stopAnimation();
                sendMessage(BluetoothService.MOVE_PLAYER, String.valueOf(PositionControler.STATE_CENTER));

                break;
        }

        return false;
    }

    @Override
    public void Draw() {
        // TODO Auto-generated method stub
        

        getScene().setBackground(
                new SpriteBackground(new Sprite(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT,
                        mBackgroundRegion, mReference.getVertexBufferObjectManager())));

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
        myPlayerData.getmTouch().Draw();
        
        super.Draw();
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        super.Destroy();

        mReference.runOnUpdateThread(new Runnable() {

            @Override
            public void run() {

                PlayersController.Destroy(mReference);
                FruitController.Destroy();
                OpponentView.Destroy();
                mPositionController.Destroy();
                mPointText.detachSelf();
                myPlayerData.getmTouch().getSprite().detachSelf();
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
            case BluetoothService.SHOT:
                doShotAction(message);
                break;

            case BluetoothService.MOVE_PLAYER:

                byte state = Byte.parseByte(message.substring(0, 1));

                stateOpponent = state;

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

        DebugLog.log("dataaaaaas - " + force + " --- " + angle);

        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {
            PlayersController.get_PlayerTwo().getmArrow().configPreLaunch(angle, force);

            PlayersController.get_PlayerTwo().getGameData().sShoted = true;
            PlayersController.get_PlayerTwo().getmArrow().getSprite().setVisible(true);
            PlayersController.get_PlayerTwo().getGameData().mDistance = 0;
            PlayersController.get_PlayerTwo().getGameData().mDirecao = (int) direction;
            PlayersController.get_PlayerTwo().flipHorizontal((int) direction);

            PlayersController.get_PlayerTwo().getGameData().setAngle(angle);
            PlayersController.get_PlayerTwo().getGameData().setForce(force);
        } else {
            PlayersController.get_PlayerOne().getmArrow().configPreLaunch(angle, force);

            PlayersController.get_PlayerOne().getGameData().sShoted = true;
            PlayersController.get_PlayerOne().getmArrow().getSprite().setVisible(true);
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