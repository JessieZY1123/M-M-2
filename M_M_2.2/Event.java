/*
 * Ziyan Jiang ---M/M/2
 */
public class Event implements Comparable<Event> {
	public double time;
	public String type;
	public int count;
	public int server;
	public int stream;
	
	public Event(double time,String type,int count,int server, int stream) {
		this.time= time;
		this.type = type;
		this.count = count;
		this.server= server;
		this.stream = stream;
		
	}

	 public int compareTo (Event e){
	        if (time < e.time) {
	            return -1;
	        }
	        else if (time > e.time) {
	            return 1;
	        }
	        else {
	            return 0;
	        }
	    }
	 @Override
	 public String toString() {
		 return "[id: "+count+", time: "+time+", type: "+type+", server: "+ server+", stream: "+stream+"]";
	 }
	 

}
