package Buffers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


import DB_Handler.DB_Handle;
import recordTypes.*;

public class MasterBuffer {
	private String Name = "Realtime_MasterBuffer";
	private final String Read_SQL = "select * from MASTERDATA order by PRODUCT_ID asc limit 10 offset ?;"; //where transaction_id<51
	
	private DB_Handle MasterBufferHandler = new DB_Handle();
	
	private Connection InstreamConnection;
	public boolean running= false;
	
	public void setup() throws ClassNotFoundException {
		MasterBufferHandler.Connect(Name);
		InstreamConnection  = MasterBufferHandler.con;
		running = true;
	}
	
	
	public void end() {
		MasterBufferHandler.CloseConnection(Name);
		InstreamConnection = null;
		running = false;
	}
	
	
	//reading transaction DB
	public ResultSet GetRecords(int Iteration) throws SQLException {
		ResultSet result;
		PreparedStatement FetchRecords = InstreamConnection.prepareStatement(Read_SQL);
		FetchRecords.setInt(1, (Iteration%10)*10);
		
		result = FetchRecords.executeQuery();
		
		return result;
		
	}
	
	public HashMap<String,MasterRecord> GeneratePartition(ResultSet QueryResult) throws SQLException {
		if(QueryResult.isBeforeFirst()) {
			
			Partition NextPartition = new Partition();
			
			return NextPartition.GenerateMasterPartition(QueryResult);
		}
		else {
			System.out.println("Error Reading Master Data. Closing MasterData");
			running = false;
		}
		return null;
	}
}
