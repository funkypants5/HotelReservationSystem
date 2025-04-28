/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author admin
 */
@Entity
public class RoomRateEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRateId;
    
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal rate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    private Date validFrom;

    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    private Date validTo;

    private String rateType;

    @ManyToOne
    @JoinColumn(name = "roomTypeId")
    private RoomTypeEntity roomType;
    
    private String status;

    public RoomRateEntity() {
    }

    public RoomRateEntity(String name, BigDecimal rate, String rateType, Date validFrom, Date validTo) {
        this.name = name;
        this.rate = rate;
        this.rateType = rateType.toUpperCase();
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.status = "AVAILABLE";
    }

    public String getRateType() {
        return rateType; 
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public Long getRoomRateId() {
        return roomRateId;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public void setRoomRateId(Long roomRateId) {
        this.roomRateId = roomRateId;
    }

    public void setStatusAvailable() {
        this.status = "AVAILABLE";
    }

    public void setStatusDisabled() {
        this.status = "DISABLED";
    }
    
    public String getDisabledStatus() {
        return this.status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRateId != null ? roomRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomRateId fields are not set
        if (!(object instanceof RoomRateEntity)) {
            return false;
        }
        RoomRateEntity other = (RoomRateEntity) object;
        if ((this.roomRateId == null && other.roomRateId != null) || (this.roomRateId != null && !this.roomRateId.equals(other.roomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if(rateType.equals("NORMAL") || rateType.equals("PUBLISHED")){
            return "Roomrate ID: " + roomRateId + "\n"
                    + "Rate Type: " + rateType +"\n"
                    + "RoomType: " + roomType.getRoomTypeName() + "\n"
                    + "Room rate: " + rate + "\n"
                    + "Room rate status: " + status;
        } else {
            return "Roomrate ID: " + roomRateId + "\n"
                    + "Rate Type: " + rateType +"\n"
                    + "RoomType: " + roomType.getRoomTypeName() + "\n"
                    + "Valid from: " + validFrom + "\n"
                    + "Valid to: " + validTo + "\n"
                    + "Room rate: " + rate + "\n"
                    + "Room rate status: " + status;
        }
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomRateId = roomRateId;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    
    public BigDecimal getRate() {
        return rate;
    }

}
