package clientSide;
import org.junit.*;
import java.io.*;

public class Client_test 
{
	ClientProgram clientProgram=new ClientProgram(); 
	@Test
	public void test1()
	{	
		int i=clientProgram.noArguments(0);
		int j=0;
		Assert.assertEquals(i,j);
	}	
	@Test
	public void test2()
	{
		int i=clientProgram.noArguments(3);
		int j=3;
		Assert.assertEquals(i,j);
	}
	@Test
	public void test3()
	{
		String arr[]=new String[]{"10.250.92.168","1212","C:\\Downloads","new.txt"};
		clientProgram.withArguments(arr);		
	}
	@Test
	public void test4()
	{
		int lines=3;		
		byte[] bytes="hai".getBytes();
		InputStream in=new ByteArrayInputStream("hai".getBytes());		
		int actual=clientProgram.readFile(lines,bytes,in);
		int expected=lines;
		Assert.assertEquals(expected, actual);
	}
	@Test
	public void test5()
	{
		int lines=3;		
		byte[] bytes="".getBytes();
		InputStream in=new ByteArrayInputStream("haiHello".getBytes());		
		int actual=clientProgram.readFile(lines,bytes,in);
		int expected=1;
		Assert.assertEquals(expected, actual);
	}
	@Test
	public void test6()
	{
		String fileName="new.txt";
		String Folder="C:\\Downloads";
		DataInputStream datain=new DataInputStream(new ByteArrayInputStream("".getBytes()));
		InputStream in=new ByteArrayInputStream("haiHello".getBytes());		
		int actual=clientProgram.fileTransmission(fileName,Folder,datain,in);
		int expected=1;
		Assert.assertEquals(expected, actual);
	}
}

