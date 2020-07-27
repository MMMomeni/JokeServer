/*--------------------------------------------------------

1. Mohammad Momeni / 01/26/2020


2. Java Build 1.8.0_241

3. Compilation Comand:

> javac JokeServer.java


4. instructions to run this program:

In separate Cmd windows:

 java JokeServer

5. Files:

  JokeServer.java


5. Notes:

I always used Ctrl+C to end each command window.

----------------------------------------------------------*/
 
import java.io.*;
import java.net.*;
import java.util.Random;


class Mode { //using getters and setters to get and set the switiching data from 
	//the ClientAdmin
    private int mode;

    private static Mode uniqueInstance;
    private Mode(){
        this.mode = 0;
    }

    public static Mode getInstance(){
        if(uniqueInstance == null)
            uniqueInstance = new Mode();

        return uniqueInstance;
    }

    public void setMode(int command){
        this.mode = command; 
    }

    public int getMode() {
        int result = this.mode;
        return result;
    }
}

public class JokeServer {
	

	public static void main(String a[]) throws IOException {
	int q_len = 6;
	int port = 4545;
	
    AdminLooper AL = new AdminLooper(); // making an instance of AdminLooper
    Thread t = new Thread(AL);
    t.start();  // 
	
	Socket sock; // a socket variable
	ServerSocket servsock = new ServerSocket (port, q_len);
	
	System.out.println("Mohammad Momeni's JokeServer 1.8, at port 4545.\n");
	while (true) {
		sock = servsock.accept();
		new Worker(sock).start();
	}

}
}
	class AdminLooper implements Runnable {
		  public boolean adminControlSwitch = true; // to switch admin Control on and off

		  public void run(){ // RUNning the Admin listen loop
		    System.out.println("In the admin looper thread");
		    
		    int q_len = 6; // requests
		    int port = 5050;  // a port for revieving calls from adminClient
		    Socket sock;

		    try{
		      ServerSocket servsock = new ServerSocket(port, q_len);
		      while (adminControlSwitch) {
			
			sock = servsock.accept();
			new AdminWorker (sock).start(); 
		      }
		    }
		    catch (IOException ioe) {System.out.println(ioe);}
		  }
		}
	
	class AdminWorker extends Thread { //each Admin client runs a thread here
		Socket sock;
		AdminWorker (Socket s){
			sock = s;
		}
		
	 
		public void run() {
			PrintStream out = null;
			BufferedReader in = null;
	        Mode newMode = Mode.getInstance(); //and instance of jokeserver to implement the mode.
			try {
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				out = new PrintStream(sock.getOutputStream());
				
				try {
					String command;  //saving the user input in command.
					command = in.readLine();
					if (command.equals("s")) {
					    
						out.println(newMode.getMode());
						if (newMode.getMode() == 0) {
							newMode.setMode(1);
						}
						else if (newMode.getMode() == 1) {
							newMode.setMode(0);
						}
					}
				}
				catch (IOException x) {
					System.out.println("Server read error!");
					x.printStackTrace();
				}
				sock.close();
			}
			catch (IOException ioe) {
				System.out.println(ioe);
				}
		}
		
		

	}
	
	 class Worker extends Thread { // each client runs a thread here
		Socket sock;
		Worker (Socket s){
			sock = s;
		}
	    //private static Mode newMode; //and instance of MOde class to implement the mode.

	    
		public void run() {
			PrintStream out = null;
			BufferedReader in = null;
        	Mode newMode = Mode.getInstance();
            
			try {
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				out = new PrintStream(sock.getOutputStream());

				
				try {
					String name;
					name = in.readLine();
					int mode = newMode.getMode();
					clientHandler (name, out, in, mode, newMode); 
				}
				catch (IOException x) {
					System.out.println("Server read error!");
					x.printStackTrace();
				}
				sock.close();
			}
			catch (IOException ioe) {
				System.out.println("Server read error!...............");
				System.out.println(ioe);
				}
			}

		
		 void clientHandler (String name, PrintStream out, BufferedReader in, int mode, Mode newMode) { // handles the users requests.
				

			 if (name.equals("")) { //checking the user input if it matched, running the codes below.

				    //int x = newMode.getMode(); //x has the value of mode now
				   if (mode == 0) {
					   System.out.println("JokeMode");
					   out.println("JokeMode");
				   }
				   else {// if the mode was something else, running this code instead.
					   System.out.println("ProverbMode");
					   out.println("ProverbMode");
				   }
				 
		 }
			 try {
					String command;
					command = in.readLine();
					if (command.equals("ProverbMode")) {
						newMode.setMode(1);
					}
					else if (command.equals("JokeMode")) {
						newMode.setMode(0);
					}

				}
				catch (IOException x) {
					System.out.println("Server read error!");
					x.printStackTrace();
				}
			 
			   }
	  }
	 
	 
	 
	
