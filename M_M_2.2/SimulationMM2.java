/*
 * Ziyan Jiang -----M/M/2 simulation
 */

/*
 * stream 1 : 1/lambda = 10
 * stream 2 : 1/lambda = 20
 * total = 50
 * ave_service = 1/ 12 
 **
 * tpye= 1: arrival events
 * tpye= 2: departure events
 * Server : -1 means no server (arrival event)
 *           1 means enter Server1
 *           2 means enter Server2
 * Server State: Busy=1
 *               Idel=0
 */

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random; 

public class SimulationMM2 {
	  public final static int BUSY = 1;
	  public final static int IDEL =0;
	 
	 LinkedList<Event> delayQueue; 
	 LinkedList<Event> futureEvent;
	 
	 double clock;
	 double totalNumber;
	 int numArrival;
	 double aveIntervarrival;
	 
	 double nextDepartureT1;
	 double nextDepartureT2;
	 int server1State;
	 int server2State;
	 
	 double numQueue;
	 double nextArrival1; 
	 double nextArrival2;
	 double totalWaitT1,totalServerT1;
	 double totalWaitT2,totalServerT2;
	 int serviced1;
	 int serviced2;
	 int arrivaled;
	 double sumdelayTime;
	 int r1=0,r2=0;
	 double sumStream1;
	 double sumStream2;
	 
	 double sumIdleTime;
	 double idleTime1;
	 double sumIdleTime1;
	 double idleTime2;
	 double sumIdleTime2;

	 
	 public void init () {
		 delayQueue = new LinkedList<Event>();
		 futureEvent = new LinkedList<Event>();
	
		 clock=0.0;
		 sumStream1=sumStream2=0.0;
		 serviced1=serviced2=arrivaled=0;
		 sumdelayTime=0.0;
		 totalWaitT1=totalServerT1=0.0;
		 totalWaitT2=totalServerT2=0.0;
		 server1State=IDEL;
		 server2State=IDEL;
		 
		 nextArrival1 = clock+ exponential(5);
		 futureEvent.add(new Event(nextArrival1,"A",1,-1,1));
		 nextArrival2 = clock+ exponential(15);
		 futureEvent.add(new Event(nextArrival2,"A",2,-1,2));
		 numArrival=2;
		 
		 totalNumber=10000;
	 }
	 
	 
	 public void run() {
		 init (); 
		 
		 while(arrivaled < totalNumber) {
			 System.out.println(" ");
			 System.out.println("Current time: "+clock);
			 Collections.sort(futureEvent);
			 System.out.println("FutureEvent size: "+futureEvent.size());
			 System.out.println("FutureEvent : ");
			 printList(futureEvent);
			 double preClock=clock;
			
			
			 Event event = (futureEvent).removeFirst();
			 clock = event.time;
			 
			 System.out.println("Next Event: "+ event);
			 System.out.println("Server state: Server1: "+server1State+ ", Server2: "+server2State);
			 System.out.println("DelayQueue size: "+delayQueue.size());
			 System.out.println("DelayEvent : ");
			 printList(delayQueue);
			 
			 if(server1State==IDEL) {
				 idleTime1 = clock-preClock;
				 sumIdleTime1 +=idleTime1;
			 }else if (server2State==IDEL) {
				 idleTime2 = clock-preClock;
				 sumIdleTime2 +=idleTime2;
			 }
			 
			 if(event.type=="A") { // if this is the arrival event, 
				 eventArrival(event);  //enter servers
				 arrivaled++;
			 }
			 
			 if(event.type=="D") { // if this is the end event, 
				 eventDeparture(event);
			 }
			 
		//	 System.out.println(delayQueue.size());
		 }
	 }
	  
	 void eventArrival(Event e) {
		 if(server1State==IDEL) { // if the server is idle
			generateDeparture1(e);  //enter server1 
		 }else if(server2State==IDEL){
			generateDeparture2(e); // enter server2
		 }else {
			 
			 delayQueue.add(e);
			 numQueue++;
			 
		 }		 
		 generateArrival(e);
	 }
	 
	  void generateArrival(Event e) {
    	  ++numArrival;
		 if(e.stream==1) {
			 nextArrival1 = clock+ exponential(5);
			 double interArrival1= nextArrival1 - e.time;
			 sumStream1 +=  interArrival1;
			 futureEvent.add(new Event(nextArrival1,"A",numArrival,-1,1));
			 r1++;
		 }else if(e.stream==2) {
			 nextArrival2 = clock+ exponential(15);
			 double interArrival2= nextArrival2 - e.time;
			 sumStream2 += interArrival2;
			 futureEvent.add(new Event(nextArrival2,"A",numArrival,-1,2));
			 r2++;
		 }
		  
		  		
	 }
	 
	 void eventDeparture(Event e) {
		 futureEvent.remove(e);
		 if(e.server==1) {
			 setServer1State(IDEL);
		 }else if(e.server==2){
			 setServer2State(IDEL);
		 }
		 
		 if(delayQueue.size()==0 && futureEvent.size()==0 ) {
			 setServer1State(IDEL);
			 setServer2State(IDEL);
		 }else if(delayQueue.size()>0 && server1State==IDEL) {
			  Event finished = delayQueue.poll();
			  double delayTime = (clock - finished.time);
//			  System.out.println("clock1: "+ clock);
//			  System.out.println("finishT1: "+ finished.time);
//			  System.out.println("delay1: "+ delayTime);
			  sumdelayTime += delayTime; 
			  numQueue--;
			  generateDeparture1(finished);	 //enter the server1

		 }else if(delayQueue.size()>0 && server2State==IDEL) {
			  Event finished = delayQueue.poll();
			  double delayTime = (clock - finished.time);
//			  System.out.println("clock2: "+ clock);
//			  System.out.println("finishT2: "+ finished.time);
//			  System.out.println("delay2: "+delayTime);
			  sumdelayTime += delayTime; 
			  numQueue--;
			  generateDeparture2(finished);	

		 }
		
		  
	//	 System.out.println(delayQueue.size());
		
	 }
	 
	 	 
	 void generateDeparture1(Event e) {
	  
		double serverTime1 = exponential(14);
		double nextDepartureT1 = clock + serverTime1;
		//System.out.println(nextDepartureT1);
		++serviced1;
		double waitTime = nextDepartureT1-e.time;
		Event Departure = new Event(nextDepartureT1,"D", e.count,1, e.stream);
		futureEvent.add(Departure);
    	setServer1State(BUSY);		
		totalWaitT1 += waitTime;
		totalServerT1 += serverTime1;


	 }
	 
	 void generateDeparture2(Event e) {
	  
			double serverTime2 = exponential(14);
			double nextDepartureT2 = clock + serverTime2;
			++serviced2;
			double waitTime = nextDepartureT2-e.time;
			Event Departure = new Event(nextDepartureT2,"D",e.count,2,e.stream);
			futureEvent.add(Departure);
			setServer2State(BUSY);
//			System.out.println( nextDepartureT2);
//			System.out.println(e.time);
//			double waitTime = nextDepartureT2-e.time;
//			System.out.println(waitTime);
			totalWaitT2 += waitTime;
			totalServerT2 +=serverTime2;
		 }
	 
	 public void setServer1State(int server1State) {
		 this.server1State = server1State;
	 }
	 public void setServer2State(int server2State) {
		 this.server2State = server2State;
	 }
	 
	 public void printReport() {

		 System.out.println("");
		 System.out.println("The number of Stream 1: "+r1);
		 System.out.println("The number of Stream 2: "+r2);
		 System.out.println("Stream1 Input speed: "+ ((double)sumStream1/r1));
		 System.out.println("Stream2 Input speed: "+ ((double)sumStream2/r2));
		 System.out.println("The number of Server 1: "+serviced1);
		 System.out.println("The number of Server 2: "+serviced2);
		 System.out.println("Total Serviced: "+(serviced1+serviced2));
		 System.out.println("Total Arrived: " +arrivaled);
			 
		 System.out.println("Total Delay time in queue: "+sumdelayTime);
		 System.out.println("Average Delay time in queue: "+(double)(sumdelayTime/arrivaled));

		 System.out.println("Total wait time of server1: "+totalWaitT1);
		 System.out.println("Total wait time of server2: "+totalWaitT2);
		 System.out.println("Total wait time of System: "+(totalWaitT1+totalWaitT2));
		 System.out.println("Average WaitTime: "+(double)((totalWaitT1+totalWaitT2)/arrivaled));
	
		 System.out.println("TotalServer1 ServerTime: "+totalServerT1);
		 System.out.println("Average Server1 ServerTime: "+(totalServerT1/serviced1));
		 System.out.println("TotalServer2 ServerTime: "+totalServerT2);
		 System.out.println("Average Server2 ServerTime: "+(totalServerT2/serviced2));
		 System.out.println("Total idle time: "+(sumIdleTime1+sumIdleTime2));
		 System.out.println("Average idle time of Server1: "+(sumIdleTime1/r1));
		 System.out.println("Average idle time of Server2: "+(sumIdleTime2/r2));

	 }
	 public static void printList(LinkedList<Event> list) {
		 if(list.size()==0) {
			 System.out.println("Empty List.");
		 }
		 for(Event i: list) {
			 System.out.println(i);
		 }
		 
	 }
	 public static double exponential(double lambda) {
			Random r = new Random();
			double u = r.nextDouble();
			double x = Math.log(1-u)/(-lambda);
			return x;
		}
}
