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
public class ReservationEntry {
    
    private String faculty;
    private String room;
    private Date date;
    private int seats;

    public ReservationEntry(String faculty, String room, Date date, int seats){
        setFaculty(faculty);
        setRoom(room);
        setDate(date);
        setSeat(seats);
    }
    
    public void setFaculty(String faculty){
        this.faculty = faculty;
    }
    
    public void setRoom(String room){
        this.room = room;
    }
    
    public void setDate(Date date){
        this.date = date;
    }
    
    public void setSeat(int seat){
        this.seats = seat;
    }
    
    public String getFaculty(){
    return this.faculty;
    }
    
    public String getRoom(){
    return this.room;
    }
    
    public Date getDate(){
    return this.date;
    }
    
    public int getSeats(){
    return this.seats;
    }
    
}
