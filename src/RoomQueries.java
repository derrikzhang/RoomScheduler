
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Derrikzhang
 */
public class RoomQueries {
    private static Connection connection;
    private static PreparedStatement addRoom;
    private static PreparedStatement getAllPossibleRoom;
    private static PreparedStatement getRoomList;
    private static PreparedStatement getSeatList;
    private static PreparedStatement dropRoom;
    private static ResultSet resultSet;
    
    public static String addRoom(String name, int seats){
        ArrayList<String> roomlist = getRoomList();
        for(String room: roomlist){
            if(name.equals(room))
                return"Room already exist.";
        }
        RoomEntry room = new RoomEntry(name, seats);
        connection = DBConnection.getConnection();
        try
        {
            addRoom = connection.prepareStatement("insert into rooms (name, seats) values (?, ?)");
            addRoom.setString(1, name);
            addRoom.setInt(2, seats);
            addRoom.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
        ArrayList<WaitlistEntry> waitlist = WaitlistQueries.getWaitlist();
        String result = "Room " + name + " has been added. ";
        String r = "";
        for(WaitlistEntry w: waitlist){
            if(seats >= w.getSeats()){
                r = ReservationQueries.addReservationEntry(w.getFaculty(), w.getDate(), w.getSeats());
                if(r.equals("Rooms are full, your reservation has been added to waiting list."))
                    continue;
                WaitlistQueries.deleteWaitlistEntry(w);
                result +=  r;
            }
        }
        return result;
    }
    
    public static ArrayList<RoomEntry> getAllPossibleRoom(Date date){
        connection = DBConnection.getConnection();
        ArrayList<String> getRoomList= getRoomList();
        ArrayList<Integer> seatList = getSeatList();
        ArrayList<RoomEntry> roomList = new ArrayList<RoomEntry>();
        ArrayList<RoomEntry>possibleRoomList = new ArrayList<RoomEntry>();
        int i =0;
        while(i<getRoomList.size()){ 
            System.out.println(seatList.get(i).intValue());
            roomList.add(new RoomEntry(getRoomList.get(i), seatList.get(i)));
            i++;
        }
        
        ArrayList<ReservationEntry> reservationList = ReservationQueries.getReservationByDate(date);
        if(reservationList==null)
            return roomList;
        for(int x=0; x<roomList.size();x++){
           for(int y =0; y<reservationList.size();y++){
               if(reservationList.get(y).getRoom().equals(roomList.get(x).getName())){
                   break;
               }
               else if(y==reservationList.size()-1){
                   possibleRoomList.add(roomList.get(x));
               }   
           }
        }
        if (possibleRoomList.size()==0)
            return null;
        return possibleRoomList;
    }
    
    public static ArrayList<String> getRoomList(){
        connection = DBConnection.getConnection();
        ArrayList<String> roomList = new ArrayList<String>();
        try
        {
            getRoomList = connection.prepareStatement("select name from rooms order by name");
            resultSet = getRoomList.executeQuery();
            while(resultSet.next())
            {
                roomList.add(resultSet.getString(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return roomList;
    }
    
    public static ArrayList<Integer> getSeatList()
    {
        connection = DBConnection.getConnection();
        ArrayList<Integer> seatList = new ArrayList<Integer>();
        try
        {
            getSeatList = connection.prepareStatement("select seats from rooms order by name");
            resultSet = getSeatList.executeQuery();
            
            while(resultSet.next())
            {
                seatList.add(resultSet.getInt(1));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return seatList;
    }
    
    public static String dropRoom(String room){
        ArrayList<Date> datelist = Dates.getAllDates();
        ArrayList<ReservationEntry> reservationList = new ArrayList<ReservationEntry>();
        connection = DBConnection.getConnection();
        try
        {
            dropRoom = connection.prepareStatement("DELETE FROM rooms where (name = ?)");
            dropRoom.setString(1, room);
            dropRoom.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        String output = "";
        
        for(Date date: datelist){
            for(ReservationEntry reservation: ReservationQueries.getReservationByDate(date)){
                if(reservation.getRoom().equals(room)){
                    String result = ReservationQueries.deleteReservationEntry(reservation);
                    if (!result.equals(""))
                        output += "Sorry, " + reservation.getFaculty() + ". Your reservation on " +reservation.getDate()+" has been canceled. You've been added to waitlist.\n";
                    else
                        output += "Sorry, " + reservation.getFaculty() + ". The room of your reservation on " +reservation.getDate()+" has been changed." + result +"\n";
                }
            }
        }
        return output;
    }
}
