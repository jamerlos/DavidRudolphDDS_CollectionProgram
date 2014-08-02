package collection;


import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan
 */
public class MTextField extends JTextField{
    
    public MTextField( ){
        super( );
    }
    
    /**
     * Clears the TextField
     */
    
    public void clear( ){
        
        this.setText( "" );
    }
    
    /**
     * Returns the text in Sql Format
     * @return 
     */
    public String getSqlText( ){
        
        return ("'" + this.getText( ).replace("'", "''") + "'").toUpperCase( );
    }
    
}
