/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.PartnerEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RecordNotFoundException;

/**
 *
 * @author zchoo
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewPartner (PartnerEntity partner) throws Exception {
        String email = partner.getEmail();
        Query query = em.createQuery("SELECT p from PartnerEntity p WHERE p.email = :email");
        query.setParameter("email", email);
        try {
            partner = (PartnerEntity)query.getSingleResult();
            throw new Exception("Partner already exist");
        } catch (NoResultException e) {
            em.persist(partner);
            em.flush();
        }
        return partner.getPartnerId();
    }

    @Override
    public List<PartnerEntity> viewAllPartners() {
        Query query = em.createQuery("SELECT p FROM PartnerEntity p");
        return query.getResultList();
    }

    @Override
    public PartnerEntity loginPartner(String email, String password) throws InvalidLoginCredentialException{
        Query query = em.createQuery("SELECT p FROM PartnerEntity p WHERE p.email = :email");
        query.setParameter("email", email);
        try{
        PartnerEntity partner = (PartnerEntity)query.getSingleResult(); 
        if (partner.getPassword().equals(password)) {
                return partner;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (NoResultException e) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

}
