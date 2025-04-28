/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.GuestEntity;
import entity.VisitorEntity;
import javax.ejb.Remote;
import util.exception.InvalidInputException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RecordNotFoundException;

/**
 *
 * @author zchoo
 */
@Remote
public interface GuestSessionBeanRemote {

    public Long createNewGuest(GuestEntity guestEntity);

    public Long createNewVisitor(VisitorEntity visitor);

    public GuestEntity visitorLogin(String email, String password) throws InvalidLoginCredentialException;

    public GuestEntity retrieveGuestByEmail(String email) throws RecordNotFoundException;


    public VisitorEntity retrieveVisitorByEmail(String email) throws RecordNotFoundException;

    public Long visitorRegister(String name, String email, String password) throws InvalidInputException;

}
