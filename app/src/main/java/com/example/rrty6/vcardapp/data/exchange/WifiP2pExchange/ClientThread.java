package com.example.rrty6.vcardapp.data.exchange.WifiP2pExchange;

import com.example.rrty6.vcardapp.data.storage.model.Card;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientThread implements Runnable {
    private InetAddress mHostAddress;
    private int mPort = 0;

    private Socket socket;

    private boolean succes = false;

    Card sendObject = null;
    Card receiveObject = null;

    //the data transfer activity passes the address of the group host and port for use in the thread
    public ClientThread(InetAddress hostAddress, int port, Card card) {
        sendObject = card;
        mHostAddress = hostAddress;
        mPort = port;
    }

    //this is called when the thread is kicked off by the data transfer display
    @Override
    public void run() {
        if (mHostAddress != null && mPort != 0) {
            //good to go, infinite loop to blast packets non stop
            while (!succes) {
                //this try block creates a new socket using the given port number
                //it only runs in first iteration of the loop when socket is null
                try {
                    if (socket == null) {
                        socket = new Socket(mHostAddress, mPort);
                    }
                    System.out.println("Start sending Client thread");
                    OutputStream mOutputStream = socket.getOutputStream();
                    InputStream mInputStream = socket.getInputStream();

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
