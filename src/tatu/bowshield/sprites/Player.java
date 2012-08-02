package tatu.bowshield.sprites;

import org.andengine.entity.scene.Scene;

import tatu.bowshield.bluetooth.OnDirectionChanged;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.screens.Game;

public class Player extends GameSprite{

	private Arc mArc;
	private Arrow mArrow;
	private Rope mRope;
	
	private int mLife;
	
	public Player(final String filepath,float X, float Y) {
		super(filepath,X,Y);

		mArc = new Arc(Game.PATH_ARC, X + 35, Y - 25);
		mArrow = new Arrow(Game.PATH_ARROW, X + 60, Y + 63);
		mRope = new Rope(Game.PATH_ROPE, X + 60, Y + 63);
		
	}

	public void Move() {
		mArrow.Move(GamePhysicalData.sShoted, GamePhysicalData.getAngulo(),
				GamePhysicalData.getForca(), GamePhysicalData.mDirecao);
		mRope.Move(GamePhysicalData.getAngulo(), GamePhysicalData.getDistance());
		mArc.Move(GamePhysicalData.getAngulo(), GamePhysicalData.getForca());
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