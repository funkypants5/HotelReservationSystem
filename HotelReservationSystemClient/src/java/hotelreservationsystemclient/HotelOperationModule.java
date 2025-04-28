/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemclient;

import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.EmployeeEntity;
import entity.RoomTypeEntity;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRights;
import util.exception.RecordNotFoundException;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import entity.RoomAllocationReportEntity;
import entity.RoomEntity;
import entity.RoomRateEntity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author zchoo
 */
public class HotelOperationModule {

    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private EmployeeEntity employee;

    public HotelOperationModule(RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, EmployeeEntity employee) {
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.employee = employee;
    }

    void runModule() {
        Scanner sc = new Scanner(System.in);
        int response = 0;
        System.out.println("\n ***Welcome " + employee.getFirstName() + " to Hotel Operations Module*** \n");
        if (employee.getAccessRightsEnum() == AccessRights.OPERATIONMANAGER) {
            runOperationManagerMenu();
        }
        if (employee.getAccessRightsEnum() == AccessRights.SALESMANAGER) {
            runSalesManagerMenu();
        }
    }

    private void runOperationManagerMenu() {
        Scanner sc = new Scanner(System.in);
        int response;

        while (true) {
            System.out.println("\n*** Welcome to the Sales Manager Module ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View Room Type Details");
            System.out.println("3: Update Room Type");
            System.out.println("4: Delete Room Type");
            System.out.println("5: View All Room Types");
            System.out.println("6: Create New Room");
            System.out.println("7: Update Room");
            System.out.println("8: Delete Room");
            System.out.println("9: View All Rooms");
            System.out.println("10: View Room Allocation Exception Report");
            System.out.println("11: Logout");
            System.out.print("> ");
            response = sc.nextInt();
            sc.nextLine();

            if (response == 1) {
                createNewRoomType();
            } else if (response == 2) {
                System.out.print("Please enter room type name: ");
                String roomTypeName = sc.nextLine();
                viewRoomTypeDetails(roomTypeName);
            } else if (response == 3) {
                updateRoomType();
            } else if (response == 4) {
                deleteRoomType();
            } else if (response == 5) {
                viewAllRoomTypes();
            } else if (response == 6) {
                createNewRoom();
            } else if (response == 7) {
                updateRoom();
            } else if (response == 8) {
                deleteRoom();
            } else if (response == 9) {
                viewAllRooms();
            } else if (response == 10) {
                viewRoomAllocationExceptionReport();
            } else if (response == 11) {
                break;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private void createNewRoomType() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Please enter room type name: ");
            String roomName = sc.nextLine();
            System.out.print("Please enter room type description (max 255 characters): ");
            String description = sc.nextLine();
            System.out.print("Please enter room type size in square meters (please exclude typing in the units): ");
            String roomSize = sc.nextLine();
            System.out.print("Please enter number of beds for each bed type (eg. 1Queen, 2King): ");
            String bed = sc.nextLine();
            System.out.print("Please enter capacity of room type (max number of occupants): ");
            String capacity = sc.nextLine();
            System.out.print("Please enter ammenities of the room type: ");
            String ammenities = sc.nextLine();
            viewAllRoomTypes();
            Long nextHighest = null;
            System.out.print("Does this room type have a higher room type? Enter Y if yes or any other key if no: ");
            String response = sc.nextLine().toUpperCase();
            if (response.equals("Y")) {
                System.out.print("Please enter next highest room type ID: ");
                nextHighest = sc.nextLong();
            }
            Long roomId = roomTypeSessionBeanRemote.createNewRoomType(new RoomTypeEntity(roomName, description, roomSize, bed, capacity, ammenities), nextHighest);
            System.out.println("New room type created with roomType Id: " + roomId + "\n");
        } catch (RecordNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private Long viewRoomTypeDetails(String roomTypeName) {

        try {
            RoomTypeEntity roomTypeEntity = roomTypeSessionBeanRemote.retrieveRoomType(roomTypeName);
            System.out.println("Room type details as follows: \n" + roomTypeEntity);
            return roomTypeEntity.getRoomTypeId();
        } catch (RecordNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return new Long(0); //dummy value
    }

    private void updateRoomType() {
        Scanner sc = new Scanner(System.in);
        viewAllRoomTypes();
        System.out.print("Please enter name of room type you wish to update: ");
        String roomTypeName = sc.nextLine();
        Long roomTypeId = viewRoomTypeDetails(roomTypeName);
        if(roomTypeId == 0) {
            return;
        }
        System.out.println("Please select the room type detail you wish to update: ");
        System.out.println("1. Name");
        System.out.println("2. Description");
        System.out.println("3. Size");
        System.out.println("4. Beds");
        System.out.println("5. Capacity");
        System.out.println("6. Amenities");
        System.out.println("7: Allocate Next Higher Room Type");
        System.out.println("8: Update Room Type Status");
        System.out.print("Enter your choice (1-8): ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        // Update the selected field based on user choice
        switch (choice) {
            case 1:
                System.out.print("Enter new room name: ");
                String newName = sc.nextLine();
                roomTypeSessionBeanRemote.updateRoomTypeName(roomTypeId, newName);
                break;
            case 2:
                System.out.print("Enter new description: ");
                String newDescription = sc.nextLine();
                roomTypeSessionBeanRemote.updateRoomTypeDescription(roomTypeId, newDescription);
                break;
            case 3:
                System.out.print("Enter new room size(Square Meters)s: ");
                String newSize = sc.nextLine();
                roomTypeSessionBeanRemote.updateRoomTypeSize(roomTypeId, newSize);
                break;
            case 4:
                System.out.print("Enter new number of beds for each bed type (eg. 1Queen, 2King): ");
                String newBed = sc.nextLine();
                roomTypeSessionBeanRemote.updateRoomTypeBeds(roomTypeId, newBed);
                break;
            case 5:
                System.out.print("Enter new capacity: ");
                String newCapacity = sc.nextLine();
                roomTypeSessionBeanRemote.updateRoomTypeCapacity(roomTypeId, newCapacity);
                break;
            case 6:
                System.out.print("Enter new amenities: ");
                String newAmenities = sc.nextLine();
                roomTypeSessionBeanRemote.updateRoomTypeAmenities(roomTypeId, newAmenities);
                break;
            case 7:
                viewAllRoomTypes();
                System.out.print("Enter new next highest room type Id: ");
                Long nextHigherRoomTypeId = sc.nextLong();
                try {
                    roomTypeSessionBeanRemote.updateRoomTypeNextHigherRoomType(roomTypeId, nextHigherRoomTypeId);
                } catch (RecordNotFoundException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
                break;

            case 8:
                System.out.println("Key in option to edit room type status: ");
                System.out.println("1: Set available");
                System.out.println("2: Set disabled");
                System.out.print("> ");
                int response = sc.nextInt();
                sc.nextLine();
                if (response == 1) {
                    roomTypeSessionBeanRemote.setRoomTypeStatus(roomTypeId, "AVAILABLE");
                    break;
                } else if (response == 2) {
                    roomTypeSessionBeanRemote.setRoomTypeStatus(roomTypeId, "DISABLED");
                    break;
                } else {
                    System.out.println("Invalid input");
                    break;
                }

            default:
                System.out.println("Invalid selection. Please choose a valid room type detail.");
                break;
        }

        System.out.println("Room type with id " + roomTypeId + " updated successfully.");
    }

    private void deleteRoomType() {
        try {
            Scanner sc = new Scanner(System.in);
            viewAllRoomTypes();
            System.out.print("Select id of room type for deletion: ");
            Long roomTypeId = sc.nextLong();
            sc.nextLine();
            String roomTypeName = roomTypeSessionBeanRemote.deleteRoomType(roomTypeId);
            System.out.println("\nRoom Type " + roomTypeName + " with id " + roomTypeId + " is deleted! \n");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void viewAllRoomTypes() {
        try {
            List<RoomTypeEntity> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();
            int count = 0;
            for (RoomTypeEntity roomType : roomTypes) {
                count++;
                System.out.println(count + ".  " + roomType + "\n");
            }
        } catch (RecordNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void createNewRoom() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.print("Please enter the room number (e.g., 2015 for the fifteenth room on floor twenty): ");
            String roomNumber = sc.nextLine().trim();
            viewAllRoomTypes();
            System.out.print("Please enter room type id from the above list: ");
            Long roomTypeId = sc.nextLong();

            System.out.println("Please enter room status: ");
            System.out.println("1: AVAILABLE");
            System.out.println("2: NOTAVAILABLE");
            int response = sc.nextInt();
            sc.nextLine();
            String status = "";
            if(response == 1) {
                status = "AVAILABLE";
            } else if(response == 2) {
                status = "NOTAVAILABLE";
            } else {
                System.out.println("Invalid option");
                return;
            }
            

            RoomEntity newRoom = new RoomEntity(roomNumber, status);

            // Call the method from the session bean to persist the new room
            Long roomId = roomSessionBeanRemote.createNewRoom(roomTypeId, newRoom);

            System.out.println("\nNew room created successfully with Id " + roomId + "\n");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateRoom() {
        Scanner sc = new Scanner(System.in);
        viewAllRooms();
        System.out.print("Please enter the room number you wish to update: ");
        String roomNumber = sc.nextLine().trim();
        Long roomId = new Long(0); //place holder
        try {
            RoomEntity room = roomSessionBeanRemote.retrieveRoom(roomNumber);
            roomId = room.getRoomId();
        } catch (RecordNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
            return;
        }

        System.out.println("Please select the room detail you wish to update: ");
        System.out.println("1. Room Number");
        System.out.println("2. Room Type");
        System.out.println("3. Room Status");
        System.out.print("Enter your choice (1-3): ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        // Update the selected field based on user choice
        switch (choice) {
            case 1:
                System.out.print("Enter new room number: ");
                String newRoomNumber = sc.nextLine().trim();
                roomSessionBeanRemote.updateRoomNumber(roomId, newRoomNumber);
                break;
            case 2:
                viewAllRoomTypes();
                System.out.print("Enter new room type ID: ");
                Long newRoomTypeId = sc.nextLong();
                try {
                roomSessionBeanRemote.updateRoomType(roomId, newRoomTypeId);
                } catch (RecordNotFoundException ex) {
                    System.out.println("Error: " + ex.getMessage());
                    return;
                }
                break;
            case 3:
                while (true) {
                    System.out.println("Enter new room status: ");
                    System.out.println("1: Available");
                    System.out.println("2: Not Available");
                    System.out.print("> ");
                    int response = sc.nextInt();
                    if (response == 1) {
                        roomSessionBeanRemote.setAvailable(roomId);
                        break;
                    } else if (response == 2) {
                        roomSessionBeanRemote.setNotAvilable(roomId);
                        break;
                    } else {
                        System.out.println("Invalide option");
                    }

                }
                break;
            default:
                System.out.println("Invalid selection. Please choose a valid room detail.");
                break;
        }

        System.out.println("Room with ID " + roomId + " updated successfully.");
    }

    private void deleteRoom() {
        try {
            Scanner sc = new Scanner(System.in);
            viewAllRooms();
            System.out.print("Enter room number for deletion: ");
            String roomNumber = sc.nextLine().trim();
            Long roomId = roomSessionBeanRemote.deleteRoom(roomNumber);
            System.out.println("\nRoom number: " + roomNumber + " with room id: " + roomId + " deleted! \n");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    private void viewAllRooms() {
        int count = 0;
        try {
            List<RoomEntity> rooms = roomSessionBeanRemote.viewAllRoom();
            System.out.println("List of all rooms: ");
            for (RoomEntity room : rooms) {
                count++;
                System.out.println(count + " " + room + "\n");
            }
        } catch (RecordNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void viewRoomAllocationExceptionReport() {
        Scanner sc = new Scanner(System.in);
        Date checkInDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        System.out.print("Enter check-in date(DD-MM-YYYY) to view allocation report of the reservations with that check in date: ");
        String checkInInput = sc.nextLine();
        try {
            checkInDate = dateFormat.parse(checkInInput);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
            return; // Exit the method upon error
        }
        List<RoomAllocationReportEntity> reports = roomSessionBeanRemote.retrieveExceptionReport(checkInDate);
        System.out.println("\nList of room allocation reports\n");
        int count = 0;
        for (RoomAllocationReportEntity report : reports) {
            count++;
            System.out.println(count + ". " + report);
        }
    }

    private void runSalesManagerMenu() {
        Scanner sc = new Scanner(System.in);
        int response;
        while (true) {

            System.out.println("\n1: Create New Room Rate");
            System.out.println("2: View Room Rate Details");
            System.out.println("3: Update Room Rate");
            System.out.println("4: Delete Room Rate");
            System.out.println("5: View All Room Rates");
            System.out.println("6: Logout");
            System.out.print("> ");
            response = sc.nextInt();

            if (response == 1) {
                createNewRoomRate();
            } else if (response == 2) {
                System.out.print("Enter room rate ID: ");
                Long roomRateId = sc.nextLong();
                viewRoomRateDetails(roomRateId);
            } else if (response == 3) {
                updateRoomRate();
            } else if (response == 4) {
                deleteRoomRate();
            } else if (response == 5) {
                viewAllRoomRates();
            } else if (response == 6) {
                break;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public void createNewRoomRate() {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            // View all room types for user selection
            viewAllRoomTypes();

            System.out.print("Enter room type ID: ");
            Long roomTypeId = sc.nextLong();
            sc.nextLine();

            System.out.print("Enter rate name: ");
            String name = sc.nextLine();

            System.out.print("Enter rate amount: ");
            BigDecimal rateAmount = sc.nextBigDecimal();
            sc.nextLine();

            String rateType;

            // Rate type selection
            System.out.println("Select rate type: ");
            System.out.println("1: PUBLISHED");
            System.out.println("2: NORMAL");
            System.out.println("3: PEAK");
            System.out.println("4: PROMOTION");
            System.out.print("> ");
            int rateTypeOption = sc.nextInt();
            sc.nextLine();

            // Initialize check-in and check-out dates to null
            Date checkInDate = null;
            Date checkOutDate = null;

            if (rateTypeOption == 1) {
                rateType = "PUBLISHED";
            } else if (rateTypeOption == 2) {
                rateType = "NORMAL";
            } else if (rateTypeOption == 3) {
                rateType = "PEAK";
                System.out.print("Enter check-in date (DD-MM-YYYY)(Include the dashed please): ");
                String checkInInput = sc.nextLine();
                System.out.print("Enter check-out date (DD-MM-YYYY)(Include the dashed please): ");
                String checkOutInput = sc.nextLine();
                try {
                    checkInDate = dateFormat.parse(checkInInput);
                    checkOutDate = dateFormat.parse(checkOutInput);
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please use DD-MM-YYYY with the dashes.");
                    return; // Exit the method upon error
                }
                if (checkInDate.after(checkOutDate)) {
                    System.out.println("Error: Check in date must be before or equal to checkout date0");
                    return;
                }

            } else if (rateTypeOption == 4) {
                rateType = "PROMOTION";
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
                if (checkInDate.after(checkOutDate)) {
                    System.out.println("Error: Check in date must be before or equal to checkout date0");
                    return;
                }
            } else {
                System.out.println("Invalid rate type selected. Please select 1-4.");
                return; // Exit the method if invalid option is chosen
            }

            // Create RoomRateEntity
            RoomRateEntity r = new RoomRateEntity(name, rateAmount, rateType, checkInDate, checkOutDate);
            Long roomRateId = roomRateSessionBeanRemote.createNewRoomRate(r, roomTypeId);

            System.out.println("New room rate with id, " + roomRateId + " created successfully!");

        } catch (Exception e) {
            System.out.println("Error creating room rate: " + e.getMessage());
        }
    }

    private RoomRateEntity viewRoomRateDetails(Long roomRateId) {

        try {
            RoomRateEntity roomRate = roomRateSessionBeanRemote.retrieveRoomRateById(roomRateId);
            System.out.println("Room rate Details: " + roomRate);
            return roomRate;
        } catch (Exception e) {
            System.out.println("Error retrieving room rate details: " + e.getMessage());
        }
        return null; //dummy return value
    }

    private void updateRoomRate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Scanner sc = new Scanner(System.in);
        viewAllRoomRates();
        System.out.print("Enter room rate ID: ");
        Long roomRateId = sc.nextLong();
        sc.nextLine();
        RoomRateEntity roomRate = viewRoomRateDetails(roomRateId);
        if(roomRate == null) {
            return;
        }

        System.out.print("Enter new rate amount: ");
        BigDecimal rateAmount = sc.nextBigDecimal();
        sc.nextLine();
        String status = "";
        System.out.println("Select status");
        System.out.println("1: Available");
        System.out.println("2: Disabled");
        System.out.print("> ");
        int response = sc.nextInt();

        if (response == 1) {
            status = "AVAILABLE";
        } else if (response == 2) {
            status = "DISABLED";
        } else {
            System.out.println("Invalid option! Returning to main menu!");
        }
        Date checkInDate = null;
        Date checkOutDate = null;

        System.out.println("Select new rate type: ");
        System.out.println("1: STANDARD");
        System.out.println("2: NORMAL");
        System.out.println("3: PEAK");
        System.out.println("4: PROMOTION");
        System.out.print("> ");
        int rateTypeOption = sc.nextInt();
        sc.nextLine();
        String rateType;

        switch (rateTypeOption) {
            case 1:
                rateType = "PUBLISHED";
                break;
            case 2:
                rateType = "NORMAL";
                break;
            case 3:
                rateType = "PEAK";
                // Prompt for dates if applicable
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
                if (checkInDate.after(checkOutDate)) {
                    System.out.println("Error: Check in date must be before or equal to checkout date0");
                    return;
                }
                break;
            case 4:
                rateType = "PROMOTION";
                // Prompt for dates if applicable
                System.out.print("Enter check-in date (DD-MM-YYYY): ");
                checkInInput = sc.nextLine();
                System.out.print("Enter check-out date (DD-MM-YYYY): ");
                checkOutInput = sc.nextLine();
                try {
                    checkInDate = dateFormat.parse(checkInInput);
                    checkOutDate = dateFormat.parse(checkOutInput);
                } catch (ParseException e) {
                    System.out.println("Invalid date format. Please use DD-MM-YYYY.");
                    return; // Exit the method upon error
                }
                if (checkInDate.after(checkOutDate)) {
                    System.out.println("Error: Check in date must be before or equal to checkout date0");
                    return;
                }
                break;
            default:
                System.out.println("Invalid rate type selected. Please select 1-4.");
                return; // Exit the method if invalid option is chosen
        }
        roomRateSessionBeanRemote.updateRoomRate(roomRateId, rateAmount, rateType, checkInDate, checkOutDate, status);
        System.out.println("Room rate with id " + roomRateId + " updated successfully!");

    }

    private void deleteRoomRate() {
        Scanner sc = new Scanner(System.in);
        viewAllRoomRates();
        System.out.print("\nEnter room rate ID to delete: ");
        Long roomRateId = sc.nextLong();
        sc.nextLine();
        RoomRateEntity roomRate = viewRoomRateDetails(roomRateId);
        if(roomRate == null) {
            return;
        }
        System.out.print("To confirm deletion press y: ");
        String response = sc.nextLine();
        if (response.equals("y")) {
            try {
                roomRateSessionBeanRemote.deleteRoomRate(roomRateId);
                System.out.println("Room rate deleted successfully!");
            } catch (Exception e) {
                System.out.println("Error deleting room rate: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled");
            return;
        }
    }

    private void viewAllRoomRates() {
        List<RoomRateEntity> roomRates = roomRateSessionBeanRemote.retrieveAllRoomRates();
        System.out.println("\nList of All Room Rates:\n");
        int count = 0;
        for (RoomRateEntity roomRate : roomRates) {
            count++;
            System.out.println(count + ". " + roomRate + "\n");
        }
    }
}
