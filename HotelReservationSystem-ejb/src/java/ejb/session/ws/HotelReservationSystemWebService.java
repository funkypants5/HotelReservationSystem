/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.PartnerEntity;
import entity.ReservationEntity;
import entity.ReserveRoomEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidLoginCredentialException;
import util.exception.RecordNotFoundException;

@WebService(serviceName = "HotelReservationSystemWebService")
@Stateless()
public class HotelReservationSystemWebService {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBean;

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private ReservationSessionBeanLocal reservationSessionBean;

    @WebMethod(operationName = "loginPartner")
    public PartnerEntity loginPartner(String email, String password) throws InvalidLoginCredentialException {
        try {
            PartnerEntity partner = partnerSessionBean.loginPartner(email, password);
            em.detach(partner);

            for (ReservationEntity reservation : partner.getReservations()) {
                em.detach(reservation);
                reservation.setPartner(null);
            }
            return partner;
        } catch (InvalidLoginCredentialException ex) {
            throw ex;
        }

    }

    @WebMethod(operationName = "viewAllRoomTypes")
    public List<RoomTypeEntity> viewAllRoomTypes() throws RecordNotFoundException {
        try {
            List<RoomTypeEntity> rts = roomTypeSessionBean.viewAllRoomTypes();
            for (RoomTypeEntity rt : rts) {
                em.detach(rt);
                rt.setNextHigherRoomType(null);
            }
            return rts;
        } catch (RecordNotFoundException ex) {
            throw ex;
        }
    }

    @WebMethod(operationName = "roomsAvailable")
    public List<RoomEntity> roomsAvailable(Long roomTypeId, Date checkInDate, Date checkOutDate) {
        List<RoomEntity> rooms = roomSessionBean.roomsAvailable(roomTypeId, checkInDate, checkOutDate);
        for (RoomEntity room : rooms) {
            em.detach(room);
        }
        return rooms;
    }

    @WebMethod(operationName = "getAvailableRoomCountForType")
    public int getAvailableRoomCountForType(Long roomTypeId, Date checkInDate, Date checkOutDate) {
        return roomSessionBean.getAvailableRoomCountForType(roomTypeId, checkInDate, checkOutDate);
    }

    @WebMethod(operationName = "createNewReservationForPartner")
    public Long createNewReservationForPartner(int numberOfRooms, Date checkInDate, Date checkOutDate, Long partnerId, Long roomTypeId) {
        return reservationSessionBean.createNewReservationForPartner(numberOfRooms, checkInDate, checkOutDate, partnerId, roomTypeId);
    }

    @WebMethod(operationName = "calculateTotalReservationFee")
    public void calculateTotalReservationFee(Long reservationId) {
        roomRateSessionBean.calculateTotalReservationFee(reservationId);
    }

    @WebMethod(operationName = "createReserveRoom")
    public void createReserveRoom(Long reservationId, Date checkInDate, Date checkOutDate) {
        reservationSessionBean.createReserveRoom(reservationId, checkInDate, checkOutDate);
    }

    @WebMethod(operationName = "viewAllPartnerReservations")
    public List<ReservationEntity> viewAllPartnerReservations(String email) {
        List<ReservationEntity> reservations = reservationSessionBean.viewAllPartnerReservations(email);

        for (ReservationEntity reservation : reservations) {
            em.detach(reservation);

            // Break the cyclic reference here
            PartnerEntity partner = reservation.getPartner();
            if (partner != null) {
                partner.setReservations(null); // Break the cycle
            }

            reservation.setReserveRoom(null);
        }
        return reservations;
    }
    
    @WebMethod(operationName = "calculateSearchRoomFee")
    public BigDecimal calculateSearchRoomFee(Date checkInDate, Date checkOutDate, Long roomTypeId) {
        return roomRateSessionBean.calculateSearchRoomFee(checkInDate, checkOutDate, roomTypeId);
    }

}
