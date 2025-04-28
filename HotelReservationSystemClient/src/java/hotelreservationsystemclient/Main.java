/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemclient;

import javax.ejb.EJB;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;

public class Main {

    @EJB
    private static PartnerSessionBeanRemote partnerSessionBean;

    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBean;

    @EJB
    private static RoomSessionBeanRemote roomSessionBean;

    @EJB
    private static RoomRateSessionBeanRemote ratesSessionBean;

    @EJB
    private static GuestSessionBeanRemote guestSessionBean;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBean;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBean;
    

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(roomSessionBean,ratesSessionBean, guestSessionBean, reservationSessionBean, employeeSessionBean, roomTypeSessionBean, partnerSessionBean);
        mainApp.runApp();
    }
}
