/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import java.util.List;
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
            System.out.println("\nHi " + this.employee.getFirstName() + " welcome to system administration \n");
            System.out.println("1: Create new employee account");
            System.out.println("2: View all employees");
            System.out.println("3: Create new partner account");
            System.out.println("4: View all partners");
            System.out.println("5: Logout");
            System.out.print("> ");
            response = sc.nextInt();

            if (response == 1) {
                createNewEmployee();
            } else if (response == 2) {
                viewAllEmployees();
            } else if (response == 3) {
                createNewPartner();
            } else if (response == 4) {
                viewAllPartners();
            } else if (response == 5) {
                break;
            } else {
                System.out.println("Invalid input please key in a valid input");
            }
        }
    }

    private void createNewEmployee() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter first name: ");
            String firstName = sc.nextLine();
            validateName(firstName);

            System.out.print("Enter last name: ");
            String lastName = sc.nextLine();
            validateName(lastName);

            System.out.println("Enter access rights: ");
            System.out.println("1: SYSTEMADMIN");
            System.out.println("2: OPERATIONSMANAGER");
            System.out.println("3: SALESMANAGER");
            System.out.println("4: GUESTRELATIONOFFICER");
            System.out.print("> ");
            int accessRightsInput = sc.nextInt();
            sc.nextLine();
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
            String username = sc.nextLine();
            validateUsername(username);

            System.out.print("Enter password (min 7 characters): ");
            String password = sc.nextLine();
            validatePassword(password);

            EmployeeEntity employee = new EmployeeEntity(firstName, lastName, accessRights, username, password);
            employeeSessionBeanRemote.createEmployee(employee);
            System.out.println("\nEmployee created successfully! \n");

        } catch (InvalidInputException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: User already exists. Please use another username employee is given extra administarative rights. \n");
        }
    }
    private void viewAllEmployees() {
        List<EmployeeEntity> listOfEmployees = employeeSessionBeanRemote.viewAllEmployees();
        System.out.println("\nList of all employees \n");
        int count = 0;
        for(EmployeeEntity employee : listOfEmployees) {
            count++;
            System.out.println(count + ". " + employee);
        }
    }

    private void createNewPartner() {
        try { 
            Scanner sc = new Scanner(System.in);
            System.out.print("Please key in partner's email: ");
            String email = sc.nextLine().trim();
            System.out.print("Please key in partner's password: ");
            String password = sc.nextLine().trim();
            validatePassword(password);
            System.out.print("Please key in partner's company name: ");
            String name = sc.nextLine();
            Long partnerId = partnerSessionBeanRemote.createNewPartner(new PartnerEntity(email, password, name));
            System.out.println("\nNew partner created with id " + partnerId + "\n");
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: Partner/Email address already exists");
        }
         
         
    }

    private void viewAllPartners() {
        List<PartnerEntity> listOfPartners = partnerSessionBeanRemote.viewAllPartners();
        System.out.println("\nList of all partners \n");
        int count = 0;
        for(PartnerEntity partner : listOfPartners) {
            count++;
            System.out.println(count + ". " + partner);
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
}
