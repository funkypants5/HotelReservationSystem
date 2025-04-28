/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author zchoo
 */
@Entity
public class RoomAllocationReportEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomAllocationReportId;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date exceptionDate;

    @Column(nullable = false)
    private String exceptionType; // "Upgrade Available" or "No Upgrade Available"

    @ManyToOne(optional = false)
    private ReservationEntity reservation;

    @Column(nullable = false)
    private String requestedRoomType;

    private String allocatedRoomType;

    public RoomAllocationReportEntity() {
    }

    public Date getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(Date exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public ReservationEntity getReservation() {
        return reservation;
    }

    public void setReservation(ReservationEntity reservation) {
        this.reservation = reservation;
    }

    public String getRequestedRoomType() {
        return requestedRoomType;
    }

    public void setRequestedRoomType(String requestedRoomType) {
        this.requestedRoomType = requestedRoomType;
    }

    public String getAllocatedRoomType() {
        return allocatedRoomType;
    }

    public void setAllocatedRoomType(String allocatedRoomType) {
        this.allocatedRoomType = allocatedRoomType;
    }

    public Long getRoomAllocationReportId() {
        return roomAllocationReportId;
    }

    public void setRoomAllocationReportId(Long roomAllocationReportId) {
        this.roomAllocationReportId = roomAllocationReportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomAllocationReportId != null ? roomAllocationReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomAllocationReportId fields are not set
        if (!(object instanceof RoomAllocationReportEntity)) {
            return false;
        }
        RoomAllocationReportEntity other = (RoomAllocationReportEntity) object;
        if ((this.roomAllocationReportId == null && other.roomAllocationReportId != null) || (this.roomAllocationReportId != null && !this.roomAllocationReportId.equals(other.roomAllocationReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if(exceptionType.equals("Upgrade Available")) {
            return "Allocation report created for reservation with reservation id: " + reservation.getReservationId() + "\n" +
                    "Upgrade was made from " + requestedRoomType + " to " + allocatedRoomType + "\n"; 
        } else {
            return "Allocation report created for reservation with reservation id: " + reservation.getReservationId() + "\n" +
                    "No room were found for upgrade and allocation\n";
        }
    }
}
