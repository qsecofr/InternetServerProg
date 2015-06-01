import java.util.*;
import java.io.*;
import java.net.*;


public class ChatServer 
{
	ArrayList clientRcv;
	
	public class ClientHandler implements Runnable
	{
		BufferedReader msgReader;
		Socket socket;
		
		public ClientHandler(Socket clientSocket)
		{
			try
			{
				socket = clientSocket;
				InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
				msgReader = new BufferedReader(isReader);
			}
			catch(Exception ex) {ex.printStackTrace();}
		}
		
		public void run()
		{
			String msg;
			try
			{
				while((msg = msgReader.readLine()) != null)
				{
					System.out.println("gelesen: " + msg);
					broadcast(msg);
				}
			}
			catch(Exception ex) {ex.printStackTrace();}
		}
		
	} //end ClientHandler
	
	public static void main (String[] args)
	{
		new ChatServer().init();
	}
	
	public void init()
	{
		clientRcv = new ArrayList();

		try
		{
			ServerSocket serverSocket = new ServerSocket(5000);
			
			while(true)
			{
				Socket clientSocket = serverSocket.accept();
				PrintWriter prt = new PrintWriter(clientSocket.getOutputStream());
				clientRcv.add(prt);
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println();
			}
			
		}
		catch(Exception ex) {ex.printStackTrace();}
	}
	
	public void broadcast(String msg)
	{
		Iterator it = clientRcv.iterator();
		while(it.hasNext())
		{
			try
			{
				PrintWriter prt = (PrintWriter) it.next();
				prt.println(msg);
				prt.flush();
			}
			catch(Exception ex) {ex.printStackTrace();}
		} //End of while
	}//end of broadcast

}
