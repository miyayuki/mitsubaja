public class Account{
	public Account(String sID, ChatServerThread thread){
		_sID = sID;
		_thread = thread;
		_sStatus = "";
	}
	public String getStatus(){
		return _sStatus;
	}
	public void setStatus(String sStatus){
		_sStatus = sStatus;
	}
	public ChatServerThread getChatServerThread(){
		return _thread;
	}
	public String getID(){
		return _sID;
	}
	private ChatServerThread _thread;
	private String _sID;
	private String _sStatus;
}
