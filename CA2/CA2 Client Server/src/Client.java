import java.io.*;
import java.net.*;


public class Client {
	
	public void readfromServer(InputStream in) throws IOException {
		String input = "";
		char c;
		while((c = (char) in.read()) != '\n') {
			input = input + c + "";
		}		
		//String ret = (String) in.read();
		System.out.println(input);
	}
	
	public String retfromServer(InputStream in) throws IOException {
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
	
	public int numofprocessors(OutputStream out, InputStream in, BufferedReader read) throws IOException {
		//receive the message from the server about the number of processors
		readfromServer(in);
		String input = read.readLine();
		int num = Integer.parseInt(input);
		if(num < 0 || num > 1000) {
			System.out.println("try again");
			numofprocessors(out, in, read);
		}
		sendtoServer(Integer.toString(num), out);
		System.out.println("The number of processor is " + num);
		return num;
	}
	
	public void LCRprocess(int num, InputStream din) throws IOException {
		
		boolean findleader = false;
		
		while(findleader == false) {
			for(int i = 0; i < num; i++) {
				readfromServer(din);
				if(retfromServer(din).equals("leader elected")) {
					findleader = true;
					readfromServer(din);
					readfromServer(din);
					break;
				}
			}
			
			if(findleader != true) {
				for(int i = 0; i < num; i++) {			 
					 readfromServer(din);
				}
			}
		}
	}
	
	public void HSalgo(int num, InputStream din) throws IOException {
		boolean findleader = false;
		
		while(findleader == false) {
			for(int i = 0; i < num; i++) {
				readfromServer(din);
				if(retfromServer(din).equals("leader elected")) {
					findleader = true;
					readfromServer(din);
					readfromServer(din);
					break;
				}
			}
			
			if(findleader != true) {
				for(int i = 0; i < num; i++) {			 
					 readfromServer(din);
					 readfromServer(din);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			//Initialisation of all the readers and writers
			Client client = new Client();
			Socket s = new Socket("localhost", 6666); // connection to the server
			OutputStream dout = new DataOutputStream(s.getOutputStream());
			InputStream din = new DataInputStream(s.getInputStream());
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
					
			client.sendtoServer("Successfull connection", dout);
			
			client.readfromServer(din); //welcome text
			
			//get the different inputs 
			//type of algorithm
			client.readfromServer(din);
			String algo = input.readLine();
			boolean yes = false;
			while(yes == false) {
				if(algo.equals("A") || algo.equals("a") || algo.equals("B") || algo.equals("b")) {
					client.sendtoServer(algo, dout);
					yes = true;
				} else {
					System.out.println("Try again");
					algo = input.readLine();
				}			
			}
			
			//order of the list
			client.readfromServer(din);
			String order = input.readLine();
			yes = false;
			while(yes == false) {
				if(order.equals("A") || order.equals("a") || order.equals("B") || order.equals("b")
					|| order.equals("C") || order.equals("c")) {
					client.sendtoServer(order, dout);
					yes = true;
				} else {
					System.out.println("Try again");
					order = input.readLine();
				}				
			}
						
			//get the number of processors 
			int num = client.numofprocessors(dout, din, input);
			System.out.println(num);
			
			//run the process
			//the process is the same no matter the algorithm 
			
			//creation of the list
			for(int i = 0; i < num; i ++) {
				client.readfromServer(din);	
				client.readfromServer(din);
				client.readfromServer(din);	
				client.readfromServer(din);	
			}
			client.readfromServer(din); //should print "Processor list created"
			
			if(algo.equals("a") || algo.equals("A")) {
				client.LCRprocess(num, din);
			} else if (algo.equals("b") || algo.equals("B")) {
				client.HSalgo(num, din);
			} else {
				s.close();
			}
			
			
			//String ret = (String) in.read();
			//System.out.println(input);
			
			
			dout.flush();
			dout.close();
			s.close();

		}catch(Exception e) {
			System.out.println(e);
		}
	}
}


