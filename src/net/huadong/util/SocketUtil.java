package net.huadong.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketUtil {
	    private Socket socket=null;
	    private String ip;
	    private int port;
	    private OutputStream outputStream;
	    //
	    private BufferedInputStream bis;
	    private DataInputStream dis;
	    private static SocketUtil instance=null;
	    /**
	     * 连接状态：正常
	     */
	    public static final int STATUS_CONNECT = 0;
	    /**
	     * 连接状态：断开连接
	     */
	    public static final int STATUS_DISCONNECT = 1;
	    /**
	     * 连接状态：连接失败
	     */
	    public static final int STATUS_PAIRED_ERROR = 2;
	    /**
	     * 连接状态：还未建立连接
	     */
	    public static final int STATUS_UN_REQUEST = 3;
	    /**
	     * 状态
	     */
	    private final AtomicInteger fStatus = new AtomicInteger(STATUS_UN_REQUEST);

	    public static SocketUtil getInstance()
	    {
	        if(instance==null)
	            instance=new SocketUtil();
	        return instance;
	    }
	    public void connect(final String ip, final int port)
	    {
//	        if(socket!=null)
//	            disconnect();
	        new Thread(){
	            @Override
	            public void run() {
	                try
	                {
	                    socket = new Socket(ip, port);
	                    fStatus.set(STATUS_CONNECT);
	                   // Log.i("SocketUtil","已连接");
	                }
	                catch (Exception ex)
	                {
	                    ex.printStackTrace();
	                }
	            }
	        }.start();
	    }
	    
	    public synchronized void write(byte[] data)
	    {
	        try
	        {
	            if(socket==null)
	                return;
	            outputStream = socket.getOutputStream();
	            outputStream.write(data);
	            outputStream.flush();
	           // Log.i("SocketUtil","发："+ByteUtil.BinaryToHexString(data));
	            //outputStream.close();
	            System.out.println(data.toString());
	        }catch (Exception ex)
	        {
	            fStatus.set(STATUS_DISCONNECT);
	            ex.printStackTrace();
	        }
	    }
	  
	    public synchronized byte[] read()
	    {
	        try{
	            if(socket==null)
	            {
	                //Log.i("SocketUtil","socket已关闭");
	                return null;
	            }
	            bis=new BufferedInputStream(socket.getInputStream());
	            dis=new DataInputStream(bis);

	            byte[] bFrame=new byte[1024*10];
	            int length=0,iRet=0;
	            byte[] bframe;
	            length=dis.read(bFrame);
	            if(length!=-1)
	            {
	                bframe=new byte[length];
	                System.arraycopy(bFrame,0,bframe,0,length);
	                //Log.d("SocketUtil","收:"+ByteUtil.BinaryToHexString(bframe));
	                return bframe;
	            }
	        }catch (Exception ex)
	        {
	            fStatus.set(STATUS_DISCONNECT);
	            ex.printStackTrace();
	        }
	        return null;
	    }
	    public void disconnect()
	    {
	        try
	        {
	            if(dis!=null)
	            dis.close();
	            if(bis!=null)
	            bis.close();
	            if(outputStream!=null)
	            outputStream.close();
	            if(socket!=null)
	            socket.close();
	            fStatus.set(STATUS_DISCONNECT);
	        }catch (Exception ex)
	        {
	            ex.printStackTrace();
	        }
	    }
	    
	   
	    
}
