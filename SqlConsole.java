package collection;


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
public class SqlConsole extends MInternalFrame{
    
    private JTextArea consoleArea;
    MDesktopPane desktop;
    private JPanel panel;
    
    public SqlConsole( ){
        
    }
    
    private void initComponents( ){
        
        consoleArea = new JTextArea( );
        desktop = MDesktopPane.getInstance();
        panel = new JPanel( );
    }
    
    private void builtComponents( ){
        
    }
    
    private void buitThis( ){
        
    }
    
}
