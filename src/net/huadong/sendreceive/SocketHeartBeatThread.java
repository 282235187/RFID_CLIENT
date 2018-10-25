package net.huadong.sendreceive;

import java.io.IOException;
import java.net.Socket;

/**
 * 心跳实现，频率4秒
 * 重新连接
 * Created by JF on 18-10-23.
 */
public class SocketHeartBeatThread extends Thread{

    private static final long HEART_BEAT_RATE = 4 * 1000;//每隔4秒检测一次
    /**设备ip地址**/
	private String ip="";
	/**设备端口号**/
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
			System.out.println("心跳检测四秒一次");
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
