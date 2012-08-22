package tatu.bowshield.control;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.sprites.GameSprite;
import tatu.bowshield.sprites.Player;

public class PlayersController {

    private static Player _currentPlayer;

    private static Player _PlayerOne;
    private static Player _PlayerTwo;

    public static void changeTurn() {
        if (_currentPlayer == _PlayerOne)
            set_currentPlayer(_PlayerTwo);
        else
            set_currentPlayer(_PlayerOne);
    }

    public static Player getOpponentPlayer() {
        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE)
            return _PlayerTwo;
        else
            return _PlayerOne;
    }

    public static void Update(boolean toAnimePlayer) {
        _PlayerTwo.Move(toAnimePlayer);
        _PlayerOne.Move(toAnimePlayer);
    }

    public static void Draw() {
        _PlayerTwo.Draw();
        _PlayerOne.Draw();
    }

    public static Player getMyPlayer() {
        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {
            return get_PlayerOne();
        } else {
            return get_PlayerTwo();
        }
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

    public static void configureGamePlayers() {
        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {
            get_PlayerOne().setMyPosition(60, 340);
            // get_PlayerOne().setMyPosition(330, 230);
            get_PlayerTwo().setMyPosition(1500, 340);
            get_PlayerTwo().flipHorizontal(2);
        } else {
            get_PlayerOne().setMyPosition(-740, 340);
            get_PlayerTwo().setMyPosition(700, 340);

            get_PlayerTwo().flipHorizontal(2);
        }
    }

    public static void Destroy(BowShieldGameActivity reference) {
        // TODO Auto-generated method stub
        _PlayerOne.Destroy(reference.getScene());
        _PlayerTwo.Destroy(reference.getScene());
    }

}
