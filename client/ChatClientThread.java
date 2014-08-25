/*---------------------------
 * ChatClientThread.java
 *
 * written by Miyuki Ozawa
 * ---------------------------*/

import java.net.Socket;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.Color;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class ChatClientThread extends Thread{

	public ChatClientThread(Socket socket, String sName, LoginWindow windowLogin){
		_socket = socket;
		_sName = sName;
		_windowLogin = windowLogin;
		_table = new Hashtable();
		_isExiting = false;
	}

	
	public void send(Packet packet){
		send(packet.getString());
	}
	
	public synchronized void send(String sLine){
	_writer.println(sLine);
	}

	public static String decode(String s) {
		
		String sNew = "";
		
		for (int i = 0; i < s.length(); i++) {
			
			switch (s.charAt(i)) {
					
				case '#':
					if (i >= s.length() - 2
						|| s.charAt(i + 1) != '#' || s.charAt(i + 2) != '!') {
						sNew += '.';
						
					} else {
						sNew += '#';
						i += 2;
					}
					break;

				default:
					sNew += s.charAt(i);
					break;
			}
		}
		
		return (sNew);
	}

	public static String encode(String s) {

		String sNew = "";
		
		for (int i = 0; i < s.length(); i++) {
			
			switch (s.charAt(i)) {
					
				case '.':
					sNew += '#';
					break;
					
				case '#':
					sNew += "##!";
					break;

				default:
					sNew += s.charAt(i);
					break;
			}
		}
		return (sNew);
	}

	public void run(){
	
		BufferedReader reader = null;
		_writer = null;
		boolean isClientOngoing = false;
	
		try{
			_writer = new PrintWriter(new OutputStreamWriter(_socket.getOutputStream(), "UTF-8"), true);
			reader = new BufferedReader(new InputStreamReader(_socket.getInputStream(), "UTF-8"));

			joinToServer();
			System.out.println("join success");
			String sLine;
			MemberList list = null;
			SerialComm serial = new SerialComm();

LOOP:
			while((sLine = reader.readLine()) != null){
				System.out.println(sLine);
				Packet packet = new Packet(sLine);
				switch(packet.getType()){

					case 'M':
						ChatWindow window = getChatWindow(packet.getFrom());
						window.setVisible(true);
						String msg = packet.getMessage();
						window.receiveMessage(msg);
						break;

					case 'P':
						if (list == null) {
							list = new MemberList(this, _sName, serial);
						}
						list.updateStatus(packet.getFrom(), packet.getStatus());
						break;

					case 'L':
						list.removeMember(packet.getFrom());
						break;
						
					case 'K':
						send(packet);
						break;

					case 'E':
						if (packet.getNum().equals("0")) {
							JOptionPane.showMessageDialog(null, "The user ID is already in use.",
							 "", JOptionPane.ERROR_MESSAGE);
							_windowLogin.setVisible(true);
							_isExiting = true;
							isClientOngoing = true;
							break LOOP;

						} else if (packet.getNum().equals("2")) {
							JOptionPane.showMessageDialog(null, "The peer has logged out.",
							 "", JOptionPane.ERROR_MESSAGE);
							break;
						}

					default:
						JOptionPane.showMessageDialog(null, "Protocol error.",
						 "", JOptionPane.ERROR_MESSAGE);
						break LOOP;
				}
			}
System.out.println("finish in try");

			if (!_isExiting) {
				JOptionPane.showMessageDialog(null, "Connection is reset by the server.",
				 "", JOptionPane.ERROR_MESSAGE);
			}

		}catch (IOException e){
System.out.println("finish in catch");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "I/O error.",
			 "", JOptionPane.ERROR_MESSAGE);

		}finally {
System.out.println("finish in finally");

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
		if (!isClientOngoing) {
			System.exit(0);
		}
	}

	public void setExiting() {
		_isExiting = true;
	}

	public ChatWindow getChatWindow(String sID){
		ChatWindow chat = (ChatWindow)_table.get(sID);
		if(chat == null){
			chat = new ChatWindow(_sName, sID, this);
			_table.put(sID, chat);
		}
		return chat;
	}

	private void joinToServer(){
		System.out.println(_sName);
		_writer.println("J."+encode(_sName));
		System.out.println(_sName+" JOINED to mitsubaja\n");
	}

	private LoginWindow _windowLogin;
	//private SerialComm _serial;

	private Socket _socket;
	
	private PrintWriter _writer;
	private Hashtable _table;

	private String _sName;

	private boolean _isExiting;

}
/* end of ChatServerThread.java */
