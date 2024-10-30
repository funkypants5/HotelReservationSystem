/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author zchoo
 */
@Local
public interface employeeSessionBeanLocal {

    public void createEmployee(Employee employee);
    
    public List<Employee> viewAllEmployees();
    
}
