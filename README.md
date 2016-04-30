##Code Execution:
To run the  project  from command line.
Unzip the folder to desired location
Cd to the project folder
mvn clean install to build the project
mvn test  to test the testcases.
##Synopsis: 
Ticket service System is a ticket reservation system for a venue. The venue has 4 level and available seats in these levels will be shown then user will be prompted to enter two level one being the max level and other a min level, number of seats, emailed. Email Id will serve as a unique Id for the user. Once these details are entered program checks for the availability for the seats if found holds them for a certain time and waits for the user confirmation. Once user confirms the seats those seats will be reserved for the user and a confirmation number will be returned.  User will be asked if they want to start another booking. If yes, whole process will be started again. If no, program will exit
If a level other than 4 levels are entered , user will be prompted to reenter the details again. Similarly if requested seats are either 0 or more than the capacity of the venue they will be asked to reenter again.
##Assumptions:
1.	SeatsHold time is 10sec
2.	Max level should be less than Min level
Orchestra -> 1
Main -> 2
Balcony1 -> 3
Balcony2 -> 4
3.	Seats will be booked in only one level. Combination of levels is not possible.
4.	Seats will be scanned from max to min
5.	Reservation states
	0 – Available
1-	Held
2-	Reserved
##SeatHold Logic:
Algorithm: Search for  requested number of seats in consecutive method in Max level if found return seatHold object if not found, search for scattered seats in the same level if found return object if not search for the level(Max-1) for consecutive seats if not found search for scattered seats. Repeat the process until  level = min level. 
