/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RecordNotFoundException;

/**
 *
 * @author zchoo
 */
@Remote
public interface PartnerSessionBeanRemote {

    public Long createNewPartner(PartnerEntity partner) throws Exception;

    public List<PartnerEntity> viewAllPartners();
    
    public PartnerEntity loginPartner(String email, String password) throws InvalidLoginCredentialException;
}
