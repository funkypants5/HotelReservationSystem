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
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

/**
 *
 * @author zchoo
 */
@Entity
public class RoomTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;

    @Column(nullable = false, unique = true)
    @Size(max = 50)
    private String roomTypeName;

    @Column(nullable = false)
    @Size(max = 255)
    private String description;

    @Column(nullable = false)
    @Size(max = 50)
    private String roomSize; // Size in square meters

    @Column(nullable = false)
    @Size(max = 50)
    private String bed; // e.g., "2 King", "3 Queen"

    @Column(nullable = false)
    @Size(max = 10)
    private String capacity; // Maximum number of occupants

    @Column(nullable = false)
    @Size(max = 50)
    private String amenities; // Comma-separated list of amenities
    
    private String status;

    @OneToOne
    @JoinColumn(name = "nextHigherRoomTypeId", nullable = true)
    private RoomTypeEntity nextHigherRoomType;

    public RoomTypeEntity() {
    }

    public RoomTypeEntity(String name, String description, String size, String bed, String capacity, String amenities) {
        this.roomTypeName = name;
        this.description = description;
        this.roomSize = size;
        this.bed = bed;
        this.capacity = capacity;
        this.amenities = amenities;
        this.status = "AVAILABLE";
    }

    public String getStatus() {
        return status;
    }

    public void setStatusAvailable() {
        this.status = "AVAILABLE";
    }
    
    public void setStatusDisabled() {
        this.status = "DISABLED";
    }

    public RoomTypeEntity getNextHigherRoomType() {
        return nextHigherRoomType;
    }

    public void setNextHigherRoomType(RoomTypeEntity nextHigherRoomType) {
        this.nextHigherRoomType = nextHigherRoomType;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are not set
        if (!(object instanceof RoomTypeEntity)) {
            return false;
        }
        RoomTypeEntity other = (RoomTypeEntity) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Room Type ID: " + roomTypeId + "\n"
                + "Room Type Name: " + roomTypeName + "\n"
                + "Description: " + description + "\n"
                + "Room Size: " + roomSize + "\n"
                + "Bed Configuration: " + bed + "\n"
                + "Capacity: " + capacity + "\n"
                + "Amenities: " + amenities + "\n"
                + "Next Higher Room Type: " + (nextHigherRoomType != null ? nextHigherRoomType.getRoomTypeName() : "None") + "\n"
                + "Room Type Status: " + status + "\n";
    }

}
