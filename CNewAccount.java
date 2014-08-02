package collection;

//import static javax.swing.GroupLayout.Alignment.BASELINE;
//import static javax.swing.GroupLayout.Alignment.CENTER;
//import static javax.swing.GroupLayout.Alignment.LEADING;
//import static javax.swing.GroupLayout.Alignment.TRAILING;
//
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import java.text.DecimalFormat;
//import java.util.Vector;
//
//import javax.swing.BorderFactory;
//import javax.swing.BoxLayout;
//import javax.swing.ButtonGroup;
//import javax.swing.GroupLayout;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//
//import javax.swing.JFrame;
//import javax.swing.JInternalFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JRadioButton;
//import javax.swing.JScrollPane;
//import javax.swing.JTextField;
//import javax.swing.border.EtchedBorder;
//import javax.swing.event.InternalFrameAdapter;
//import javax.swing.event.InternalFrameEvent;
//
//public class CNewAccount extends MInternalFrame {
//        
//    private static final long serialVersionUID = -4674282708644072352L;
//    private static final String[] TREATMENT_TYPE_1 = {"PHASE I","PHASE II","TMJ","COMBINED","OTHER"};
//    private static final String[] TREATMENT_TYPE_2 = {"PHASE I","PHASE II","TMJ","COMBINED","XRAYS","OTHER"};
//    private static final String[] STATE = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", 
//        "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", 
//        "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY", " " };
//
//    private MSAccessDatabase trans;
//
//    GridLayout topPanelLayout = new GridLayout(0,2);
//    GridLayout lowerPanelLayout = new GridLayout(0,2);
//    Font font = new Font("Tahoma", Font.BOLD, 12);
//    DecimalFormat df2 = new DecimalFormat("#0.00");
//    DecimalFormat df = new DecimalFormat();
//    ExceptionLog log = new ExceptionLog();
//
//    MDesktopPane desktop = MDesktopPane.getInstance();
//
//    Account c                  = new Account();
//    JFrame cFrame              = new JFrame(); 
//    JPanel mPanel              = new JPanel();
//    JPanel topPanel            = new JPanel();
//    JPanel lowerPanel          = new JPanel();
//    JPanel patientRightPanel   = new JPanel();
//    JPanel patientLeftPanel    = new JPanel();
//    JPanel treatmentInfoPanel  = new JPanel();
//    JPanel collectionInfoPanel = new JPanel();
//    JScrollPane scrollPane     = new JScrollPane(mPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//
//    private String title;
//    private String dataID;
//
//    //Start of Patient Info Panel Variables
//    //Start of Patient Right Component
//    private JLabel patientLabel;
//    private JLabel officeLabel;
//    private JLabel address1Label;
//    private JLabel address2Label;
//    private JLabel cityLabel;
//    private JLabel stateLabel;
//    private JLabel zipLabel;
//    private JLabel emailLabel;
//
//    private JTextField patientField;	
//    private JTextField officeField;
//    private JTextField address1Field;
//    private JTextField address2Field;
//    private JTextField cityField;	
//    private JComboBox stateCBox;
//    private JTextField zipField;
//    private JTextField emailField;
//
//    //Start of Patient Left Component
//    private JLabel homePhoneLabel;
//    private JLabel mobilePhoneLabel;
//    private JLabel debtorLabel;
//    private JLabel dobahLabel;
//    private JLabel driverLicenseLabel;
//    private JLabel lastDOSLabel;
//
//    private JTextField homePhoneField;	
//    private JTextField mobilePhoneField;	
//    private JTextField debtorField;	
//    private MDateField dobahField;	
//    private JTextField driverLicenseField;	
//    private MDateField lastDOSField;
//    //End of Patient Info Panel Variables
//
//    //Start of Treatment Info components
//    private JLabel tx1TypeLabel;
//    @SuppressWarnings("rawtypes")
//    private JComboBox tx1TypeCBox;
//
//    private JLabel tx1CostLabel;
//    private JTextField tx1CostField;
//
//    private JLabel tx2CheckLabel;
//    private JRadioButton yes2ndButton;
//    private JRadioButton no2ndButton;
//
//    ButtonGroup tx2Group = new ButtonGroup();
//
//    private JLabel tx2TypeLabel;
//    @SuppressWarnings("rawtypes")
//    private JComboBox tx2TypeCBox;
//
//    private JLabel tx2CostLabel;
//    private JTextField tx2CostField;
//
//    private JLabel amountPaidLabel;
//    private JTextField amountPaidField;
//
//    private JLabel applianceMadeLabel;
//    private JRadioButton yesApplianceMadeButton;
//    private JRadioButton noApplianceMadeButton;
//
//    ButtonGroup applianceMadeGroup = new ButtonGroup();
//
//    private JLabel nApplianceLabel;
//    private JTextField nApplianceField;
//
//    private JLabel applianceDeliverLabel;
//    private JRadioButton yesDeliverButton;
//    private JRadioButton noDeliverButton;
//
//    ButtonGroup deliverGroup = new ButtonGroup();
//
//    private JLabel xraysTakenLabel;
//    private JRadioButton yesXraysTakenButton;
//    private JRadioButton noXraysTakenButton;
//
//    ButtonGroup xraysGroup = new ButtonGroup();
//
//    private JLabel nVisitLabel;
//    private JTextField nVisitField;
//
//    //Start of Collection Info components
//    private JLabel tx1CostOutLabel;
//    private JTextField tx1AmountOutField;
//
//    private JLabel tx1TypeOutLabel;
//    private JTextField tx1TypeOutField;
//
//    private JLabel tx2CostOutLabel;
//    private JTextField tx2AmountOutField;
//
//    private JLabel tx2TypeOutLabel;
//    private JTextField tx2TypeOutField;
//
//    private JLabel liableAmountOutLabel;
//    private JTextField liableAmountOutField;
//
//    private JLabel contractBreakdownOutLabel;
//    private JTextField contractBreakdownOutField;
//
//    private JLabel settleAmountOutLabel;
//    private JTextField settleAmountOutField;
//
//    private JLabel settleReasonOutLabel;
//    private JTextField settleReasonOutField;
//
//    private JLabel totalPaidOutLabel;
//    private JTextField totalPaidOutField;
//
//    private JLabel paidBreakdownOutLabel;
//    private JTextField paidBreakdownOutField;
//
//    private JLabel amountOweOutLabel;
//    private JTextField amountOweOutField;
//
//    private JLabel contractType;
//    private JRadioButton englishButton;
//    private JRadioButton spanishButton;	
//    private JLabel outputAlertLabel;
//
//    ButtonGroup contractGroup;
//
//    //INTERACTIVE BUTTON
//    private JButton CalculateButton;
//    private JButton ResetButton;
//    private JButton EnterDBButton;
//
//    private boolean affirmClose;
//        
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
////XXXXXXXXXXXXXXXXXXXXXXXXXXX         Constructor         XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//   
//    public CNewAccount( ){
//        
//        this("Account Edit",null);
//    }
//    
//    public CNewAccount(String title,String dataID){
//        
//            this.title = title;
//            this.dataID = dataID;
//            
//            initComponents();
//            addEventListeners();
//            builtComponents();
//            builtThis();
//            loadData();
//    }
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
////XXXXXXXXXXXXXXXXXXXXXXXXXXX        Initialization       XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//	
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    private void initComponents()
//    {
//            trans = MSAccessDatabase.getInstance( );
//            patientLabel = new JLabel("PATIENT:");
//            patientField = new JTextField();
//            patientLabel.setFont(font);
//
//            officeLabel = new JLabel("OFFICE:");
//            officeField = new JTextField();
//            officeLabel.setFont(font);
//
//            address1Label = new JLabel("ADDRESS 1:");
//            address1Field = new JTextField();
//            address1Label.setFont(font);
//
//            address2Label = new JLabel("ADDRESS 2:");
//            address2Field = new JTextField();
//            address2Label.setFont(font);
//
//            cityLabel = new JLabel("CITY:");
//            cityField = new JTextField();
//            cityLabel.setFont(font);
//
//            stateLabel = new JLabel("ST:");
//            stateCBox  = new JComboBox(STATE);
//            stateLabel.setFont(font);
//
//            zipLabel = new JLabel("ZIP:");
//            zipField = new JTextField();
//            zipLabel.setFont(font);
//
//            emailLabel = new JLabel("EMAIL:");
//            emailField = new JTextField();
//            emailLabel.setFont(font);
//
//            homePhoneLabel = new JLabel("HOME PHONE:");
//            homePhoneField = new JTextField();
//            homePhoneLabel.setFont(font);
//
//            mobilePhoneLabel = new JLabel("MOBILE PHONE:");
//            mobilePhoneField = new JTextField();
//            mobilePhoneLabel.setFont(font);
//
//            debtorLabel = new JLabel("DEBTOR:");
//            debtorField = new JTextField();
//            debtorLabel.setFont(font);
//
//            dobahLabel = new JLabel("DOBAH:");
//            dobahField = new MDateField();
//            dobahLabel.setToolTipText("Date of Birth of Account Holder");
//            dobahLabel.setFont(font);
//
//
//            driverLicenseLabel = new JLabel("DRIVER LICENSE:");
//            driverLicenseField = new JTextField();
//            driverLicenseLabel.setFont(font);
//
//            lastDOSLabel = new JLabel("LAST DATE OF SERVICE:");
//            lastDOSField = new MDateField();
//            lastDOSLabel.setFont(font);
//
//            topPanel.setBorder(BorderFactory.createTitledBorder(
//                            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Patient Info"));
//            treatmentInfoPanel.setBorder(BorderFactory.createTitledBorder(
//                            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Treatment Info"));
//            collectionInfoPanel.setBorder(BorderFactory.createTitledBorder(
//                            BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Collection Info"));
//
//
//            //Bottom Treatment Info components
//            tx1TypeLabel = new JLabel("TREATMENT 1 TYPE:");
//            tx1TypeCBox  = new JComboBox(TREATMENT_TYPE_1);
//            tx1TypeLabel.setFont(font);
//
//            tx1CostLabel = new JLabel("TREATMENT 1 COST:");
//            tx1CostField = new JTextField(15);
//            tx1CostLabel.setFont(font);
//
//            tx2CheckLabel = new JLabel("IS THERE A 2ND CONTRACT:");
//            yes2ndButton  = new JRadioButton("YES");
//            no2ndButton   = new JRadioButton("NO");
//            tx2Group.add(yes2ndButton);
//            tx2Group.add(no2ndButton);
//            no2ndButton.setSelected(true);
//
//            tx2CheckLabel.setFont(font);
//
//            tx2TypeLabel = new JLabel("TREATMENT 2 TYPE:");
//            tx2TypeCBox  = new JComboBox(TREATMENT_TYPE_2);
//            tx2TypeCBox.setEnabled(false);
//
//            tx2TypeLabel.setFont(font);
//
//            tx2CostLabel = new JLabel("TREATMENT 2 COST:");
//            tx2CostField = new JTextField(15);
//            tx2CostField.setEnabled(false);
//            tx2CostLabel.setFont(font);
//
//            amountPaidLabel = new JLabel("AMOUNT PAID:");
//            amountPaidField = new JTextField(10);
//            amountPaidLabel.setFont(font);
//
//            applianceMadeLabel     = new JLabel("APPLIANCE MADE:");
//            yesApplianceMadeButton = new JRadioButton("YES");
//            noApplianceMadeButton  = new JRadioButton("NO");
//            applianceMadeGroup.add(yesApplianceMadeButton);
//            applianceMadeGroup.add(noApplianceMadeButton);
//            noApplianceMadeButton.setSelected(true);
//
//            applianceMadeLabel.setFont(font);
//
//            nApplianceLabel  = new JLabel("# OF APPLIANCE:");
//            nApplianceField  = new JTextField(15);
//            nApplianceField.setEnabled(false);
//            nApplianceLabel.setFont(font);
//
//            applianceDeliverLabel = new JLabel("HAS APPLIANCE BEEN DELIVERED:");
//            yesDeliverButton      = new JRadioButton("YES");
//            noDeliverButton       = new JRadioButton("NO");
//            yesDeliverButton.setEnabled(false);
//            noDeliverButton.setEnabled(false);
//            deliverGroup.add(yesDeliverButton);
//            deliverGroup.add(noDeliverButton);
//            noDeliverButton.setSelected(true);
//            applianceDeliverLabel.setFont(font);
//
//            xraysTakenLabel     = new JLabel("HAS XRAYS BEEN TAKEN:");
//            yesXraysTakenButton = new JRadioButton("YES");
//            noXraysTakenButton  = new JRadioButton("NO");
//            xraysGroup.add(yesXraysTakenButton);
//            xraysGroup.add(noXraysTakenButton);
//            noXraysTakenButton.setSelected(true);
//            yesXraysTakenButton.setEnabled(false);
//            noXraysTakenButton.setEnabled(false);
//
//            xraysTakenLabel.setFont(font);		
//
//            nVisitLabel = new JLabel("# OF VISITS:");
//            nVisitField = new JTextField(15);
//            nVisitLabel.setFont(font);
//            //END OF TREATMENT INFO INIT
//
//            //START OF INIT COLLECTION INFO COMPONENTS
//            tx1TypeOutLabel = new JLabel("TREATMENT 1 TYPE:");
//            tx1TypeOutField = new JTextField(30);
//            tx1TypeOutField.setFocusable(false);
//            tx1TypeOutLabel.setFont(font);
//
//            tx1CostOutLabel = new JLabel("TREATMENT 1 COST:");
//            tx1AmountOutField = new JTextField(30);
//            tx1AmountOutField.setFocusable(false);
//            tx1CostOutLabel.setFont(font);
//
//            tx2TypeOutLabel = new JLabel("TREATMENT 2 TYPE:");
//            tx2TypeOutField = new JTextField(30);
//            tx2TypeOutField.setFocusable(false);
//            tx2TypeOutLabel.setFont(font);
//
//            tx2CostOutLabel = new JLabel("TREATMENT 2 COST:");
//            tx2AmountOutField = new JTextField(30);
//            tx2AmountOutField.setFocusable(false);
//            tx2CostOutLabel.setFont(font);
//
//            liableAmountOutLabel = new JLabel("LIABLE TREATMENT AMOUNT:");
//            liableAmountOutField = new JTextField(30);
//            liableAmountOutField.setFocusable(false);
//            liableAmountOutLabel.setFont(font);
//
//            contractBreakdownOutLabel = new JLabel("CONTRACT BREAKDOWN:");
//            contractBreakdownOutField = new JTextField(30);
//            contractBreakdownOutField.setFocusable(false);
//            contractBreakdownOutLabel.setFont(font);
//
//            settleAmountOutLabel = new JLabel("SETTLE AMOUNT:");
//            settleAmountOutField = new JTextField(30);
//            settleAmountOutField.setFocusable(false);
//            settleAmountOutLabel.setFont(font);
//
//            settleReasonOutLabel = new JLabel("SETTLE REASON:");
//            settleReasonOutField = new JTextField(30);
//            settleReasonOutField.setFocusable(false);
//            settleReasonOutLabel.setFont(font);
//
//            totalPaidOutLabel = new JLabel("TOTAL PAID:");
//            totalPaidOutField = new JTextField(30);
//            totalPaidOutField.setFocusable(false);
//            totalPaidOutLabel.setFont(font);
//
//            paidBreakdownOutLabel = new JLabel("PAID BREAKDOWN:");
//            paidBreakdownOutField = new JTextField(30);
//            paidBreakdownOutField.setFocusable(false);
//            paidBreakdownOutLabel.setFont(font);
//
//            amountOweOutLabel = new JLabel("AMOUNT OWE");
//            amountOweOutField = new JTextField(30);
//            amountOweOutField .setFocusable(false);
//            amountOweOutLabel.setFont(font);
//
//            outputAlertLabel = new JLabel("");
//            outputAlertLabel.setForeground(new Color(255,25,25));
//            outputAlertLabel.setFont(font);
//
//            contractType  = new JLabel("TYPE OF CONTRACT:");
//            englishButton = new JRadioButton("ENGLISH");
//            spanishButton = new JRadioButton("SPANISH");
//            contractGroup = new ButtonGroup();
//            contractGroup.add(englishButton);
//            contractGroup.add(spanishButton);
//            contractType.setFont(font);
//            englishButton.setFont(font);
//            spanishButton.setFont(font);
//            //END OF COLLECTION INFO COMPONENTS INIT
//
//            //INTERRACTIVE BUTTON INIT
//            CalculateButton = new JButton("CALCULATE");
//
//            ResetButton = new JButton("RESET");
//            ResetButton.setBackground(new Color(250,0,0));
//            ResetButton.setForeground(new Color(255,255,255));
//
//            EnterDBButton = new JButton("ENTER TO DATABASE");
//            EnterDBButton.setEnabled(false);
//    }
//	
//
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
////XXXXXXXXXXXXXXXXXXXXXXXXXXX        Built Components     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX	
//	
//    private void builtComponents(){
//
//        buildPatientRComponent();
//        buildPatientLComponent();
//        topPanel.setLayout(topPanelLayout);
//        topPanel.add(patientRightPanel);
//        topPanel.add(patientLeftPanel);	
//
//        buildTxInfoComponent();
//        buildCollectionInfoComponent();
//        lowerPanel.setLayout(lowerPanelLayout);
//        lowerPanel.add(treatmentInfoPanel);
//        lowerPanel.add(collectionInfoPanel);
//
//        mPanel.setLayout(new BoxLayout(mPanel, BoxLayout.Y_AXIS));
//        mPanel.add(topPanel);
//        mPanel.add(new JLabel(" "));
//        mPanel.add(lowerPanel);
//        mPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20)); 
//    }
//
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
////XXXXXXXXXXXXXXXXXXXXXXXXXXX          Build This          XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//    
//    private void builtThis(){
//        
//        this.setClosable(true);
//        this.setTitle(title);
//        this.setIconifiable(true);
//        this.setResizable(true);
//        this.setMaximizable(true);
//        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
//        this.setVisible(true);
//        this.setSize(desktop.getSize());
//        
//        scrollPane.setViewportView(mPanel);
//        this.add(scrollPane);
//
//        desktop.add(this);
//        this.moveToFront();
//    }
//    
//    protected void affirmAction(){}
//    
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
////XXXXXXXXXXXXXXXXXXXXXXXXXXX       AddEventListeners      XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//	
//    private void addEventListeners()
//    {
//       yes2ndButton.addActionListener(new ActionListener() {
//    	   
//            @Override
//            public void actionPerformed( ActionEvent evt ) {
//            	
//        		tx2TypeCBox.setEnabled(true);
//        		tx2CostField.setEnabled(true);      
//            }
//        });
//       
//        no2ndButton.addActionListener(new ActionListener() {
//            
//            @Override
//            public void actionPerformed( ActionEvent evt ) {
//            	
//        		if(tx2TypeCBox.getSelectedItem() == "XRAYS")
//        		{
//        			tx2CostField.setText("250.00");
//        			tx2CostField.setEnabled(false);
//        			yesXraysTakenButton.setEnabled(true);
//        			noXraysTakenButton.setEnabled(true);	
//        		}
//        		else
//        		{
//        			tx2CostField.setText("");
//        			tx2CostField.setEnabled(true);
//        			yesXraysTakenButton.setEnabled(false);
//        			noXraysTakenButton.setEnabled(false);				
//        		}
//            }
//        });
//        
//        yesApplianceMadeButton.addActionListener(new java.awt.event.ActionListener() {
//        	
//            @Override
//            public void actionPerformed( ActionEvent evt ) {
//            	
//                nApplianceField.setEnabled(true);
//                yesDeliverButton.setEnabled(true);
//                noDeliverButton.setEnabled(true);
//                c.setApplianceMade(true);
//            }
//        });
//        
//        noApplianceMadeButton.addActionListener(new ActionListener() {
//        	
//            @Override
//            public void actionPerformed( ActionEvent evt ) {
//            	
//                nApplianceField.setEnabled(false);
//                yesDeliverButton.setEnabled(false);
//                noDeliverButton.setEnabled(false);
//                c.setApplianceMade(false);
//                c.setnApplianceOk(true);
//            }
//        });
//        
//        yesDeliverButton.addActionListener(new ActionListener() {
//        	
//            @Override
//            public void actionPerformed( ActionEvent evt ) {
//            	
//            	c.setApplianceDel(true);
//            }
//        });
//        
//        noDeliverButton.addActionListener(new ActionListener() {
//        	
//            @Override
//            public void actionPerformed( ActionEvent evt ) {
//            	
//            	c.setApplianceDel(false);
//            }
//        });
//
//        yesXraysTakenButton.addActionListener(new ActionListener() {
//        	
//            @Override
//            public void actionPerformed( ActionEvent evt ) {
//            	
//        		c.setXraysTaken(true);
//            }
//        });
//        
//        noXraysTakenButton.addActionListener(new ActionListener() {
//        	
//            @Override
//            public void actionPerformed( ActionEvent evt ) {
//            	
//        		c.setXraysTaken(false);
//            }
//        });
//        
//        CalculateButton.addActionListener(new ActionListener() {
//            
//            @Override
//            public void actionPerformed( ActionEvent evt ) {
//                
//                tx1AmountOutField.setText("");
//                tx1TypeOutField.setText("");
//                tx2AmountOutField.setText("");
//                tx2TypeOutField.setText("");
//                totalPaidOutField.setText("");
//                settleReasonOutField.setText("");
//                paidBreakdownOutField.setText("");
//                settleAmountOutField.setText("");
//                liableAmountOutField.setText("");
//                contractBreakdownOutField.setText("");
//                amountOweOutField.setText("");
//                //outputAlertLabel.setText("");
//                EnterDBButton.setEnabled(false);
//                //tx1Error.setText("");
//                outputAlertLabel.setText("");
//				    
//                c = new Account(); 
//
//                handleOutputLabels();
// 
//                if (c.isCalculateOk())
//                {
//                    //calculate settle amount
//                    calculate();
//                
//                   if (c.isCalculateOk())
//            {
//            //calculate settle amount
//            calculate();
//        
//            //HANDLE PAID BREAKDOWN
//            if(c.hasXraysTaken())
//            {
//                if (c.getTotalPaid() >= 250)
//                {
//                    paidBreakdownOutField.setText("$" + df2.format(c.getTotalPaid() - 250)
//                        + "/TX + $250/XRAYS");
//                }
//                if (c.getTotalPaid() < 250)
//                {
//                    paidBreakdownOutField.setText("$0.00/TX + $" + df2.format(250 - c.getTotalPaid())
//                        + "/XRAYS");
//                }
//
//            }
//            else
//            {
//            	paidBreakdownOutField.setText("-");
//            }
//
//            //HANDLE AMOUNT OWE
//            c.setBalance();
//            amountOweOutField.setText("$" + df2.format(c.getBalance()));
//        
//            //HANDLE NO SEND ALERT
//            //DISPLAY "DO NOT SENT TO COLLECTION" IF PT HAS PAID IN FULL OR OVERPAID THAN SETTLEAMOUNT
//            if ((c.getSettledAmount() - c.getTotalPaid()) <  50)
//            {
//            	EnterDBButton.setEnabled(false);
//                outputAlertLabel.setText("!!!DO NOT SEND TO COLLECTION!!!");
//                EnterDBButton.setVisible(false);
//            }
//            else
//            {
//                outputAlertLabel.setText("");
//                EnterDBButton.setEnabled(true);
//                EnterDBButton.setVisible(true);
//            }
//        }
//        }}});
//        
//        ResetButton.addActionListener(new ActionListener() {
//        	
//            @Override
//            public void actionPerformed( ActionEvent evt ) {
//
//                //RESET OUTPUTS
//                patientField.setText("");
//                debtorField.setText("");
//                dobahField.setText("");
//                driverLicenseField.setText("");
//                officeField.setText("");
//                address1Field.setText("");
//                address2Field.setText("");
//                cityField.setText("");
//                stateCBox.setSelectedIndex(0);
//                zipField.setText("");
//                homePhoneField.setText("");
//                mobilePhoneField.setText("");
//                emailField.setText(""); 
//                englishButton.setSelected(true);
//
//                //RESET INPUTS
//                contractGroup.clearSelection();
//                tx2Group.clearSelection();
//                applianceMadeGroup.clearSelection();
//                deliverGroup.clearSelection();
//                xraysGroup.clearSelection();
//                no2ndButton.setSelected(true);
//                noDeliverButton.setEnabled(false);
//                yesDeliverButton.setEnabled(false);
//                noDeliverButton.setSelected(true);
//                noApplianceMadeButton.setSelected(true);
//                yesXraysTakenButton.setEnabled(false);
//                noXraysTakenButton.setEnabled(false);
//                noXraysTakenButton.setSelected(true);
//                tx1TypeCBox.setSelectedIndex(0);
//                tx2TypeCBox.setSelectedIndex(0);
//                tx2TypeCBox.setEnabled(false);
//                tx2CostField.setEnabled(false);
//                tx1CostField.setText("");
//                tx2CostField.setText("");
//                tx2CostField.setEnabled(false);
//                nApplianceField.setText("");
//                nApplianceField.setEnabled(false);
//                amountPaidField.setText("");
//                nVisitField.setText("");
//                lastDOSField.setText("");
//                EnterDBButton.setVisible(true);
//
//                //reset output
//                tx1AmountOutField.setText("");
//                tx1TypeOutField.setText("");
//                tx2AmountOutField.setText("");
//                tx2TypeOutField.setText("");
//                totalPaidOutField.setText("");
//                settleReasonOutField.setText("");
//                paidBreakdownOutField.setText("");
//                settleAmountOutField.setText("");
//                liableAmountOutField.setText("");
//                contractBreakdownOutField.setText("");
//                amountOweOutField.setText("");
//                //outputAlertLabel.setText("");
//                EnterDBButton.setEnabled(false);
//                //tx1Error.setText("");
//                outputAlertLabel.setText("");
//
//                c = new Account(); 
//            }
//        });
//        
//        EnterDBButton.addActionListener(new ActionListener() {
//        	
//            @Override
//            public void actionPerformed( ActionEvent evt ) {
//                        
//                String query = null;
//
//                //set the proper table for insert
//                if (englishButton.isSelected() || spanishButton.isSelected()){
//
//                    query = "insert into COLLECTION_INFO_DB ([PATIENT NAME],[ACCOUNT HOLDER],[DOBAH],[DL],[OFFICE],[ADDRESS 1],"
//                        + "[ADDRESS 2],[CITY],[STATE],[ZIP],[HOME PHONE],[MOBILE PHONE],[EMAIL ADDRESS],[CONTRACT 1],[CONTRACT TYPE 1],"
//                        + "[CONTRACT 2],[CONTRACT TYPE 2],[LIABLE CONTRACT AMOUNT],[CONTRACT BREAKDOWN],[SETTLED AMOUNT],[SETTLED REASON],"
//                        + "[AMOUNT PAID],[PAID BREAKDOWN],[AMOUNT OWE],[LAST DATE OF SERVICE],[LANGUAGE]) values ( '" + patientField.getText() + "','" + debtorField.getText()+ "','" 
//                        + dobahField.getText() + "','" + driverLicenseField.getText()+ "','" + officeField.getText() + "','" +address1Field.getText() + "','" +address2Field.getText() + "','"
//                        + cityField.getText() + "','" + (String) stateCBox.getSelectedItem() + "','" + zipField.getText() + "','" + homePhoneField.getText() + "','" + mobilePhoneField.getText() + "','"
//                        + emailField.getText() + "','" + tx1AmountOutField.getText() + "','" + tx1TypeOutField.getText() + "','" + tx2AmountOutField.getText() 
//                        + "','" + tx2TypeOutField.getText() + "','" + liableAmountOutField.getText() + "','" + contractBreakdownOutField.getText() + "','" 
//                        + settleAmountOutField.getText() + "','" + settleReasonOutField.getText() + "','" + totalPaidOutField.getText() + "','" 
//                        + paidBreakdownOutField.getText() + "','" + amountOweOutField.getText() + "','" + lastDOSField.getText() + "','" 
//                        + (englishButton.isSelected() ? "ENGLISH" : "SPANISH") + "')";
//
//                        String result = trans.executeInsert( query.toUpperCase( ) );
//                        EnterDBButton.setEnabled(false);
//                        EnterDBButton.setVisible(false);
//                        outputAlertLabel.setText(result);	
//                }
//
//                else {
//                        JOptionPane.showMessageDialog(new JFrame(), "Please Select Type of Contract.", "Contract Type Alert", JOptionPane.WARNING_MESSAGE);
//                }
//            }
//        });
//        
//        this.addInternalFrameListener( new InternalFrameAdapter(){
//            
//            @Override
//            public void internalFrameClosed( InternalFrameEvent event ){
//                
//                CNewAccount.this.affirmAction();
//            } 
//    });
//    }
//
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
////XXXXXXXXXXXXXXXXXXXXXXXXXXX        Private Classes       XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
////XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX   
//    
//	private void buildPatientRComponent()
//	{
//		GroupLayout layout = new GroupLayout(patientRightPanel);
//		patientRightPanel.setLayout(layout);
//                layout.setAutoCreateGaps(true);
//                layout.setAutoCreateContainerGaps(true);
//        
//		layout.setHorizontalGroup(layout.createSequentialGroup() //SET HORIZONTAL ALIGNMENTS OF COMPONENTS
//				
//				.addGroup(layout.createParallelGroup(LEADING) //ARRANCE TOP COMPONENTS
//						.addGroup(layout.createSequentialGroup()
//								.addGroup(layout.createParallelGroup(LEADING)
//										.addComponent(patientLabel)
//										.addComponent(officeLabel)
//										.addComponent(address1Label)
//										.addComponent(address2Label)
//										.addComponent(cityLabel)
//										.addComponent(emailLabel))
//								.addGroup(layout.createParallelGroup(LEADING)
//										.addComponent(patientField)
//										.addComponent(officeField)
//										.addComponent(address1Field)
//										.addComponent(address2Field)						
//										.addGroup(layout.createSequentialGroup()
//												.addComponent(cityField)
//												.addGroup(layout.createParallelGroup(TRAILING)
//														.addComponent(stateLabel))
//												.addGroup(layout.createParallelGroup(LEADING)
//																.addComponent(stateCBox))
//												.addGroup(layout.createParallelGroup(TRAILING)
//														.addComponent(zipLabel))
//												.addGroup(layout.createParallelGroup(LEADING)	
//														.addComponent(zipField)))
//										.addComponent(emailField))))
//					);
//		
//		layout.setVerticalGroup(layout.createSequentialGroup() //SET VERTICAL ALIGNEMTNS OF COMPONENTS				
//				.addGap(10,10,10)
//		        .addGroup(layout.createParallelGroup(BASELINE) //ARRANGE VERTICAL ALIGNMENT OF TOP COMPONENTS
//						.addGroup(layout.createSequentialGroup()
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(patientLabel)
//										.addComponent(patientField))
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(officeLabel)
//										.addComponent(officeField))
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(address1Label)
//										.addComponent(address1Field))
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(address2Label)
//										.addComponent(address2Field))
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(cityLabel)
//										.addComponent(cityField)
//										.addGroup(layout.createSequentialGroup()
//												.addGroup(layout.createParallelGroup(BASELINE)
//														.addComponent(cityField)
//														.addComponent(stateLabel)
//														.addComponent(stateCBox)
//														.addComponent(zipLabel)
//														.addComponent(zipField))))
//		        				.addGroup(layout.createParallelGroup(BASELINE)
//		        						.addComponent(emailLabel)
//		        						.addComponent(emailField))))
//		        						);
//	}
//	
//	private void buildPatientLComponent()
//	{
//		GroupLayout layout = new GroupLayout(patientLeftPanel);
//		patientLeftPanel.setLayout(layout);
//        layout.setAutoCreateGaps(true);
//        layout.setAutoCreateContainerGaps(true);
//        
//		layout.setHorizontalGroup(layout.createSequentialGroup() //SET HORIZONTAL ALIGNMENTS OF COMPONENTS
//				
//				.addGroup(layout.createParallelGroup(LEADING) //ARRANCE TOP COMPONENTS
//						.addGroup(layout.createSequentialGroup()
//								.addGroup(layout.createParallelGroup(LEADING)
//											.addComponent(homePhoneLabel)
//											.addComponent(mobilePhoneLabel)
//											.addComponent(debtorLabel)
//											.addComponent(dobahLabel)
//											.addComponent(driverLicenseLabel)
//											.addComponent(lastDOSLabel))														
//								.addGroup(layout.createParallelGroup(LEADING)
//											.addComponent(homePhoneField)
//											.addComponent(mobilePhoneField)
//											.addComponent(debtorField)
//											.addComponent(dobahField)
//											.addComponent(driverLicenseField)
//											.addComponent(lastDOSField))))
//					);
//		
//		layout.setVerticalGroup(layout.createSequentialGroup() //SET VERTICAL ALIGNEMTNS OF COMPONENTS				
//				.addGap(10,10,10)
//		        .addGroup(layout.createParallelGroup(BASELINE) //ARRANGE VERTICAL ALIGNMENT OF TOP COMPONENTS
//						.addGroup(layout.createSequentialGroup()
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(homePhoneLabel)
//										.addComponent(homePhoneField)
//										
//										)
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(mobilePhoneLabel)
//										.addComponent(mobilePhoneField)
//										
//										)
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(debtorLabel)
//										.addComponent(debtorField))
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(dobahLabel)
//										.addComponent(dobahField))
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addGroup(layout.createSequentialGroup()
//												.addGroup(layout.createParallelGroup(BASELINE)
//														.addComponent(driverLicenseLabel)
//														.addComponent(driverLicenseField))))
//		        				.addGroup(layout.createParallelGroup(BASELINE)
//		        						.addComponent(lastDOSLabel)
//		        						.addComponent(lastDOSField)))))	  
//		        		 ;
//	}
//
//	private void buildTxInfoComponent()
//	{
//		GroupLayout layout = new GroupLayout(treatmentInfoPanel);
//		treatmentInfoPanel.setLayout(layout);
//        layout.setAutoCreateGaps(true);
//        layout.setAutoCreateContainerGaps(true);
//        
//		layout.setHorizontalGroup(layout.createSequentialGroup() //SET HORIZONTAL ALIGNMENTS OF COMPONENTS
//				.addGroup(layout.createParallelGroup(LEADING) //ARRANCE TOP COMPONENTS
//						.addGroup(layout.createSequentialGroup()
//								.addGroup(layout.createParallelGroup(TRAILING)
//										.addComponent(tx1TypeLabel)
//										.addComponent(tx1CostLabel)
//										.addComponent(tx2CheckLabel)
//										.addComponent(tx2TypeLabel)
//										.addComponent(tx2CostLabel)
//										.addComponent(amountPaidLabel)
//										.addComponent(applianceMadeLabel)
//										.addComponent(nApplianceLabel)
//										.addComponent(applianceDeliverLabel)
//										.addComponent(xraysTakenLabel)
//										.addComponent(nVisitLabel))
//								.addGroup(layout.createParallelGroup(LEADING)
//										.addComponent(tx1TypeCBox)
//										.addComponent(tx1CostField)
//										.addGroup(layout.createSequentialGroup()
//												.addComponent(yes2ndButton)
//												.addComponent(no2ndButton))
//										.addComponent(tx2TypeCBox)
//										.addComponent(tx2CostField)
//										.addComponent(amountPaidField)
//										.addGroup(layout.createSequentialGroup()
//												.addComponent(yesApplianceMadeButton)
//												.addComponent(noApplianceMadeButton))
//										.addComponent(nApplianceField)
//										.addGroup(layout.createSequentialGroup()
//												.addComponent(yesDeliverButton)
//												.addComponent(noDeliverButton))
//										.addGroup(layout.createSequentialGroup()
//												.addComponent(yesXraysTakenButton)
//												.addComponent(noXraysTakenButton))
//										.addComponent(nVisitField)
//										.addComponent(CalculateButton, CENTER, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))))
//										);
//	
//		layout.setVerticalGroup(layout.createSequentialGroup() //SET VERTICAL ALIGNEMTNS OF COMPONENTS				
//		          .addGroup(layout.createParallelGroup(BASELINE) //ARRANGE VERTICAL ALIGNMENT OF BOTTOM COMPONENTS
//		        		  .addGroup(layout.createSequentialGroup()
//		        				.addGroup(layout.createParallelGroup(BASELINE)
//		        						 .addComponent(tx1TypeLabel)
//		        						 .addComponent(tx1TypeCBox))		        						  
//		        				.addGroup(layout.createParallelGroup(BASELINE)
//		        						 .addComponent(tx1CostLabel)
//		        						 .addComponent(tx1CostField))	
//		        				.addGap(30,30,30)
//		        				.addGroup(layout.createParallelGroup(BASELINE)
//		        						 .addComponent(tx2CheckLabel)
//		        						 .addComponent(yes2ndButton)
//		        						 .addComponent(no2ndButton))
//		        				.addGroup(layout.createParallelGroup(BASELINE)
//		        						 .addComponent(tx2TypeLabel)
//		        						 .addComponent(tx2TypeCBox))
//		        				.addGroup(layout.createParallelGroup(BASELINE)
//		        						.addComponent(tx2CostLabel)
//		        						.addComponent(tx2CostField))
//		        				.addGap(30,30,30)
//						        .addGroup(layout.createParallelGroup(BASELINE)
//				        				.addComponent(amountPaidLabel)
//		        						.addComponent(amountPaidField))
//		        				.addGap(30,30,30)
//								.addGroup(layout.createParallelGroup(BASELINE)
//				        				.addComponent(applianceMadeLabel)
//				        				.addComponent(yesApplianceMadeButton)
//				        				.addComponent(noApplianceMadeButton))
//								.addGroup(layout.createParallelGroup(BASELINE)
//						        		.addComponent(nApplianceLabel)
//						        		.addComponent(nApplianceField))
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(applianceDeliverLabel)
//										.addComponent(yesDeliverButton)
//										.addComponent(noDeliverButton))
//								.addGap(30,30,30)
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(xraysTakenLabel)
//										.addComponent(yesXraysTakenButton)
//										.addComponent(noXraysTakenButton))
//								.addGap(30,30,30)
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(nVisitLabel)
//										.addComponent(nVisitField))
//								.addGap(30,30,30)
//								.addGroup(layout.createParallelGroup(BASELINE)
//										.addComponent(CalculateButton)
//										))))  
//		        		 ;
//	}
//	
//	private void buildCollectionInfoComponent()
//	{
//            GroupLayout layout = new GroupLayout(collectionInfoPanel);
//            collectionInfoPanel.setLayout(layout);
//            layout.setAutoCreateGaps(true);
//            layout.setAutoCreateContainerGaps(true);
//        
//            layout.setHorizontalGroup(layout.createSequentialGroup() //SET HORIZONTAL ALIGNMENTS OF COMPONENTS
//                .addGroup(layout.createParallelGroup(LEADING) //ARRANCE TOP COMPONENTS
//                    .addGroup(layout.createSequentialGroup()
//                        .addGroup(layout.createParallelGroup(TRAILING)
//                            .addGroup(layout.createSequentialGroup()
//                                .addGroup(layout.createParallelGroup(LEADING)
//                                                .addComponent(tx1TypeOutLabel)
//                                                .addComponent(tx1CostOutLabel)
//                                                .addComponent(tx2TypeOutLabel)
//                                                .addComponent(tx2CostOutLabel)
//                                                .addComponent(liableAmountOutLabel)
//                                                .addComponent(contractBreakdownOutLabel)
//                                                .addComponent(settleAmountOutLabel)
//                                                .addComponent(settleReasonOutLabel)
//                                                .addComponent(totalPaidOutLabel)
//                                                .addComponent(paidBreakdownOutLabel)
//                                                .addComponent(amountOweOutLabel))														
//                                .addGroup(layout.createParallelGroup(LEADING)
//                                                .addComponent(tx1TypeOutField)
//                                                .addComponent(tx1AmountOutField)
//                                                .addComponent(tx2TypeOutField)
//                                                .addComponent(tx2AmountOutField)
//                                                .addComponent(liableAmountOutField)
//                                                .addComponent(contractBreakdownOutField)
//                                                .addComponent(settleAmountOutField)
//                                                .addComponent(settleReasonOutField)
//                                                .addComponent(totalPaidOutField)
//                                                .addComponent(paidBreakdownOutField)
//                                                .addComponent(amountOweOutField)
//                                                .addGroup(layout.createSequentialGroup()
//                                                                .addGap(80,80,80)
//                                                                .addComponent(contractType)
//                                                                .addGroup(layout.createParallelGroup(TRAILING)
//                                                                                .addComponent(englishButton))
//                                                                .addGroup(layout.createParallelGroup(TRAILING)
//                                                                                .addComponent(spanishButton)))				
//                                                .addComponent(EnterDBButton, CENTER, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
//                                                .addComponent(outputAlertLabel,CENTER, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
//                                                .addComponent(ResetButton, CENTER, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)))
//                                    .addGroup(layout.createParallelGroup(LEADING) //HANDLE LOWER INPUT COMPONENTS
//                                                    .addGroup(layout.createSequentialGroup()))))));
//
//            layout.setVerticalGroup(layout.createSequentialGroup() //SET VERTICAL ALIGNEMTNS OF COMPONENTS				
//                .addGroup(layout.createParallelGroup(BASELINE) //ARRANGE VERTICAL ALIGNMENT OF BOTTOM COMPONENTS
//                    .addGroup(layout.createSequentialGroup()
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(tx1TypeOutLabel)
//                            .addComponent(tx1TypeOutField))
//                        .addGap(10,10,10)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(tx1CostOutLabel)
//                            .addComponent(tx1AmountOutField))
//                        .addGap(10,10,10)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(tx2TypeOutLabel)
//                            .addComponent(tx2TypeOutField))
//                        .addGap(10,10,10)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(tx2CostOutLabel)
//                            .addComponent(tx2AmountOutField))
//                        .addGap(10,10,10)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                           .addComponent(liableAmountOutLabel)
//                           .addComponent( liableAmountOutField))
//                        .addGap(10,10,10)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(contractBreakdownOutLabel)
//                            .addComponent(contractBreakdownOutField))
//                        .addGap(10,10,10)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(settleAmountOutLabel)
//                            .addComponent(settleAmountOutField))
//                        .addGap(10,10,10)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(settleReasonOutLabel)
//                            .addComponent(settleReasonOutField))
//                        .addGap(10,10,10)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(totalPaidOutLabel)
//                            .addComponent(totalPaidOutField))
//                        .addGap(10,10,10)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(paidBreakdownOutLabel)
//                            .addComponent(paidBreakdownOutField))
//                        .addGap(10,10,10)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(amountOweOutLabel)
//                            .addComponent(amountOweOutField))
//                        .addGap(30,30,30)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(contractType)
//                            .addComponent(englishButton)
//                            .addComponent(spanishButton))
//                        .addGap(30,30,30)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(EnterDBButton)
//                            .addComponent(outputAlertLabel))
//                        .addGap(20,20,20)
//                        .addGroup(layout.createParallelGroup(BASELINE)
//                            .addComponent(ResetButton)))) )
//               ;
//	}
//
//	private void calculate() //calculate total amount due
//	{
//	    //IF VISITS EXCEED MORE THAN 6, SETTLE AMOUNT IS TOTAL TX COST
//	    if(c.getnVisits() > 6)
//	    {   
//	        c.totalTreatmentDue();
//	        settleAmountOutField.setText("$" + df2.format(c.getSettledAmount()));
//	        settleReasonOutField.setText("FULL CONTRACT AMOUNT DUE");
//	    }
//	    
//	    //SETTLE IF VISITS IS LESS THAN SIX AND APPLIANCE HAS BEEN DELIVERED
//	    //60% OF EACH TX CONTRACT IS DUE + # OF VISITS * 150; IF XRAYS WERE TAKEN
//	    //ADD 250 TO SETTLE AMOUNT
//	    if((c.hasApplianceDel() == true) && (c.getnVisits() <= 6))
//	    {
//	        if(yes2ndButton.isSelected()== true)
//	        {
//	            if (c.hasXraysContract() == true)
//	            {   
//	                
//	                if (c.hasXraysTaken())
//	                {
//	                    c.sixtyPercent(250);
//	                    settleAmountOutField.setText("$" + df2.format(c.getSettledAmount()));
//	                    settleReasonOutField.setText("60%/TX + " + (c.getnVisits() - 1) 
//	                            + " VISITS + XRAYS");
//	                }
//	                else
//	                {
//	                    c.sixtyPercent(0);
//	                    settleAmountOutField.setText("$" + df2.format(c.getSettledAmount()));
//	                    settleReasonOutField.setText("60%/" + tx1TypeCBox.getSelectedItem() 
//	                            + " + "+ (c.getnVisits() - 1)
//	                            + " VISITS");
//	                }
//	            }
//	            else
//	            {
//	                    c.sixtyPercent2nd();
//	                    settleAmountOutField.setText("$" + df2.format(c.getSettledAmount()));
//	                    settleReasonOutField.setText("60%/" + tx1TypeCBox.getSelectedItem() 
//	                            + " + 60%/" + tx2TypeCBox.getSelectedItem() 
//	                            + " + " + (c.getnVisits() - 1) + " VISITS");
//	            }
//	        }
//	        
//	       if (no2ndButton.isSelected() == true)
//	       {
//	             c.sixtyPercent2nd();
//	             settleAmountOutField.setText("$" + df2.format(c.getSettledAmount()));
//	             settleReasonOutField.setText("60%/TX + " + (c.getnVisits() - 1) 
//	                         + " VISITS");
//	                
//	       }
//	    }
//
//	    
//	    //IF APPLIANCE WAS NOT DELIVERED, CHARGE 650 PER APPLIANCE 
//	    if((c.hasApplianceMade() == true) && (c.getnVisits() <= 6) && (c.hasApplianceDel() == false) && 
//	            (c.hasXraysTaken() == false))
//	    {
//	        c.onlyAppliance();
//	        settleAmountOutField.setText("$" + df2.format(c.getSettledAmount()));
//	        
//	        if(c.getnVisits() < 2)
//	        {
//	            settleReasonOutField.setText("$650 x " + c.getnAppliance() + " APPLIANCE");
//	        }
//	        else
//	        {
//	            settleReasonOutField.setText("$650 x " + c.getnAppliance() + " APPLIANCE + "
//	                    + (c.getnVisits() - 1) + " VISITS");
//	        }
//	    }
//	    
//	    if((c.hasApplianceMade() == true) && (c.getnVisits() <= 6) && (c.hasApplianceDel() == false) && 
//	            (c.hasXraysTaken() == true))
//	    {
//	        c.onlyAppliance(c.getTx2Cost());
//	        settleAmountOutField.setText("$" + df2.format(c.getSettledAmount()));
//	        
//	        if (c.getnVisits() < 2)
//	        {
//	            settleReasonOutField.setText("$650 x " + c.getnAppliance() + " APPLIANCE + XRAYS");
//	        }
//	        else
//	        {
//	            settleReasonOutField.setText("$650 x " + c.getnAppliance() + " APPLIANCE + "
//	                    + (c.getnVisits() - 1) + " VISITS + XRAYS"); 
//	        }
//	    }
//	    
//	    //IF APPLIANCE WAS NOT MADE, CHARGE 150 FOR STUDY MODEL ONLY
//	    if((c.hasApplianceMade()== false) && (c.getnVisits() <= 6) && (c.hasXraysTaken() == true))
//	    {
//	        c.onlyStudyModel(250);
//	        settleAmountOutField.setText("$" + df2.format(c.getSettledAmount())); 
//	        settleReasonOutField.setText("STUDY MODEL + XRAYS");
//	    }
//	    
//	    if((c.hasApplianceMade()== false) && (c.getnVisits() <= 6) && (c.hasXraysTaken() == false))
//	    {
//	        c.onlyStudyModel(0);
//	        settleAmountOutField.setText("$" + df2.format(c.getSettledAmount())); 
//	        settleReasonOutField.setText("STUDY MODEL ONLY");
//	    }
//	    
//	}
//	
//	private void handleOutputLabels()  //handles the proper outputs
//	{
//	            
//	        //HANDLE TX 1 AMOUNT
//	        try
//	        {
//                    tx1CostField.setForeground(new java.awt.Color(0, 0, 0));
//                    tx1TypeOutField.setText((String) tx1TypeCBox.getSelectedItem());
//	            tx1AmountOutField.setText( "$" + df2.format( Integer.parseInt(tx1CostField.getText() ) ) );
//	            tx2AmountOutField.setText("");
//	            tx2TypeOutField.setText("");
//	            //tx1Error.setForeground(new java.awt.Color(0,0,0));
//	            //tx1Error.setText("");
//	            String amount = tx1CostField.getText();
//	            c.setTx1Cost(Double.parseDouble(amount));
//	            c.setTx1AmountOk(true);
//	            tx1TypeOutField.setText((String) tx1TypeCBox.getSelectedItem());
//	        }
//	        catch(NumberFormatException e)
//	        {
//	        	tx1CostField.setForeground(new java.awt.Color(204, 0, 0));
//	            //tx1Error.setForeground(new java.awt.Color(204,0,0));
//	            //tx1Error.setText("Input Mismatch");
//	            c.setTx1AmountOk(false);
//	            log.insertGUILog(e + " at tx1CostField");
//	        }
//	        
//	        //HANDLE TX 2 AMOUNT
//	        if(yes2ndButton.isSelected())
//	        {
//	            if(tx2TypeCBox.getSelectedItem() != "XRAYS")
//	            {
//	                try
//	                {
//	                    tx2CostField.setForeground(new java.awt.Color(0, 0, 0));
//	                    tx2AmountOutField.setText( "$" + df2.format( Integer.parseInt( tx2CostField.getText() ) ) );
//	                   // tx2Error.setForeground(new java.awt.Color(0,0,0));
//	                    //tx2Error.setText("");
//	                    String amount = tx2CostField.getText();
//	                    c.setTx2Cost(Double.parseDouble(amount));
//	                    c.setTx2AmountOk(true);
//	                    tx2TypeOutField.setText((String) tx2TypeCBox.getSelectedItem());
//	            
//	                }
//	                catch(NumberFormatException e)
//	                {
//	                    tx2CostField.setForeground(new java.awt.Color(204, 0, 0));
//	                    //tx2Error.setForeground(new java.awt.Color(204,0,0));
//	                   // tx2Error.setText("Input Mismatch");
//	                    c.setTx2AmountOk(false);
//	                    tx2TypeOutField.setText((String) tx2TypeCBox.getSelectedItem());
//	                    log.insertGUILog(e + " at tx2CostField");
//	                }   
//	            }
//	            else
//	            {
//	            yesXraysTakenButton.setEnabled(true);
//	            noXraysTakenButton.setEnabled(true);
//	            tx2CostField.setEnabled(false);
//	            c.setXraysContract(true, 250);
//	            tx2TypeOutField.setText((String) tx2TypeCBox.getSelectedItem());
//	            c.setTx2Cost(250);
//	            tx2AmountOutField.setText("$250.00");
//	            c.setTx2AmountOk(true);
//	            }
//	        }
//
//	        
//	        //HANDLE AMOUNT PAID
//	        try
//	        {
//	            amountPaidField.setForeground(new java.awt.Color(0, 0, 0));
//	            totalPaidOutField.setText("$" + df2.format(Double.parseDouble(amountPaidField.getText())));
//	            //amountPaidError.setForeground(new java.awt.Color(0,0,0));
//	            //amountPaidError.setText("");
//	            String amount = amountPaidField.getText();
//	            c.totalPaid(Double.parseDouble(amount));
//	            c.setAmountPaidOk(true);
//	        }
//	        catch(NumberFormatException e)
//	        {
//	            amountPaidField.setForeground(new java.awt.Color(204, 0, 0));
//	            //amountPaidError.setForeground(new java.awt.Color(204,0,0));
//	            //amountPaidError.setText("Input Mismatch");
//	            c.setAmountPaidOk(false);
//	            log.insertGUILog(e + " at amountPaidField");
//	        }
//	        
//	        //HANDLE NUMBER OF APPLIANCE        
//	        if(yesApplianceMadeButton.isSelected())
//	        {
//	            try
//	            {
//	                nApplianceField.setForeground(new java.awt.Color(0, 0, 0));
//	                c.setnAppliance(Integer.parseInt(nApplianceField.getText())); 
//	                c.setnApplianceOk(true);
//	            }
//	            catch (NumberFormatException e)
//	            {
//	                nApplianceField.setForeground(new java.awt.Color(204, 0, 0));
//	                c.setnApplianceOk(false);
//	                log.insertGUILog(e + " at nApplianceField");
//	            }
//	        }
//	        
//	        //HANDLE NUMBER OF VISITS
//	        try
//	        {
//	            nVisitField.setForeground(new java.awt.Color(0, 0, 0));
//	            c.setnVisits(Integer.parseInt(nVisitField.getText()));  
//	            c.setnVisitsOk(true);
//	        }
//	        catch (NumberFormatException e)
//	        {
//	            nVisitField.setForeground(new java.awt.Color(204, 0, 0));
//	            //nVisitError.setForeground(new java.awt.Color(204,0,0));
//	            //nVisitError.setText("Input Mismatch");
//	            c.setnVisitsOk(false);
//	            log.insertGUILog(e + " at nVisitField");
//	        }
//	        
//	        //HANDLE LIABLE AMOUNT
//	        
//	        if ((yes2ndButton.isSelected() && (tx2TypeCBox.getSelectedItem() == "XRAYS")) 
//	                || yesXraysTakenButton.isSelected())
//	        {
//	            c.setTx2AmountOk(true);
//	        }
//	        
//	        
//	        if (c.isCalculateOk())
//	        {
//	            c.setLiableAmount();
//	            liableAmountOutField.setText("$" + df2.format(c.getLiableAmount()));
//	            liableAmtBreakdown();
//	        }
//	}
//	
//	private void liableAmtBreakdown() //handle output field for Liable amount breakdown
//	{
//	        
//	    //Handle Contract Breakdown
//	    if (yes2ndButton.isSelected())
//	    {
//	        if((tx2TypeCBox.getSelectedItem() != "XRAYS") || c.hasXraysTaken())
//	        {
//	            contractBreakdownOutField.setText("$" + df.format( c.getTx1Cost( ) ).toString( ).replace(",", "")
//	            + "/" + tx1TypeCBox.getSelectedItem()
//	            + " + $" + df.format( c.getTx2Cost( ) ).toString( ).replace(",", "")  
//                    + "/" + tx2TypeCBox.getSelectedItem( ) ) ;  
//	        }
//	        
//	        if(!c.hasXraysTaken() && (tx2TypeCBox.getSelectedItem() == "XRAYS"))
//	        {
//	            contractBreakdownOutField.setText("XRAYS WERE NOT TAKEN");
//	        }
//	    } 
//	    else
//	    {
//	    	contractBreakdownOutField.setText("");
//	    }
//	}
//        
//        private void loadData(){
//            
//            Object[] info;
//            
//            if( dataID != null ){
//                
//                Vector<Vector> data = trans.executeRetrieve("select * from COLLECTION_INFO_DB where ID = " + dataID );
//                
//                info = data.get(1).toArray();
//                
//                patientField             .setText( info[1] != null ? info[1].toString( ) : "" );	
//                debtorField              .setText( info[2] != null ? info[2].toString( ) : "" );	
//                dobahField               .setText( info[3] != null ? info[3].toString( ) : "" );
//                driverLicenseField       .setText( info[4] != null ? info[4].toString( ) : "" );
//                officeField              .setText( info[5] != null ? info[5].toString( ) : "" );
//                
//                address1Field            .setText( info[6] != null ?info[6].toString( )  : "" );
//                address2Field            .setText( info[7] != null ?info[7].toString( )  : "" );
//                cityField                .setText( info[8] != null ?info[8].toString( )  : "" );
//                stateCBox                .setSelectedItem( info[9].toString( ) );
//                zipField                 .setText( info[10] != null ? info[10].toString( ) : "" );
//                homePhoneField           .setText( info[11] != null ? info[11].toString( ) : "" );
//                mobilePhoneField         .setText( info[12] != null ? info[12].toString( ) : "" );               
//                emailField               .setText( info[13] != null ? info[13].toString( ) : "" );     
//                 
//                tx1CostField             .setText( info[14] != null ? info[14].toString( ).replace("$", "") : "" );
//                tx1AmountOutField        .setText( info[14] != null ? info[14].toString( ) : "" );
//                tx1TypeOutField          .setText( info[15] != null ? info[15].toString( ) : "" );
//                tx2CostField             .setText( info[16] != null ? info[16].toString( ).replace("$", "") : "" );
//                tx2AmountOutField        .setText( info[16] != null ? info[16].toString( ) : "" );
//                tx2TypeOutField          .setText( info[17] != null ? info[17].toString( ) : "" );
//                liableAmountOutField     .setText( info[18] != null ? info[18].toString( ) : "" );
//                contractBreakdownOutField.setText( info[19] != null ? info[19].toString( ) : "" );
//                
//                settleAmountOutField     .setText( info[20] != null ? info[20].toString( ) : "" );
//                settleReasonOutField     .setText( info[21] != null ? info[21].toString( ) : "" );
//                amountPaidField          .setText( info[22] != null ? info[22].toString( ).replace("$", "") : "" );
//                totalPaidOutField        .setText( info[22] != null ? info[22].toString( ) : "" );
//                paidBreakdownOutField    .setText( info[23] != null ? info[23].toString( ) : "" );
//                amountOweOutField        .setText( info[24] != null ? info[24].toString( ) : "" );
//                lastDOSField             .setText( info[25] != null ? info[25].toString( ) : "" );
//                
//                if( info[16] != null )
//                    yes2ndButton.setSelected(true);
//                else
//                    no2ndButton.setSelected(false);
//                
//                if( info[26].toString().equals("ENGLISH") )
//                    englishButton.setSelected(true);
//                else
//                    spanishButton.setSelected(true);
//                
//
//                if( info[27] != null){
//                    if( info[27].toString().equals("YES") )
//                        yesApplianceMadeButton.setSelected(true);
//                    else
//                        noApplianceMadeButton.setSelected(true);
//                }
//                else{       
//                    if( info[21].toString().contains("APPLIANCE") )
//                        yesApplianceMadeButton.setSelected(true);
//                    else
//                        noApplianceMadeButton.setSelected(true);
//                }
//
//                if( info[28] != null){
//                    nApplianceField.setText( info[28].toString() );
//                }
//                
//                if( info[29] != null){
//                    
//                    if( info[29].toString().equals("YES") )
//                        yesDeliverButton.setSelected(true);
//                    else
//                        noDeliverButton.setSelected(true);
//                }
//                else{
//                    
//                    if( info[21].toString().contains("APPLIANCE") || info.toString().contains("TREATMENT COMPLETED"))
//                        yesDeliverButton.setSelected(true);
//                    else
//                        noDeliverButton.setSelected(true);
//                }
//                
//                
//                                
//                if( info[30] != null ){
//                    
//                    if( info[30].toString().equals("YES") )
//                        yesXraysTakenButton.setSelected(true);
//                    else
//                        noXraysTakenButton.setSelected(true);   
//                }   
//                else{
//                    
//                    if( info[21].toString().contains("XRAY") )
//                        yesXraysTakenButton.setSelected(true);
//                    else
//                        noXraysTakenButton.setSelected(true);
//                }
//                
//                if( info[31] != null ){
//                   nApplianceField.setText( info[31].toString() ); 
//                }
//                                
//                                
//                                
//                                
//                                
//                                
//                                
//                
//
//                
//
//                
//                
//                
//
////                nApplianceField.setText( info[0][12].toString() );
////                nVisitField    .setText( info[0][12].toString() );
////                tx1TypeCBox    .setSelectedItem( info[0][1].toString() );
////                tx2TypeCBox    .setSelectedItem( info[0][1].toString() );
//                
//                
//                
//                    
//                
//                
////	private MDateField lastDOSField.setText( info[0][12].toString() );
//            }
//        }
//
//}
