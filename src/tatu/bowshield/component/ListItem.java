package tatu.bowshield.component;

import org.andengine.entity.text.Text;

import tatu.bowshield.activity.BowShieldGameActivity;

public class ListItem extends Button {

    private String mText;
    private Object mContainer;
    private Text   mTextSprite;

    public ListItem(BowShieldGameActivity reference, String text, String path1, String path2, float x, float y, int position) {
        super(reference, path1, path2, x, y, position);
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
