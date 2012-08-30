package tatu.bowshield.sprites;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.screens.Game;

public class Player extends AnimatedGameSprite {

    private Arc       mArc;
    private Arrow     mArrow;
    private Indicator mIndicator;

    private int       state;

    public static int STATE_AIMING  = 0;
    public static int STATE_STOP    = 1;
    public static int STATE_SHOTED  = 2;
    public static int STATE_WALKING = 3;

    public static GameSprite head;
    
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private int mCount;

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

    // 8 and 1 are the columns and the rows of the animation sprite
    public Player(BowShieldGameActivity reference, final String filepath, float X, float Y, GamePhysicalData physicalDta) {
        super(reference, filepath, X, Y, 9, 1);

        mArc = new Arc(reference, Game.PATH_ARC, X + Arc.DISTANCE_CORRECT_1, Y + Arc.DISTANCE_CORRECT_Y);
        mArrow = new Arrow(reference, Game.PATH_ARROW, X + 15, Y + 43);
        mIndicator = new Indicator(reference, Game.PATH_ROPE, X - 15, Y - 20);

        mGameData = physicalDta;
        mCount = 0;
        state = STATE_STOP;
        mArc.pSprite.setVisible(false);
        
        head = new GameSprite(reference, "gfx/head.png", X, Y);
        
    }

    public GamePhysicalData getGameData() {
        return this.mGameData;
    }

    public void setMyPosition(float x, float y) {
        this.setPosition(x, y);

        if (mGameData.mDirecao == 1) {
            mArc.setPosition(x + Arc.DISTANCE_CORRECT_1, y + Arc.DISTANCE_CORRECT_Y);
        } else {
            mArc.setPosition(x + Arc.DISTANCE_CORRECT_2, y + Arc.DISTANCE_CORRECT_Y);
        }

        mIndicator.setPosition(x - 15, y - 20);
        head.setPosition(x + 20, y);
        animate();
        mArc.pSprite.setVisible(false);
        state = STATE_WALKING;
    }

    public void stopAnimation() {
        super.stopAnimation(8);
        mArc.pSprite.setVisible(true);
        state = STATE_STOP;
    }

    public void Move(boolean toAnime) {

        mArrow.Move(mGameData.sShoted, mGameData.getAngulo(), mGameData.getForca(), mGameData.mDirecao);
        mIndicator.Move(mGameData.getAngulo(), mGameData.getForca(), mGameData.mDirecao, state);
        mArc.Move(mGameData.getAngulo(), mGameData.getForca(), mGameData.mDirecao, this.getSprite().getX(), this
                .getSprite().getY(), state);

        if (toAnime) {

        }

    }

    @Override
    public void Draw() {
        mArrow.Draw();
        super.Draw();
        mArc.Draw();
        mIndicator.Draw();
        head.Draw();
    }

    public void flipHorizontal1(int direction) {
        mArc.flipHorizontal(direction);
        mArrow.flipHorizontal(direction);
        mIndicator.flipHorizontal(direction);
        this.flipHorizontal(direction);
    }

    public void Destroy(Scene s) {
        s.detachChild(mArc.getSprite());
        s.detachChild(mArrow.getSprite());
        s.detachChild(mIndicator.getSprite());
        s.detachChild(getSprite());
    }

    public Arc getmArc() {
        // TODO Auto-generated method stub
        return this.mArc;
    }
}