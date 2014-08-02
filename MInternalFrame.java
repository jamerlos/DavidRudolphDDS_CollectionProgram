package collection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

public class MInternalFrame extends JInternalFrame{
    
    Dimension screenSize;
    Dimension desktopSize;
    
    Boolean isIconifiable;
    Boolean isClosable;
    Boolean isResizable;
    Boolean isVisible;
    Boolean isMaximum;
            
    String title;

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXX        Constructors         XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    
    /**
     * Construct a MInternalFrame
     */
    public MInternalFrame() {

        this("Untitled", true,true,true,true);
    }
    
    /**
     * Construct a MInternalFrame
     * 
     * @param title Set the title of the Internal Frame 
     */
    public MInternalFrame(String title) {
        
        this( title,true,true,true,true );
    }
    
    /**
     * Construct a MInternalFrame
     * 
     * @param title Set the title of the internal Frame
     * @param isClosable Set whether the internal frame is closable
     * @param isIconifiable Set whether the internal frame is iconifiable
     * @param isResizable Set whether the internal frame is resizable
     * @param isVisible  Set whether the internal frame is visible
     */
    public MInternalFrame( String title, Boolean isClosable, Boolean isIconifiable, Boolean isResizable, Boolean isVisible ){
        
        this.title         = title;
        this.isClosable    = isClosable;
        this.isIconifiable = isIconifiable;
        this.isResizable   = isResizable;
        this.isVisible     = isVisible;
        
        this.builtThis();
        this.runLater();
    }
    
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXX         Built This          XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    
    private void builtThis(){
        
        this.setClosable   ( isClosable    );
        this.setIconifiable( isIconifiable );
        this.setResizable  ( isResizable   );
        this.setVisible    ( isVisible     );    
        this.setTitle      ( title         );
        
        this.desktopSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0, getWidth(desktopSize), getHeight(desktopSize));
        
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);     
    }
    
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXX        Private Class         XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    
    private void setClosable( Boolean isClosable ){
        
        super.setClosable( isClosable );
    }
    
    private void setIconifiable( Boolean isIconifiable ){
        
        super.setIconifiable( isIconifiable );
    }
    
    private void setResizable( Boolean isResizable ){
        
        super.setResizable ( isResizable );
    }
    
    private void setVisible (Boolean isVisible ){
        
        super.setVisible( isVisible );
    }
    
    private static int getWidth(Dimension desktopSize) {
        
            return (int) (desktopSize.width - (desktopSize.width * .3));
    }

    private static int getHeight( Dimension desktopSize)
    {
            return (int) (desktopSize.height - (desktopSize.height * .2));
    }
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXX        Public  Class         XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    
    public void runLater( ){}
    
//    public static Object generateValue( MInternalFrame owner ){
//
//        StringBuilder value = new StringBuilder( );
//
//        if( owner != null )
//            value.append( owner.getValue( ) );
//
//        value.append( "AddSongWizard" );
//
//        return value.toString( );
//    }
    
}
