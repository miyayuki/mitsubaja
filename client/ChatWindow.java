/*---------------------------
 * ChatWindow.java
 * written by Miyuki Ozawa
 * ---------------------------*/

import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.text.*;

public class ChatWindow extends JFrame implements ActionListener{

	public ChatWindow(String name, String sID, ChatClientThread thread){
		_name = name;
		_thread = thread;
		_sID = sID;
		_format = new SimpleDateFormat(S_FORMAT_TIME);
		
		setTitle(_name+" - "+_sID);
		setBounds(600, 300, 400, 400);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());

		_textPane = new JTextPane();
		_scrollPane = new JScrollPane(_textPane);
		_textPane.setEditable(false);

		_doc = (DefaultStyledDocument)_textPane.getDocument();

		Style style = _doc.addStyle(S_STYLE_MY_LINE,
									_doc.getStyle(S_STYLE_DEFAULT));
		StyleConstants.setForeground(style, COLOR_MY_LINE);

		_field = new JTextField();
		_field.addActionListener(this);

		add(_scrollPane, BorderLayout.CENTER);
		
		add(_field, BorderLayout.SOUTH);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
System.out.println(_field.getText().length());
		if(_field.getText().length() == 0){
System.out.println("can't send message");
		}else{
			if (source == _field) {
				sendMessage(_field.getText());
				_field.setText("");
			}
		}
	}

	public void receiveMessage(String sMessage){
		write(_sID, sMessage, null);
	}

	public void sendMessage(String sMessage){
		_thread.send("M."+_thread.encode(_name)+"."+_thread.encode(_sID)+"."+_thread.encode(sMessage));
		write(_name, sMessage, _doc.getStyle(S_STYLE_MY_LINE));
	}

	public void write(String sNickname, String s, Style style) {
		int offset = _doc.getLength();
		Date date = new Date(System.currentTimeMillis());
		
		s = _format.format(date) + " <" + sNickname + ">: " + s + "\n";
		try {
			_doc.insertString(offset, s, null);
		} catch (BadLocationException unused) {
		}
		if (style != null) {
			_doc.setCharacterAttributes(offset, _doc.getLength() - offset,style, true);
		}
		_textPane.setCaretPosition(_doc.getLength());
		JScrollBar bar = _scrollPane.getVerticalScrollBar();
		bar.setValue(bar.getMaximum());
	}

	private ChatClientThread _thread;
	private DefaultStyledDocument _doc;

	private JButton _buttonSend;

	private JTextPane _textPane;
	private JTextField _field;
	private JScrollPane _scrollPane;

	private SimpleDateFormat _format;

	private String _name;
	private String _sID;
	
	private static final Color COLOR_MY_LINE = Color.blue;
	private static final String S_FORMAT_TIME = "HH:mm:ss";
	private static final String S_STYLE_DEFAULT = "default";
	private static final String S_STYLE_MY_LINE = "myline";
}

/* end of ChatWindow.java */
