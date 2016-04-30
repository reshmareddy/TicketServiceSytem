/**
 * Created by A871784 on 4/28/2016.
 */
public interface OnTimerExpiredListener {


    public void onTimerExpired(SeatHold seatHold);

    public boolean isSeatHoldRemoved(int holdID);
}
