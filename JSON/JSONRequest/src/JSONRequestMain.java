import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;



public class JSONRequestMain {
	
	public static void main(String[] args) {
		
		// creating job detail using quartz job scheduler.
		int timeToMakeRequest = 1 ;//in minutes
		JobDetail job = JobBuilder.newJob(MakeRequestAndManipulate.class)
				.withIdentity("CallUrl", "group1").build();
		//create tigger to call job
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("CallUrl", "group1").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(timeToMakeRequest).repeatForever()).build();

	   try{
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(job,trigger);

	   }catch(Exception e){
			 e.printStackTrace();
		 }

}


}