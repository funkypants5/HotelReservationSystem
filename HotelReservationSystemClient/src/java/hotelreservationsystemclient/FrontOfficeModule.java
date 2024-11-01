/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemclient;

import ejb.session.stateless.BookingSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;

/**
 *
 * @author zchoo
 */
public class FrontOfficeModule {

    private GuestSessionBeanRemote guestSessionBeanRemote;
    private BookingSessionBeanRemote bookingSessionBeanRemote;

    public FrontOfficeModule(GuestSessionBeanRemote guestSessionBeanRemote, BookingSessionBeanRemote bookingSessionBeanRemote) {
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.bookingSessionBeanRemote = bookingSessionBeanRemote;
    }
    
    void runModule() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
