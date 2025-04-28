/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.ReserveRoomEntity;
import entity.RoomAllocationReportEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EntityDisabledException;
import util.exception.EntityInUseException;
import util.exception.RecordNotFoundException;

/**
 *
 * @author zchoo
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long createNewRoom(Long roomTypeId, RoomEntity roomEntity) throws Exception {
        RoomTypeEntity rt = em.find(RoomTypeEntity.class, roomTypeId);
        RoomEntity room = new RoomEntity();
        String rtStatus = rt.getStatus();
        if (rt == null) {
            throw new RecordNotFoundException("Room type Id is invalid, no room type found");
        }

        if (rtStatus.equals("DISABLED")) {
            throw new EntityDisabledException("Room type disabled, unable to create new room for this room type");
        }

        String roomNumber = roomEntity.getRoomNumber();
        Query query = em.createQuery("SELECT r from RoomEntity r WHERE r.roomNumber = :roomNumber");
        query.setParameter("roomNumber", roomNumber);
        try {
            room = (RoomEntity) query.getSingleResult();
            throw new Exception("Room already exist");
        } catch (NoResultException e) {
            roomEntity.setRoomType(rt);
            em.persist(roomEntity);
            em.flush();
            return roomEntity.getRoomId();
        }

    }

    @Override
    public RoomEntity retrieveRoom(String roomNumber) throws RecordNotFoundException {
        try {
            Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomNumber = :roomNumber");
            query.setParameter("roomNumber", roomNumber);

            return (RoomEntity) query.getSingleResult();

        } catch (NoResultException ex) {
            throw new RecordNotFoundException("Room not found");
        }
    }

    @Override
    public void updateRoomNumber(Long roomId, String newRoomNumber) {
        RoomEntity room = em.find(RoomEntity.class, roomId);
        room.setRoomNumber(newRoomNumber);
    }

    @Override
    public void updateRoomType(Long roomId, Long newRoomTypeId) throws RecordNotFoundException {
        RoomEntity room = em.find(RoomEntity.class, roomId);
        RoomTypeEntity rt = em.find(RoomTypeEntity.class, newRoomTypeId);
        if (rt == null) {
            throw new RecordNotFoundException("New room type does not exist");
        }
        room.setRoomType(rt);
    }

    @Override
    public void setNotAvilable(Long roomId) {
        RoomEntity room = em.find(RoomEntity.class, roomId);
        room.setRoomStatusNotAvailable();
    }

    @Override
    public void setAvailable(Long roomId) {
        RoomEntity room = em.find(RoomEntity.class, roomId);
        room.setRoomStatusAvailable();
    }

    @Override
    public List<RoomEntity> viewAllRoom() throws RecordNotFoundException {
        Query query = em.createQuery("SELECT r FROM RoomEntity r");
        if (query == null) {
            throw new RecordNotFoundException("No rooms found ");
        }
        return query.getResultList();
    }

    @Override
    public Long deleteRoom(String roomNumber) throws Exception {
        RoomEntity room = retrieveRoom(roomNumber);
        if (room == null) {
            throw new RecordNotFoundException("Room for deletion is not found!");
        }

        Long roomId = room.getRoomId();

        // Check if there are any reservations associated with this room
        Query query = em.createQuery("SELECT rr FROM ReserveRoomEntity rr WHERE rr.room.roomId = :roomId");
        query.setParameter("roomId", roomId);

        List<ReserveRoomEntity> reserveRooms = query.getResultList();

        if (!reserveRooms.isEmpty()) {
            // If the room is reserved, change its status and throw an exception
            room.setRoomStatusNotAvailable();
            throw new EntityInUseException("Room is reserved, room status changed to not available.");
        } else {
            // If no reservations are found, delete the room
            em.remove(room);
            em.flush(); // Explicitly flush to make sure deletion occurs immediately
            return roomId;
        }
    }

    @Override
    public List<RoomEntity> roomsAvailable(Long roomTypeId, Date checkInDate, Date checkOutDate) {
        //get all the unavailable rooms
        String jpql = "SELECT rr FROM ReserveRoomEntity rr "
                + "JOIN rr.reservation r "
                + "WHERE r.roomType.roomTypeId = :roomTypeId "
                + "AND ((r.checkIn < :checkOutDate AND r.checkOut > :checkInDate)) "
                + "OR r.roomType.status != :disabledStatus";

        Query query = em.createQuery(jpql);
        String disabledStatus = "DISABLED";
        query.setParameter("roomTypeId", roomTypeId);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);
        query.setParameter("disabledStatus", disabledStatus);

        List<ReserveRoomEntity> reserveRooms = query.getResultList();

        if (reserveRooms != null && !reserveRooms.isEmpty()) {
            for (ReserveRoomEntity reserveRoom : reserveRooms) {
                if (reserveRoom != null) {
                    reserveRoom.getRoom().setRoomStatusNotAvailable();
                }
            }
        }
        String roomStatus = "AVAILABLE";
        String newJpql = "SELECT r from RoomEntity r WHERE r.roomStatus = :roomStatus "
                + "AND (r.roomType.roomTypeId = :roomTypeId) "
                + "AND (r.roomType.status != :disabledStatus)";
        Query newQuery = em.createQuery(newJpql);
        newQuery.setParameter("roomStatus", roomStatus);
        newQuery.setParameter("roomTypeId", roomTypeId);
        newQuery.setParameter("disabledStatus", disabledStatus);
        List<RoomEntity> rooms = newQuery.getResultList();

        //reset to available
        if (reserveRooms != null && !reserveRooms.isEmpty()) {
            for (ReserveRoomEntity reserveRoom : reserveRooms) {
                if (reserveRoom != null) {
                    reserveRoom.getRoom().setRoomStatusAvailable();
                }
            }
        }
        return rooms;
    }

    @Override
    public int getAvailableRoomCountForType(Long roomTypeId, Date checkInDate, Date checkOutDate) {
        // Step 1: Query to retrieve the total number of rooms reserved within the specified date range
        RoomTypeEntity rt = em.find(RoomTypeEntity.class, roomTypeId);
        if (rt.getStatus().equals("DISABLED")) {
            return 0;
        }
        String reservationQuery = "SELECT SUM(r.numOfRooms) FROM ReservationEntity r WHERE r.roomType.roomTypeId = :roomTypeId "
                + "AND ((r.checkIn < :checkOutDate) AND (r.checkOut > :checkInDate)) ";// Adjusted condition to check for overlaps
        Query query = em.createQuery(reservationQuery);
        query.setParameter("roomTypeId", roomTypeId);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);

        // Retrieve the number of reserved rooms
        Long reservedRoomCount = (Long) query.getSingleResult();
        reservedRoomCount = (reservedRoomCount == null) ? 0 : reservedRoomCount; // Handle null case

        // Step 2: Fetch total number of rooms of the specified room type
        String totalRoomsQuery = "SELECT COUNT(r) FROM RoomEntity r "
                + "WHERE r.roomType.roomTypeId = :roomTypeId "
                + "AND r.roomStatus = :roomStatus";
        Query totalQuery = em.createQuery(totalRoomsQuery);
        totalQuery.setParameter("roomTypeId", roomTypeId);
        totalQuery.setParameter("roomStatus", "AVAILABLE");
        Long totalRoomCount = (Long) totalQuery.getSingleResult();

        // Step 3: Calculate the available rooms by subtracting reserved rooms from total rooms
        int availableRoomCount = totalRoomCount.intValue() - reservedRoomCount.intValue();

        return Math.max(availableRoomCount, 0); // Ensure no negative values are returned
    }

    @Override
    public List<RoomAllocationReportEntity> retrieveExceptionReport(Date date) {
        // Create calendars to handle the start and end of the day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Start of the day
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfDay = calendar.getTime();

        // End of the day
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endOfDay = calendar.getTime();

        // Update the query to check if exceptionDate falls within the day’s range
        Query query = em.createQuery("SELECT r FROM RoomAllocationReportEntity r WHERE r.exceptionDate >= :startOfDay AND r.exceptionDate <= :endOfDay");
        query.setParameter("startOfDay", startOfDay);
        query.setParameter("endOfDay", endOfDay);

        return query.getResultList();
    }

}
