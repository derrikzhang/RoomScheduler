/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Date;
import java.sql.Timestamp;
/**
 *
 * @author Derrikzhang
 */
public class WaitlistEntry {
    private String faculty;
    private Date date;
    private int seats;
    private Timestamp timestamp;
    
    public WaitlistEntry(String faculty, Date date, int seats, Timestamp timestamp){
        setFaculty(faculty);
        setDate(date);
        setSeat(seats);
        setTimestamp(timestamp);
    }
    
    public void setFaculty(String faculty){
        this.faculty = faculty;
    }
    
    public void setDate(Date date){
        this.date = date;
    }
    
    public void setSeat(int seat){
        this.seats = seat;
    }
    
    public void setTimestamp(Timestamp timestamp){
        this.timestamp = timestamp;
    }
    
    public String getFaculty(){
    return this.faculty;
    }
    
    public Date getDate(){
    return this.date;
    }
    
    public int getSeats(){
    return this.seats;
    }
    
    public Timestamp getTimestamp(){
    return this.timestamp;
    }
    
}
