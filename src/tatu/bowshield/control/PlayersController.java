package tatu.bowshield.control;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.sprites.Player;

public class PlayersController {
	
	private static Player _currentPlayer;
	
	private static Player _PlayerOne;
	private static Player _PlayerTwo;
	
	public static void changeTurn()
	{
		if(_currentPlayer == _PlayerOne)
			set_currentPlayer(_PlayerTwo);
		else
			set_currentPlayer(_PlayerOne);
	}
	public static Player getWatingPlayer()
	{
		if(_currentPlayer == _PlayerOne)
			return _PlayerTwo;
		else
			return _PlayerOne;
	}
	public static void Update()
	{
		_currentPlayer.Move();
	}
	
	public static void Draw()
	{
		_PlayerTwo.Draw();
		_PlayerOne.Draw();
	}
	
	public static Player get_currentPlayer() {
		return _currentPlayer;
	}

	public static void set_currentPlayer(Player _currentPlayer) {
		PlayersController._currentPlayer = _currentPlayer;
	}

	public static Player get_PlayerOne() {
		return _PlayerOne;
	}

	public static void set_PlayerOne(Player _PlayerOne) {
		PlayersController._PlayerOne = _PlayerOne;
	}

	public static Player get_PlayerTwo() {
		return _PlayerTwo;
	}

	public static void set_PlayerTwo(Player _PlayerTwo) {
		PlayersController._PlayerTwo = _PlayerTwo;
	}
	
}
