package tatu.bowshield.sprites;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.screens.Game;

public class Player extends GameSprite {

    private Arc   mArc;
    private Arrow mArrow;
    private Indicator mIndicator;

    private int state;
    
    public int STATE_AIMING = 0;
    public int STATE_STOP = 1;
    public int STATE_SHOTED = 2;
    public int STATE_WALKING = 3;
    
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private int   mCount;

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount += mCount;
    }

    private GamePhysicalData mGameData;

    public Arrow getmArrow() {
        return mArrow;
    }

    public void setmArrow(Arrow mArrow) {
        this.mArrow = mArrow;
    }

    private int mLife;

    public Player(final String filepath, float X, float Y, GamePhysicalData physicalDta) {
        super(filepath, X, Y);

        mArc = new Arc(Game.PATH_ARC, X + 20, Y + 20);
        mArrow = new Arrow(Game.PATH_ARROW, X + 15, Y + 43);
        mIndicator = new Indicator(Game.PATH_ROPE, X - 15, Y - 20);
        
        mGameData = physicalDta;
        mCount = 0;
        state = STATE_STOP;
    }

    public GamePhysicalData getGameData() {
        return this.mGameData;
    }

    public void setMyPosition(float x, float y) {
        this.setPosition(x, y);
        mArc.setPosition(x + 20, y + 20);
        mIndicator.setPosition(x - 15, y - 20);
    }

    public void Move() {

        mArrow.Move(mGameData.sShoted, mGameData.getAngulo(), mGameData.getForca(), mGameData.mDirecao);
        mIndicator.Move(mGameData.getAngulo(), mGameData.getForca(), mGameData.mDirecao, state);
        mArc.Move(mGameData.getAngulo(), mGameData.getForca(), mGameData.mDirecao, this.getSprite().getX(), this.getSprite().getY(),state);

    }

    @Override
    public void Draw() {
        // TODO Auto-generated method stub
        mArrow.Draw();
        super.Draw();
        mArc.Draw();
        mIndicator.Draw();
    }

    public void flipHorizontal1(int direction) {
        mArc.flipHorizontal(direction);
        mArrow.flipHorizontal(direction);
//        if(direction == 2)
//        {
//            mArc.setPosition(this.getSprite().getX() - 20, mArc.getSprite().getY());
//            mArc.getSprite().setRotationCenterX(mArc.getSprite().getWidth());
//            mArc.getSprite().setRotationCenterY(mArc.getSprite().getHeight()/2 + 8);
//        }
//        else
//        {
//            mArc.setPosition(this.getSprite().getX() + 20, this.getSprite().getY() + 20);
//            mArc.getSprite().setRotationCenterX(0);
//            mArc.getSprite().setRotationCenterY(mArc.getSprite().getHeight()/2 + 8);
//        }
        mIndicator.flipHorizontal(direction);
        this.flipHorizontal(direction);
    }

    public void Destroy(Scene s) {
        s.detachChild(mArc.getSprite());
        s.detachChild(mArrow.getSprite());
        s.detachChild(mIndicator.getSprite());
        s.detachChild(getSprite());
    }
}