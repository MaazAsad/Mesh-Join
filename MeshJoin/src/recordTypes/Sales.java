package recordTypes;

public class Sales {
	
	public long TRANSACTION_ID ;
	public String PRODUCT_ID = "PRODUCT_ID";
    public String PRODUCT_NAME = "PRODUCT_NAME";
    public String SUPPLIER_ID = "SUPPLIER_ID";
    public String SUPPLIER_NAME = "SUPPLIER_NAME";
    
    
    public String CUSTOMER_ID;
    public String CUSTOMER_NAME = "CUSTOMER_NAME";
    
    public String STORE_ID;
    public String STORE_NAME = "STORE_NAME";
    public String TRANSACTION_DATE = "T_DATE";
    
    public int TRANSACTION_QUANTITY;
    public double PRICE;
    public double TOTAL;
    
    public boolean Joined = false;
    
    public Sales() {}
    
    public Sales(TransactionRecord TransRec, MasterRecord MasterRec) {
    	TRANSACTION_ID = TransRec.TRANSACTION_ID;
    	PRODUCT_ID = TransRec.PRODUCT_ID;
        
        CUSTOMER_ID= TransRec.CUSTOMER_ID;
        CUSTOMER_NAME = TransRec.CUSTOMER_NAME;
        
        STORE_ID = TransRec.STORE_ID;
        STORE_NAME = TransRec.STORE_NAME;
        TRANSACTION_DATE = TransRec.TRANSACTION_DATE;
        
        TRANSACTION_QUANTITY = TransRec.TRANSACTION_QUANTITY;
       
        if(MasterRec != null) {
        	 PRODUCT_NAME = MasterRec.PRODUCT_NAME;
             SUPPLIER_ID = MasterRec.SUPPLIER_ID;
             SUPPLIER_NAME = MasterRec.SUPPLIER_NAME;
             PRICE = MasterRec.PRICE;
             TOTAL  = TRANSACTION_QUANTITY * PRICE;
             Joined = true;
        }
        else {
        	PRODUCT_NAME = SUPPLIER_ID =SUPPLIER_NAME = "";
        	PRICE = TOTAL = 0.0;
        }
    }
    
    public void CaclulateTotal() {
    	TOTAL = PRICE * TRANSACTION_QUANTITY;
    }
}
