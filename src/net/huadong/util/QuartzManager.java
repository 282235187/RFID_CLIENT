package net.huadong.util;

import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzManager {  
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();  //����һ��SchedulerFactory����ʵ��
    private static String JOB_GROUP_NAME = "FH_JOBGROUP_NAME";                      //������
    private static String TRIGGER_GROUP_NAME = "FH_TRIGGERGROUP_NAME";              //��������
  
    /**���һ����ʱ����ʹ��Ĭ�ϵ�������������������������������  
     * @param jobName ������
     * @param cls ����
     * @param time ʱ�����ã��ο�quartz˵���ĵ�
     */
    public static void addJob(String jobName, Class<? extends Job> cls, String time) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();//ͨ��SchedulerFactory����Scheduler����
            JobDetail jobDetail= JobBuilder.newJob(cls).withIdentity(jobName,JOB_GROUP_NAME).build();//��������Jobʵ���༰������һЩ��̬��Ϣ������һ����ҵʵ��
            CronTrigger trigger = (CronTrigger) TriggerBuilder
                    .newTrigger() //����һ���µ�TriggerBuilder���淶һ��������
                    .withIdentity(jobName, TRIGGER_GROUP_NAME)//����������һ�����ֺ�����
                    .withSchedule(CronScheduleBuilder.cronSchedule(time))
                    .build();
            sched.scheduleJob(jobDetail, trigger);  
            if (!sched.isShutdown()) {  
                sched.start();        // ����  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    /**���һ����ʱ����ʹ��Ĭ�ϵ�������������������������������  ����������
     * @param jobName ������
     * @param cls ����
     * @param time ʱ�����ã��ο�quartz˵���ĵ�
     */
    public static void addJob(String jobName, Class<? extends Job> cls, String time, Map<String,Object> parameter) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();//ͨ��SchedulerFactory����Scheduler����
            JobDetail jobDetail= JobBuilder.newJob(cls).withIdentity(jobName,JOB_GROUP_NAME).build();//��������Jobʵ���༰������һЩ��̬��Ϣ������һ����ҵʵ��
            jobDetail.getJobDataMap().put("parameterList", parameter);//������
            CronTrigger trigger = (CronTrigger) TriggerBuilder
                    .newTrigger()//����һ���µ�TriggerBuilder���淶һ��������
                    .withIdentity(jobName, TRIGGER_GROUP_NAME)//����������һ�����ֺ�����
                    .withSchedule(CronScheduleBuilder.cronSchedule(time))
                    .build();
            sched.scheduleJob(jobDetail, trigger);  
            if (!sched.isShutdown()) {  
                sched.start();        // ����  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /**���һ����ʱ���� 
     * @param jobName    ������ 
     * @param jobGroupName    �������� 
     * @param triggerName    �������� 
     * @param triggerGroupName    ���������� 
     * @param jobClass    ���� 
     * @param time    ʱ�����ã��ο�quartz˵���ĵ� 
     */
    public static void addJob(String jobName, String jobGroupName,  
            String triggerName, String triggerGroupName, Class<? extends Job> jobClass,  
            String time) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  
            JobDetail jobDetail= JobBuilder.newJob(jobClass).withIdentity(jobName,jobGroupName).build();// �������������飬����ִ����
            CronTrigger trigger = (CronTrigger) TriggerBuilder// ������  
                    .newTrigger()
                    .withIdentity(triggerName, triggerGroupName)
                    .withSchedule(CronScheduleBuilder.cronSchedule(time))
                    .build();
            sched.scheduleJob(jobDetail, trigger);
            if (!sched.isShutdown()) {  
                sched.start();// ����  
            } 
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    /**���һ����ʱ����  ����������
     * @param jobName    ������ 
     * @param jobGroupName    �������� 
     * @param triggerName    �������� 
     * @param triggerGroupName    ���������� 
     * @param jobClass    ���� 
     * @param time    ʱ�����ã��ο�quartz˵���ĵ� 
     */
    public static void addJob(String jobName, String jobGroupName,  
            String triggerName, String triggerGroupName, Class<? extends Job> jobClass,  
            String time, Map<String,Object> parameter) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  
            JobDetail jobDetail= JobBuilder.newJob(jobClass).withIdentity(jobName,jobGroupName).build();// �������������飬����ִ����
            jobDetail.getJobDataMap().put("parameterList", parameter);//������
            CronTrigger trigger = (CronTrigger) TriggerBuilder// ������  
                    .newTrigger()
                    .withIdentity(triggerName, triggerGroupName)
                    .withSchedule(CronScheduleBuilder.cronSchedule(time))
                    .build();
            sched.scheduleJob(jobDetail, trigger);
            if (!sched.isShutdown()) {  
                sched.start();// ����  
            } 
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    } 
  
    /** �޸�һ������Ĵ���ʱ��(ʹ��Ĭ�ϵ�������������������������������) 
     * @param jobName    ������ 
     * @param time    �µ�ʱ������
     */
    public static void modifyJobTime(String jobName, String time) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();//ͨ��SchedulerFactory����Scheduler����
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName,TRIGGER_GROUP_NAME);//ͨ������������������ȡTriggerKey
            CronTrigger trigger = (CronTrigger)sched.getTrigger(triggerKey);//ͨ��TriggerKey��ȡCronTrigger
            if (trigger == null) {  
                return;  
            }  
            String oldTime = trigger.getCronExpression();  
            if (!oldTime.equalsIgnoreCase(time)) {  
                JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);//ͨ����������������ȡJobKey
                JobDetail jobDetail = sched.getJobDetail(jobKey); 
                Class<? extends Job> objJobClass = jobDetail.getJobClass();  
                removeJob(jobName);  
                addJob(jobName, objJobClass, time);  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    /**�޸�һ������Ĵ���ʱ�� 
     * @param triggerName    ��������
     * @param triggerGroupName    ����������������
     * @param time    ���º��ʱ�����
     */
    public static void modifyJobTime(String triggerName, String triggerGroupName, String time) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();                              //ͨ��SchedulerFactory����Scheduler����
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,triggerGroupName);     //ͨ������������������ȡTriggerKey
            CronTrigger trigger = (CronTrigger)sched.getTrigger(triggerKey);                //ͨ��TriggerKey��ȡCronTrigger
            if (trigger == null)  return;  
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(trigger.getCronExpression());
            String oldTime = trigger.getCronExpression();  
            if (!oldTime.equalsIgnoreCase(time)) {  
                trigger = (CronTrigger)trigger.getTriggerBuilder()        //���¹���trigger
                        .withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder)
                        .withSchedule(CronScheduleBuilder.cronSchedule(time))
                        .build();
                sched.rescheduleJob(triggerKey, trigger);                //���µ�trigger��������jobִ��
            }
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    /**�Ƴ�һ������(ʹ��Ĭ�ϵ�������������������������������) 
     * @param jobName    ��������
     */
    public static void removeJob(String jobName) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName,TRIGGER_GROUP_NAME);     //ͨ������������������ȡTriggerKey
            JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);                        //ͨ����������������ȡJobKey
            sched.pauseTrigger(triggerKey);    // ֹͣ������  
            sched.unscheduleJob(triggerKey);// �Ƴ�������  
            sched.deleteJob(jobKey);        // ɾ������  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    /**�Ƴ�һ������
     * @param jobName    ������
     * @param jobGroupName    ��������
     * @param triggerName    ��������
     * @param triggerGroupName    ����������
     */
    public static void removeJob(String jobName, String jobGroupName,String triggerName, String triggerGroupName) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,triggerGroupName);     //ͨ������������������ȡTriggerKey
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);                            //ͨ����������������ȡJobKey
            sched.pauseTrigger(triggerKey);    // ֹͣ������  
            sched.unscheduleJob(triggerKey);// �Ƴ�������  
            sched.deleteJob(jobKey);        // ɾ������  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    } 
    
    /**
     * �������ж�ʱ���� 
     */
    public static void startJobs() {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  
            sched.start();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /**
     * �ر����ж�ʱ���� 
     */
    public static void shutdownJobs() {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  
            if (!sched.isShutdown()) {  
                sched.shutdown();  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
}
