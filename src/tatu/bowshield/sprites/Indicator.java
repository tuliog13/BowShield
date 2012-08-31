package tatu.bowshield.sprites;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.PlayersController;
import tatu.bowshield.screens.Game;
import tatu.bowshield.util.DebugLog;

public class Indicator extends GameSprite {

    private GameSprite mForceBar;
    public GameSprite head;
    
    
    public Indicator(BowShieldGameActivity reference, final String filepath, float X, float Y) {
        super(reference, filepath, X, Y);
        
        mForceBar = new GameSprite(reference, Game.PATH_FORCE_BAR, X + 1, Y+ 1);
        head = new GameSprite(reference, "gfx/head.png", X + 55, Y - 30);
    }

    public void Move(float angulo, float força, int direcao, int state) {
        
        if(state == PlayersController.getMyPlayer().STATE_AIMING)
        {
           this.getSprite().setVisible(true);
           mForceBar.getSprite().setVisible(true);
           head.getSprite().setVisible(true);
           mForceBar.getSprite().setWidth(força* 34);
           
           if(angulo > -30 && angulo < 30)
           {
               if(direcao == 1)
               head.getSprite().setRotation(angulo);
               else
               {
                   head.getSprite().setRotation(-angulo);
               }
           }
           
           head.getSprite().setRotationCenterX( 30 );
           head.getSprite().setRotationCenterY( 40 );
           
        }
        else
        {
            this.getSprite().setVisible(false);
            head.getSprite().setVisible(false);
            mForceBar.getSprite().setVisible(false);
        }
        mForceBar.setPosition(this.getSprite().getX() +1,this.getSprite().getY() + 1);
       
    }
    
    @Override
    public void flipHorizontal(int direction) {
        // TODO Auto-generated method stub
        super.flipHorizontal(direction);
        head.flipHorizontal(direction);
    }
    
    @Override
    public void setPosition(float x, float y) {
        // TODO Auto-generated method stub
        super.setPosition(x, y);
        head.setPosition(x + 55, y + 15);
    }
    
    @Override
    public void Draw() {
        // TODO Auto-generated method stub
        super.Draw();
        head.Draw();
        mForceBar.Draw();
        
    }

}
