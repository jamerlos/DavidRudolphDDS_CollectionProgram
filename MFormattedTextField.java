package collection;


import javax.swing.JFormattedTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan
 */
public class MFormattedTextField extends JFormattedTextField{
    
    /**
     * Construct a MFormmattedTextField
     */
    public MFormattedTextField(){
        super( );
    }
    
    /**
     * Construct a MFormmattedTextField
     * 
     * @param af 
     */
    public MFormattedTextField( AbstractFormatter af ){
        super( af );
    }
    
    /**
     * Clears the text field
     */
    public void clear( ){
        
        this.setText( "" );
    } 
    
    /**
     * Formats the text in the field to MSAccess format
     * 
     * @return 
     */
    public String getSqlText( ){
        
        return ("'" + this.getText( ) + "'");
    }
}
