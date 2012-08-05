/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tatu.bowshield.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import tatu.bowshield.Util.DebugLog;
import tatu.bowshield.activity.DeviceListActivity;
import tatu.bowshield.control.IOnBluetoothConnectListener;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for incoming
 * connections, a thread for connecting with a device, and a thread for
 * performing data transmissions when connected.
 */
public class BluetoothChatService {

	private static final String NAME = "BluetoothChat";

	private static final UUID MY_UUID = UUID
			.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

	private final BluetoothAdapter mAdapter;
	// private IOnBluetoothConnectListener connectionListener;
	private AcceptThread mAcceptThread;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private int mState;

	public static final int STATE_NONE = 0;
	public static final int STATE_LISTEN = 1;
	public static final int STATE_CONNECTING = 2;
	public static final int STATE_CONNECTED = 3;

	OnMessageReceivedListener listener;
	static BluetoothChatService instance;
	private Handler mHandler;

	private BluetoothChatService(BluetoothAdapter adapter) {
		mAdapter = adapter;
		mState = STATE_NONE;
	}

	public static BluetoothChatService getInstance(BluetoothAdapter adapter) {
		if (instance == null) {
			instance = new BluetoothChatService(adapter);
		}
		return instance;
	}

	public void setOnMenssageReceivedListener(OnMessageReceivedListener listener) {
		this.listener = listener;
	}

	// public void setListener(IOnBluetoothConnectListener listener) {
	// this.connectionListener = listener;
	// }

	public void setHandler(Handler handler) {
		this.mHandler = handler;
	}

	public synchronized int getState() {
		return mState;
	}

	public synchronized void start() {

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		if (mAcceptThread == null) {
			mAcceptThread = new AcceptThread();
			mAcceptThread.start();
		}

		mHandler.sendEmptyMessage(STATE_LISTEN);
	}

	public synchronized void connect(BluetoothDevice device) {

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		mHandler.sendEmptyMessage(STATE_CONNECTING);
	}

	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device) {

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}

		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		mHandler.sendEmptyMessage(STATE_CONNECTED);
	}

	public synchronized void stop() {
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
	}

	public void write(String message) {
		ConnectedThread r;
		synchronized (this) {
			r = mConnectedThread;
		}
		byte[] mBytes = message.getBytes();
		
		try {
			r.write(mBytes);
		} catch (Exception e) {

		}
	}

	private void connectionFailed() {
		mHandler.sendEmptyMessage(STATE_NONE);
	}

	private void connectionLost() {
		mHandler.sendEmptyMessage(STATE_NONE);
	}

	private class AcceptThread extends Thread {

		private final BluetoothServerSocket mmServerSocket;

		public AcceptThread() {

			BluetoothServerSocket tmp = null;

			try {
				tmp = mAdapter
						.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mmServerSocket = tmp;
		}

		public void run() {

			BluetoothSocket socket = null;

			while (mState != STATE_CONNECTED) {
				try {
					DebugLog.log("Accepting...");
					socket = mmServerSocket.accept();
					DebugLog.log("Accepted!!");
				} catch (Exception e) {
					DebugLog.log("Failed: " + e.getMessage());
					break;
				}

				connected(socket, socket.getRemoteDevice());
			}
		}

		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * This thread runs while attempting to make an outgoing connection with a
	 * device. It runs straight through; the connection either succeeds or
	 * fails.
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;

			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
			}
			mmSocket = tmp;
		}

		public void run() {
			mAdapter.cancelDiscovery();

			try {
				DebugLog.log("Connecting...");
				mmSocket.connect();
				DebugLog.log("Connected!");
			} catch (Exception e) {
				DebugLog.log("Error connecting: " + e.getMessage());
				connectionFailed();
				try {
					mmSocket.close();
				} catch (IOException e2) {
				}
				return;
			}

			synchronized (BluetoothChatService.this) {
				mConnectThread = null;
			}

			connected(mmSocket, mmDevice);
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private class ConnectedThread extends Thread {

		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the BluetoothSocket input and output streams
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024];
			int bytes;

			// Keep listening to the InputStream while connected
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.read(buffer);

					// Send the obtained bytes to the UI Activity
					// mHandler.obtainMessage(BluetoothChat.MESSAGE_READ, bytes,
					// -1, buffer)
					// .sendToTarget();
					byte[] readBuf = (byte[]) buffer;
					// construct a string from the valid bytes in the buffer
					String readMessage = new String(readBuf, 0, bytes);
					listener.onMessageReceived(readMessage);

				} catch (IOException e) {
					connectionLost();
					break;
				}
			}
		}

		public void write(byte[] buffer) {
			try {
				mmOutStream.write(buffer);
			} catch (IOException e) {
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}
}
