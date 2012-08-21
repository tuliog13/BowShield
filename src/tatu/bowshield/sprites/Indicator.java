package tatu.bowshield.sprites;

import tatu.bowshield.control.PlayersController;
import tatu.bowshield.screens.Game;

public class Indicator extends GameSprite {

    private GameSprite mForceBar;
    
    public Indicator(final String filepath, float X, float Y) {
        super(filepath, X, Y);
        
        mForceBar = new GameSprite(Game.PATH_FORCE_BAR, X + 1, Y+ 1);
        
    }

    public void Move(float angulo, float força, int direcao, int state) {
        
        if(state == PlayersController.getMyPlayer().STATE_AIMING)
        {
           this.getSprite().setVisible(true);
           mForceBar.getSprite().setVisible(true);
           mForceBar.getSprite().setWidth(força* 34);
        }
        else
        {
            this.getSprite().setVisible(false);
            mForceBar.getSprite().setVisible(false);
        }
        mForceBar.setPosition(this.getSprite().getX() +1,this.getSprite().getY() + 1);
       
    }
    
    @Override
    public void Draw() {
        // TODO Auto-generated method stub
        super.Draw();
        
        mForceBar.Draw();
        
    }

}
