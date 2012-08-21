package tatu.bowshield.component;

import org.andengine.input.touch.TouchEvent;

import android.view.KeyEvent;

public abstract class PopUpLayout {

    protected int WIDTH;
    protected int HEIGHT;

    public abstract void onDraw();

    public abstract void Update();

    public abstract void TouchEvent(TouchEvent event);

    public abstract void Destroy();

    public abstract void onKeyDown(int keyCode, KeyEvent event);

}
