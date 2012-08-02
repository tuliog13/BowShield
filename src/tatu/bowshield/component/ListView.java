package tatu.bowshield.component;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.util.color.Color;

import tatu.bowshield.sprites.GameSprite;

public class ListView {

	private String PATH_ITEM = "gfx/itemBG.png";
	private String PATH_FONT = "gfx/Arial.TTF";

	private List<ListItem> mItems;
	private int mX;
	private int mItemHeight;
	private Font mTextFont;

	private OnListItemClickListener mListener;

	public ListView(int x, int y) {

		BitmapTextureAtlas mTextFontTextureAtlas = new BitmapTextureAtlas(
				GameSprite.getGameReference().getTextureManager(), 512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTextFont = FontFactory.create(GameSprite.getGameReference()
				.getFontManager(), mTextFontTextureAtlas, 32);

		GameSprite.getGameReference().getEngine().getFontManager()
				.loadFont(mTextFont);

		GameSprite.getGameReference().getEngine().getTextureManager()
				.loadTexture(mTextFontTextureAtlas);

		ListItem emptyItem = new ListItem(PATH_ITEM, x, y);
		mItemHeight = emptyItem.getHeight();
		mItems = new ArrayList<ListItem>();

		this.mX = x;
	}

	public void addItem(String text, Object container) {
		int y, count = mItems.size();
		y = count * mItemHeight;

		ListItem newItem = new ListItem(PATH_ITEM, mX, y);
		newItem.setText(text);
		newItem.setContainer(container);

		Text mText = new Text(mX, y, mTextFont, text, GameSprite
				.getGameReference().getVertexBufferObjectManager());
		newItem.setTextSprite(mText);

		mItems.add(newItem);
	}

	public void clear() {
		detach();
		mItems.clear();
	}

	public void updateItems(TouchEvent event) {

		float x = event.getX();
		float y = event.getY();
		
		switch (event.getAction()) {
		case TouchEvent.ACTION_DOWN:
			break;

		case TouchEvent.ACTION_MOVE:
			break;
		case TouchEvent.ACTION_UP:
			
			for (int i = 0; i < mItems.size(); i++) {
				ListItem item = mItems.get(i);

				if ((x >= item.getX() && x <= item.getX() + item.getWidth())
						&& (y >= item.getY() && y <= item.getY() + item.getHeight())) {
					mListener.onItemClick(i, item.getContainer());
				}
			}
			
			break;
		}

		
	}

	public void draw() {
		for (int i = 0; i < mItems.size(); i++) {
			mItems.get(i).Draw();
		}
	}

	public void setOnListItemClickListener(OnListItemClickListener listener) {
		mListener = listener;
	}

	public void detach() {
		for (int i = 0; i < mItems.size(); i++) {
			GameSprite.getGameReference().getScene()
					.detachChild(mItems.get(i).getSprite());

			GameSprite.getGameReference().getScene()
					.detachChild(mItems.get(i).getTextSprite());
		}
	}
}
