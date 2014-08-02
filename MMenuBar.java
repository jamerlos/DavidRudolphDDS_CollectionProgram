package collection;

/******************************************************************************
 *                                                                            *
 *                      Class: MMenuBar                                       *
 *                                                                            *
 *   A custom Menu Bar                                                        *                                                                         *
 *                                                                            *
 *****************************************************************************/

import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * A custom Menu Bar   
 * 
 * @author Juan
 */
public class MMenuBar extends JMenuBar{
    
    private static JMenu fileMenuOption;
    private static JMenu helpMenuOption;
    private static JMenu toolMenuOption;

    public static JMenuItem quit;
    public static JMenuItem console;
    public static JMenuItem newAccounts;
    public static JMenuItem helpContent;
    public static JMenuItem about;
    
    MDesktopPane desktop;
    
    public MMenuBar( ){
        
        initComponents( );
        builtComponents( );
        builtThis( );
        addEventListeners( );
    }
    
    private void initComponents( ){
        
        desktop = MDesktopPane.getInstance( );

        fileMenuOption = new JMenu( "File"  );
        toolMenuOption = new JMenu( "Tools" );
        helpMenuOption = new JMenu( "Help"  );

        newAccounts = new JMenuItem( "New Accounts"  );
        console     = new JMenuItem( "Console"       );
        quit        = new JMenuItem( "Quit"          );
        helpContent = new JMenuItem( "Help"          );
        about       = new JMenuItem( "About"         );
    }
    
    private void builtComponents( ){

        fileMenuOption.add( newAccounts );
        fileMenuOption.addSeparator( );
        fileMenuOption.add( quit        );
        
        helpMenuOption.add( console    );
        helpMenuOption.add(helpContent );
        helpMenuOption.add(about       );
    }
    
    private void builtThis( ){
        
        this.add( fileMenuOption );
        this.add( toolMenuOption );
        this.add( helpMenuOption );
    }
    
    private void addEventListeners( ){

        newAccounts.addActionListener( new ActionListener( ) {

            @Override
            public void actionPerformed( java.awt.event.ActionEvent evt ) {

                AccountEdit account1 = new AccountEdit( );
                account1.setSize( desktop.getSize() );
                desktop.addFrame( account1 );
            }
        });
        
        console.addActionListener( new ActionListener( ) {

            @Override
            public void actionPerformed( java.awt.event.ActionEvent evt ) {
               
               Console console = new Console( );
               desktop.addFrame( console );
               
            }
        });
        
       quit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
                int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to quit?","Warning", JOptionPane.YES_OPTION );
                
                if (dialogResult == JOptionPane.YES_OPTION )
                    System.exit(0);
            }
        });
    }
    
    
}
