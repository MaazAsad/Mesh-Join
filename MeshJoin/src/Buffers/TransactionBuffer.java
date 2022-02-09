package Buffers;
import DB_Handler.DB_Handle;
import recordTypes.Partition;
import recordTypes.TransactionRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


//extend into thread
public class TransactionBuffer {
	
	private String Name = "Realtime_TransactionBuffer";
	private final String Read_SQL = "select * from TRANSACTIONS limit 50 offset ?;"; //where transaction_id<51
	
	private DB_Handle TransactionBufferHandler = new DB_Handle();
	
	private Connection InstreamConnection;
	public boolean running = false;
	

	
	public void setup() throws ClassNotFoundException {
		System.out.println("Setting up Transaction Buffer");
		TransactionBufferHandler.Connect(Name);
		InstreamConnection  = TransactionBufferHandler.con;
		running = true;
	}
	
	
	public void end() {
		TransactionBufferHandler.CloseConnection(Name);
		InstreamConnection = null;
		running = false;
	}
	
	
	//reading transaction DB
	public ResultSet GetRecords(int Iteration) throws SQLException {
		ResultSet result;
		PreparedStatement FetchRecords = InstreamConnection.prepareStatement(Read_SQL);
		FetchRecords.setInt(1, (Iteration)*50);
		
		result = FetchRecords.executeQuery();
		

		
		return result;
		
	}
	
	public HashMap<Integer,TransactionRecord> GeneratePartition(ResultSet QueryResult) throws SQLException {
		if(QueryResult.isBeforeFirst()) {
			
			Partition NextPartition = new Partition();
			
			return NextPartition.GenerateTransactionPartition(QueryResult);
		}
		else {
			System.out.println("Transaction Table has been completly read. Closing Transaction Buffer");
			running = false;
			
		}
		return null;
		
	}
	
	
}
