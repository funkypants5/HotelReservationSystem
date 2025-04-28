/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import entity.ReservationEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Startup;

@Singleton
@LocalBean
@Startup
public class RoomAllocationTimerSessionBean {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @Schedule(hour = "2", minute = "0", second = "0", persistent = false)
    public void allocateRoomsAuto() {
        // Set today's date for room allocation
        Date today = new Date();
        allocateRoomsForDate(today);
    }

    public void allocateRoomsForDate(Date date) {
        // Retrieve all reservations with the check-in date matching the provided date
        List<ReservationEntity> reservations = reservationSessionBean.retrieveReservationsByCheckInDate(date);

        for (ReservationEntity reservation : reservations) {
            reservationSessionBean.createReserveRoom(reservation.getReservationId(), reservation.getCheckIn(), reservation.getCheckOut());
        }
    }

    public void manualAllocateRooms(Date date) {
            allocateRoomsForDate(date);
    }
}
