/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.exception;

/**
 *
 * @author zchoo
 */
public class EmployeeNotFoundException extends Exception{

    public EmployeeNotFoundException() {
    }
    
    public EmployeeNotFoundException(String msg) {
        super(msg);
    }
}
