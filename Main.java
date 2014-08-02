package collection;


import javax.swing.UIManager.*;
import javax.swing.UIManager;

public class Main {
    
    static MSAccessDatabase database = MSAccessDatabase.getInstance();

    public static void main( String[] args ){

        setTheme( );
        splash( );
    }

    private static void setTheme( ){

        try {
            for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels( ) ) {

                if ( "Nimbus".equals( info.getName( ) ) ) {

                    UIManager.setLookAndFeel( info.getClassName( ) );
                    break;
                }
            }
        } 
        catch ( Exception e ) {
        // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }

    private static void splash( ){
        
//        database.openConnection();
//        new DavidRudolphDDS( "admin" );
       new Splash( ); 
    }
        
        
	

}
