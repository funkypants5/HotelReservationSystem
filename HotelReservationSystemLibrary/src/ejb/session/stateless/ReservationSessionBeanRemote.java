/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.ReservationEntity;
import entity.ReserveRoomEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author zchoo
 */
@Remote
public interface ReservationSessionBeanRemote {

    public Long createNewReservation(ReservationEntity reservationEntity, Long guestId, Long roomTypeId);

    public void createReserveRoom(Long reservationId, Date checkInDate, Date checkOutDate);

    public List<ReservationEntity> retrieveReservations(String guestEmail);

    public void setCheckIn(Long reservationId);

    public List<ReserveRoomEntity> retrieveReserveRooms(Long reservationId);

    public void setCheckOut(Long reservationId);

    public void manualAllocateRooms(Date date);

    public List<ReservationEntity> viewAllReservations(String guestEmail);

    public ReservationEntity retrieveReservationsById(Long reservationId);

    public List<ReservationEntity> retrieveReservationsByCheckInDate(Date date);
    
    public Long createNewReservationForPartner(int numberOfRooms, Date checkInDate, Date checkOutDate, Long partnerId, Long roomTypeId);
    
    public List<ReservationEntity> viewAllPartnerReservations(String email);

}
