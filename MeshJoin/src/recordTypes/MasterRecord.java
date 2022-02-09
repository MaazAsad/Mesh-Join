package recordTypes;

public class MasterRecord {
	public String PRODUCT_ID = "PRODUCT_ID";
    public String PRODUCT_NAME = "PRODUCT_NAME";
    public String SUPPLIER_ID = "SUPPLIER_ID";
    public String SUPPLIER_NAME = "SUPPLIER_NAME";
    public double PRICE;
    
    public MasterRecord(String PID, String PName, String SupID, String SupName, double Price) {
    	PRODUCT_ID = PID;
    	PRODUCT_NAME = PName;
    	SUPPLIER_ID = SupID;
    	SUPPLIER_NAME = SupName;
    	PRICE = Price;
    }
	
}
