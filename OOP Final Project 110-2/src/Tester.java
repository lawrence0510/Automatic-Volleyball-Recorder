import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class Tester {
	
	public static void main(String[] args) {
		String server = "jdbc:mysql://140.119.19.73:3315/";
		String database = "110306047"; // change to your own database
		String url = server + database + "?useSSL=false";
		String username = "110306047"; // change to your own username
		String password = "wughu"; // change to your own password
		
		try(Connection conn = DriverManager.getConnection(url, username, password);){
			LoginPage loginPage = new LoginPage(conn);
			loginPage.setVisible(true);
			loginPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}