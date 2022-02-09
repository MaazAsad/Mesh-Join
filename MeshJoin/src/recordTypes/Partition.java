package recordTypes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Partition {
	
	public HashMap<Integer,TransactionRecord> GenerateTransactionPartition(ResultSet Results) throws SQLException {
		HashMap<Integer,TransactionRecord> RecordsHashMap = new HashMap <Integer,TransactionRecord>();
		
		while (Results.next()) {
			long TRANSACTION_ID = Results.getLong("TRANSACTION_ID"); ;
		    String PRODUCT_ID = Results.getString("PRODUCT_ID");;
		    String CUSTOMER_ID = Results.getString("CUSTOMER_ID");;
		    String CUSTOMER_NAME = Results.getString("CUSTOMER_NAME");
		    String STORE_ID= Results.getString("STORE_ID");;
		    String STORE_NAME = Results.getString("STORE_NAME");
		    String TRANSACTION_DATE = Results.getString("T_DATE");
		    int QTY= Results.getInt("QUANTITY");; 
			
			TransactionRecord NewRecord = new TransactionRecord(TRANSACTION_ID,PRODUCT_ID,CUSTOMER_ID,CUSTOMER_NAME,STORE_ID,STORE_NAME,TRANSACTION_DATE,QTY);

			RecordsHashMap.put((int)TRANSACTION_ID, NewRecord);  
		}
		return RecordsHashMap;
	}
	
	public HashMap<String,MasterRecord> GenerateMasterPartition(ResultSet Results) throws SQLException{
		HashMap<String,MasterRecord> MasterRecordsHashMap = new HashMap <String,MasterRecord>();
		
		while (Results.next()) {
			
		    String PRODUCT_ID = Results.getString("PRODUCT_ID");;
		    String PRODUCT_NAME = Results.getString("PRODUCT_NAME");
		    String SUPPLIER_ID= Results.getString("SUPPLIER_ID");;
		    String SUPPLIER_NAME = Results.getString("SUPPLIER_NAME");
		    double Price= Results.getDouble("PRICE");; 
			
			MasterRecord NewRecord = new MasterRecord(PRODUCT_ID,PRODUCT_NAME,SUPPLIER_ID,SUPPLIER_NAME,Price);

			MasterRecordsHashMap.put(PRODUCT_ID,NewRecord);  
		}
		
		return MasterRecordsHashMap;
	}
}
