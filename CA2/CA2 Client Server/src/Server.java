import java.io.*;
import java.net.*;


public class Server {
	public String getInput(InputStream in) throws IOException {
		String input = "";
		char c;
		while((c = (char) in.read()) != '\n') {
			input = input + c + "";
		}
		return input;
	}
	
	public void sendtoServer(String tosend, OutputStream out) throws IOException {
		if(tosend.charAt(tosend.length() - 1) != '\n') {
			tosend += '\n';
		}		
		for(int i = 0; i < tosend.length(); i++) {
			out.write(tosend.charAt(i));
		}
	}
	
	public int numofprocessors(OutputStream out, InputStream in) throws IOException {
		sendtoServer("Insert the number of processors you want. Please choose a number between 0 - 1000", out);
		String received = getInput(in);
		int num = Integer.parseInt(received);
		System.out.println(num);
		return num;
	}
	
	public static void main(String[] args) {
		try {
			//Initialisation of everything
			Server server = new Server();
			ServerSocket ss = new ServerSocket(6666); //creation of the server socket
			Socket s = ss.accept(); //establishing connection
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			//BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
			
			
			//the process
			String connected = server.getInput(dis);
			System.out.println(connected);
			server.sendtoServer("Welcome to my COMP212 Assignment", dout); 
			server.sendtoServer("Please choose which algorithm you want: A for LCR algorithm and B for HS algorithm", dout);
			String in = server.getInput(dis);
			System.out.println("The client chose algorithm " + in);
			server.sendtoServer("A: Random || B: Ascending || C: Descending", dout);
			String order = server.getInput(dis);
			
			//Test code
			if(in.equals("a") || in.equals("A")) {
				System.out.println("The client chooses an LCR algorithm");
			} else if (in.equals("b") || in.equals("B")) {
				System.out.println("The client chooses an HS algorithm");
			} 
			//Test code
			if(order.equals("a") || order.equals("A")) {
				System.out.println("The order of the list is random");
			} else if (order.equals("b") || order.equals("B")) {
				System.out.println("The order of the list is ascending");
			} else if (order.equals("c") || order.equals("C")) {
				System.out.println("The order of the list is descending");
			} 
			
			
			if(in.equals("A") || in.equals("a")) {
				int num = server.numofprocessors(dout, dis);
				//System.out.println(num);
				LCRAlgorithm algorithm = new LCRAlgorithm(); 
				algorithm.LCRSimulator(num, order, server, dout); 
			} else if(in.equals("B") || in.equals("b")) { 
				int num = server.numofprocessors(dout, dis);
				HSSimulator algorithm = new HSSimulator(); 
				algorithm.HRAlgorithm(num, order, server, dout); 
			} 

					
			ss.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

