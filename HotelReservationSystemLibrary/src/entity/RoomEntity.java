/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class RoomEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(nullable = false)
    private String roomNumber; // e.g., "2015" for room on floor 20, 15th room

    @ManyToOne
    @JoinColumn(name = "roomTypeId")
    private RoomTypeEntity roomType; // Reference to the room's type

    @Column(nullable = false)
    private String roomStatus; // e.g., "available", "not available"

    public RoomEntity() {
    }

    public RoomEntity(String roomNumber, String roomStatus) {
        this.roomNumber = roomNumber;
        this.roomStatus = roomStatus.toUpperCase();
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatusAvailable() {
        this.roomStatus = "AVAILABLE";
    }

    public void setRoomStatusNotAvailable() {
        this.roomStatus = "NOTAVAILABLE";
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof RoomEntity)) {
            return false;
        }
        RoomEntity other = (RoomEntity) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Room ID: " + roomId + "\n"
                + "Room Number: " + roomNumber + "\n"
                + "Room Type: " + roomType.getRoomTypeName() + "\n"
                + "Room Status: " + roomStatus + "\n";
    }

}
