package clientSide;
import org.apache.log4j.*;
import java.io.*;
import java.net.*;

public class Client 
{
	static Logger logger=Logger.getLogger(Client.class);
	public static void main(String[] args)
	{
		Socket clientSocket = null;	
		DataInputStream dataInputStream=null;
		InputStream socketIn=null;
		try
		{
			int i=3,count,PortNumber;
			String IpAddress,download;
			if(args.length==0)
			{
				System.out.println("Enter arguments in the Format : ");
				System.out.println("IpAddress PortNumber FolderName filename1 filename2 filename3 .... \n");										
			}
			else
			{
				IpAddress=args[0];
				PortNumber=Integer.parseInt(args[1]);
				download=args[2];
				count=args.length;			
				clientSocket=new Socket(IpAddress,PortNumber);				
				PrintWriter socketOut=new PrintWriter(clientSocket.getOutputStream(),true);
				DataOutputStream dataOutputStream=new DataOutputStream(clientSocket.getOutputStream());
				socketIn=clientSocket.getInputStream();
				dataInputStream=new DataInputStream(clientSocket.getInputStream());								
				dataOutputStream.writeInt(count-3);				
				while(i!=count)
				{						
					String filename=args[i];						
					socketOut.println(filename);						
					logger.debug("Requested file is ::   \""+filename+"\"");												
					int choice=dataInputStream.readInt();				
					if(choice==0)
						System.out.println("Sorry File is not existed on Server!!");
					else
					{
						File f = new File(filename);
						if(f.exists())						
							logger.debug("File Already Exists.It's overriding now !!!!! ");												
						long size=dataInputStream.readLong();						
						logger.debug("->The no. of bytes :" + size);						
						byte[] bytes = new byte[(int)size];
						int bytesread = socketIn.read(bytes,0,bytes.length);
						FileOutputStream fileOutputStream = new FileOutputStream(download+"\\"+f);												
						int noOfBytes = bytesread;
						do
						{							
							bytesread=socketIn.read(bytes,noOfBytes,(bytes.length-noOfBytes));
							if(noOfBytes>=0)
								noOfBytes += bytesread;													
						}while(bytesread>0);
						fileOutputStream.write(bytes,0,noOfBytes);
						fileOutputStream.flush();							
						logger.debug("->File "+filename+" was downloaded  "+"( "+noOfBytes+" )");	
						logger.debug("Downloading .. "+args[i]+" Completed ");
						fileOutputStream.close();
					}	
					logger.debug("\n----------------------------------------------\n");
					i++;
				}
				socketOut.close();
				dataInputStream.close();
				socketIn.close();
				clientSocket.close();
			}									
		}
		catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
		}			
	}
}
