package tatu.bowshield.control;

import java.util.EventListener;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.sprites.GameSprite;

public class MoveControl extends GameSprite implements EventListener {

    public static OnDirectionChanged sListener;

    public MoveControl(BowShieldGameActivity reference, final String filepath, float X, float Y) {
        super(reference, filepath, X, Y);

    }

    @Override
    public void Draw() {
        // TODO Auto-generated method stub
        super.Draw();
    }

    public void Move() {

    }

    public static void setOnDirectionChangedListener(OnDirectionChanged listener) {
        sListener = listener;
    }

    public void configPreLaunch(float angulo, float for�a) {

    }

}