package collection;

/******************************************************************************
 *                                                                            *
 *                      Class: AccountEdit                                    *
 *                                                                            *
 *   Interface to enter new accounts                                          *
 *                                                                            *
 *****************************************************************************/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.MaskFormatter;

/**
 * Interface to enter new accounts
 * 
 * @author Juan
 */
public class AccountEdit extends MInternalFrame{
    
    private MSAccessDatabase  database;
    
    private final String ENGLISH_TABLE = "COLLECTION_INFO_ENGLISH_DB";
    private final String SPANISH_TABLE = "COLLECTION_INFO_SPANISH_DB";        
    private final String[] TXTYPES1 = {"PHASE I","PHASE II","TMJ","COMBINED","OTHER"};
    private final String[] TXTYPES2 = {"PHASE I","PHASE II","TMJ","COMBINED","XRAYS","OTHER"};
    private final String[] STATE = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", 
                                    "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", 
                                    "NC", "ND", "OH", "OK", "OR", "PA","RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", 
                                    "WI", "WY", " " };
    
    private DecimalFormat df;
    
    private MaskFormatter frmtSocial;
    private MaskFormatter frmtPhone;
    private MaskFormatter frmtZip;
    
    private Account account;
    
    private ConsoleLog consoleLog;
    
    private StringBuilder strResult;
    
    private DecimalFormat df2; 
    
    private String accountID;
    private String table;
    
    MDesktopPane desktop;
    
    private MSAccessDatabase trans;
    
    private JPanel mainPanel;
    private JPanel treatmentInfoPanel;
    private JPanel collectionInfoPanel;
    private JPanel patientInfoPanel;
    private JPanel patientInfoRightPanel;
    private JPanel patientInfoLeftPanel;
    private JPanel notePanel;
    private JPanel resultPanel;
    private JPanel actionPanel;
    
    /**
     * Start of patient info components
     */
    private JLabel lblPatient;
    private JLabel lblPatientSS;
    private JLabel lblOffice;
    private JLabel lblAddress1;
    private JLabel lblAddress2;
    private JLabel lblCity;
    private JLabel lblState;
    private JLabel lblZip;
    private JLabel lblEmail;
    
    private JLabel lblHomePhone;
    private JLabel lblMobilePhone;
    private JLabel lblDebtor;
    private JLabel lblDOBAH;
    private JLabel lblDebtorSS;
    private JLabel lblLicense;
    private JLabel lblLDOS;
    
    private MTextField txtPatient;
    private MFormattedTextField txtPatientSS;
    private MTextField txtOffice;
    private MTextField txtAddress1;
    private MTextField txtAddress2;
    private MTextField txtCity;
    private MFormattedTextField txtZip;
    private MTextField txtEmail;
    
    private MFormattedTextField txtHomePhone;
    private MFormattedTextField txtMobilePhone;
    private MTextField txtDebtor;
    private MTextField txtLicense;
    private MFormattedTextField txtDebtorSS;
    
    private MComboBox cboState;
    
    private MDateField dteDOBAH;
    private MDateField dteLDOS;
    
    private MTextArea txtNotes;
    /**
     * End of patient info Components
     */
    
    /**
     * Start of Treatment Info
     */
    private JLabel lblTxt1Type;
    private JLabel lblTxt1Cost;
    private JLabel lblIs2ndContract;
    private JLabel lblTxt2Type;
    private JLabel lblTxt2Cost;
    private JLabel lblAmountPaid;
    private JLabel lblApplianceMade;
    private JLabel lblApplianceCount;
    private JLabel lblIsApplianceDelivered;
    private JLabel lblVisitCount;
    private JLabel lblIsXraysTaken;
    
    private MTextField txtTx1Cost;
    private MTextField txtTx1Other;
    private MTextField txtTx2Cost;
    private MTextField txtTx2Other;
    private MTextField txtAmountPaid;
    private MTextField txtApplianceCount;
    private MTextField txtVisitCount;
    
    private MComboBox cboTx1Type;
    private MComboBox cboTx2Type;
    
    private JRadioButton rdoYes2ndContract;
    private JRadioButton rdoNo2ndContract;
    private JRadioButton rdoYesApplianceMade;
    private JRadioButton rdoNoApplianceMade;
    private JRadioButton rdoYesApplianceDelivered;
    private JRadioButton rdoNoApplianceDelivered;
    private JRadioButton rdoYesXraysTaken;
    private JRadioButton rdoNoXraysTaken;
    
    private ButtonGroup grp2ndContract;
    private ButtonGroup grpApplianceMade;
    private ButtonGroup grpApplianceDelivered;
    private ButtonGroup grpXraysTaken;
    
    private JButton btnCalculate;
    private JButton btnReset;
    private JButton btnEnter;
    private JButton btnOverride;
    
    
    /**
     * End of Treatment Info
     */
    
    /**
     * Start of Collection Info
     */
    
    private JLabel lblOutTx1Type;
    private JLabel lblOutTx1Cost;
    private JLabel lblOutTx2Type;
    private JLabel lblOutTx2Cost;
    private JLabel lblOutLiableAmount;
    private JLabel lblOutContractBreakdown;
    private JLabel lblOutSettleAmount;
    private JLabel lblOutSettleReason;
    private JLabel lblOutTotalPaid;
    private JLabel lblOutPaidBreakdown;
    private JLabel lblOutAmountOwe;
    private JLabel lblAction;
    
    private MTextField txtOutTx1Type;
    private MTextField txtOutTx1Cost;
    private MTextField txtOutTx2Type;
    private MTextField txtOutTx2Cost;
    private MTextField txtOutLiableAmount;
    private MTextField txtOutContractBreakdown;
    private MTextField txtOutSettleAmount;
    private MTextField txtOutSettleReason;
    private MTextField txtOutTotalPaid;
    private MTextField txtOutPaidBreakdown;
    private MTextField txtOutAmountOwe;
    
    
    /**
     * End of Collection Info
     */
    
    private JLabel lblContractLanguage;
    private JRadioButton rdoEnglish;
    private JRadioButton rdoSpanish;
    private ButtonGroup grpContractLanguage;
    
    private MTextArea txtResult;
    private JScrollPane scrollPane;
    private JScrollPane resultScrollPane;
    
    /**
     * Construct a AccountEdit Interface
     */
    public AccountEdit( ){
        
        this( null, null );
    }
    
    /**
     * Construct a AccountEdit Interface
     * 
     * @param accountID  An ID of an dataRow
     * @param table
     */
    public AccountEdit( String accountID, String table ){
        
        this.title  = "Account Edit";
        this.accountID = accountID;
        this.table = table;
        
        this.initComponents( );
        this.buildComponents( );
        this.addEventListeners( );
        this.builtThis( );
    }
    
    private void initComponents( ){
        
        database = MSAccessDatabase.getInstance( );
        
        try {
            frmtSocial = new MaskFormatter( "###-##-####"    );
            frmtPhone  = new MaskFormatter( "(###) ###-####" );
            frmtZip    = new MaskFormatter( "#####" );
        } 
        catch (ParseException ex) {
            
        }
        
        df = new DecimalFormat("#0.00");
        
        desktop    = MDesktopPane.getInstance( );
        consoleLog = ConsoleLog.getInstance( );
        strResult  = new StringBuilder( );
        account    = new Account( );
        
        mainPanel             = new JPanel( );
        patientInfoPanel      = new JPanel( );
        treatmentInfoPanel    = new JPanel( );
        collectionInfoPanel   = new JPanel( );
        patientInfoLeftPanel  = new JPanel( );
        patientInfoRightPanel = new JPanel( );
        notePanel             = new JPanel( );
        treatmentInfoPanel    = new JPanel( );
        resultPanel           = new JPanel( );
        actionPanel           = new JPanel( );

        lblPatient     = new JLabel( "PATIENT"    );
        lblPatientSS   = new JLabel( "PATIENT SS" );
        lblOffice      = new JLabel( "OFFICE"     );
        lblAddress1    = new JLabel( "ADDRESS 1"  );
        lblAddress2    = new JLabel( "ADDRESS 2"  );
        lblCity        = new JLabel( "CITY"       );
        lblState       = new JLabel( "STATE"      );
        lblZip         = new JLabel( "ZIP"        );
        lblEmail       = new JLabel( "EMAIL"      );
       
        lblHomePhone   = new JLabel( "HOME"      );
        lblMobilePhone = new JLabel( "MOBILE"    );
        lblDebtor      = new JLabel( "DEBTOR"    );
        lblDOBAH       = new JLabel( "DOBAH"     );
        lblDebtorSS    = new JLabel( "DEBTOR SS" );
        lblLicense     = new JLabel( "DL"        );
        lblLDOS        = new JLabel( "LDOS"      );
        
        txtNotes = new MTextArea( );
    
        txtPatient     = new MTextField( );
        txtPatientSS   = new MFormattedTextField( frmtSocial );
        txtOffice      = new MTextField( );
        txtAddress1    = new MTextField( );
        txtAddress2    = new MTextField( );
        txtCity        = new MTextField( );
        txtZip         = new MFormattedTextField( frmtZip );
        txtEmail       = new MTextField( );
        txtHomePhone   = new MFormattedTextField( frmtPhone );
        txtMobilePhone = new MFormattedTextField( frmtPhone );
        txtDebtor      = new MTextField( );
        txtDebtorSS    = new MFormattedTextField( frmtSocial );
        txtLicense     = new MTextField( );
    
        cboState = new MComboBox( STATE );

        dteDOBAH = new MDateField( );
        dteLDOS  = new MDateField( );

        lblTxt1Type             = new JLabel( "TREATMENT 1 TYPE"    );
        lblTxt1Cost             = new JLabel( "TREATMENT 1 COST"    );
        lblIs2ndContract        = new JLabel( "2ND CONTRACT"        );
        lblTxt2Type             = new JLabel( "TREAMENT 2 TYPE"     );
        lblTxt2Cost             = new JLabel( "TREAMENT 2 COST"     );
        lblAmountPaid           = new JLabel( "AMOUNT PAID"         );
        lblApplianceMade        = new JLabel( "APPLIANCE MADE"      );
        lblApplianceCount       = new JLabel( "# OF APPLIANCES"     );
        lblIsApplianceDelivered = new JLabel( "APPLIANCE DELIVERED" );
        lblVisitCount           = new JLabel( "# OF VISITS"         );
        lblIsXraysTaken         = new JLabel( "XRAYS TAKEN"         );

        txtTx1Cost        = new MTextField( );
        txtTx1Other       = new MTextField( );
        txtTx2Cost        = new MTextField( );
        txtTx2Other       = new MTextField( );
        txtAmountPaid     = new MTextField( );
        txtApplianceCount = new MTextField( );
        txtVisitCount     = new MTextField( );

        cboTx1Type = new MComboBox( TXTYPES1 );
        cboTx2Type = new MComboBox( TXTYPES2 );

        rdoYes2ndContract        = new JRadioButton( "YES" );
        rdoNo2ndContract         = new JRadioButton( "NO"  );
        rdoYesApplianceMade      = new JRadioButton( "YES" );
        rdoNoApplianceMade       = new JRadioButton( "NO"  );
        rdoYesApplianceDelivered = new JRadioButton( "YES" );
        rdoNoApplianceDelivered  = new JRadioButton( "NO"  );
        rdoYesXraysTaken         = new JRadioButton( "YES" );
        rdoNoXraysTaken          = new JRadioButton( "NO"  );
        
        grp2ndContract        = new ButtonGroup( );
        grpApplianceMade      = new ButtonGroup( );
        grpApplianceDelivered = new ButtonGroup( );
        grpXraysTaken         = new ButtonGroup( );
        
            
        lblOutTx1Type           = new JLabel( "TREATMENT 1 TYPE"      );
        lblOutTx1Cost           = new JLabel( "TREATMENT 1 COST"      );
        lblOutTx2Type           = new JLabel( "TREATMENT 2 TYPE"      );
        lblOutTx2Cost           = new JLabel( "TREATMENT 2 COST"      );
        lblOutLiableAmount      = new JLabel( "LIABLE TREATMENT COST" );
        lblOutContractBreakdown = new JLabel( "CONTRACT BREAKDOWN"    );
        lblOutSettleAmount      = new JLabel( "SETTLE AMOUNT"         );
        lblOutSettleReason      = new JLabel( "SETTLE REASON"         );
        lblOutTotalPaid         = new JLabel( "TOTAL PAID"            );
        lblOutPaidBreakdown     = new JLabel( "PAID BREAKDOWN"        );
        lblOutAmountOwe         = new JLabel( "AMOUNT OWE"            );
        lblAction               = new JLabel( "" );

        txtOutTx1Type           = new MTextField( );
        txtOutTx1Cost           = new MTextField( );
        txtOutTx2Type           = new MTextField( );
        txtOutTx2Cost           = new MTextField( );
        txtOutLiableAmount      = new MTextField( );
        txtOutContractBreakdown = new MTextField( );
        txtOutSettleAmount      = new MTextField( );
        txtOutSettleReason      = new MTextField( );
        txtOutTotalPaid         = new MTextField( );
        txtOutPaidBreakdown     = new MTextField( );
        txtOutAmountOwe         = new MTextField( );
        
        txtResult = new MTextArea( );
        
        lblContractLanguage = new JLabel( "CONTRACT:" );
        
        rdoEnglish = new JRadioButton( "ENGLISH" );
        rdoSpanish = new JRadioButton( "SPANISH" );
        
        grpContractLanguage = new ButtonGroup( );
        
        btnCalculate = new JButton( "CALCULATE"   );
        btnReset     = new JButton( "RESET"       );
        btnEnter     = new JButton( "ENTER TO DB" );
        btnOverride  = new JButton( "OVERRIDE"    );
        
        resultScrollPane = new JScrollPane( txtResult, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ); 
    }
    
    private void buildComponents( ){
        
        strResult.append( ">> Ready...\n>>" );
        
        lblLicense  .setToolTipText( "Driver Lincense"                  );
        lblDebtorSS .setToolTipText( "Debtor Social Security"           );
        lblPatientSS.setToolTipText( "Debtor Social Security"           );
        lblLDOS     .setToolTipText( "Last Date of Service"             );
        lblDOBAH    .setToolTipText( "Date of Birth of Account  Holder" );
        
        grp2ndContract.add( rdoYes2ndContract );
        grp2ndContract.add( rdoNo2ndContract  );
        rdoNo2ndContract.setSelected( true );
        
        grpApplianceMade.add( rdoYesApplianceMade );
        grpApplianceMade.add( rdoNoApplianceMade  );
        rdoNoApplianceMade.setSelected( true );
        
        grpApplianceDelivered.add( rdoYesApplianceDelivered );
        grpApplianceDelivered.add( rdoNoApplianceDelivered  );
        rdoNoApplianceDelivered.setSelected( true );
        
        grpXraysTaken.add( rdoYesXraysTaken );
        grpXraysTaken.add( rdoNoXraysTaken  );
        rdoNoXraysTaken.setSelected( true );
        
        txtTx1Other             .setEnabled( false );
        cboTx2Type              .setEnabled( false );
        txtTx2Other             .setEnabled( false );
        txtTx2Cost              .setEnabled( false );
        txtApplianceCount       .setEnabled( false );
        rdoYesApplianceDelivered.setEnabled( false );
        rdoNoApplianceDelivered .setEnabled( false );
        rdoYesXraysTaken        .setEnabled( false );
        rdoNoXraysTaken         .setEnabled( false );
        
        btnCalculate.setBackground( Color.GREEN );
        btnEnter    .setBackground( Color.CYAN  );
        btnReset    .setBackground( Color.RED   );
        btnReset    .setForeground( Color.WHITE );
        btnOverride .setBackground( Color.RED   );
        btnOverride .setForeground( Color.WHITE );
        
        cboState.setSelectedIndex (4);
        cboTx2Type.setSelectedIndex(-1);
        
        patientInfoLeftPanel.setLayout( new PropLayout( ) );
        patientInfoLeftPanel.add( lblPatient    , "+ | 0.20, 25" );
        patientInfoLeftPanel.add( txtPatient    , "  | 0.80, 25" );
        patientInfoLeftPanel.add( lblPatientSS  , "+ | 0.20, 25" );
        patientInfoLeftPanel.add( txtPatientSS  , "  | 0.80, 25" );
        patientInfoLeftPanel.add( lblOffice     , "+ | 0.20, 25" );
        patientInfoLeftPanel.add( txtOffice     , "  | 0.80, 25" );
        patientInfoLeftPanel.add( lblAddress1   , "+ | 0.20, 25" );
        patientInfoLeftPanel.add( txtAddress1   , "  | 0.80, 25" );
        patientInfoLeftPanel.add( lblAddress2   , "+ | 0.20, 25" );
        patientInfoLeftPanel.add( txtAddress2   , "  | 0.80, 25" );
        patientInfoLeftPanel.add( lblCity       , "+ | 0.21, 25" );
        patientInfoLeftPanel.add( txtCity       , "  | 0.34, 25" );
        patientInfoLeftPanel.add( lblState      , "  | 0.10, 25" );
        patientInfoLeftPanel.add( cboState      , "  | 0.15, 25" );
        patientInfoLeftPanel.add( lblZip        , "  | 0.05, 25" );
        patientInfoLeftPanel.add( txtZip        , "  | 0.15, 25" );
        patientInfoLeftPanel.add( lblEmail      , "+ | 0.20, 25" );
        patientInfoLeftPanel.add( txtEmail      , "  | 0.80, 25" );
        
        patientInfoRightPanel.setLayout( new PropLayout( ) );
        patientInfoRightPanel.add( lblHomePhone  , "+ | 0.20, 25" );
        patientInfoRightPanel.add( txtHomePhone  , "  | 0.80, 25" );
        patientInfoRightPanel.add( lblMobilePhone, "+ | 0.20, 25" );
        patientInfoRightPanel.add( txtMobilePhone, "  | 0.80, 25" );
        patientInfoRightPanel.add( lblDebtor     , "+ | 0.20, 25" );
        patientInfoRightPanel.add( txtDebtor     , "  | 0.80, 25" );
        patientInfoRightPanel.add( lblDOBAH      , "+ | 0.20, 25" );
        patientInfoRightPanel.add( dteDOBAH      , "  | 0.80, 25" );
        patientInfoRightPanel.add( lblDebtorSS   , "+ | 0.20, 25" );
        patientInfoRightPanel.add( txtDebtorSS   , "  | 0.80, 25" );
        patientInfoRightPanel.add( lblLicense    , "+ | 0.20, 25" );
        patientInfoRightPanel.add( txtLicense    , "  | 0.80, 25" );
        patientInfoRightPanel.add( lblLDOS       , "+ | 0.20, 25" );
        patientInfoRightPanel.add( dteLDOS       , "  | 0.80, 25" );
        
        notePanel.setBorder(  BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ),"Notes" ) );
        notePanel.setLayout( new PropLayout( ) );
        notePanel.add( txtNotes, "+ | 1.00, 1.00" );
        
        patientInfoPanel.setLayout( new PropLayout( ) );
        patientInfoPanel.setBorder( BorderFactory.createTitledBorder(  BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ),"Patient Info" ) );
        patientInfoPanel.add( patientInfoLeftPanel , "+ | 0.50, 1.00" );
        patientInfoPanel.add( patientInfoRightPanel, "  | 0.50, 1.00" );
        
        treatmentInfoPanel.setBorder( BorderFactory.createTitledBorder(  BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ),"Treatment Info" ) );
        treatmentInfoPanel.setLayout( new PropLayout( ) );
        treatmentInfoPanel.add( lblTxt1Type              , "+ | 0.40, 25" );
        treatmentInfoPanel.add( cboTx1Type               , "  | 0.60, 25" );
        treatmentInfoPanel.add( new JLabel( )            , "+ | 0.40, 25" );
        treatmentInfoPanel.add( txtTx1Other              , "  | 0.60, 25" );
        treatmentInfoPanel.add( lblTxt1Cost              , "+ | 0.35, 25" );
        treatmentInfoPanel.add( new JLabel( "$" )        , "  | 0.05, 25" );
        treatmentInfoPanel.add( txtTx1Cost               , "  | 0.55, 25" );
        treatmentInfoPanel.add( new JLabel(".00" )       , "  | 0.05, 25" );
        treatmentInfoPanel.add( lblIs2ndContract         , "+ | 0.40, 25" );
        treatmentInfoPanel.add( rdoYes2ndContract        , "  | 0.15, 25" );
        treatmentInfoPanel.add( rdoNo2ndContract         , "  | 0.15, 25" );
        treatmentInfoPanel.add( new JLabel( )            , "  | 0.70, 25" );
        treatmentInfoPanel.add( lblTxt2Type              , "+ | 0.40, 25" );
        treatmentInfoPanel.add( cboTx2Type               , "  | 0.60, 25" );
        treatmentInfoPanel.add( new JLabel( )            , "+ | 0.40, 25" );
        treatmentInfoPanel.add( txtTx2Other              , "  | 0.60, 25" );
        treatmentInfoPanel.add( lblTxt2Cost              , "+ | 0.35, 25" );
        treatmentInfoPanel.add( new JLabel( "$" )        , "  | 0.05, 25" );
        treatmentInfoPanel.add( txtTx2Cost               , "  | 0.55, 25" );
        treatmentInfoPanel.add( new JLabel(".00" )       , "  | 0.05, 25" );
        treatmentInfoPanel.add( lblAmountPaid            , "+ | 0.35, 25" );
        treatmentInfoPanel.add( new JLabel( "$" )        , "  | 0.05, 25" );
        treatmentInfoPanel.add( txtAmountPaid            , "  | 0.55, 25" );
        treatmentInfoPanel.add( new JLabel(".00" )       , "  | 0.05, 25" );
        treatmentInfoPanel.add( lblApplianceMade         , "+ | 0.40, 25" );
        treatmentInfoPanel.add( rdoYesApplianceMade      , "  | 0.15, 25" );
        treatmentInfoPanel.add( rdoNoApplianceMade       , "  | 0.15, 25" );
        treatmentInfoPanel.add( new JLabel( )            , "  | 0.70, 25" );
        treatmentInfoPanel.add( lblApplianceCount        , "+ | 0.40, 25" );
        treatmentInfoPanel.add( txtApplianceCount        , "  | 0.60, 25" );
        treatmentInfoPanel.add( lblIsApplianceDelivered  , "+ | 0.40, 25" );
        treatmentInfoPanel.add( rdoYesApplianceDelivered , "  | 0.15, 25" );
        treatmentInfoPanel.add( rdoNoApplianceDelivered  , "  | 0.15, 25" );
        treatmentInfoPanel.add( new JLabel( )            , "  | 0.70, 25" );
        treatmentInfoPanel.add( lblIsXraysTaken          , "+ | 0.40, 25" );
        treatmentInfoPanel.add( rdoYesXraysTaken         , "  | 0.15, 25" );
        treatmentInfoPanel.add( rdoNoXraysTaken          , "  | 0.15, 25" );
        treatmentInfoPanel.add( new JLabel( )            , "  | 0.70, 25" );
        treatmentInfoPanel.add( lblVisitCount            , "+ | 0.40, 25" );
        treatmentInfoPanel.add( txtVisitCount            , "  | 0.60, 25" );
        
        txtOutTx1Type          .setEditable( false );
        txtOutTx1Cost          .setEditable( false );
        txtOutTx2Type          .setEditable( false );
        txtOutTx2Cost          .setEditable( false );
        txtOutLiableAmount     .setEditable( false );
        txtOutContractBreakdown.setEditable( false );
        txtOutSettleAmount     .setEditable( false );
        txtOutSettleReason     .setEditable( false );
        txtOutTotalPaid        .setEditable( false );
        txtOutPaidBreakdown    .setEditable( false );
        txtOutAmountOwe        .setEditable( false );
        
        txtOutTx1Type          .setFocusable( false );
        txtOutTx1Cost          .setFocusable( false );
        txtOutTx2Type          .setFocusable( false );
        txtOutTx2Cost          .setFocusable( false );
        txtOutLiableAmount     .setFocusable( false );
        txtOutContractBreakdown.setFocusable( false );
        txtOutSettleAmount     .setFocusable( false );
        txtOutSettleReason     .setFocusable( false );
        txtOutTotalPaid        .setFocusable( false );
        txtOutPaidBreakdown    .setFocusable( false );
        txtOutAmountOwe        .setFocusable( false );
        
        lblAction.setFont( new Font( lblAction.getFont( ).getName( ), Font.BOLD, 20 ) );
        lblAction.setHorizontalAlignment( SwingConstants.CENTER );
        lblAction.setForeground( Color.RED );
        
        collectionInfoPanel.setBorder( BorderFactory.createTitledBorder(  BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ),"Collection Info" ) );
        collectionInfoPanel.setLayout( new PropLayout( ) );
        collectionInfoPanel.add( lblOutTx1Type          , "+ | 0.40, 25"   );
        collectionInfoPanel.add( txtOutTx1Type          , "  | 0.60, 25"   );
        collectionInfoPanel.add( lblOutTx1Cost          , "+ | 0.40, 25"   );
        collectionInfoPanel.add( txtOutTx1Cost          , "  | 0.60, 25"   );
        collectionInfoPanel.add( lblOutTx2Type          , "+ | 0.40, 25"   );
        collectionInfoPanel.add( txtOutTx2Type          , "  | 0.60, 25"   );
        collectionInfoPanel.add( lblOutTx2Cost          , "+ | 0.40, 25"   );
        collectionInfoPanel.add( txtOutTx2Cost          , "  | 0.60, 25"   );
        collectionInfoPanel.add( lblOutLiableAmount     , "+ | 0.40, 25"   );
        collectionInfoPanel.add( txtOutLiableAmount     , "  | 0.60, 25"   );
        collectionInfoPanel.add( lblOutContractBreakdown, "+ | 0.40, 25"   );
        collectionInfoPanel.add( txtOutContractBreakdown, "  | 0.60, 25"   );
        collectionInfoPanel.add( lblOutSettleAmount     , "+ | 0.40, 25"   );
        collectionInfoPanel.add( txtOutSettleAmount     , "  | 0.60, 25"   );
        collectionInfoPanel.add( lblOutSettleReason     , "+ | 0.40, 25"   );
        collectionInfoPanel.add( txtOutSettleReason     , "  | 0.60, 25"   );
        collectionInfoPanel.add( lblOutTotalPaid        , "+ | 0.40, 25"   );
        collectionInfoPanel.add( txtOutTotalPaid        , "  | 0.60, 25"   );
        collectionInfoPanel.add( lblOutPaidBreakdown    , "+ | 0.40, 25"   );
        collectionInfoPanel.add( txtOutPaidBreakdown    , "  | 0.60, 25"   );
        collectionInfoPanel.add( lblOutAmountOwe        , "+ | 0.40, 25"   );
        collectionInfoPanel.add( txtOutAmountOwe        , "  | 0.60, 25"   );
        collectionInfoPanel.add( lblAction              , "+ | 1.00, 1.00" );
        
        txtResult.setEditable( false );
        txtResult.setFocusable( false );
        txtResult.setText( strResult.toString( ) );
        
        grpContractLanguage.add( rdoEnglish );
        grpContractLanguage.add( rdoSpanish );
        rdoEnglish.setSelected( true );

        resultPanel.setBorder( BorderFactory.createTitledBorder(  BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ),"Result" ) );
        resultPanel.setLayout( new PropLayout( ) );
        resultPanel.add( resultScrollPane, "+ | 1.00, 1.00" );
        
        btnEnter.setEnabled( false );
        
        actionPanel.setBorder( BorderFactory.createTitledBorder(  BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ), " " ) );
        actionPanel.setLayout( new PropLayout( ) );
        actionPanel.add( lblContractLanguage, "+ | 0.25, 25"   );
        actionPanel.add( rdoEnglish         , "  | 0.25, 25"   );
        actionPanel.add( rdoSpanish         , "  | 0.25, 25"   );
        actionPanel.add( new JLabel( )      , "  | 0.25, 25"   );
        actionPanel.add( btnCalculate       , "+ | 1.00, 40"   );
        actionPanel.add( btnEnter           , "+ | 1.00, 40"   );
        actionPanel.add( btnReset           , "+ | 1.00, 40"   );
        actionPanel.add( btnOverride        , "+ | 1.00, 40"   );
        actionPanel.add( resultPanel        , "+ | 1.00, 1.00" );
        
        mainPanel.setPreferredSize( new Dimension(1600,900) );
        mainPanel.setLayout( new PropLayout( ) );
        mainPanel.add( patientInfoPanel   , "+ | 1000, 270" );   
        mainPanel.add( notePanel          , "  | 500, 270" );
        mainPanel.add( treatmentInfoPanel , "+ | 500, 450" );
        mainPanel.add( collectionInfoPanel, "  | 500, 450" );
        mainPanel.add( actionPanel        , "  | 500, 450" );
        scrollPane = new JScrollPane( mainPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ); 

    }
    
    private void builtThis( ){
        
        /**
         * Disables Features not yet available
         */
        
        btnOverride.setEnabled( false );
        
        /**
         * 
         */
//        this.setLayout( new PropLayout( ) );
        

//        this.setVisible( true );
//        this.setDefaultCloseOperation( DISPOSE_ON_CLOSE );
//        this.setTitle(title);
//        this.setIconifiable(true);
//        this.setResizable(true);
//        this.setMaximizable(true);
//        this.setSize( 1200,700 );
        
        this.setClosable(true);
        this.setTitle(title);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setMaximizable(true);
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        
        resultScrollPane.setViewportView( txtResult );
        this.add( scrollPane );
        
        if( accountID != null )
            loadData( );
      
        txtPatient.requestFocus( );
    }
    
    private void addEventListeners( ){
        
        btnEnter.addActionListener( new ActionListener( ){

            @Override
            public void actionPerformed( ActionEvent event ) {
                
                String table = null;
                
                if( rdoEnglish.isSelected( ) )
                  table = ENGLISH_TABLE;
                
                else if( rdoSpanish.isSelected( ) )
                    table = SPANISH_TABLE;
                
                String query = "insert into " + table + " ([PATIENT NAME],[ACCOUNT HOLDER],[DOBAH],[DL],"
                        + "[PATIENT SOCIAL],[DEBTOR SOCIAL],[OFFICE],[ADDRESS 1],[ADDRESS 2],[CITY],[STATE],"
                        + "[ZIP],[HOME PHONE],[MOBILE PHONE],[EMAIL ADDRESS],[CONTRACT 1],[CONTRACT TYPE 1],"
                        + "[CONTRACT 2],[CONTRACT TYPE 2],[LIABLE CONTRACT AMOUNT],[CONTRACT BREAKDOWN],"
                        + "[SETTLED AMOUNT],[SETTLED REASON],[AMOUNT PAID],[PAID BREAKDOWN],[AMOUNT OWE],"
                        + "[LAST DATE OF SERVICE],[APPLIANCE MADE],[NUMBER OF APPLIANCE],[SECOND CONTRACT],"
                        + "[APPLIANCE DELIVERED],[XRAYS TAKEN],[NUMBER OF VISITS],[NOTES]) "
                        + "values ("
                        + txtPatient.getSqlText( )                             + ","
                        + txtDebtor.getSqlText( )                              + "," 
                        + dteDOBAH.getSqlDate( )                               + ","
                        + txtLicense.getSqlText( )                             + "," 
                        + txtPatientSS.getSqlText( )                           + ","
                        + txtDebtorSS.getSqlText( )                            + "," 
                        + txtOffice.getSqlText( )                              + "," 
                        + txtAddress1.getSqlText( )                            + "," 
                        + txtAddress2.getSqlText( )                            + "," 
                        + txtCity.getSqlText( )                                + "," 
                        + cboState.getSelectedSqlItem( )                       + ","
                        + txtZip.getSqlText( )                                 + ","
                        + txtHomePhone.getSqlText( )                           + "," 
                        + txtMobilePhone.getSqlText( )                         + "," 
                        + txtEmail.getSqlText( )                               + "," 
                        + txtTx1Cost.getSqlText( )                             + "," 
                        + cboTx1Type.getSelectedSqlItem( )                     + "," 
                        + txtTx2Cost.getSqlText( )                             + "," 
                        + cboTx2Type.getSelectedSqlItem( )                     + ","
                        + txtOutLiableAmount.getSqlText( )                     + ","  
                        + txtOutContractBreakdown.getSqlText( )                + ","  
                        + txtOutSettleAmount.getSqlText( )                     + "," 
                        + txtOutSettleReason.getSqlText( )                     + ","  
                        + txtAmountPaid.getSqlText( )                          + ","  
                        + txtOutPaidBreakdown.getSqlText( )                    + ","  
                        + txtOutAmountOwe.getSqlText( )                        + ","
                        + dteLDOS.getSqlDate( )                                + ","
                        + (rdoYesApplianceMade.isSelected( ) ? "1" : "0")      + ","
                        + txtApplianceCount.getSqlText( )                      + ","  
                        + (rdoYes2ndContract.isSelected( ) ? "1" : "0")        + ","
                        + (rdoYesApplianceDelivered.isSelected( ) ? "1" : "0") + ","
                        + (rdoYesXraysTaken.isSelected( ) ? "1" : "0" )        + ","
                        + txtVisitCount.getSqlText( )                          + ","
                        + txtNotes.getSqlText( )                               + ")" ;
                
               String result =  database.executeInsert( query );
               
               strResult.append( "\n>> " + result );
               txtResult.setText( strResult.toString( ) );
            }
        });
        
        btnCalculate.addActionListener( new ActionListener( ){

            @Override
            public void actionPerformed( ActionEvent event ) {
                checkInputs( );
                getCollectionInfo( );
            }
        });
        
        btnReset.addActionListener( new ActionListener( ){

            @Override
            public void actionPerformed( ActionEvent event ) {
                reset( );
            }
        });
        
        rdoYes2ndContract.addActionListener( new ActionListener( ){
            
            @Override
            public void actionPerformed( ActionEvent event ){
                
                cboTx2Type.setEnabled( true );
                txtTx2Cost.setEnabled( true );
                cboTx2Type.setSelectedIndex(0);
            }
        });
        
        rdoNo2ndContract.addActionListener( new ActionListener( ){
            
            @Override
            public void actionPerformed( ActionEvent event ){
                
                cboTx2Type.setEnabled( false );
                txtTx2Cost.setEnabled( false );
                
                rdoYesXraysTaken.setEnabled( false );
                rdoNoXraysTaken.setEnabled( false );
                rdoNoXraysTaken.setSelected( true );
                cboTx2Type.setSelectedIndex(-1);
                
                txtTx2Cost.clear( );
                txtTx2Other.clear( );
            }
        });
        
        rdoYesApplianceMade.addActionListener( new ActionListener( ){
            
            @Override
            public void actionPerformed( ActionEvent event ){
                
                txtApplianceCount.setEnabled( true );
                rdoYesApplianceDelivered.setEnabled( true );
                rdoNoApplianceDelivered.setEnabled( true );
            }
        });
        
        rdoNoApplianceMade.addActionListener( new ActionListener( ){
            
            @Override
            public void actionPerformed( ActionEvent event ){
                
                txtApplianceCount.setEnabled( false );
                rdoNoApplianceDelivered.setEnabled( false );
                rdoYesApplianceDelivered.setEnabled( false );
                txtApplianceCount.clear( );
            }
        });
        
        cboTx1Type.addActionListener( new ActionListener( ) {
           
            @Override
            public void actionPerformed( ActionEvent event ){
                
                if( cboTx1Type.getSelectedItem( ).equals( "OTHER" ) )
                    txtTx1Other.setEnabled( true );
                else{
                    txtTx1Other.setEnabled( false );
                    txtTx1Other.clear( );
                }
            }
        });
        
        cboTx2Type.addActionListener( new ActionListener( ){
            
            @Override
            public void actionPerformed( ActionEvent event ){
                
                if( cboTx2Type.getSelectedIndex( ) == -1 )
                    return;
                
                if( cboTx2Type.getSelectedItem( ).equals( "OTHER" ) )
                    txtTx2Other.setEnabled( true );
                else{
                    txtTx2Other.setEnabled( false );
                    txtTx2Other.clear( );
                }
                
                if( cboTx2Type.getSelectedItem( ).equals( "XRAYS" ) ){
                    
                    rdoYesXraysTaken.setEnabled( true );
                    rdoNoXraysTaken.setEnabled( true );
                    txtTx2Cost.setText( "250" );
                    txtTx2Cost.setEnabled( false );
                    
                }
                else{
                    rdoYesXraysTaken.setEnabled( false );
                    rdoNoXraysTaken.setEnabled( false );
                    txtTx2Cost.setEnabled( true );
                }
            }
        });
        
        
        
    }
    
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXX   PRIVATE CLASS XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    private void checkInputs( ){

        int check;
        boolean isException = false;
        
        try{
            consoleLog.append( "\n>>Transaction " + ConsoleLog.getTransactionCount( ) + ":" );
            check = Integer.parseInt( txtTx1Cost.getText( ) );
            txtTx1Cost.setBackground( null );
        }
        catch ( Exception exception ){
            
            txtTx1Cost.setBackground( Color.RED);
            consoleLog.append( exception + " at Tx1 Cost Field" );
            strResult.append( "\n>> Error: TREATMENT 1 COST IS INVALID" );
            isException = true;
        }
        finally{
            try{
                if( rdoYes2ndContract.isSelected( ) ){
                    check = Integer.parseInt( txtTx2Cost.getText( ) );
                    txtTx2Cost.setBackground( null );
                }
            }
            catch ( Exception exception ){
                
                txtTx2Cost.setBackground( Color.RED );
                consoleLog.append( exception + " at Tx2 Cost Field" );
                strResult.append( "\n>> Error: TREATMENT 2 COST IS INVALID" );
                isException = true;
            }
            finally{
                try{
                    check = Integer.parseInt( txtAmountPaid.getText( ) );
                    txtAmountPaid.setBackground( null );
                }
                catch ( Exception exception ){
                    
                    txtAmountPaid.setBackground( Color.RED );
                    consoleLog.append( exception + " at Amount Paid Field" );
                    strResult.append( "\n>> Error: AMOUNT PAID IS INVALID" );
                    isException = true;
                }
                finally{
                    try{
                        if( rdoYesApplianceMade.isSelected( ) )
                            check = Integer.parseInt( txtApplianceCount.getText( ) );
                        
                        txtApplianceCount.setBackground( null );
                    }
                    catch ( Exception exception ){
                        
                        txtApplianceCount.setBackground( Color.RED );
                        consoleLog.append( exception + "at Appliance Count field");
                        strResult.append( "\n>> Error: # OF APPLIANCE IS INVALID" );
                        isException = true;
                    }
                    finally{
                        try{

                            check = Integer.parseInt( txtVisitCount.getText( ) );
                            txtVisitCount.setBackground( null );
                        }
                        catch ( Exception exception ){
                            
                            txtVisitCount.setBackground( Color.RED );
                            consoleLog.append( exception + "at Visit Count Field" );
                            strResult.append( "\n>> Error: # OF VISITS IS INVALID" );
                            isException = true;
                        }
                        finally{

                            if( cboTx1Type.getSelectedItem( ).equals( "OTHER" ) ){
                                
                                if( txtTx1Other.getText( ).length( ) == 0 ){

                                    strResult.append( "\n>> Error: MISSING TREATMENT 1 TYPE" );
                                    txtTx1Other.setBackground( Color.RED );
                                }
                                else{
                                    txtTx1Other.setBackground( null );   
                                }
                            }
                            
                            if( cboTx2Type.getSelectedIndex( ) != -1 ){
                                
                                if( cboTx2Type.getSelectedItem( ).equals( "OTHER" ) ){

                                    if( txtTx2Other.getText( ).length( ) == 0 ){

                                            strResult.append( "\n>> Error: MISSING TREATMENT 2 TYPE " );
                                            txtTx2Other.setBackground( Color.RED );
                                        }
                                    else{
                                        txtTx2Other.setBackground( null );   
                                    }
                                }
                            }
                                   
                            if(!isException)
                                strResult.append( "\n>>Treatment Info Verified..." );
                            else
                                strResult.append( "\n>>" );
                        }
                    }
                }
            }
        }
        txtResult.setText( strResult.toString( ) );
    }
    
    private void getCollectionInfo( ){
        
       account.setTx1Cost( Integer.parseInt( txtTx1Cost.getText( )) );
       
       account.set2ndContract( rdoYes2ndContract.isSelected( ));
       
       if( rdoYes2ndContract.isSelected( ) )
            account.setTx2Cost( Integer.parseInt( txtTx2Cost.getText( ) ) );
       
       account.setAmountPaid( Integer.parseInt( txtAmountPaid.getText( ) ) );
       
       if( rdoYesApplianceMade.isSelected( ) ){
            account.setApplianceCount( Integer.parseInt( txtApplianceCount.getText( ) ) );
            account.setApplianceDelivered( rdoYesApplianceDelivered.isSelected( ) );
       }
       
       account.setVisitCount( Integer.parseInt( txtVisitCount.getText( ) ) );
       
       account.setApplianceMade( rdoYesApplianceMade.isSelected( ) );
       account.setXraysTaken( rdoYesXraysTaken.isSelected( ) );
       
       if( cboTx1Type.getSelectedItem( ).equals( "OTHER" ) )
            account.setTx1Type( txtTx1Other.getText( ).toUpperCase( ) );
       else
            account.setTx1Type( cboTx1Type.getSelectedItem( ).toString( ) );
       
       if( cboTx2Type.getSelectedIndex( ) != -1 ){
           
            if( cboTx2Type.getSelectedItem( ).equals( "OTHER" ) )
                account.setTx2Type( txtTx2Other.getText( ).toUpperCase( ) );
            else
                account.setTx2Type( cboTx2Type.getSelectedItem( ).toString( ) );
       }
       
       account.calculate( );
       
       txtOutTx1Type.setText( account.getTx1Type( )       );
       txtOutTx1Cost.setText( "$" + df.format( account.getTx1Cost( ) ) );
       
       if( rdoYes2ndContract.isSelected( ) ){
           
            txtOutTx2Type.setText( account.getTx2Type( )       );
            txtOutTx2Cost.setText( "$" + df.format( account.getTx2Cost( ) ) );
       }
       txtOutLiableAmount     .setText( "$" + df.format( account.getLiableAmount( ) ) );
       txtOutContractBreakdown.setText( account.getContractBreakdown( )  );
       txtOutSettleAmount     .setText( "$" + df.format( account.getSettleAmount( ) ) );
       txtOutSettleReason     .setText( account.getSettleReason( )       );
       txtOutTotalPaid        .setText( "$" + df.format( account.getAmountPaid( ) ) );
       txtOutAmountOwe        .setText( "$" + df.format( account.getAmountOwe( ) ) );
       txtOutPaidBreakdown    .setText( account.getPaidBreakdown( )      );
       
       strResult.append( "\n>>Calculation...Successful.\n" );
       txtResult.setText( strResult.toString( ) );
       
       if( account.getSettleAmount() < account.getAmountPaid( ) ){
           
           lblAction.setText( "DO NOT SENT TO COLLECTION" );
           btnEnter .setEnabled( false );
       }
       else{
           lblAction.setText( "" );
           btnEnter.setEnabled( true );
       }
    }
    
    private void reset( ){
        
        txtPatient    .clear( );
        txtPatientSS  .clear( );
        txtOffice     .clear( );
        txtAddress1   .clear( );
        txtAddress2   .clear( );
        txtCity       .clear( );
        txtZip        .clear( );
        txtEmail      .clear( );
        txtHomePhone  .clear( );
        txtMobilePhone.clear( );
        txtDebtor     .clear( );
        dteDOBAH      .clear( );
        txtDebtorSS   .clear( );
        txtLicense    .clear( );
        dteLDOS       .clear( );
        txtNotes      .clear( );
        
        cboTx1Type             .setSelectedIndex(0);
        txtTx1Other            .clear( );
        txtTx1Cost             .clear( );
        cboTx2Type             .setSelectedIndex( -1 );
        rdoNo2ndContract       .setSelected( true );
        cboTx2Type             .setEnabled( false );
        txtTx2Cost             .clear( );
        txtAmountPaid          .clear( );
        rdoNoApplianceMade     .setSelected( true );
        txtApplianceCount      .clear( );
        txtApplianceCount      .setEnabled( false );
        rdoNoApplianceDelivered.setSelected( true );
        rdoYesApplianceDelivered.setEnabled( false );
        rdoNoApplianceDelivered.setEnabled( false );
        rdoNoXraysTaken        .setSelected( true );
        rdoYesXraysTaken       .setEnabled( false );
        rdoNoXraysTaken        .setEnabled( false );
        txtApplianceCount      .clear( );
        txtVisitCount          .clear( );
        
        txtOutTx1Type          .clear( );
        txtOutTx1Cost          .clear( );
        txtOutTx2Type          .clear( );
        txtOutTx2Cost          .clear( );
        txtOutLiableAmount     .clear( );
        txtOutContractBreakdown.clear( );
        txtOutSettleAmount     .clear( );
        txtOutSettleReason     .clear( );
        txtOutTotalPaid        .clear( );
        txtOutPaidBreakdown    .clear( );
        txtOutAmountOwe        .clear( );
    }
    
    private void loadData( ){
        /**
         * 00 - [PATIENT NAME] 
         * 01 - [ACCOUNT HOLDER] 
         * 02 - [DOBAH] 
         * 03 - [DL]
         * 04 - [PATIENT SOCIAL] 
         * 05 - [DEBTOR SOCIAL] 
         * 06 - [OFFICE]
         * 07 - [ADDRESS 1] 
         * 08 - [ADDRESS 2] 
         * 09 - [CITY] 
         * 10 - [STATE]
         * 11 - [ZIP] 
         * 12 - [HOME PHONE] 
         * 13 - [MOBILE PHONE] 
         * 14 - [EMAIL ADDRESS]
         * 15 - [CONTRACT 1] 
         * 16 - [CONTRACT TYPE 1] 
         * 17 - [CONTRACT 2]
         * 25 - [CONTRACT TYPE 2] 
         * 19 - [LIABLE CONTRACT AMOUNT] 
         * 20 - [CONTRACT BREAKDOWN]
         * 21 - [SETTLED AMOUNT] 
         * 22 - [SETTLED REASON] 
         * 23 - [AMOUNT PAID] 
         * 24 - [PAID BREAKDOWN]
         * 25 - [AMOUNT OWE] 
         * 26 - [LAST DATE OF SERVICE] 
         * 27 - [APPLIANCE MADE]
         * 28 - [NUMBER OF APPLIANCE] 
         * 29 - [SECOND CONTRACT] 
         * 30 - [APPLIANCE DELIVERED]
         * 31 - [XRAYS TAKEN] 
         * 32 - [NUMBER OF VISITS]
         */

        String query = "select [PATIENT NAME],[ACCOUNT HOLDER],[DOBAH],[DL],"
                + "[PATIENT SOCIAL],[DEBTOR SOCIAL],[OFFICE],[ADDRESS 1],[ADDRESS 2],[CITY],[STATE],"
                + "[ZIP],[HOME PHONE],[MOBILE PHONE],[EMAIL ADDRESS],[CONTRACT 1],[CONTRACT TYPE 1],"
                + "[CONTRACT 2],[CONTRACT TYPE 2],[LIABLE CONTRACT AMOUNT],[CONTRACT BREAKDOWN],"
                + "[SETTLED AMOUNT],[SETTLED REASON],[AMOUNT PAID],[PAID BREAKDOWN],[AMOUNT OWE],"
                + "[LAST DATE OF SERVICE],[APPLIANCE MADE],[NUMBER OF APPLIANCE],[SECOND CONTRACT],"
                + "[APPLIANCE DELIVERED],[XRAYS TAKEN],[NUMBER OF VISITS] "
                + "from " + table
                + " where [ID] = " + accountID;
        
        Vector<Vector> data = database.executeRetrieve(query);
        
        txtPatient    .setText( data.get(0).toString( ) );
        txtPatientSS  .setText( data.get(4).toString( ) );
        txtOffice     .setText( data.get(6).toString( ) );
        txtAddress1   .setText( data.get(7).toString( ) );
        txtAddress2   .setText( data.get(8).toString( ) );
        txtCity       .setText( data.get(9).toString( ) );
        txtZip        .setText( data.get(10).toString( ) );
        txtEmail      .setText( data.get(14).toString( ) );
        txtHomePhone  .setText( data.get(12).toString( ) );
        txtMobilePhone.setText( data.get(13).toString( ) );
        txtDebtor     .setText( data.get(1).toString( ) );
        dteDOBAH      .setText( data.get(2).toString( ) );
        txtDebtorSS   .setText( data.get(5).toString( ) );
        txtLicense    .setText( data.get(3).toString( ) );
        dteLDOS       .setText( data.get(26).toString( ) );
        txtNotes      .setText( data.get(0).toString( ) );
        
        cboTx1Type              .setSelectedItem( data.get(16).toString( ) );
        txtTx1Other             .setText( data.get(0).toString( ) );
        
        txtTx1Cost              .setText( data.get(15).toString( ) );
        cboTx2Type              .setSelectedItem( data.get(16) );
        
        rdoNo2ndContract        .setSelected( true );
        
        cboTx2Type              .setSelectedItem( data.get(17).toString( ) );
        txtTx2Cost              .setText( data.get(25).toString( ) );
        
        txtAmountPaid           .setText( data.get(23).toString( ) );
        rdoNoApplianceMade      .setSelected( true );
        txtApplianceCount       .setText( data.get(28).toString( ) );
        rdoNoApplianceDelivered .setSelected( true );
        rdoYesApplianceDelivered.setEnabled( false );
        rdoNoApplianceDelivered .setEnabled( false );
        txtVisitCount           .setText( data.get(32).toString( ) );

        txtOutTx1Type           .setText( data.get(0).toString( ) );
        txtOutTx1Cost           .setText( data.get(0).toString( ) );
        txtOutTx2Type           .setText( data.get(0).toString( ) );
        txtOutTx2Cost           .setText( data.get(0).toString( ) );
        txtOutLiableAmount      .setText( data.get(0).toString( ) );
        txtOutContractBreakdown .setText( data.get(0).toString( ) );
        txtOutSettleAmount      .setText( data.get(0).toString( ) );
        txtOutSettleReason      .setText( data.get(0).toString( ) );
        txtOutTotalPaid         .setText( data.get(0).toString( ) );
        txtOutPaidBreakdown     .setText( data.get(0).toString( ) );
        txtOutAmountOwe         .setText( data.get(0).toString( ) );
    }
}
    

