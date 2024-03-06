import java.util.LinkedList;

public class HSSimulator {
	
	
	public void HRAlgorithm(int num, String order) {
		
		LinkedList<Processor> processors = Processor.createprocessorlist(num, order); 
		
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
					processors.get(i).sendBidirMessage();
				}
			}
						
			Message.increaseround();
		}
						
		if(findleader != false) {
			System.out.println("The leader is " + leaderID + " elected in phase " + phase );
			System.out.println("The number of messages sent is " + numofmess);
		} else {
			System.out.println("No leader have been elected. System failure sorry :((");
		}
		
		
	}
	
}

