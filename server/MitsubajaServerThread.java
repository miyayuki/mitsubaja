/*---------------------------*
 * MitsubajaServerThread.java
 *
 * written by Miyuki Ozawa
 *i
 * packet
 * 0  1  2  3
 * M.from.to.message
 * J.from
 * L.from
 * P.from.status
 * K.for (keep-alive)
 * E.num.to.message
 *   0:dupcilate
 *   1:protocol error
 *   2:user not found
 *Hashtable get put
 * run() ソケットを作ってTCPコネクションつくってthread作ってstart
 * getAccount sID
 * putAccount sID Account
 * removeAccount
 * enumrate Accountの
 *---------------------------*/
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;

/* constructer */
public class MitsubajaServerThread extends Thread{
	public static final int iPortDefault = 33328;
	public MitsubajaServerThread(){
		_table = new Hashtable();
	}
	public void run(){
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(iPortDefault);
			while(true){
				/* acceptしたら、すぐに子スレッドに渡す */
				Socket socket = serverSocket.accept();
				System.out.println("connection accepted");
				/* call child process */
				ChatServerThread thread = new ChatServerThread(socket, this);
				thread.start();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		/* socket close */
		try{
			serverSocket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	} /* end of run */
	public synchronized Account getAccount(String sID){
		return (Account)_table.get(sID);
	}
	public synchronized Enumeration getAccounts(){
		return _table.elements();
	}
	public synchronized void putAccount(String sID, Account account){
		_table.put(sID, account);
	}
	public synchronized void removeAccount(String sID){
		_table.remove(sID);
	}
	public static void main(String[] args){
		System.out.println("mitsubaja server version 0.0 (2010-02-03)");
		MitsubajaServerThread thread = new MitsubajaServerThread();
		thread.start();
		(new TimerThread(thread)).start();
	}
	private Hashtable _table;
}

class TimerThread extends Thread {

	public TimerThread(MitsubajaServerThread threadServer) {
		_threadServer = threadServer;
	}

	public void run() {

		try {

			for (; ;) {
				sleep(L_INTERVAL);

				for (Enumeration e = _threadServer.getAccounts(); e.hasMoreElements(); ) {
					((Account)e.nextElement()).getChatServerThread().checkAlive();
				}
			}
		
		} catch (InterruptedException unused) {
		}
	}

	private MitsubajaServerThread _threadServer;

	private static final long L_INTERVAL = 1000 /* msec */ * 60 /* sec */ * 2 /* min */;
}

/* end of mitsubaja_server.java*/
