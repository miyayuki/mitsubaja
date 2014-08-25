/*-------------------------
 * ChatServerThread.java
 *
 * written by Miyuki Ozawa
 * ------------------------*/

import java.net.Socket;
import java.io.*;
import java.util.*;

public class ChatServerThread extends Thread{
	public ChatServerThread(Socket socket, MitsubajaServerThread server){
		_socket = socket;
		_server = server;
		_sID = null;
		_isExiting = false;
		_isKeptAlive = true;
	}
	public void checkAlive() {

		if (!_isKeptAlive) {
			try {
				_socket.close(); // forcible termination.

			} catch (IOException unsued) {
			}

		} else {
			_isKeptAlive = false;
			send("K." + _sID);
		}
	}
	public void send(Packet packet){
		send(packet.getString());
	}
	public synchronized void send(String sLine){
		_writer.println(sLine);
	}
	public void run(){
	
		BufferedReader reader = null;
		_writer = null;
	
		try{
			_writer = new PrintWriter(new OutputStreamWriter(_socket.getOutputStream(), "UTF-8"), true);
			reader = new BufferedReader(new InputStreamReader(_socket.getInputStream(), "UTF-8"));

			String sLine;

LOOP:
			while((sLine = reader.readLine()) != null){
				Packet packet = new Packet(sLine);
				switch(packet.getType()){
					case 'M':
						message(packet);
						break;

					case 'P':
						presence(packet);
						break;

					case 'J':
						join(packet);
						break;

					case 'L':
						leave(packet);
						_isExiting = true;
						break LOOP;

					case 'K':
						_isKeptAlive = true;
						break;

					case 'E':
					default:
						error(packet);
						break LOOP;
				}
			}

		} catch (IOException e){
			System.out.println("I/O error for " + (_sID == null ? "???" : _sID));

		} finally {
		
			if (!_isExiting) {
				System.out.println("connection with " + (_sID == null ? "???" : _sID)
				+ " reset by the client.");
				if (_sID != null) {
					leave(new Packet("L." + _sID));
				}
			}
		
			if (_writer != null) {
				_writer.close();
			}
			if (reader != null) {
				try {
					reader.close();
	
				} catch (IOException unused) {
				}
			}

			try {
				_socket.close();

			} catch (IOException unused) {
			}
		}
	}
	private void broadcast(String sLine){
		for(Enumeration e = _server.getAccounts(); e.hasMoreElements(); ){
			Account account = (Account)e.nextElement();
			System.out.println(account.getID());
			ChatServerThread thread = account.getChatServerThread();
			if(thread != this){
				thread.send(sLine);
			}
		}
	}
	private void error(Packet packet){
		String sID = packet.getFrom();
		send("E.1."+sID+".ERROR");
	}
	private void join(Packet packet){
		_sID = packet.getFrom();
		if(_server.getAccount(_sID)==null){
			_server.putAccount(_sID,new Account(_sID, this));
			send("P." + _sID + ".");
			for(Enumeration e=_server.getAccounts(); e.hasMoreElements(); ){
				Account account = (Account)e.nextElement();
				if (!account.getID().equals(_sID)) {
					send("P."+account.getID()+"."+account.getStatus());
				}
			}
			//broadcast("P."+sID+".joined to mitsubaja");
			broadcast("P."+_sID);
			System.out.println(_sID+" JOINED to mitsubaja\n");
		}else{
			send("E.0."+_sID+".Your ID is incorrect");
			System.out.println(_sID+" INCORRECT ID\n");
			_sID = null;
		}
	}
	private void leave(Packet packet){
		String sID = packet.getFrom();
		if(_server.getAccount(sID)!=null){
			_server.removeAccount(sID);
			//broadcast("P."+sID+". leaved from mitsubaja");
			broadcast("L."+sID);
			System.out.println(sID+" LEFT from mitsubaja\n");
		}else{
			send("E.0."+sID+".Failed to logout");
		}
	}
	private void message(Packet packet){
		Account account = _server.getAccount(packet.getTo());
		if(account == null){
			send("E.2."+packet.getFrom()+".unknown user");
		}else{
			account.getChatServerThread().send(packet);
			System.out.println(packet.getFrom()+" is SEND message to "+packet.getTo()+"\n");
		}
	}
	private void presence(Packet packet){
		String sID = packet.getFrom();
		String sStatus = packet.getStatus();
		Account account = _server.getAccount(sID);
		account.setStatus(sStatus);

		broadcast("P."+sID+"."+sStatus);
		System.out.println(sID+" is"+sStatus+"\n");
	}
	private Socket _socket;
	private MitsubajaServerThread _server;
	private PrintWriter _writer;

	private String _sID;

	private boolean _isExiting;
	private boolean _isKeptAlive;
}
/* end of ChatServerThread.java */
