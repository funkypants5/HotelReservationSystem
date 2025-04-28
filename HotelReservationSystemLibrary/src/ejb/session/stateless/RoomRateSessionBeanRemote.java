/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomAllocationReportEntity;
import entity.RoomEntity;
import entity.RoomRateEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RecordNotFoundException;

/**
 *
 * @author zchoo
 */
@Remote
public interface RoomRateSessionBeanRemote {


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
