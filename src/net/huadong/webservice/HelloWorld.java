package net.huadong.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface HelloWorld {
	@WebMethod
	String say(String str,String ip,String port);
}
