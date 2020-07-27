/*--------------------------------------------------------

1. Mohammad Momeni / 01/26/2020


2. Java Build 1.8.0_241

3. Compilation Comand:

> javac JokeClient.java


4. instructions to run this program:

In separate Cmd windows:

 java JokeClient

5. Files:

  JokeClient.java

5. Notes:

I always used Ctrl+C to end each command window.

----------------------------------------------------------*/
 
import java.io.*;
import java.net.*;
public class JokeClient{
   public static String[][] jokes;
    public static String[][] proverbs;



	public static void main (String args[]) {
		String sName;
		int[] myIntArray = new int[]{0, 0};
		jokes = new String[][] {{"JA, I tried to sure the airport for misplacing my luggage. I lost my case.", "true"}, 
			{"JB, What is Forest Gump's password? 1Forest1", "true"},
		{"JC, What's a foot long and slippery? A slipper!", "true"},
		{"JD, Why doesn't the sun go to college? Because it has a million degrees!", "true"}};


		proverbs = new String[][] {{"PA, Lightning never strikes twice in the same place.", "true"},
		{"PB, Life begins at forty.", "true"},
		{"PC, Money doesn’t grow on trees.", "true"},
		{"PD, Never test the depth of water with both feet.", "true"}};
		//Server name will be localhost by default if user does not put anything
		if (args.length < 1)
			sName = "localhost";
		//user always can write their own servername though
		else
			sName = args[0];
		System.out.println("Mohammad Momeni's JokeClient, 1.8.\n");
		System.out.println("Using server: " + sName + ", Port: 4545");
		//Reading text
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			String name;
			System.out.println("Enter your Name please: ");
			String id = in.readLine ();	//getting the id
			do {
			System.out.print("Press Enter to get data or type 'quit' to quit: ");
			System.out.flush (); 
			name = in.readLine ();
			// keep going if user does not write quit
			if (name.indexOf("quit") < 0) 
				myIntArray = printRemote(name, sName, id, myIntArray);
			}
			while (name.indexOf("quit") < 0);
			System.out.println ("User cancelled it.");
		} 
		catch (IOException x) {
			x.printStackTrace ();
			}
	}

	static String toText (byte ip[]) {
		StringBuffer result = new StringBuffer ();
		for (int i = 0; i < ip.length; ++ i) {
			if (i > 0) result.append (".");
				result.append (0xff & ip[i]);
		}
		return result.toString ();
	}

	static int[] printRemote (String name, String sName, String id, int[] myIntArray){// using this method to make my code cleaner
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;
		String dataFromServer;
		
		try{
			//server port 2495
			sock = new Socket(sName, 4545);
			fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			toServer = new PrintStream(sock.getOutputStream());
			toServer.println(name); 
			//toServer.flush();
			dataFromServer = fromServer.readLine();
			System.out.print(dataFromServer+"\n");
			
			if(dataFromServer.equals("JokeMode")) {
				for(int i = 0; i<=3 ; i++) {
					if(jokes[i][1].equals("true")) {
						System.out.print(id + " hear this joke: " + jokes[i][0] + "\n");
						jokes[i][1] = "false";
						myIntArray[0]++;
						break;
					}
					else if(myIntArray[0] == 4) {
						System.out.print("Joke Cycle finished \n");
						toServer.println("ProverbMode");
						for(int j = 0; j<=3 ; j++) {
							jokes[j][1] = "true";
							myIntArray[0]= 0;
							
						}
						break;
					}
					
				}
			}
			else {
				for(int i = 0; i<=3 ; i++) {
					if(proverbs[i][1].equals("true")) {
						System.out.print(id + " hear this proverb: " + proverbs[i][0] + "\n");
						proverbs[i][1] = "false";
						myIntArray[1]++;
						break;
					}
					else if(myIntArray[1] == 4) {
						System.out.print("Proverb Cycle finished \n");
						toServer.println("JokeMode");
						for(int j = 0; j<=3 ; j++) {
							proverbs[j][1] = "true";
							myIntArray[1]= 0;
						
						}
						break;
					}
				}
			}
			// once a socket has been closed it is not available for furthur networking use
			sock.close(); // we close here
		} catch (IOException x) {
		System.out.println ("Socket error.");
		x.printStackTrace ();
		}
		return myIntArray;
	}
}