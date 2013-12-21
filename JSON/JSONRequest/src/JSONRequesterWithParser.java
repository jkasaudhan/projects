import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class JSONRequesterWithParser {
	public String Call(String url){
		StringBuffer response = new StringBuffer();
		try{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
	
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			
		}
		in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return response.toString();
	}
	
	public void ParseAndSaveJSONResponse(String response){
		try{
		JSONObject jsonData = new JSONObject(response);

		JSONObject rec = jsonData.getJSONObject("rec");
		//System.out.println(rec.toString());
		JSONObject veh = rec.getJSONObject("vehicles");
		//System.out.println(veh.toString());
		JSONArray vehicles = (JSONArray)veh.get("vehicles");
		//System.out.println(vehcles);
		
		System.out.println("No of vehicles data found :" + vehicles.length());
		List<String> vehicleList = new ArrayList<>();
		
		
		for(int item = 0; item < vehicles.length();item++){
			//properties for single vehicle
			JSONObject vehData = vehicles.getJSONObject(item);
			JSONObject position = vehData.getJSONObject("position");
			
			VehicleData vehicle = new VehicleData();
			Date currentDateTime = new Date();
			vehicle.dateTime = currentDateTime.toString();
			vehicle.address = position.getString("address");
			vehicle.latitude = position.getString("latitude");
			vehicle.longitude = position.getString("longitude");
			vehicle.carName = vehData.getString("carName");
			vehicle.licensePlate = vehData.getString("licensePlate");
			vehicle.group = vehData.getString("group");
			
			String filePath =  FindFilePath(vehicle.address);
			Save(vehicle,filePath);
			
			
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
}

	private void Save(VehicleData vehicle,String filePath) {
		//print each vehicle information in file in tabular format
		try{
		//File file = new File("E:\\Projects\\Car2Go\\car1.txt"); // just for testing.
		//System.out.println("File Path"+filePath);
		File file = new File(filePath);
		FileWriter fw = null;
		if(!file.exists()){
			file.createNewFile();
			fw = new FileWriter(file.getAbsoluteFile());
			String header = "Date Time"	
					 + "\t" + "Address"
					 + "\t" + "Car Name"
					 + "\t" + "License Plate"
					 + "\t" + "Group"
					 + "\t" + "Latitude"
					 + "\t" + "Longitude"	+"\n";
			fw.write(header);
		}else {
			fw = new FileWriter(file.getAbsoluteFile(),true);
			String formatedVehicleData = vehicle.dateTime 
					 + "\t" + vehicle.address 
					 + "\t" + vehicle.carName
					 + "\t" + vehicle.licensePlate
					 + "\t" + vehicle.group
					 + "\t" + vehicle.latitude 
					 + "\t" + vehicle.longitude	+"\n";
			fw.write(formatedVehicleData);
			
		}
		fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	private String FindFilePath(String cityRef){
		String[] cityName = cityRef.split(" ");
		Date date = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-dd-HH-mm");
		String timeStamp =  dateFormatter.format(date);
		//String filePath = "E:\\Projects\\Car2Go\\" + cityName[cityName.length - 1] + ".txt"; // last word of address would have city name but this is not the exact case with drive-now api.
		File fileDirectory = new File(System.getProperty("user.dir") + "\\DriveNowDocs\\Munich1") ;
		String filePath =  fileDirectory + "\\Munich" + timeStamp +".txt"; 
		System.out.println(filePath);
		if(!fileDirectory.exists()){
			fileDirectory.mkdirs();
		}
		return filePath;
	}

}
