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

import tatu.bowshield.util.DebugLog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

/**
 * This class does all the work for setting up and managing Bluetooth connections with other devices. It has a thread
 * that listens for incoming connections, a thread for connecting with a device, and a thread for performing data
 * transmissions when connected.
 */
public class BluetoothService {

    private static final String    NAME             = "BluetoothChat";

    private static final UUID      MY_UUID          = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    private final BluetoothAdapter mAdapter;
    // private IOnBluetoothConnectListener connectionListener;
    private AcceptThread           mAcceptThread;
    private ConnectThread          mConnectThread;
    private ConnectedThread        mConnectedThread;
    private int                    mState;

    public static final int        STATE_LOST       = 0;
    public static final int        STATE_LISTEN     = 1;
    public static final int        STATE_CONNECTING = 2;
    public static final int        STATE_CONNECTED  = 3;

    /* ADD HERE TYPE OF BLUETOOTH MENSSAGE */
    public static final byte       SHOT             = 0;
    public static final byte       MOVE_PLAYER      = 1;
    public static final byte       CONFIRM_RECIEVE  = 127;

    OnMessageReceivedListener      listener;
    static BluetoothService        instance;
    private Handler                mHandler;
    private boolean                readyToSend      = true;

    private BluetoothService(BluetoothAdapter adapter) {
        mAdapter = adapter;
        mState = STATE_LOST;
    }

    public static BluetoothService getInstance(BluetoothAdapter adapter) {
        if (instance == null) {
            instance = new BluetoothService(adapter);
        }
        return instance;
    }

    public void setOnMenssageReceivedListener(OnMessageReceivedListener listener) {
        this.listener = listener;
    }

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

        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

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

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {

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

    private void connectionFailed() {
        mHandler.sendEmptyMessage(STATE_LOST);
    }

    private void connectionLost() {
        mHandler.sendEmptyMessage(STATE_LOST);
    }

    private class AcceptThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {

            BluetoothServerSocket tmp = null;

            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
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

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mAdapter.cancelDiscovery();

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(STATE_LOST);
            }
            mmSocket = tmp;
        }

        public void run() {

            try {
                mmSocket.connect();
            } catch (Exception e) {
                DebugLog.log("Error connecting: " + e.getMessage());
                connectionFailed();
                return;
            }

            synchronized (BluetoothService.this) {
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
        private final InputStream     mmInStream;
        private final OutputStream    mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {

            byte type;
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {

                    type = (byte) mmInStream.read();

                    // if (type != CONFIRM_RECIEVE) {
                    bytes = mmInStream.read(buffer);

                    byte[] readBuf = (byte[]) buffer;
                    String readMessage = new String(readBuf, 0, bytes);

                    // sendType(CONFIRM_RECIEVE);
                    listener.onMessageReceived(type, readMessage);
                    // } else {
                    // readyToSend = true;
                    // DebugLog.log("Confirm recevide: ready to send");
                    // }

                } catch (IOException e) {
                    connectionLost();
                    break;
                }
            }
        }

        public void sendMenssage(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void sendType(byte buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            // try {
            // mmSocket.close();
            // } catch (IOException e) {
            // }
        }
    }

    public void write(byte type, String message) {
        ConnectedThread r;
        synchronized (this) {
            r = mConnectedThread;
        }

        // if (readyToSend) {

        byte[] mBytes = message.getBytes();
        readyToSend = false;

        try {
            r.sendType(type);
            r.sendMenssage(mBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // DebugLog.log("Sended: Type: " + type + " Message: " + message + " - Send blocked!");
        // } else {
        // DebugLog.log("Failed to send: Type: " + type + " Message: " + message + " - Send not done yet!");
        // }

    }
}
