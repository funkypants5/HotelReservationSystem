/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreserservationsystemseclient;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import ws.hotelreservationsystem.HotelReservationSystemWebService_Service;
import ws.hotelreservationsystem.PartnerEntity;
import ws.hotelreservationsystem.ReservationEntity;
import ws.hotelreservationsystem.RoomEntity;
import ws.hotelreservationsystem.RoomTypeEntity;

/**
 *
 * @author zchoo
 */
public class MainApp {

    private HotelReservationSystemWebService_Service service;
    private Scanner sc;
    private PartnerEntity partner;
    private Date checkInDate;
    private Date checkOutDate;
    private Long roomTypeId;
    private int numOfRoomsAvailable;
    private List<ReservationEntity> reservations;

    public MainApp() {
        service = new HotelReservationSystemWebService_Service();
        sc = new Scanner(System.in);
        this.checkInDate = null;
        this.checkOutDate = null;
    }

    public void runApp() {
        while (true) {
            System.out.println("\n***Welcome to Hotel Reservation Web Service***\n");
            System.out.println("What would you like to do today?\n");
            System.out.println("1: Login to account");
            System.out.println("2: Search room");
            System.out.println("3: Exit");
            System.out.print(">");
            int response = sc.nextInt();
            sc.nextLine();
            if (response == 1) {
                login();
            } else if (response == 2) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                System.out.print("Enter check-in date (DD-MM-YYYY): ");
                String checkInInput = sc.nextLine();
                System.out.print("Enter check-out date (DD-MM-YYYY): ");
                String checkOutInput = sc.nextLine();
                try {
                    this.checkInDate = dateFormat.parse(checkInInput);
                    this.checkOutDate = dateFormat.parse(checkOutInput);
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please use DD-MM-YYYY.");
                    return; // Exit the method upon error
                }
                if (!checkOutDate.after(checkInDate)) {
                    System.out.println("Error: Check in date must be before checkout date. No same day checkout allowed either.");
                    return;
                }
                searchRooms();
            } else if (response == 3) {
                break;
            } else {
                System.out.println("Invalid input!");
            }
        }
    }

    private void login() {
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        try {
            this.partner = service.getHotelReservationSystemWebServicePort().loginPartner(email, password);
            System.out.println("Login as partner: " + partner.getPartnerName());
            runPartnerMenu();
        } catch (Exception ex) {
            System.out.println("Error: Username or password is invalid");
        }
    }

    private void runPartnerMenu() {
        while (true) {
            System.out.println("\n***Welcome " + partner.getPartnerName() + " to Hotel Reservation Web Service***\n");
            System.out.println("What would you like to do today?");
            System.out.println("1: Search Room");
            System.out.println("2: Reserve Room");
            System.out.println("3: View Reservation Details");
            System.out.println("4: View All Reservations");
            System.out.println("5: Logout");
            System.out.print(">");
            int response = sc.nextInt();
            sc.nextLine();
            if (response == 1) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                System.out.print("Enter check-in date (DD-MM-YYYY): ");
                String checkInInput = sc.nextLine();
                System.out.print("Enter check-out date (DD-MM-YYYY): ");
                String checkOutInput = sc.nextLine();
                try {
                    this.checkInDate = dateFormat.parse(checkInInput);
                    this.checkOutDate = dateFormat.parse(checkOutInput);
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please use DD-MM-YYYY.");
                    return; // Exit the method upon error
                }
                if (!checkOutDate.after(checkInDate)) {
                    System.out.println("Error: Check in date must be before checkout date. No same day checkout allowed either.");
                    return;
                }
                searchRooms();
            } else if (response == 2) {
                reserveRoom();
            } else if (response == 3) {
                viewReservationDetails();
            } else if (response == 4) {
                viewAllReservations();
            } else if (response == 5) {
                break;
            } else {
                System.out.println("Invalid input!");
            }
        }
    }

    private void reserveRoom() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        System.out.print("Enter check-in date (DD-MM-YYYY): ");
        String checkInInput = sc.nextLine();
        System.out.print("Enter check-out date (DD-MM-YYYY): ");
        String checkOutInput = sc.nextLine();
        try {
            this.checkInDate = dateFormat.parse(checkInInput);
            this.checkOutDate = dateFormat.parse(checkOutInput);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
            return; // Exit the method upon error
        }
        if (!checkOutDate.after(checkInDate)) {
            System.out.println("Error: Check in date must be before checkout date. No same day checkout allowed either.");
            return;
        }
        searchRooms();
        if (numOfRoomsAvailable == 0) {
            return;
        }
        System.out.print("Kindly key in number of rooms: ");
        int numberOfRooms = sc.nextInt();
        sc.nextLine();
        System.out.println("Maximum number of rooms available for reservation: " + numOfRoomsAvailable);
        if (numberOfRooms > numOfRoomsAvailable) {
            System.out.println("Rooms required is above available capacity. Current rooms available for room type: " + numOfRoomsAvailable + "\n");
            return;
        }
        Long reservationId = service.getHotelReservationSystemWebServicePort().createNewReservationForPartner(numberOfRooms, checkInDate, checkOutDate, partner.getPartnerId(), roomTypeId);
        System.out.println("\nReservation with reservationId, " + reservationId + " created for partner with partnerId, " + partner.getPartnerId() + "\n");
        service.getHotelReservationSystemWebServicePort().calculateTotalReservationFee(reservationId);
        this.partner = service.getHotelReservationSystemWebServicePort().loginPartner(partner.getEmail(), partner.getPassword());

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(checkInDate);
        Calendar currentCalendar = Calendar.getInstance();

        boolean isCheckInToday = (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
                && calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR));

        int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        if (isCheckInToday && currentHour >= 2) {
            service.getHotelReservationSystemWebServicePort().createReserveRoom(reservationId, checkInDate, checkOutDate);
        }
    }

    private void viewReservationDetails() {
        try {

            viewAllReservations();

            System.out.print("\nEnter the Reservation ID to view details: ");
            Long reservationId = sc.nextLong();
            sc.nextLine();

            ReservationEntity selectedReservation = null;
            for (ReservationEntity reservation : reservations) {
                if (reservation.getReservationId().equals(reservationId)) {
                    selectedReservation = reservation;
                    break;
                }
            }

            if (selectedReservation != null) {
                System.out.println("\nReservation Details:");
                System.out.println("Reservation ID: " + selectedReservation.getReservationId());
                System.out.println("Check-In Date: " + selectedReservation.getCheckIn());
                System.out.println("Check-Out Date: " + selectedReservation.getCheckOut());
                System.out.println("Room Type: " + selectedReservation.getRoomType().getRoomTypeName());
                System.out.println("Number Of Rooms: " + selectedReservation.getNumOfRooms());
                System.out.println("Total Price: " + selectedReservation.getPrice());
            } else {
                System.out.println("No reservation found with the given ID.");
            }

        } catch (Exception e) {
            System.out.println("Error retrieving reservation details: " + e.getMessage());
        }
    }

    private void viewAllReservations() {
        try {
            this.reservations = service.getHotelReservationSystemWebServicePort().viewAllPartnerReservations(partner.getEmail());

            if (reservations.isEmpty()) {
                System.out.println("No reservations found for this guest.");
            } else {
                System.out.println("\nList of Reservations:");
                for (ReservationEntity reservation : reservations) {
                    System.out.println("Reservation ID: " + reservation.getReservationId()
                            + ", Check-In Date: " + reservation.getCheckIn()
                            + ", Check-Out Date: " + reservation.getCheckOut());
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving reservations: " + e.getMessage());
        }
    }

    private void searchRooms() {
        try {
            List<RoomTypeEntity> roomTypes = service.getHotelReservationSystemWebServicePort().viewAllRoomTypes();
            int count = 0;
            for (RoomTypeEntity roomType : roomTypes) {
                count++;
                System.out.println("\n" + count + ".\nRoom Type Id: " + roomType.getRoomTypeId());
                System.out.println("Room Type Name: " + roomType.getRoomTypeName());
                System.out.println("Beds: " + roomType.getBed());
                System.out.println("Capacity: " + roomType.getCapacity());
                System.out.println("Ammenities: " + roomType.getAmenities());
                System.out.println("Description: " + roomType.getDescription() + "\n");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        System.out.print("Please enter room type id: ");
        this.roomTypeId = sc.nextLong();
        sc.nextLine();
        List<RoomEntity> rooms = service.getHotelReservationSystemWebServicePort().roomsAvailable(roomTypeId, checkInDate, checkOutDate);
        numOfRoomsAvailable = service.getHotelReservationSystemWebServicePort().getAvailableRoomCountForType(roomTypeId, checkInDate, checkOutDate);
        if (numOfRoomsAvailable == 0) {
            System.out.println("No rooms available for room type!");
            return;
        }
        BigDecimal price = service.getHotelReservationSystemWebServicePort().calculateSearchRoomFee(checkInDate, checkOutDate, roomTypeId);

        int count = 0;
        System.out.println("List of available rooms \n");
        for (RoomEntity room : rooms) {
            count++;
            System.out.println("\n" + count + ".\nRoom id:" + room.getRoomId());
            System.out.println("Room number: " + room.getRoomNumber());
            System.out.println("Room type: " + room.getRoomType().getRoomTypeName() + "\n");
        }
        System.out.println("Maximum number of rooms available for reservation: " + numOfRoomsAvailable);
        System.out.println("Price for of 1 room for selected room type over the selected dates: $" + price);
    }

}
