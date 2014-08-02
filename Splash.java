package collection;

/******************************************************************************
 *                                                                            *
 *                      Class: Splash                                         *
 *                                                                            *
 *   Initial Login Dialog to access program                                   *                                                                         *
 *                                                                            *
 *****************************************************************************/

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 *
 * Initial Login Dialog to access program
 * 
 * @author Juan
 */
public class Splash extends JFrame{
    
    MSAccessDatabase database;
    private JLabel lblTitle;
    private JLabel lblUser;
    private JLabel lblPassword;
    
    private JPanel panel;
    
    private JTextField txtUser;
    private JPasswordField txtPassword;
    
    private JButton btnVerify;
    
    public Splash( ){
        
        this.initComponents( );
        this.builtComponents( );
        this.addEventListeners( );
        this.builtThis( );
    }
    
    private void initComponents( ){
        
        
        database = MSAccessDatabase.getInstance( );

        panel = new JPanel( );
        
        lblTitle    = new JLabel( "David J Rudolph DDS Inc" );
        lblUser     = new JLabel( "Username" );
        lblPassword = new JLabel( "Password" );
        
        txtUser     = new JTextField( );
        txtPassword = new JPasswordField( );
        
        btnVerify   = new JButton( "Enter" ); 
    }
    
    private void builtComponents( ){
        
        lblTitle.setOpaque( true );
        lblTitle.setBackground( Color.WHITE );
        lblTitle.setHorizontalAlignment( JLabel.CENTER );
        lblTitle.setFont( new Font( lblTitle.getFont( ).getName( ), Font.BOLD, 20 ) );
        
        panel.setLayout( new PropLayout( ) );
        panel.add( lblTitle    , "+ | 1.00, 50"       );
        panel.add( lblUser     , "+ | 0.20, 25 | 40"  );
        panel.add( txtUser     , "  | 0.80, 25"       );
        panel.add( lblPassword , "+ | 0.20, 25"       );
        panel.add( txtPassword , "  | 0.80, 25"       );
        panel.add( btnVerify   , "+ | 0.30, 25"       ); 
    }
 
    private void builtThis( ){
        
        this.setTitle( "Login" );
        this.add( panel );
        this.setSize( 400, 250 );
        this.setVisible( true );
        this.setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        this.setLocationRelativeTo(null);
    }
    
    private void addEventListeners( ){
        
        btnVerify.addActionListener( new ActionListener( ) {
            
            @Override
             public void actionPerformed( ActionEvent event ){
                 
              String username = txtUser.getText( ).toUpperCase( );
              char[] password = txtPassword.getPassword( );
              
//              boolean access = database.openConnection( );
              boolean access = database.establishConnection( username, new String( password ) );
              
              
              /**
               * NOT YET WORKING, future release.
               */
              if ( !access ){
                  
                new DavidRudolphDDS( "admin" );
                dispose( );
              }

//                JOptionPane.showMessageDialog( null, "Username and Password do not match. Please Try Again","Access Denied",
//                                                JOptionPane.ERROR_MESSAGE);
              else{

                new DavidRudolphDDS( "admin" );
                dispose( );
              }
             }
             
        });
        
//        txtPassword.addKeyListener( new KeyListener ( ) {
//            
//            public void keyPressed( KeyEvent e){
//                
//                if(e.getKeyChar( ) == KeyEvent.VK_ENTER ){
//                    
//                    btnVerify.doClick( );
//                }
//            }
//
//            @Override
//            public void keyTyped(KeyEvent ke) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void keyReleased(KeyEvent ke) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
        
    }
}
