/*--------------------------------------------------------

1. Mohammad Momeni / 01/26/2020


2. Java Build 1.8.0_241

3. Compilation Comand:

> javac JokeClientAdmin.java


4. instructions to run this program:

In separate Cmd windows:

 java JokeClientAdmin

5. Files:

  JokeClientAdmin.java

5. Notes:

I always used Ctrl+C to end each command window.

----------------------------------------------------------*/
 
import java.io.*;
import java.net.*;
public class JokeClientAdmin{
	public static void main (String args[]) {
		String sName;
		//Server name will be localhost by default if user does not put anything
		if (args.length < 1)
			sName = "localhost";
		//user always can write their own servername though
		else
			sName = args[0];
		System.out.println("Mohammad Momeni's JokeClientAdmin, 1.8.\n");
		System.out.println("Using server: " + sName + ", Port: 5050");
		//Reading text
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			String name;
			do {
			System.out.print("Enter 's' to change the server mode: ");
			System.out.flush (); 
			name = in.readLine ();
			// keep going if user does not write quit
			if (name.indexOf("quit") < 0) 
				printRemote(name, sName);
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

	static void printRemote (String name, String sName){// using this method to make my code cleaner
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;
		String dataFromServer;

		try{
			//server port 2495
			sock = new Socket(sName, 5050);
			fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			toServer = new PrintStream(sock.getOutputStream());
			toServer.println(name); 
			toServer.flush();
			for (int i = 1; i <=3; i++){
				dataFromServer = fromServer.readLine();
				if (dataFromServer != null)
					System.out.println(dataFromServer);
			}// once a socket has been closed it is not available for furthur networking use
			sock.close(); // we close here
		} catch (IOException x) {
		System.out.println ("Socket error.");
		x.printStackTrace ();
		}
	}
}