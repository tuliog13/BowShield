package tatu.bowshield.sprites;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.GamePhysicalData;

public class Rope extends GameSprite {

    public Rope(final String filepath, float X, float Y) {
        super(filepath, X, Y);

    }

    public void Move(float angulo, float força, int direcao) {
        if (direcao == 1) {
            this.pSprite.setRotation(angulo);
            if(Math.abs(força/5) <= 80)this.pSprite.setWidth(-força/5);
        } else {
            this.pSprite.setRotation(-angulo);
            if(Math.abs(força/5) <= 80) this.pSprite.setWidth(força/5);
        }
        this.pSprite.setRotationCenterX(0);
        this.pSprite.setRotationCenterY(0);
    }

}
