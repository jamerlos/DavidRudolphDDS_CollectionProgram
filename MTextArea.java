package collection;


import javax.swing.JTextArea;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan
 */
public class MTextArea extends JTextArea{
    
    public MTextArea( ){
        super( );
    }
    
    public MTextArea( String str ){
        super( str );
    }
    
    public void clear( ){
        
        this.setText( "" );
    }
    
    public String getSqlText( ){
        
        String text = "'" + this.getText( ) + "'";
        
        return text;
    }
    
}
