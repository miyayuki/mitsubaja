/*---------------------------
 * Member.java
 * written by Miyuki Ozawa
 * ---------------------------*/

public class Member{
	public Member(String sID, String sStatus){
		this.sID = sID;
		this.sStatus = sStatus;
		//this.sStatus = _serialTest.inRead;
	}

	public boolean equals(Object o){
		return ((Member)o).sID.equals(sID);
	}

	public String sID;
	public String sStatus;
	private SerialTest _serialTest;
}
