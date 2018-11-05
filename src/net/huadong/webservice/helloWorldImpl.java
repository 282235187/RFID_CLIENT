package net.huadong.webservice;

import java.util.HashMap;

import javax.jws.WebService;

import net.huadong.sendreceive.MyRunnable1;

@WebService
public class helloWorldImpl implements HelloWorld {

	MyRunnable1 m1;
	HashMap m=new HashMap();
	
	@Override
	public String say(String str,String ip,String port) {
		// TODO Auto-generated method stub
		
		String[] ipot=str.split(",");
		//System.out.println(str+"===="+ipot[0]+""+port);
		if(ipot[2].equals("1")) {
			m1=new MyRunnable1();
			m1.setIH(ipot[0], Integer.parseInt(ipot[1]));
			m1.start();	
			m.put(ipot[0], m1);
			try {
				Thread.sleep(1000);
				if(m1.connectStatus==1) {
					return "连接成功";
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return "操作失败请重试";
		}
		if(ipot[2].equals("0")) {
			
			MyRunnable1 mm=(MyRunnable1) m.get(ipot[0]);
			mm.runflag=false;
			try {
				Thread.sleep(1000);
				//if(m1.connectStatus==0) {
					return "已断开连接";
				//}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return "操作失败请重试";
		}

		return "操作失败请重试";
	}

}
