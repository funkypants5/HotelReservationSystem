/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsreservationclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import util.exception.RecordNotFoundException;

/**
 *
 * @author zchoo
 */
public class MainApp {

    private GuestSessionBeanRemote guestSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private GuestEntity currentGuest;
    private RoomRateSessionBeanRemote ratesSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private Scanner scanner = new Scanner(System.in);

    public MainApp(RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote ratesSessionBeanRemote,
            GuestSessionBeanRemote guestSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote,
            EmployeeSessionBeanRemote employeeSessionBeanRemote,
            PartnerSessionBeanRemote partnerSessionBeanRemote,
            RoomTypeSessionBeanRemote roomTypeSessionBeanRemote) {
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.ratesSessionBeanRemote = ratesSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
    }

    public void runApp() {

        System.out.println("\n*** Welcome to HoRS Reservation Client ***");
        visitorMenu();

    }

    private void visitorMenu() {
        while (true) {
            System.out.println("\n*** Visitor Menu ***");
            System.out.println("1: Guest Login");
            System.out.println("2: Register as Guest");
            System.out.println("3: Search Hotel Room");
            System.out.println("4: Exit");
            System.out.print("> ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    onlineVisitorLogin();
                    if (currentGuest != null) {
                        guestMenu();
                    }
                    break;
                case 2:
                    onlineVisitorRegister();
                    break;
                case 3:
                    Date checkInDate = null;
                    Date checkOutDate = null;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                    System.out.print("\nEnter check-in date (DD-MM-YYYY): ");
                    String checkInInput = scanner.nextLine();
                    System.out.print("Enter check-out date (DD-MM-YYYY): ");
                    String checkOutInput = scanner.nextLine();
                    try {
                        checkInDate = dateFormat.parse(checkInInput);
                        checkOutDate = dateFormat.parse(checkOutInput);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please use DD-MM-YYYY.");
                        break;
                    }
                    if (checkInDate.after(checkOutDate)) {
                        System.out.println("Error: Check in date must be before or equal to checkout date0");
                        break;
                    }
                    searchRooms(checkInDate, checkOutDate);
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            if (choice == 4) {
                break;
            }
        }
    }

    private void guestMenu() {
        while (currentGuest != null) {
            System.out.println("\n*** Guest Menu ***\n");
            System.out.println("1: Search Hotel Room");
            System.out.println("2: Reserve Hotel Room");
            System.out.println("3: View My Reservation Details");
            System.out.println("4: View All My Reservations");
            System.out.println("5: Logout");
            System.out.print("> ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Date checkInDate = null;
                    Date checkOutDate = null;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                    System.out.print("\nEnter check-in date (DD-MM-YYYY): ");
                    String checkInInput = scanner.nextLine();
                    System.out.print("Enter check-out date (DD-MM-YYYY): ");
                    String checkOutInput = scanner.nextLine();
                    try {
                        checkInDate = dateFormat.parse(checkInInput);
                        checkOutDate = dateFormat.parse(checkOutInput);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please use DD-MM-YYYY.");
                        break;
                    }
                    if (!checkOutDate.after(checkInDate)) {
                        System.out.println("Error: Check in date must be before checkout date. No same day checkout allowed either.");
                        break;
                    }
                    searchRooms(checkInDate, checkOutDate);
                    break;
                case 2:
                    reserveRoom();
                    break;
                case 3:
                    viewReservationDetails();
                    break;
                case 4:
                    viewAllReservations();
                    break;
                case 5:
                    currentGuest = null;
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public void onlineVisitorLogin() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            currentGuest = guestSessionBeanRemote.visitorLogin(email, password);
            System.out.println("Welcome " + currentGuest.getName() + "!");
            guestMenu();
        } catch (InvalidLoginCredentialException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    public void onlineVisitorRegister() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        try {
            guestSessionBeanRemote.visitorRegister(name, email, password);
            currentGuest = guestSessionBeanRemote.retrieveVisitorByEmail(email);
            System.out.println("Registration successful! Guest account with id, " + currentGuest.getGuestId() + " created!\n");
            guestMenu();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Pair<Integer, Long> searchRooms(Date checkInDate, Date checkOutDate) {
        Scanner sc = new Scanner(System.in);
        try {
            List<RoomTypeEntity> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();
            int count = 0;
            for (RoomTypeEntity roomType : roomTypes) {
                count++;
                System.out.println(count + ".  " + roomType);
            }
        } catch (RecordNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        System.out.print("Please enter room type id: ");
        Long roomTypeId = sc.nextLong();
        sc.nextLine();

        List<RoomEntity> rooms = roomSessionBeanRemote.roomsAvailable(roomTypeId, checkInDate, checkOutDate);
        int numOfRoomsAvailable = roomSessionBeanRemote.getAvailableRoomCountForType(roomTypeId, checkInDate, checkOutDate);
        BigDecimal price = ratesSessionBeanRemote.calculateSearchRoomFee(checkInDate, checkOutDate, roomTypeId);
        if (numOfRoomsAvailable == 0) {
            System.out.println("No rooms available for selected room type and date.");
            return new Pair<>(0, 0L);
        }
        int count = 0;
        System.out.println("List of available rooms \n");
        for (RoomEntity room : rooms) {
            count++;
            System.out.println(count + ". " + room + "\n");
        }
        System.out.println("Number of rooms available for room type: " + numOfRoomsAvailable + "\n");
        System.out.println("Price for of 1 room for selected room type over the selected dates: $" + price);
        return new Pair<>(numOfRoomsAvailable, roomTypeId);

    }

    public void reserveRoom() {
        Date checkInDate = null;
        Date checkOutDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter check-in date (DD-MM-YYYY): ");
        String checkInInput = sc.nextLine();
        System.out.print("Enter check-out date (DD-MM-YYYY): ");
        String checkOutInput = sc.nextLine();
        try {
            checkInDate = dateFormat.parse(checkInInput);
            checkOutDate = dateFormat.parse(checkOutInput);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
            return; // Exit the method upon error
        }
        if (!checkOutDate.after(checkInDate)) {
            System.out.println("Error: Check in date must be before checkout date. No same day checkout allowed either.");
            return;
        }

        Pair<Integer, Long> pair = searchRooms(checkInDate, checkOutDate);
        int numOfRoomsAvailable = pair.getKey();
        if (numOfRoomsAvailable == 0) {
            return;
        }

        Long roomTypeId = pair.getValue();

        System.out.print("Kindly key in number of rooms: ");
        int numberOfRooms = sc.nextInt();
        sc.nextLine();
        if (numberOfRooms > numOfRoomsAvailable) {
            System.out.println("Rooms required is above available capacity. Current rooms available for room type: " + numOfRoomsAvailable + "\n");
            return;
        }

        Long reservationId = reservationSessionBeanRemote.createNewReservation(new ReservationEntity(numberOfRooms, checkInDate, checkOutDate), currentGuest.getGuestId(), roomTypeId);
        System.out.println("\nReservation with reservationId, " + reservationId + " created for guest with guestId, " + currentGuest.getGuestId() + "\n");
        ratesSessionBeanRemote.calculateTotalReservationFee(reservationId);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(checkInDate);
        Calendar currentCalendar = Calendar.getInstance();

        boolean isCheckInToday = (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
                && calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR));

        int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        if (isCheckInToday && currentHour >= 2) {
            reservationSessionBeanRemote.createReserveRoom(reservationId, checkInDate, checkOutDate);
        }

    }

    public void viewReservationDetails() {
        if (currentGuest == null) {
            System.out.println("You must be logged in to view reservation details.");
            return;
        }

        try {
            // Step 1: Retrieve all reservations for the current guest
            List<ReservationEntity> reservations = reservationSessionBeanRemote.viewAllReservations(currentGuest.getEmail());

            if (reservations.isEmpty()) {
                System.out.println("No reservations found for your account.");
                return;
            }

            // Step 2: Display all reservations associated with the guest
            System.out.println("\nList of your reservations:");
            for (ReservationEntity reservation : reservations) {
                System.out.println("Reservation ID: " + reservation.getReservationId()
                        + ", Check-In Date: " + reservation.getCheckIn()
                        + ", Check-Out Date: " + reservation.getCheckOut()
                        + ", Status: " + reservation.getReservationStatus());
            }

            // Step 3: Ask the guest to enter the Reservation ID to view specific details
            System.out.print("\nEnter the Reservation ID to view details: ");
            Long reservationId = scanner.nextLong();
            scanner.nextLine(); // Clear buffer

            // Step 4: Fetch and display details of the selected reservation
            ReservationEntity selectedReservation = null;
            for (ReservationEntity reservation : reservations) {
                if (reservation.getReservationId().equals(reservationId)) {
                    selectedReservation = reservation;
                    break;
                }
            }

            // Step 5: If reservation is found, display its details
            if (selectedReservation != null) {
                System.out.println("\nReservation Details:");
                System.out.println("Reservation ID: " + selectedReservation.getReservationId());
                System.out.println("Check-In Date: " + selectedReservation.getCheckIn());
                System.out.println("Check-Out Date: " + selectedReservation.getCheckOut());
                System.out.println("Status: " + selectedReservation.getReservationStatus());
                System.out.println("Number Of Rooms: " + selectedReservation.getNumOfRooms());
                System.out.println(selectedReservation.getRoomType());
                System.out.println("Total Price: " + selectedReservation.getPrice());
            } else {
                System.out.println("No reservation found with the given ID.");
            }

        } catch (Exception e) {
            System.out.println("Error retrieving reservation details: " + e.getMessage());
        }
    }

    public void viewAllReservations() {
        try {
            List<ReservationEntity> reservations = reservationSessionBeanRemote.viewAllReservations(currentGuest.getEmail());

            if (reservations.isEmpty()) {
                System.out.println("No reservations found for this guest.");
            } else {
                System.out.println("List of Reservations:");
                for (ReservationEntity reservation : reservations) {
                    System.out.println("Reservation ID: " + reservation.getReservationId()
                            + ", Check-In Date: " + reservation.getCheckIn()
                            + ", Check-Out Date: " + reservation.getCheckOut()
                            + ", Status: " + reservation.getReservationStatus());
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving reservations: " + e.getMessage());
        }
    }
}
