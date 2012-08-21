package tatu.bowshield.sprites;

import tatu.bowshield.control.PlayersController;
import android.view.MotionEvent;

public class Arc extends GameSprite {

    public Arc(final String filepath, float X, float Y) {
        super(filepath, X, Y);
    }

    public void Move(float angulo, float força, int direcao, float x, float y, int state) {

        if (state == PlayersController.getMyPlayer().STATE_AIMING) {
            flipHorizontal(direcao);
            if (direcao == 1) {
                this.pSprite.setRotation(angulo);
                this.pSprite.setPosition(x + 20, y + 20);
                this.pSprite.setRotationCenterX(0);
                this.pSprite.setRotationCenterY(this.pSprite.getHeight() / 2 + 7);
            } else {
                this.pSprite.setRotation(-angulo);
                this.pSprite.setPosition(x - 20, y + 20);
                this.pSprite.setRotationCenterX(this.pSprite.getWidth());
                this.pSprite.setRotationCenterY(this.pSprite.getHeight() / 2 + 7);
            }
        } else if (state == PlayersController.getMyPlayer().STATE_SHOTED) {

        }
        
        if (direcao == 1) {
            this.pSprite.setPosition(x + 20, y + 20);
        } else {
            this.pSprite.setPosition(x - 20, y + 20);

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