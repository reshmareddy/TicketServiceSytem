import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by A871784 on 4/28/2016.
 */
public class SeatHoldMonitor extends Thread {
    private static final int HOLD_EXPIRE_TIME_IN_MILLI = 10000;

    private OnTimerExpiredListener listener;
    private SeatHold seatHold;

    public SeatHoldMonitor(OnTimerExpiredListener listener,SeatHold seatHold){
        this.listener = listener;
        this.seatHold = seatHold;
    }


    public void run() {
        // Loop for ten iterations.
        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(HOLD_EXPIRE_TIME_IN_MILLI);

            if(!listener.isSeatHoldRemoved(seatHold.getSeatId())){
                listener.onTimerExpired(seatHold);
                System.out.println("Seat hold id "+seatHold.getSeatId()+" seats are timeout");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
//    public void run() {
//            // Loop for ten iterations.
//            long startTime = System.currentTimeMillis();
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if(startTime-(System.currentTimeMillis()) > 10000){
//                if(mHashMap.containsKey(seatHoldId)){
//                    Optional<Integer> lev = mHashMap.get(seatHoldId).getLevel();
//                    seatList = mHashMap.get(seatHoldId).getSeatList();
//                    if (lev.isPresent()){
//                        if(lev.get() == 1){
//                            for(int i=0; i < seatList.size() ; i++ ){
//                                l1[seatList.get(i).getRow()][seatList.get(i).getRow()] = 0; //setting to reserved
//                            }
//                        }
//                    }
//
//                    mHashMap.remove(seatHoldId);
//
//                }
//              
//  System.out.println("Timed out");
//                System.exit(0);
//            }
//
//        }

    }


