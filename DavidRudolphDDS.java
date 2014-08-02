package collection;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan
 */
public class DavidRudolphDDS extends JFrame {
    
//    MSAccessDatabase database; ///temporarily

    private static MDesktopPane desktop;
    static private DatabaseExplorer databaseExplorer;
    static private JButton collectionButton;
    static private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static private Dimension desktopSize;
    static private GridLayout mainPanelLayout = new GridLayout(2,0);
    static private GridLayout lowerPanelLayout = new GridLayout(0,3);
    
    static int MIN_WIDTH  = 1300;
    static int MIN_HEIGHT = 700;
	
    //Menu Bar variables
    private static MMenuBar menuBar;

    static private JPanel mainPanel;
    static private JPanel lowerPanel;
    
    private String user;
	
    public DavidRudolphDDS( String user ){
        
        this.user = user;

        this.setTitle( "David J. Rudolph, DDS");
        this.initComponents();
        this.builtComponents();
        this.builtThis();
    }
        
    public void initComponents(){
        
//        database = MSAccessDatabase.getInstance( );//temporarily
        collectionButton = new JButton("Collection"); 

        databaseExplorer = new DatabaseExplorer();

        desktop    = MDesktopPane.getInstance();
        mainPanel  = new JPanel();
        lowerPanel = new JPanel();

        menuBar = new MMenuBar();
    }

   private void builtComponents(){

        desktopSize = desktop.getSize();

        //Add components to Menu Bar


        lowerPanel.setLayout(lowerPanelLayout);
        lowerPanel.add(new JLabel(""));
        lowerPanel.add(collectionButton);

        mainPanel.setLayout(mainPanelLayout);
        mainPanel.add(new JLabel("DAVID J RUDOLPH DDS INC"));
    }

    private void builtThis(){	
        
//        database.openConnection(); //temporarily
        this.setJMenuBar(menuBar);
        this.add(desktop);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize( new Dimension( Math.min(1600,getWidth( screenSize.width ) ),Math.min( 900, getHeight( screenSize.height ) ) ) );
        this.setVisible(true);
        this.setLocationRelativeTo(null);	
        this.setContentPane(desktop);

        databaseExplorer.setSize( desktop.getSize() );
        databaseExplorer.setClosable(false);
        databaseExplorer.setResizable(false);
        desktop.addFrame(databaseExplorer);

        desktop.setDragMode( JDesktopPane.OUTLINE_DRAG_MODE );	
    }

    public static int getWidth(int width){
        
        int newWidth = (int) ( (width) - (width * 0.30) );
        
        if (newWidth > MIN_WIDTH )
            return newWidth;
        
        else
            return MIN_WIDTH;
    }

    public static int getHeight(int height){
        
        int newHeight = (int) ( ( height - (height * 0.23 ) ) );
        
        if( newHeight > MIN_HEIGHT )
            return newHeight;
        
        else
            return MIN_HEIGHT;
    }
	
    public void internalFrameClosing(InternalFrameEvent e) {
		
        mainPanel.setVisible(true);
    }
}
