package net.huadong.webservice;

import javax.xml.ws.Endpoint;

/***/

public class Service {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		helloWorldImpl hWI=new helloWorldImpl();
		String address="http://192.168.1.232:9521/world";
		Endpoint.publish(address, hWI);//jdkʵ��
		System.out.println("webService Started");
	}

}
