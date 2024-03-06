import java.util.LinkedList;
import java.io.*;


public class LCRAlgorithm extends Server{

	public void LCRSimulator(int num, String order, Server s, OutputStream out) throws IOException {
		//create a linked list of processors	
		LinkedList<Processor> processors = Processor.createprocessorlist(num, order, s, out); //change 3 by another value like n 

		//boolean of to check if they found the leader or not
		boolean findleader = false;
		int leaderID = 0;
		int round = 0;
		int numofmess = 0;
				
		while(findleader == false) {
				
			for(int i = 0; i < processors.size(); i++) {
				//System.out.println("currently looking for the leader");
				processors.get(i).LCRprocess(s, out);
				if(processors.get(i).getstatus() == status.LEADER) {
					findleader = true;
					s.sendtoServer("The leader has been elected", out);
					leaderID = processors.get(i).getId();
					round = Message.getround();
					numofmess = Message.getnumofmessagesent();
					s.sendtoServer("The leader has been elected after " + round + " rounds.", out);
					s.sendtoServer("The id is " + leaderID, out);
					break;
				}
			}
			
			if(findleader != true) {
				for(int i = 0; i < processors.size(); i++) {			 
					 processors.get(i).sendMessageClockwise(s, out);
				}
			}
			
			Message.increaseround();
			
		} 
		
						
		if(findleader != false) {
			s.sendtoServer("The leader is " + leaderID, out);
			System.out.println("The leader is " + leaderID);
			s.sendtoServer("The number of messages sent is " + numofmess, out);
			System.out.println("The number of messages sent is " + numofmess);
		} else {
			s.sendtoServer("No leader have been elected. System failure sorry :((", out);
			System.out.println("No leader have been elected. System failure sorry :((");
		}
		
		
	}
	
}
