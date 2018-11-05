package net.huadong.sendreceive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import net.huadong.util.BytesUtil;
import net.huadong.util.DBHelper;
import net.huadong.util.Parameter;

import net.huadong.webservice.helloWorldImpl;

public class MyRunnable1 extends Thread  {
	/**设备ip地址**/
	private String ip="";
	/**设备端口号**/
	private int port=0;
	private int len;
	private String sb ="";
	private String sb2="";
	private String retu="";
	/**记录第一次连接，10000毫秒后判断是否连接成功，默认是0**/
	int cont=0;
	Socket socket=null;
	InputStream inputStream;
	private OutputStream outputStream;
	/**与服务端的连接状态
	 * 未连接 ：0
	 * 已连接：1**/
	public int connectStatus=0;
	//判断断线重发的循环的标志位
	private boolean resendTag=true;
	private static final long HEART_BEAT_RATE = 3 * 1000;//心跳包每隔3秒发送一次
	private static final Logger logger = Logger.getLogger(MyRunnable1.class);// 日志文件
	BytesUtil bytesUtil;
	//连接
	public boolean runflag=true;
	public synchronized void stopthread()
	{
		runflag=false;
	}
	public synchronized void getrunflag()
	{
		runflag=true;
	} 
	public void setIH(String ip,int port) {
		this.ip=ip;
		this.port=port;
	}
	public void setCS(int connStatus) {
		this.connectStatus=connStatus;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(ip+"==="+port);
		PropertyConfigurator.configure("src/log4j.properties");	
		byte[] bytess = new byte[1024];
		long begintime=0;
		long endtime=0;
		long endtime2=0;
		while(runflag) {
			try {
				if(connectStatus==0) {
					connectStatus=1;
					socket =new Socket(ip,port);
					//建立连接后获得输入流，接收设备传递的数据
					inputStream=socket.getInputStream();		 					
					begintime = System.currentTimeMillis();
					System.out.println("连接时间："+begintime+new Date());
					
					timeTask(10000,1);
					//开启心跳线程
					if(resendTag) {
						hearBeatThread();
					}
				
				}
	
				len=inputStream.read(bytess);
				if(len !=-1) {
					byte[] subData=bytesUtil.subBytes(bytess,0,len); //截取两个字节(英文字符)  汉字（三个字节)
					sb=bytesUtil.Bytes2HexString(subData);
					logger.info("ip:"+ip+"端口："+port+" 连接成功！！！返回信息："+sb);
					if(sb.equals("0BFFFFFFFFFFFFFB")) {
							//System.out.println(port+"端口连接成功，没集卡");
					}if(sb.length()==60) {
						if(!sb2.equals(sb)) {
							Parameter[] param= {
								new Parameter(sb.substring(0,3),Parameter.IN),
								new Parameter(ip,Parameter.IN),
								new Parameter(retu,Parameter.OUT)
							};
							DBHelper.execProcedure("{call PKG_RFID.P_TRUCK_RFID_ARRIVE(?,?,?)}", param);
							System.out.println("执行存储过程！"+"返回值："+retu);
							System.out.println("");
							//System.out.println(sb.substring(2,15));
						}
							sb2=sb;
					}
					System.out.println("get message from Oneserver:"+sb+"===="+sb.length()+"=|||="+len);
					endtime = System.currentTimeMillis();
					long jiange=endtime-endtime2;
					endtime2=endtime;
					long costTime = (endtime - begintime);
					//要换算为微秒，就除上1000，就可以
					System.out.println("时间间隔："+costTime+"开始："+begintime+"结束："+endtime+"  间隔："+jiange);
						
				}else {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.warn("ip:"+ip+"端口："+port+" 异常断开，重连...");
					System.out.println("与服务端已断开");
					disconnect();
					connectStatus=0;
				}					
				
			} catch (IOException e) {
				// TODO Auto-generated catch block			
				e.printStackTrace();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				disconnect();
				logger.warn("ip:"+ip+"端口："+port+" 连接失败");
				System.out.println("连接失败");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		connectStatus=0;
	}
	
	public void disconnect(){
        try
        {
            if(outputStream!=null)
            	outputStream.close();
            if(inputStream!=null)
            	inputStream.close();
            if(socket!=null)
            	socket.close();
//            outputStream=null;
//            inputStream=null;
//            socket=null;
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
	
	public void timeTask(int jishi,int cS) {
		
		cont=0;//确保每次重新连接，进行10*1000毫秒检测是否连接成功
		sb="";
		Timer timer=new Timer();
		TimerTask task=new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(cont!=0) {
					
					if(sb.equals("")) {
						logger.warn("ip:"+ip+"端口："+port+" 第一次连接10秒未响应");
						System.out.println(port+"端口"+jishi/1000+"秒未响应，连接失败");
						//10秒未响应连接失败，关闭scoket、I/O流
						
						disconnect();
					}
					connectStatus=cS;	
					System.out.println(jishi/1000+"秒后"+new Date());
					//计时停止
					timer.cancel();
				}else  cont=1;
					
			}
				
		};
			
		timer.schedule(task, 0, jishi);
	}
	
	/**
	 *  每三秒发送一个心跳包
	 * */
	public void hearBeatThread() { 
		new Thread(new Runnable() {
            @Override
            public void run() {
            	 while(runflag) {
          		   try {
          			 resendTag=false;
          			Thread.sleep(HEART_BEAT_RATE);
//          			if(socket.isClosed()) {
//          				System.out.println("socket已关闭");
//          			}
          			//socket.setSoTimeout(9000);
          			outputStream = socket.getOutputStream();
          			outputStream.write(0xFF);
          			boolean p=ping(ip, 3, 3000);
          			if(! p) {
          				disconnect();
          				logger.warn("ip:"+ip+"端口："+port+" 未ping通，重连...");
              			System.out.println(port+"::断线重连");
              			connectStatus=0;
          			}
          			System.out.println(p+"：pingf的结果");
          			//outputStream.flush();
          			//socket.sendUrgentData();
          			System.out.println(port+"::心跳检测三秒一次");
          			System.out.println("");
          		} catch (IOException e) {
          			// TODO Auto-generated catch block
          			try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
          			disconnect();
          			logger.warn("ip:"+ip+"端口："+port+" 异常断开，重连...");
          			System.out.println(port+"::断线重连");
          			connectStatus=0;
          			e.printStackTrace();
          		} catch (InterruptedException e) {
          			// TODO Auto-generated catch block
          			e.printStackTrace();
          		}
          	   }
            }
        }).start();
	}
	public static boolean ping(String ipAddress, int pingTimes, int timeOut) {  
		BufferedReader in = null;  
	    Runtime r = Runtime.getRuntime();  // 将要执行的ping命令,此命令是windows格式的命令  
	    String pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;  
	    try {   // 执行命令并获取输出  
	    	System.out.println(pingCommand);   
	        Process p = r.exec(pingCommand);   
	        if (p == null) {    
	        	return false;   
	        }
	        in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数  
	        int connectedCount = 0;   
	        String line = null;   
	        while ((line = in.readLine()) != null) {    
	        	connectedCount += getCheckResult(line);   
	        }   // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真  
	        return connectedCount == pingTimes;  
	    } catch (Exception ex) {   
	        ex.printStackTrace();   // 出现异常则返回假  
	        return false;  
	    } finally {   
	        try {    
	           in.close();   
	        } catch (IOException e) {    
	           e.printStackTrace();   
	        }  
	    }
	 }
	//若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
	private static int getCheckResult(String line) {  // System.out.println("控制台输出的结果为:"+line);  
		Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",    Pattern.CASE_INSENSITIVE);  
	    Matcher matcher = pattern.matcher(line);  
	    while (matcher.find()) {
	    	return 1;
	    }
	    return 0; 
	}
}
