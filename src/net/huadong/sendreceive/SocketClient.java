package net.huadong.sendreceive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import net.huadong.util.SocketUtil;

public class SocketClient {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		byte[] bytess= {'O','B','F','F','F'};
//	    byte[] bytes = new byte[] { (byte) 0xAA, (byte) 0xFF, (byte) 0x03, (byte) 0x64, (byte) 0x00, (byte) 0x00 };
//		String nn=new String(bytess,0,5);
//		String nnn=new String(bytes,0,6);
//		System.out.println(nn+""+nn.length()+"===="+nnn+"=="+bytes);
		MyRunnable1 m1=new MyRunnable1();
		m1.setIH("192.168.1.235", 27011);
		
		Thread t1=new Thread(m1);
		t1.start();
		
//		MyRunnable1 m1=new MyRunnable1();
//		m1.setIH("127.0.0.1", 719);
//		Thread t1=new Thread(m1);
//		t1.start();
//		
//		MyRunnable1 m2=new MyRunnable1();
//		m2.setIH("192.168.1.233", 1002);
//		Thread t2=new Thread(m2);
//		t2.start();
		
//		MyRunnable1 m3=new MyRunnable1();
//		m3.setIH("192.168.1.233", 1003);
//		Thread t3=new Thread(m3);
//		t3.start();
//		
//		MyRunnable1 m4=new MyRunnable1();
//		m4.setIH("192.168.1.233", 1004);
//		Thread t4=new Thread(m4);
//		t4.start();
		//Thread t2=new Thread(new MyRunnable1());
		//t2.start();
		
		/*SocketUtil socketUtil=new SocketUtil();
		//要连接的服务器ip与端口
		String host="10.18.6.65";
		int port=1001;
		
		String host1="10.18.6.65";
		int port1=1002;
//		
//		socketUtil.connect(host, port);
//		byte[] bytes= {1,2,90,42,'o'};
//		socketUtil.write(bytes);
//		
//		socketUtil.connect(host1, port1);
		//与服务端建立连接
		Socket socket =new Socket(host,port);
		Socket socket1=new Socket(host1,port1);
		//建立连接后获得输出流
		OutputStream outputStream=socket.getOutputStream();
		
		String message="你好！！！";
		//socketUtil.write(message.getBytes());
		socket.getOutputStream().write(message.getBytes());
		//通过shutdownOutput发送完数据，关闭了输出流，后续只能接收数据
		//socket.shutdownOutput();
		
		InputStream inputStream=socket.getInputStream();
		byte[] bytess=new byte[1024];
		int len;
		String sb ="";
		while((len = inputStream.read(bytess))!=-1) {
			//统一编码格式
			sb=new String(bytess,0,len);
			
			System.out.println("get message from Oneserver:"+sb);
		}
		
		InputStream inputStream1=socket1.getInputStream();
		byte[] bytess1=new byte[1024];
		int len1;
		String sb1 ="";
		while((len1 = inputStream.read(bytess1))!=-1) {
			//统一编码格式
			sb1=new String(bytess1,0,len1);
			
			System.out.println("get message from Tserver:"+sb1);
		}*/
		
		//inputStream.close();
		//outputStream.close();
		//socket.close();
	}

}
