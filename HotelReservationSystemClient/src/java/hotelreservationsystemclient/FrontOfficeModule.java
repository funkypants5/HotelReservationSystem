/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.EmployeeEntity;
import entity.GuestEntity;
import entity.ReservationEntity;
import entity.ReserveRoomEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;
import javax.ejb.EJB;
import util.exception.RecordNotFoundException;

/**
 *
 * @author zchoo
 */
public class FrontOfficeModule {

    private GuestSessionBeanRemote guestSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private EmployeeEntity employee;

    public FrontOfficeModule(GuestSessionBeanRemote guestSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote,
            RoomSessionBeanRemote roomSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote,
            EmployeeEntity employee) {
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.employee = employee;
    }

    public void runModule() {
        Scanner sc = new Scanner(System.in);
        int response;

        while (true) {
            System.out.println("\n***Welcome " + employee.getFirstName() + " to Front Office Module***\n");
            System.out.println("1: Walk-in Search Room");
            System.out.println("2: Walk-in Reserve Room");
            System.out.println("3: Check-in Guest");
            System.out.println("4: Check-out Guest");
            System.out.println("5: Manually Trigger Room Allocation");
            System.out.println("6: Logout");
            System.out.print("Enter your choice (1-6): ");
            response = sc.nextInt();
            sc.nextLine();

            if (response == 1) {
                Date checkInDate = null;
                Date checkOutDate = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                System.out.print("Enter check-in date (DD-MM-YYYY): ");
                String checkInInput = sc.nextLine();
                System.out.print("Enter check-out date (DD-MM-YYYY): ");
                String checkOutInput = sc.nextLine();
                try {
                    checkInDate = dateFormat.parse(checkInInput);
                    checkOutDate = dateFormat.parse(checkOutInput);
                    if (!checkOutDate.after(checkInDate)) {
                        System.out.println("Error: Check in date must be before checkout date. No same day checkout allowed either.");
                        break;
                    }
                    walkInSearchRoom(checkInDate, checkOutDate);
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please use DD-MM-YYYY.");
                    break; // Exit the method upon error
                }

            } else if (response == 2) {
                walkInReserveRoom();
            } else if (response == 3) {
                checkInGuest();
            } else if (response == 4) {
                System.out.print("Enter guest email: ");
                String email = sc.nextLine();
                checkOutGuest(email);
            } else if (response == 5) {
                triggerRoomAllocation();
            } else if (response == 6) {
                break;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private Pair<Integer, Long> walkInSearchRoom(Date checkInDate, Date checkOutDate) {
        Scanner sc = new Scanner(System.in);
        Long roomTypeId;
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
        roomTypeId = sc.nextLong();
        sc.nextLine();

        List<RoomEntity> rooms = roomSessionBeanRemote.roomsAvailable(roomTypeId, checkInDate, checkOutDate);
        int numOfRoomsAvailable = rooms.size();

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

        BigDecimal price = roomRateSessionBeanRemote.calculateWalkInSearchFee(checkInDate, checkOutDate, roomTypeId);
        System.out.println("Number of rooms available for room type: " + numOfRoomsAvailable + "\n");
        System.out.println("Price for of 1 room for selected room type over the selected dates: $" + price);

        return new Pair<>(numOfRoomsAvailable, roomTypeId);

    }

    private void walkInReserveRoom() {
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

        Pair<Integer, Long> pair = walkInSearchRoom(checkInDate, checkOutDate);
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

        System.out.print("Kindly key in guest name: ");
        String name = sc.nextLine();
        System.out.print("Kindly key in guest email: ");
        String email = sc.nextLine();

        Long guestId = guestSessionBeanRemote.createNewGuest(new GuestEntity(name, email));
        Long reservationId = reservationSessionBeanRemote.createNewReservation(new ReservationEntity(numberOfRooms, checkInDate, checkOutDate), guestId, roomTypeId);
        roomRateSessionBeanRemote.calculateTotalWalkInReservationFee(reservationId);
        System.out.println("Reservation with reservationId, " + reservationId + " created for guest with guestId, " + guestId);
        ReservationEntity reservation = reservationSessionBeanRemote.retrieveReservationsById(reservationId);
        System.out.println("Reservation price: " + reservation.getPrice());

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

    private void checkInGuest() {
        Scanner sc = new Scanner(System.in);
        BigDecimal totalPrice = BigDecimal.ZERO;
        System.out.print("Enter guest email: ");
        String email = sc.nextLine();
        List<ReservationEntity> reservations = reservationSessionBeanRemote.retrieveReservations(email);
        System.out.println("\nRooms for customer as stated below \n");
        for (ReservationEntity reservation : reservations) {

            int count = 0;
            List<ReserveRoomEntity> reserveRooms = reservationSessionBeanRemote.retrieveReserveRooms(reservation.getReservationId());
            for (ReserveRoomEntity reserveRoom : reserveRooms) {
                count++;
                System.out.println(count + ". " + reserveRoom.getRoom());
            }
            reservationSessionBeanRemote.setCheckIn(reservation.getReservationId());

            totalPrice = totalPrice.add(reservation.getPrice());

        }
        System.out.println("Total price for payment: " + totalPrice);
    }

    private void checkOutGuest(String email) {
        List<ReservationEntity> reservations = reservationSessionBeanRemote.retrieveReservations(email);
        System.out.println("\nRooms customer stayed. Please ensure all keys are collected \n");
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ReservationEntity reservation : reservations) {
            int count = 0;
            List<ReserveRoomEntity> reserveRooms = reservationSessionBeanRemote.retrieveReserveRooms(reservation.getReservationId());
            for (ReserveRoomEntity reserveRoom : reserveRooms) {
                count++;
                System.out.println(count + ". " + reserveRoom.getRoom());
            }
            reservationSessionBeanRemote.setCheckOut(reservation.getReservationId());
            reservation = reservationSessionBeanRemote.retrieveReservationsById(reservation.getReservationId());
            totalPrice = totalPrice.add(reservation.getPrice());

        }
        System.out.println("Total price for payment: " + totalPrice);
    }

    private void triggerRoomAllocation() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the date for manual room allocation (dd-MM-yyyy): ");
        String dateString = scanner.nextLine();

        try {
            Date date = sdf.parse(dateString);
            reservationSessionBeanRemote.manualAllocateRooms(date);
            System.out.println("Rooms allocated successfully! \n");
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
        }
    }

}
