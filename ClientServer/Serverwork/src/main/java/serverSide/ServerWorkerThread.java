package serverSide;
import org.apache.log4j.*;

import java.io.*;
import java.net.*;

public class ServerWorkerThread implements Runnable
{
	static Logger OutputInfo=Logger.getLogger(ServerWorkerThread.class);
	Socket socket;
	String Folder;
	DataInputStream dataInputStream;
	DataOutputStream dataOutputStream;
	InputStreamReader inputStreamReader;
	FileOutputStream fileOutputStream;	
	public ServerWorkerThread(Socket socket,String Folder)
	{
		this.socket=socket;
		this.Folder=Folder;
	}
	public void run()
	{
		int arg;
		try
		{
			dataInputStream=new DataInputStream(socket.getInputStream());
			dataOutputStream=new DataOutputStream(socket.getOutputStream());
			inputStreamReader=new InputStreamReader(socket.getInputStream());
			BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
			arg=dataInputStream.readInt();
			OutputInfo.debug("The Number of Files Requested by CLIENT is/are : "+arg);
			OutputInfo.debug("");
			while(arg>0)
			{
				String fileName=bufferedReader.readLine();
				File f=new File(Folder+"\\"+fileName);
				if(f.exists()==false)
				{
					OutputInfo.debug("Requsted File \""+fileName+"\" does not exist at "+Folder);					
					dataOutputStream.writeInt(0);
				}
				else
				{
					dataOutputStream.writeInt(1);
					OutputInfo.debug("Requsted File \""+fileName+"\" exists at "+Folder);
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
				}arg--;
			}			
		}
		catch(Exception e)
		{
			System.out.println("hey"+e);
			e.printStackTrace();
		}		
	}
}
