package collection;


import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan
 */
public class MDesktopPane extends JDesktopPane {
    
    private static MDesktopPane desktopPane = new MDesktopPane();
    
    private MDesktopPane(){
        
    }
    
    public static MDesktopPane getInstance(){
        
        return desktopPane;
    }
    
//    public boolean restoreFrame( Object value ){
//
//        MInternalFrame[] frames = getMFrames( );
//        Object tmpValue = null;
//
//        for( MInternalFrame frame : frames ){
//
//            tmpValue = frame.getValue( );
//
//            if( tmpValue != null && tmpValue.equals( value ) ){
//
//                restoreFrame( frame );
//                return true;
//            }
//        }
//
//        return false;
//    }
    
    public MInternalFrame[] getMFrames( ){

        JInternalFrame[] allFrames = getAllFrames( );
        ArrayList<MInternalFrame> mriFrames = new ArrayList<MInternalFrame>( );

        for( JInternalFrame frame : allFrames ){

            if( frame instanceof MInternalFrame )
                mriFrames.add( (MInternalFrame)frame );
        }

        return mriFrames.toArray( new MInternalFrame[mriFrames.size( )] );
    }
    
    public void restoreFrame( JInternalFrame frame ){
        try{

            frame.setIcon( false );
            frame.setMaximum( false );
            frame.setSelected( true );

        }catch( Exception exception ){
            
            
        }
    }
    
    
    public void addFrame( Component compnt ){
        
        this.add( compnt );
        moveToFront(compnt);
    }
    
}
