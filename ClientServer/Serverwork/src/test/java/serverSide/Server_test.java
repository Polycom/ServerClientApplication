package serverSide;
import org.junit.Assert;
import org.junit.Test;
import java.io.*;

public class Server_test 
{
	ServerProgram serverProgram=new ServerProgram();	
	ServerWorkerThread serverWorkerThread = new ServerWorkerThread(null,null);	
	@Test
	public void test1() 
	{
		String arr[]=new String[]{"123","C:\\Downloads","new.txt"};
		boolean result=serverProgram.validteAssignCommandLine(arr);
		boolean expected=true;		
		Assert.assertEquals(expected,result);
	}
	@Test
	public void test2() 
	{
		String arr[]=new String[]{};
		boolean result=serverProgram.validteAssignCommandLine(arr);
		boolean expected=false;		
		Assert.assertEquals(expected,result);
	}
	@Test
	public void test3()
	{
		try
		{		
			InputStreamReader in=new InputStreamReader(new ByteArrayInputStream("new.txt".getBytes()));
			BufferedReader bf=new BufferedReader(in);
			String folder="C:\\Users\\mgiddaluri";
			String fileName="new.txt";
			String actual=serverWorkerThread.readValidateFileName(bf,folder);
			Assert.assertEquals(fileName, actual);
		}
		catch(Exception e)
		{
			System.out.println("Exception "+e);
		}	
	}
	@Test
	public void test4()
	{
		try
		{		
			InputStreamReader in=new InputStreamReader(new ByteArrayInputStream("hai".getBytes()));
			BufferedReader bf=new BufferedReader(in);
			String folder="C:\\Users\\mgiddaluri";
			String fileName=null;
			String actual=serverWorkerThread.readValidateFileName(bf,folder);
			Assert.assertEquals(fileName, actual);
		}
		catch(Exception e)
		{
			System.out.println("Exception "+e);
		}	
	}
	
  	@Test
	public void test5()
	{
		try
		{	
			DataOutputStream out=new DataOutputStream(new ByteArrayOutputStream(1024));
			String filename="new.txt";
			File f=new File("C:\\Users\\mgiddaluri\\"+filename);
			serverWorkerThread.fileExists(f,filename,out);		
		}
		catch(Exception e)
		{
			System.out.println("Exception "+e);
		}	
	}
 	@Test
	public void test6()
	{
		try
		{	
			DataOutputStream out=new DataOutputStream(new ByteArrayOutputStream(1024));
			String filename="polycom.jpg";
			String f="C:\\Users\\mgiddaluri\\";
			serverWorkerThread.sendFileToClient(filename,out,f);		
		}
		catch(Exception e)
		{
			System.out.println("Exception "+e);
		}	
	}
 	@Test
	public void test7()
	{
		try
		{			
			DataOutputStream out=new DataOutputStream(new ByteArrayOutputStream(1024));
			serverWorkerThread.sendErrorToClient(out);		
		}
		catch(Exception e)
		{
			System.out.println("Exception "+e);
		}	
	}
			
	
}

