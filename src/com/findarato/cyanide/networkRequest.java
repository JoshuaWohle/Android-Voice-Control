package com.findarato.cyanide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class networkRequest extends AsyncTask<URL, Integer, Long> {

	public static Socket socket = null;
	public static ObjectOutputStream objectOutputStream = null;
	public static ObjectInputStream objectInputStream = null;
	Boolean commanderThreadRunning = false;
	String command = null;
	String serverIP = null;
	int port = 0;
	boolean stop = false;
	
	public networkRequest(String serverIP, int port, String command) {
		super();
		this.serverIP = serverIP;
		this.port = port;
		this.command = command;
	}
	
	public networkRequest(String serverIP, int port, String command, boolean stop) {
		super();
		this.serverIP = serverIP;
		this.port = port;
		this.command = command;
		this.stop = stop;
	}

	@Override
	protected Long doInBackground(URL... params) {

		try {
			if(socket == null) {
				socket = new Socket(this.serverIP, this.port);
				objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				objectInputStream = new ObjectInputStream(socket.getInputStream());
			}
			objectOutputStream.writeObject(this.command);
		} catch(UnknownHostException e) {
			Log.v("socket", "Uknown host exception");
			e.printStackTrace();
		} catch (IOException e) {
			Log.v("socket", "IO exception");
			e.printStackTrace();
		}
		
		if(stop)
			closeConnection();
		
		return null;
	}
	
	public static void closeConnection() {
		try {
			objectOutputStream.writeObject("DISCONNECT");
			Thread.sleep(1000);
			socket.close();
			objectOutputStream.close();
			objectInputStream.close();
			socket = null;
			objectInputStream = null;
			objectOutputStream = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
