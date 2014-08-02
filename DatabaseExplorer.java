package collection;

/******************************************************************************
 *                                                                            *
 *                      Class: DatabaseExplorer                               *
 *                                                                            *
 *   Class that display data from the Collection Database                     *
 *                                                                            *
 *****************************************************************************/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;

/**
 * Class that display data from the Collection Database     
 * 
 * @author Juan
 */
public class DatabaseExplorer extends MInternalFrame {

    private static final long serialVersionUID = 2706603240303322258L;

    final private String [] filter = {"PATIENT NAME","ACCOUNT HOLDER"};
    
    private final String ENGLISH_TABLE = "COLLECTION_INFO_ENGLISH_DB";
    private final String SPANISH_TABLE = "COLLECTION_INFO_SPANISH_DB";
    private final String MASTER_TABLE  = "MASTER_COLLECTION_DATABASE";

    private MSAccessDatabase database;

    private JPanel mainPanel;

    private JScrollPane scrollPane;
    
    private JTabbedPane tabbedPane;

    private JMTable masterTable;
    private JMTable englishTable;
    private JMTable spanishTable;
    
    private JPopupMenu collectionMenu;
    private JMenuItem collectionDelete;
    private JMenuItem collectionEdit;
    private JMenuItem collectionNew;
    
    private JPopupMenu englishMenu;
    private JMenuItem mnuEnglishDelete;
    
    private JPopupMenu spanishMenu;
    private JMenuItem mnuSpanishDelete;
    
    private String[] engColumnNames;
    private Object[][] englishData;
    
    private String[] spaColumnNames;
    private Object[][] spanishData;
    
    private String[] masColumnNames;
    private Object[][] masterData;
    
    private JPopupMenu engPopupMenu;
    private JMenuItem mnuEngEdit;
    private JMenuItem mnuEngDelete;
    private JMenuItem mnuEngToMaster;
    
    private JPopupMenu spaPopupMenu;
    private JMenuItem mnuSpaEdit;
    private JMenuItem mnuSpaDelete;
    private JMenuItem mnuSpaToMaster;
    
    private JPopupMenu masPopupMenu;
    private JMenuItem mnuMasDelete;
    
    private MDesktopPane desktop;
    
    Vector <Vector> result;
            
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXX        Constructors       XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        
    public DatabaseExplorer( ){
        
        super( "Search Account",false,false,false,true);
        
        this.initComponents( );
        this.getData( );
        this.builtComponents( );
        this.addEventListeners( );
        this.builtThis( );
    }
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXX        Initialization       XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    
    public void initComponents( ){
        
        database = MSAccessDatabase.getInstance( );
        desktop  = MDesktopPane.getInstance( );
//        database.openConnection();
        
        result = new Vector<Vector>( );
        
        collectionMenu   = new JPopupMenu( );
        collectionNew    = new JMenuItem( "New" );
        collectionEdit   = new JMenuItem( "Edit" );
        collectionDelete = new JMenuItem( "Delete" );
    
        englishMenu   = new JPopupMenu( );
        mnuEnglishDelete = new JMenu( "Delete" );
    
        spanishMenu   = new JPopupMenu( );
        mnuSpanishDelete = new JMenuItem( "Delete" );
        
        tabbedPane = new JTabbedPane( );

        mainPanel   = new JPanel();
        
        engPopupMenu   = new JPopupMenu( );
        mnuEngEdit     = new JMenuItem( "Edit Account"    );
        mnuEngDelete   = new JMenuItem( "Delete Account"  );
        mnuEngToMaster = new JMenuItem( "Enter to Master" );
        
        spaPopupMenu   = new JPopupMenu( );
        mnuSpaEdit     = new JMenuItem( "Edit Account"    );
        mnuSpaDelete   = new JMenuItem( "Delete Account"  );
        mnuSpaToMaster = new JMenuItem( "Enter to Master" );
        
        masPopupMenu = new JPopupMenu( );
        mnuMasDelete = new JMenuItem( "Delete Account" );

        scrollPane = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXX        Built Components     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

  public void getData( ){
      
            String query1 = "select [ID],[PATIENT NAME],[ACCOUNT HOLDER],[OFFICE],[HOME PHONE],[MOBILE PHONE],"
                    + "[CONTRACT 1],[CONTRACT TYPE 1],[CONTRACT 2],[CONTRACT TYPE 2],[SETTLED AMOUNT],[SETTLED REASON],"
                    + "[AMOUNT PAID],[AMOUNT OWE],[LAST DATE OF SERVICE],[FIRST NOTICE SENT DATE],[FINAL NOTICE SENT DATE],"
                    + "[RESPOND DATE] "
                    + "from COLLECTION_INFO_ENGLISH_DB "
                    + "order by [ID]";
            
            Vector <Vector> result    = database.executeRetrieve( query1 );
            Vector<Vector> columnMeta = new Vector<Vector>();

            columnMeta = result.get(0);

            engColumnNames = columnMeta.toArray(new String[columnMeta.size()]);
            
            Vector <Vector> dataRow = new Vector<Vector>();

            englishData = new Object[result.size()-1][columnMeta.size()];
            
            for( int index = 1; index < result.size(); index++ ){ 

                Vector temp = result.get(index);
                
                for( int i = 0; i < temp.size(); i++){
                    
                    if(temp.get(i) == null)
                        englishData[index - 1][i] = "";
                    
                    else
                        englishData[index - 1][i] = temp.get(i);   
                }
            }
            
            String query2 = "select [ID],[PATIENT NAME],[ACCOUNT HOLDER],[OFFICE],[HOME PHONE],[MOBILE PHONE],"
                    + "[CONTRACT 1],[CONTRACT TYPE 1],[CONTRACT 2],[CONTRACT TYPE 2],[SETTLED AMOUNT],[SETTLED REASON],"
                    + "[AMOUNT PAID],[AMOUNT OWE],[LAST DATE OF SERVICE],[FIRST NOTICE SENT DATE],[FINAL NOTICE SENT DATE],"
                    + "[RESPOND DATE] "
                    + "from COLLECTION_INFO_SPANISH_DB "
                    + "order by [ID]";

            result = database.executeRetrieve( query2 );
            columnMeta = result.get(0);
            
            spaColumnNames = columnMeta.toArray( new String[columnMeta.size( )] );
            
            spanishData = new Object[result.size( ) - 1][columnMeta.size( )];
            
            for( int index = 1; index < result.size(); index++ ){ 

                Vector temp = result.get(index);
                
                for( int i = 0; i < temp.size(); i++){
                    
                    if(temp.get(i) == null)
                        spanishData[index - 1][i] = "";
                    
                    else
                        spanishData[index - 1][i] = temp.get(i);   
                }
            }
            
            String query3 = "select * from MASTER_COLLECTION_DATABASE "
                    + "order by [ID]";
            result = database.executeRetrieve( query3 );
            columnMeta = result.get(0);
            
            masColumnNames = columnMeta.toArray( new String[columnMeta.size( )] );
            
            masterData = new Object[result.size( ) - 1][columnMeta.size( )];
            
            for( int index = 1; index < result.size(); index++ ){ 

                Vector temp = result.get(index);
                
                for( int i = 0; i < temp.size(); i++){
                    
                    if(temp.get(i) == null)
                        masterData[index - 1][i] = "";
                    
                    else
                        masterData[index - 1][i] = temp.get(i);   
                }
            }
        }
   
    public void builtComponents( ){
        
        englishTable = new JMTable( engColumnNames, englishData, filter );
        spanishTable = new JMTable( spaColumnNames, spanishData, filter );
        masterTable  = new JMTable( masColumnNames, masterData, filter );
        
        collectionMenu.add( collectionNew    );
        collectionMenu.addSeparator( );
        collectionMenu.add( collectionEdit   );
        collectionMenu.add( collectionDelete );
        
        englishMenu.add( mnuEnglishDelete );
        
        spanishMenu.add( mnuSpanishDelete );
        
        engPopupMenu.add( mnuEngEdit     );
        engPopupMenu.add( mnuEngDelete   );
        engPopupMenu.addSeparator( );
        engPopupMenu.add( mnuEngToMaster );
        
        spaPopupMenu.add( mnuSpaEdit     );
        spaPopupMenu.add( mnuSpaDelete   );
        spaPopupMenu.addSeparator( );
        spaPopupMenu.add( mnuSpaToMaster );
        
        masPopupMenu.add( mnuMasDelete );

        englishTable.setAutoResize( JTable.AUTO_RESIZE_OFF );
        spanishTable.setAutoResize( JTable.AUTO_RESIZE_OFF );
        
        englishTable.setPopup( engPopupMenu );
        spanishTable.setPopup( spaPopupMenu );
        masterTable .setPopup( masPopupMenu );
        
        tabbedPane.add( "English"        , englishTable );
        tabbedPane.add( "Spanish"        , spanishTable );	
        tabbedPane.add( "Master Database", masterTable  );
    }

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXX          Build This          XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        
    public void builtThis(){
        
        this.setClosable(true);
        this.setTitle(title);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        scrollPane.setViewportView( tabbedPane );

        this.add(scrollPane);
    }
        
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXX       AddEventListeners      XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        
    public void addEventListeners( ){
        
        mnuEngDelete.addActionListener( new ActionListener( ){

            @Override
            public void actionPerformed( ActionEvent event ) {
                
                int option = JOptionPane.showOptionDialog( DatabaseExplorer.this,
                        "Are you sure you want to delete the selected account?",
                        "Verify Delete",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        new Object[]{"YES","NO"},
                        "NO" );
                
                if ( option == 1 )
                    return;
                
                String accountID = englishTable.getSelectedValue(0);
                
                String query = "delete from " + ENGLISH_TABLE + " WHERE [ID] = " + accountID;
                
                Boolean result = database.execute( query );
                
                getData( );
                englishTable.update( engColumnNames, englishData, filter );
            } 
        });
        
        mnuEngEdit.addActionListener( new ActionListener( ){
           
            @Override
            public void actionPerformed( ActionEvent event ){
                
                String accountID = englishTable.getSelectedValue(0);
                
                AccountEdit accountEdit = new AccountEdit( accountID, ENGLISH_TABLE );
                accountEdit.setSize( desktop.getSize() );
                desktop.addFrame( accountEdit );
            }
        });
        
        mnuEngToMaster.addActionListener( new ActionListener( ){
        
            @Override
            public void actionPerformed( ActionEvent event ){
                
                String patient = SqlFormat.format( englishTable.getSelectedValue(1) );
                String debtor  = SqlFormat.format( englishTable.getSelectedValue(2) );
                String office  = SqlFormat.format( englishTable.getSelectedValue(3) );
                String amount  = SqlFormat.format( englishTable.getSelectedValue(12));
                String agency  = SqlFormat.format( "PCR & ASSOCIATES" );
                
                String query = "insert into " + MASTER_TABLE 
                        + "([PATIENT],[DEBTOR],[OFFICE],[PLACEMENT DATA],[PLACEMENT AMOUNT],[COLLECTION AGENCY]) "
                        + "value (" + patient + "," + debtor + "," + office + "," + amount + "," + agency + ");";
            }
        });
        
        mnuSpaDelete.addActionListener( new ActionListener( ){

            @Override
            public void actionPerformed(ActionEvent event) {
                
                int option = JOptionPane.showOptionDialog( DatabaseExplorer.this,
                        "Are you sure you want to delete the selected account?",
                        "Verify Delete",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        new Object[]{"YES","NO"},
                        "NO" );
                
                if ( option == 1 )
                    return;
                
                String accountID = spanishTable.getSelectedValue(0);
                
                String query = "delete from " + SPANISH_TABLE + " WHERE [ID] = " + accountID;
                
                Boolean result = database.execute( query );
                
                getData( );
                spanishTable.update( spaColumnNames, spanishData, filter );
            } 
        });
        
        mnuMasDelete.addActionListener( new ActionListener( ){

            @Override
            public void actionPerformed(ActionEvent event) {
                
                int option = JOptionPane.showOptionDialog( DatabaseExplorer.this,
                        "Are you sure you want to delete the selected account?",
                        "Verify Delete",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        new Object[]{"YES","NO"},
                        "NO" );
                
                if ( option == 1 )
                    return;
                
                String accountID = masterTable.getSelectedValue(0);
                
                String query = "delete from " + MASTER_TABLE + " WHERE [ID] = " + accountID;
                
                Boolean result = database.execute( query );
                
                getData( );
                masterTable.update( masColumnNames, masterData, filter );
            } 
        });
        
        mnuSpaToMaster.addActionListener( new ActionListener( ){
        
            @Override
            public void actionPerformed( ActionEvent event ){
                
                String patient = SqlFormat.format( spanishTable.getSelectedValue(1) );
                String debtor  = SqlFormat.format( spanishTable.getSelectedValue(2) );
                String office  = SqlFormat.format( spanishTable.getSelectedValue(3) );
                String amount  = SqlFormat.format( spanishTable.getSelectedValue(12));
                String agency  = SqlFormat.format( "PCR & ASSOCIATES" );
                
                String query = "insert into " + MASTER_TABLE 
                        + "([PATIENT],[DEBTOR],[OFFICE],[PLACEMENT DATA],[PLACEMENT AMOUNT],[COLLECTION AGENCY]) "
                        + "value (" + patient + "," + debtor + "," + office + "," + amount + "," + agency + ");";
            }
        }); 
    }
    
    class DataInput{
        
        DataInput( ){
            
        }
    }
}
