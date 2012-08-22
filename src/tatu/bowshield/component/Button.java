package tatu.bowshield.component;

import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.activity.BowShieldGameActivity;

public class Button extends GameButtom {

    private int     mId;
    private boolean active;

    public Button(BowShieldGameActivity reference, String filepath, String filepath2, float X, float Y, int id) {
        super(reference, filepath, filepath2, X, Y);
        this.mId = id;
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean onTouchEvent(TouchEvent pSceneTouchEvent) {
        return super.onTouchEvent(pSceneTouchEvent);
    }

    public int getId() {
        return mId;
    }

}
