package tatu.bowshield.bluetooth;

import tatu.bowshield.sprites.Arrow;

public interface OnDirectionChanged {
    public void onDirectionChanged();

    public void onTurnDone();

    public void onArrowOutofScreen(int type);
}
