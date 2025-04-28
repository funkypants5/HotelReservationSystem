/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author zchoo
 */
@Entity
public class ReserveRoomEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reserveRoomId;
    
    @ManyToOne
    @JoinColumn(name = "reservationId")
    private ReservationEntity reservation;
    
    @ManyToOne
    @JoinColumn(name = "roomId")
    private RoomEntity room;

    public ReserveRoomEntity() {
    }

    public ReservationEntity getReservation() {
        return reservation;
    }

    public void setReservation(ReservationEntity reservation) {
        this.reservation = reservation;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public Long getReserveRoomId() {
        return reserveRoomId;
    }

    public void setReserveRoomId(Long reserveRoomId) {
        this.reserveRoomId = reserveRoomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reserveRoomId != null ? reserveRoomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reserveRoomId fields are not set
        if (!(object instanceof ReserveRoomEntity)) {
            return false;
        }
        ReserveRoomEntity other = (ReserveRoomEntity) object;
        if ((this.reserveRoomId == null && other.reserveRoomId != null) || (this.reserveRoomId != null && !this.reserveRoomId.equals(other.reserveRoomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReserveRoomEntity[ id=" + reserveRoomId + " ]";
    }
    
}
