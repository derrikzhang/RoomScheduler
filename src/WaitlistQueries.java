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
/**
 *
 * @author Derrikzhang
 */
public class WaitlistQueries {
    private static Connection connection;
    private static PreparedStatement getWaitlistByDate;
    private static PreparedStatement addWaitlistEntry;
    private static PreparedStatement deletWaitlistEntry;
    private static PreparedStatement getWaitlistByFaculty;
    private static ResultSet resultSet;
    
    
    public static ArrayList<WaitlistEntry> getWaitlist(){
        connection = DBConnection.getConnection();
        ArrayList<WaitlistEntry> waitlist = new ArrayList<WaitlistEntry>();
        try
        {
            getWaitlistByDate = connection.prepareStatement("select faculty, date, seats, timestamp from waitlist order by date, timestamp");
            resultSet = getWaitlistByDate.executeQuery();
            while(resultSet.next())
            {
                waitlist.add(new WaitlistEntry(resultSet.getString(1), resultSet.getDate(2), resultSet.getInt(3), resultSet.getTimestamp(4)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return waitlist;
    }
    
    public static ArrayList<WaitlistEntry> getWaitlistByFaculty(String faculty){
        connection = DBConnection.getConnection();
        ArrayList<WaitlistEntry> waitlist = new ArrayList<WaitlistEntry>();
        try
        {
            getWaitlistByFaculty = connection.prepareStatement("select faculty, date, seats, timestamp  from waitlist where faculty = ?");
            getWaitlistByFaculty.setString(1, faculty);
            resultSet = getWaitlistByFaculty.executeQuery();
            
            while(resultSet.next())
            {
                waitlist.add(new WaitlistEntry(resultSet.getString(1), resultSet.getDate(2),resultSet.getInt(3), resultSet.getTimestamp(4)));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return waitlist;
    }
    
    public static String addWaitlistEntry(WaitlistEntry waitlist)
    {
        ArrayList<WaitlistEntry>waitList = getWaitlist();
        for(WaitlistEntry w: waitList){
            if(waitlist.getFaculty().equals(w.getFaculty())&&waitlist.getDate().equals(w.getSeats()))
                return "Waitlist already exist.";
        }
        connection = DBConnection.getConnection();
        try
        {
            addWaitlistEntry = connection.prepareStatement("insert into waitlist(faculty, date, seats, timestamp) values (?, ?, ?, ?)");
            addWaitlistEntry.setString(1, waitlist.getFaculty());
            addWaitlistEntry.setDate(2, waitlist.getDate());
            addWaitlistEntry.setInt(3, waitlist.getSeats());
            addWaitlistEntry.setTimestamp(4, waitlist.getTimestamp());
            addWaitlistEntry.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return "Waitlist has been added.";
    }
    
    public static void deleteWaitlistEntry(WaitlistEntry waitlist){
        connection = DBConnection.getConnection();
        try
        {
            deletWaitlistEntry = connection.prepareStatement("DELETE FROM waitlist where (faculty = ?) AND (date = ?) AND (seats = ?)");
            deletWaitlistEntry.setString(1, waitlist.getFaculty());
            deletWaitlistEntry.setDate(2, waitlist.getDate());
            deletWaitlistEntry.setInt(3, waitlist.getSeats());
            deletWaitlistEntry.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    
}
