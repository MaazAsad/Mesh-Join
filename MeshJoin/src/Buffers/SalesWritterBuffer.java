package Buffers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


import DB_Handler.DB_Handle;
import recordTypes.*;

public class SalesWritterBuffer {
	
	private String Name = "Realtime_DW_Buffer";
	private DB_Handle DWBufferHandler = new DB_Handle();
	
	private Connection OutStreamConnection;
	private boolean running= false;
	

    private String QueryTransaction = "select * from SALES where TRANSACTION_ID=?";
    private String UpdateSales = "INSERT INTO SALES(TRANSACTION_ID, PRODUCT_ID, SUPPLIER_ID, CUSTOMER_ID, STORE_ID, DATE_ID,QUANTITY, TOTAL_SALE)"
            + "VALUES (?,?,?,?,?,?,?,?)";

    private PreparedStatement TransInsert;
    private PreparedStatement QueryTrans;
    
    //for Supplier Table
    private String QuerySupplier = "select * from SUPPLIERS     where SUPPLIER_ID=?";
    private String UpdateSupplier= "insert into SUPPLIERS(SUPPLIER_ID,SUPPLIER_NAME) values(?,?)";
    private PreparedStatement QuerySupplierStatement;
    private PreparedStatement PreparedSupplierStatement;
    
    //for customer table
    private String QueryCustomer = "select * from CUSTOMERS where CUSTOMER_ID=?";
    private String UpdateCustomer = "insert into CUSTOMERS values( ?,?)";
    private PreparedStatement QueryCustomerStatement;
    private PreparedStatement PreparedCustomerStatement;
    
    //for store table
    private String QueryStore = "select * from STORES where STORE_ID=?";
    private String UpdateStore = "insert into STORES values( ?,? )";
    private PreparedStatement QueryStoreStatement;
    private PreparedStatement PreparedStoreStatement;
    
    //product table
    private String QueryProduct = "select * from PRODUCTS where PRODUCT_ID=?";
    private String UpdateProduct= "insert into PRODUCTS values(?,?)";
    private PreparedStatement QueryProductStatement;
    private PreparedStatement PreparedProductStatement;
    
    //dates table
    private String QueryDate = "select * from DATES where DATE_ID=?";
    private String UpdateDate = "INSERT INTO DATES(DATE_ID, DAY, MONTH, QUATER, YEAR, WEEK_OF_YEAR, WEEK_DAY)"+ "VALUES(?,?,?,?,?,?,?)";
   
    private Calendar TempCalendar = new GregorianCalendar();
    
    private PreparedStatement QueryDateStatement;
    private PreparedStatement PreparedDateStatement;
    private String WEEKDAYS[] = {null,"SUNDAY","MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY","FRIDAY","SATURDAY"};
	
	public void setup() throws ClassNotFoundException, SQLException {
		DWBufferHandler.Connect(Name);
		OutStreamConnection  = DWBufferHandler.con;
		running = true;
		
		
		//Sales table
        QueryTrans= OutStreamConnection.prepareStatement(QueryTransaction);
        TransInsert= OutStreamConnection.prepareStatement(UpdateSales);
        
        //Supplier
        QuerySupplierStatement = OutStreamConnection.prepareStatement(QuerySupplier);
        PreparedSupplierStatement = OutStreamConnection.prepareStatement(UpdateSupplier);
        
        //Customer                 
        QueryCustomerStatement = OutStreamConnection.prepareStatement(QueryCustomer);
        PreparedCustomerStatement = OutStreamConnection.prepareStatement(UpdateCustomer);
        
        //Store                 
        QueryStoreStatement = OutStreamConnection.prepareStatement(QueryStore);
        PreparedStoreStatement = OutStreamConnection.prepareStatement(UpdateStore);
        
        //Product
        QueryProductStatement= OutStreamConnection.prepareStatement(QueryProduct);
        PreparedProductStatement = OutStreamConnection.prepareStatement(UpdateProduct);
        
        //Dates
        QueryDateStatement = OutStreamConnection.prepareStatement(QueryDate);
        PreparedDateStatement = OutStreamConnection.prepareStatement(UpdateDate);
        
        //Sales
        QueryTrans = OutStreamConnection.prepareStatement(QueryTransaction);
        TransInsert = OutStreamConnection.prepareStatement(UpdateSales);
	}
	
	public void InsertSales(ArrayList<Sales> ListOfSales) throws SQLException, ParseException {
		Sales TempSale;
		
		for (int i=0; i< ListOfSales.size(); ++i) {
			TempSale = ListOfSales.get(i);
			if(TempSale.Joined) {
				Add_Update_Customer(TempSale.CUSTOMER_ID, TempSale.CUSTOMER_NAME);
				Add_Update_Supplier(TempSale.SUPPLIER_ID, TempSale.SUPPLIER_NAME);
				Add_Update_Product(TempSale.PRODUCT_ID, TempSale.PRODUCT_NAME);
				Add_Update_Date(TempSale.TRANSACTION_DATE);
				Add_Update_Store(TempSale.STORE_ID, TempSale.STORE_NAME);
				
				
				//
				Add_Transaction(TempSale.TRANSACTION_ID,TempSale.PRODUCT_ID, TempSale.SUPPLIER_ID, TempSale.CUSTOMER_ID, TempSale.STORE_ID, TempSale.TRANSACTION_DATE, 
						TempSale.TRANSACTION_QUANTITY, TempSale.TOTAL);
			}			
		}
	}
	
	public void Add_Update_Customer(String ID, String CustomerName) throws SQLException {
		QueryCustomerStatement.setString(1, ID);
		
		ResultSet temp = QueryCustomerStatement.executeQuery();
		
		if(!temp.next()) {
			PreparedCustomerStatement.setString(1, ID);
			PreparedCustomerStatement.setString(2, CustomerName);
			PreparedCustomerStatement.execute();
		}
	}
	
	public void Add_Update_Product(String ID, String ProdName) throws SQLException {
		QueryProductStatement.setString(1, ID);
		
		ResultSet temp = QueryProductStatement.executeQuery();
		
		if(!temp.next()) {
			PreparedProductStatement.setString(1, ID);
			PreparedProductStatement.setString(2, ProdName);
			PreparedProductStatement.execute();
		}
	}
	
	public void Add_Update_Supplier(String ID, String SupplierName) throws SQLException {
		QuerySupplierStatement.setString(1, ID);
		
		ResultSet temp = QuerySupplierStatement.executeQuery();
		
		if(!temp.next()) {
			PreparedSupplierStatement.setString(1, ID);
			PreparedSupplierStatement.setString(2, SupplierName);
			PreparedSupplierStatement.execute();
		}
	}
	
	public void Add_Update_Store(String ID, String StoreName) throws SQLException {
		QueryStoreStatement.setString(1, ID);
		
		ResultSet temp = QueryStoreStatement.executeQuery();
		
		if(!temp.next()) {
			PreparedStoreStatement.setString(1, ID);
			PreparedStoreStatement.setString(2, StoreName);
			PreparedStoreStatement.execute();
		}
	}
	
	public void Add_Update_Date(String TransDate) throws SQLException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	    try {
	        java.util.Date UtilDate = format.parse(TransDate);
	        java.sql.Date SqlDate = new java.sql.Date(UtilDate.getTime());
	        
	        TempCalendar.setTime(SqlDate);
            
            QueryDateStatement.setDate(1, SqlDate);
            
            ResultSet TempResult = QueryDateStatement.executeQuery();
            if(!TempResult.next()) {
            	int Year, Month, Day,Quater=0;
                Year = TempCalendar.get(Calendar.YEAR);
                Month = TempCalendar.get(Calendar.MONTH)+1;
                Day = TempCalendar.get(Calendar.DAY_OF_MONTH);
                
                if (Month <= 3) {
                    Quater = 1;
                } 
                else if (Month <= 6) {
                    Quater = 2;
                }
                else if (Month <= 9) {
                    Quater = 3;
                }
                else if (Month <= 12) {
                    Quater = 4;
                }
 
                PreparedDateStatement.setDate(1, SqlDate);
                PreparedDateStatement.setInt(2, Day);
                PreparedDateStatement.setInt(3, Month);
                PreparedDateStatement.setInt(4, Quater);
                PreparedDateStatement.setInt(5, Year);
                PreparedDateStatement.setInt(6, TempCalendar.get(Calendar.WEEK_OF_YEAR));
                PreparedDateStatement.setString(7, WEEKDAYS[TempCalendar.get(Calendar.DAY_OF_WEEK)]);
                
//                System.out.println("week: "+ TempCalendar.get(Calendar.WEEK_OF_YEAR));
                PreparedDateStatement.execute();
                
                
            }
	        
	        
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	}

	public void Add_Transaction(long ID,String ProdID, String SupID, String CustID,String StoreID, String TransDate, int Qty,double Total) throws SQLException, ParseException {
		
		QueryTrans.setDouble(1, ID);
		
		ResultSet TempResult = QueryTrans.executeQuery();
		if(!TempResult.next()) {
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date UtilDate = format.parse(TransDate);
	        java.sql.Date SqlDate = new java.sql.Date(UtilDate.getTime());
	        
	        TempCalendar.setTime(SqlDate);
	        
			TransInsert.setDouble(1, ID);
			TransInsert.setString(2, ProdID);
			TransInsert.setString(3, SupID);
			TransInsert.setString(4, CustID);
			TransInsert.setString(5, StoreID);
			TransInsert.setDate(6, SqlDate);
			TransInsert.setInt(7, Qty);
			TransInsert.setDouble(8, Total);
			
			System.out.println("Adding Transaction " + ID);
			TransInsert.execute();
		}
		
	}
	
	public void end() {
		DWBufferHandler.CloseConnection(Name);
		OutStreamConnection = null;
		running = false;
		
	}
}
