package serverSide;
import org.apache.log4j.*;
import java.io.*;
import java.net.*;

public class ServerWorkerThread implements Runnable
{
	static Logger OutputInfo=Logger.getLogger(ServerWorkerThread.class);
	Socket socket;
	String Folder;
	
	public ServerWorkerThread(Socket socket,String Folder)
	{
		this.socket=socket;
		this.Folder=Folder;
	}
	public void run()
	{		
		try
		{			
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader); 
			clientHandler(dataInputStream, dataOutputStream,bufferedReader,Folder);
			// Close the stream here after handling.
		}
		catch(Exception e)
		{
			System.out.println("Exception in run "+e);
			e.printStackTrace();
		}
	}
	
	protected void clientHandler(DataInputStream dataInputStream,DataOutputStream dataOutputStream,BufferedReader bufferedReader,String Folder) throws Exception
	{		
	   int noOfFiles = dataInputStream.readInt();
	   while(noOfFiles>0)
		{		   
			String fileName = readValidateFileName(bufferedReader,Folder);
			if (fileName == null)
			{				
				sendErrorToClient(dataOutputStream);
			}
			else
			{
				dataOutputStream.writeBoolean(true);
				sendFileToClient(fileName,dataOutputStream,Folder);
			}
			noOfFiles--;
		}			
		
	}
	
	protected String readValidateFileName(BufferedReader bufferedReader,String Folder)
	{		
		try
		{			
			String fileName=bufferedReader.readLine();
			File f=new File(Folder+"\\"+fileName);
			if(f.exists()==false)
			{
				// Put your logs here
				System.out.println("File \""+fileName+"\"does not exist at server");
				OutputInfo.debug("Requsted File \""+fileName+"\" does not exist at "+Folder);
				return null;
			}
			else
			{									
				return fileName;				
			}			
		}
		catch(Exception e)
		{
			System.out.println("Exception in Validating FileName "+e);
			e.printStackTrace();
			return "";
		}		
	}
	
	public void sendFileToClient(String fileName,DataOutputStream dataOutputStream,String Folder)
	{
		try
		{			
			File f=new File(Folder+"\\"+fileName);			
			fileExists(f,fileName,dataOutputStream);			
		}
		catch(Exception e)
		{
			System.out.println("Exception in doing things "+e);
			e.printStackTrace();
		}
	}
	public void fileExists(File f,String fileName,DataOutputStream dataOutputStream)
	{
		try
		{
			OutputInfo.debug("Requsted File \""+fileName+"\" exists at "+Folder);
			System.out.println("Requsted File \""+fileName+"\" exists at "+Folder);
			OutputInfo.debug("");
			byte[] arrayOfFile=new byte[(int)f.length()];
			FileInputStream fileInputStream=new FileInputStream(f);
			long length=(long)arrayOfFile.length;
			dataOutputStream.writeLong(length);
			OutputInfo.debug("Sending "+fileName+" of size ("+arrayOfFile.length+" bytes) ");					
			fileInputStream.read(arrayOfFile,0,(int)length);
			dataOutputStream.write(arrayOfFile,0,(int)length);
			dataOutputStream.flush();
			fileInputStream.close();
			OutputInfo.debug("Sending Completed.......!\n");
			OutputInfo.debug("__________________________________________\n");
			OutputInfo.debug("");
		}
		catch(Exception e)
		{
			System.out.println("Exception in File Transmission "+e);
		}
	}
	public void sendErrorToClient(DataOutputStream dataOutputStream)
	{
		try
		{			
			dataOutputStream.writeBoolean(false);			
		}
		catch(Exception e)
		{
			System.out.println("Exception in sending Error Report "+e);
		}
	}	
}
