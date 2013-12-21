import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class MakeRequestAndManipulate implements Job {

	public void execute(JobExecutionContext context)
	throws JobExecutionException {
 
		
		
		JSONRequesterWithParser jrwp = new JSONRequesterWithParser();
		//String url = "https://www.drive-now.com/php/metropolis/json.vehicle_filter?cit=6099";//For Berlin
		//Berlin 6099, Cologne 1774, Dusseldorf 1293, Hamburg 40065, Munich 4604
		String url = "https://www.drive-now.com/php/metropolis/json.vehicle_filter?cit=4604 "; 
		String response = jrwp.Call(url);
		jrwp.ParseAndSaveJSONResponse(response);
		System.out.println("Request sent and resoponse saved.");
	}
}
