package collection;

/******************************************************************************
 *                                                                            *
 *                      Class: JM Table                                       *
 *                                                                            *
 *   A custom interface which implements JTable to be utilized by MSACCESS    *                                                                         *
 *                                                                            *
 *****************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 *  A custom interface which implements JTable to be utilized by MSACCESS
 * 
 * @author Juan
 */
public class JMTable extends JPanel{
    
    private boolean DEBUG = false;
    private JTable table;
    private JTextField txtFilter;
    private TableRowSorter<TableModel> sorter;
    private TableModel model;
    private JPanel searchPanel;
    private JLabel lblSearch;
    private JComboBox cboFilter;
    private JScrollPane scrollPane;
    private JPopupMenu popupMenu;
    
    private String[] columnNames;
    private String[] filter;
    private Object[][] data;
    
    private JButton btnRefresh;
    
    /**
     * Construct a new JMTable
     * 
     * @param columnNames The metadata of the columns
     * @param data The data of the table
     * @param filter The column in which the table can be filtered
     */
    public JMTable( String[] columnNames, Object[][] data, String[] filter ) {
        
        super();
        
        this.columnNames = columnNames;
        this.data = data;
        this.filter = filter;
        
        initComponents();
        builtComponents();
        addEventListeners();
        searchPanelatTable();
        builtThis();    
    }
    
    public void initComponents(){
        
        model     = new TableModel();
        sorter    = new TableRowSorter<TableModel>(model);
        table     = new JTable(model);
        cboFilter = new JComboBox();
        
        searchPanel = new JPanel(new SpringLayout());
        
        lblSearch = new JLabel("Search:", SwingConstants.TRAILING);
        
        txtFilter = new JTextField();
        
        btnRefresh = new JButton("Refresh");
    }
    
    public void builtComponents(){
        
        table.setRowSorter(sorter);
//        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid( true );
        table.setGridColor( Color.GRAY );
        
        lblSearch.setLabelFor(txtFilter);
        lblSearch.setHorizontalAlignment(JLabel.LEADING);
        
        for ( int index = 0;index < filter.length;index++ )
            cboFilter.addItem( filter[index] );
        
        scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       
        searchPanel.setLayout( new PropLayout() );
        searchPanel.add(lblSearch, "+ | 0.05, 1.00" );  
        searchPanel.add(txtFilter, "  | 0.65, 1.00" );
        searchPanel.add(cboFilter, "  | 0.15, 1.00" );
        searchPanel.add(btnRefresh,"  | 0.15, 1.00" );
    }
    
    public void builtThis(){
        
        this.setLayout( new PropLayout() );
        this.add( scrollPane , "+ | 1.00, 0.90" );
        this.add( searchPanel, "+ | 1.00, 0.10" );
    }
    
    public void addEventListeners(){
        
        txtFilter.getDocument().addDocumentListener( new DocumentListener() {
                    
            @Override
            public void changedUpdate(DocumentEvent e) {

                newFilter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {

                newFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                newFilter();
            }
        });
        
        btnRefresh.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed( ActionEvent event){
                
                model.fireTableDataChanged();
            }
        });
        
        table.addMouseListener( new MouseAdapter( ){

            @Override
            public void mouseReleased( MouseEvent  event ){

                int rowPoint = table.rowAtPoint( event.getPoint( ) );

                if( rowPoint >= 0 && rowPoint < table.getRowCount( ) )
                    table.setRowSelectionInterval( rowPoint, rowPoint );

                else
                    table.clearSelection();

                int rowIndex = table.getSelectedRow( );

                if( rowIndex < 0 )
                    return;

                if( event.isPopupTrigger( ) && event.getComponent( ) instanceof JTable ){

                    popupMenu.show( event.getComponent( ), event.getX( ), event.getY( ) );
                }
            }
        });
    }
    /**
     * Sets the table's auto resize mode when the table is resized. For further 
     * information on how the different resize modes work
     * 
     * @param 
     * mode - One of 5 legal values: AUTO_RESIZE_OFF, 
     * AUTO_RESIZE_NEXT_COLUMN, 
     * AUTO_RESIZE_SUBSEQUENT_COLUMNS, 
     * AUTO_RESIZE_LAST_COLUMN, 
     * AUTO_RESIZE_ALL_COLUMNS 
     */
    public void setAutoResize( int i ){
        
        table.setAutoResizeMode( i );
    }
    
    /**
     * Updates Table
     * 
     * @param columnNames
     * @param data
     * @param filter 
     */
    public void update( String[] columnNames, Object[][] data, String[] filter ){
        
        this.columnNames = columnNames;
        this.data        = data;
        this.filter      = filter;
        model.fireTableDataChanged();
    }
    
    public String getSelectedValue(int index){
        
        int row = table.convertRowIndexToModel( table.getSelectedRow() );
        int column = index;
        
        String value = table.getModel().getValueAt( row, column ).toString();
        
        return value;
    }
 
    private void newFilter() {
        
        RowFilter<TableModel, Object> rf = null;

        try {
            String columnFilter = cboFilter.getSelectedItem().toString();
            
            int columnIndex = table.getColumnModel().getColumnIndex(columnFilter);
            
            rf = RowFilter.regexFilter(txtFilter.getText().toUpperCase(), columnIndex);
        } 
        catch (java.util.regex.PatternSyntaxException e) {
            return;
        }

        sorter.setRowFilter(rf);
    }
    
    public void fireTableDataChange(){
        
        model.fireTableDataChanged();
    }
    
    public void setData(Object[][] data){
        
        this.data = data;
        
    }
    
    public void searchPanelatTable(){
        
        final TableColumnModel columnModel = table.getColumnModel();

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer( );
        centerRenderer.setHorizontalAlignment( JLabel.LEFT );

        for (int column = 0; column < table.getColumnCount(); column++) 
        {
            int mininumWidth = 80; 

            for ( int row = 0; row < table.getRowCount( ); row++ )
            {
                TableCellRenderer renderer = table.getCellRenderer( row, column );
                Component comp = table.prepareRenderer( renderer, row, column );
                mininumWidth = Math.max( comp.getPreferredSize( ).width, mininumWidth );
            }

            columnModel.getColumn( column ).setPreferredWidth( mininumWidth );
            table.getColumnModel( ).getColumn( column ).setCellRenderer( centerRenderer );
        }
    }
    
    /**
     * Adds a JPopupMenu cmpt to the table
     * 
     * @param popupMenu a JPopupMenu component
     */
    public void setPopup( JPopupMenu popupMenu ){
        
        this.popupMenu = popupMenu;
    }
 
    class TableModel extends AbstractTableModel {
        
        public int getColumnCount() {
            return columnNames.length;
        }
 
        public int getRowCount() {
            return data.length;
        }
 
        public String getColumnName(int col) {
            return columnNames[col];
        }
 
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
 

        public Class getColumnClass(int c) {
            
            return getValueAt(0, c).getClass();
        }
 
        public boolean isCellEditable(int row, int col) {

            if (col < 2) 
                return false;

            else 
                return true;
        }
 
        public void setValueAt(Object value, int row, int col) {
            
            if (DEBUG) {
                
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
                                   + value.getClass() + ")");
            }
 
            data[row][col] = value;
            
            fireTableCellUpdated(row, col);
 
            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }
 
        private void printDebugData() {
            
            int numRows = getRowCount();
            int numCols = getColumnCount();
 
            for (int i=0; i < numRows; i++) {
                
                System.out.print("    row " + i + ":");
                
                for (int j=0; j < numCols; j++)
                    System.out.print("  " + data[i][j]);
                
                System.out.println();
            }
            
            System.out.println("--------------------------");
        }
    }   
}
