package net.huadong.util;

public class BytesUtil {

	/**
     * byte[] 转为16进制String
     */
    public static String Bytes2HexString(byte[] b) { 
        String ret = ""; 
        for (int i = 0; i < b.length; i++) { 
            String hex = Integer.toHexString(b[i] & 0xFF); 
            if (hex.length() == 1) { 
                hex = '0' + hex; 
            } 
            ret += hex.toUpperCase(); 
        } 
        return ret; 
    } 
    
    /**
     * 从一个byte[]数组中截取一部分
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i=begin;i<begin+count; i++) 
        	bs[i-begin] = src[i];
        return bs;
    }
    
    //转化十六进制编码为字符串
    public static String toStringHex(String s)
    {
        byte[] baKeyword = new byte[s.length()/2];
        for(int i = 0; i < baKeyword.length; i++)
        {
          try
          {
              baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16));
          }
          catch(Exception e)
          {
              e.printStackTrace();
          }
        }
     
        try 
        {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        } 
        catch (Exception e1) 
        {
            e1.printStackTrace();
        } 
        return s;
    }
}
