/*-----------------------------
 * MemberTableModel.java
 *
 * written by Miyuki Ozawa
 * ---------------------------*/

import java.util.LinkedList;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;

public class MemberTableModel extends LinkedList implements TableModel{

	public MemberTableModel(MemberList list){
		_list = list;
	}
	
	public void addTableModelListener(TableModelListener unused) {
	}

	public Class getColumnClass(int iColumn) {
		switch (iColumn) {
				case 0:
					return String.class;
				case 1:
				default:
					return String.class;
		}
	}

	public int getColumnCount() {
		return (2);
	}

	public String getColumnName(int iColumn) {
		String s;
		switch (iColumn) {
				case 0:
					s = "Name";
					break;
				case 1:
				default:
					s = "Status";
					break;
		}
		return (s);
	}

	public int getRowCount() {
		return size();
	}

	public Object getValueAt(int iRow, int iColumn) {
		Member member = (Member)get(iRow);
		switch (iColumn) {
				case 0:
					return member.sID;
				case 1:
				default:
//System.out.println(_serial.inRead);
					return member.sStatus;
					//return _serial.inRead;
		}
	}

	public boolean isCellEditable(int iRow, int iColumn) {
		return iRow==0 && iColumn==1?true:false;
	}

	public void removeTableModelListener(TableModelListener unused) {
	}

	public void setValueAt(Object value, int iRow, int iColumn) {
		Member member = (Member)get(iRow);
		SerialTest serial = new SerialTest();
System.out.println("S*"+serial.inRead);
		member.sStatus = (String)value;
		_list.sendStatus(member.sStatus);
	}

	private MemberList _list;
}
