package tatu.bowshield.sprites;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.PlayersController;
import android.view.MotionEvent;

public class Arc extends GameSprite {

    public static final int DISTANCE_CORRECT_1 = 60;
    public static final int DISTANCE_CORRECT_2 = 23;
    public static final int DISTANCE_CORRECT_Y = 15;
   
    public Arc(BowShieldGameActivity reference, final String filepath, float X, float Y) {
        super(reference, filepath, X, Y);
    }

    public void Move(float angulo, float força, int direcao, float x, float y, int state) {

        if (state == PlayersController.getMyPlayer().STATE_AIMING) {
            flipHorizontal(direcao);
            if (direcao == 1) {
                this.pSprite.setRotation(angulo);
                this.pSprite.setPosition(x + DISTANCE_CORRECT_1, y + DISTANCE_CORRECT_Y);
                this.pSprite.setRotationCenterX(0);
                this.pSprite.setRotationCenterY(this.pSprite.getHeight() / 2 + 7);
            } else {
                this.pSprite.setRotation(-angulo);
                this.pSprite.setPosition(x + DISTANCE_CORRECT_2, y + DISTANCE_CORRECT_Y);
                this.pSprite.setRotationCenterX(this.pSprite.getWidth());
                this.pSprite.setRotationCenterY(this.pSprite.getHeight() / 2 + 7);
            }
        } else if (state == PlayersController.getMyPlayer().STATE_SHOTED) {

        }
        
        if (direcao == 1) {
            this.pSprite.setPosition(x + DISTANCE_CORRECT_1, y + DISTANCE_CORRECT_Y);
        } else {
            this.pSprite.setPosition(x + DISTANCE_CORRECT_2, y + DISTANCE_CORRECT_Y);

        }
        
    }

    public boolean onTouchEvent(MotionEvent event) {

        int myEventAction = event.getAction();

        float X = event.getX();

        switch (myEventAction) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                // pSprite.setRotation((float) (X * 0.5));
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }
}