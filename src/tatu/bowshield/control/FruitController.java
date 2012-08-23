package tatu.bowshield.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.sprites.Fruit;
import tatu.bowshield.sprites.Player;

public class FruitController {

    private static Player      _player;
    private static int         _quantidade;
    private static List<Fruit> _fruits;

    public static void Initialize(BowShieldGameActivity reference, int quantidade, Player player) {
        _player = player;
        _quantidade = quantidade;

        _fruits = new ArrayList<Fruit>();

        // for (int i = 0; i < _quantidade; i++) {
        // Random r = new Random();
        // if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {
        // _fruits.add(new Fruit(reference, "gfx/apple.png", 160, (i * 65) + 165));
        // } else {
        // _fruits.add(new Fruit(reference, "gfx/apple.png", 660, (i * 65) + 165));
        // }
        // }

        if (GamePhysicalData.GAME_TYPE == GamePhysicalData.SERVER_TYPE) {
            _fruits.add(new Fruit(reference, "gfx/apple.png", 30, 80));
            _fruits.add(new Fruit(reference, "gfx/apple.png", 70, 130));
            _fruits.add(new Fruit(reference, "gfx/apple.png", 120,170 ));
            _fruits.add(new Fruit(reference, "gfx/apple.png", 150,240 ));
            _fruits.add(new Fruit(reference, "gfx/apple.png", 175, 100 ));
            _fruits.add(new Fruit(reference, "gfx/apple.png", 30, 220 ));
        } else {
            _fruits.add(new Fruit(reference, "gfx/apple.png", 630, 80));
            _fruits.add(new Fruit(reference, "gfx/apple.png", 570, 130));
            _fruits.add(new Fruit(reference, "gfx/apple.png", 650,160 ));
            _fruits.add(new Fruit(reference, "gfx/apple.png", 590,240 ));
            _fruits.add(new Fruit(reference, "gfx/apple.png", 750, 130 ));
            _fruits.add(new Fruit(reference, "gfx/apple.png", 750, 225 ));
        }

    }

    public static void Update() {
        if (_fruits != null) {
            for (int i = 0; i < _fruits.size(); i++) {
                if (_player.getmArrow().getSprite().collidesWith(_fruits.get(i).getSprite())) {
                    PlayersController.getOpponentPlayer().setmCount(1);
                    _fruits.get(i).getSprite().detachSelf();
                    _fruits.remove(i);
                }
            }
        }

    }

    public static void Destroy() {
        for (Fruit fruit : _fruits) {
            fruit.getSprite().detachSelf();
        }
    }

    public static void Draw() {
        for (Fruit fruit : _fruits) {
            fruit.Draw();
        }
    }

}
