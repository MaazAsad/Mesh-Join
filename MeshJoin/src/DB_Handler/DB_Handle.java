package DB_Handler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;



public class DB_Handle {
	
	public Connection con = null;
	
	public void Connect(String ThreadName) throws ClassNotFoundException {
		 String url = "jdbc:mysql://localhost:3306/DWH_PROJ";
	    String username = "root";
	    String password = "M@azAsad1";

	    try {
	      Class.forName("com.mysql.jdbc.Driver");
	      con = DriverManager.getConnection(url, username, password);

	      System.out.println(ThreadName + " Connected!");

	    } catch (SQLException ex) {
	        throw new Error("Error ", ex);
	    } 
		
	}
	
	public void CloseConnection(String Name) {
		try {
	        if (con != null) {
	            con.close();
	            con = null;
	            System.out.println(Name + "Connection Closed");
	        }
	      } catch (SQLException ex) {
	          System.out.println(ex.getMessage());
	      }
		
	}
	
	
	public void GetMasterRowCount() throws SQLException {
		Statement Sample = con.createStatement();
		ResultSet result = Sample.executeQuery("SELECT COUNT(*) from MASTERDATA;");
		
		while(result.next()) {
			System.out.println(result.getInt(1));
		}
	}
   
}
