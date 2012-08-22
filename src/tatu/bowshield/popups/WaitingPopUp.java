package tatu.bowshield.popups;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.component.PopUpLayout;
import tatu.bowshield.sprites.GameSprite;
import android.view.KeyEvent;

public class WaitingPopUp extends PopUpLayout {

    private Text   mWatingText;
    // private Text mConnectingText;
    private Font   mTextFont;
    private Scene  mScene;
    public boolean attached = false;

    public WaitingPopUp(BowShieldGameActivity reference) {
        WIDTH = 700;
        HEIGHT = 380;

        BitmapTextureAtlas mTextFontTextureAtlas = new BitmapTextureAtlas(reference.getTextureManager(), 512, 512,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mTextFont = FontFactory.create(reference.getFontManager(), mTextFontTextureAtlas, 32);

        reference.getEngine().getFontManager().loadFont(mTextFont);

        reference.getEngine().getTextureManager().loadTexture(mTextFontTextureAtlas);

        mWatingText = new Text(100, 350, mTextFont, "Jogo criado, aguardando oponente.",
                reference.getVertexBufferObjectManager());

        // mConnectingText = new Text(300, 300, mTextFont,
        // "Alguem parece estar chegando...", GameSprite
        // .getGameReference().getVertexBufferObjectManager());
    }

    @Override
    public void onDraw() {
        if (!attached) {
            try {
                mScene.attachChild(mWatingText);
                attached = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void Update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void TouchEvent(org.andengine.input.touch.TouchEvent event) {
        // TODO Auto-generated method stub

    }

    public void setConnecting() {
        try {
            mScene.detachChild(mWatingText);
            // mScene.attachChild(mConnectingText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Destroy() {
        try {
            mScene.detachChild(mWatingText);
            // mScene.detachChild(mConnectingText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        PopUp.hidePopUp();
    }

}
