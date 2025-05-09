/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import java.util.Scanner;
import util.enumeration.AccessRights;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;

/**
 *
 * @author zchoo
 */
public class MainApp {

    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote ratesSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private EmployeeEntity employee;

    public MainApp(RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote ratesSessionBeanRemote, GuestSessionBeanRemote guestSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote,
            EmployeeSessionBeanRemote employeeSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote) {
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.ratesSessionBeanRemote = ratesSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {

            response = 0;

            while (true) {
                System.out.println("*** Welcome to Hotel Reservation System ***\n");
                System.out.println("1: Login");
                System.out.println("2: Exit\n");
                System.out.print("> ");

                response = scanner.nextInt();
                scanner.nextLine();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        menuMain();

                    } catch (InvalidLoginCredentialException ex) { //chaneg to login exception
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 2) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Hotel Reservation System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() >= 7 && password.length() >= 7) {
            this.employee = employeeSessionBeanRemote.employeeLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void menuMain() {
        AccessRights accessright = this.employee.getAccessRightsEnum();
        if (accessright == AccessRights.SYSTEMADMIN) {
            SystemAdministrationModule module = new SystemAdministrationModule(employeeSessionBeanRemote, partnerSessionBeanRemote, this.employee);
            module.runModule();
        } else if (accessright == AccessRights.GUESTRELATIONOFFICER) {
            FrontOfficeModule module = new FrontOfficeModule(guestSessionBeanRemote, reservationSessionBeanRemote, roomSessionBeanRemote, roomTypeSessionBeanRemote, ratesSessionBeanRemote, this.employee);
            module.runModule();
        } else if (accessright == AccessRights.OPERATIONMANAGER || accessright == AccessRights.SALESMANAGER) {
            HotelOperationModule module = new HotelOperationModule(roomSessionBeanRemote, ratesSessionBeanRemote, roomTypeSessionBeanRemote, employee);
            module.runModule();
        }
    }

}
