/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.RecordNotFoundException;

@Local
public interface RoomTypeSessionBeanLocal {

    public Long createNewRoomType(RoomTypeEntity roomType, Long nextHighest) throws RecordNotFoundException;

    public RoomTypeEntity retrieveRoomType(String roomTypeName) throws RecordNotFoundException;

    public List<RoomTypeEntity> viewAllRoomTypes() throws RecordNotFoundException;

    public void updateRoomTypeName(Long roomTypeId, String newName);

    public void updateRoomTypeDescription(Long roomTypeId, String newDescription);

    public void updateRoomTypeBeds(Long roomTypeId, String newBed);

    public void updateRoomTypeCapacity(Long roomTypeId, String newCapacity);

    public void updateRoomTypeAmenities(Long roomTypeId, String newAmenities);

    public void updateRoomTypeSize(Long roomTypeId, String newSize);

    public RoomTypeEntity retrieveRoomTypeEntityById(Long roomTypeId) throws RecordNotFoundException;

    public String deleteRoomType(Long roomTypeId) throws Exception;

    public void updateRoomTypeNextHigherRoomType(Long roomTypeId, Long nextHigherRoomTypeId) throws RecordNotFoundException;

    public void setRoomTypeStatus(Long roomTypeId, String status);
}
