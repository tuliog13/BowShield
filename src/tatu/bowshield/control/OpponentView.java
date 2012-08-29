package tatu.bowshield.control;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.primitive.Rectangle;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.screens.Game;
import tatu.bowshield.sprites.AnimatedGameSprite;
import tatu.bowshield.sprites.Fruit;
import tatu.bowshield.sprites.GameSprite;
import tatu.bowshield.sprites.Player;

public class OpponentView {

    public static GameSprite         _background;
    public static AnimatedGameSprite _opponent;
    public static GameSprite         _arrow;
    public static GameSprite         _arrowEnemy;

    private static List<Fruit>       _fruits;

    public static Game               _gameReference;

    public static float              WIDTH  = 265.03f;
    public static float              HEIGTH = 159.03f;

    public static float              positionX;
    public static float              positionY;
    static Rectangle                 rec1;
    static Rectangle                 rec2;
    static Rectangle                 rec3;
    static Rectangle                 rec4;
    static BowShieldGameActivity     mReference;

    public static void Initialize(BowShieldGameActivity reference, int x, int y) {

        mReference = reference;
        positionX = x;
        positionY = y;

        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {

            _background = new GameSprite(mReference, _gameReference.PATH_BACKGROUND1, getAlignedPositionX(0),
                    getAlignedPositionY(0));
        } else {
            _background = new GameSprite(mReference, _gameReference.PATH_BACKGROUND, getAlignedPositionX(0),
                    getAlignedPositionY(0));
        }
        _background.getSprite().setWidth(WIDTH);
        _background.getSprite().setHeight(HEIGTH);

        _opponent = new AnimatedGameSprite(mReference, _gameReference.PATH_PLAYER1,
                getAlignedPositionX((int) PlayersController.getOpponentPlayer().getSprite().getX()),
                getAlignedPositionY((int) PlayersController.getOpponentPlayer().getSprite().getY()), 9, 1);
        _opponent.getSprite().setWidth(getAlignedWidth(PlayersController.getOpponentPlayer().getSprite().getWidth()));
        _opponent.getSprite()
                .setHeight(getAlignedHeigth(PlayersController.getOpponentPlayer().getSprite().getHeight()));

        _arrow = new GameSprite(mReference, _gameReference.PATH_ARROW, getAlignedPositionX(-300),
                getAlignedPositionY(400));
        _arrow.getSprite().setWidth(
                getAlignedWidth(PlayersController.getOpponentPlayer().getmArrow().getSprite().getWidth()));
        _arrow.getSprite().setHeight(
                getAlignedWidth(PlayersController.getOpponentPlayer().getmArrow().getSprite().getHeight()));

        _arrowEnemy = new GameSprite(mReference, _gameReference.PATH_ARROW, getAlignedPositionX(-300),
                getAlignedPositionY(400));
        _arrowEnemy.getSprite().setWidth(
                getAlignedWidth(PlayersController.getOpponentPlayer().getmArrow().getSprite().getWidth()));
        _arrowEnemy.getSprite().setHeight(
                getAlignedWidth(PlayersController.getOpponentPlayer().getmArrow().getSprite().getHeight()));

        _fruits = new ArrayList<Fruit>();

//        for (int i = 0; i < 5; i++) {
//            if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {
//                _fruits.add(new Fruit(mReference, "gfx/apple.png", getAlignedPositionX(660),
//                        getAlignedPositionY((i * 65) + 165)));
//            } else {
//                _fruits.add(new Fruit(mReference, "gfx/apple.png", getAlignedPositionX(160),
//                        getAlignedPositionY((i * 65) + 165)));
//            }
//        }
        
        if (GamePhysicalData.GAME_TYPE != GamePhysicalData.SERVER_TYPE) {
            _fruits.add(new Fruit(reference, "gfx/apple.png",getAlignedPositionX( 30),getAlignedPositionY( 80)));
            _fruits.add(new Fruit(reference, "gfx/apple.png",getAlignedPositionX( 70), getAlignedPositionY(130)));
            _fruits.add(new Fruit(reference, "gfx/apple.png",getAlignedPositionX( 120),getAlignedPositionY(170 )));
            _fruits.add(new Fruit(reference, "gfx/apple.png",getAlignedPositionX( 150),getAlignedPositionY(240 )));
            _fruits.add(new Fruit(reference, "gfx/apple.png",getAlignedPositionX( 175), getAlignedPositionY(100 )));
            _fruits.add(new Fruit(reference, "gfx/apple.png",getAlignedPositionX( 30), getAlignedPositionY(220 )));
        } else {
            _fruits.add(new Fruit(reference, "gfx/apple.png", getAlignedPositionX( 630), getAlignedPositionY(80)));
            _fruits.add(new Fruit(reference, "gfx/apple.png",getAlignedPositionX(  570), getAlignedPositionY(130)));
            _fruits.add(new Fruit(reference, "gfx/apple.png", getAlignedPositionX( 650),getAlignedPositionY(160 )));
            _fruits.add(new Fruit(reference, "gfx/apple.png", getAlignedPositionX( 590),getAlignedPositionY(240 )));
            _fruits.add(new Fruit(reference, "gfx/apple.png", getAlignedPositionX( 750), getAlignedPositionY(130) ));
            _fruits.add(new Fruit(reference, "gfx/apple.png", getAlignedPositionX( 750),getAlignedPositionY( 225) ));
        }

        for (Fruit fruit : _fruits) {
            fruit.getSprite().setWidth(getAlignedWidth(fruit.getSprite().getWidth()));
            fruit.getSprite().setHeight(getAlignedWidth(fruit.getSprite().getHeight()));
        }

        _opponent.animate();

    }

    public static void Update() {
        float myX = PlayersController.getMyPlayer().getmArrow().getSprite().getX();
        float myY = PlayersController.getMyPlayer().getmArrow().getSprite().getY();

        float opponentX = PlayersController.getOpponentPlayer().getmArrow().getSprite().getX();
        float opponentY = PlayersController.getOpponentPlayer().getmArrow().getSprite().getY();

        _opponent.getSprite().setFlippedHorizontal(PlayersController.getOpponentPlayer().getSprite().isFlippedHorizontal());
        
        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {

            _arrow.setPosition(getAlignedPositionX((int) myX - 800), getAlignedPositionY((int) myY));
            _arrowEnemy.setPosition(getAlignedPositionX((int) opponentX - 800), getAlignedPositionY((int) opponentY));

            _opponent.setPosition(
                    getAlignedPositionX((int) (PlayersController.getOpponentPlayer().getSprite().getX() - 800)),
                    getAlignedPositionY(330));

            if (getAlignedPositionX(myX - 800) < positionX || getAlignedPositionX(myX - 800) > positionX + WIDTH
                    || getAlignedPositionY(myY) > positionY + HEIGTH || getAlignedPositionY(myY) < positionY) {
                _arrow.getSprite().setVisible(false);
            } else {
                _arrow.getSprite().setVisible(true);
            }

            if (getAlignedPositionX(opponentX - 800) < positionX
                    || getAlignedPositionX(opponentX - 800) > positionX + WIDTH
                    || getAlignedPositionY(opponentY) > positionY + HEIGTH
                    || getAlignedPositionY(opponentY) < positionY) {
                _arrowEnemy.getSprite().setVisible(false);
            } else {
                _arrowEnemy.getSprite().setVisible(true);
            }
        } else {
            _arrow.setPosition(getAlignedPositionX((int) myX + 800), getAlignedPositionY((int) myY));
            _arrowEnemy.setPosition(getAlignedPositionX((int) opponentX + 800), getAlignedPositionY((int) opponentY));

            _opponent.setPosition(
                    getAlignedPositionX((int) (PlayersController.getOpponentPlayer().getSprite().getX() + 800)),
                    getAlignedPositionY(330));

            if (getAlignedPositionX(myX + 800) < positionX || getAlignedPositionX(myX + 800) > positionX + WIDTH
                    || getAlignedPositionY(myY) > positionY + HEIGTH || getAlignedPositionY(myY) < positionY) {
                _arrow.getSprite().setVisible(false);
            } else {
                _arrow.getSprite().setVisible(true);
            }

            if (getAlignedPositionX(opponentX + 800) < positionX
                    || getAlignedPositionX(opponentX + 800) > positionX + WIDTH
                    || getAlignedPositionY(opponentY) > positionY + HEIGTH
                    || getAlignedPositionY(opponentY) < positionY) {
                _arrowEnemy.getSprite().setVisible(false);
            } else {
                _arrowEnemy.getSprite().setVisible(true);
            }
        }

        if (_fruits != null) {
            for (int i = 0; i < _fruits.size(); i++) {
                if (_arrow.getSprite().collidesWith(_fruits.get(i).getSprite())) {
                    PlayersController.getMyPlayer().setmCount(1);
                    _fruits.get(i).getSprite().detachSelf();
                    _fruits.remove(i);
                    PlayersController.getMyPlayer().getGameData().setsShoted(false);
                    PlayersController.getMyPlayer().getmArrow().getSprite().setVisible(false);
                }
            }
        }
        
        _background.setPosition(positionX, positionY);
        for (int i = 0; i < _fruits.size(); i++) {
            if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {
                // _fruits.get(i).setPosition(getAlignedPositionX(660),getAlignedPositionY((i * 60)+ 180));
            } else {
                // _fruits.get(i).setPosition(getAlignedPositionX(160),getAlignedPositionY((i * 60)+ 180));
            }
        }

        int state = PlayersController.getOpponentPlayer().getState();
        if(state == Player.STATE_STOP){
            _opponent.stopAnimation(8);
        }else if(state == Player.STATE_WALKING){
            _opponent.animate();
        }
        
        rec1.setPosition(positionX, positionY);
        rec2.setPosition(positionX + WIDTH, positionY);
        rec3.setPosition(positionX, positionY);
        rec4.setPosition(positionX, positionY + HEIGTH);

    }

    public static float getPositionX() {
        return positionX;
    }

    public static float getPositionY() {
        return positionY;
    }

    public static void MoveX(float x) {
        positionX = x;
    }

    public static void MoveY(float y) {
        positionY = y;
    }
    
    public static void Draw() {
        rec1 = new Rectangle(positionX, positionY, 5, HEIGTH, mReference.getVertexBufferObjectManager());
        rec1.setColor(0f, 0f, 0f);
        rec2 = new Rectangle(positionX + WIDTH, positionY, 5, HEIGTH, mReference.getVertexBufferObjectManager());
        rec2.setColor(0f, 0f, 0f);
        rec3 = new Rectangle(positionX, positionY, WIDTH, 5, mReference.getVertexBufferObjectManager());
        rec3.setColor(0f, 0f, 0f);
        rec4 = new Rectangle(positionX, positionY + HEIGTH, WIDTH + 5, 5, mReference.getVertexBufferObjectManager());
        rec4.setColor(0f, 0f, 0f);

        mReference.getScene().attachChild(_background.getSprite());
        mReference.getScene().attachChild(_opponent.getSprite());
        mReference.getScene().attachChild(_arrow.getSprite());
        mReference.getScene().attachChild(_arrowEnemy.getSprite());

        for (Fruit fruit : _fruits) {
            mReference.getScene().attachChild(fruit.getSprite());
        }

        mReference.getScene().attachChild(rec1);
        mReference.getScene().attachChild(rec2);
        mReference.getScene().attachChild(rec3);
        mReference.getScene().attachChild(rec4);
    }

    public static void Destroy() {
        mReference.getScene().detachChild(_background.getSprite());
        mReference.getScene().detachChild(_opponent.getSprite());
        mReference.getScene().detachChild(_arrow.getSprite());
        mReference.getScene().detachChild(_arrowEnemy.getSprite());

        for (Fruit fruit : _fruits) {
            mReference.getScene().detachChild(fruit.getSprite());
        }

        mReference.getScene().detachChild(rec1);
        mReference.getScene().detachChild(rec2);
        mReference.getScene().detachChild(rec3);
        mReference.getScene().detachChild(rec4);
    }

    private static float getAlignedWidth(float width) {
        if (width > 0) {
            return ((width * WIDTH) / 800);
        } else {
            return width;
        }
    }

    private static float getAlignedHeigth(float heigth) {
        if (heigth > 0) {
            return ((heigth * HEIGTH) / 480);
        } else {
            return heigth;
        }
    }

    private static float getAlignedPositionX(float x) {
        if (x > 0) {
            return ((x * WIDTH) / 800) + positionX;
        } else {
            return x + positionX;
        }
    }

    private static float getAlignedPositionY(float y) {
        if (y > 0) {
            return ((y * HEIGTH) / 480) + positionY;
        } else {
            return y + positionY;
        }
    }

    public static void setGameReference(Game game) {
        _gameReference = game;
    }

}
