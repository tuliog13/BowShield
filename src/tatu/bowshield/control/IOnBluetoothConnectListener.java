package tatu.bowshield.control;

public interface IOnBluetoothConnectListener {
	public void onConnected();
	public void onConnecting();
	public void onConnectionLost();
	public void onWaitingConnection();
}
