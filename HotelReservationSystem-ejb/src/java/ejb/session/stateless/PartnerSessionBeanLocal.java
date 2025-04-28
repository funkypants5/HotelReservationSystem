/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author zchoo
 */
@Local
public interface PartnerSessionBeanLocal {

    public Long createNewPartner(PartnerEntity partner) throws Exception;

    public List<PartnerEntity> viewAllPartners();
    
    public PartnerEntity loginPartner(String email, String password) throws InvalidLoginCredentialException;

}
