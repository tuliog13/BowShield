package tatu.bowshield.screens;

import tatu.bowshield.component.PopUp;
import tatu.bowshield.control.Constants;
import tatu.bowshield.control.OnPopupResult;
import tatu.bowshield.control.ScreenManager;
import tatu.bowshield.popups.ConfirmDialog;
import tatu.bowshield.sprites.GameSprite;
import android.view.KeyEvent;

public class Results extends SimpleScreen implements OnPopupResult{

	private int id;
	

	public Results(int id, String filepath) {
		super(id, filepath);
		// TODO Auto-generated constructor stub
		
		PopUp.setListener(this);
		
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
		
		
	}
	
	@Override
	public void Update() {
		// TODO Auto-generated method stub
		super.Update();
		
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		PopUp.showPopUp(new ConfirmDialog(700, 350));
		PopUp.bringToFront();
		
		return false;
	}
	
}
