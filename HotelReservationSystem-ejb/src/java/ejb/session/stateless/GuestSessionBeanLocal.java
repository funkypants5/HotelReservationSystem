/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.GuestEntity;
import entity.VisitorEntity;
import javax.ejb.Local;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RecordNotFoundException;

/**
 *
 * @author zchoo
 */
@Local
public interface GuestSessionBeanLocal {

    public GuestEntity visitorLogin(String email, String password) throws InvalidLoginCredentialException;

    public GuestEntity retrieveGuestByEmail(String email) throws RecordNotFoundException;

    public VisitorEntity retrieveVisitorByEmail(String email) throws RecordNotFoundException;

    public Long visitorRegister(String name, String email, String password) throws InvalidInputException;

    public Long createNewVisitor(VisitorEntity visitor);

    public Long createNewGuest(GuestEntity guestEntity);

}
