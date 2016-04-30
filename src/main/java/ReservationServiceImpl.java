
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by A871784 on 4/25/2016.
 */
public class ReservationServiceImpl implements TicketService, OnTimerExpiredListener  {
    public static int seats = 0;
    public static int minLev = 0;
    public static int maxLev = 0;
    public static String email = null;
    private HashMap<Integer,SeatHold> mhashmap = new HashMap<Integer,SeatHold>();
    private List<SeatHold> reserveList = new ArrayList<SeatHold>();
    int[][] l1 = new int[25][50];
    int l1Left = 25*50;
    int[][] l2 = new int[20][100];
    int l2Left = 20*100;
    int[][] l3 = new int[15][100];
    int l3Left = 15*100;
    int[][] l4 = new int[15][100];
    int l4Left = 15*100;
    int uId;
    SeatHold seatHold;
    Seat seat;
    SeatHoldMonitor seatHoldMonitor;

    List tempSeats = new ArrayList();

    /**
     * resevation states
     * 0= available
     * 1=hold
     * 2=reserved
     *
     *
     */


    public void printMenu(){
        try {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("NOTE:MAX venue level should be lessthan MIN Venue Level "+"\n"+" Orchestra -> 1"+"\n" +"Main -> 2"+"\n"+
                    "Balcony1 -> 3"+"\n"+"Balcony2 -> 4");
            System.out.println("Please enter max level No:");
            String maxL = bufferRead.readLine();
            maxLev = Integer.valueOf(maxL);
            Optional<Integer> maxLevel = Optional.of(maxLev);
            System.out.println("Please enter min level No: ");
            String minL = bufferRead.readLine();
            minLev = Integer.valueOf(minL);
            Optional<Integer> minLevel = Optional.of(minLev);
            if(maxLev < minLev) {
                System.out.println("Enter no.of seats "); // map level string to int
                String s = bufferRead.readLine();
                seats = Integer.valueOf(s);
                if (seats == 0) {
                    System.out.println("please enter valid seats. Lets start over!");
                    printMenu();
                }
                System.out.println("Please enter your email address: ");
                String email = bufferRead.readLine();
                if (minLevel.isPresent() && maxLevel.isPresent() && seats != 0) {
                    SeatHold heldSeats = findAndHoldSeats(seats, minLevel, maxLevel, email);
                    if (heldSeats != null) {
                        System.out.println("Enter Y to reserve and N to abort: ");
                        String decision = bufferRead.readLine();
                        if (decision.equalsIgnoreCase("Y")) {
                            String confirmCode = reserveSeats(heldSeats.getSeatId(), heldSeats.getEmail());//reservation
                            if (confirmCode!= null) {
                                decision = bufferRead.readLine();
                                if (decision.equalsIgnoreCase("Y")) {
                                    printMenu();
                                } else {
                                    System.out.println("We appreciate your business.");
                                }

                            }
                        } else {
                            System.out.println("We appreciate your business.");
//                        printMenu();
                        }
                    } else {
                        System.out.println("Requested more seats than available.starting over");
                        printMenu();
                    }
                } else {
                    System.out.println("Please enter Valid values");
                    printMenu();
                }
            }else {
                System.out.println("Please enter Valid values");
                printMenu();
            }

        } catch (IOException e) {
            System.out.println("Please enter valid values");
            printMenu();
            e.printStackTrace();
        }
    }

    @Override
    public int numSeatsAvailable(Optional<Integer> venueLevel) {
        if (venueLevel.isPresent()) {
            if (venueLevel.get() == 1) { //check corresponding level left seats and return them.
                return l1Left;

            }
            if (venueLevel.get() == 2) {
               return  l2Left;
            }
            if (venueLevel.get() == 3 ) {
                return l3Left;
            }
            if (venueLevel.get() == 4 ) {
                return l4Left;
            }

        }

            return l1Left+l2Left+l3Left+l4Left; //is this right?

    }

    @Override
    public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {

        AtomicInteger atomicInteger = new AtomicInteger();

        if (maxLevel.get() < minLevel.get()) {

            for (int level = maxLevel.get(); level <= minLevel.get(); level++) {

                if ( numSeats!=0 && (numSeats <= getSeatsLeftAtLevel(level))) {
                    int tempCount = 0;
                    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

                    //continuous seats
                    int row = getLevelDims(level).getRow();
                    int col = getLevelDims(level).getColumn();
                    int[][] l = getLevelSize(level);
                    int seatsLeft = getSeatsLeftAtLevel(level);
                    for (int i = 0; i < row; i++) {
                        for (int j = 0; j < col; j++) {
                            if (getSeatStatus(level, i, j) == 0) {
                                tempCount++;
                                seat = new Seat(i, j);
                                tempSeats.add((tempCount - 1), seat);//add seats into the linked list
                                if (tempCount == numSeats) {//tempseat.size()
                                    uId = atomicInteger.getAndIncrement();

                                    //hold those seats
                                    seatsLeft = seatsLeft - numSeats; //update available seats
                                    seatHold = new SeatHold(maxLevel, tempSeats, customerEmail, uId);

                                    mhashmap.put(uId, seatHold);
                                    for (int k = 0; k < tempSeats.size(); k++) {
                                        seat = (Seat) tempSeats.get(k);
                                        System.out.println("seat locations" + seat.getRow() + seat.getColumn());
                                        setSeatStatus(level, seat.getRow(), seat.getColumn(), 1); //setting to hold status

                                    }
                                    tempSeats.clear();
                                    seatHoldMonitor = new SeatHoldMonitor(this, seatHold);
                                    seatHoldMonitor.start();
                                    System.out.println("found seats do you want to reserve for $ "+getReservationCost(level,numSeats)+" ?");
                                    return seatHold;

                                }
                            } else {
                                tempCount = 0; //continuos seats
                            }

                        }

                    }
                    //scattered seats
                    for (int i = 0; i < row; i++) {
                        for (int j = 0; j < col; j++) {
                            if (getSeatStatus(level, i, j) == 0) {
                                tempCount++;
                                seat = new Seat(i, j);
                                tempSeats.add((tempCount - 1), seat);//add seats into the linked list
                                if (tempCount == numSeats) {//tempseat.size()
                                    uId = atomicInteger.getAndIncrement();

                                    //hold those seats
                                    seatsLeft = seatsLeft - numSeats; //update available seats
                                    seatHold = new SeatHold(maxLevel, tempSeats, customerEmail, uId);

                                    mhashmap.put(uId, seatHold);
                                    for (int k = 0; k < tempSeats.size(); k++) {
                                        seat = (Seat) tempSeats.get(k);
                                        System.out.println("seat locations" + seat.getRow() + seat.getColumn());
                                        setSeatStatus(level, seat.getRow(), seat.getColumn(), 1); //setting to hold status
                                    }
                                    tempSeats.clear();
                                    seatHoldMonitor = new SeatHoldMonitor(this, seatHold);
                                    seatHoldMonitor.start();
                                    System.out.println("found seats do you want to reserve for $ "+getReservationCost(level,numSeats)+" ?");

                                        return seatHold;

//                                    break;
                                }
                            }
                        }

                    }


                }


            }
//            System.out.println("Requested more seats than available.Please start over");
//            printMenu();
            return seatHold;
        } else {
            System.out.println("ERROR!! MAXlevel should be less than MIN level Lets start over");
            printMenu();
        }

        return null;
    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        //search for this seatHoldId in the hashmap and if you find the seatHold Obj then confirm reservation
        //if you dint find the obj then the hold has been expired.
        if(mhashmap.containsKey(seatHoldId) && (mhashmap.get(seatHoldId) != null)){
            seatHold = mhashmap.remove(seatHoldId);
            reserveList.add(seatHold);
            if(customerEmail.equalsIgnoreCase(seatHold.getEmail())){
                Optional<Integer> lev = seatHold.getLevel();
                if(lev.isPresent() && lev.get() == 1){
                   List<Seat> seats =seatHold.getSeatList();
                    for(int i=0; i <seats.size() ; i++ ){
                        l1[seats.get(i).getRow()][seats.get(i).getRow()] = 2; //setting to reserved
                    }
                }
                if(lev.isPresent() && lev.get() == 2){
                    List<Seat> seats =seatHold.getSeatList();
                    for(int i=0; i <seats.size() ; i++ ){
                        l2[seats.get(i).getRow()][seats.get(i).getRow()] = 2; //setting to reserved
                    }
                }
                if(lev.isPresent() && lev.get() == 3){
                    List<Seat> seats =seatHold.getSeatList();
                    for(int i=0; i <seats.size() ; i++ ){
                        l2[seats.get(i).getRow()][seats.get(i).getRow()] = 2; //setting to reserved
                    }
                }
                if(lev.isPresent() && lev.get() == 4){
                    List<Seat> seats =seatHold.getSeatList();
                    for(int i=0; i <seats.size() ; i++ ){
                        l2[seats.get(i).getRow()][seats.get(i).getRow()] = 2; //setting to reserved
                    }
                }
                System.out.println("Reservation confirmed for: "+seatHold.getEmail()+"\n"+"your confirmation number is "+seatHoldId);
                System.out.println("Do you want to start another booking? Y Or N");
                return String.valueOf(seatHoldId);

            }
        }
        else{
            System.out.println("sorry. Timed out. Do you want to restart the process? Y or N");


            return null;
        }

        return String.valueOf(seatHoldId);
    }


    public String totalSeatsAvailable(){
        int[] totalSeats = new int[5];
        int seats = 0;
        for(int i =1; i< 5; i++) {
            Optional<Integer> level = Optional.of(i);
            seats = numSeatsAvailable(level);
            totalSeats[i] = seats;

        }

        return "\n"+"1 Orchestra -> "+totalSeats[1]+"\n" +"2 Main -> "+totalSeats[2]+"\n"+
                "3 Balcony1 -> "+totalSeats[3]+"\n"+"4 Balcony2 -> "+totalSeats[4];
    }


    @Override
    public void onTimerExpired(SeatHold seatHold) {

        List<Seat> list = seatHold.getSeatList();
        Optional<Integer> level = seatHold.getLevel();
        int currentLevel = -1;
        if(level.isPresent()){
            currentLevel = level.get();
        }

        for(int i =0; i < list.size();i++){
            Seat seat = list.get(i);
            switch( currentLevel){
                case 1:
                    l1[seat.getRow()][seat.getColumn()] = 0;
                    l1Left++;
                    break;
                case 2:
                    l2[seat.getRow()][seat.getColumn()] = 0;
                    l2Left++;
                    break;
                case 3:
                    l3[seat.getRow()][seat.getColumn()] = 0;
                    l3Left++;
                    break;
                case 4:
                    l4[seat.getRow()][seat.getColumn()] = 0;
                    l4Left++;
                    break;
                default:
                    //unknown level
                    break;
            }
        }

        mhashmap.remove(seatHold.getSeatId());
        System.out.println(" removed "+seatHold.getSeatId() +" from hash map");
    }

    @Override
    public boolean isSeatHoldRemoved(int holdID) {
        if(mhashmap.containsKey(holdID)){
            return false;
        }else{
            return true;
        }
    }

    public int  getSeatsLeftAtLevel(int level){

      if(level == 1){
          return  l1Left;
      }
        if(level == 2){
            return  l2Left;
        }
        if(level == 3){
            return  l3Left;
        }
        if(level == 4){
            return  l4Left;
        }

        return 0;
    }

    public int getSeatStatus(int level,int row, int coloum){
        int[][] lev = getLevelSize(level);

        return lev[row][coloum];

    }

    public void setSeatStatus(int level, int row, int coloum, int status){
        int[][] le = getLevelSize(level);
        le[row][coloum] = status;


    }
    public int[][] getLevelSize(int level){
        int[][] lx = new int[0][0];
        if(level == 1){
            return l1;
        }
        if(level == 2){
            return l2;
        }
        if(level == 3){
            return l3;
        }
        if(level == 4){
            return l4;
        }
        return lx;
    }
    public Seat getLevelDims(int level) {

        if (level == 1) {
            return new Seat(25, 50);
        }
        if (level == 2) {
            return new Seat(20, 100);
        }
        if (level == 3) {
            return new Seat(15, 100);
        }
        if (level == 4) {
            return new Seat(15, 100);
        }
        return seat;
    }

    public int getReservationCost(int level,int resSeats){

        if (level == 1) {
            return resSeats*100 ;
        }
        if (level == 2) {
            return resSeats*75;
        }
        if (level == 3) {
            return resSeats*50;
        }
        if (level == 4) {
            return resSeats*40;
        }

        return 0;
    }

    /**
     *For testing only
     */

    public HashMap<Integer, SeatHold> getMhashmap() {
        return mhashmap;
    }

    public void setMhashmap(HashMap<Integer, SeatHold> mhashmap) {
        this.mhashmap = mhashmap;
    }
}
