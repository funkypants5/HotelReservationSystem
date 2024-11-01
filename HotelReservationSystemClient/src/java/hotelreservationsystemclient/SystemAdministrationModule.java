/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.enumeration.AccessRights;
import util.exception.InvalidInputException;

/**
 *
 * @author zchoo
 */
public class SystemAdministrationModule {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private EmployeeEntity employee;

    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, EmployeeEntity employee) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.employee = employee;
    }

    public void runModule() {
        Scanner sc = new Scanner(System.in);
        int response;
        while (true) {
            System.out.println("Welcome " + this.employee.getFirstName() + " to system administration \n");
            System.out.println("1: Create new employee account");
            System.out.println("2: View all amployees");
            System.out.println("3: Create new partner account");
            System.out.println("4: View all partners");
            System.out.println("5: Logout");
            System.out.print("> ");
            response = sc.nextInt();

            if (response == 1) {
                createNewUser();
            } else if (response == 2) {
                viewAllUser();
            } else if (response == 3) {
                createNewPartner();
            } else if (response == 4) {
                viewAllPartners();
            } else if (response == 5) {
                break;
            } else {
                System.out.println("Invalid input please key in a valid input");
                try {
                    Thread.sleep(2000); // Delay for 2000 milliseconds (2 seconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createNewUser() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter first name: ");
            String firstName = sc.next().trim();
            validateName(firstName);

            System.out.print("Enter last name: ");
            String lastName = sc.next().trim();
            validateName(lastName);

            System.out.println("Enter access rights: ");
            System.out.println("1: SYSTEMADMIN");
            System.out.println("2: OPERATIONSMANAGER");
            System.out.println("3: SALESMANAGER");
            System.out.println("4: GUESTRELATIONOFFICER");
            System.out.print("> ");
            int accessRightsInput = sc.nextInt();
            AccessRights accessRights = null;
            switch (accessRightsInput) {
                case 1:
                    accessRights = AccessRights.SYSTEMADMIN;
                    System.out.println("Access rights set to SYSTEMADMIN.");
                    break;
                case 2:
                    accessRights = AccessRights.OPERATIONMANAGER;
                    System.out.println("Access rights set to OPERATIONSMANAGER.");
                    break;
                case 3:
                    accessRights = AccessRights.SALESMANAGER;
                    System.out.println("Access rights set to SALESMANAGER.");
                    break;
                case 4:
                    accessRights = AccessRights.GUESTRELATIONOFFICER;
                    System.out.println("Access rights set to GUESTRELATIONOFFICER.");
                    break;
                default:
                    System.out.println("Invalid input: Please enter a number between 1 and 4.");

            }

            System.out.print("Enter username (min 7 characters): ");
            String username = sc.next().trim();
            validateUsername(username);

            System.out.print("Enter password (min 7 characters): ");
            String password = sc.next().trim();
            validatePassword(password);

            EmployeeEntity employee = new EmployeeEntity(firstName, lastName, accessRights, username, password);
            employeeSessionBeanRemote.createEmployee(employee);
            System.out.println("Employee created successfully!");

        } catch (InvalidInputException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void validateName(String name) throws InvalidInputException {
        if (name.isEmpty() || name.length() > 32) {
            throw new InvalidInputException("Name must be between 1 and 32 characters.");
        }
    }

    private static void validateUsername(String username) throws InvalidInputException {
        if (username.length() < 7) {
            throw new InvalidInputException("Username must be at least 7 characters long.");
        }
    }

    private static void validatePassword(String password) throws InvalidInputException {
        if (password.length() < 7) {
            throw new InvalidInputException("Password must be at least 7 characters long.");
        }
    }

    private void viewAllUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void createNewPartner() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void viewAllPartners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
