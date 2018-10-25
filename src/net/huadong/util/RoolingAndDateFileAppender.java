package net.huadong.util;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
 
 
 
 
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
public class RoolingAndDateFileAppender extends RollingFileAppender{
	private String datePattern;
	private String dateStr="";//�ļ����������
	private String expirDays="1";//�����������
	private String isCleanLog="true";
	private String maxIndex="100";
	private File rootDir;
	public void setDatePattern(String datePattern){
		if(null!=datePattern&&!"".equals(datePattern)){
			this.datePattern=datePattern;
		}
	}
	public String getDatePattern(){
		return this.datePattern;
	}
	public void rollOver(){
		dateStr=new SimpleDateFormat(this.datePattern).format(new Date(System.currentTimeMillis()));
		File target = null;
		File file=null;
		if(qw!=null){
			long size=((CountingQuietWriter)this.qw).getCount();
			LogLog.debug("rolling over count="+size);
		}
		LogLog.debug("maxBackupIndex="+this.maxBackupIndex);
		//���maxIndex<=0��������
		if(maxIndex!=null&&Integer.parseInt(maxIndex)>0){
			//ɾ�����ļ�
			file=new File(this.fileName+'.'+dateStr+'.'+Integer.parseInt(this.maxIndex));
			if(file.exists()){
				//���������־�ﵽ���������������ɾ�������һ����־��������־Ϊβ�ż�һ
				Boolean boo = reLogNum();
				if(!boo){
					LogLog.debug("��־����������ʧ�ܣ�");
				}
			}
	}
		//��ȡ���������ļ�����
		int count=cleanLog();
		//�������ļ�
		target=new File(fileName+"."+dateStr+"."+(count+1));
		this.closeFile();
		file=new File(fileName);
		LogLog.debug("Renaming file"+file+"to"+target);
		file.renameTo(target);
		try{
			setFile(this.fileName,false,this.bufferedIO,this.bufferSize);
		}catch(IOException e){
			LogLog.error("setFile("+this.fileName+",false)call failed.",e);
		}
	}
	public int cleanLog(){
		int count=0;//��¼�����ļ�����
		if(Boolean.parseBoolean(isCleanLog)){
			File f=new File(fileName);
			rootDir=f.getParentFile();
			File[] listFiles = rootDir.listFiles();
			for(File file:listFiles){
				if(file.getName().contains(dateStr)){
					count=count+1;//�ǵ�����־����+1
				}else{
					if(Boolean.parseBoolean(isCleanLog)){
						//���������־
						String[] split=file.getName().split("\\\\")[0].split("\\.");
						//У����־���֣���ȡ�����ڣ��жϹ���ʱ��
						if(split.length==4 && isExpTime(split[2])){
							file.delete();
					}
				}
				}
			}
		}
		return count;
	}
	public Boolean isExpTime(String time){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date logTime=format.parse(time);
			Date nowTime=format.parse(format.format(new Date()));
			//�����־�뵱ǰ��������
			int days=(int)(nowTime.getTime()-logTime.getTime())/(1000*3600*24);
			if(Math.abs(days)>=Integer.parseInt(expirDays)){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			LogLog.error(e.toString());
			return false;
		}
	}
	/**
	 * ���������־�ﵽ���������������ÿ��ɾ��β��Ϊ1����־��
	 * ������־������μ�ȥ1��������
	 * @return
	 */
	public Boolean reLogNum(){
		boolean renameTo=false;
		File startFile = new File(this.fileName+'.'+dateStr+'.'+"1");
		if(startFile.exists()&&startFile.delete()){
			for(int i=2;i<=Integer.parseInt(maxIndex);i++){
				File target = new File(this.fileName+'.'+dateStr+'.'+(i-1));
				this.closeFile();
				File file = new File(this.fileName+'.'+dateStr+'.'+i);
				renameTo=file.renameTo(target);
			}
		}
		return renameTo;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getExpirDays() {
		return expirDays;
	}
	public void setExpirDays(String expirDays) {
		this.expirDays = expirDays;
	}
	public String getIsCleanLog() {
		return isCleanLog;
	}
	public void setIsCleanLog(String isCleanLog) {
		this.isCleanLog = isCleanLog;
	}
	public String getMaxIndex() {
		return maxIndex;
	}
	public void setMaxIndex(String maxIndex) {
		this.maxIndex = maxIndex;
	}
	
  }
 
