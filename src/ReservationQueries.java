/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.Calendar;
/**
 *
 * @author Derrikzhang
 */
public class ReservationQueries {
    private static Connection connection;
    private static PreparedStatement addReservationEntry;
    private static PreparedStatement getReservationByDate;
    private static PreparedStatement getReservationsByFaculty;
    private static PreparedStatement getRoomsReservedByDate;
    private static PreparedStatement deleteReservationEntry;
    private static ResultSet resultSet;
    

    public static String addReservationEntry(String faculty, Date date, int seats)
    {
        System.out.print(faculty);
        System.out.print(date);
        System.out.print("=="+seats+"==");
        Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        if (seats <= 0){
            return null;
        }
        if (getReservationByDate(date)!= null){
            for(ReservationEntry reservation: getReservationByDate(date)){
                if (faculty.equals(reservation.getFaculty()))
                    return "Each faculty can only reserve one room in a day.";
        }
        }
        String room = "";
        connection = DBConnection.getConnection();
        
        ArrayList<RoomEntry> possibleRoom = RoomQueries.getAllPossibleRoom(date);
        int minSeat = -1;
        if (possibleRoom == null){
            WaitlistQueries.addWaitlistEntry(new WaitlistEntry(faculty, date, seats, timestamp));
            return ("Rooms are full, your reservation has been added to waiting list.");
        }
        for (RoomEntry r: possibleRoom){
            System.out.println("Name: "+r.getName());
            System.out.println("Seats: "+r.getSeats());
            if (seats<= r.getSeats()){
                if (minSeat<0){
                    minSeat = r.getSeats();
                    room = r.getName();
                    System.out.print(minSeat);
                }
                else if (minSeat > r.getSeats()){
                    minSeat = r.getSeats();
                    room = r.getName();
                    System.out.print(minSeat);
                }
            }
        }
        if (minSeat <0){
            WaitlistQueries.addWaitlistEntry(new WaitlistEntry(faculty, date, seats, timestamp));
            return ("Rooms are full, your reservation has been added to waiting list.");
        }
        System.out.print(minSeat);
        try
        {
            addReservationEntry = connection.prepareStatement("insert into reservations (faculty, room, date, seats, timestamp) values (?, ?, ?, ?, ?)");
            addReservationEntry.setString(1, faculty);
            addReservationEntry.setString(2, room);
            addReservationEntry.setDate(3, date);
            addReservationEntry.setInt(4, seats);
            addReservationEntry.setTimestamp(5, timestamp);
            addReservationEntry.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return faculty + " reserved room " + room + ".";
    }
    

    public static ArrayList<ReservationEntry> getReservationByDate(Date date){
        connection = DBConnection.getConnection();
        ArrayList<ReservationEntry> reservationList = new ArrayList<ReservationEntry>();
        try
        {
            getReservationByDate = connection.prepareStatement("select faculty, room, date, seats, timestamp  from reservations where date = ?");
            getReservationByDate.setDate(1, date);
            resultSet = getReservationByDate.executeQuery();
            while(resultSet.next())
            {
                reservationList.add(new ReservationEntry(resultSet.getString(1), resultSet.getString(2),resultSet.getDate(3),resultSet.getInt(4)));
            }
            if (reservationList.size()==0)
                return null;
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return reservationList;
    }
    
    public static ArrayList<RoomEntry> getRoomsReservedByDate(Date date){
        connection = DBConnection.getConnection();
        ArrayList<RoomEntry> roomList = new ArrayList<RoomEntry>();
        try
        {
            getRoomsReservedByDate = connection.prepareStatement("select (name, seats) from rooms where date =?");
            getRoomsReservedByDate.setDate(1, date);
            resultSet = getRoomsReservedByDate.executeQuery();
            
            while(resultSet.next())
            {
                roomList.add(new RoomEntry(resultSet.getString(1), resultSet.getInt(2)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return roomList;
    }
    
    public static ArrayList<ReservationEntry> getReservationsByFaculty(String faculty){
        connection = DBConnection.getConnection();
        ArrayList<ReservationEntry> reservationList = new ArrayList<ReservationEntry>();
        try
        {
            getReservationsByFaculty = connection.prepareStatement("SELECT faculty, room, date, seats FROM reservations WHERE faculty = ?");
            getReservationsByFaculty.setString(1, faculty);
            resultSet = getReservationsByFaculty.executeQuery();
            
            while(resultSet.next())
            {
                reservationList.add(new ReservationEntry(resultSet.getString(1), resultSet.getString(2),resultSet.getDate(3),resultSet.getInt(4)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return reservationList;
    }
    
    public static String deleteReservationEntry(ReservationEntry reservation){
        connection = DBConnection.getConnection();
        try
        {
            deleteReservationEntry = connection.prepareStatement("DELETE FROM reservations where (faculty = ?) AND (room = ?) AND (date = ?) AND (seats = ?)");
            deleteReservationEntry.setString(1, reservation.getFaculty());
            deleteReservationEntry.setString(2, reservation.getRoom());
            deleteReservationEntry.setDate(3, reservation.getDate());
            deleteReservationEntry.setInt(4, reservation.getSeats());
            deleteReservationEntry.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        String result = "";
        ArrayList<WaitlistEntry> waitList = WaitlistQueries.getWaitlist();
        ArrayList<RoomEntry> roomList = RoomQueries.getAllPossibleRoom(reservation.getDate());
        if(roomList==null)
            return "";
        for(WaitlistEntry w: waitList)
               if(w.getSeats()<=roomList.get(0).getSeats()){
                   result = addReservationEntry(w.getFaculty(), w.getDate(), w.getSeats());
                   WaitlistQueries.deleteWaitlistEntry(w);
                   break;
               }
       return result;
    }
}

