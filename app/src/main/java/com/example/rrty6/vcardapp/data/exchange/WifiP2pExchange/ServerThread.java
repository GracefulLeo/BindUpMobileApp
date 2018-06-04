package com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange;

import com.example.rrty6.vcardapp.data.storage.model.Card;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {
    private int mPort = 0;

    private ServerSocket serverSocket = null;
    private Socket socket = null;

    Card sendObject = null;
    Card receiveObject = null;

    OutputStream mOutputStream = null;
    InputStream mInputStream = null;
    private boolean succes = false;

    //the data transfer activity passes port for use in the thread
    public ServerThread(int port, Card card) {
        sendObject = card;
        mPort = port;
    }

    //this is called when the thread is kicked off by the data transfer display
    @Override
    public void run() {
        if (mPort != 0) {
            //good to go, infinite loop to blast packets non stop
            while (!succes) {
                //this try block creates a new socket using the given port number
                //it only runs in first iteration of the loop when socket is null
                try {
                    if (socket == null) {
                        serverSocket = new ServerSocket(mPort);
                        socket = serverSocket.accept();
                    }
                    mOutputStream = socket.getOutputStream();
                    mInputStream = socket.getInputStream();

                    ObjectOutputStream oos = new ObjectOutputStream(mOutputStream);
                    oos.writeObject(sendObject);

                    ObjectInputStream ois = new ObjectInputStream(mInputStream);
                    receiveObject = (Card) ois.readObject();

                    if (receiveObject != null) {
                        succes = true;
                        ois.close();
                        oos.close();
                        mInputStream.close();
                        mOutputStream.close();
                        socket.close();
                        serverSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Card getReceiveObject() {
        return receiveObject;
    }
}
