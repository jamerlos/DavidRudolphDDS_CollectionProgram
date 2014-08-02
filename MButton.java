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
public class MButton extends JComboBox{
    
    public MButton( ){
        
        super( );
    }
  
    /**
     * Adds an array of Items to the item list. This method works only if the JComboBox uses a mutable data model.
     * 
     * @param items  the items to be added to the list
     */
    public void addItems( String[] items ){
        
        for( String item : items )
            addItem( item );    
    }
    
}
