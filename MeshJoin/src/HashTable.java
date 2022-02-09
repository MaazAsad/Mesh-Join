
import Buffers.*;
import DB_Handler.*;
import recordTypes.*;

import java.util.HashMap;
import java.util.ArrayList;


public class HashTable {
	
	HashMap<String, ArrayList<Sales>> RunningHashTable = new HashMap<String, ArrayList<Sales>>();
	
	
	public ArrayList<Sales> LoadHashMap(HashMap<Integer,TransactionRecord> TransactionPartition, HashMap<String,MasterRecord> MasterPartition){
		ArrayList<Sales> temp;
		ArrayList<Sales> SalesUpdated = new ArrayList<Sales>();
		if(TransactionPartition != null) {
			for(int i:TransactionPartition.keySet()) {
				String ProdID = TransactionPartition.get(i).PRODUCT_ID;
				temp = RunningHashTable.get(ProdID);
				
				if(temp != null) {
					Sales TempSales = new Sales(TransactionPartition.get(i), MasterPartition.get(ProdID));
					temp.add(TempSales);
					RunningHashTable.put(ProdID,temp);
					SalesUpdated.add(TempSales);
				}
				else {
					temp = new ArrayList<Sales>();
					Sales TempSales = new Sales(TransactionPartition.get(i), MasterPartition.get(ProdID));
					temp.add(TempSales);
					RunningHashTable.put(ProdID,temp);
					SalesUpdated.add(TempSales);
				}
				
			}
			
		}
		return SalesUpdated;
	}
	
	public ArrayList<Sales> UpdateHashTable(HashMap<String,MasterRecord> MasterPartition) {
		ArrayList<Sales> temp = null;
		ArrayList<Sales> ToSend = new ArrayList<Sales>();
		for(String i: MasterPartition.keySet()) {
			temp = RunningHashTable.get(i);
			if(temp != null) {
				for(int j =0; j < temp.size(); ++j) {
					Sales record = temp.get(j);
					record.PRODUCT_NAME = MasterPartition.get(i).PRODUCT_NAME;
					record.SUPPLIER_ID = MasterPartition.get(i).SUPPLIER_ID;
					record.SUPPLIER_NAME = MasterPartition.get(i).SUPPLIER_NAME;
					record.PRICE = MasterPartition.get(i).PRICE;
					record.CaclulateTotal();
					record.Joined = true;
					temp.set(j, record);
					ToSend.add(record);
				}
				RunningHashTable.put(i, temp);
			}
		}
		return ToSend;
	}
	
	public void ClearEntries(HashMap<Integer,TransactionRecord> TransactionPartition) {
		//for each transaction
		//get the product id list
		//match the transaction id
		//remove the same ones from the list
		//send these sales records ot outbuffer
		ArrayList<Sales> Temp,ToRemove;
		Sales TempSales;
		
		if(TransactionPartition !=null) {
			ToRemove = new ArrayList<Sales>();
			for(int i:TransactionPartition.keySet()) {
				
				
				String ProdID = TransactionPartition.get(i).PRODUCT_ID;
				Temp = RunningHashTable.get(ProdID);
				
				if(Temp !=null) {
					
					for(int j=0; j<Temp.size(); ++j) {
						TempSales = Temp.get(j);
						if(TempSales.TRANSACTION_ID == i) {
							ToRemove.add(TempSales);
						}
					}
					
					for(int j=0 ; j < ToRemove.size(); ++j) {
						for(int k=0; k<Temp.size(); ++k) {
							if(ToRemove.get(j).TRANSACTION_ID == Temp.get(k).TRANSACTION_ID) {
								Temp.remove(k);
								break;
							}
						}
					}
					RunningHashTable.put(ProdID, Temp);
				}
				
			}
		}
		else {
			ToRemove = null;
		}
		
//		return ToSend;
	}
	
	
	public void PrintHashTable() {
		ArrayList<Sales> temp;
		for (String i:RunningHashTable.keySet()) {
			temp = RunningHashTable.get(i);
			System.out.println("Product Key: " + i);
			
			for (int j=0; j<temp.size(); j++) {
				Sales TempSales = temp.get(j);
				System.out.println(TempSales.TRANSACTION_ID);
			}
			System.out.println();
		}
	}
	
	

}
