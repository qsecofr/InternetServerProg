import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;

import java.awt.*;

public class ChatClient 
{
	JTextArea receiver;
	JTextArea sender;
	JTextField msg;

	BufferedReader msgReader;
	
	PrintWriter msgPrt;
	Socket socket;
	
	public static void main(String[] args)
	{
		ChatClient client = new ChatClient();
		client.initGUI();
	}
	
	public void initGUI()
	{
		//initialize the GUI
		JFrame frame = new JFrame("ChatClient");
		JPanel mainPanel = new JPanel();
		
		receiver = new JTextArea(15, 20);
		receiver.setLineWrap(true);
		receiver.setWrapStyleWord(true);
		receiver.setEditable(false);
		
		JScrollPane rcvScroller = new JScrollPane(receiver);
		rcvScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		rcvScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		msg = new JTextField();
		JButton sendButton = new JButton("SEND");
		//Listner to send
		sendButton.addActionListener(new SendButtonListener());
		mainPanel.add(rcvScroller);
		mainPanel.add(sendButton);
		mainPanel.add(msg);
		//Startet NetInit()
		initNet();
		//MultiThreading
		Thread rcvThread = new Thread(new rcvReader());
		rcvThread.start();
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(400, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initNet()
	{
		
		try
		{
			//Socket
			socket = new Socket("127.0.0.1", 5000);
			InputStreamReader rcvStream = new InputStreamReader(socket.getInputStream());
			//Printwriter
			msgReader = new BufferedReader(rcvStream);
			msgPrt = new PrintWriter(socket.getOutputStream());
			System.out.println("Network initialized");
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		
	}
	
	private class SendButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent ev)
		{
			//get the text from the textvariable/field and forward to the server (via PrintWriter)
			try
			{
				msgPrt.println(msg.getText());
				msgPrt.flush();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			msg.setText("");
			msg.requestFocus();
		}

	}
	
	public class rcvReader implements Runnable
	{
		public void run()
		{
			String msg;
			try
			{
				while((msg = msgReader.readLine()) != null)
				{
					System.out.println("gelesen: " + msg);
					receiver.append(msg + "\n");
				}
			}
			catch(Exception ex) {ex.printStackTrace();}
		}
	}
	
}
