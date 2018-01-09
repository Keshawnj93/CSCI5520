import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Jan8Practice {
    
    static Mndsgn user;
    public static void main(String[] args){
        //Load Driver
        try{
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e){
            System.out.println ("Error loading jdbc driver");
            System.exit(0);
        }
        System.out.println("Driver Loaded Successfully");
        
        //Connect to Database
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(user.conn + "practicedb", user.user, user.password);
        } catch (Exception e){
            System.out.println ("Error connecting to server");
            System.exit(0);
        }
        System.out.println("Database Connected Successfully");
        
        //Display data
       Statement statement = null;
        try{
            statement = connection.createStatement();
        } catch (Exception e){
            System.out.println ("Error creating statement");
            System.exit(0);
        }
        
        try{
            ResultSet res = statement.executeQuery("select * from fruits");
            while (res.next()){
                System.out.println(res.getInt(1) + " " + res.getString(2) + " "
                + res.getString(3));
            }
        } catch (Exception e){
            
        }
    }
}
