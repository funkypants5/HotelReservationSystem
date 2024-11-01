/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemclient;

import ejb.session.stateless.RatesSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;

/**
 *
 * @author zchoo
 */
public class HotelOperationModule {
   private RoomSessionBeanRemote roomSessionBeanRemote;
    private RatesSessionBeanRemote ratesSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;

    public HotelOperationModule(RoomSessionBeanRemote roomSessionBeanRemote, RatesSessionBeanRemote ratesSessionBeanRemote, RoomTypeSessionBeanRemote roomTypeSessionBeanRemote) {
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.ratesSessionBeanRemote = ratesSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
    }


    void runModule() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
