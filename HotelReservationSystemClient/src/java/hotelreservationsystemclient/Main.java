/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemclient;

import ejb.session.stateless.BookingSessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RatesSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;

public class Main {

    @EJB
    private static PartnerSessionBeanRemote partnerSessionBean;

    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBean;

    @EJB
    private static RoomSessionBeanRemote roomSessionBean;

    @EJB
    private static RatesSessionBeanRemote ratesSessionBean;

    @EJB
    private static GuestSessionBeanRemote guestSessionBean;

    @EJB
    private static BookingSessionBeanRemote bookingSessionBean;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBean;

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(roomSessionBean,ratesSessionBean, guestSessionBean, bookingSessionBean, employeeSessionBean, roomTypeSessionBean, partnerSessionBean);
        mainApp.runApp();
    }
}
