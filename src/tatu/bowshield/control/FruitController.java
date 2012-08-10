package tatu.bowshield.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tatu.bowshield.sprites.Fruit;
import tatu.bowshield.sprites.Player;

public class FruitController {
	
	private static Player _player;
	private static int _quantidade;
	private static List<Fruit> _fruits;
	
	public static void Initialize(int quantidade, Player player)
	{
		_player = player;
		_quantidade = quantidade;
		
		_fruits = new ArrayList<Fruit>();
		
		for(int i = 0; i < _quantidade ; i++)
		{
			Random r = new Random();
			if(GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE)
			{
				_fruits.add(new Fruit("gfx/apple.png",r.nextInt(260),r.nextInt(210)));
			}
			else
			{
				_fruits.add(new Fruit("gfx/apple.png",r.nextInt(260) + 500,r.nextInt(210)));
			}
		}
	}
	
	public static void Update()
	{
		if(_fruits != null)
		{
			for(Fruit fruit : _fruits)
			{
				if(_player.getmArrow().getSprite().collidesWith(fruit.getSprite()))
				{
					fruit.getSprite().detachSelf();
				}
			}
		}
	}
	
	public static void Destroy()
	{
		for(Fruit fruit : _fruits)
		{
			fruit.getSprite().detachSelf();
		}
	}
	
	public static void Draw()
	{
		for(Fruit fruit : _fruits)
		{
			fruit.Draw();
		}
	}
	
}
