package clientSide;
import org.apache.log4j.*;
import java.io.*;
import javax.net.ssl.*;
public class Client
{		
	InputStream socketIn;	
	static Logger logger=Logger.getLogger(Client.class);	
	public static void main(String[] args)
	{		
		ClientclientProgram=new Client();
		if(args.length<=3)
			clientProgram.noArguments(args.length);		
		else
			clientProgram.withArguments(args);	
	}
	
	public int noArguments(int a)
	{
		if(a<3)
		{
			System.out.println("Enter arguments in the Format : ");
			System.out.println("IpAddress PortNumber FolderName filename1 filename2 filename3 .... \n");
		}
		else if(a==3)
			System.out.println("Provide atleast one File to download ");
		return a;
	}
	
	public String withArguments(String arr[])
	{
		int i=3,count,PortNumber;
		String IpAddress,download,s="No";
		IpAddress=arr[0];
		PortNumber=Integer.parseInt(arr[1]);
		download=arr[2];
		count=arr.length;
		try
		{		
			SSLSocketFactory sslSocketFactory=(SSLSocketFactory)SSLSocketFactory.getDefault();
			SSLSocket clientSocket=(SSLSocket)sslSocketFactory.createSocket(IpAddress,PortNumber);					
			PrintWriter socketOut=new PrintWriter(clientSocket.getOutputStream(),true);
			DataOutputStream dataOutputStream=new DataOutputStream(clientSocket.getOutputStream());
			socketIn=clientSocket.getInputStream();
			DataInputStream dataInputStream=new DataInputStream(clientSocket.getInputStream());								
			dataOutputStream.writeInt(count-3);				
			while(i!=count)
			{	
				s=doThis(arr[i],download,dataInputStream,socketOut);
				i++;
			}
			socketOut.close();
			dataInputStream.close();
			socketIn.close();
			clientSocket.close();	
		}
		catch(Exception e)
		{
			System.out.println("Exception in withArguments"+e);
		}
	
		return s;
	}
	
	public String doThis(String filename,String download,DataInputStream dataInputStream,PrintWriter socketOut)
	{			
	
		try
		{
			socketOut.println(filename);						
			logger.debug("Requested file is ::   \""+filename+"\"");												
			boolean choice=dataInputStream.readBoolean();				
			if(choice==false)			
				System.out.println("Sorry File "+filename +" is not existed on Server!!");				
			else
				fileTransmission(filename,download,dataInputStream,socketIn);		
			logger.debug("\n----------------------------------------------\n");	
			return "Done";
		}
		catch(Exception e)
		{
			System.out.println("Exception in doThis "+e);
			return "Exception";
		}	
				
	}	
	
	public int fileTransmission(String filename,String download,DataInputStream dataInputStream,InputStream socketIn)
	{
		File f = new File(filename);
		if(f.exists())						
			logger.debug("File Already Exists.It's overriding now !!!!! ");
		try
		{			
			long size=dataInputStream.readLong();			
			logger.debug("->The no. of bytes :" + size);			
			byte[] bytes = new byte[(int)size];			
			int bytesread = socketIn.read(bytes,0,bytes.length);			
			FileOutputStream fileOutputStream = new FileOutputStream(download+"\\"+f);	
			int byteshold=readFile(bytesread,bytes,socketIn);		
			fileOutputStream.write(bytes,0,byteshold);
			fileOutputStream.flush();							
			logger.debug("->File "+filename+" was downloaded  "+"( "+byteshold+" )");	
			logger.debug("Downloading .. "+filename+" Completed ");
			logger.debug("Done!");
			fileOutputStream.close();
			return 0;
		}
		catch(Exception e)
		{
			System.out.println("Exception in fileTransmission "+e);
			return 1;
		}		
	}
	public int readFile(int lines,byte[] bytes,InputStream socketIn)
	{
		int noOfBytes=lines;
		try
		{
			do
			{							
				lines=socketIn.read(bytes,noOfBytes,(bytes.length-noOfBytes));
				if(noOfBytes>=0)
					noOfBytes += lines;													
			}while(lines>0);
			return noOfBytes;
		}
		catch(Exception e)
		{
			System.out.println("Exception in readFile "+e);
			return 1;
		}
		
	}
}
