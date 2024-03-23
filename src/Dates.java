/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author Derrikzhang
 */
public class Dates {
    private static Connection connection;
    private static PreparedStatement addDate;
    private static PreparedStatement getAllDates;
    private static ResultSet resultSet;
    
    public static String addDate(Date date)
    {
        ArrayList<Date> datelist = getAllDates();
        if(!datelist.isEmpty()){
            for(Date d: datelist)
                if(d.equals(date))
                    return "Date already exist.";
        }
        connection = DBConnection.getConnection();
        try
        {
            addDate = connection.prepareStatement("insert into date (date) values (?)");
            addDate.setDate(1, date);
            addDate.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return "Date " + date.toString() + " has been added.";
    }
    
    
    public static ArrayList<Date> getAllDates(){
        connection = DBConnection.getConnection();
            ArrayList<Date> dateList = new ArrayList<Date>();
            try
            {
                getAllDates = connection.prepareStatement("select date from date order by date");
                resultSet = getAllDates.executeQuery();

                while(resultSet.next())
                {
                    dateList.add(resultSet.getDate(1));
                }
            }
            catch(SQLException sqlException)
            {
                sqlException.printStackTrace();
            }
            return dateList;
    }
}
