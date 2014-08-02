package collection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan
 */
public class ConsoleLog {
    
    final private static ConsoleLog consoleLog = new ConsoleLog( );
    
    static StringBuilder strLog = new StringBuilder( );
    
    private static int transaction = 0;
    
    private ConsoleLog( ){
        
    }
    
    public static ConsoleLog getInstance( ){
        
        return consoleLog;
    }
    
    public static void append( String log ){
        
        strLog.append( "\n>> " + log );
    }
    
    public static String retrieveLog( ){
        
        return strLog.toString( );
    }
    
    public static int getTransactionCount( ){
        
        transaction++;
        
        return transaction;
    }
}
