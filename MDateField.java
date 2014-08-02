package collection;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class MDateField extends MTextField{
    
    CalendarButton calerdarButton;
    SimpleDateFormat currentFormat;
    String dteFormat;
    JLabel lblCalendar;
    Date date;
    
    private JPopupMenu dateMenu;
    private JMenuItem mnuClear;
    private JMenuItem mnuSelect;

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    
    /**
     * Construct a Date Field
     */
    public MDateField(){
        
        this( null, "MM-dd-yyyy" );
    }
    
    /**
     * Construct a Date Field
     * 
     * @param dteFormat Sets the formats of the date (Default: MMM dd, yyyy)
     */
    public MDateField( String dteFormat ){
        this( null, dteFormat );
    }
    
    /**
     * Construct a Date Field
     * 
     * @param date The date that should initially be displayed
     * @param dteFormat Sets the formats of the date (Default: MMM dd, yyyy)
     */
    public MDateField( Date date, String dteFormat ){
        
        this.date = date;
        this.dteFormat = dteFormat;
        
        initComponents( );
        builtComponents( );
        addEventListeners( );
        builtThis(); 
        
    }
    
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Utility Methods: Instantiation     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX 
    
    private void initComponents( ){
        
        calerdarButton = new CalendarButton();
        currentFormat  = new SimpleDateFormat( dteFormat );
        
        dateMenu  = new JPopupMenu( );
        mnuSelect = new JMenuItem( "Select Date" );
        mnuClear  = new JMenuItem( "Clear" );
    }
     
    private void builtComponents( ){
       
        dateMenu.add( mnuSelect );
        dateMenu.add( mnuClear  );
    }
    
    private void builtThis( ){
        
        this.setEditable( false );
        this.setBackground( Color.WHITE );
        this.add( calerdarButton );
    }
    
    private void addEventListeners(   ){
        
       this.addMouseListener( new MouseAdapter(  ) {
            
             @Override
             public void mousePressed( MouseEvent  ev ) {

                    if (ev.isPopupTrigger()) {
                        
                       dateMenu.show (ev.getComponent (), ev.getX( ), ev.getY( ) );
                    }
             }

             @Override
             public void mouseReleased( MouseEvent ev ) {

                    if ( ev.isPopupTrigger ( )) {
                        
                        dateMenu.show(ev.getComponent(), ev.getX(), ev.getY());
                        
                    }
             }

             @Override
             public void mouseClicked( MouseEvent ev ) {
                 
                 calerdarButton.doClick( );
             }
            
        });

        mnuClear.addActionListener( new ActionListener( ){
            
            @Override
            public void actionPerformed( ActionEvent event ){
                
                setText( null );
            }

        });
        
        mnuSelect.addActionListener( new ActionListener( ){
            
            @Override
            public void actionPerformed( ActionEvent event ){
                
                calerdarButton.doClick( );
            }

        });
    }

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX              public Class              XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX 
    
    /**
     * Sets the format for the calendar field
     * 
     * @param newFormat (Default: MMM dd, yyyy)
     */
    public void setFormat( String dteFormat){
        
        this.dteFormat = dteFormat;     
    }
    
    /**
     * Clear the DateField
     */
    public void clear( ){
        
        this.setText( "" );
    }
    
    /**
     * Sets the date for the date field
     * 
     * @param date initial date to be shown in date field
     */
    public void setDate( String date ){
        try {
            
            this.date = currentFormat.parse( date );
        }
        catch ( ParseException ex ) {
            
            Logger.getLogger( MDateField.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }
    
    public String getSqlDate( ){
        
        return ("'" + this.getText( ) + "'");
    }
    
    public String getDate( ){
        
        return this.getText( );
    }
    
    /**
    * Sets the date for the date field
    * 
    * @param date initial date to be shown in date field
    */
    public void setDate( Date date ){

            this.date = date;
    }
      
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX              Inner Class               XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX    
    
        /** 
         * A CalendarButton is a button that displays a popup calendar (A JCalendarPopup).
         * @author  Don Corley <don@donandann.com>
         * @version 1.4.3
         *  Copyright (C) 2010  Don Corley
         *
         *  This program is free software: you can redistribute it and/or modify
         *  it under the terms of the GNU General Public License as published by
         *  the Free Software Foundation, either version 3 of the License, or
         *  (at your option) any later version.
         *
         *  This program is distributed in the hope that it will be useful,
         *  but WITHOUT ANY WARRANTY; without even the implied warranty of
         *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
         *  GNU General Public License for more details.
         *
         *  You should have received a copy of the GNU General Public License
         *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
         */
        public class CalendarButton extends JButton
            implements PropertyChangeListener, ActionListener
        {

            /**
             * The language property key.
             */
            public static final String LANGUAGE_PARAM = "language";
            /**
             * The name of the date property (defaults to "date").
             */
            String dateParam = JCalendarPopup.DATE_PARAM;
            /**
             * The initial date for this button.
             */
            Date targetDate = null;
            /**
             * The language to use.
             */
            String languageString = null;

            /**
             * Creates new CalendarButton.
             */
            public CalendarButton(){
                
                super();
                // Get current classloader
                ClassLoader cl = this.getClass().getClassLoader();
                // Create icons
                try   {
                    Icon icon  = new ImageIcon(cl.getResource("images/buttons/" + JCalendarPopup.CALENDAR_ICON + ".gif"));
                    this.setIcon(icon);
                } catch (Exception ex)  {
                    this.setText("C");
                }

                this.setName("CalendarButton");

                this.setMargin(JCalendarPopup.NO_INSETS);
                this.setOpaque(false);

                this.addActionListener(this);
                this.setBackground( Color.WHITE );
            }
            
            /**
             * Creates new CalendarButton.
             * @param dateTarget The initial date for this button.
             */
            public CalendarButton(Date dateTarget){
                
                this();
                this.init(null, dateTarget, null);
            }
            
            /**
             * Creates new CalendarButton.
             * @param strDateParam The name of the date property (defaults to "date").
             * @param dateTarget The initial date for this button.
             */
            public CalendarButton(String strDateParam, Date dateTarget){
                
                this();
                this.init(strDateParam, dateTarget, null);
            }
            
            /**
             * Creates new CalendarButton.
             * @param strDateParam The name of the date property (defaults to "date").
             * @param dateTarget The initial date for this button.
             * @param strLanguage The language to use.
             */
            public CalendarButton(String strDateParam, Date dateTarget, String strLanguage){
                
                this();
                this.init(strDateParam, dateTarget, strLanguage);
            }
            
            /**
             * Creates new CalendarButton.
             * @param strDateParam The name of the date property (defaults to "date").
             * @param dateTarget The initial date for this button.
             * @param strLanguage The language to use.
             */
            public void init(String strDateParam, Date dateTarget, String strLanguage){
                
                if (strDateParam == null)
                    strDateParam = JCalendarPopup.DATE_PARAM;
                
                dateParam = strDateParam; 
                targetDate = dateTarget;
                languageString = strLanguage;
            }
            
            /**
             * Get the current date.
             */
            public Date getTargetDate(){
                
                return targetDate;
            }
            
            /**
             * Set the current date.
             */
            public void setTargetDate(Date dateTarget){
                
                targetDate = dateTarget;
            }
            
            /**
             * Get the name of the date property for this button.
             */
            public String getDateParam(){
                
                return dateParam;
            }
            
            /**
             * Get the name of the date property for this button.
             */
            public void setDateParam(String newParam){
                
                dateParam = newParam;
            }
            
            /**
             * Get the language.
             * @return
             */
            public String getLanguage(){
                
                return languageString;
            }
            
            /**
             * Set the language.
             * @param languageString
             */
            public void setLanguage(String languageString){
                    
                this.languageString = languageString;
            }
            
            /**
             * The user pressed the button, display the JCalendarPopup.
             * @param e The ActionEvent.
             */
            public void actionPerformed(ActionEvent e) {
                
                if (e.getSource() == this){
                    
                    Date dateTarget = this.getTargetDate();
                    JCalendarPopup popup = JCalendarPopup.createCalendarPopup(this.getDateParam(), dateTarget, this, languageString);
                    popup.addPropertyChangeListener(this);
                }
            }
            
            /**
             * Propagate the change to my listeners.
             * Watch for date and language changes, so I can keep up to date.
             * @param evt The property change event.
             */
            public void propertyChange(final java.beans.PropertyChangeEvent evt)
            {
                this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());

                if (dateParam.equalsIgnoreCase(evt.getPropertyName())){
                    
                    if (evt.getNewValue() instanceof Date){

                        targetDate = (Date)evt.getNewValue();
                        
                        if( dteFormat != null){
                            
                            SimpleDateFormat newFormat = new SimpleDateFormat( dteFormat );
                            MDateField.this.setText( newFormat.format( targetDate ) );
                        }
                        date = targetDate;
                    }
                }

                if (LANGUAGE_PARAM.equalsIgnoreCase(evt.getPropertyName())){
                    
                    if (evt.getNewValue() instanceof String)
                        languageString = (String)evt.getNewValue();
                }
            }

        }
}