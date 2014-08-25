public class Packet{
	public static final int IDX_TYPE = 0;
	public static final int IDX_FROM = 1;
	public static final int IDX_TO = 2;
	public static final int IDX_MESSAGE = 3;
	public static final int IDX_STATUS = 2;
	public static final int IDX_TOTAL = 4;
	public static final int IDX_NUM = 1;

	public Packet(String sLine){
		_aosArguments = sLine.split("\\.");
		System.out.println(sLine+" "+_aosArguments.length);
	}
	public String get(int idx){
		return (_aosArguments[idx]);
	}
	public String getFrom(){
		return(ChatClientThread.decode(_aosArguments[IDX_FROM]));
	}
	public String getMessage(){
System.out.println("getMessage() OK");
		return(ChatClientThread.decode(_aosArguments[IDX_MESSAGE]));
	}
	public String getNum(){
		return(_aosArguments[IDX_NUM]);
	}
	public String getTo(){
		return(ChatClientThread.decode(_aosArguments[IDX_TO]));
	}
	public char getType(){
		return(_aosArguments[IDX_TYPE].charAt(0));
	}
	public String getStatus(){
		if(_aosArguments.length < 3){
			System.out.println("getStatus OK");
			return("");
		}else{
			return(ChatClientThread.decode(_aosArguments[IDX_STATUS]));
		}
	}
	/* change packet into an one line of String */
	public String getString(){
		String s = "";
		for(int i=0;i<_aosArguments.length;i++){
			s += _aosArguments[i]+ (i<(_aosArguments.length-1)?".":"");
		}
		return s;
	}
	private String[] _aosArguments;
}
