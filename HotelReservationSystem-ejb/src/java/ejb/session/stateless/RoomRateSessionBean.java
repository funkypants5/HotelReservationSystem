/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RecordNotFoundException;

@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long createNewRoomRate(RoomRateEntity r, Long roomTypeId) {

        RoomTypeEntity rt = em.find(RoomTypeEntity.class, roomTypeId);
        r.setRoomType(rt);
        em.persist(r);
        em.flush();
        return r.getRoomRateId();
    }

    @Override
    public List<RoomRateEntity> retrieveAllRoomRates() {
        Query query = em.createQuery("SELECT rr FROM RoomRateEntity rr");
        return query.getResultList();
    }

    @Override
    public RoomRateEntity retrieveRoomRateById(Long roomRateId) throws RecordNotFoundException {
        RoomRateEntity rt = em.find(RoomRateEntity.class, roomRateId);
        if (rt == null) {
            throw new RecordNotFoundException("Room rate not found!");
        }
        return rt;
    }

    @Override
    public void updateRoomRate(Long roomRateId, BigDecimal rateAmount, String rateType, Date checkInDate, Date checkOutDate, String status) {
        RoomRateEntity rr = em.find(RoomRateEntity.class, roomRateId);
        rr.setRate(rateAmount);
        rr.setRateType(rateType);
        rr.setValidTo(checkInDate);
        rr.setValidFrom(checkOutDate);
        if (status.equals("AVAILABLE")) {
            rr.setStatusAvailable();
        } else {
            rr.setStatusDisabled();
        }
    }

    @Override
    public void deleteRoomRate(Long roomRateId) throws Exception {
        RoomRateEntity rr = em.find(RoomRateEntity.class, roomRateId);
        if (rr == null) {
            throw new RecordNotFoundException("Room rate not found!");
        }
        Query reservationQuery = null;

// Ensure valid dates for applicable rate types
        Date startDate = rr.getValidFrom();
        Date endDate = rr.getValidTo();

// Only check dates if they are required (for "PEAK" or "PROMOTION" rate types)
        if ((rr.getRateType().equals("PEAK") || rr.getRateType().equals("PROMOTION")) && (startDate == null || endDate == null)) {
            throw new Exception("Invalid room rate dates for PEAK or PROMOTION rate types.");
        }

        if (rr.getRateType().equals("PEAK") || rr.getRateType().equals("PROMOTION")) {
            reservationQuery = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.checkIn <= :endDate AND r.checkOut >= :startDate");
            reservationQuery.setParameter("startDate", startDate);
            reservationQuery.setParameter("endDate", endDate);
        } else {
            Long roomTypeId = rr.getRoomType().getRoomTypeId();
            reservationQuery = em.createQuery("SELECT r FROM ReservationEntity r WHERE r.roomType.roomTypeId = :roomTypeId");
            reservationQuery.setParameter("roomTypeId", roomTypeId);
        }

        boolean roomTypeUsed = false;
        try {
            // Fetch reservations
            List<ReservationEntity> reservations = reservationQuery.getResultList();

            // Check if there are any reservations with the same room type
            for (ReservationEntity reservation : reservations) {
                if (reservation.getRoomType().getRoomTypeId().equals(rr.getRoomType().getRoomTypeId())) {
                    roomTypeUsed = true;
                    break;
                }
            }
        } catch (NoResultException ex) {
            em.remove(rr);
            em.flush();
        }

// Disable room rate if it is used; otherwise, delete it
        if (roomTypeUsed) {
            rr.setStatusDisabled();
            throw new Exception("Room rate is used. Room rate set to disabled instead.");
        } else {
            try {
                em.remove(rr);
                em.flush();
            } catch (Exception e) {
                throw new Exception("Error deleting room rate: " + e.getMessage());
            }
        }
    }

//This method is  for the future of calculating rates
    @Override
    public void calculateTotalReservationFee(Long reservationId) {
        BigDecimal totalFee = BigDecimal.ZERO;
        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);
        Date checkInDate = reservation.getCheckIn();
        Date checkOutDate = reservation.getCheckOut();
        Long roomTypeId = reservation.getRoomType().getRoomTypeId();
        int numOfRooms = reservation.getNumOfRooms();

        // Set up calendar to iterate through the reservation nights (excluding check-out day)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);

        while (calendar.getTime().before(checkOutDate)) { // Exclude the check-out date
            Date currentDate = calendar.getTime();
            RoomRateEntity applicableRate = findBestApplicableRate(roomTypeId, currentDate);

            if (applicableRate != null) {
                totalFee = totalFee.add(applicableRate.getRate());
            }

            // Move to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Multiply total fee by the number of rooms booked
        reservation.setPrice(totalFee.multiply(BigDecimal.valueOf(numOfRooms)));
    }

    //this method for the future of calculating rates
    private RoomRateEntity findBestApplicableRate(Long roomTypeId, Date currentDate) {
        // JPQL to get applicable room rates for the specific date
        Query query = em.createQuery("SELECT r FROM RoomRateEntity r WHERE r.roomType.roomTypeId = :roomTypeId "
                + "AND r.status = :status "
                + "AND ((r.validFrom IS NULL AND r.validTo IS NULL) OR "
                + "(r.validFrom <= :currentDate AND r.validTo >= :currentDate))");
        String status = "AVAILABLE";
        query.setParameter("status", status);
        query.setParameter("roomTypeId", roomTypeId);
        query.setParameter("currentDate", currentDate);

        List<RoomRateEntity> applicableRates = query.getResultList();

        // Logic to find best applicable rate based on priority
        RoomRateEntity bestRate = null;
        for (RoomRateEntity rate : applicableRates) {
            if (bestRate == null && rate.getRateType().equals("NORMAL")) {
                bestRate = rate;
            } else if (rate.getRateType().equals("PROMOTION")) {
                bestRate = rate;
                return bestRate;
            } else if (rate.getRateType().equals("PEAK") && !bestRate.getRateType().equals("PROMOTION")) {
                bestRate = rate; // Prioritize peak over default
            }
        }
        return bestRate;
    }

    @Override
    public void calculateTotalWalkInReservationFee(Long reservationId) {
        BigDecimal totalFee = BigDecimal.ZERO;
        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);
        Date checkInDate = reservation.getCheckIn();
        Date checkOutDate = reservation.getCheckOut();
        Long roomTypeId = reservation.getRoomType().getRoomTypeId();
        int numOfRooms = reservation.getNumOfRooms();

        String jpql = "SELECT rr FROM RoomRateEntity rr WHERE rr.roomType.roomTypeId = :roomTypeId AND rr.rateType = :rateType AND rr.status = :status";
        Query query = em.createQuery(jpql);
        String status = "AVAILABLE";
        query.setParameter("status", status);
        query.setParameter("roomTypeId", roomTypeId);
        query.setParameter("rateType", "PUBLISHED");

        RoomRateEntity publishedRate = (RoomRateEntity) query.getSingleResult();
        BigDecimal price = publishedRate.getRate();

        // Iterate through the reservation nights (excluding the check-out date)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);

        while (calendar.getTime().before(checkOutDate)) { // Exclude check-out date
            totalFee = totalFee.add(price);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Calculate the final total by multiplying by the number of rooms
        totalFee = totalFee.multiply(BigDecimal.valueOf(numOfRooms));
        reservation.setPrice(totalFee);
    }
    
    @Override
    public BigDecimal calculateSearchRoomFee(Date checkInDate, Date checkOutDate, Long roomTypeId) {
        BigDecimal totalFee = BigDecimal.ZERO;

        // Set up calendar to iterate through the reservation nights (excluding check-out day)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);

        while (calendar.getTime().before(checkOutDate)) { // Exclude the check-out date
            Date currentDate = calendar.getTime();
            RoomRateEntity applicableRate = findBestApplicableRate(roomTypeId, currentDate);

            if (applicableRate != null) {
                totalFee = totalFee.add(applicableRate.getRate());
            }

            // Move to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Multiply total fee by the number of rooms booked
        return totalFee;
    }
    
    @Override
    public BigDecimal calculateWalkInSearchFee(Date checkInDate, Date checkOutDate, Long roomTypeId) {
        BigDecimal totalFee = BigDecimal.ZERO;

        String jpql = "SELECT rr FROM RoomRateEntity rr WHERE rr.roomType.roomTypeId = :roomTypeId AND rr.rateType = :rateType AND rr.status = :status";
        Query query = em.createQuery(jpql);
        String status = "AVAILABLE";
        query.setParameter("status", status);
        query.setParameter("roomTypeId", roomTypeId);
        query.setParameter("rateType", "PUBLISHED");

        RoomRateEntity publishedRate = (RoomRateEntity) query.getSingleResult();
        BigDecimal price = publishedRate.getRate();

        // Iterate through the reservation nights (excluding the check-out date)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);

        while (calendar.getTime().before(checkOutDate)) { // Exclude check-out date
            totalFee = totalFee.add(price);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Calculate the final total by multiplying by the number of rooms
        return totalFee;
    }

}
