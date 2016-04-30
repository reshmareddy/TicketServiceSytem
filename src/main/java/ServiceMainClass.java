/**
 * Created by A871784 on 4/28/2016.
 */
public class ServiceMainClass {

    public static void main(String[] args) {
        ReservationServiceImpl reservationService = new ReservationServiceImpl();
        String totalSeats = reservationService.totalSeatsAvailable();
        System.out.println("Hello There! Seats available in this venue by level: " + totalSeats);
        //call numSeatsAvailable for each level


        reservationService.printMenu();

    }
}
