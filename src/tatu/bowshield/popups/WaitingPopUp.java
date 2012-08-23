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
import android.graphics.Color;
import android.view.KeyEvent;

public class WaitingPopUp extends PopUpLayout {

    private String PATH_FONT = "gfx/lithos.otf";

    private Text   mWatingText;
    private Font   mTextFont;
    private Scene  mScene;
    public boolean attached  = false;

    public WaitingPopUp(BowShieldGameActivity reference) {
        
        WIDTH = 700;
        HEIGHT = 380;

        mTextFont = FontFactory.createFromAsset(reference.getFontManager(), reference.getTextureManager(), 300, 200,
                reference.getAssets(), PATH_FONT, 32, true, Color.rgb(84, 56, 20));

        reference.getEngine().getFontManager().loadFont(mTextFont);
        mTextFont.load();

        mWatingText = new Text(190, 85, mTextFont, "Aguardando jogadores...", reference.getVertexBufferObjectManager());

        mScene = reference.getScene();
    }

    @Override
    public void onDraw() {
        try {
            mScene.attachChild(mWatingText);
        } catch (Exception e) {
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
        PopUp.hidePopUp();
    }

}
