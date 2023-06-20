
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Weather {
    public double[] day1;
    public double[] day2;
    public double[] day3;
    public double[] day4;
    public double[] day5;
    public double[] day6;
    public double[] day7;
    public double[] day8;
    
    public String[] weatherDescriptions;
    
    public ArrayList<double[]> days = new ArrayList<>();
    
    //this variable of parser can be united to one in the main class with a static, but for now, we can make multiple instances
    JSONParser parser = new JSONParser();
    
    public Weather(String zipCode) {
        double[] cord = apiHandler.getCord(zipCode); 
       
        String apiResponse = apiHandler.getAPIpage("https://api.openweathermap.org/data/2.5/onecall?lat="+cord[0]+"&lon="+cord[1]
        		+"&exclude=current,minutely,hourly,alerts&appid="+apiHandler.WEATHER_API_KEY);
        System.out.println(apiResponse);
        
        //get selected attributes
        try {
            JSONArray jsonResponse = ((JSONArray) ((JSONObject) parser.parse(apiResponse)).get("daily"));
            
            day1 = initWeatherDay((JSONObject) jsonResponse.get(0));
            day2 = initWeatherDay((JSONObject) jsonResponse.get(1));
            day3 = initWeatherDay((JSONObject) jsonResponse.get(2));
            day4 = initWeatherDay((JSONObject) jsonResponse.get(3));
            day5 = initWeatherDay((JSONObject) jsonResponse.get(4));
            day6 = initWeatherDay((JSONObject) jsonResponse.get(5));
            day7 = initWeatherDay((JSONObject) jsonResponse.get(6));
            day8 = initWeatherDay((JSONObject) jsonResponse.get(7));
            
            weatherDescriptions = new String[8];
            for(int i=0; i < 8; i++) {
            	weatherDescriptions[i] = (String) ((JSONObject) ((JSONArray) ((JSONObject) jsonResponse.get(i)).get("weather")).get(0)).get("main");
            	System.out.println(weatherDescriptions[i]);
            }

        } catch (ParseException e) {
            e.printStackTrace();
          
        }

        days.add(day1);
        System.out.println(day1);
        days.add(day2);
        System.out.println(day2);
        days.add(day3);
        System.out.println(day3);
        days.add(day4);
        System.out.println(day4);
        days.add(day5);
        System.out.println(day5);
        days.add(day6);
        System.out.println(day6);
        days.add(day7);
        days.add(day8);
    }
    
    public double[] getDay(int i) {
    	return days.get(i);
    }

    private double[] initWeatherDay(JSONObject weatherJson){
        double[] returnArr = new double[6];
        returnArr[0] = (double) ((JSONObject) weatherJson.get("temp")).get("day") - 273;
        returnArr[1] = (double) ((JSONObject) weatherJson.get("temp")).get("night") - 273;
        returnArr[2] = ((long) weatherJson.get("humidity") );
        return returnArr;
    }
    
    public static double round(double value, int places) {
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long temp = Math.round(value);
        return (double) temp / factor;
    }
}