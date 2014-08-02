package collection;





public class Account {
    final double LIABLE_PERCENTAGE = 0.60;
    final int VISIT_COST = 150;
    final int APPLIANCE_COST = 650;
    final int XRAYS_COST = 250;
    final int MAX_VISIT = 5;
    
    private StringBuilder strSettleReason      = new StringBuilder( );
    private StringBuilder strContractBreakdown = new StringBuilder( );
    private StringBuilder strPaidBreakdown     = new StringBuilder( );
    
    private boolean is2ndContract        = false;
    private boolean isApplianceDelivered = false;
    private boolean isApplianceMade      = false;
    private boolean isXraysTaken         = false;
    
    private int tx1Cost        = 0;
    private int tx2Cost        = 0;
    private int amountPaid     = 0;
    private int applianceCount = 0;
    private int visitCount     = 0;
    private int liableAmount   = 0;
    private int settleAmount   = 0;

    private String tx1Type;
    private String tx2Type;
    
    public Account( ){
        
    }

    public void setTx1Cost( int amount ){

        tx1Cost = amount;
    }

    public int getTx1Cost( ){

        return tx1Cost;
    }

    public void setTx2Cost( int amount ){

        tx2Cost = amount;
    }

    public int getTx2Cost( ){

        return tx2Cost;
    }

    public void setAmountPaid( int amount ){

        amountPaid = amount;
    }

    public int getAmountPaid( ){

        return amountPaid;
    }
    
    public int getLiableAmount( ){
        
        return liableAmount;
    }

    public void setApplianceCount( int count ){

        applianceCount = count;
    }

    public int getApplianceCount( ){

        return applianceCount;
    }

    public void setVisitCount( int count ){

        visitCount = count - 1;
    }

    public int getVisitCount( ){

        return visitCount;
    }
    
    public void setTx1Type( String type ){
        
        tx1Type = type;
    }
    
    public String getTx1Type( ){
        
        return tx1Type;
    }
    
    public void setTx2Type( String type ){

        tx2Type = type;
    }
    
    public String getTx2Type( ){
        
        return tx2Type;
    }
    
    public void set2ndContract( boolean bool ){
        
        is2ndContract = bool;
    }
    
    public boolean is2ndContract( ){
        
        return is2ndContract;
    }
    
    public void setApplianceDelivered( boolean bool ){
        
        isApplianceDelivered = bool;
    }
    
    public boolean isApplianceDelivered( ){
        
        return isApplianceDelivered;
    }
    
    public void setApplianceMade( boolean bool ){
        
        isApplianceMade = bool;
    }
    
    public boolean getApplianceMade( ){
        
        return isApplianceMade;
    }
    
    public void setXraysTaken( boolean bool ){
        
        isXraysTaken = bool;
    }
    
    public boolean isXraysTaken( ){
        
        return isXraysTaken;
    }
    
    public void setSettleAmount( int amount ){
        
        settleAmount = amount;
    }
    
    public int getSettleAmount( ){
        
        return settleAmount;
    }
    
    public int getAmountOwe( ){
        
        return settleAmount - amountPaid;
    }
    
    public String getSettleReason( ){
        
        return strSettleReason.toString( );
    }
    
    public String getContractBreakdown( ){
        
        return strContractBreakdown.toString( );
    }
    
    public String getPaidBreakdown( ){
        
        return strPaidBreakdown.toString( );
    }
    
    public void calculate( ){
        
        strSettleReason.setLength(0);
        strContractBreakdown.setLength(0);
        strPaidBreakdown.setLength(0);
        
        liableAmount = tx1Cost + tx2Cost;
    
        settleAmount = 150;
        strSettleReason.append( "STUDY MODELS ONLY" );
        
        if( isApplianceMade ){
            
            settleAmount = applianceCount * APPLIANCE_COST;
            strSettleReason.setLength(0);
            strSettleReason.append( "$" + APPLIANCE_COST + " X " + applianceCount + " APPLIANCES" );
        
            if( isApplianceDelivered ){
                
                settleAmount = (int)( liableAmount * LIABLE_PERCENTAGE );
                strSettleReason.setLength(0);
                
                if( tx2Type == null || !tx2Type.equals( "XRAYS" ) )
                    strSettleReason.append( "60%/TX" );
                
                else
                    strSettleReason.append( "60%/TX" );
            }
                
            
            settleAmount += visitCount * VISIT_COST;
            strSettleReason.append( " + " + visitCount + " VISITS" );
        }
        
        if( tx2Type != null ){
            
            if( tx2Type.equals( "XRAYS" ) ){

                if( isXraysTaken ){
                    settleAmount += XRAYS_COST;
                    strSettleReason.append( " + " + "XRAYS" );
                }
                else
                    liableAmount -= XRAYS_COST;
            }
        }
        

        
        strContractBreakdown.append( "$" + tx1Cost + "/" + tx1Type );
        
        if( is2ndContract ){
            
            if( tx2Type.equals( "XRAYS" ) ){

                if( isXraysTaken )
                    strContractBreakdown.append( " + $250/XRAYS" );
                else{
                    strContractBreakdown.setLength(0);
                    strContractBreakdown.append( "XRAYS WERE NOT TAKEN" );
                }
            }
            else if( tx2Type == null ){
                strContractBreakdown.setLength(0);
                strContractBreakdown.append( "NO XRAYS CONTRACT" );
            }
            else{
                strContractBreakdown.append( " + $" + tx2Cost + "/" + tx2Type );
            }
        }
        
        if( isXraysTaken ){
            
            if( amountPaid >= XRAYS_COST )
                strPaidBreakdown.append( "$" + (amountPaid - XRAYS_COST) + "/TX + " + "$250/XRAYS" );
            
            else if( amountPaid < XRAYS_COST )
                strPaidBreakdown.append( "$0/TX + $" + amountPaid + "/XRAYS" );
        }
        
        if( visitCount > MAX_VISIT || settleAmount > liableAmount ){
            
            settleAmount = liableAmount;
            
            if( isXraysTaken )
                settleAmount += XRAYS_COST;
            
            strSettleReason.setLength(0);
            strSettleReason.append( "FULL CONTRACT AMOUNT DUE" );
        }
    }
}
