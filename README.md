# M-M-2
Discrete-event-simulation 
There are two streams of arrivals to the two servers; there is one line for both and customers are fcfs.  


Arrival times are exponentially distributed. customer in stream 1 arrive with an average of 5 customers/hour, stream 2 at 15 customers/hour.  Thus there are on average 20 customers/hr arrivals. Both servers can handle an average (exponentially distributed service times) of 12 or 13 or 14 customers/hour. Run your program for each of the service times for 10,000 customers to compare the avg wait time for customers; the avg idle time for servers.


Must include debugging information for the first 50 arrivals for each service time.
 Debugging information would be  current time, the queue length, busy servers or idle, both event queues: future and delayed. one output for each time change.




now that your debugging works, you should run the simulation for the three service times, collecting avg service time, avg interarrival rate, avg wait before getting service.
