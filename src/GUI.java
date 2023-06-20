/*Runner file, note that the program uses file writing and reading...
requires extensive setup to run(password.txt, favorites.txt, states.xml). Please contact if you need the jar file. */

/* Otherwise, please follow what is shown the video. Input a state, enter two VALID zipcodes(For example, 92804, Anaheim and 94209, Sacramento) Not personal info dont worry
 * (Data sanitation was also included, but not tested extensively)
The output would be the weather data, Day Temperature, Night Temperature, humidity as well as the distance. 
The custom perferences is located on the top right of the output page, click it will allow you to customise the advisor setting*/
//(If it fits in the requirement, it will say advised To go, if not, it will say advised NOT to go)

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.JTextField;
import java.awt.Choice;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import java.io.BufferedReader;
import javax.swing.SwingConstants;   
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.FileWriter;


public class GUI {
	
	
	private JFrame frame;
	private HashMap<String, String> states = new HashMap<String, String>();
	private String[] stateFullState;
	public String inputus;
	private JTextField startZip; ////
	private JTextField endZip;
	private JTextField inputTempDay;
	private JTextField inputTempnight;
	private JTextField inputhumidity;
	private JTextField maxtempday;
	private JTextField maxtempnight;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	
	public static String DOCUMENT_FOLDER;
	apiHandler get;
	double distance[];
	String str;
	Weather weather;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		DOCUMENT_FOLDER = System.getProperty("user.home")+"/weatherForcast-jyeh";

		File f = new File(DOCUMENT_FOLDER);
		if(!f.exists()) (new File(DOCUMENT_FOLDER)).mkdirs();
		
		System.out.println("!!! "+DOCUMENT_FOLDER);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		states = getStates();
		stateFullState = (String[]) states.keySet().toArray(new String[0]);
	

		frame = new JFrame();
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		starterScreen("");
	}
	
	public HashMap<String,String> getStates(){
		HashMap<String, String> returnVar = new HashMap<String, String>();
		

		try {
		    InputStreamReader isReader= new InputStreamReader(this.getClass().getResourceAsStream("states"));
		    BufferedReader br = new BufferedReader(isReader);  
			
			br.readLine(); //first line as xml
			String st; 
			while ((st = br.readLine()) != null) {
				String[] stArray = st.split(",");
				if(stArray.length >= 2){
					returnVar.put(stArray[0], stArray[1]);
				}
			} 
		}catch(IOException e) {
			e.printStackTrace();
		}
		return returnVar;
	}
	/**
	 * Initialize the contents of the frame.
	 */
	
	
	public void starterScreen(String name) {
		//
		Choice states1 = new Choice();
		states1.setBounds(64, 175, 192, 18);
		frame.getContentPane().add(states1);
		for(int i = 0; i < stateFullState.length; i++) {
			states1.add(stateFullState[i]);
		}
		//
		JButton recent = new JButton("Favorites");
		recent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
				favouritesPanel(name);
			}
		});
		recent.setFont(new Font("Tahoma", Font.PLAIN, 18));
		recent.setBounds(466, 10, 110, 51);
		frame.getContentPane().add(recent);

		//
		
		
		//
		startZip = new JTextField();
		startZip.setBounds(321, 174, 235, 42);
		frame.getContentPane().add(startZip);
		startZip.setColumns(10);
		
		//
		

		
		//
		JButton next = new JButton("Next");
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputzip = startZip.getText();
				String endzip = endZip.getText();
				String selectedstate = (String) states1.getSelectedItem();
				
				try {
					get = new apiHandler();
					distance = get.getDistance(inputzip, endzip);
					str = String.valueOf(distance[0]);
					weather = new Weather(endzip);
				}catch(Exception e3) {
					
					JOptionPane.showMessageDialog(frame, "Incorrect input, Please Try again");
				}
				
				
				(new favouritesWriter()).write(selectedstate,inputzip,endzip);
				
				remove();
				weatherPanel(name, inputzip ,endzip);
				
			}
		});
		
		next.setFont(new Font("Tahoma", Font.PLAIN, 20));
		next.setBounds(204, 361, 154, 42);
		frame.getContentPane().add(next);
		
		JLabel lblNewLabel = new JLabel("Select State:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblNewLabel.setBounds(84, 132, 121, 36);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblEnterAddress = new JLabel("Enter Start ZIP:");
		lblEnterAddress.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblEnterAddress.setBounds(377, 128, 140, 36);
		frame.getContentPane().add(lblEnterAddress);
		
		JLabel username = new JLabel("Welcome! " + name);
		username.setHorizontalAlignment(SwingConstants.CENTER);
		username.setFont(new Font("Tahoma", Font.PLAIN, 20));
		username.setBounds(204, 76, 141, 42);
		frame.getContentPane().add(username);
		
		endZip = new JTextField();
		endZip.setColumns(10);
		endZip.setBounds(321, 286, 235, 42);
		frame.getContentPane().add(endZip);
		
		JLabel lblEnterEndZip = new JLabel("Enter End ZIP:");
		lblEnterEndZip.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblEnterEndZip.setBounds(377, 240, 140, 36);
		frame.getContentPane().add(lblEnterEndZip);
		
	}
	
	
	public void weatherPanel(String name,String startzip, String endzip){
		ArrayList<String> day = new ArrayList<>();
		double temperatureDay = weather.getDay(day.size())[0];
		double temperatureNight = weather.getDay(day.size())[1];
		double humidity = weather.getDay(day.size())[2];
		String description = weather.weatherDescriptions[day.size()];
		
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
				starterScreen(name);
			}
		});
		back.setFont(new Font("Tahoma", Font.PLAIN, 20));
		back.setBounds(10, 401, 110, 51);
		frame.getContentPane().add(back);
		
		JLabel firstzip = new JLabel(startzip);
		firstzip.setHorizontalAlignment(SwingConstants.CENTER);
		firstzip.setFont(new Font("Tahoma", Font.PLAIN, 20));
		firstzip.setBounds(116, 38, 83, 41);
		frame.getContentPane().add(firstzip);
		
		JLabel firstzip_1 = new JLabel(endzip);
		firstzip_1.setHorizontalAlignment(SwingConstants.CENTER);
		firstzip_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		firstzip_1.setBounds(391, 38, 83, 41);
		frame.getContentPane().add(firstzip_1);
		
		JLabel arrow = new JLabel("------------------------------>");
		arrow.setHorizontalAlignment(SwingConstants.CENTER);
		arrow.setFont(new Font("Tahoma", Font.PLAIN, 13));
		arrow.setBounds(202, 53, 179, 13);
		frame.getContentPane().add(arrow);
		
		JLabel distancelabel = new JLabel("Distance:");
		distancelabel.setHorizontalAlignment(SwingConstants.CENTER);
		distancelabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		distancelabel.setBounds(174, 107, 83, 41);
		frame.getContentPane().add(distancelabel);
		
		JLabel daytemplabel = new JLabel("Day Temperature:");
		 daytemplabel.setHorizontalAlignment(SwingConstants.CENTER);
		 daytemplabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		 daytemplabel.setBounds(88, 167, 169, 41);
		frame.getContentPane().add( daytemplabel);
		
		JLabel lblTemperature = new JLabel("Night Temperature:");
		lblTemperature.setHorizontalAlignment(SwingConstants.CENTER);
		lblTemperature.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTemperature.setBounds(72, 234, 196, 41);
		frame.getContentPane().add(lblTemperature);
		
		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDescription.setBounds(153, 360, 110, 41);
		frame.getContentPane().add(lblDescription);
		
		JLabel calcDistance = new JLabel(str);
		calcDistance.setHorizontalAlignment(SwingConstants.CENTER);
		calcDistance.setFont(new Font("Tahoma", Font.PLAIN, 18));
		calcDistance.setBounds(320, 107, 83, 41);
		frame.getContentPane().add(calcDistance);
		
		JLabel tempatday = new JLabel(temperatureDay+" C");
		tempatday.setHorizontalAlignment(SwingConstants.CENTER);
		tempatday.setFont(new Font("Tahoma", Font.PLAIN, 18));
		tempatday.setBounds(320, 167, 83, 41);
		frame.getContentPane().add(tempatday);
		
		JLabel Temperaturenight = new JLabel(temperatureNight+" C");
		Temperaturenight.setHorizontalAlignment(SwingConstants.CENTER);
		Temperaturenight.setFont(new Font("Tahoma", Font.PLAIN, 18));
		Temperaturenight.setBounds(320, 234, 83, 41);
		frame.getContentPane().add(Temperaturenight);
		
		JLabel Weather = new JLabel(humidity+"");
		Weather.setHorizontalAlignment(SwingConstants.CENTER);
		Weather.setFont(new Font("Tahoma", Font.PLAIN, 18));
		Weather.setBounds(320, 299, 83, 41);
		frame.getContentPane().add(Weather);
		
		JLabel humiditylabel = new JLabel("Humidity:");
		humiditylabel.setHorizontalAlignment(SwingConstants.CENTER);
		humiditylabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		humiditylabel.setBounds(139, 299, 129, 41);
		frame.getContentPane().add(humiditylabel);
		
		JLabel Weather_1 = new JLabel(description);
		Weather_1.setHorizontalAlignment(SwingConstants.CENTER);
		Weather_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		Weather_1.setBounds(320, 360, 83, 41);
		frame.getContentPane().add(Weather_1);
		
		JLabel dayDisplay = new JLabel("Day: "+(day.size()+1));
		dayDisplay.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dayDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		dayDisplay.setBounds(239, 85, 94, 21);
		frame.getContentPane().add(dayDisplay);
		
		
		double[] compare = new double[6];
		try {
			File file = new File(GUI.DOCUMENT_FOLDER+"/Preferences");
			if(!file.exists()) write("Preferences", "50\n50\n30\n100\n100");		
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st = br.readLine();
			for(int i = 0; i < 6; i++) {			
				if(st == null) {
					System.out.println("error");
					break;
				}
				try {					
					compare[i] = Double.parseDouble(st);
				}catch(Exception e) {
					System.out.println("not a double");
				}
				st = br.readLine();
			}
			if(st == null) System.out.println("error");
		} catch (IOException e1) {
			remove();
			starterScreen(name);
		}

		double[] current = weather.getDay(day.size());
		int falseCount = 0;
		if(!(compare[4] > current[0] && current[0] > compare[0])) {

			falseCount++;
	}
	if(!(current[5] > compare[1] && current[1] > compare[1])) {

			falseCount++;
	}
	if(current[2] > compare[2]) { falseCount++;}
	String answer;
	
	if(falseCount >= 2 ) {
		answer = "Advised NOT to go";
	}
	else {answer = "Advised to go";}

		
		JLabel lblNewLabel_2 = new JLabel(answer);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(150, 10, 180, 13);
		frame.getContentPane().add(lblNewLabel_2);
		
		
		JButton btnNewButton = new JButton("Next");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				day.add("");
				if(day.size() >= 8) {
					for(int i = day.size() - 1 ; i >= 0; i--) {
						day.remove(i);
					}
				}

				double temperatureDay = weather.getDay(day.size())[0];
				double temperatureNight = weather.getDay(day.size())[1];
				double humidity = weather.getDay(day.size())[2];
				String description = weather.weatherDescriptions[day.size()];
				
				dayDisplay.setText("Day: "+(day.size()+1));
				
				tempatday.setText(temperatureDay+"");
				Temperaturenight.setText(temperatureNight+"");
				Weather.setText(humidity+"");
				Weather_1.setText(description);
				

				
				double[] compare = new double[6];
				try {
					File file = new File(GUI.DOCUMENT_FOLDER+"/Preferences");
					if(!file.exists()) write("Preferences", "50\n50\n30\n100\n100");		
					
					BufferedReader br = new BufferedReader(new FileReader(file));
					String st = br.readLine();
					for(int i = 0; i < 6; i++) {			
						if(st == null) {
							System.out.println("error");
							break;
						}
						try {					
							compare[i] = Double.parseDouble(st);
						}catch(Exception e3) {
							System.out.println("not a double");
						}
						st = br.readLine();
					}
					if(st == null) System.out.println("error");
					
				} catch (IOException e1) {
					remove();
					starterScreen(name);
				}

				double[] current = weather.getDay(day.size());
				int falseCount = 0;
				if(!(compare[4] > current[0] && current[0] > compare[0])) {

					falseCount++;
			}
			if(!(current[5] > compare[1] && current[1] > compare[1])) {

					falseCount++;
			}
			if(current[2] > compare[2]) { falseCount++;}
			String answer;
			
			if(falseCount >= 2 ) {
				answer = "Advised NOT to go";
			}
			else {answer = "Advised to go";}

				lblNewLabel_2.setText(answer);
			}
		});
		btnNewButton.setBounds(320, 420, 85, 21);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(day.size() > 0) {
					day.remove(day.size()-1);
				}else {
					for(int i = 0; i < 7; i++) {
						day.add("");
					}
				}
				double temperatureDay = weather.getDay(day.size())[0];
				double temperatureNight = weather.getDay(day.size())[1];
				double humidity = weather.getDay(day.size())[2];
				String description = weather.weatherDescriptions[day.size()];

				dayDisplay.setText("Day: "+(day.size()+1));
				
				tempatday.setText(temperatureDay+"");
				Temperaturenight.setText(temperatureNight+"");
				Weather.setText(humidity+"");
				Weather_1.setText(description);

				
				double[] compare = new double[6];
				try {
					File file = new File(GUI.DOCUMENT_FOLDER+"/Preferences");
					if(!file.exists()) write("Preferences", "50\n50\n30\n100\n100");		
					
					BufferedReader br = new BufferedReader(new FileReader(file));
					String st = br.readLine();
					for(int i = 0; i < 6; i++) {				
						if(st == null) {
							
							break;
						}
						compare[i] = Double.parseDouble(st);
						st = br.readLine();
					}
				} catch (IOException e1) {
					remove();
					starterScreen(name);
				}
				
				double[] current = weather.getDay(day.size());
				int falseCount = 0;
				if(!(compare[4] > current[0] && current[0] > compare[0])) {

					falseCount++;
			}
			if(!(current[5] > compare[1] && current[1] > compare[1])) {

					falseCount++;
			}
			if(current[2] > compare[2]) { falseCount++;}
			String answer;
			
			if(falseCount >= 2 ) {
				answer = "Advised NOT to go";
			}
			else {answer = "Advised to go";}
				
				lblNewLabel_2.setText(answer);
			}
		});
		btnBack.setBounds(202, 420, 85, 21);
		frame.getContentPane().add(btnBack);
		
		JButton btnNewButton_1 = new JButton("Preferences");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
				showPreferences(name);
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton_1.setBounds(430, 6, 153, 22);
		frame.getContentPane().add(btnNewButton_1);
		

	}
		
		

	

	
	public void showPreferences(String name) {
		
		JLabel lblNewLabel = new JLabel("Preference Setting");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(214, 36, 190, 33);
		frame.getContentPane().add(lblNewLabel);
		//
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
				starterScreen(name);
			}
		});
		back.setFont(new Font("Tahoma", Font.PLAIN, 20));
		back.setBounds(10, 401, 110, 51);
		frame.getContentPane().add(back);
		
		
		inputTempDay = new JTextField();
		inputTempDay.setBounds(308, 101, 80, 19);
		frame.getContentPane().add(inputTempDay);
		inputTempDay.setColumns(10);
		
		
		inputTempnight = new JTextField();
		inputTempnight.setColumns(10);
		inputTempnight.setBounds(308, 140, 80, 19);
		frame.getContentPane().add(inputTempnight);
			
		inputhumidity = new JTextField();
		inputhumidity.setColumns(10);
		inputhumidity.setBounds(308, 300, 80, 19);
		frame.getContentPane().add(inputhumidity);
		
		
		maxtempday = new JTextField();
		maxtempday.setColumns(10);
		maxtempday.setBounds(308, 180, 80, 19);
		frame.getContentPane().add(maxtempday);
		
		maxtempnight = new JTextField();
		maxtempnight.setColumns(10);
		maxtempnight.setBounds(308, 220, 80, 19);
		frame.getContentPane().add(maxtempnight);
		
		JButton Confirm = new JButton("Confirm");
		Confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String mindaytemp = inputTempDay.getText();
				String minnighttemp = inputTempnight.getText();
				String humid = inputhumidity.getText();
				String maxday = maxtempday.getText();
				String maxnight = maxtempnight.getText();
				
				try {
					int mindaytemps = Integer.parseInt(mindaytemp);
					int minnighttemps = Integer.parseInt(minnighttemp);
					int humidity = Integer.parseInt(humid);
					int maxdaytemps = Integer.parseInt(maxday);
					int maxnightstemps = Integer.parseInt(maxnight);
					write("Preferences", mindaytemp+"\n"+minnighttemp+"\n"+humid+"\n"+maxday+"\n"+maxnight);
				}catch(NumberFormatException e1){
					JOptionPane.showMessageDialog(frame, "Incorrect input, Please Try again");
				}
			}
			
		});
		 Confirm.setFont(new Font("Tahoma", Font.PLAIN, 20));
		 Confirm.setBounds(214, 373, 153, 39);
		frame.getContentPane().add(Confirm);
		
		lblNewLabel_3 = new JLabel("MinimmumTemperature(Day)");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(85, 94, 197, 33);
		frame.getContentPane().add(lblNewLabel_3);
		
		lblNewLabel_4 = new JLabel("Minimum Temperature(Night)");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(85, 132, 197, 33);
		frame.getContentPane().add(lblNewLabel_4);
		
		lblNewLabel_5 = new JLabel("Maximum Temperature(Day)");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(85, 172, 197, 33);
		frame.getContentPane().add(lblNewLabel_5);
		
		lblNewLabel_6 = new JLabel("Maximum Temperature(Night)");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_6.setBounds(85, 212, 197, 33);
		frame.getContentPane().add(lblNewLabel_6);
		
		JLabel lblNewLabel_6_2 = new JLabel("Humidity");
		lblNewLabel_6_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_6_2.setBounds(85, 292, 197, 33);
		frame.getContentPane().add(lblNewLabel_6_2);
	}
	
	public void favouritesPanel(String name) {
		favouritesWriter tempWriter = new favouritesWriter();
		tempWriter.read();
		String first = "";
		String second = "";
		String third = "";
		String fourth = "";
		if(favouritesWriter.places.size()>=1) {
			first = favouritesWriter.places.get(0);
		}
		if(favouritesWriter.places.size()>=2) {
			second = favouritesWriter.places.get(1);
		}
		if(favouritesWriter.places.size()>=3) {
			third = favouritesWriter.places.get(2);
		}
		if(favouritesWriter.places.size()>=4) {
			fourth = favouritesWriter.places.get(3);
		}
		
		//
		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PrintWriter writer = new PrintWriter("Favorites");
					writer.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				remove();
				starterScreen(name); 
				favouritesWriter.places.clear();;
				FileWriter fw;
				try {
					fw = new FileWriter(GUI.DOCUMENT_FOLDER+"/Favorites");
					BufferedWriter writer = new BufferedWriter(fw);
					writer.flush();
					writer.close();
				} catch (IOException e1) {
			
					e1.printStackTrace();
				}
				
			}
		});
		
		 clear.setFont(new Font("Tahoma", Font.PLAIN, 20));
		 clear.setBounds(228, 369, 153, 39);
		frame.getContentPane().add(clear);
		
		JLabel lblNewLabel_1 = new JLabel(first);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(24, 111, 538, 23);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel(second);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setBounds(24, 171, 538, 23);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel(third);
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_2.setBounds(24, 234, 538, 23);
		frame.getContentPane().add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_3 = new JLabel(fourth);
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3.setBounds(24, 292, 538, 23);
		frame.getContentPane().add(lblNewLabel_1_3);
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
				starterScreen(name);
			}
		});
		back.setFont(new Font("Tahoma", Font.PLAIN, 20));
		back.setBounds(10, 401, 110, 51);
		frame.getContentPane().add(back);
		

		JLabel lblNewLabel = new JLabel("Previous Places");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(228, 38, 153, 33);
		frame.getContentPane().add(lblNewLabel);
		
		
	}
	
	

	//TextFile Write - > Password, Preferences
	public void write(String filename, String pass)
	{
		BufferedWriter writer = null;
		try 
		{
			File file = new File(GUI.DOCUMENT_FOLDER+"/"+filename);
			if (!file.exists()) 
			{
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			writer = new BufferedWriter(fw);
			writer.write(pass);
			writer.close();
		} 
		catch (IOException e) 	
		{
			e.printStackTrace();
		} 
		
	}
	//TextFile Parse and Read
	public String read(String name) throws Exception {
		File file = new File(GUI.DOCUMENT_FOLDER+"/"+name);
		  BufferedReader br = new BufferedReader(new FileReader(file));
		  
		  String st;
		  st = br.readLine();
		    return st;
	}
	
	public void remove() {
		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();
	}
}

