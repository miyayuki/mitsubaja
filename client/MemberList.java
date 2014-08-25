/*-----------------------------
 * MemberList.java
 * written by Miyuki Ozawa
 * ---------------------------*/

import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class MemberList extends JFrame implements MouseListener, WindowListener{

	public MemberList(ChatClientThread thread, String name, SerialComm serial){
System.out.println("Show list success");
		_thread = thread;
		_name = name;
		_serial = serial;
		getContentPane().setLayout(new BorderLayout());

		setBounds(100,100, 500, 300);
		setTitle("mitsubaja");

		_tableModel = new MemberTableModel(this);
		_table = new JTable(_tableModel);
		_table.addMouseListener(this);
		//_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		_scrollPane = new JScrollPane();
		_scrollPane.getViewport().setView(_table);
		_scrollPane.setPreferredSize(new Dimension(200, 450));

		getContentPane().add(_scrollPane, BorderLayout.CENTER);

		addWindowListener(this);
		addMouseListener(this);
		setVisible(true);
	}

	public void mouseClicked(MouseEvent unused) {
	}

	public void mouseEntered(MouseEvent unused) {
	}

	public void mouseExited(MouseEvent unused) {
	}

	public void mousePressed(MouseEvent event) {

		Object source = event.getSource();
		if (source == _table) {
			switch (event.getClickCount()) {
				case 1:
				break;

				//クリックが2回だった場合->chatWindowを呼ぶ
				case 2:
					System.out.println("Double Click");
					int row = _table.getSelectedRow();
					
					if (_table.columnAtPoint(event.getPoint()) == 0) {
						_thread.getChatWindow(((Member)_tableModel.get(row)).sID).setVisible(true);
					}
				break;

				default:
				break;
			}
		}
	}

	public void mouseReleased(MouseEvent unused) {
	}

	public void removeMember(String sID){
		Member member = new Member(sID, null);
		int idx = _tableModel.indexOf(member);
		if(idx>=0){
			_tableModel.remove(idx);
		}
		_table.revalidate();
		_table.repaint();
	}

	public void sendStatus(String sStatus){
		_thread.send("P."+_thread.encode(_name)+"."+_thread.encode(sStatus));
	}

	public void updateStatus(String sID, String sStatus){
		Member member = new Member(sID, sStatus);
		int idx = _tableModel.indexOf(member);
		if(idx<0){
			_tableModel.addLast(member);
		}else{
			((Member)_tableModel.get(idx)).sStatus=sStatus;
		}
		_table.revalidate();
		_table.repaint();
	}

	public void windowActivated(WindowEvent unused) {
	}

	public void windowClosed(WindowEvent unused) {
	}

	public void windowClosing(WindowEvent unused) {
		_thread.setExiting();
		_thread.send("L."+_thread.encode(_name));
	}

	public void windowDeactivated(WindowEvent unused) {
	}

	public void windowDeiconified(WindowEvent unused) {
	}

	public void windowIconified(WindowEvent unused) {
	}
	public void windowOpened(WindowEvent unused) {
	}

	private ChatClientThread _thread;
	private SerialComm _serial;

	private String _name;
	
	private JTable _table;
	private MemberTableModel _tableModel;
	private String[] columnNames = {"NAME", "PRESENCE"};
	private JScrollPane _scrollPane;
}
/* end of MemberList.java */
