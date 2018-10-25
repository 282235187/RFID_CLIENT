package net.huadong.sendreceive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MyRunnable2 implements Runnable {

	String host="10.18.6.65";
	int port=1002;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Socket socket =new Socket(host,port);
			
			OutputStream outputStream=socket.getOutputStream();	
			String message="你好！！！";
			socket.getOutputStream().write(message.getBytes());
			
			InputStream inputStream=socket.getInputStream();
			byte[] bytess=new byte[1024];
			int len;
			String sb ="";
			while((len = inputStream.read(bytess))!=-1) {
				//统一编码格式
				sb=new String(bytess,0,len);
				
				System.out.println("get message from TWOserver:"+sb);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
