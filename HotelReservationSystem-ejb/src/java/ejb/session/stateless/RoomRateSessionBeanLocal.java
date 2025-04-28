/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRateEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.RecordNotFoundException;

/**
 *
 * @author zchoo
 */
@Local
public interface RoomRateSessionBeanLocal {

    public Long createNewRoomRate(RoomRateEntity r, Long roomTypeId);

    public List<RoomRateEntity> retrieveAllRoomRates();

    public RoomRateEntity retrieveRoomRateById(Long roomRateId) throws RecordNotFoundException;

    public void updateRoomRate(Long roomRateId, BigDecimal rateAmount, String rateType, Date checkInDate, Date checkOutDate, String status);

    public void deleteRoomRate(Long roomRateId) throws Exception;

    public void calculateTotalReservationFee(Long reservationId);

    public void calculateTotalWalkInReservationFee(Long reservationId);

    public BigDecimal calculateSearchRoomFee(Date checkInDate, Date checkOutDate, Long roomTypeId);
    
    public BigDecimal calculateWalkInSearchFee(Date checkInDate, Date checkOutDate, Long roomTypeId);
}
