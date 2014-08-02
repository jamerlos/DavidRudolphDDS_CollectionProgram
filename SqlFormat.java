package collection;

/******************************************************************************
 *                                                                            *
 *                      Class: SqlFormat                                      *
 *                                                                            *
 *   Class that provide useful methods for sql formatting                     *
 *                                                                            *
 *****************************************************************************/

/**
 * Class that provide useful methods for sql formatting   
 * @author Juan
 */
public class SqlFormat {
    
    public static String format( String query ){
        
        query = query.replace( "'","''" );
        
        return query;
    }
    
}
