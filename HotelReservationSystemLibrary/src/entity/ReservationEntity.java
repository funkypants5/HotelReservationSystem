/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author zchoo
 */
@Entity
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    
    @Column(nullable = false)
    private int numOfRooms;
    
    @ManyToOne
    @JoinColumn(name = "roomTypeId", nullable = false)
    private RoomTypeEntity roomType;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date checkIn;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date checkOut;
    
    @ManyToOne
    @JoinColumn(name = "guestId")
    private GuestEntity guest;
    
    @ManyToOne
    @JoinColumn(name = "partnerId")
    private PartnerEntity partner;
    
    @OneToMany(mappedBy = "reservation")
    private List<ReserveRoomEntity> reserveRoom;
    
    private String reservationStatus;
    
    private BigDecimal price;

    public ReservationEntity() {
    }

    public ReservationEntity(int numOfRooms, Date checkIn, Date checkOut) {
        this.numOfRooms = numOfRooms;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.reservationStatus= "NOTASSIGNED";
    }

    public PartnerEntity getPartner() {
        return partner;
    }

    public void setPartner(PartnerEntity partner) {
        this.partner = partner;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatusCheckIn() {
        this.reservationStatus = "Check-in";
    }
    
    public void setReservationStatusCheckOut() {
        this.reservationStatus = "Check-out";
    }
    
    public void setReservationStatusNotAssigned() {
        this.reservationStatus = "NOTASSIGNED";
    }
    
    public void setReservationStatusAssign() {
        this.reservationStatus = "ASSIGNED";
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<ReserveRoomEntity> getReserveRoom() {
        return reserveRoom;
    }

    public void setReserveRoom(List<ReserveRoomEntity> reserveRoom) {
        this.reserveRoom = reserveRoom;
    }

    public int getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public GuestEntity getGuest() {
        return guest;
    }

    public void setGuest(GuestEntity guest) {
        this.guest = guest;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof ReservationEntity)) {
            return false;
        }
        ReservationEntity other = (ReservationEntity) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BookingEntity[ id=" + reservationId + " ]";
    }
    
}
