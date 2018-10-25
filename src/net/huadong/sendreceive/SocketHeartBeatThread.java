package net.huadong.sendreceive;

import java.io.IOException;
import java.net.Socket;

/**
 * ����ʵ�֣�Ƶ��4��
 * ��������
 * Created by JF on 18-10-23.
 */
public class SocketHeartBeatThread extends Thread{

    private static final long HEART_BEAT_RATE = 4 * 1000;//ÿ��4����һ��
    /**�豸ip��ַ**/
	private String ip="";
	/**�豸�˿ں�**/
	private int port=0;
    
	private Socket socket;
	
	private MyRunnable1 myRunnable;
    public void SocketHeartBeatThread (Socket msocket) {
    	this.socket=msocket;
    }
   @Override
   public void run() {
	   myRunnable=new MyRunnable1();
	   while(true) {
		   try {
			Thread.sleep(HEART_BEAT_RATE);
			socket.sendUrgentData(0xFF);
			System.out.println("�����������һ��");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			myRunnable.disconnect();
			myRunnable.setCS(0);
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
   }

}
