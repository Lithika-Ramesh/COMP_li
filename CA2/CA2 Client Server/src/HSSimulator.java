import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

public class HSSimulator {
	
	
	public void HRAlgorithm(int num, String order, Server s, OutputStream out) throws IOException {
		
		LinkedList<Processor> processors = Processor.createprocessorlist(num, order, s, out); 
		
		//boolean of to check if they found the leader or not
		boolean findleader = false;
		int leaderID = 0;
		int round = 0;
		int numofmess = 0;
		int phase = 0;

		while(findleader == false){
			
			for(int i = 0; i < processors.size(); i++) {
				processors.get(i).HSprocess();
				if(processors.get(i).getstatus() == status.LEADER) {
					findleader = true;
					leaderID = processors.get(i).getId();
					phase = processors.get(i).getPhase();
					round = Message.getround();
					numofmess = Message.getnumofmessagesent();
					System.out.println("The leader has been elected after " + round + " rounds.");
					System.out.println("The id is " + leaderID);
					break;
				}
			}
			
			if(findleader != true) {
				for(int i = 0; i < processors.size(); i++) {
					processors.get(i).sendBidirMessage(s, out);
				}
			}
						
			Message.increaseround();
		}
						
		if(findleader != false) {
			System.out.println("The leader is " + leaderID + " elected in phase " + phase );
			s.sendtoServer("The leader is " + leaderID + " elected in phase " + phase , out);
			System.out.println("The number of messages sent is " + numofmess);
			s.sendtoServer("The number of messages sent is " + numofmess, out);
		} else {
			System.out.println("No leader have been elected. System failure sorry :((");
			s.sendtoServer("No leader have been elected. System failure sorry :((", out);
		}
		
		
	}
	
}

