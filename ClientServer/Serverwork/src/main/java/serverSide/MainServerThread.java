package serverSide;
import org.apache.log4j.*;
import javax.net.ssl.*;
public class MainServerThread 
{	
	public static int port = -1;
	public static String dirName = null;
	static Logger Output=Logger.getLogger(MainServerThread.class);
	public static void main(String args[])
	{
		MainServerThread newServer = new MainServerThread();
		if (newServer.validteAssignCommandLine(args))
		{
			newServer.startServer();
		}
		else
		{
			System.exit(1);
		}		
	}
	
	protected boolean validteAssignCommandLine(String args[])
	{		
		if(args.length>0)
		{
			port=Integer.parseInt(args[0]);
			dirName=args[1];
			return true;
		}
		else
		{
			port=1212;
			dirName="C:\\Users\\mgiddaluri";
			System.out.println("Enter arguments in the Format : ");
			System.out.println("PortNumber SearchingFolderName\n");
			System.out.println("Now the Default PortNumber is : 1212 ");
			System.out.println("Searching Folder is : C:\\Users\\mgiddaluri ");	
			return false;
		}			
		
	}
	
	protected void startServer()
	{
		try
		{
			SSLServerSocketFactory sslServerSocketFactory=(SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
			SSLServerSocket serverSocket=(SSLServerSocket)sslServerSocketFactory.createServerSocket(port);			
			System.out.println("Listening to Port "+port);			
			Output.debug("Listening to Port "+port);			
			Output.debug("");
			while(true)
			{
				SSLSocket socket=(SSLSocket)serverSocket.accept();				
				ServerWorkerThread workerThread=new ServerWorkerThread(socket,dirName);
				Thread t=new Thread(workerThread);
				t.start();
			}			
		}
		catch(Exception e)
		{
			System.out.println("Exception in proceeding : "+e);
		}
	}

}
