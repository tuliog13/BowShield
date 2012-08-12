package tatu.bowshield.bluetooth;

public interface OnMessageReceivedListener {
    public void sendMessage(byte type, String message);
	public void onMessageReceived(byte type, String message);
}
