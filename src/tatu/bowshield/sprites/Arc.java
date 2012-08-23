package tatu.bowshield.sprites;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.PlayersController;
import android.view.MotionEvent;

public class Arc extends GameSprite {

    public static final int DISTANCE_CORRECT_1 = 60;
    public static final int DISTANCE_CORRECT_2 = 23;
    public static final int DISTANCE_CORRECT_Y = 15;
    
    public GameSprite linha;
    
    private boolean can;
   
    public Arc(BowShieldGameActivity reference, final String filepath, float X, float Y) {
        super(reference, filepath, X, Y);
        
        linha = new GameSprite(reference, "gfx/linha.png", 0, 0);
        can = false;
    }

    public void Move(float angulo, float força, int direcao, float x, float y, int state) {

        float inicialX= PlayersController.getMyPlayer().getGameData().getInicialX();
        float inicialY= PlayersController.getMyPlayer().getGameData().getInicialY();
        
        if(!can)
            linha.setPosition(inicialX, inicialY);
        
        can = true;
        
        this.linha.getSprite().setRotationCenterX(0);
        this.linha.getSprite().setRotationCenterX(0);
        
        if (state == PlayersController.getMyPlayer().STATE_AIMING) {
            
            linha.getSprite().setVisible(true);
            
            flipHorizontal(direcao);
            if (direcao == 1) {
                this.pSprite.setRotation(angulo);
                this.pSprite.setPosition(x + DISTANCE_CORRECT_1, y + DISTANCE_CORRECT_Y);
                this.pSprite.setRotationCenterX(0);
                this.pSprite.setRotationCenterY(this.pSprite.getHeight() / 2 + 7);

                if(this == PlayersController.getMyPlayer().getmArc())
                {
                    this.linha.getSprite().setWidth(-força*120);
                    linha.getSprite().setFlippedHorizontal(false);
                    linha.getSprite().setRotation(angulo);
                }
                
            } else {
                this.pSprite.setRotation(-angulo);
                this.pSprite.setPosition(x + DISTANCE_CORRECT_2, y + DISTANCE_CORRECT_Y);
                this.pSprite.setRotationCenterX(this.pSprite.getWidth());
                this.pSprite.setRotationCenterY(this.pSprite.getHeight() / 2 + 7);
                
                if(this == PlayersController.getMyPlayer().getmArc())
                {
                    this.linha.getSprite().setWidth(força*120);
                    linha.getSprite().setFlippedHorizontal(true);
                    linha.getSprite().setRotation(-angulo);
                }
            }
        } else if (state == PlayersController.getMyPlayer().STATE_SHOTED) {
            
            can = false;
            if(this == PlayersController.getMyPlayer().getmArc())
            {
                linha.getSprite().setVisible(false);
            }
        }
        
        if (direcao == 1) {
            this.pSprite.setPosition(x + DISTANCE_CORRECT_1, y + DISTANCE_CORRECT_Y);
        } else {
            this.pSprite.setPosition(x + DISTANCE_CORRECT_2, y + DISTANCE_CORRECT_Y);

        }
        
    }
    
    @Override
    public void Draw() {
        // TODO Auto-generated method stub
        super.Draw();
        linha.Draw();
        if(this == PlayersController.getOpponentPlayer().getmArc())
        {
            linha.getSprite().setVisible(false);
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