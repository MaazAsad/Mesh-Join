import Buffers.*;
import recordTypes.*;


import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;



public class MeshJoin {
	public int i,S1; 
	public Queue<HashMap<Integer,TransactionRecord>> PartitionQueue = new LinkedList<HashMap<Integer,TransactionRecord>>();
	
	public HashMap<Integer,TransactionRecord> TransactionPartition;
	public HashMap<Integer,TransactionRecord> CompletedTransactionPartition;
	public HashMap<String,MasterRecord> MasterPartition;
	
	public HashTable TestHashTable = new HashTable();
	
	public TransactionBuffer TransactionStreamBuffer = new TransactionBuffer();
	public MasterBuffer MasterDataBuffer = new MasterBuffer();
	public SalesWritterBuffer SalesWrite = new SalesWritterBuffer();
	//add outstream buffer
	
	public ResultSet TransactionQueryResult;
	public ResultSet MasterQueryResult;
	
	public ArrayList<Sales> UpdateSales= null;
	
	public MeshJoin() throws ClassNotFoundException, SQLException {
		TransactionStreamBuffer.setup();
		MasterDataBuffer.setup();
		SalesWrite.setup();
		i=S1= 0;
	}
	
	public void Start() throws SQLException, ParseException {
		
		TransactionQueryResult = TransactionStreamBuffer.GetRecords(i);
		System.out.println("Loading Transaction partition: " + (i+1));			
		TransactionPartition = TransactionStreamBuffer.GeneratePartition(TransactionQueryResult);
		
		PartitionQueue.add(TransactionPartition);
		
					
		MasterQueryResult = MasterDataBuffer.GetRecords(i);
		System.out.println("Loading Master Data partition: " + (i%10+1));
		MasterPartition = MasterDataBuffer.GeneratePartition(MasterQueryResult);

		
		UpdateSales = TestHashTable.LoadHashMap(TransactionPartition, MasterPartition);
		if(UpdateSales != null) {
			SalesWrite.InsertSales(UpdateSales);
		}
		
		while (!PartitionQueue.isEmpty()) {
			S1++;
			i++;
			
			if(TransactionStreamBuffer.running) {
				TransactionQueryResult = TransactionStreamBuffer.GetRecords(i);
				System.out.println("Loading Transaction partition: " + (i+1));			
				TransactionPartition = TransactionStreamBuffer.GeneratePartition(TransactionQueryResult);
				
				PartitionQueue.add(TransactionPartition);
				
			}
			MasterQueryResult = MasterDataBuffer.GetRecords(i);
			System.out.println("Loading Master Data partition: " + (i%10+1));
			MasterPartition = MasterDataBuffer.GeneratePartition(MasterQueryResult);
			
			UpdateSales=TestHashTable.LoadHashMap(TransactionPartition, MasterPartition);
			if(UpdateSales != null) {
				SalesWrite.InsertSales(UpdateSales);
			}
			UpdateSales=TestHashTable.UpdateHashTable(MasterPartition);
			if(UpdateSales != null) {
				SalesWrite.InsertSales(UpdateSales);
			}
			
			//after first 10
			//consequently pop a partition
			if(S1>=10) {
				System.out.println("Transaction Partition " + (S1-10+1) + " Dequeued");
				CompletedTransactionPartition= PartitionQueue.remove();
				//TO-DO 
				// clear the entries from the hashtable
				// UpdateSales =TestHashTable.ClearEntries(CompletedTransactionPartition);
				// send the sales data to the outstream buffer to write to db
				
				if(CompletedTransactionPartition == null) {
					TransactionStreamBuffer.end();
					MasterDataBuffer.end();
				}
//				else {
//					//All Sales Uploaded into the Ware House
//					TransactionStreamBuffer.end();
//					MasterDataBuffer.end();
//				}
			}
			
			if(!TransactionStreamBuffer.running && !TransactionStreamBuffer.running && !MasterDataBuffer.running) {
				System.out.println("All data has been loaded into the Data ware house :) <3");
				
			}
		} 	
		
	}
	

}
