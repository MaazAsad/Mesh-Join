package recordTypes;


public class TransactionRecord {
	
	public long TRANSACTION_ID ;
    public String PRODUCT_ID;
    public String CUSTOMER_ID;
    public String CUSTOMER_NAME = "CUSTOMER_NAME";
    
    public String STORE_ID;
    public String STORE_NAME = "STORE_NAME";
    public String TRANSACTION_DATE = "T_DATE";
    public int TRANSACTION_QUANTITY;
    
    public  TransactionRecord(long TransID, String ProdID, String CustID,String CustName, String StoreID, String StoreName, String Date, int qty) {
    	
    	TRANSACTION_ID = TransID;
    	PRODUCT_ID = ProdID;
    	CUSTOMER_ID = CustID;
    	CUSTOMER_NAME = CustName;
    	STORE_ID= StoreID;
    	STORE_NAME = StoreName;
    	TRANSACTION_DATE = Date;
    	TRANSACTION_QUANTITY = qty;
    	
    }

}
