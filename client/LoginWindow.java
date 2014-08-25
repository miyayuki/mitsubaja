/*----------------------------
 * LoginWindow.java
 *
 * written by Miyuki Ozawa
 * ---------------------------*/

import java.net.Socket;
import java.io.PrintWriter;
import java.io.*;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame implements ActionListener{

	public LoginWindow(){
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100, 350, 170);
		setTitle("Login Window");

		_panel1 = new JPanel();
		_panel2 = new JPanel();

		_nameField = new JTextField(20);
		getContentPane().add(_nameField);
		JLabel label1 = new JLabel("Login Name");

		_addrField = new JTextField("fran.sfc.wide.ad.jp", 20);
		getContentPane().add(_addrField);
		JLabel label2 = new JLabel("Server Name");
		
		
		_button = new JButton();
		_button.setText("Join!");
		_button.addActionListener(this);
		getContentPane().add(_button);

		_panel1.add(label1);
		_panel1.add(_nameField);
		_panel2.add(label2);
		_panel2.add(_addrField);
		//_panel2.add(_button);

		Container contentPane =  getContentPane();
		contentPane.add(_panel1, BorderLayout.NORTH);
		contentPane.add(_panel2, BorderLayout.CENTER);
		contentPane.add(_button, BorderLayout.SOUTH);
		pack();
	}

	public void actionPerformed(ActionEvent event){
		try{
			Object sourse = event.getSource();
			if(sourse == _button){
				_addr = _addrField.getText();
				_name = _nameField.getText();
				Socket socket = new Socket(_addr, PORT);
				System.out.println("connect success");
			/* call thread */
				ChatClientThread thread = new ChatClientThread(socket, _name, this);
				thread.start();
				//thread.run();
				setVisible(false);
			}
		}catch(IOException e){
			JOptionPane.showMessageDialog(null,
			 "Failed to connect to the server.",
			 "", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JPanel _panel1, _panel2;
	private JButton _button;
	private JTextField _nameField;
	private JTextField _addrField;

	private String _addr;
	private String _name;

	private static final int PORT = 33328;
}

/* end of LoginWindow.java */
