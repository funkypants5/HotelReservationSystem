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
import util.enumeration.AccessRights;

/**
 *
 * @author zchoo
 */
@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @PostConstruct
    public void postConstruct() {
        if(em.find(EmployeeEntity.class, 1l) == null) {
            employeeSessionBean.createEmployee(new EmployeeEntity("Sean", "Lee", AccessRights.SYSTEMADMIN, "sean123", "password"));
        }
    }

}
