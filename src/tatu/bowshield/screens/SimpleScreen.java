package tatu.bowshield.screens;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.sprites.GameSprite;
import android.view.KeyEvent;

public class SimpleScreen extends Screen {

    private GameSprite backgroundImage;
    protected int      currentAlpha;
    private int        id;

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean isShowing) {
        this.isShowing = isShowing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private boolean isShowing;

    public SimpleScreen(BowShieldGameActivity reference, int id) {
        super(reference, id);
        // TODO Auto-generated constructor stub
    }

    public SimpleScreen(BowShieldGameActivity reference, int id, String filepath) {
        super(reference, id + 100);
        setId(id);

        currentAlpha = 250;
        isShowing = false;

        backgroundImage = new GameSprite(mReference, filepath, 0, 0);
        backgroundImage.getSprite().setAlpha(currentAlpha);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        ScreenManager.hideSimpleScreen(this.id);

        return super.onKeyDown(keyCode, event);
    }

    public void Show() {
        isShowing = true;
        mReference.getScene().attachChild(backgroundImage.getSprite());
    }

    public void Destroy() {
        backgroundImage.getSprite().setVisible(false);
        backgroundImage.getSprite().detachSelf();
    }

    public void Hide() {
        isShowing = false;
    }

    @Override
    public void Update() {
        // TODO Auto-generated method stub
        super.Update();

        if (isShowing) {
            if (currentAlpha > 10) {
                currentAlpha -= 9;
                backgroundImage.getSprite().setAlpha(currentAlpha);
            } else {
                backgroundImage.getSprite().setAlpha(currentAlpha);
                currentAlpha = 1;
            }
        } else {
            if (currentAlpha < 250) {
                currentAlpha += 9;
                backgroundImage.getSprite().setAlpha(currentAlpha);
            } else {
                mReference.getScene().detachChild(backgroundImage.getSprite());
                if (ScreenManager.getCurrentScreen().hasSimpleScreens()) {
                    for (SimpleScreen screen : ScreenManager.getCurrentScreen().get_simpleScreens()) {
                        screen.onCallChildrenDetach();
                    }
                }
            }
        }

    }

    public void onCallChildrenDetach() {
        // TODO Auto-generated method stub

    }
}
