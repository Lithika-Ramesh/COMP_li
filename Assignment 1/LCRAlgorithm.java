import java.util.LinkedList;


public class LCRAlgorithm {

	public void LCRSimulator(int num, String order) {
		//create a linked list of processors		
		LinkedList<Processor> processors = Processor.createprocessorlist(num, order); //change 3 by another value like n 
		
		//boolean of to check if they found the leader or not
		boolean findleader = false;
		int leaderID = 0;
		int round = 0;
		int numofmess = 0;
				
		while(findleader == false) {
				
			for(int i = 0; i < processors.size(); i++) {
				//System.out.println("currently looking for the leader");
				processors.get(i).LCRprocess();
				if(processors.get(i).getstatus() == status.LEADER) {
					findleader = true;
					leaderID = processors.get(i).getId();
					round = Message.getround();
					numofmess = Message.getnumofmessagesent();
					System.out.println("The leader has been elected after " + round + " rounds.");
					System.out.println("The id is " + leaderID);
					break;
				}
			}
			
			if(findleader != true) {
				for(int i = 0; i < processors.size(); i++) {			 
					 processors.get(i).sendMessageClockwise();
				}
			}
			
			Message.increaseround();
			
		} 
		
						
		if(findleader != false) {
			System.out.println("The leader is " + leaderID);
			System.out.println("The number of messages sent is " + numofmess);
		} else {
			System.out.println("No leader have been elected. System failure sorry :((");
		}
		
		
	}
	
}
