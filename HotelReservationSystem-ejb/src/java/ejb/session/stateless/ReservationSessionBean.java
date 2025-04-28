/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import ejb.session.singleton.RoomAllocationTimerSessionBean;
import entity.GuestEntity;
import entity.PartnerEntity;
import entity.ReservationEntity;
import entity.ReserveRoomEntity;
import entity.RoomAllocationReportEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private RoomAllocationTimerSessionBean roomAllocationTimerSessionBean;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long createNewReservation(ReservationEntity reservation, Long guestId, Long roomTypeId) {
    
        GuestEntity guest = em.find(GuestEntity.class, guestId);
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        reservation.setRoomType(roomType);
        reservation.setGuest(guest);
        em.persist(reservation);
        em.flush();
        return reservation.getReservationId();
    }

    @Override
    public void createReserveRoom(Long reservationId, Date checkInDate, Date checkOutDate) {
        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);
        String status = reservation.getReservationStatus();

        if (status.equals("NOTASSIGNED")) {
            int roomsToAllocate = reservation.getNumOfRooms();
            RoomTypeEntity currentRoomType = reservation.getRoomType();
            String requestedRoomTypeName = currentRoomType.getRoomTypeName();

            while (roomsToAllocate > 0 && currentRoomType != null) {
                String currentRoomTypeName = currentRoomType.getRoomTypeName();
                List<RoomEntity> availableRooms = roomSessionBean.roomsAvailable(currentRoomType.getRoomTypeId(), checkInDate, checkOutDate);
                int roomsToAssign = Math.min(roomsToAllocate, availableRooms.size()); // Number of rooms that can be allocated in current type

                for (int i = 0; i < roomsToAssign; i++) {
                    ReserveRoomEntity reserveRoom = new ReserveRoomEntity();
                    reserveRoom.setRoom(availableRooms.get(i));
                    reserveRoom.setReservation(reservation);
                    em.persist(reserveRoom);
                    reservation.getReserveRoom().add(reserveRoom);
                    if (!requestedRoomTypeName.equals(currentRoomTypeName)) {
                        RoomAllocationReportEntity report = new RoomAllocationReportEntity();
                        report.setExceptionType("Upgrade Available");
                        report.setAllocatedRoomType(currentRoomTypeName);
                        report.setExceptionDate(reservation.getCheckIn());
                        report.setRequestedRoomType(requestedRoomTypeName);
                        report.setReservation(reservation);
                        em.persist(report);
                    }
                }

                roomsToAllocate -= roomsToAssign;

                if (roomsToAllocate > 0) {
                    currentRoomType = currentRoomType.getNextHigherRoomType();
                    if (currentRoomType == null) {
                        RoomAllocationReportEntity report = new RoomAllocationReportEntity();
                        report.setExceptionType("Upgrade Unavailable");
                        report.setExceptionDate(reservation.getCheckIn());
                        report.setRequestedRoomType(requestedRoomTypeName);
                        report.setReservation(reservation);
                        em.persist(report);
                    }
                }
            }

            if (roomsToAllocate == 0 || currentRoomType == null) {
                reservation.setReservationStatusAssign(); // Set status to assigned if all rooms were successfully allocated
            }
        }
    }

    @Override
    public List<ReservationEntity> retrieveReservations(String guestEmail) {
        Query query = em.createQuery("SELECT g FROM GuestEntity g WHERE g.email = :guestEmail");
        query.setParameter("guestEmail", guestEmail);
        List<GuestEntity> guests = query.getResultList();

        List<ReservationEntity> reservationsForToday = new ArrayList<>();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Date startOfToday = today.getTime();

        today.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
        Date startOfTomorrow = today.getTime();

        for (GuestEntity guest : guests) {
            Long guestId = guest.getGuestId();

            Query newQuery = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.guest.guestId = :guestId "
                    + "AND r.checkIn >= :startOfToday AND r.checkIn < :startOfTomorrow");
            newQuery.setParameter("guestId", guestId);
            newQuery.setParameter("startOfToday", startOfToday);
            newQuery.setParameter("startOfTomorrow", startOfTomorrow);

            // Retrieve all reservations for this guest on today's date
            List<ReservationEntity> reservations = newQuery.getResultList();
            reservationsForToday.addAll(reservations); // Add these reservations to the result list
        }

        return reservationsForToday;
    }

    @Override
    public void setCheckIn(Long reservationId) {
        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);
        reservation.setReservationStatusCheckIn();
    }

    @Override
    public List<ReserveRoomEntity> retrieveReserveRooms(Long reservationId) {
        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);

        List<ReserveRoomEntity> reserveRooms = reservation.getReserveRoom();
        reserveRooms.size();
        return reserveRooms;
    }

    @Override
    public void setCheckOut(Long reservationId) {
        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);
        reservation.setReservationStatusCheckOut();
    }

    public List<ReservationEntity> retrieveReservationsByCheckInDate(Date date) {
        String jpql = "SELECT r FROM ReservationEntity r WHERE r.checkIn = :checkInDate";
        Query query = em.createQuery(jpql);
        query.setParameter("checkInDate", date);
        return query.getResultList();
    }

    @Override
    public void manualAllocateRooms(Date date) {
        roomAllocationTimerSessionBean.manualAllocateRooms(date);
    }

    @Override
    public List<ReservationEntity> viewAllReservations(String guestEmail) {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.guest.email = :email");
        query.setParameter("email", guestEmail);
        return query.getResultList();
    }

    @Override
    public ReservationEntity retrieveReservationsById(Long reservationId) {
        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);
        return reservation;
    }

    @Override
    public Long createNewReservationForPartner(int numberOfRooms, Date checkInDate, Date checkOutDate, Long partnerId, Long roomTypeId){
        PartnerEntity partner = em.find(PartnerEntity.class, partnerId);
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);
        ReservationEntity reservation = new ReservationEntity(numberOfRooms, checkInDate, checkOutDate);
        reservation.setRoomType(roomType);
        reservation.setPartner(partner);
        em.persist(reservation);
        em.flush();
        return reservation.getReservationId();
    }

    @Override
    public List<ReservationEntity> viewAllPartnerReservations(String email) {
        Query query = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.partner.email = :email");
        query.setParameter("email", email);
        return query.getResultList();
    }

}
