import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by A871784 on 4/23/2016.
 */
public class SeatHold {
    Optional<Integer>  level;
    List<Seat> seatList = new ArrayList<Seat>();
    String email;
    int seatId;

    public SeatHold(Optional<Integer> level, List<Seat> seatList, String email, int seatId) {
        this.level = level;
        this.seatList.addAll(seatList);
        this.email = email;
        this.seatId = seatId;
    }

    public Optional<Integer> getLevel() {
        return level;
    }

    public void setLevel(Optional<Integer> level) {
        this.level = level;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }
}
