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
		logger.info("�ȴ����ӵ���");
		System.out.println("�ȴ�����...");
		// ���������ܵ����׽��ֵ�����,����һ��Socket����
		Socket socket=server.accept();
		//�ѽ��������ӣ���SOCKET��ȡ�����������������������ж�ȡ
		//InputStream inputStream =socket.getInputStream();
		// �ֽ�����ȡ���ݣ���ֹ�ַ�����ȡ��ʱ��readLine����
		byte[] bytes=new byte[1024];
		
		int len;
		/*StringBuilder sb=new StringBuilder();
		//len==-1����ͻ����������ѶϿ�����
		while((len=inputStream.read(bytes))!=-1) {
			//ͳһ�����ʽ������ʹ��utf-8
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
