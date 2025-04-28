/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import entity.EmployeeEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.RoomEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import util.enumeration.AccessRights;
import util.exception.RecordNotFoundException;

/**
 *
 * @author zchoo
 */
@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {

    @EJB
    private RoomSessionBeanLocal roomSessionBean;

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBean;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBean;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        if (em.find(EmployeeEntity.class, 1l) == null) {
            employeeSessionBean.createEmployee(new EmployeeEntity("sysmanager", "manager", AccessRights.SYSTEMADMIN, "sysadmin", "password"));
            employeeSessionBean.createEmployee(new EmployeeEntity("opmanager", "manager", AccessRights.OPERATIONMANAGER, "opmanager", "password"));
            employeeSessionBean.createEmployee(new EmployeeEntity("salesmanager", "manager", AccessRights.SALESMANAGER, "salesmanager", "password"));
            employeeSessionBean.createEmployee(new EmployeeEntity("guestrelo", "manager", AccessRights.GUESTRELATIONOFFICER, "guestrelo", "password"));
            Long rt1 = 0L, rt2 = 0L, rt3 = 0L, rt4 = 0L, rt5 = 0L;
            try {
            rt5 = roomTypeSessionBean.createNewRoomType(new RoomTypeEntity("Grand Suite", "Luxury suite with top-notch services and space.", "80 square meters", "1 King bed + 1 Sofa bed", "4", "WiFi, TV, Kitchen, Balcony"), null);
            rt4 = roomTypeSessionBean.createNewRoomType(new RoomTypeEntity("Junior Suite", "A suite with more comfort and a living area.", "45 square meters", "1 King bed", "2", "WiFi, TV, Living Room, Mini Bar"), rt5);
            rt3 = roomTypeSessionBean.createNewRoomType(new RoomTypeEntity("Family Room", "Spacious room designed for families.", "60 square meters", "1 King bed + 1 Sofa bed", "4", "WiFi, TV, Kitchenette"), rt4);
            rt2 = roomTypeSessionBean.createNewRoomType(new RoomTypeEntity("Premier Room", "An upscale room offering enhanced facilities.", "40 square meters", "1 King bed or 2 Twin beds", "2", "WiFi, TV, Coffee Machine"), rt3);
            rt1 = roomTypeSessionBean.createNewRoomType(new RoomTypeEntity("Deluxe Room", "A comfortable room with luxury amenities.", "35 square meters", "1 King bed", "2", "WiFi, TV, Mini Bar"), rt2);
            } catch (RecordNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            }

            // Deluxe Room Rates
            roomRateSessionBean.createNewRoomRate(new RoomRateEntity("Deluxe Room Published", new BigDecimal("100"), "Published", null, null), rt1);
            roomRateSessionBean.createNewRoomRate(new RoomRateEntity("Deluxe Room Normal", new BigDecimal("50"), "Normal", null, null), rt1);

            // Premier Room Rates
            roomRateSessionBean.createNewRoomRate(new RoomRateEntity("Premier Room Published", new BigDecimal("200"), "Published", null, null), rt2);
            roomRateSessionBean.createNewRoomRate(new RoomRateEntity("Premier Room Normal", new BigDecimal("100"), "Normal", null, null), rt2);

            // Family Room Rates
            roomRateSessionBean.createNewRoomRate(new RoomRateEntity("Family Room Published", new BigDecimal("300"), "Published", null, null), rt3);
            roomRateSessionBean.createNewRoomRate(new RoomRateEntity("Family Room Normal", new BigDecimal("150"), "Normal", null, null), rt3);

            // Junior Suite Rates
            roomRateSessionBean.createNewRoomRate(new RoomRateEntity("Junior Suite Published", new BigDecimal("400"), "Published", null, null), rt4);
            roomRateSessionBean.createNewRoomRate(new RoomRateEntity("Junior Suite Normal", new BigDecimal("200"), "Normal", null, null), rt4);

            // Grand Suite Rates
            roomRateSessionBean.createNewRoomRate(new RoomRateEntity("Grand Suite Published", new BigDecimal("500"), "Published", null, null), rt5);
            roomRateSessionBean.createNewRoomRate(new RoomRateEntity("Grand Suite Normal", new BigDecimal("250"), "Normal", null, null), rt5);

            try {
                // Deluxe Room - RoomType ID: rt1
                roomSessionBean.createNewRoom(rt1, new RoomEntity("0101", "Available"));
                roomSessionBean.createNewRoom(rt1, new RoomEntity("0201", "Available"));
                roomSessionBean.createNewRoom(rt1, new RoomEntity("0301", "Available"));
                roomSessionBean.createNewRoom(rt1, new RoomEntity("0401", "Available"));
                roomSessionBean.createNewRoom(rt1, new RoomEntity("0501", "Available"));

                // Premier Room - RoomType ID: rt2
                roomSessionBean.createNewRoom(rt2, new RoomEntity("0102", "Available"));
                roomSessionBean.createNewRoom(rt2, new RoomEntity("0202", "Available"));
                roomSessionBean.createNewRoom(rt2, new RoomEntity("0302", "Available"));
                roomSessionBean.createNewRoom(rt2, new RoomEntity("0402", "Available"));
                roomSessionBean.createNewRoom(rt2, new RoomEntity("0502", "Available"));

                // Family Room - RoomType ID: rt3
                roomSessionBean.createNewRoom(rt3, new RoomEntity("0103", "Available"));
                roomSessionBean.createNewRoom(rt3, new RoomEntity("0203", "Available"));
                roomSessionBean.createNewRoom(rt3, new RoomEntity("0303", "Available"));
                roomSessionBean.createNewRoom(rt3, new RoomEntity("0403", "Available"));
                roomSessionBean.createNewRoom(rt3, new RoomEntity("0503", "Available"));

                // Junior Suite - RoomType ID: rt4
                roomSessionBean.createNewRoom(rt4, new RoomEntity("0104", "Available"));
                roomSessionBean.createNewRoom(rt4, new RoomEntity("0204", "Available"));
                roomSessionBean.createNewRoom(rt4, new RoomEntity("0304", "Available"));
                roomSessionBean.createNewRoom(rt4, new RoomEntity("0404", "Available"));
                roomSessionBean.createNewRoom(rt4, new RoomEntity("0504", "Available"));

                // Grand Suite - RoomType ID: rt5
                roomSessionBean.createNewRoom(rt5, new RoomEntity("0105", "Available"));
                roomSessionBean.createNewRoom(rt5, new RoomEntity("0205", "Available"));
                roomSessionBean.createNewRoom(rt5, new RoomEntity("0305", "Available"));
                roomSessionBean.createNewRoom(rt5, new RoomEntity("0405", "Available"));
                roomSessionBean.createNewRoom(rt5, new RoomEntity("0505", "Available"));
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        

    }

}
    
