package tatu.bowshield.sprites;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.screens.Game;

public class Player extends AnimatedGameSprite {

    private Arc          mArc;
    private Arrow        mArrow;
    private Indicator    mIndicator;
    AnimatedGameSprite   mRespireSprite;

    private int          state;

    public static String PATH_RESPIRE  = "gfx/respir.png";

    public static int    STATE_AIMING  = 0;
    public static int    STATE_STOP    = 1;
    public static int    STATE_SHOTED  = 2;
    public static int    STATE_WALKING = 3;

    private int          mY            = 340;

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
        
        mRespireSprite = new AnimatedGameSprite(reference, PATH_RESPIRE, pSprite.getX(), pSprite.getY(), 5, 1);
        mRespireSprite.setAnimationSettings(new long[] { 290, 290, 290, 290 }, 1, 4, true);
        mRespireSprite.animate();
    }

    public GamePhysicalData getGameData() {
        return this.mGameData;
    }

    public void setMyPosition(float x, float y) {
        this.setPosition(x, mY);

        if (mGameData.mDirecao == 1) {
            mArc.setPosition(x + Arc.DISTANCE_CORRECT_1, y + Arc.DISTANCE_CORRECT_Y);
        } else {
            mArc.setPosition(x + Arc.DISTANCE_CORRECT_2, y + Arc.DISTANCE_CORRECT_Y);
        }

        pSprite.setVisible(true);
        mIndicator.setPosition(x - 15, y - 20);
        animate();
        mArc.pSprite.setVisible(false);
        mRespireSprite.getSprite().setVisible(false);
        state = STATE_WALKING;
    }

    public void stopAnimationAndRespire() {
        super.stopAnimation(8);
        pSprite.setVisible(false);
        mArc.pSprite.setVisible(false);
        mRespireSprite.setPosition(pSprite.getX() + 35, mY);
        mRespireSprite.getSprite().setVisible(true);
        state = STATE_STOP;
    }

    public void Move(boolean toAnime) {

        mArrow.Move(mGameData.sShoted, mGameData.getAngulo(), mGameData.getForca(), mGameData.mDirecao);
        mIndicator.Move(mGameData.getAngulo(), mGameData.getForca(), mGameData.mDirecao, state);
        mArc.Move(mGameData.getAngulo(), mGameData.getForca(), mGameData.mDirecao, this.getSprite().getX(), mY + 8,
                state);

        if (state == STATE_AIMING) {
            mArc.pSprite.setVisible(true);
            pSprite.setPosition(pSprite.getX(), mY + 8);
            mRespireSprite.getSprite().setVisible(false);
            super.stopAnimation(8);
            pSprite.setVisible(true);
        }

    }

    @Override
    public void Draw() {
        mArrow.Draw();
        super.Draw();
        mIndicator.Draw();
        mArc.Draw();
        mRespireSprite.Draw();
    }

    public void flipHorizontal1(int direction) {
        mArc.flipHorizontal(direction);
        mArrow.flipHorizontal(direction);
        mIndicator.flipHorizontal(direction);
        mRespireSprite.flipHorizontal(direction);
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