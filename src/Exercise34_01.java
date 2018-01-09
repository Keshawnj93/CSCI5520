import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Exercise34_01 extends Application{
    private Statement initDB = null, stmt = null;
    private PreparedStatement pstmt = null;
    private Label lbl = new Label();
    private Text t = new Text();
    private TextField txtID = new TextField(), txtLast = new TextField(), 
            txtFirst = new TextField(), txtMI = new TextField(), txtAddress = new TextField(), 
            txtCity = new TextField(), txtState = new TextField(), txtTele = new TextField(), 
            txtEmail = new TextField();   
    private Button view, insert, update, clear;
    private Connection connection;
    
    @Override
    public void start(Stage primaryStage){
        initDB();
        view = new Button("View");
        insert = new Button("Insert");
        update = new Button("Update");
        clear = new Button("Clear");
        
        view.setOnAction(e -> {
            if (viewValidate()){
                view();
            } else {
                t.setText("Please enter a valid ID");
                t.setFill(Color.RED);
                lbl.setText(t.getText());
            }
        });
        
        insert.setOnAction(e -> {
            if(insertValidate()){
                insert();
            }
        });
        
        update.setOnAction(e -> {
           if (insertValidate()){
               update();
           } 
        });
        
        clear.setOnAction(e -> {
            clear();
        });
        
        txtMI.setPrefColumnCount(1);
        
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));
        
        pane.addRow(0, lbl);
        pane.addRow(1, new Label("ID"), txtID);
        pane.addRow(2, new Label("Last Name"), txtLast, new Label("First Name"), txtFirst, new Label("MI"), txtMI);
        pane.addRow(3, new Label("Address"), txtAddress);
        pane.addRow(4, new Label("City"), txtCity);
        pane.addRow(5, new Label("State"), txtState);
        pane.addRow(6, new Label("Telephone"), txtTele);
        pane.addRow(7, new Label("Email"), txtEmail);
        HBox buttons = new HBox();
        buttons.getChildren().addAll(view, insert, update, clear);
        pane.add(buttons, 3, 8);
        
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Exercise 34_01");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(e -> {
            dropTable();
            Platform.exit();
        });
    }
    
    private void initDB(){
        //Load Driver
        try{
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e){
            System.out.println ("Error loading jdbc driver");
            System.exit(0);
        }
        System.out.println("Driver Loaded Successfully");
        
        //Connect to Database
        connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost/CSCI5520", "root", "Flylow13");
        } catch (Exception e){
            System.out.println ("Error connecting to server");
            System.exit(0);
        }
        System.out.println("Database Connected Successfully");
        
        //Create initial table
        try{
            initDB = connection.createStatement();
        } catch (Exception e){
            System.out.println ("Error: Statement could not be created");
            System.exit(0);
        }
        
        try{
            initDB.execute("create table Staff("
                    + "id char(9) not null,"
                    + "lastName varchar(15),"
                    + "firstName varchar(15),"
                    + "mi char(1),"
                    + "address varchar(20),"
                    + "city varchar(20),"
                    + "state char(2),"
                    + "telephone char(10),"
                    + "email varchar(40),"
                    + "primary key(id)"
                    + ");");
        } catch (Exception e){
            System.out.println ("Error in executing statement");
            System.exit(0);
        }
    }
    
    private boolean viewValidate(){
        //Is the ID valid
        return (txtID.getText().length() < 10 && txtID.getText().length() > 0);
    }
    
    private boolean insertValidate(){
        boolean ret;
        
        //Is the ID valid
        ret = (txtID.getText().length() < 10 && txtID.getText().length() > 0);
        if (!ret){
            lbl.setText("Please enter a valid ID");
            return ret;
        }
        
        //Is last name valid
        ret = (txtLast.getText().length() < 16);
        if (!ret){
            lbl.setText("Last name must be under 15 characters");
            return ret;
        }
        
        //Is first name valid
        ret = (txtFirst.getText().length() < 16);
        if (!ret){
            lbl.setText("Fast name must be under 15 characters");
            return ret;
        }
        
        //Is middle initial valid
        ret = (txtMI.getText().length() < 2);
        if (!ret){
            lbl.setText("Middle initial must be no more than 1 character");
            return ret;
        }
        
        //Is address valid
        ret = (txtAddress.getText().length() < 21);
        if (!ret){
            lbl.setText("Address must be under 20 characters");
            return ret;
        }
        
        //Is the city valid
        ret = (txtCity.getText().length() < 21);
        if (!ret){
            lbl.setText("City must be under 20 characters");
            return ret;
        }
        
        //Is state valid
        ret = (txtState.getText().length() < 3);
        if (!ret){
            lbl.setText("State must be under 2 characters");
            return ret;
        }
        
        //Is telephone valid
        ret = (txtTele.getText().length() < 11);
        if (!ret){
            lbl.setText("Telephone must be under 10 characters");
            return ret;
        }
        
        //Is email valid
        ret = (txtMI.getText().length() < 41);
        if (!ret){
            lbl.setText("Email must be under 40 characters");
            return ret;
        }
        
        return ret;
    }
    
    private void view(){
        String id = txtID.getText();
        try {
            stmt = connection.createStatement();
        } catch (Exception e){
            System.out.println ("Error in creating statement");
        }
        
        ResultSet res = null;
        try {
            res = stmt.executeQuery("Select * from Staff where id = " + id);
        } catch (Exception e){
            //
        }
        
        try {
            if (!res.isBeforeFirst()){ // Result set is empty
                t.setText("The requested ID could not be found");
                t.setFill(Color.RED);
                lbl.setText(t.getText()); 
            } else{
                res.next();
                txtLast.setText(res.getString(2));
                txtFirst.setText(res.getString(3));
                txtMI.setText(res.getString(4));
                txtAddress.setText(res.getString(5));
                txtCity.setText(res.getString(6));
                txtState.setText(res.getString(7));
                txtTele.setText(res.getString(8));
                txtEmail.setText(res.getString(9));
                
                t.setText("ID Retrieved Successfully");
                t.setFill(Color.RED);
                lbl.setText(t.getText()); 
            }
        } catch (Exception e){
            System.out.println("Error in retrieval");
        }
    }
    
    private void insert(){
        String state = "Insert into staff (id, lastName, firstName, mi, address, city, state, telephone, email)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        
        try{
            pstmt = connection.prepareCall(state);
        } catch (Exception e){
            System.out.println("Error in preparing statement");
        }
        
        try {
            //Test whether the ID entered is already in use
            stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("Select * from Staff where id = " + txtID.getText());
            if (res.isBeforeFirst()){
                lbl.setText("Error: The ID is already in use");
                return;
            }
            
            //Insert new row into table
            pstmt.setString(1, txtID.getText());
            pstmt.setString(2, txtFirst.getText());
            pstmt.setString(3, txtLast.getText());
            pstmt.setString(4, txtMI.getText());
            pstmt.setString(5, txtAddress.getText());
            pstmt.setString(6, txtCity.getText());
            pstmt.setString(7, txtState.getText());
            pstmt.setString(8, txtTele.getText());
            pstmt.setString(9, txtEmail.getText());
            
            pstmt.execute();
            
            lbl.setText("ID: " + txtID.getText() + " added successfully");
        } catch (Exception e){
            //
        }
    }
    
    private void update(){
        String state = "Update staff "
                + "set lastName = ?, firstName = ?, mi = ?, address = ?, city = ?, state = ?, telephone = ?, email = ? "
                + "where id = ?;";
        
        try{
            pstmt = connection.prepareCall(state);
        } catch (Exception e){
            System.out.println("Error in preparing statement");
        }
        
        try {
            //Test whether the ID entered is already in use
            stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("Select * from Staff where id = " + txtID.getText());
            if (!res.isBeforeFirst()){
                lbl.setText("Error: ID could not be found");
                return;
            }
            
            //Insert new row into table
            pstmt.setString(1, txtLast.getText());
            pstmt.setString(2, txtFirst.getText());
            pstmt.setString(3, txtMI.getText());
            pstmt.setString(4, txtAddress.getText());
            pstmt.setString(5, txtCity.getText());
            pstmt.setString(6, txtState.getText());
            pstmt.setString(7, txtTele.getText());
            pstmt.setString(8, txtEmail.getText());
            pstmt.setString(9, txtID.getText());
            
            pstmt.execute();
            
            lbl.setText("ID: " + txtID.getText() + " updated successfully");
        } catch (Exception e){
            //
        }
    }
    
    private void clear(){
        //Clear all text fields
        txtID.setText("");
        txtFirst.setText("");
        txtLast.setText("");
        txtMI.setText("");
        txtAddress.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtTele.setText("");
        txtEmail.setText("");
        lbl.setText("");
    }
    
    private void dropTable(){
        try{
            stmt = connection.createStatement();
            stmt.execute("Drop table staff;");
            connection.close();
        } catch (Exception e){
            //
        }
        
    }
    
    public static void main(String[] args){
        launch(args);
    } 
}
