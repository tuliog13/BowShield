package tatu.bowshield.component;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;

import tatu.bowshield.sprites.GameSprite;

public class ListItem extends GameSprite {

	private String mText;
	private Object mContainer;
	private Text mTextSprite;

	public ListItem(String filepath, float X, float Y) {
		super(filepath, X, Y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Draw() {
		super.Draw();
		
		Scene scene = GameSprite.getGameReference().getScene();

		try {
			scene.attachChild(mTextSprite);
		} catch (Exception e) {
		}
	}
	
	public String getText() {
		return mText;
	}

	public void setText(String text) {
		this.mText = text;
	}

	public Object getContainer() {
		return mContainer;
	}

	public void setContainer(Object container) {
		this.mContainer = container;
	}

	public Text getTextSprite() {
		return mTextSprite;
	}

	public void setTextSprite(Text textSprite) {
		this.mTextSprite = textSprite;
	}
	
	

}
