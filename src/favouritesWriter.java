import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class favouritesWriter extends GUI {
	
	
	
	public favouritesWriter(){
		super();
	}
	//Overloaded write() method in GUI class
	public void write(String state,String zip, String endzip) {
		BufferedWriter writer = null;
		try 
		{
			FileWriter fw = new FileWriter(GUI.DOCUMENT_FOLDER+"/Favorites", true);
			writer = new BufferedWriter(fw);
			writer.write(state);
			writer.write(" ");
			writer.write(zip);
			writer.write(" ");
			writer.write(endzip);
			writer.newLine();
			writer.flush();
			writer.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> places = new ArrayList<String>();
	public ArrayList<String> read() {
		String st;
		try {
			File stateFile = new File(GUI.DOCUMENT_FOLDER+"/Favorites");
			if(!stateFile.exists()) return null;
			BufferedReader br = new BufferedReader(new FileReader(stateFile)); 
			
		  //reads the first line
		  st = br.readLine();
		  while(st != null) {
			  //While theres still line
			  if(st.length() > 0) {			
				  //add to the first place(index 0) of the arraylist
				  places.add(0,st);
			  }
			  //read new line
			  st = br.readLine();
		  }
		  //return the arraylist
		  br.close();
		  return places;
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
