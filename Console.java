package collection;


import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan
 */
public class Console extends MInternalFrame{

    MDesktopPane desktop;
    
    ConsoleLog consoleLog;
    
    private JPanel panel;
    
    private JLabel lblTitle;
    
    private JTextArea txtConsole;
    
    private JButton btnClose;
    
    private JScrollPane scrollPane;
    
    public Console( ){
      
        initComponents( );
        builtComponents( );
        builtThis( );
    }
    
    private void initComponents( ){
        
        desktop = MDesktopPane.getInstance( );
        
        consoleLog = ConsoleLog.getInstance( );

        panel = new JPanel( );
        
        lblTitle = new JLabel( "Console" );
        
        txtConsole = new JTextArea( ConsoleLog.retrieveLog( ) );
        
        btnClose = new JButton( "Close" ); 
        
        scrollPane = new JScrollPane( );
    }
    
    private void builtComponents( ){
        
        lblTitle.setOpaque( true );
        lblTitle.setBackground( Color.WHITE );
        lblTitle.setHorizontalAlignment( JLabel.CENTER );
        lblTitle.setFont( new Font( lblTitle.getFont( ).getName( ), Font.BOLD, 30 ) );
        
        scrollPane = new JScrollPane( txtConsole, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ); 
        
        panel.setLayout( new PropLayout( ) );
        panel.add( lblTitle  , "+ | .50, 60" );
        panel.add( scrollPane, "+ | 1.00, 1.00 | 15" );
        panel.add( btnClose  , "+ | 0.30, 35" );
    }
    
    private void builtThis( ){
       
        this.setTitle( "Console" );
        this.add( panel );
        this.setSize( 600, 600 );
        this.setVisible( true );
    }
}
 