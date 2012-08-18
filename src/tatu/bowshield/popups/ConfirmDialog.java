package tatu.bowshield.popups;

import java.util.EventListener;

import org.andengine.entity.sprite.Sprite;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.component.Button;
import tatu.bowshield.component.ButtonManager;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.component.PopUpLayout;
import tatu.bowshield.control.GamePhysicalData;
import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.sprites.GameSprite;
import org.andengine.input.touch.TouchEvent;

import android.view.KeyEvent;

import tatu.bowshield.control.Constants;

public class ConfirmDialog extends PopUpLayout implements IOnButtonTouch {

    private String    PATH_BUTTON         = "gfx/buttonn.png";
    private String    PATH_BUTTON_PRESSED = "gfx/buttonp.png";

    private final int BTN_YES             = 0;
    private final int BTN_NO              = 1;

    Button            btnYes;
    Button            btnNo;
    ButtonManager     bManager;

    GameSprite        sprite;

    public ConfirmDialog(int width, int heigth) {
        btnYes = new Button(PATH_BUTTON, PATH_BUTTON_PRESSED, 167, 270, BTN_YES);
        btnNo = new Button(PATH_BUTTON, PATH_BUTTON_PRESSED, 413, 270, BTN_NO);

        WIDTH = width;
        HEIGHT = heigth;

        sprite = new GameSprite("gfx/texto.png", 257, 120);

        bManager = new ButtonManager(this);
        bManager.addButton(btnYes);
        bManager.addButton(btnNo);
    }

    @Override
    public void onButtonTouch(int buttonId) {
        // TODO Auto-generated method stub
        switch (buttonId) {

            case BTN_YES:
                PopUp.sendResult(Constants.RESULT_YES);
                break;

            case BTN_NO:
                PopUp.sendResult(Constants.RESULT_NO);
                break;

        }
    }

    @Override
    public void onDraw() {
        // TODO Auto-generated method stub
        bManager.drawButtons();
        sprite.Draw();
    }

    @Override
    public void Update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        bManager.detach();
        GameSprite.getGameReference().getScene().detachChild(sprite.getSprite());
    }

    @Override
    public void TouchEvent(TouchEvent event) {
        // TODO Auto-generated method stub
        bManager.updateButtons(event);
    }

    @Override
    public void onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

    }

}
