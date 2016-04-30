import java.util.HashMap;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;

public class ReservationServiceImplTest  {


    ReservationServiceImpl reservationService = new ReservationServiceImpl();



@Test
    public void testNumSeatsAvailable() throws Exception {
        int totalSeats =reservationService.numSeatsAvailable(Optional.of(1));
        Assert.assertNotNull(totalSeats);
        Assert.assertEquals(totalSeats,1250);
        totalSeats =reservationService.numSeatsAvailable(Optional.of(2));
        Assert.assertNotNull(totalSeats);
        Assert.assertEquals(totalSeats,2000);
        totalSeats =reservationService.numSeatsAvailable(Optional.of(3));
        Assert.assertNotNull(totalSeats);
        Assert.assertEquals(totalSeats,1500);
        totalSeats =reservationService.numSeatsAvailable(Optional.of(4));
        Assert.assertNotNull(totalSeats);
        Assert.assertEquals(totalSeats,1500);
        totalSeats =reservationService.numSeatsAvailable(Optional.of(5));
        Assert.assertNotNull(totalSeats);
        Assert.assertEquals(totalSeats,6250);


}
    @Test
    public void testFindAndHoldSeats() throws Exception {
        SeatHold heldSeats = reservationService.findAndHoldSeats(5, Optional.of(2), Optional.of(1), "resh");
        Assert.assertEquals("resh", heldSeats.getEmail().toString());
        Assert.assertNotNull(heldSeats.getSeatList());
        Assert.assertNotNull(heldSeats.getSeatId());
        Assert.assertEquals(Optional.of(1),heldSeats.getLevel());
        Assert.assertEquals(5,heldSeats.getSeatList().size());

        //) seats
        ReservationServiceImpl reservService = new ReservationServiceImpl();
        SeatHold hSeats = reservService.findAndHoldSeats(0, Optional.of(2), Optional.of(1), "dimmy");
        Assert.assertNull(hSeats);
        //seats more than individual levels capacity
        ReservationServiceImpl resService = new ReservationServiceImpl();
        SeatHold hSea = resService.findAndHoldSeats(3000, Optional.of(2), Optional.of(1), "dimmy");
        Assert.assertNull(hSea);
        //seats equal to level capacity
        ReservationServiceImpl rService = new ReservationServiceImpl();
        SeatHold hS = rService.findAndHoldSeats(1500, Optional.of(2), Optional.of(1), "dimmy");
        Assert.assertEquals("dimmy", hS.getEmail().toString());
        Assert.assertNotNull(hS.getSeatList());
        Assert.assertEquals(1500,hS.getSeatList().size());
        Assert.assertNotNull(hS.getSeatId());
        Assert.assertEquals(Optional.of(1), hS.getLevel());

    }
@Test
    public void testReserveSeats() throws Exception {
    ReservationServiceImpl reservService = new ReservationServiceImpl();
    SeatHold hSeats = reservService.findAndHoldSeats(5, Optional.of(2), Optional.of(1), "dimmy");
     HashMap<Integer,SeatHold> hashmap = new HashMap<Integer,SeatHold>();
    hashmap.put(hSeats.getSeatId(),hSeats);
    reservService.setMhashmap(hashmap);
    String code = reservService.reserveSeats(hSeats.getSeatId(),hSeats.getEmail());
    Assert.assertNotNull(code);
    Assert.assertEquals(code, String.valueOf(hSeats.getSeatId()));


    }



}