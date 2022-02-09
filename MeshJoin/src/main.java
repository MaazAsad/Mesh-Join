import java.sql.SQLException;
import java.text.ParseException;




public class main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		// TODO Auto-generated method stub
		
//		
//		HashMap<Integer,TransactionRecord> TransactiontestPartition;
//		HashMap<String,MasterRecord> MasterTestPartition;
//		
//		HashTable TestHashTable = new HashTable();
//		
//		TransactionBuffer NewTransactionHandler = new TransactionBuffer();
//		NewTransactionHandler.setup();
//		MasterBuffer NewMasterBuffer = new MasterBuffer();
//		NewMasterBuffer.setup();
//		
//		
//		for (int i =0; i<2; ++i) {
//			ResultSet SampleResult = NewTransactionHandler.GetRecords(i);
//			System.out.println("Creating Transaction partition: " + (i+1));			
//			TransactiontestPartition = NewTransactionHandler.GeneratePartition(SampleResult);			
//			
//						
//			ResultSet MasterResult = NewMasterBuffer.GetRecords(i);
//			System.out.println("loading Master Data partition" + (i+1)%10);
//			MasterTestPartition = NewMasterBuffer.GeneratePartition(MasterResult);
//
//			
//			TestHashTable.LoadHashMap(TransactiontestPartition, MasterTestPartition);
//			TestHashTable.UpdateHashTable(MasterTestPartition);
//			
//		}
//		
//		TestHashTable.PrintHashTable();
//		
//		
		
		MeshJoin MyMeshJoin = new MeshJoin();
		
		MyMeshJoin.Start();
	}

}
