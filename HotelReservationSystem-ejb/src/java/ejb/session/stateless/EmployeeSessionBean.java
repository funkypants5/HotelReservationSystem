/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;


import entity.EmployeeEntity;
import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author zchoo
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createEmployee(EmployeeEntity employee) {
        em.persist(employee);
    }

    @Override
    public List<EmployeeEntity> viewAllEmployees() {
        Query query = em.createQuery("SELECT e FROM EmployeeEntity e");
        return query.getResultList();
    }
    
    @Override
     public EmployeeEntity employeeLogin (String username, String password) throws InvalidLoginCredentialException{
         try
        {
            EmployeeEntity employee = retrieveEmployeeByUsername(username);
            
            if(employee.getPassword().equals(password))
            {               
                return employee;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(EmployeeNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
     }

    private EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM EmployeeEntity e WHERE e.username = :username");
        query.setParameter("username", username);

        try {
            return (EmployeeEntity) query.getSingleResult();
        } catch (Exception ex) {
            throw new EmployeeNotFoundException("Employee Username " + username + " does not exist!");
        }
    }

    @Override
    public void createNewPartner(PartnerEntity partner) {
        em.persist(partner);
    }

    @Override
    public List<PartnerEntity> viewAllPartners() {
        Query query = em.createQuery("SELECT p FROM PartnerEntity p");
        return query.getResultList();
    }

   
}
