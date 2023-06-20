import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class apiHandler extends GUI {
	static JSONParser jsonParser = new JSONParser();
	static final String ZIP_API_KEY = "***";
	static final String WEATHER_API_KEY = "***";
	
	static apiHandler ApiHandler;
	
	public apiHandler(){
		super();
	}
	
	public double[] getDistance(String Zip1, String Zip2) {
		double[] apiResponse = getAPIpages("https://www.zipcodeapi.com/rest/"+ZIP_API_KEY+"/distance.json/"+Zip1+"/"+Zip2+"/km");
		
		return apiResponse;
	}

	public static double[] getCord(String zipCode) {
		double[] apiResponse = getAPIpages("https://www.zipcodeapi.com/rest/"+ZIP_API_KEY+"/info.json/"+zipCode+"/degrees");
		
		return apiResponse;
	}
	
	public static double [] getAPIpages(String url) {
		String response = "";
		try {
			System.out.println(url);
			
			URL url1 = new URL(url);
			
			HttpURLConnection con = (HttpURLConnection) url1.openConnection();
			//change status to "get"
			con.setRequestMethod("GET");
			//opes the url streaam
			try{
				InputStream inputStream = url1.openStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				//create a new string, copying everything on the output side
				String line = bufferedReader.readLine();
				while(line != null) {
					response += line+"\n";
					line = bufferedReader.readLine();}
				
			
				JSONObject jsonResponse = new JSONObject();
				jsonResponse = (JSONObject) jsonParser.parse(response);
				
				double[] ans = new double[2];
				//if the url asks for distance AND the response contains the key "distance"
				if( url.indexOf("km") != -1 && jsonResponse.containsKey("distance") ) {				
					ans[0] = (double) jsonResponse.get("distance");
					return ans;
				}
				//else if the url asks for coordinates AND contains the key word for latitude and longitude
				else if(url.indexOf("degrees") != 1 && (jsonResponse.containsKey("lat") && jsonResponse.containsKey("lng"))) {
					ans[0] = (double) jsonResponse.get("lat");
					ans[1] = (double) jsonResponse.get("lng");
					return ans;
				}
			} catch (ParseException e) {
				System.out.print("Nope");
			}
			catch(java.io.IOException e){
				System.out.println("Nope, Please Try Again");
			}
			
			}catch(Exception e) {
				System.out.println("Nope, Please Try Again");
			}
		
		
		return null;
	}
	
	public static String getAPIpage(String url) {
		String response = "";
		try {
			System.out.println(url);
			
			URL url1 = new URL(url);
			
			HttpURLConnection con = (HttpURLConnection) url1.openConnection();
			//change status to "get"
			con.setRequestMethod("GET");
			//opes the url streaam
			try{
				InputStream inputStream = url1.openStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				//create a new string, copying everything on the output side
				String line = bufferedReader.readLine();
				while(line != null) {
					response += line+"\n";
					line = bufferedReader.readLine();}}
			catch(java.io.IOException e){
				System.out.println("Nope, Please Try Again");
			}
			
			}catch(Exception e) {
				System.out.println("Nope, Please Try Again");
			}
		
		
		return response;
	}
	
}
