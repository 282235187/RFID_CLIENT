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
	/**�豸ip��ַ**/
	private String ip="";
	/**�豸�˿ں�**/
	private int port=0;
	private int len;
	private String sb ="";
	private String sb2="";
	private String retu="";
	/**��¼��һ�����ӣ�10000������ж��Ƿ����ӳɹ���Ĭ����0**/
	int cont=0;
	Socket socket=null;
	InputStream inputStream;
	private OutputStream outputStream;
	/**�����˵�����״̬
	 * δ���� ��0
	 * �����ӣ�1**/
	public int connectStatus=0;
	//�ж϶����ط���ѭ���ı�־λ
	private boolean resendTag=true;
	private static final long HEART_BEAT_RATE = 3 * 1000;//������ÿ��3�뷢��һ��
	private static final Logger logger = Logger.getLogger(MyRunnable1.class);// ��־�ļ�
	BytesUtil bytesUtil;
	//����
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
					//�������Ӻ����������������豸���ݵ�����
					inputStream=socket.getInputStream();		 					
					begintime = System.currentTimeMillis();
					System.out.println("����ʱ�䣺"+begintime+new Date());
					
					timeTask(10000,1);
					//���������߳�
					if(resendTag) {
						hearBeatThread();
					}
				
				}
	
				len=inputStream.read(bytess);
				if(len !=-1) {
					byte[] subData=bytesUtil.subBytes(bytess,0,len); //��ȡ�����ֽ�(Ӣ���ַ�)  ���֣������ֽ�)
					sb=bytesUtil.Bytes2HexString(subData);
					logger.info("ip:"+ip+"�˿ڣ�"+port+" ���ӳɹ�������������Ϣ��"+sb);
					if(sb.equals("0BFFFFFFFFFFFFFB")) {
							//System.out.println(port+"�˿����ӳɹ���û����");
					}if(sb.length()==60) {
						if(!sb2.equals(sb)) {
							Parameter[] param= {
								new Parameter(sb.substring(0,3),Parameter.IN),
								new Parameter(ip,Parameter.IN),
								new Parameter(retu,Parameter.OUT)
							};
							DBHelper.execProcedure("{call PKG_RFID.P_TRUCK_RFID_ARRIVE(?,?,?)}", param);
							System.out.println("ִ�д洢���̣�"+"����ֵ��"+retu);
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
					//Ҫ����Ϊ΢�룬�ͳ���1000���Ϳ���
					System.out.println("ʱ������"+costTime+"��ʼ��"+begintime+"������"+endtime+"  �����"+jiange);
						
				}else {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.warn("ip:"+ip+"�˿ڣ�"+port+" �쳣�Ͽ�������...");
					System.out.println("�������ѶϿ�");
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
				logger.warn("ip:"+ip+"�˿ڣ�"+port+" ����ʧ��");
				System.out.println("����ʧ��");
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
		
		cont=0;//ȷ��ÿ���������ӣ�����10*1000�������Ƿ����ӳɹ�
		sb="";
		Timer timer=new Timer();
		TimerTask task=new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(cont!=0) {
					
					if(sb.equals("")) {
						logger.warn("ip:"+ip+"�˿ڣ�"+port+" ��һ������10��δ��Ӧ");
						System.out.println(port+"�˿�"+jishi/1000+"��δ��Ӧ������ʧ��");
						//10��δ��Ӧ����ʧ�ܣ��ر�scoket��I/O��
						
						disconnect();
					}
					connectStatus=cS;	
					System.out.println(jishi/1000+"���"+new Date());
					//��ʱֹͣ
					timer.cancel();
				}else  cont=1;
					
			}
				
		};
			
		timer.schedule(task, 0, jishi);
	}
	
	/**
	 *  ÿ���뷢��һ��������
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
//          				System.out.println("socket�ѹر�");
//          			}
          			//socket.setSoTimeout(9000);
          			outputStream = socket.getOutputStream();
          			outputStream.write(0xFF);
          			boolean p=ping(ip, 3, 3000);
          			if(! p) {
          				disconnect();
          				logger.warn("ip:"+ip+"�˿ڣ�"+port+" δpingͨ������...");
              			System.out.println(port+"::��������");
              			connectStatus=0;
          			}
          			System.out.println(p+"��pingf�Ľ��");
          			//outputStream.flush();
          			//socket.sendUrgentData();
          			System.out.println(port+"::�����������һ��");
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
          			logger.warn("ip:"+ip+"�˿ڣ�"+port+" �쳣�Ͽ�������...");
          			System.out.println(port+"::��������");
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
	    Runtime r = Runtime.getRuntime();  // ��Ҫִ�е�ping����,��������windows��ʽ������  
	    String pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;  
	    try {   // ִ�������ȡ���  
	    	System.out.println(pingCommand);   
	        Process p = r.exec(pingCommand);   
	        if (p == null) {    
	        	return false;   
	        }
	        in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // ���м�����,�������Ƴ���=23ms TTL=62�����Ĵ���  
	        int connectedCount = 0;   
	        String line = null;   
	        while ((line = in.readLine()) != null) {    
	        	connectedCount += getCheckResult(line);   
	        }   // �����������=23ms TTL=62����������,���ֵĴ���=���Դ����򷵻���  
	        return connectedCount == pingTimes;  
	    } catch (Exception ex) {   
	        ex.printStackTrace();   // �����쳣�򷵻ؼ�  
	        return false;  
	    } finally {   
	        try {    
	           in.close();   
	        } catch (IOException e) {    
	           e.printStackTrace();   
	        }  
	    }
	 }
	//��line����=18ms TTL=16����,˵���Ѿ�pingͨ,����1,��t����0.
	private static int getCheckResult(String line) {  // System.out.println("����̨����Ľ��Ϊ:"+line);  
		Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",    Pattern.CASE_INSENSITIVE);  
	    Matcher matcher = pattern.matcher(line);  
	    while (matcher.find()) {
	    	return 1;
	    }
	    return 0; 
	}
}
