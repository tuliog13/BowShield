package tatu.bowshield.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.andengine.entity.primitive.Rectangle;

import tatu.bowshield.screens.Game;
import tatu.bowshield.sprites.Fruit;
import tatu.bowshield.sprites.GameSprite;

public class OpponentView {

	public static GameSprite _background;
	public static GameSprite _opponent;
	public static GameSprite _arrow;
	public static GameSprite _arrowEnemy;
	
	private static List<Fruit> _fruits;
	
	public static Game _gameReference;

	public static float WIDTH = 265.03f ;
	public static float HEIGTH = 159.03f;

	public static float positionX;
	public static float positionY;
	static Rectangle rec1;
	static Rectangle rec2;
	static Rectangle rec3;
	static Rectangle rec4;
	public static void Initialize(int x, int y) {
		positionX = x;
		positionY = y;

		_background = new GameSprite(_gameReference.PATH_BACKGROUND, getAlignedPositionX(0), getAlignedPositionY(0));
		_background.getSprite().setWidth(WIDTH);
		_background.getSprite().setHeight(HEIGTH);
		
		_opponent = new GameSprite(_gameReference.PATH_PLAYER1, getAlignedPositionX((int) PlayersController.getOpponentPlayer().getSprite().getX()), getAlignedPositionY((int) PlayersController.getOpponentPlayer().getSprite().getY()));
		_opponent.getSprite().setWidth(getAlignedWidth(PlayersController.getOpponentPlayer().getSprite().getWidth()));
		_opponent.getSprite().setHeight(getAlignedHeigth(PlayersController.getOpponentPlayer().getSprite().getHeight()));
		
		_arrow = new GameSprite(_gameReference.PATH_ARROW, getAlignedPositionX(-300), getAlignedPositionY(400));
		_arrow.getSprite().setWidth(getAlignedWidth(PlayersController.getOpponentPlayer().getmArrow().getSprite().getWidth()));
		_arrow.getSprite().setHeight(getAlignedWidth(PlayersController.getOpponentPlayer().getmArrow().getSprite().getHeight()));
		
		_arrowEnemy = new GameSprite(_gameReference.PATH_ARROW, getAlignedPositionX(-300), getAlignedPositionY(400));
		_arrowEnemy.getSprite().setWidth(getAlignedWidth(PlayersController.getOpponentPlayer().getmArrow().getSprite().getWidth()));
		_arrowEnemy.getSprite().setHeight(getAlignedWidth(PlayersController.getOpponentPlayer().getmArrow().getSprite().getHeight()));
		
		
		_fruits = new ArrayList<Fruit>();
		
		for(int i = 0; i < 5 ; i++)
		{
			if(GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE)
			{
				_fruits.add(new Fruit("gfx/apple.png",getAlignedPositionX(660),getAlignedPositionY((i * 65)+ 165)));
			}
			else
			{
				_fruits.add(new Fruit("gfx/apple.png",getAlignedPositionX(160),getAlignedPositionY((i * 65)+ 165)));
			}
		}
		
		for(Fruit fruit : _fruits)
		{
			fruit.getSprite().setWidth(getAlignedWidth(fruit.getSprite().getWidth()));
			fruit.getSprite().setHeight(getAlignedWidth(fruit.getSprite().getHeight()));
		}
		
		
	}
	
	public static void Update()
	{
		float myX = PlayersController.getMyPlayer().getmArrow()
				.getSprite().getX();
		float myY = PlayersController.getMyPlayer()
				.getmArrow().getSprite().getY();
		
		float opponentX = PlayersController.getOpponentPlayer().getmArrow()
				.getSprite().getX();
		float opponentY = PlayersController.getOpponentPlayer()
				.getmArrow().getSprite().getY();
		
		if(GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE){
			
			_arrow.setPosition(getAlignedPositionX((int) myX - 800), getAlignedPositionY((int) myY));
			_arrowEnemy.setPosition(getAlignedPositionX((int) opponentX - 800), getAlignedPositionY((int) opponentY));
			
			_opponent.setPosition(getAlignedPositionX((int) (PlayersController.getOpponentPlayer().getSprite().getX() - 800)),getAlignedPositionY( 330));
			
			if(getAlignedPositionX(myX - 800) < positionX || getAlignedPositionX(myX - 800) > positionX + WIDTH  || getAlignedPositionY(myY) > positionY + HEIGTH || getAlignedPositionY(myY) < positionY)
			{
				_arrow.getSprite().setVisible(false);
			}
			else
			{
				_arrow.getSprite().setVisible(true);
			}
			
			if(getAlignedPositionX(opponentX - 800) < positionX || getAlignedPositionX(opponentX - 800) > positionX + WIDTH  || getAlignedPositionY(opponentY) > positionY + HEIGTH || getAlignedPositionY(opponentY) < positionY)
			{
				_arrowEnemy.getSprite().setVisible(false);
			}
			else
			{
				_arrowEnemy.getSprite().setVisible(true);
			}
		}
		else
		{
			_arrow.setPosition(getAlignedPositionX((int) myX + 800), getAlignedPositionY((int) myY));
			_arrowEnemy.setPosition(getAlignedPositionX((int) opponentX + 800), getAlignedPositionY((int) opponentY));
			
			_opponent.setPosition(getAlignedPositionX((int) (PlayersController.getOpponentPlayer().getSprite().getX() + 800)),getAlignedPositionY( 330));
			
			if(getAlignedPositionX(myX + 800) < positionX || getAlignedPositionX(myX + 800) > positionX + WIDTH  || getAlignedPositionY(myY) > positionY + HEIGTH || getAlignedPositionY(myY) < positionY)
			{
				_arrow.getSprite().setVisible(false);
			}
			else
			{
				_arrow.getSprite().setVisible(true);
			}
			
			if(getAlignedPositionX(opponentX + 800) < positionX || getAlignedPositionX(opponentX + 800) > positionX + WIDTH  || getAlignedPositionY(opponentY) > positionY + HEIGTH || getAlignedPositionY(opponentY) < positionY)
			{
				_arrowEnemy.getSprite().setVisible(false);
			}
			else
			{
				_arrowEnemy.getSprite().setVisible(true);
			}
		}
		
		if(_fruits != null)
		{
			for(int i = 0; i < _fruits.size(); i++)
			{
				if(_arrow.getSprite().collidesWith(_fruits.get(i).getSprite()))
				{
					PlayersController.getMyPlayer().setmCount(1);
					_fruits.get(i).getSprite().detachSelf();            
					_fruits.remove(i);
					PlayersController.getMyPlayer().getGameData().setsShoted(false);
				}
			}
		}
		_background.setPosition(positionX,positionY);
		for(int i = 0; i < _fruits.size(); i++)
		{
			if(GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE)
			{
			//_fruits.get(i).setPosition(getAlignedPositionX(660),getAlignedPositionY((i * 60)+ 180));
			}else
			{
			//	_fruits.get(i).setPosition(getAlignedPositionX(160),getAlignedPositionY((i * 60)+ 180));
			}
		}
		
		rec1.setPosition(positionX, positionY);
		rec2.setPosition(positionX + WIDTH, positionY);
		rec3.setPosition(positionX, positionY);
		rec4.setPosition(positionX ,positionY + HEIGTH);
		
	}
	
	
	
	public static float getPositionX() {
		return positionX;
	}

	public static float getPositionY() {
		return positionY;
	}

	public static void MoveX(float x)
	{
		positionX = x;
	}
	public static void MoveY(float y)
	{
		positionY = y;
	}
	public static void Draw()
	{
		rec1 = new Rectangle(positionX, positionY, 5, HEIGTH, GameSprite.getGameReference()
				.getVertexBufferObjectManager());
		rec1.setColor(0f, 0f, 0f);
		rec2 = new Rectangle(positionX + WIDTH, positionY,5, HEIGTH, GameSprite.getGameReference()
				.getVertexBufferObjectManager());
		rec2.setColor(0f, 0f, 0f);
		rec3 = new Rectangle(positionX, positionY, WIDTH, 5, GameSprite.getGameReference()
				.getVertexBufferObjectManager());
		rec3.setColor(0f, 0f, 0f);
		rec4 = new Rectangle(positionX, positionY + HEIGTH, WIDTH, 5, GameSprite.getGameReference()
				.getVertexBufferObjectManager());
		rec4.setColor(0f, 0f, 0f);

		GameSprite.getGameReference().getScene().attachChild(_background.getSprite());
		GameSprite.getGameReference().getScene().attachChild(_opponent.getSprite());
		GameSprite.getGameReference().getScene().attachChild(_arrow.getSprite());
		GameSprite.getGameReference().getScene().attachChild(_arrowEnemy.getSprite());
		
		for(Fruit fruit : _fruits)
		{
			GameSprite.getGameReference().getScene().attachChild(fruit.getSprite());
		}
		
		GameSprite.getGameReference().getScene().attachChild(rec1);
		GameSprite.getGameReference().getScene().attachChild(rec2);
		GameSprite.getGameReference().getScene().attachChild(rec3);
		GameSprite.getGameReference().getScene().attachChild(rec4);
	}
	
	public static void Destroy()
	{
		GameSprite.getGameReference().getScene().detachChild(_background.getSprite());
		GameSprite.getGameReference().getScene().detachChild(_opponent.getSprite());
		GameSprite.getGameReference().getScene().detachChild(_arrow.getSprite());
		GameSprite.getGameReference().getScene().detachChild(_arrowEnemy.getSprite());
		
		for(Fruit fruit : _fruits)
		{
			GameSprite.getGameReference().getScene().detachChild(fruit.getSprite());
		}
		
		GameSprite.getGameReference().getScene().detachChild(rec1);
		GameSprite.getGameReference().getScene().detachChild(rec2);
		GameSprite.getGameReference().getScene().detachChild(rec3);
		GameSprite.getGameReference().getScene().detachChild(rec4);
	}
	
	private static float getAlignedWidth(float width) {
		if (width > 0) {
			return  ((width * WIDTH) / 800);
		}
		else
		{
			return width;
		}
	}
	
	private static float getAlignedHeigth(float heigth) {
		if (heigth > 0) {
			return  ((heigth * HEIGTH) / 480);
		}
		else
		{
			return heigth;
		}
	}
	
	private static float getAlignedPositionX(float x) {
		if (x > 0) {
			return ((x * WIDTH) / 800) + positionX;
		}
		else
		{
			return x + positionX;
		}
	}

	private static float getAlignedPositionY(float y) {
		if (y > 0) {
			return ((y * HEIGTH) / 480) + positionY;
		}
		else
		{
			return y + positionY;
		}
	}

	public static void setGameReference(Game game) {
		_gameReference = game;
	}

}
