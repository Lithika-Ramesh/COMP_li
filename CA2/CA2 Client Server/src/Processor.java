import java.util.LinkedList;
import java.io.*;
import java.util.Random;

enum status {LEADER, UNKNOWN};

public class Processor extends Server{
	private int myID;
	private int sendId;
	//private int inID;
	private status mystatus;
	private Processor previousProcessor;
	private Processor nextProcessor;
	private Message previousMessage;
	private Message nextMessage;
	private int phase = 0; //HS part 
	
	private Message sendClock; //the message that will be sent to the next processor 
	private Message sendCounterclock; //the message that will be sent to the previous processor 
	
	//constructors
	public Processor() {
		//if the id is unknown then do nothing
	}
	
	public Processor(int id) {

		myID = id;
		sendId = myID;
		mystatus = status.UNKNOWN; //the default status of any processor is unknown
		previousProcessor = null; //we cannot tell who is the previous one yet
		nextProcessor = null; // same for the next one
		//since it is a constructor, the prev and next message are still null
		previousMessage = new Message();
		nextMessage = new Message();
		//the initial message to send is always the one with the current id of the processor
		sendClock = new Message(sendId, Direction.OUT, 1);
		sendCounterclock = new Message(sendId, Direction.OUT, 1);
		
	}
	
	//getters and setters
	public void setId(int id) {
		myID = id;
	}
	public int getId() {
		return myID;
	}
	public void setsendId(int id) {
		sendId = id;
	}
	public int getSendId() {
		return sendId;
	}
	public void setstatus(status st) {
		mystatus = st;
	}
	public status getstatus() {
		return mystatus;
	}

	
	 public void setprevProcessor(LinkedList<Processor> processors) { 
		 int index = 0; 
		 for (int i = 0; i < processors.size(); i++) { 
			 if(processors.get(i) == this) { 
				 index = i; break; 
				 } 
			 } 
		 if(index > 0) { 
			 previousProcessor = processors.get(index - 1); 
			 } else { 
			 previousProcessor = processors.get(processors.size() - 1); 
		} 
	}
	 
	public Processor getprevProc() {
		return previousProcessor;
	}

	
	 public void setnextProcessor(LinkedList<Processor> processors) { 
		 int index = 0;
		 for(int i = 0; i < processors.size(); i++) { 
			 if(processors.get(i) == this){ 
				 index = i; break; 
			}
		} 
		 if(index < processors.size() - 1) { 
			 nextProcessor = processors.get(index + 1); 
		} else { 
			nextProcessor = processors.get(0); 
			} 
		 }
	 
	public Processor getnxtProc() {
		return nextProcessor;
	}	

	 public void setprevMessage(Message prevmess) { previousMessage = prevmess; }
	 public Message getprevMessage() { return previousMessage; } 
	 public void setnxtMessage(Message nxtmess) { nextMessage = nxtmess; } 
	 public Message getnxtMessage() { return nextMessage; }
	
	public void setPreMessage(int id) {previousMessage.setId(id);}
	public void setNextMessage(int id) {nextMessage.setId(id);}
	public Message getPrevMessage() {return previousMessage;}
	public Message getNextMessage() {return nextMessage;}
	public void incPhase() {phase++;}
	public int getPhase() {return phase;}
	
	//This is the method that will create the ring = successful 
	public static LinkedList<Processor> createprocessorlist(int numofProcessor, String order, 
			Server s, OutputStream out) throws IOException{
		
		 LinkedList<Processor> processorList = new LinkedList<Processor>();
		 if(order.equals("A") || order.equals("a")) {//random list
			 Random rand = new Random(); //generate a random integer
		     
		     //create the list using
		     for(int i = 0; i < numofProcessor; i++) {
		    	 boolean isunique = true;
	             int n;
	             //find a unique id
	             do {
	                 isunique = true;
	                 n = rand.nextInt(3 * numofProcessor); //we chose alpha = 3
	                 for (int counter = 0; counter < processorList.size(); counter++) {
	                     if (processorList.get(counter).getId() == n)
	                         isunique = false;
	                 }
	             } while (isunique == false);
	             
	             //add that processor to the list
	             Processor processor = new Processor(n);
	             processorList.add(processor);
		     }
		 } else if(order.equals("B") || order.equals("b")){//Ascending or from small to big
			 for(int i = 0; i < numofProcessor; i++) {
				 Processor processor = new Processor(i+1);
				 processorList.add(processor);
			 }
		 } else if(order.equals("C") || order.equals("c")) {//descending
			 for(int i = numofProcessor; i > 0; i--) {
				 Processor processor = new Processor(i);
				 processorList.add(processor);
			 }
		 } 
	     //System.out.println(numofProcessor);
		 //System.out.println(processorList.size());
	     
	     //define the next and previous processor for each processor
	     for(int i = 0; i < processorList.size(); i++) {    	 
	    	 processorList.get(i).setnextProcessor(processorList);	    	 
	    	 processorList.get(i).setprevProcessor(processorList);
	    	 s.sendtoServer("Now: " + processorList.get(i).getId(), out);
	    	 s.sendtoServer("Previous: " + processorList.get(i).previousProcessor.getId(), out);
	    	 s.sendtoServer("Next: " + processorList.get(i).nextProcessor.getId(), out);
	    	 s.sendtoServer("---------------------------------------", out);
	     }
	     System.out.println("Processor list created");	
	     s.sendtoServer("Processor list created", out);
	     return processorList;
	}
	
	//send message A to the next processor 
	public void sendMessageClockwise(Server s, OutputStream out) throws IOException {		
			if(previousProcessor.sendClock != null) {
				previousMessage = previousProcessor.sendClock;
				previousProcessor.sendClock = null;
				Message.increasenumofmessagesent();
			}
			if(previousMessage != null) {
				s.sendtoServer("My ID: " + myID + "| ID received: " + previousMessage.getsendId() + 
						"| Status: " + mystatus, out);
			} else {
				s.sendtoServer("My ID: " + myID + "| ID received: null" + 
						"| Status: " + mystatus, out);
			}
		}

	

	 public void LCRprocess(Server s, OutputStream out) throws IOException { 
		 if(Message.getround() > 1) { 
			 if(previousMessage != null) {
				 if(previousMessage.getsendId() > myID) {
					 sendClock = previousMessage;
					 previousMessage = null;
				 } else if (previousMessage.getsendId() == myID) {
					 setstatus(status.LEADER);
					 previousMessage = null;
				 } else {
					 previousMessage = null;
				 }
			 }
			 if(sendClock != null) {
				 s.sendtoServer("My ID: " + myID + "| ID to Send: " + sendClock.getsendId() + 
							"| Status: " + mystatus, out);
			 } else {
				 s.sendtoServer("My ID: " + myID + "| ID to Send: null" + 
							"| Status: " + mystatus, out);
			 }
		 } else {
			 sendMessageClockwise(s, out);
		 }
	 }

	
	 
	//HS code
	
	public void sendBidirMessage(Server s, OutputStream out) throws IOException {
		if(previousProcessor.sendClock != null) {
			previousMessage = previousProcessor.sendClock;
			previousProcessor.sendClock = null;
			Message.increasenumofmessagesent();
		}
		if(nextProcessor.sendCounterclock != null) {
			nextMessage = nextProcessor.sendCounterclock;
			nextProcessor.sendCounterclock = null;
			Message.increasenumofmessagesent();
		}
		if(previousMessage != null) {
			s.sendtoServer("Previous id: " + previousMessage.getsendId() + 
					"| previous direction: " + previousMessage.getDir() + 
					"| hopcount: " + previousMessage.gethopcount(), out);
		} else {
			s.sendtoServer("Previous id: null "  + 
					"| previous direction: null "  + 
					"| hopcount: null " , out);
		}
		if(nextMessage != null) {
			s.sendtoServer("Next id: " + nextMessage.getsendId() + 
					"| Next direction: " + nextMessage.getDir() + 
					"| hopcount: " + nextMessage.gethopcount(), out);
		} else {
			s.sendtoServer("Next id: null "  + 
					"| Next direction: null "  + 
					"| hopcount: null ", out );
		}
	}
	
	public void HSprocess() { //this just follows the pseudocode given in the assignment sheet 
		//upon receiving <inID, out, hopCount> from counterclockwise neighbor
		if(previousMessage != null && previousMessage.getDir() == Direction.OUT) {
			if(previousMessage.getsendId() > myID && previousMessage.gethopcount() > 1) {
				sendClock = previousMessage;
				sendClock.sethopcount(previousMessage.gethopcount() - 1);
				previousMessage = null;
			} else if(previousMessage.getsendId() > myID && previousMessage.gethopcount() == 1) {
				sendCounterclock = previousMessage;
				sendCounterclock.setDirection(Direction.IN);
				previousMessage = null;
			} else if(previousMessage.getsendId() == myID ) {
				setstatus(status.LEADER);
			}
		}
		
		//upon receiving <hinID, out, hopCount> from clockwise neighbor
		if(nextMessage != null && nextMessage.getDir() == Direction.OUT) {
			if(nextMessage.getsendId() > myID && nextMessage.gethopcount() > 1 ) {
				sendCounterclock = nextMessage;
				sendCounterclock.sethopcount(nextMessage.gethopcount() - 1);
				nextMessage = null;
			} else if(nextMessage.getsendId() > myID && nextMessage.gethopcount() == 1) {
				sendClock = nextMessage;
				sendClock.setDirection(Direction.IN);
				nextMessage = null;
			} else if(nextMessage.getsendId() == myID) {
				setstatus(status.LEADER);
			}
		}
		
		//upon receiving <inID, in, 1> from counterclockwise neighbor, in which inID = myIDi
		if(previousMessage != null && previousMessage.getDir() == Direction.IN 
				&& previousMessage.gethopcount() == 1 && previousMessage.getsendId() != myID) {
			sendClock = previousMessage;
			previousMessage = null;
		}
		
		//upon receiving <inID, in, 1> from clockwise neighbour, in which inID = myIDi
		if(nextMessage != null && nextMessage.getDir() == Direction.IN
				&& nextMessage.gethopcount() == 1 && nextMessage.getsendId() != myID) {
			sendCounterclock = nextMessage;
			nextMessage = null;
		}
		
		//upon receiving <inID, in, 1> from both clockwise and counterclockwise neighbors, in
		//both of which inID = myIDi holds
		if(nextMessage != null && nextMessage.getDir() == Direction.IN
				&& nextMessage.gethopcount() == 1 && nextMessage.getsendId() == myID &&
				previousMessage != null && previousMessage.getDir() == Direction.IN 
				&& previousMessage.gethopcount() == 1 && previousMessage.getsendId() == myID) {
			incPhase();
			sendClock = nextMessage;
			sendClock.setDirection(Direction.OUT);
			sendClock.sethopcount((int) Math.pow(2, getPhase()));
						
			sendCounterclock = previousMessage;
			sendCounterclock.setDirection(Direction.OUT);
			sendCounterclock.sethopcount((int) Math.pow(2, getPhase()));
			
			nextMessage = null;
			previousMessage = null;
		}
		
		previousMessage = null;
		nextMessage = null;
	}
	
}
