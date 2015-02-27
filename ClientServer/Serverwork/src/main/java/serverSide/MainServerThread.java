package serverSide;
import org.apache.log4j.*;
import java.net.*;


public class MainServerThread 
{
	static Logger Output=Logger.getLogger(MainServerThread.class);
	public static void main(String args[])
	{
		ServerSocket serverSocket;
		Socket socket;
		String Folder;
		int PortNumber;
		try
		{	
			if(args.length==0)
			{
				System.out.println("Enter arguments in the Format : ");
				System.out.println("PortNumber SearchingFolderName\n");
				System.out.println("Now the Default PortNumber is : 1212 ");
				System.out.println("Searching Folder is : C:\\Users\\mgiddaluri ");
				Folder="C:\\Users\\mgiddaluri";
				PortNumber=1212;
			}
			else
			{
				Folder=args[1];
				PortNumber=Integer.parseInt(args[0]);
			}			
			serverSocket=new ServerSocket(PortNumber);
			System.out.println("Listening to Port "+PortNumber);
			Output.debug("Listening to Port "+PortNumber);
			Output.debug("");
			while(true)
				{
					socket=serverSocket.accept();
					ServerWorkerThread workerThread=new ServerWorkerThread(socket,Folder);
					Thread t=new Thread(workerThread);
					t.start();
				}
		}
		catch(Exception e)
		{
				System.out.println("Hai"+e);
			
		}
	}
}
