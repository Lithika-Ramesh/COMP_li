
enum Direction {IN, OUT};

public class Message {
	
	//round and munofmessagesent are static values because they are 
	private static int round = 0; //the initial round is round 0
	private static int numofmessagesent = 0; //initially, there are no messages sent 
	private int sendID; //id of the message
	private Direction direction; //direction of the message
	private int hopcount = 1;
	
	//constructors
	public Message() {		
	}
	
	public Message(int id, Direction dir, int hop) {
		sendID = id;
		direction = dir;
		hopcount = hop;
	}
	
	//getters and setters
	public static void increaseround() {
		round++;
	}
	
	public int getsendId() {
		return sendID;
	}
	
	public static int getround() {
		return round;
	}
	
	public static void increasenumofmessagesent() {
		numofmessagesent++;
	}
	
	public static int getnumofmessagesent() {
		return numofmessagesent;
	}
	
	public void setId(int id) {
		sendID = id;
	}
	
	public void setDirection(Direction dir) {
		direction = dir;
	}
	
	public Direction getDir() {
		return direction;
	}
	
	public int gethopcount() {
		return hopcount;
	}
	
	public void sethopcount(int hopc) {
		hopcount = hopc;
	}
	
}

