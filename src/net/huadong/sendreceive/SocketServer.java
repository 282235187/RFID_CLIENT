package net.huadong.sendreceive;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class SocketServer {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int port=8080;
		Logger logger=Logger.getLogger(SocketServer.class);
		ServerSocket server =new ServerSocket(port);
		logger.info("等待连接到来");
		System.out.println("等待连接...");
		// 侦听并接受到此套接字的连接,返回一个Socket对象
		Socket socket=server.accept();
		//已建立好连接，从SOCKET获取输入流，并建立缓冲区进行读取
		//InputStream inputStream =socket.getInputStream();
		// 字节流读取数据，防止字符流读取的时候readLine阻塞
		byte[] bytes=new byte[1024];
		
		int len;
		/*StringBuilder sb=new StringBuilder();
		//len==-1代表客户端与服务端已断开连接
		while((len=inputStream.read(bytes))!=-1) {
			//统一编码格式，这里使用utf-8
			sb.append(new String(bytes,0,len,"utf-8"));
		}
		System.out.println("get message from client:"+sb);*/
		
		OutputStream outputStream=socket.getOutputStream();
		outputStream.write("hello Client,I get the message:".getBytes("utf-8"));
		
		//inputStream.close();
		//outputStream.close();
		//socket.close();
		//server.close();
		
		
	}

}
