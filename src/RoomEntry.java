/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Derrikzhang
 */
public class RoomEntry {
    private String name;
    private int seats;
    
    public RoomEntry(String name, int seats){
        setName(name);
        setSeats(seats);
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setSeats(int seat){
        this.seats= seat;
    }
    
    public String getName(){
        return this.name;
    }
    
    public int getSeats(){
        return this.seats;
    }
    
}
