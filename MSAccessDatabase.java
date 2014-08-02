package collection;

/******************************************************************************
 *                                                                            *
 *                      Class: MSAccessDatabase                               *
 *                                                                            *
 *   Establish connection and provide methods for MSAccess Database           *                                                                         *
 *                                                                            *
 *****************************************************************************/

import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Establish connection and provide methods for MSAccess Database  
 * 
 * @author Juan
 */
public class MSAccessDatabase {
    
    private static MSAccessDatabase database = new MSAccessDatabase( );
    private String username;
    private String password;
    
    ConsoleLog consoleLog;
    
    private String dataSource = "Collection";
    private String dbURL = "jdbc:odbc:" + dataSource;
    private ExceptionLog log = new ExceptionLog();
    Connection connection;
    Statement statement;
    
    private MSAccessDatabase( ){
        
        
    }
    
    public boolean establishConnection( String username, String password ){
        
        this.username = username;
        this.password = password;
        
        boolean access = openConnection( );
        
        return access;
    }
    
    public boolean openConnection( ){
        
        consoleLog = ConsoleLog.getInstance( );
        
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            connection = DriverManager.getConnection(dbURL,"","");
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return true;
        } 
        catch (Exception ex) {
            
            Logger.getLogger(MSAccessDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
    
    public void closeConnection(){
        try {
            statement.close();
            connection.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(MSAccessDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initInstance( ){
        
        database = new MSAccessDatabase( );
    }
    
    public boolean execute( String query ){
        
        try{
            
            statement.execute( query );
            return true;
        }
        catch( SQLException exception ){
            
            String error = "=====SQL ERROR===== "
                + "\n>> Query: "  + query + ""
                + "\n>> Exception: " + exception 
                + "\n>> Location: @ MSAccessTrans.java >> execute(String query)";
            
            log.insertSQLLog( error );
            return false;
        }
        
    }
    
    public String executeInsert( String query ){
        
    	try {
    		statement.execute( query );
                return "Successfuly Entered";
	} 
        catch ( Exception exception ) {
            
            String error = "=====SQL ERROR===== "
                    + "\n>> Query: "  + query + ""
                    + "\n>> Exception: " + exception 
                    + "\n>> Location: @ MSAccessTrans.java >> executeInsert(String query)";
            
            log.insertSQLLog( error );
            return "Error: Check SQL Log for Details";
	}  	
    }
    
    public Vector <Vector> executeRetrieve( String query ){
        Vector<Vector> data = new Vector<Vector>();
        ResultSetMetaData metaData = null;
    	
    	try {
    		statement.execute( query );
		ResultSet rs = statement.getResultSet();
                metaData = rs.getMetaData();
                
                int columnCount = metaData.getColumnCount();

                Vector columnNames  = new Vector();
        	for(int i = 1; i <= columnCount; i++)
        	{
                    columnNames.addElement(metaData.getColumnName(i));
        	}
                
                data.addElement(columnNames);

                while ( rs.next() )
                {
                    Vector row = new Vector( columnCount );

                    for ( int i = 1; i <= columnCount; i++ )
                    {
                        row.addElement( rs.getObject(i) );
                    }
                    data.add( row );    
                }
                
	    	return data;
	} 
        catch ( Exception exception ) {

            System.out.println( "Error: " + exception );  
            
            String error = "=====SQL ERROR===== "
                    + "\n>> Query: "  + query + ""
                    + "\n>> Exception: " + exception 
                    + "\n>> Location: @ MSAccessTrans.java >> executeRetrieve(String query)";
            
            log.insertSQLLog( error );
            
            return data;
	}  	
    }
    
    public boolean verifyAccess( String username, String password ){
        try {
            
            String query = "Select * from ACCESS_USER where [USER] = '" + username + "' and [PASSWORD] = '" + password + "'";
            statement.execute( query );
            
            ResultSet result = statement.getResultSet( );
            
            return result.first( );
            
        } catch (SQLException ex) {
            
            JOptionPane.showMessageDialog( null, ex,"Inane error",
                        JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    
    @SuppressWarnings("rawtypes")
    public Vector <Vector> getData(ResultSet result)
    {
        Vector<Vector> data = new Vector<Vector>();
    	
    	try
    	{
    		ResultSet rs  = result;
    		ResultSetMetaData metaData = rs.getMetaData();
    		
        	int columnCount = metaData.getColumnCount();
        	
                Vector columnNames  = new Vector();
        	for(int i = 1; i <= columnCount; i++)
        	{
        		columnNames.addElement(metaData.getColumnName(i));
        	}
                data.addElement(columnNames);
                
                while ( rs.next() )
                {
                    Vector row = new Vector( columnCount );

                    for ( int i = 1; i <= columnCount; i++ )
                    {
                        row.addElement( rs.getObject( i ) );
                    }
                    data.addElement( row );    
                }
            
    		return data; 
    	}catch( Exception e )
    	{
    		return null;
    	}

    }
    
    public Vector getRows( ResultSet result )
    {
    	Vector data= new Vector( );
    	
    	try
    	{
            ResultSet rs  = result;
            ResultSetMetaData metaData = rs.getMetaData( );

            int columnCount = metaData.getColumnCount( );
    		
            while(  rs.next( ) )
            {
                Vector row = new Vector( columnCount );
                
                for ( int i = 1; i <= columnCount; i++ )
                {
                    row.addElement( rs.getObject( i ) );
                }
                data.addElement( row );                
            }   		
            
            return data;
    	}
    	catch ( Exception exception ) //insert exception error into log file
    	{
            System.out.println( "Error: " + exception );  
            
            String error = "=====SQL ERROR===== "
                + "\n>> Result: "  + result.toString( ) + ""
                + "\n>> Exception: " + exception 
                + "\n>> Location: @ MSAccessTrans.java >> executeRetrieve(String query) >> Line232\n";
            
            log.insertSQLLog( error ); 
            
            return data;
    	}
    }
    
        public static MSAccessDatabase getInstance(){
        
        return database;
    }
}

