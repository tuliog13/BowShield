package tatu.bowshield.sprites;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.screens.Game;

public class Player extends GameSprite{

	private Arc mArc;
	private Arrow mArrow;
	private Rope mRope;
	
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
	private int mLife;
	
	public Player(final String filepath,float X, float Y, GamePhysicalData physicalDta) {
		super(filepath,X,Y);

		mArc = new Arc(Game.PATH_ARC, X + 35, Y - 25);
		mArrow = new Arrow(Game.PATH_ARROW, X + 60, Y + 63);
		mRope = new Rope(Game.PATH_ROPE, X + 60, Y + 63);
		mArc.pSprite.setVisible(false);
		mGameData = physicalDta;
		mCount = 0;
	}
	
	public GamePhysicalData getGameData()
	{
		return this.mGameData;
	}
	
	public void setMyPosition(float x, float y)
	{
		this.setPosition(x, y);
		mArc.setPosition(x + 35, y - 25);
		mArrow.setPosition(x + 60, y + 63);
		mRope.setPosition(x + 60, y + 63);
	}
	
	public void Move() {
		
		mArrow.Move(mGameData.sShoted, mGameData.getAngulo(),
				mGameData.getForca(), mGameData.mDirecao);
		mRope.Move(mGameData.getAngulo(), mGameData.getDistance(),mGameData.mDirecao);
		mArc.Move(mGameData.getAngulo(), mGameData.getForca(),mGameData.mDirecao);
		
	}
	
	@Override
	public void Draw() {
		// TODO Auto-generated method stub
		super.Draw();
		mArc.Draw();
		mArrow.Draw();
		mRope.Draw();
	}
	
	public void flipHorizontal1(int direction)
	{
		mArc.flipHorizontal(direction);
		mArrow.flipHorizontal(direction);
		mRope.flipHorizontal(direction);
		this.flipHorizontal(direction);
	}
	public void Destroy(Scene s)
	{
		s.detachChild(mArc.getSprite());
		s.detachChild(mArrow.getSprite());
		s.detachChild(mRope.getSprite());
		s.detachChild(getSprite());
	}
}