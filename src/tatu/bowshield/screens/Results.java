package tatu.bowshield.screens;

import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.activity.BowShieldGameActivity;
import tatu.bowshield.component.Button;
import tatu.bowshield.component.ButtonManager;
import tatu.bowshield.component.PopUp;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.IOnButtonTouch;
import tatu.bowshield.control.OnPopupResult;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.popups.ConfirmDialog;
import android.view.KeyEvent;

public class Results extends SimpleScreen implements OnPopupResult, IOnButtonTouch {

    private int   id;

    Button        btnPlayAgain;
    Button        btnBackToMenu;

    ButtonManager bManager;

    public Results(BowShieldGameActivity reference, int id, String filepath) {
        super(reference, id, filepath);
        // TODO Auto-generated constructor stub

        PopUp.setListener(this);

        btnPlayAgain = new Button(mReference, "gfx/buttons/create_normal.png", "gfx/buttons/create_pressed.png", 170, 288, 0);
        btnBackToMenu = new Button(mReference, "gfx/buttons/create_normal.png", "gfx/buttons/create_pressed.png", 420, 288, 1);

        bManager = new ButtonManager(mReference, this);
        bManager.addButton(btnPlayAgain);
        bManager.addButton(btnBackToMenu);

        // IMPORTANTE : PARA ADICIONAR COMPONENTES EM UMA SIMPLESCREEN
        // É PRECISO SETAR O ALPHA DA SPRITE NO UPDATE COM O ALPHA DO PAI

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void Show() {
        // TODO Auto-generated method stub
        super.Show();
        bManager.drawButtons();

    }

    @Override
    public void Update() {
        // TODO Auto-generated method stub
        super.Update();

        btnBackToMenu.getSprite().setAlpha(currentAlpha);
        btnPlayAgain.getSprite().setAlpha(currentAlpha);

    }

    @Override
    public void onResultReceived(int result) {
        // TODO Auto-generated method stub

        switch (result) {
            case Constants.RESULT_YES:
                PopUp.forceClose();
                ScreenManager.changeScreen(1);
                break;

            case Constants.RESULT_NO:
                PopUp.hidePopUp();
                break;
        }

    }

    @Override
    public void onCallChildrenDetach() {
        // TODO Auto-generated method stub
        super.onCallChildrenDetach();

        bManager.detach();

    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        // TODO Auto-generated method stub

        bManager.updateButtons(pSceneTouchEvent);

        return super.onSceneTouchEvent(pSceneTouchEvent);

    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
        super.Destroy();

        bManager.detach();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        PopUp.showPopUp(new ConfirmDialog(mReference, 700, 350));
        PopUp.bringToFront();

        return false;
    }

    @Override
    public void onButtonTouch(int buttonId) {
        // TODO Auto-generated method stub

        switch (buttonId) {

            case 0: // play angain

                ScreenManager.hideSimpleScreen(this.id);

                break;

            case 1: // Menu
                ScreenManager.changeScreen(1);
                break;

        }

    }

}
