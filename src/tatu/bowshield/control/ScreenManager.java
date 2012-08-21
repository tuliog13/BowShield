package tatu.bowshield.control;

import java.util.ArrayList;
import java.util.List;

import org.andengine.input.touch.TouchEvent;

import tatu.bowshield.screens.Screen;
import tatu.bowshield.screens.SimpleScreen;
import android.view.KeyEvent;

public class ScreenManager {

    private static List<Screen> _screens;
    private static Screen       _currentScreen;

    public static void addScreen(Screen screen) {
        if (_screens == null)// Initialize
        {
            _screens = new ArrayList<Screen>();

        }

        _screens.add(screen);
    }

    public static void changeScreen(int idScreen) {

        if (_currentScreen != null) {
            DestroySimpleScreens();
            _currentScreen.Destroy();
        }

        _currentScreen = getScreen(idScreen);
        _currentScreen.Initialize();
        _currentScreen.Load();
        _currentScreen.Draw();
    }

    public static void showSimpleScreen(int id) {
        for (SimpleScreen screen : _currentScreen.get_simpleScreens()) {
            if (screen.getId() == id) {
                screen.Show();
            }
        }
    }

    public static void hideSimpleScreen(int id) {
        for (SimpleScreen screen : _currentScreen.get_simpleScreens()) {
            if (screen.getId() == id) {
                screen.Hide();
            }
        }
    }

    public static void Update() {

        _currentScreen.Update();
        if (_currentScreen.get_simpleScreens() != null) {
            for (SimpleScreen screen : _currentScreen.get_simpleScreens()) {
                screen.Update();
            }
        }

    }

    public static void reDraw() {
        _currentScreen.Draw();
    }

    private static Screen getScreen(int id) {
        for (Screen screen : _screens) {
            if (screen.getId() == id) {
                return screen;
            }
        }
        return null;
    }

    public static Screen getCurrentScreen() {
        return _currentScreen;
    }

    public static void onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        // TODO Auto-generated method stub

        if (!hasASimpleScreenActived()) {
            _currentScreen.onSceneTouchEvent(pSceneTouchEvent);
        } else {
            for (SimpleScreen screen : _currentScreen.get_simpleScreens()) {
                if (screen.isShowing()) {
                    screen.onSceneTouchEvent(pSceneTouchEvent);
                }
            }
        }

    }

    public static void DestroySimpleScreens() {
        if (_currentScreen.get_simpleScreens() != null) {
            for (SimpleScreen screen : _currentScreen.get_simpleScreens()) {
                screen.Destroy();
            }
        }
        _currentScreen.set_simpleScreens(null);
    }

    public static boolean hasASimpleScreenActived() {
        if (_currentScreen.get_simpleScreens() != null) {
            for (SimpleScreen screen : _currentScreen.get_simpleScreens()) {
                if (screen.isShowing()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (!hasASimpleScreenActived()) {
            _currentScreen.onKeyDown(keyCode, event);
        } else {
            for (SimpleScreen screen : _currentScreen.get_simpleScreens()) {
                screen.onKeyDown(keyCode, event);
            }
        }
    }

}
