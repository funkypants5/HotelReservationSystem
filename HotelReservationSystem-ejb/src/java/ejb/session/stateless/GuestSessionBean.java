/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.GuestEntity;
import entity.VisitorEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RecordNotFoundException;

@Stateless
public class GuestSessionBean implements GuestSessionBeanRemote, GuestSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long createNewGuest(GuestEntity guest) {
        String email = guest.getEmail();
        Query query = em.createQuery("SELECT g from GuestEntity g WHERE g.email = :email");
        query.setParameter("email", email);
        try {
            guest = (GuestEntity) query.getSingleResult();
        } catch (NoResultException e) {
            em.persist(guest);
            em.flush();
        }

        return guest.getGuestId();
    }

    @Override
    public Long createNewVisitor(VisitorEntity visitor) {
        em.persist(visitor);
        em.flush();
        return visitor.getVisitorId();
    }

    @Override
    public Long visitorRegister(String name, String email, String password) throws InvalidInputException {
        // Check if the email is already registered
        try {
            retrieveVisitorByEmail(email);
            throw new InvalidInputException("A guest with this email already exists!");
        } catch (RecordNotFoundException ex) {
            // Proceed to register new guest if email does not exist
            VisitorEntity newVisitor = new VisitorEntity(name, email, password);
            em.persist(newVisitor);
            em.flush();
            return newVisitor.getVisitorId();
        }
    }

    @Override
    public VisitorEntity visitorLogin(String email, String password) throws InvalidLoginCredentialException {
        try {
            // Retrieve the guest by email
            VisitorEntity visitor = retrieveVisitorByEmail(email);

            //Check if the password matches
            if (visitor.getPassword().equals(password)) {
                return visitor; // Login successful
            } else {
                throw new InvalidLoginCredentialException("Invalid email or password!");
            }
        } catch (RecordNotFoundException ex) {
            throw new InvalidLoginCredentialException("Invalid email or password!");
        }
    }

    @Override
    public GuestEntity retrieveGuestByEmail(String email) throws RecordNotFoundException {

        Query query = em.createQuery("SELECT g FROM GuestEntity g WHERE g.email = :email");
        query.setParameter("email", email);
        if (query == null) {
            throw new RecordNotFoundException("Guest with email " + email + " does not exist!");
        }
        GuestEntity guest = (GuestEntity) query.getSingleResult();
        return guest;
    }

    @Override
    public VisitorEntity retrieveVisitorByEmail(String email) throws RecordNotFoundException {
        try {
            return em.createQuery("SELECT v FROM VisitorEntity v WHERE v.email = :email", VisitorEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new RecordNotFoundException("Visitor with email " + email + " does not exist!");
        }
    }

}
