package collection;


import javax.swing.JComboBox;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan
 */
public class MComboBox extends JComboBox{

    public MComboBox( ){
        super( );
    }
    
    public MComboBox( Object[] item ){
       super( item );
    }
    
    public String getSelectedSqlItem( ){
        
        return "'" + this.getSelectedItem( ) + "'";
        
    }
    
}
