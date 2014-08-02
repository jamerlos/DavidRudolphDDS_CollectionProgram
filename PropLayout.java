package collection;


//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXX                                                                                              XXXXX
//XXX                                       Class: PropLayout                                      XXXXX
//XXX                                                                                              XXXXX
//XXX     A Layout Manager that creates a single column of rows. This Layout Manger has the        XXXXX
//XXX     following features:                                                                      XXXXX
//XXX       - Rows may be vertically aligned relative to the parent container.                     XXXXX
//XXX       - Rows may be individually horizontally aligned relative to the parent container.      XXXXX
//XXX       - Components may be individually vertically aligned relative to their row.             XXXXX
//XXX       - Parent padding must be specified gloabally.                                          XXXXX
//XXX       - Row and component padding may be specified globally and individually.                XXXXX
//XXX       - Component sizes must be specified individually.                                      XXXXX
//XXX       - Rows will be added top to bottom, unless designated with a row index.                XXXXX
//XXX       - Components will be added left to right, unless designated with a column index.       XXXXX
//XXX       - Padding and comp size preferred values may be specifed as a static pixel count       XXXXX
//XXX         or proportional value. The proportional value must be > 0 and <= 1. The final        XXXXX
//XXX         value is calculated by multiplying the proportional value with the free space.       XXXXX
//XXX         The free space is the parent container less static defined padding and               XXXXX
//XXX         components.                                                                          XXXXX
//XXX       - Padding and comp size min/max values may only be specified as static pixel           XXXXX
//XXX         counts. When a proportional pref value is calcuated to a final value that is         XXXXX
//XXX         outside the bounds of the specified min/max values, the final value is frozen        XXXXX
//XXX         at the appropriate bound. This is referred to as bounded. The max size is            XXXXX
//XXX         preferred over the min size on conflict (max < min).                                 XXXXX
//XXX       - All preferred values are required globally and are pre-initiallized with             XXXXX
//XXX         default values. Individual preferred values are optional, in which case the          XXXXX
//XXX         global default is used. The comp size preferred value is a special case. The         XXXXX
//XXX         global default is user alterable by way of the component's setPreferredSize( )       XXXXX
//XXX         method.                                                                              XXXXX
//XXX       - All min/max values are optional both globally and individually. The comp size        XXXXX
//XXX         min/max values are a special case. The global defaults are user alterable by         XXXXX
//XXX         way of the component's setMinimumSize( ) / setMaximumSize( ) methods.                XXXXX
//XXX       - The preferred, minimum, and maximum layout sizes may each be set to a fixed          XXXXX
//XXX         dimension. Some components use these values to affect their behavior.                XXXXX
//XXX                                                                                              XXXXX
//XXX     There are two ways to add a component to PropLayout. Both involve use of the parent      XXXXX
//XXX     container method parent.add( component, object ). The first argument is the              XXXXX
//XXX     component being added. The second may be one of the following:                           XXXXX
//XXX       - A PropLayout.Constraint object. All required fields not specified will be            XXXXX
//XXX         initilized to their global default value.                                            XXXXX
//XXX       - A String object. All required fields not specified will be initilized to their       XXXXX
//XXX         global default value. A specific syntax must be followed:                            XXXXX
//XXX           - All values may be skipped as long as preceeding separators are used.             XXXXX
//XXX           - The row and column locations have a shortcut syntax. If blank or -1, the         XXXXX
//XXX             component will be added to the last row, last column. If "+" or < -1, the        XXXXX
//XXX             row / column is incremented before the component is added.                       XXXXX
//XXX           - The horizontalRowAlign is a single character that may be one of:                 XXXXX
//XXX             l=left, c=center, r=right.                                                       XXXXX
//XXX           - The verticalCompAlign is a single character that may be one of:                  XXXXX
//XXX             t=top, m=middle, b=bottom.                                                       XXXXX
//XXX                                                                                              XXXXX
//XXX           - "location(row,column) | ..."                                                     XXXXX
//XXX             "width(pref:min:max), height(pref:min:max) | ..."                                XXXXX
//XXX             "rowGap(top,bottom,left,right) | ..."                                            XXXXX
//XXX             "compGap(left,right,top,bottom) | ..."                                           XXXXX
//XXX             "horizontalRowAlign ([l,c,r]), verticalCompAlignment ([t,m,b])"                  XXXXX
//XXX                                                                                              XXXXX
//XXX           - "  | widthPref, heightPref"                                                      XXXXX
//XXX           - "+ | widthPref, heightPref | rowGapTop | compGapLeft"                            XXXXX
//XXX                                                                                              XXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

//Future Enhancements:
//    - terms for bounding type: selfish vs distribute, rigid vs flex
//    - global boolean to set bounding type: setPropBoundType( boolean distribute )
//    - each comp has a double called 'bounded weight' to define comp bounded expansion weight
//    - prop width: default to flex, allow for rigid
//        - flex: bounded comp divides prop to other comps...always fills "intended" space...comps resize slower/faster according to static and non-static rows
//        - rigid: no division of props...no assumed "intentions"...comps always resize according to their prop and static-only rows
//    - prop height: default to flex, allow for rigid
//        - rigid height ignores static or bounded comps on non-static rows
//        - total intended height from tallest prop on each row
//        - bounded comp divides prop to other comps only when tallest comp in row

import java.awt.*;
import java.util.*;

/**
 * A custom proportional layout manager.
 *
 * @author Mike Maughan
 */
public class PropLayout implements LayoutManager2{

    private GapSize parentGap;
    private GapSize rowGap;
    private GapSize compGap;

    private VertAlign parentAlign;
    private HorzAlign rowAlign;
    private VertAlign compAlign;

    private Dimension prefLayoutSize;
    private Dimension minLayoutSize;
    private Dimension maxLayoutSize;

    private ArrayList<RowConstraint> rowConstraints;
    private ArrayList<ArrayList<Component>> components;
    private ArrayList<ArrayList<CompConstraint>> compConstraints;

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * Constructs an PropLayout.
     */
    public PropLayout( ){

        this.parentGap = new GapSize( 10, 10, 10, 10 );
        this.rowGap    = new GapSize(  0,  0,  5,  0 );
        this.compGap   = new GapSize(  5,  0,  0,  0 );

        this.parentAlign = VertAlign.TOP;
        this.rowAlign    = HorzAlign.CENTER;
        this.compAlign   = VertAlign.TOP;

        this.rowConstraints  = new ArrayList<RowConstraint>( );
        this.components      = new ArrayList<ArrayList<Component>>( );
        this.compConstraints = new ArrayList<ArrayList<CompConstraint>>( );
    }

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Add/Remove Components     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    @Override
    public void addLayoutComponent( String constraint, Component component ){

        addLayoutComponent( component, constraint );
    }

    @Override
    public void addLayoutComponent( Component component, Object objConstraint ){
        try{

            Constraint constraint = null;

            if( component == null || (objConstraint != null && !(objConstraint instanceof Constraint) && !(objConstraint instanceof String)) )
            /******************************************************************************/
            /**/    throw new Exception( "Invalid arguments." );
            /******************************************************************************/

            if( objConstraint == null )
            /******************************************************************************/
            /**/    constraint = new Constraint( );
            /******************************************************************************/

            else if( objConstraint instanceof Constraint )
            /******************************************************************************/
            /**/    constraint = (Constraint)objConstraint;
            /******************************************************************************/

            else if( objConstraint instanceof String ){
            /******************************************************************************/
            /**/    objConstraint = ((String)objConstraint).replaceAll("\\s+","").toLowerCase( );
            /**/    constraint    = new Constraint( );
            /**/
            /**/    String[] constraintTokens = ((String)objConstraint).split( "\\|" );
            /**/
            /**/    if( constraintTokens.length > 0 && !constraintTokens[0].isEmpty( ) ){
            /**/    /**************************************************************************/
            /**/    /**/    String[] positionTokens = constraintTokens[0].split( "," );
            /**/    /**/
            /**/    /**/    if( positionTokens.length > 0 && !positionTokens[0].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    int row = -1;
            /**/    /**/    /**/
            /**/    /**/    /**/    if( positionTokens[0].equals( "+" ) )
            /**/    /**/    /**/        row = -2;
            /**/    /**/    /**/
            /**/    /**/    /**/    else
            /**/    /**/    /**/        row = Integer.parseInt( positionTokens[0] );
            /**/    /**/    /**/
            /**/    /**/    /**/    constraint.row = row;
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**/
            /**/    /**/    if( positionTokens.length > 1 && !positionTokens[1].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    int column = -1;
            /**/    /**/    /**/
            /**/    /**/    /**/    if( !positionTokens[1].equals( "+" ) )
            /**/    /**/    /**/        column = Integer.parseInt( positionTokens[1] );
            /**/    /**/    /**/
            /**/    /**/    /**/    constraint.column = column;
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**************************************************************************/
            /**/    }
            /**/
            /**/    if( constraintTokens.length > 1 && !constraintTokens[1].isEmpty( ) ){
            /**/    /**************************************************************************/
            /**/    /**/    String[] compSizeTokens = constraintTokens[1].split( "," );
            /**/    /**/
            /**/    /**/    if( compSizeTokens.length > 0 && !compSizeTokens[0].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    String[] widthTokens = compSizeTokens[0].split( ":" );
            /**/    /**/    /**/
            /**/    /**/    /**/    if( widthTokens.length > 0 && !widthTokens[0].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double pref = Double.parseDouble( widthTokens[0] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( pref >= 0 )
            /**/    /**/    /**/    /**/        constraint.compSize.width.pref = pref;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( widthTokens.length > 1 && !widthTokens[1].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double min = Double.parseDouble( widthTokens[1] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( min >= 0 )
            /**/    /**/    /**/    /**/        constraint.compSize.width.min = min;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( widthTokens.length > 2 && !widthTokens[2].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double max = Double.parseDouble( widthTokens[2] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( max >= 0 )
            /**/    /**/    /**/    /**/        constraint.compSize.width.max = max;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**/
            /**/    /**/    if( compSizeTokens.length > 1 && !compSizeTokens[1].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    String[] heightTokens = compSizeTokens[1].split( ":" );
            /**/    /**/    /**/
            /**/    /**/    /**/    if( heightTokens.length > 0 && !heightTokens[0].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double pref = Double.parseDouble( heightTokens[0] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( pref >= 0 )
            /**/    /**/    /**/    /**/        constraint.compSize.height.pref = pref;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( heightTokens.length > 1 && !heightTokens[1].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double min = Double.parseDouble( heightTokens[1] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( min >= 0 )
            /**/    /**/    /**/    /**/        constraint.compSize.height.min = min;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( heightTokens.length > 2 && !heightTokens[2].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double max = Double.parseDouble( heightTokens[2] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( max >= 0 )
            /**/    /**/    /**/    /**/        constraint.compSize.height.max = max;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**************************************************************************/
            /**/    }
            /**/
            /**/    if( constraintTokens.length > 2 && !constraintTokens[2].isEmpty( ) ){
            /**/    /**************************************************************************/
            /**/    /**/    String[] rowGapTokens = constraintTokens[2].split( "," );
            /**/    /**/
            /**/    /**/    if( rowGapTokens.length > 0 && !rowGapTokens[0].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    String[] topTokens = rowGapTokens[0].split( ":" );
            /**/    /**/    /**/
            /**/    /**/    /**/    if( topTokens.length > 0 && !topTokens[0].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double pref = Double.parseDouble( topTokens[0] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( pref >= 0 )
            /**/    /**/    /**/    /**/        constraint.rowGap.top.pref = pref;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( topTokens.length > 1 && !topTokens[1].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double min = Double.parseDouble( topTokens[1] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( min >= 0 )
            /**/    /**/    /**/    /**/        constraint.rowGap.top.min = min;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( topTokens.length > 2 && !topTokens[2].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double max = Double.parseDouble( topTokens[2] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( max >= 0 )
            /**/    /**/    /**/    /**/        constraint.rowGap.top.max = max;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**/
            /**/    /**/    if( rowGapTokens.length > 1 && !rowGapTokens[1].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    String[] bottomTokens = rowGapTokens[1].split( ":" );
            /**/    /**/    /**/
            /**/    /**/    /**/    if( bottomTokens.length > 0 && !bottomTokens[0].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double pref = Double.parseDouble( bottomTokens[0] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( pref >= 0 )
            /**/    /**/    /**/    /**/        constraint.rowGap.bottom.pref = pref;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( bottomTokens.length > 1 && !bottomTokens[1].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double min = Double.parseDouble( bottomTokens[1] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( min >= 0 )
            /**/    /**/    /**/    /**/        constraint.rowGap.bottom.min = min;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( bottomTokens.length > 2 && !bottomTokens[2].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double max = Double.parseDouble( bottomTokens[2] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( max >= 0 )
            /**/    /**/    /**/    /**/        constraint.rowGap.bottom.max = max;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**/
            /**/    /**/    if( rowGapTokens.length > 2 && !rowGapTokens[2].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    String[] leftTokens = rowGapTokens[2].split( ":" );
            /**/    /**/    /**/
            /**/    /**/    /**/    if( leftTokens.length > 0 && !leftTokens[0].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double pref = Double.parseDouble( leftTokens[0] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( pref >= 0 )
            /**/    /**/    /**/    /**/        constraint.rowGap.left.pref = pref;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( leftTokens.length > 1 && !leftTokens[1].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double min = Double.parseDouble( leftTokens[1] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( min >= 0 )
            /**/    /**/    /**/    /**/        constraint.rowGap.left.min = min;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( leftTokens.length > 2 && !leftTokens[2].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double max = Double.parseDouble( leftTokens[2] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( max >= 0 )
            /**/    /**/    /**/    /**/        constraint.rowGap.left.max = max;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**/
            /**/    /**/    if( rowGapTokens.length > 3 && !rowGapTokens[3].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    String[] rightTokens = rowGapTokens[3].split( ":" );
            /**/    /**/    /**/
            /**/    /**/    /**/    if( rightTokens.length > 0 && !rightTokens[0].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double pref = Double.parseDouble( rightTokens[0] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( pref >= 0 )
            /**/    /**/    /**/    /**/        constraint.rowGap.right.pref = pref;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( rightTokens.length > 1 && !rightTokens[1].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double min = Double.parseDouble( rightTokens[1] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( min >= 0 )
            /**/    /**/    /**/    /**/        constraint.rowGap.right.min = min;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( rightTokens.length > 2 && !rightTokens[2].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double max = Double.parseDouble( rightTokens[2] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( max >= 0 )
            /**/    /**/    /**/    /**/        constraint.rowGap.right.max = max;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**************************************************************************/
            /**/    }
            /**/
            /**/    if( constraintTokens.length > 3 && !constraintTokens[3].isEmpty( ) ){
            /**/    /**************************************************************************/
            /**/    /**/    String[] compGapTokens = constraintTokens[3].split( "," );
            /**/    /**/
            /**/    /**/    if( compGapTokens.length > 0 && !compGapTokens[0].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    String[] leftTokens = compGapTokens[0].split( ":" );
            /**/    /**/    /**/
            /**/    /**/    /**/    if( leftTokens.length > 0 && !leftTokens[0].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double pref = Double.parseDouble( leftTokens[0] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( pref >= 0 )
            /**/    /**/    /**/    /**/        constraint.compGap.left.pref = pref;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( leftTokens.length > 1 && !leftTokens[1].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double min = Double.parseDouble( leftTokens[1] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( min >= 0 )
            /**/    /**/    /**/    /**/        constraint.compGap.left.min = min;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( leftTokens.length > 2 && !leftTokens[2].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double max = Double.parseDouble( leftTokens[2] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( max >= 0 )
            /**/    /**/    /**/    /**/        constraint.compGap.left.max = max;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**/
            /**/    /**/    if( compGapTokens.length > 1 && !compGapTokens[1].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    String[] rightTokens = compGapTokens[1].split( ":" );
            /**/    /**/    /**/
            /**/    /**/    /**/    if( rightTokens.length > 0 && !rightTokens[0].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double pref = Double.parseDouble( rightTokens[0] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( pref >= 0 )
            /**/    /**/    /**/    /**/        constraint.compGap.right.pref = pref;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( rightTokens.length > 1 && !rightTokens[1].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double min = Double.parseDouble( rightTokens[1] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( min >= 0 )
            /**/    /**/    /**/    /**/        constraint.compGap.right.min = min;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( rightTokens.length > 2 && !rightTokens[2].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double max = Double.parseDouble( rightTokens[2] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( max >= 0 )
            /**/    /**/    /**/    /**/        constraint.compGap.right.max = max;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**/
            /**/    /**/    if( compGapTokens.length > 2 && !compGapTokens[2].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    String[] topTokens = compGapTokens[2].split( ":" );
            /**/    /**/    /**/
            /**/    /**/    /**/    if( topTokens.length > 0 && !topTokens[0].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double pref = Double.parseDouble( topTokens[0] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( pref >= 0 )
            /**/    /**/    /**/    /**/        constraint.compGap.top.pref = pref;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( topTokens.length > 1 && !topTokens[1].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double min = Double.parseDouble( topTokens[1] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( min >= 0 )
            /**/    /**/    /**/    /**/        constraint.compGap.top.min = min;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( topTokens.length > 2 && !topTokens[2].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double max = Double.parseDouble( topTokens[2] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( max >= 0 )
            /**/    /**/    /**/    /**/        constraint.compGap.top.max = max;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**/
            /**/    /**/    if( compGapTokens.length > 3 && !compGapTokens[3].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    String[] bottomTokens = compGapTokens[3].split( ":" );
            /**/    /**/    /**/
            /**/    /**/    /**/    if( bottomTokens.length > 0 && !bottomTokens[0].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double pref = Double.parseDouble( bottomTokens[0] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( pref >= 0 )
            /**/    /**/    /**/    /**/        constraint.compGap.bottom.pref = pref;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( bottomTokens.length > 1 && !bottomTokens[1].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double min = Double.parseDouble( bottomTokens[1] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( min >= 0 )
            /**/    /**/    /**/    /**/        constraint.compGap.bottom.min = min;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**/
            /**/    /**/    /**/    if( bottomTokens.length > 2 && !bottomTokens[2].isEmpty( ) ){
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    /**/    double max = Double.parseDouble( bottomTokens[2] );
            /**/    /**/    /**/    /**/
            /**/    /**/    /**/    /**/    if( max >= 0 )
            /**/    /**/    /**/    /**/        constraint.compGap.bottom.max = max;
            /**/    /**/    /**/    /******************************************************************/
            /**/    /**/    /**/    }
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**************************************************************************/
            /**/    }
            /**/
            /**/    if( constraintTokens.length > 4 && !constraintTokens[4].isEmpty( ) ){
            /**/    /**************************************************************************/
            /**/    /**/    String[] alignTokens = constraintTokens[4].split( "," );
            /**/    /**/
            /**/    /**/    if( alignTokens.length > 0 && !alignTokens[0].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    String row = alignTokens[0];
            /**/    /**/    /**/
            /**/    /**/    /**/    if( row.equals( "l" ) )
            /**/    /**/    /**/        constraint.rowAlign = HorzAlign.LEFT;
            /**/    /**/    /**/
            /**/    /**/    /**/    else if( row.equals( "c" ) )
            /**/    /**/    /**/        constraint.rowAlign = HorzAlign.CENTER;
            /**/    /**/    /**/
            /**/    /**/    /**/    else if( row.equals( "r" ) )
            /**/    /**/    /**/        constraint.rowAlign = HorzAlign.RIGHT;
            /**/    /**/    /**/
            /**/    /**/    /**/    else
            /**/    /**/    /**/        throw new Exception( "Invalid row alignment." );
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**/
            /**/    /**/    if( alignTokens.length > 1 && !alignTokens[1].isEmpty( ) ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    String comp = alignTokens[1];
            /**/    /**/    /**/
            /**/    /**/    /**/    if( comp.equals( "t" ) )
            /**/    /**/    /**/        constraint.compAlign = VertAlign.TOP;
            /**/    /**/    /**/
            /**/    /**/    /**/    else if( comp.equals( "m" ) )
            /**/    /**/    /**/        constraint.compAlign = VertAlign.MIDDLE;
            /**/    /**/    /**/
            /**/    /**/    /**/    else if( comp.equals( "b" ) )
            /**/    /**/    /**/        constraint.compAlign = VertAlign.BOTTOM;
            /**/    /**/    /**/
            /**/    /**/    /**/    else
            /**/    /**/    /**/        throw new Exception( "Invalid component alignment." );
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**************************************************************************/
            /**/    }
            /********************************************************************************/
            }

            RowConstraint rowConstraint   = null;
            CompConstraint compConstraint = null;

            if( rowConstraints.isEmpty( ) ){
            /********************************************************************************/
            /**/    int rowIndexVirt = 0;
            /**/    int colIndexVirt = 0;
            /**/
            /**/    if( constraint.row > 0  )
            /**/        rowIndexVirt = constraint.row;
            /**/
            /**/    if( constraint.column > 0 )
            /**/        colIndexVirt = constraint.column;
            /**/
            /**/    rowConstraint  = new RowConstraint ( rowIndexVirt );
            /**/    compConstraint = new CompConstraint( colIndexVirt );
            /**/
            /**/    rowConstraints .add( rowConstraint                    );
            /**/    components     .add( new ArrayList<Component>( )      );
            /**/    compConstraints.add( new ArrayList<CompConstraint>( ) );
            /**/
            /**/    components     .get(0).add( component      );
            /**/    compConstraints.get(0).add( compConstraint );
            /********************************************************************************/
            }

            else if( constraint.row < -1 ){
            /********************************************************************************/
            /**/    int rowIndexReal = rowConstraints.size( );
            /**/    int rowIndexVirt = rowConstraints.get(rowIndexReal - 1).row + 1;
            /**/
            /**/    int colIndexVirt = 0;
            /**/
            /**/    if( constraint.column > 0 )
            /**/        colIndexVirt = constraint.column;
            /**/
            /**/    rowConstraint  = new RowConstraint ( rowIndexVirt );
            /**/    compConstraint = new CompConstraint( colIndexVirt );
            /**/
            /**/    rowConstraints .add( rowConstraint                    );
            /**/    components     .add( new ArrayList<Component>( )      );
            /**/    compConstraints.add( new ArrayList<CompConstraint>( ) );
            /**/
            /**/    components     .get(rowIndexReal).add( component      );
            /**/    compConstraints.get(rowIndexReal).add( compConstraint );
            /********************************************************************************/
            }

            else if( constraint.row == -1 ){
            /********************************************************************************/
            /**/    int rowIndexReal = rowConstraints.size( ) - 1;
            /**/
            /**/    int colIndexReal = 0;
            /**/    int colIndexVirt = 0;
            /**/
            /**/    ArrayList<CompConstraint> compConstraintRow = compConstraints.get( rowIndexReal );
            /**/
            /**/    if( constraint.column < 0 ){
            /**/    /**************************************************************************/
            /**/    /**/    colIndexReal = compConstraintRow.size( );
            /**/    /**/    colIndexVirt = compConstraintRow.get(colIndexReal - 1).column + 1;
            /**/    /**************************************************************************/
            /**/    }
            /**/
            /**/    else{
            /**/    /**************************************************************************/
            /**/    /**/    colIndexReal = compConstraintRow.size( );
            /**/    /**/    colIndexVirt = constraint.column;
            /**/    /**/
            /**/    /**/    for( int column = 0; column < colIndexReal; column++ ){
            /**/    /**/
            /**/    /**/        int tmpColIndexVirt = compConstraintRow.get(column).column;
            /**/    /**/
            /**/    /**/        if( tmpColIndexVirt >= colIndexVirt ){
            /**/    /**/
            /**/    /**/            colIndexReal = column;
            /**/    /**/            break;
            /**/    /**/        }
            /**/    /**/    }
            /**/    /**************************************************************************/
            /**/     }
            /**/
            /**/     rowConstraint  = rowConstraints.get( rowIndexReal );
            /**/     compConstraint = new CompConstraint( colIndexVirt );
            /**/
            /**/     components     .get(rowIndexReal).add( colIndexReal, component      );
            /**/     compConstraints.get(rowIndexReal).add( colIndexReal, compConstraint );
            /********************************************************************************/
            }

            else{
            /********************************************************************************/
            /**/    int rowIndexReal = rowConstraints.size( );
            /**/    int rowIndexVirt = constraint.row;
            /**/
            /**/    int colIndexReal = 0;
            /**/    int colIndexVirt = 0;
            /**/
            /**/    boolean isFound = false;
            /**/
            /**/    for( int row = 0; row < rowIndexReal; row++ ){
            /**/
            /**/        int tmpRowIndexVirt = rowConstraints.get(row).row;
            /**/
            /**/        if( tmpRowIndexVirt >= rowIndexVirt ){
            /**/
            /**/            rowIndexReal = row;
            /**/
            /**/            if( tmpRowIndexVirt == rowIndexVirt )
            /**/                isFound = true;
            /**/
            /**/            break;
            /**/        }
            /**/    }
            /**/
            /**/    if( !isFound ){
            /**/    /**************************************************************************/
            /**/    /**/    if( constraint.column > 0 )
            /**/    /**/        colIndexVirt = constraint.column;
            /**/    /**/
            /**/    /**/    rowConstraint = new RowConstraint( rowIndexVirt );
            /**/    /**/
            /**/    /**/    rowConstraints .add( rowIndexReal, rowConstraint                    );
            /**/    /**/    components     .add( rowIndexReal, new ArrayList<Component>( )      );
            /**/    /**/    compConstraints.add( rowIndexReal, new ArrayList<CompConstraint>( ) );
            /**/    /**************************************************************************/
            /**/    }
            /**/
            /**/    else{
            /**/    /**************************************************************************/
            /**/    /**/    ArrayList<CompConstraint> compConstraintRow = compConstraints.get( rowIndexReal );
            /**/    /**/
            /**/    /**/    if( constraint.column < 0 ){
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    colIndexReal = compConstraintRow.size( );
            /**/    /**/    /**/    colIndexVirt = compConstraintRow.get(colIndexReal - 1).column + 1;
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**/
            /**/    /**/    else{
            /**/    /**/    /**********************************************************************/
            /**/    /**/    /**/    colIndexReal = compConstraintRow.size( );
            /**/    /**/    /**/    colIndexVirt = constraint.column;
            /**/    /**/    /**/
            /**/    /**/    /**/    for( int column = 0; column < colIndexReal; column++ ){
            /**/    /**/    /**/
            /**/    /**/    /**/        int tmpColIndexVirt = compConstraintRow.get(column).column;
            /**/    /**/    /**/
            /**/    /**/    /**/        if( tmpColIndexVirt >= colIndexVirt ){
            /**/    /**/    /**/
            /**/    /**/    /**/            colIndexReal = column;
            /**/    /**/    /**/            break;
            /**/    /**/    /**/        }
            /**/    /**/    /**/    }
            /**/    /**/    /**********************************************************************/
            /**/    /**/    }
            /**/    /**/
            /**/    /**/    rowConstraint = rowConstraints.get( rowIndexReal );
            /**/    /**************************************************************************/
            /**/    }
            /**/
            /**/    compConstraint = new CompConstraint( colIndexVirt );
            /**/
            /**/    components     .get(rowIndexReal).add( colIndexReal, component      );
            /**/    compConstraints.get(rowIndexReal).add( colIndexReal, compConstraint );
            /********************************************************************************/
            }

            rowConstraint .setData( constraint );
            compConstraint.setData( constraint );

        }catch( Exception exception ){

            
        }
    }

    @Override
    public void removeLayoutComponent( Component component ){

        for( int row = 0, rowCount = components.size( ); row < rowCount; row++ ){

            int compIndex = components.get(row).indexOf( component );

            if( compIndex < 0 )
                continue;

            components     .get(row).remove( compIndex );
            compConstraints.get(row).remove( compIndex );

            if( components.get(row).isEmpty( ) ){

                components     .remove( row );
                compConstraints.remove( row );
                rowConstraints .remove( row );
            }

            break;
        }
    }

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Layout Container     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    @Override
    public void layoutContainer( Container parent ){

        synchronized( parent.getTreeLock( ) ){

            ParentLayoutData parentData = new ParentLayoutData( );
            RowLayoutData[] rowData     = new RowLayoutData[parentData.rowCount];
            CompLayoutData[][] compData = new CompLayoutData[parentData.rowCount][];

            for( int row = 0; row < parentData.rowCount; row++ ){

                rowData[row]  = new RowLayoutData( rowConstraints.get( row ) );
                compData[row] = new CompLayoutData[parentData.columnCount[row]];

                for( int column = 0; column < parentData.columnCount[row]; column++ )
                    compData[row][column] = new CompLayoutData( components.get(row).get( column ), compConstraints.get(row).get( column ) );
            }

            Insets parentInsets = parent.getInsets( );

            parentData.remainWidth [0] = parent.getWidth( )  - parentInsets.left - parentInsets.right;
            parentData.remainHeight[0] = parent.getHeight( ) - parentInsets.top  - parentInsets.bottom;

            parentData.remainWidth [1] = parentData.remainWidth [0];
            parentData.remainHeight[1] = parentData.remainHeight[0];

            parentData.remainWidth [2] = parentData.remainWidth [0];
            parentData.remainHeight[2] = parentData.remainHeight[0];

            if( parentData.isGapProp.left.pref )
                parentData.staticGapLeft = parentGap.left.pref * parentData.remainWidth[0];

            else
                parentData.staticGapLeft = parentGap.left.pref;

            if( parentGap.left.max >= 0 && parentData.staticGapLeft > parentGap.left.max )
                parentData.staticGapLeft = parentGap.left.max;

            else if( parentGap.left.min >= 0 && parentData.staticGapLeft < parentGap.left.min )
                parentData.staticGapLeft = parentGap.left.min;

            parentData.remainWidth[1] -= parentData.staticGapLeft;
            parentData.remainWidth[2] -= parentData.staticGapLeft;

            if( parentData.isGapProp.right.pref )
                parentData.staticGapRight = parentGap.right.pref * parentData.remainWidth[0];

            else
                parentData.staticGapRight = parentGap.right.pref;

            if( parentGap.right.max >= 0 && parentData.staticGapRight > parentGap.right.max )
                parentData.staticGapRight = parentGap.right.max;

            else if( parentGap.right.min >= 0 && parentData.staticGapRight < parentGap.right.min )
                parentData.staticGapRight = parentGap.right.min;

            parentData.remainWidth[1] -= parentData.staticGapRight;
            parentData.remainWidth[2] -= parentData.staticGapRight;

            if( parentData.isGapProp.top.pref )
                parentData.staticGapTop = parentGap.top.pref * parentData.remainHeight[0];

            else
                parentData.staticGapTop = parentGap.top.pref;

            if( parentGap.top.max >= 0 && parentData.staticGapTop > parentGap.top.max )
                parentData.staticGapTop = parentGap.top.max;

            else if( parentGap.top.min >= 0 && parentData.staticGapTop < parentGap.top.min )
                parentData.staticGapTop = parentGap.top.min;

            parentData.remainHeight[1] -= parentData.staticGapTop;
            parentData.remainHeight[2] -= parentData.staticGapTop;

            if( parentData.isGapProp.bottom.pref )
                parentData.staticGapBottom = parentGap.bottom.pref * parentData.remainHeight[0];

            else
                parentData.staticGapBottom = parentGap.bottom.pref;

            if( parentGap.bottom.max >= 0 && parentData.staticGapBottom > parentGap.bottom.max )
                parentData.staticGapBottom = parentGap.bottom.max;

            else if( parentGap.bottom.min >= 0 && parentData.staticGapBottom < parentGap.bottom.min )
                parentData.staticGapBottom = parentGap.bottom.min;

            parentData.remainHeight[1] -= parentData.staticGapBottom;
            parentData.remainHeight[2] -= parentData.staticGapBottom;

            for( int row = 0; row < parentData.rowCount; row++ ){

                RowLayoutData rowDatum  = rowData[row];

                rowDatum.remainWidth[0] = parentData.remainWidth[1];
                rowDatum.remainWidth[1] = parentData.remainWidth[1];

                if( rowDatum.isGapProp.left.pref )
                    rowDatum.propGapWidth += rowDatum.gap.left.pref;

                else{

                    rowDatum.staticGapLeft = rowDatum.gap.left.pref;

                    if( rowDatum.gap.left.max >= 0 && rowDatum.staticGapLeft > rowDatum.gap.left.max )
                        rowDatum.staticGapLeft = rowDatum.gap.left.max;

                    else if( rowDatum.gap.left.min >= 0 && rowDatum.staticGapLeft < rowDatum.gap.left.min )
                        rowDatum.staticGapLeft = rowDatum.gap.left.min;

                    rowDatum.remainWidth[0] -= rowDatum.staticGapLeft;
                    rowDatum.remainWidth[1] -= rowDatum.staticGapLeft;
                }

                if( rowDatum.isGapProp.right.pref )
                    rowDatum.propGapWidth += rowDatum.gap.right.pref;

                else{

                    rowDatum.staticGapRight = rowDatum.gap.right.pref;

                    if( rowDatum.gap.right.max >= 0 && rowDatum.staticGapRight > rowDatum.gap.right.max )
                        rowDatum.staticGapRight = rowDatum.gap.right.max;

                    else if( rowDatum.gap.right.min >= 0 && rowDatum.staticGapRight < rowDatum.gap.right.min )
                        rowDatum.staticGapRight = rowDatum.gap.right.min;

                    rowDatum.remainWidth[0] -= rowDatum.staticGapRight;
                    rowDatum.remainWidth[1] -= rowDatum.staticGapRight;
                }

                if( rowDatum.isGapSpec.top.pref || row > 0 ){

                    if( rowDatum.isGapProp.top.pref ){

                        rowDatum.propGapHeight   += rowDatum.gap.top.pref;
                        parentData.propRowHeight += rowDatum.gap.top.pref;
                    }
                    else{

                        rowDatum.staticGapTop = rowDatum.gap.top.pref;

                        if( rowDatum.gap.top.max >= 0 && rowDatum.staticGapTop > rowDatum.gap.top.max )
                            rowDatum.staticGapTop = rowDatum.gap.top.max;

                        else if( rowDatum.gap.top.min >= 0 && rowDatum.staticGapTop < rowDatum.gap.top.min )
                            rowDatum.staticGapTop = rowDatum.gap.top.min;

                        parentData.remainHeight[1] -= rowDatum.staticGapTop;
                        parentData.remainHeight[2] -= rowDatum.staticGapTop;
                    }
                }

                if( rowDatum.isGapSpec.bottom.pref || row < (parentData.rowCount - 1) ){

                    if( rowDatum.isGapProp.bottom.pref ){

                        rowDatum.propGapHeight   += rowDatum.gap.bottom.pref;
                        parentData.propRowHeight += rowDatum.gap.bottom.pref;
                    }
                    else{

                        rowDatum.staticGapBottom = rowDatum.gap.bottom.pref;

                        if( rowDatum.gap.bottom.max >= 0 && rowDatum.staticGapBottom > rowDatum.gap.bottom.max )
                            rowDatum.staticGapBottom = rowDatum.gap.bottom.max;

                        else if( rowDatum.gap.bottom.min >= 0 && rowDatum.staticGapBottom < rowDatum.gap.bottom.min )
                            rowDatum.staticGapBottom = rowDatum.gap.bottom.min;

                        parentData.remainHeight[1] -= rowDatum.staticGapBottom;
                        parentData.remainHeight[2] -= rowDatum.staticGapBottom;
                    }
                }

                for( int column = 0; column < parentData.columnCount[row]; column++ ){

                    CompLayoutData compDatum = compData[row][column];

                    if( compDatum.isCompProp.width.pref )
                        compDatum.propWidth += compDatum.size.width.pref;

                    else{

                        compDatum.staticCompWidth = compDatum.size.width.pref;

                        if( compDatum.size.width.max >= 0 && compDatum.staticCompWidth > compDatum.size.width.max )
                            compDatum.staticCompWidth = compDatum.size.width.max;

                        else if( compDatum.size.width.min >= 0 && compDatum.staticCompWidth < compDatum.size.width.min )
                            compDatum.staticCompWidth = compDatum.size.width.min;

                        compDatum.staticWidth += compDatum.staticCompWidth;
                    }

                    if( compDatum.isCompProp.height.pref )
                        compDatum.propHeight += compDatum.size.height.pref;

                    else{

                        compDatum.staticCompHeight = compDatum.size.height.pref;

                        if( compDatum.size.height.max >= 0 && compDatum.staticCompHeight > compDatum.size.height.max )
                            compDatum.staticCompHeight = compDatum.size.height.max;

                        else if( compDatum.size.height.min >= 0 && compDatum.staticCompHeight < compDatum.size.height.min )
                            compDatum.staticCompHeight = compDatum.size.height.min;

                        compDatum.staticHeight += compDatum.staticCompHeight;
                    }

                    if( compDatum.isGapProp.top.pref )
                        compDatum.propHeight += compDatum.gap.top.pref;

                    else{

                        compDatum.staticGapTop = compDatum.gap.top.pref;

                        if( compDatum.gap.top.max >= 0 && compDatum.staticGapTop > compDatum.gap.top.max )
                            compDatum.staticGapTop = compDatum.gap.top.max;

                        else if( compDatum.gap.top.min >= 0 && compDatum.staticGapTop < compDatum.gap.top.min )
                            compDatum.staticGapTop = compDatum.gap.top.min;

                        compDatum.staticHeight += compDatum.staticGapTop;
                    }

                    if( compDatum.isGapProp.bottom.pref )
                        compDatum.propHeight += compDatum.gap.bottom.pref;

                    else{

                        compDatum.staticGapBottom = compDatum.gap.bottom.pref;

                        if( compDatum.gap.bottom.max >= 0 && compDatum.staticGapBottom > compDatum.gap.bottom.max )
                            compDatum.staticGapBottom = compDatum.gap.bottom.max;

                        else if( compDatum.gap.bottom.min >= 0 && compDatum.staticGapBottom < compDatum.gap.bottom.min )
                            compDatum.staticGapBottom = compDatum.gap.bottom.min;

                        compDatum.staticHeight += compDatum.staticGapBottom;
                    }

                    if( compDatum.isGapSpec.left.pref || column > 0 ){

                        if( compDatum.isGapProp.left.pref )
                            compDatum.propWidth += compDatum.gap.left.pref;

                        else{

                            compDatum.staticGapLeft = compDatum.gap.left.pref;

                            if( compDatum.gap.left.max >= 0 && compDatum.staticGapLeft > compDatum.gap.left.max )
                                compDatum.staticGapLeft = compDatum.gap.left.max;

                            else if( compDatum.gap.left.min >= 0 && compDatum.staticGapLeft < compDatum.gap.left.min )
                                compDatum.staticGapLeft = compDatum.gap.left.min;

                            compDatum.staticWidth += compDatum.staticGapLeft;
                        }
                    }

                    if( compDatum.isGapSpec.right.pref || column < (parentData.columnCount[row] - 1) ){

                        if( compDatum.isGapProp.right.pref )
                            compDatum.propWidth += compDatum.gap.right.pref;

                        else{

                            compDatum.staticGapRight = compDatum.gap.right.pref;

                            if( compDatum.gap.right.max >= 0 && compDatum.staticGapRight > compDatum.gap.right.max )
                                compDatum.staticGapRight = compDatum.gap.right.max;

                            else if( compDatum.gap.right.min >= 0 && compDatum.staticGapRight < compDatum.gap.right.min )
                                compDatum.staticGapRight = compDatum.gap.right.min;

                            compDatum.staticWidth += compDatum.staticGapRight;
                        }
                    }

                    rowDatum.remainWidth[0] -= compDatum.staticWidth;
                    rowDatum.propCompWidth  += compDatum.propWidth;

                    rowDatum.staticCompHeight = Math.max( rowDatum.staticCompHeight, compDatum.staticHeight );
                    rowDatum.propCompHeight   = Math.max( rowDatum.propCompHeight  , compDatum.propHeight   );
                }

                parentData.remainHeight[1] -= rowDatum.staticCompHeight;
                parentData.propRowHeight   += rowDatum.propCompHeight;
            }

            if( parentData.remainHeight[1] < 0 )
                parentData.remainHeight[1] = 0;

            for( int row = 0; row < parentData.rowCount; row++ ){

                RowLayoutData rowDatum = rowData[row];

                if( rowDatum.remainWidth[0] < 0 )
                    rowDatum.remainWidth[0] = 0;

                if( rowDatum.isGapProp.left.pref ){

                    rowDatum.staticGapLeft = rowDatum.gap.left.pref * rowDatum.remainWidth[0];

                    if( rowDatum.gap.left.max >= 0 && rowDatum.staticGapLeft > rowDatum.gap.left.max )
                        rowDatum.staticGapLeft = rowDatum.gap.left.max;

                    else if( rowDatum.gap.left.min >= 0 && rowDatum.staticGapLeft < rowDatum.gap.left.min )
                        rowDatum.staticGapLeft = rowDatum.gap.left.min;

                    rowDatum.remainWidth[1] -= rowDatum.staticGapLeft;
                }

                if( rowDatum.isGapProp.right.pref ){

                    rowDatum.staticGapRight = rowDatum.gap.right.pref * rowDatum.remainWidth[0];

                    if( rowDatum.gap.right.max >= 0 && rowDatum.staticGapRight > rowDatum.gap.right.max )
                        rowDatum.staticGapRight = rowDatum.gap.right.max;

                    else if( rowDatum.gap.right.min >= 0 && rowDatum.staticGapRight < rowDatum.gap.right.min )
                        rowDatum.staticGapRight = rowDatum.gap.right.min;

                    rowDatum.remainWidth[1] -= rowDatum.staticGapRight;
                }

                if( rowDatum.isGapSpec.top.pref || row > 0 ){

                    if( rowDatum.isGapProp.top.pref ){

                        rowDatum.staticGapTop = rowDatum.gap.top.pref * parentData.remainHeight[1];

                        if( rowDatum.gap.top.max >= 0 && rowDatum.staticGapTop > rowDatum.gap.top.max )
                            rowDatum.staticGapTop = rowDatum.gap.top.max;

                        else if( rowDatum.gap.top.min >= 0 && rowDatum.staticGapTop < rowDatum.gap.top.min )
                            rowDatum.staticGapTop = rowDatum.gap.top.min;

                        parentData.remainHeight[2] -= rowDatum.staticGapTop;
                    }
                }

                if( rowDatum.isGapSpec.bottom.pref || row < (parentData.rowCount - 1) ){

                    if( rowDatum.isGapProp.bottom.pref ){

                        rowDatum.staticGapBottom = rowDatum.gap.bottom.pref * parentData.remainHeight[1];

                        if( rowDatum.gap.bottom.max >= 0 && rowDatum.staticGapBottom > rowDatum.gap.bottom.max )
                            rowDatum.staticGapBottom = rowDatum.gap.bottom.max;

                        else if( rowDatum.gap.bottom.min >= 0 && rowDatum.staticGapBottom < rowDatum.gap.bottom.min )
                            rowDatum.staticGapBottom = rowDatum.gap.bottom.min;

                        parentData.remainHeight[2] -= rowDatum.staticGapBottom;
                    }
                }

                for( int column = 0; column < parentData.columnCount[row]; column++ ){

                    CompLayoutData compDatum = compData[row][column];

                    if( compDatum.isCompProp.width.pref ){

                        compDatum.staticCompWidth = compDatum.size.width.pref * rowDatum.remainWidth[0];

                        if( compDatum.size.width.max >= 0 && compDatum.staticCompWidth > compDatum.size.width.max )
                            compDatum.staticCompWidth = compDatum.size.width.max;

                        else if( compDatum.size.width.min >= 0 && compDatum.staticCompWidth < compDatum.size.width.min )
                            compDatum.staticCompWidth = compDatum.size.width.min;

                        compDatum.staticWidth += compDatum.staticCompWidth;
                    }

                    if( compDatum.isCompProp.height.pref ){

                        compDatum.staticCompHeight = compDatum.size.height.pref * parentData.remainHeight[1];

                        if( compDatum.size.height.max >= 0 && compDatum.staticCompHeight > compDatum.size.height.max )
                            compDatum.staticCompHeight = compDatum.size.height.max;

                        else if( compDatum.size.height.min >= 0 && compDatum.staticCompHeight < compDatum.size.height.min )
                            compDatum.staticCompHeight = compDatum.size.height.min;

                        compDatum.staticHeight += compDatum.staticCompHeight;
                    }

                    if( compDatum.isGapProp.top.pref ){

                        compDatum.staticGapTop = compDatum.gap.top.pref * parentData.remainHeight[1];

                        if( compDatum.gap.top.max >= 0 && compDatum.staticGapTop > compDatum.gap.top.max )
                            compDatum.staticGapTop = compDatum.gap.top.max;

                        else if( compDatum.gap.top.min >= 0 && compDatum.staticGapTop < compDatum.gap.top.min )
                            compDatum.staticGapTop = compDatum.gap.top.min;

                        compDatum.staticHeight += compDatum.staticGapTop;
                    }

                    if( compDatum.isGapProp.bottom.pref ){

                        compDatum.staticGapBottom = compDatum.gap.bottom.pref * parentData.remainHeight[1];

                        if( compDatum.gap.bottom.max >= 0 && compDatum.staticGapBottom > compDatum.gap.bottom.max )
                            compDatum.staticGapBottom = compDatum.gap.bottom.max;

                        else if( compDatum.gap.bottom.min >= 0 && compDatum.staticGapBottom < compDatum.gap.bottom.min )
                            compDatum.staticGapBottom = compDatum.gap.bottom.min;

                        compDatum.staticHeight += compDatum.staticGapBottom;
                    }

                    if( compDatum.isGapSpec.left.pref || column > 0 ){

                        if( compDatum.isGapProp.left.pref ){

                            compDatum.staticGapLeft = compDatum.gap.left.pref * rowDatum.remainWidth[0];

                            if( compDatum.gap.left.max >= 0 && compDatum.staticGapLeft > compDatum.gap.left.max )
                                compDatum.staticGapLeft = compDatum.gap.left.max;

                            else if( compDatum.gap.left.min >= 0 && compDatum.staticGapLeft < compDatum.gap.left.min )
                                compDatum.staticGapLeft = compDatum.gap.left.min;

                            compDatum.staticWidth += compDatum.staticGapLeft;
                        }
                    }

                    if( compDatum.isGapSpec.right.pref || column < (parentData.columnCount[row] - 1) ){

                        if( compDatum.isGapProp.right.pref ){

                            compDatum.staticGapRight = compDatum.gap.right.pref * rowDatum.remainWidth[0];

                            if( compDatum.gap.right.max >= 0 && compDatum.staticGapRight > compDatum.gap.right.max )
                                compDatum.staticGapRight = compDatum.gap.right.max;

                            else if( compDatum.gap.right.min >= 0 && compDatum.staticGapRight < compDatum.gap.right.min )
                                compDatum.staticGapRight = compDatum.gap.right.min;

                            compDatum.staticWidth += compDatum.staticGapRight;
                        }
                    }

                    rowDatum.remainWidth[1]  -= compDatum.staticWidth;
                    rowDatum.staticCompHeight = Math.max( rowDatum.staticCompHeight, compDatum.staticHeight );
                }

                parentData.remainHeight[2] -= rowDatum.staticCompHeight;
            }

            if( parentData.remainHeight[2] < 0 )
                parentData.remainHeight[2] = 0;

//complex bounded code here
//rounding correction here

            parentData.locationX = parentInsets.left + parentData.staticGapLeft;
            parentData.locationY = parentInsets.top  + parentData.staticGapTop;

            switch( parentAlign ){

                case MIDDLE:

                    parentData.locationY += parentData.remainHeight[2] / 2;
                    break;

                case BOTTOM:

                    parentData.locationY += parentData.remainHeight[2];
                    break;
            }

            for( int row = 0; row < parentData.rowCount; row++ ){

                RowLayoutData rowDatum = rowData[row];

                if( rowDatum.remainWidth[1] < 0 )
                    rowDatum.remainWidth[1] = 0;

                rowDatum.locationX = parentData.locationX + rowDatum.staticGapLeft;
                rowDatum.locationY = parentData.locationY + rowDatum.staticGapTop;

                switch( rowDatum.align ){

                    case CENTER:

                        rowDatum.locationX += rowDatum.remainWidth[1] / 2;
                        break;

                    case RIGHT:

                        rowDatum.locationX += rowDatum.remainWidth[1];
                        break;
                }

                for( int column = 0; column < parentData.columnCount[row]; column++ ){

                    CompLayoutData compDatum = compData[row][column];

                    compDatum.locationX = rowDatum.locationX + compDatum.staticGapLeft;
                    compDatum.locationY = rowDatum.locationY + compDatum.staticGapTop;

                    switch( compDatum.align ){

                        case MIDDLE:

                            compDatum.locationY += (rowDatum.staticCompHeight - compDatum.staticHeight) / 2;
                            break;

                        case BOTTOM:

                            compDatum.locationY += (rowDatum.staticCompHeight - compDatum.staticHeight);
                            break;
                    }
//maybe try rounding correction?
//if the actual row width != expected row width:
//go through each comp/gap and icrement/decrement size to match expected
//to be most acurate, start with the comps/gaps closest to the next/prev integer and work back until the expected size is reached
                    components.get(row).get(column).setBounds( (int)compDatum.locationX, (int)compDatum.locationY, (int)compDatum.staticCompWidth, (int)compDatum.staticCompHeight );

                    rowDatum.locationX += compDatum.staticGapLeft + compDatum.staticCompWidth + compDatum.staticGapRight;
                }

                parentData.locationY += rowDatum.staticGapTop + rowDatum.staticCompHeight + rowDatum.staticGapBottom;
            }
        }
//
//complex bounding code
//
//
//            0) this is another option for handling bounding:
//            0) consider 3 rows of 3 columns
//            0) as you resize larger, all 3 rows fill at same rate and there is no unused space in the container
//            0) then the entire middle row max bounds
//            0) as you continue to resize, the first and last rows keep growing at original rate, so more unused space accumulates (from missing middle row accumulation)
//            0) instead, divde the middle row proportion equally (or by user defined weights) into the other two rows
//            0) this will continue to fill the entire container and keep total container proportion the same at all times
//            1) total height proportion & total width proportion / global
//            2) total bounded height proportion & total bounded width proportion / per iteration
//            3) unbounded height count & unbounded width count / per iteration
//            4) bound height state & bound width state / per iteration
//            5) height prop to add & width prop to add / once
//
//
//            do{
//
//                stuff goes here
//
//            }while( parentData.isHeightBounded );
//
//
//            boolean tmpBoundHeightState = true;
//            double tmpBoundHeightSum = 0.0;
//
//            double tmpDynamicHeight = parentData.remainHeight;
//            int tmpDynamicHeightCount = 0;
//
//            while( tmpBoundHeightState ){
//
//                tmpBoundHeightState = false;
//                parentData.remainHeight = tmpDynamicHeight;
//
//                for( int row = 0; row < parentData.rowCount; row++ ){
//
//                    RowLayoutData rowData = parentData.rowDatas[row];
//
//                    boolean tmpBoundWidth  = true;
//                    double tmpDynamicWidth = rowData.remainWidth;
//
//                    while( tmpBoundWidth ){
//
//                        tmpBoundWidth = false;
//                        rowData.remainWidth = tmpDynamicWidth;
//
//                        for( int column = 0; column < parentData.columnCount[row]; column++ ){
//
//                            CompLayoutData compData = parentData.compDatas[row][column];
//
//                            if( compData.isCompProp.width.pref && compData.sizeMode.width.pref == PropMode.PARTIAL ){
//
//                                Double tmpMaxSize = null;
//                                Double tmpMinSize = null;
//
//                                compData.staticWidth = compData.size.width.pref * rowData.remainWidth;
//
//                                if( compData.isCompProp.width.max )
//                                    tmpMaxSize = compData.size.width.max * parentData.totalWidth;
//
//                                else
//                                    tmpMaxSize = compData.size.width.max;
//
//                                if( compData.isCompProp.width.min )
//                                    tmpMinSize = compData.size.width.min * parentData.totalWidth;
//
//                                else
//                                    tmpMinSize = compData.size.width.min;
//
//        //                        if( maxSize != null && compData.staticWidth > maxSize )
//        //                            compData.staticWidth = maxSize;
//        //
//        //                        else if( minSize != null && compData.staticWidth < minSize )
//        //                            compData.staticWidth = minSize;
//        //
//        //                        rowData.dynamicWidth -= compData.staticWidth;
//        //                        somehow subtract new width from row dynamic width and start over. also need to mark this comp as bounded so not calculated again. dont need to start over immediately, only after this inner loop.
//                            }
//
//                            if( compData.isCompProp.height.pref && compData.sizeMode.height.pref == PropMode.PARTIAL ){
//
//                                Double tmpMaxSize = null;
//                                Double tmpMinSize = null;
//
//                                compData.staticHeight = compData.size.height.pref * parentData.remainHeight;
//
//                                if( compData.isCompProp.height.max )
//                                    tmpMaxSize = compData.size.height.max * parentData.totalHeight;
//
//                                else
//                                    tmpMaxSize = compData.size.height.max;
//
//                                if( compData.isCompProp.height.min )
//                                    tmpMinSize = compData.size.height.min * parentData.totalHeight;
//
//                                else
//                                    tmpMinSize = compData.size.height.min;
//
//        //                        if( maxSize != null && compData.staticHeight > maxSize )
//        //                            compData.staticHeight = maxSize;
//        //
//        //                        else if( minSize != null && compData.staticHeight < minSize )
//        //                            compData.staticHeight = minSize;
//        //
//        //                        somehow figure out if row has a new hight and if so, correct the parent dynamic height and start over. maybe subtract difference between old and new row. also need to mark this comp as bounded so not calculated again. dont need to exit immediately, only after inner loop.
//                            }
//
//                            if( column > 0 ){
//
//                                if( !compData.isGapProp.left.pref || compData.gapMode.left.pref == PropMode.FULL ){
//
//                                    Double tmpMaxGap = null;
//                                    Double tmpMinGap = null;
//
//                                    if( compData.isGapProp.left.pref )
//                                        compData.staticGapLeft = compData.gap.left.pref * parentData.totalWidth;
//
//                                    else
//                                        compData.staticGapLeft = compData.gap.left.pref;
//
//                                    if( compData.isGapProp.left.max )
//                                        tmpMaxGap = compData.gap.left.max * parentData.totalWidth;
//
//                                    else
//                                        tmpMaxGap = compData.gap.left.max;
//
//                                    if( compData.isGapProp.left.min )
//                                        tmpMinGap = compData.gap.left.min * parentData.totalWidth;
//
//                                    else
//                                        tmpMinGap = compData.gap.left.min;
//
//                                    if( tmpMaxGap != null && compData.staticGapLeft > tmpMaxGap )
//                                        compData.staticGapLeft = tmpMaxGap;
//
//                                    else if( tmpMinGap != null && compData.staticGapLeft < tmpMinGap )
//                                        compData.staticGapLeft = tmpMinGap;
//
//                                    rowData.remainWidth -= compData.staticGapLeft;
//                                }
//                            }
//
//                            if( column < (parentData.columnCount[row] - 1) ){
//
//                                if( !compData.isGapProp.right.pref || compData.gapMode.right.pref == PropMode.FULL ){
//
//                                    Double tmpMaxGap = null;
//                                    Double tmpMinGap = null;
//
//                                    if( compData.isGapProp.right.pref )
//                                        compData.staticGapRight = compData.gap.right.pref * parentData.totalWidth;
//
//                                    else
//                                        compData.staticGapRight = compData.gap.right.pref;
//
//                                    if( compData.isGapProp.right.max )
//                                        tmpMaxGap = compData.gap.right.max * parentData.totalWidth;
//
//                                    else
//                                        tmpMaxGap = compData.gap.right.max;
//
//                                    if( compData.isGapProp.right.min )
//                                        tmpMinGap = compData.gap.right.min * parentData.totalWidth;
//
//                                    else
//                                        tmpMinGap = compData.gap.right.min;
//
//                                    if( tmpMaxGap != null && compData.staticGapRight > tmpMaxGap )
//                                        compData.staticGapRight = tmpMaxGap;
//
//                                    else if( tmpMinGap != null && compData.staticGapRight < tmpMinGap )
//                                        compData.staticGapRight = tmpMinGap;
//
//                                    rowData.remainWidth -= compData.staticGapRight;
//                                }
//                            }
//
//                            if( !compData.isGapProp.top.pref || compData.gapMode.top.pref == PropMode.FULL ){
//
//                                Double tmpMaxGap = null;
//                                Double tmpMinGap = null;
//
//                                if( compData.isGapProp.top.pref )
//                                    compData.staticGapTop = compData.gap.top.pref * parentData.totalHeight;
//
//                                else
//                                    compData.staticGapTop = compData.gap.top.pref;
//
//                                if( compData.isGapProp.top.max )
//                                    tmpMaxGap = compData.gap.top.max * parentData.totalHeight;
//
//                                else
//                                    tmpMaxGap = compData.gap.top.max;
//
//                                if( compData.isGapProp.top.min )
//                                    tmpMinGap = compData.gap.top.min * parentData.totalHeight;
//
//                                else
//                                    tmpMinGap = compData.gap.top.min;
//
//                                if( tmpMaxGap != null && compData.staticGapTop > tmpMaxGap )
//                                    compData.staticGapTop = tmpMaxGap;
//
//                                else if( tmpMinGap != null && compData.staticGapTop < tmpMinGap )
//                                    compData.staticGapTop = tmpMinGap;
//                            }
//
//                            if( !compData.isGapProp.bottom.pref || compData.gapMode.bottom.pref == PropMode.FULL ){
//
//                                Double tmpMaxGap = null;
//                                Double tmpMinGap = null;
//
//                                if( compData.isGapProp.bottom.pref )
//                                    compData.staticGapBottom = compData.gap.bottom.pref * parentData.totalHeight;
//
//                                else
//                                    compData.staticGapBottom = compData.gap.bottom.pref;
//
//                                if( compData.isGapProp.bottom.max )
//                                    tmpMaxGap = compData.gap.bottom.max * parentData.totalHeight;
//
//                                else
//                                    tmpMaxGap = compData.gap.bottom.max;
//
//                                if( compData.isGapProp.bottom.min )
//                                    tmpMinGap = compData.gap.bottom.min * parentData.totalHeight;
//
//                                else
//                                    tmpMinGap = compData.gap.bottom.min;
//
//                                if( tmpMaxGap != null && compData.staticGapBottom > tmpMaxGap )
//                                    compData.staticGapBottom = tmpMaxGap;
//
//                                else if( tmpMinGap != null && compData.staticGapBottom < tmpMinGap )
//                                    compData.staticGapBottom = tmpMinGap;
//                            }
//
//                            rowData.staticHeight = Math.max( rowData.staticHeight, (compData.staticHeight + compData.staticGapTop + compData.staticGapBottom) );
//                        }
//
//                        totalRowHeight += rowData.staticRowHeight;
//                    }
//                }
//            }
//
//
//            totalRowHeight = 0.0;
//
//            for( int row = 0; row < rowCount; row++ ){
//
//                columnCount = components.get(row).size( );
//                RowLayoutData rowData = rowLayoutData.get( row );
//
//                rowData.totalCompWidth  = 0.0;
//                rowData.staticRowHeight = 0.0;
//
//                for( int column = 0; column < columnCount; column++ ){
//
//                    CompLayoutData compData = compLayoutData.get(row).get( column );
//
//                    if( compData.size.width.min != null && compData.maxStaticWidth < compData.size.width.min )
//                        compData.maxStaticWidth = compData.size.width.min;
//
//                    else if( compData.size.width.max != null && compData.maxStaticWidth > compData.size.width.max )
//                        compData.maxStaticWidth = compData.size.width.max;
//
//                    if( compData.size.height.min != null && compData.maxStaticHeight < compData.size.height.min )
//                        compData.maxStaticHeight = compData.size.height.min;
//
//                    else if( compData.size.height.max != null && compData.maxStaticHeight > compData.size.height.max )
//                        compData.maxStaticHeight = compData.size.height.max;
//
//                    rowData.totalCompWidth += compData.maxStaticWidth;
//                    rowData.staticRowHeight = Math.max( rowData.staticRowHeight, compData.maxStaticHeight );
//                }
//
//                totalRowHeight += rowData.staticRowHeight;
//            }
//
//
//
//
//
    }

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Utility Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    private boolean isProportion( double number ){

        if( number > 0 && number <= 1 )
            return true;

        return false;
    }

    private GapState getGapPropState( GapSize gapSize ){

        GapState isGapProp = new GapState( );

        isGapProp.top.pref    = isProportion( gapSize.top.pref    );
        isGapProp.top.min     = isProportion( gapSize.top.min     );
        isGapProp.top.max     = isProportion( gapSize.top.max     );
        isGapProp.bottom.pref = isProportion( gapSize.bottom.pref );
        isGapProp.bottom.min  = isProportion( gapSize.bottom.min  );
        isGapProp.bottom.max  = isProportion( gapSize.bottom.max  );
        isGapProp.left.pref   = isProportion( gapSize.left.pref   );
        isGapProp.left.min    = isProportion( gapSize.left.min    );
        isGapProp.left.max    = isProportion( gapSize.left.max    );
        isGapProp.right.pref  = isProportion( gapSize.right.pref  );
        isGapProp.right.min   = isProportion( gapSize.right.min   );
        isGapProp.right.max   = isProportion( gapSize.right.max   );

        return isGapProp;
    }

    private CompState getCompPropState( CompSize compSize ){

        CompState isCompProp = new CompState( );

        isCompProp.width.pref  = isProportion( compSize.width.pref  );
        isCompProp.width.min   = isProportion( compSize.width.min   );
        isCompProp.width.max   = isProportion( compSize.width.max   );
        isCompProp.height.pref = isProportion( compSize.height.pref );
        isCompProp.height.min  = isProportion( compSize.height.min  );
        isCompProp.height.max  = isProportion( compSize.height.max  );

        return isCompProp;
    }

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Overridden Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    @Override
    public void invalidateLayout( Container parent ){

//when optimizations are used and layout data is cached until something changes, this method should force cache to be cleared
//when the component size comes from the component's getPreferredSize(),getMinimumSize(),getMaximumSize() methods, these values can never be cached because no way to know they've changed (unless it calls this method when that happens)
    }

    @Override
    public final float getLayoutAlignmentX( Container parent ){

        return 0;
    }

    @Override
    public final float getLayoutAlignmentY( Container parent ){

        return 0;
    }

    @Override
    public Dimension preferredLayoutSize( Container parent ){

        Dimension size = new Dimension( -1, -1 );

        if( prefLayoutSize != null ){

            size.width  = prefLayoutSize.width;
            size.height = prefLayoutSize.height;
        }

        if( size.width < 0 || size.height < 0 ){

//the preferred size is the current size of the parent (less insets) unless:
//the parent size < (specified minLayoutSize or calculated minLayoutSize()) (whichever is greatest)
//the parent size > (specified maxLayoutSize or calculated maxLayoutSize()) (whichever is least)

            if( size.width < 0 )
                size.width = 0;

            if( size.height < 0 )
                size.height = 0;
        }

        return size;

//        double prefParentWidth  = 0.0;
//        double prefParentHeight = 0.0;
//
//        double prefRowWidth  = 0.0;
//        double prefRowHeight = 0.0;
//
//        Component component = null;
//        CompConstraint compConstraint = null;
//        RowConstraint rowConstraint = null;
//
//        int columnCount = 0;
//
//        for( int row = 0, rowCount = components.size( ); row < rowCount; row++ ){
//
//            columnCount   = components.get(row).size( );
//            rowConstraint = rowConstraints.get( row );
//
//            prefRowWidth  = 0.0;
//            prefRowHeight = 0.0;
//
//            for( int column = 0; column < columnCount; column++ ){
//
//                component = components.get(row).get( column );
//                compConstraint = compConstraints.get(row).get( column );
//
//                prefRowWidth += component.getPreferredSize().width;
//                prefRowHeight = Math.max( prefRowHeight, component.getPreferredSize().height );
//
//                if( column != 0 )
//                    prefRowWidth += compConstraint.gap.left.pref;
//            }
//
//            if( row != 0 )
//                prefRowHeight += rowConstraint.gap.top.pref;
//
//            prefParentWidth = Math.max( prefParentWidth, prefRowWidth );
//            prefParentHeight += prefRowHeight;
//        }
//
//        prefParentWidth  += parent.getInsets().left + parent.getInsets().right  + parentGap.left.pref + parentGap.right.pref;
//        prefParentHeight += parent.getInsets().top  + parent.getInsets().bottom + parentGap.top.pref  + parentGap.bottom.pref;
//
//        prefParentWidth  = Math.floor( prefParentWidth  + 0.5 );
//        prefParentHeight = Math.floor( prefParentHeight + 0.5 );
//
//        return new Dimension( (int)prefParentWidth, (int)prefParentHeight );
    }

    @Override
    public Dimension minimumLayoutSize( Container parent ){

        Dimension size = new Dimension( -1, -1 );

        if( minLayoutSize != null ){

            size.width  = minLayoutSize.width;
            size.height = minLayoutSize.height;
        }

        if( size.width < 0 || size.height < 0 ){

//the minimum size is the static gaps and comps as well as min bounded values
//(some height min bounds may not be acheived because total static height and other min bounds already define max parent height)

            if( size.width < 0 )
                size.width = 0;

            if( size.height < 0 )
                size.height = 0;
        }

        return size;

//        double minParentWidth  = 0.0;
//        double minParentHeight = 0.0;
//
//        double minRowWidth  = 0.0;
//        double minRowHeight = 0.0;
//
//        Component component = null;
//        CompConstraint compConstraint = null;
//        RowConstraint rowConstraint = null;
//
//        int columnCount = 0;
//
//        for( int row = 0, rowCount = components.size( ); row < rowCount; row++ ){
//
//            columnCount   = components.get(row).size( );
//            rowConstraint = rowConstraints.get( row );
//
//            minRowWidth  = 0.0;
//            minRowHeight = 0.0;
//
//            for( int column = 0; column < columnCount; column++ ){
//
//                component = components.get(row).get( column );
//                compConstraint = compConstraints.get(row).get( column );
//
//                minRowWidth += component.getMinimumSize().width;
//                minRowHeight = Math.max( minRowHeight, component.getMinimumSize().height );
//
//                if( column != 0 )
//                    minRowWidth += compConstraint.gap.left.pref;
//            }
//
//            if( row != 0 )
//                minRowHeight += rowConstraint.gap.top.pref;
//
//            minParentWidth = Math.max( minParentWidth, minRowWidth );
//            minParentHeight += minRowHeight;
//        }
//
//        minParentWidth  += parent.getInsets().left + parent.getInsets().right  + parentGap.left.pref + parentGap.right.pref;
//        minParentHeight += parent.getInsets().top  + parent.getInsets().bottom + parentGap.top.pref  + parentGap.bottom.pref;
//
//        minParentWidth  = Math.floor( minParentWidth  + 0.5 );
//        minParentHeight = Math.floor( minParentHeight + 0.5 );
//
//        return new Dimension( (int)minParentWidth, (int)minParentHeight );
    }

    @Override
    public Dimension maximumLayoutSize( Container parent ){

        Dimension size = new Dimension( -1, -1 );

        if( maxLayoutSize != null ){

            size.width  = maxLayoutSize.width;
            size.height = maxLayoutSize.height;
        }

        if( size.width < 0 || size.height < 0 ){

//there is no max size (short.max_value) unless:
//all gaps and comps have static and max bounded width/height, then use that value
//for width/height separately, if just one component has an unbounded prop, then no max (short.max_value)
//otherwise use max row width, total height

            if( size.width < 0 )
                size.width = Short.MAX_VALUE;

            if( size.height < 0 )
                size.height = Short.MAX_VALUE;
        }

        return size;

//        double maxParentWidth  = 0.0;
//        double maxParentHeight = 0.0;
//
//        double maxRowWidth  = 0.0;
//        double maxRowHeight = 0.0;
//
//        Component component = null;
//        CompConstraint compConstraint = null;
//        RowConstraint rowConstraint = null;
//
//        int columnCount = 0;
//
//        for( int row = 0, rowCount = components.size( ); row < rowCount; row++ ){
//
//            columnCount   = components.get(row).size( );
//            rowConstraint = rowConstraints.get( row );
//
//            maxRowWidth  = 0.0;
//            maxRowHeight = 0.0;
//
//            for( int column = 0; column < columnCount; column++ ){
//
//                component = components.get(row).get( column );
//                compConstraint = compConstraints.get(row).get( column );
//
//                maxRowWidth += component.getMaximumSize().width;
//                maxRowHeight = Math.max( maxRowHeight, component.getMaximumSize().width );
//
//                if( column != 0 )
//                    maxRowWidth += compConstraint.gap.left.pref;
//            }
//
//            if( row != 0 )
//                maxRowHeight += rowConstraint.gap.top.pref;
//
//            maxParentWidth = Math.max( maxParentWidth, maxRowWidth );
//            maxParentHeight += maxRowHeight;
//        }
//
//        maxParentWidth  += parent.getInsets().left + parent.getInsets().right  + parentGap.left.pref + parentGap.right.pref;
//        maxParentHeight += parent.getInsets().top  + parent.getInsets().bottom + parentGap.top.pref  + parentGap.bottom.pref;
//
//        maxParentWidth  = Math.floor( maxParentWidth  + 0.5 );
//        maxParentHeight = Math.floor( maxParentHeight + 0.5 );
//
//        return new Dimension( (int)maxParentWidth, (int)maxParentHeight );
    }

//method no longer necessary as the layout now has the ability to use the preferred layout of a component (don't explicitly provide a height/width)
//    public int getStaticWidth( Container parent ){
//
//        int staticWidth = 0;
//
//        for( int row = 0, rowCount = components.size( ); row < rowCount; row++ ){
//
//            int rowWidth = 0;
//
//            for( int column = 0, columnCount = components.get(row).size( ); column < columnCount; column++ ){
//
//                CompLayoutData layoutData = new CompLayoutData( components.get(row).get( column ), compConstraints.get(row).get( column ) );
//
//                if( !layoutData.sizeState.width.pref )
//                    rowWidth += layoutData.size.width.pref;
//
//                if( row != 0 )
//                    rowWidth += layoutData.gap.left.pref;
//            }
//
//            staticWidth = Math.max( staticWidth, rowWidth );
//        }
//
//        staticWidth += parent.getInsets().left + parent.getInsets().right + parentGapSize.left.pref + parentGapSize.right.pref;
//
//        return staticWidth;
//    }
//
//method no longer necessary as the layout now has the ability to use the preferred layout of a component (don't explicitly provide a height/width)
//    public int getStaticHeight( Container parent ){
//
//        int staticHeight = 0;
//
//        for( int row = 0, rowCount = components.size( ); row < rowCount; row++ ){
//
//            int rowHeight = 0;
//            RowLayoutData rowData = new RowLayoutData( rowConstraints.get( row ) );
//
//            for( int column = 0, columnCount = components.get(row).size( ); column < columnCount; column++ ){
//
//                CompLayoutData compData = new CompLayoutData( components.get(row).get( column ), compConstraints.get(row).get( column ) );
//
//                if( !compData.sizeState.height.pref )
//                    rowHeight = (int)Math.max( rowHeight, compData.size.height.pref );
//            }
//
//            staticHeight += rowHeight;
//
//            if( row != 0 )
//                staticHeight += rowData.gap.top.pref;
//        }
//
//        staticHeight += parent.getInsets().top + parent.getInsets().bottom + parentGapSize.top.pref + parentGapSize.bottom.pref;
//
//        return staticHeight;
//    }

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Accessor Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * Retrieves the parent gaps.
     *
     * @return the parent gaps
     */
    public GapSize getParentGap( ){

        return new GapSize( this.parentGap );
    }

    /**
     * Retrieves the default row gaps.
     *
     * @return the default row gaps
     */
    public GapSize getRowGap( ){

        return new GapSize( this.rowGap );
    }

    /**
     * Retrieves the default component gaps.
     *
     * @return the default component gaps
     */
    public GapSize getCompGap( ){

        return new GapSize( this.compGap );
    }

    /**
     * Retrieves the vertical alignment of all rows relative to the parent container.
     *
     * @return the vertical parent alignment
     */
    public VertAlign getParentAlignment( ){

        return parentAlign;
    }

    /**
     * Retrieves the default horizontal row alignment relative to the parent container.
     *
     * @return the default horizontal row alignment
     */
    public HorzAlign getRowAlignment( ){

        return rowAlign;
    }

    /**
     * Retries the default vertical component alignment relative to the containing row.
     *
     * @return the default vertical component alignment
     */
    public VertAlign getCompAlignment( ){

        return compAlign;
    }

    /**
     * Retrieves the component at the specified position. The row and column are treated as virtual
     * positions derived when the component was added to the layout and not as absolute positions
     * (these will be the same in many cases). Null is returned if the position is invalid.
     *
     * @param row the virtual row position
     * @param column the virtual column position
     *
     * @return the component at the specified position or null if invalid
     */
    public Component getComponent( int row, int column ){

        int rowIndexReal = -1;
        int colIndexReal = -1;

        for( int index = 0, count = rowConstraints.size( ); index < count; index++ ){

            if( rowConstraints.get(index).row == row ){

                rowIndexReal = index;
                break;
            }
        }

        if( rowIndexReal >= 0 ){

            for( int index = 0, count = compConstraints.get(rowIndexReal).size( ); index < count; index++ ){

                if( compConstraints.get(rowIndexReal).get(index).column == column ){

                    colIndexReal = index;
                    break;
                }
            }
        }

        try{

            if( rowIndexReal < 0 )
                throw new Exception( "Layout component row index not found." );

            if( colIndexReal < 0 )
                throw new Exception( "Layout component column index not found." );

        }catch( Exception exception ){

            return null;
        }

        return components.get(row).get( column );
    }

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Mutator Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * Sets all common preferred gaps in the layout. A negative value is ignored.
     * See setParentGap( double ), setRowGap( double ), setCompGap( double ).
     *
     * @param allCommPref the common preferred gap
     *
     * @return method chain
     */
    public PropLayout setGap( double allCommPref ){

        setParentGap( allCommPref );
        setRowGap   ( allCommPref );
        setCompGap  ( allCommPref );

        return this;
    }

    /**
     * Sets all preferred parent gaps. A negative value is ignored.
     *
     * @param allPref the common preferred parent gap
     *
     * @return method chain
     */
    public PropLayout setParentGap( double allPref ){

        return setParentGap( allPref, allPref, allPref, allPref );
    }

    /**
     * Sets the preferred parent gaps. A negative value is ignored.
     *
     * @param leftPref the left preferred parent gap
     * @param rightPref the right preferred parent gap
     * @param topPref the top preferred parent gap
     * @param bottomPref the bottom preferred parent gap
     *
     * @return method chain
     */
    public PropLayout setParentGap( double leftPref, double rightPref, double topPref, double bottomPref ){

        if( leftPref >= 0 )
            this.parentGap.left.pref = leftPref;

        if( rightPref >= 0 )
            this.parentGap.right.pref = rightPref;

        if( topPref >= 0 )
            this.parentGap.top.pref = topPref;

        if( bottomPref >= 0 )
            this.parentGap.bottom.pref = bottomPref;

        return this;
    }

    /**
     * Sets the parent gaps. If a Size is null or the Size clear constructor was called with false, it
     * is ignored. If the Size clear constructor was called with true, the Size pref value is ignored
     * and the min,max values are cleared. Negative pref values are ignored. If a min,max value is -1,
     * it is ignored. If a min,max value < -1, it is cleared.
     *
     * @param left the left parent gap
     * @param right the right parent gap
     * @param top the top parent gap
     * @param bottom the bottom parent gap
     *
     * @return method chain
     */
    public PropLayout setParentGap( Size left, Size right, Size top, Size bottom ){

        this.parentGap.left  .copySize( left  , false );
        this.parentGap.right .copySize( right , false );
        this.parentGap.top   .copySize( top   , false );
        this.parentGap.bottom.copySize( bottom, false );

        return this;
    }

    /**
     * Sets the parent gaps. If the GapSize is null or the GapSize clear constructor was called with
     * false, it is ignored. If the GapSize clear constructor was called with true, the GapSize pref
     * values are ignored and the min,max values are cleared. The same goes for each Size value.
     * Negative pref values are ignored. If a min,max value is -1, it is ignored. If a min,max
     * value < -1, it is cleared.
     *
     * @param parentGap the parent gaps
     *
     * @return method chain
     */
    public PropLayout setParentGap( GapSize parentGap ){

        this.parentGap.copyGapSize( parentGap, false );
        return this;
    }

    /**
     * Sets the default top preferred row gap. A negative value is ignored. The gap is only applied
     * between rows. This value may be overridden for individual rows when adding components to the
     * layout. To specify a preceding first row gap, override this value with an individual component.
     *
     * @param topPref the default top preferred row gap
     *
     * @return method chain
     */
    public PropLayout setRowGap( double topPref ){

        return setRowGap( -1, -1, topPref, -1 );
    }

    /**
     * Sets the default preferred row gaps. A negative value is ignored. The top and bottom gaps are
     * only applied between rows. These values may be overridden for individual rows when adding
     * components to the layout. To specify a preceding first or trailing last row gap, override
     * these values with an individual component.
     *
     * @param leftPref the default left preferred row gap
     * @param rightPref the default right preferred row gap
     * @param topPref the default top preferred row gap
     * @param bottomPref the default bottom preferred row gap
     *
     * @return method chain
     */
    public PropLayout setRowGap( double leftPref, double rightPref, double topPref, double bottomPref ){

        if( leftPref >= 0 )
            this.rowGap.left.pref = leftPref;

        if( rightPref >= 0 )
            this.rowGap.right.pref = rightPref;

        if( topPref >= 0 )
            this.rowGap.top.pref = topPref;

        if( bottomPref >= 0 )
            this.rowGap.bottom.pref = bottomPref;

        return this;
    }

    /**
     * Sets the default row gaps. The top and bottom gaps are only applied between rows. These values
     * may be overridden for individual rows when adding components to the layout. To specify a
     * preceding first or trailing last row gap, override these values with an individual component. If
     * a Size is null or the Size clear constructor was called with false, it is ignored. If the Size
     * clear constructor was called with true, the Size pref value is ignored and the min,max values are
     * cleared. Negative pref values are ignored. If a min,max value is -1, it is ignored. If a min,max
     * value < -1, it is cleared.
     *
     * @param left the default left row gap
     * @param right the default right row gap
     * @param top the default top row gap
     * @param bottom the default bottom row gap
     *
     * @return method chain
     */
    public PropLayout setRowGap( Size left, Size right, Size top, Size bottom ){

        this.rowGap.left  .copySize( left  , false );
        this.rowGap.right .copySize( right , false );
        this.rowGap.top   .copySize( top   , false );
        this.rowGap.bottom.copySize( bottom, false );

        return this;
    }

    /**
     * Sets the default row gaps. The top and bottom gaps are only applied between rows. These values
     * may be overridden for individual rows when adding components to the layout. To specify a
     * preceding first or trailing last row gap, override these values with an individual component. If
     * the GapSize is null or the GapSize clear constructor was called with false, it is ignored. If the
     * GapSize clear constructor was called with true, the GapSize pref values are ignored and the
     * min,max values are cleared. The same goes for each Size value. Negative pref values are ignored.
     * If a min,max value is -1, it is ignored. If a min,max value < -1, it is cleared.
     *
     * @param rowGap the row gaps
     *
     * @return method chain
     */
    public PropLayout setRowGap( GapSize rowGap ){

        this.rowGap.copyGapSize( rowGap, false );
        return this;
    }

    /**
     * Sets the default left preferred component gap. A negative value is ignored. The gap is only applied
     * between components. This value may be overridden by individual components as they are added to the
     * layout. To specify a preceding first component gap, override this value with an individual
     * component.
     *
     * @param leftPref the default left preferred component gap
     *
     * @return method chain
     */
    public PropLayout setCompGap( double prefLeft ){

        return setCompGap( prefLeft, -1, -1, -1 );
    }

    /**
     * Sets the default preferred component gaps. A negative value is ignored. The left and right gaps
     * are only applied between components. These values may be overridden by individual components as
     * they are added to the layout. To specify a preceding first or trailing last component gap,
     * override these values with an individual component.
     *
     * @param leftPref the default left preferred component gap
     * @param rightPref the default right preferred component gap
     * @param topPref the default top preferred component gap
     * @param bottomPref the default bottom preferred component gap
     *
     * @return method chain
     */
    public PropLayout setCompGap( double leftPref, double rightPref, double topPref, double bottomPref ){

        if( leftPref >= 0 )
            this.compGap.left.pref = leftPref;

        if( rightPref >= 0 )
            this.compGap.right.pref = rightPref;

        if( topPref >= 0 )
            this.compGap.top.pref = topPref;

        if( bottomPref >= 0 )
            this.compGap.bottom.pref = bottomPref;

        return this;
    }

    /**
     * Sets the default component gaps. The left and right gaps are only applied between components.
     * These values may be overridden by individual components as they are added to the layout. To
     * specify a preceding first or trailing last component gap, override these values with an
     * individual component. If a Size is null or the Size clear constructor was called with false, it
     * is ignored. If the Size clear constructor was called with true, the Size pref value is ignored
     * and the min,max values are cleared. Negative pref values are ignored. If a min,max value is -1,
     * it is ignored. If a min,max value < -1, it is cleared.
     *
     * @param left the default left component gap
     * @param right the default right component gap
     * @param top the default top component gap
     * @param bottom the default bottom component gap
     *
     * @return method chain
     */
    public PropLayout setCompGap( Size left, Size right, Size top, Size bottom ){

        this.compGap.left  .copySize( left  , false );
        this.compGap.right .copySize( right , false );
        this.compGap.top   .copySize( top   , false );
        this.compGap.bottom.copySize( bottom, false );

        return this;
    }

    /**
     * Sets the default component gaps. The left and right gaps are only applied between components.
     * These values may be overridden by individual components as they are added to the layout. To
     * specify a preceding first or trailing last component gap, override these values with an
     * individual component. If the GapSize is null or the GapSize clear constructor was called with
     * false, it is ignored. If the GapSize clear constructor was called with true, the GapSize pref
     * values are ignored and the min,max values are cleared. The same goes for each Size value.
     * Negative pref values are ignored. If a min,max value is -1, it is ignored. If a min,max
     * value < -1, it is cleared.
     *
     * @param compGap the component gaps
     *
     * @return method chain
     */
    public PropLayout setCompGap( GapSize compGap ){

        this.compGap.copyGapSize( compGap, false );
        return this;
    }

    /**
     * Sets the vertical alignment of all rows relative to the parent container. The following values
     * are ignored: null, clear.
     *
     * @param alignment the vertical parent alignment
     *
     * @return method chain
     */
    public PropLayout setParentAlignment( VertAlign alignment ){

        if( alignment != null && alignment != VertAlign.CLEAR )
            this.parentAlign = alignment;

        return this;
    }

    /**
     * Sets the default horizontal row alignment relative to the parent container. The following values
     * are ignored: null, clear. This value may be overridden for individual rows when adding components
     * to the layout.
     *
     * @param alignment the default horizontal row alignment
     *
     * @return method chain
     */
    public PropLayout setRowAlignment( HorzAlign alignment ){

        if( alignment != null && alignment != HorzAlign.CLEAR )
            this.rowAlign = alignment;

        return this;
    }

    /**
     * Sets the default vertical component alignment relative to the containing row. The following values
     * are ignored: null, clear. This value may be overridden by individual components as they are added
     * to the layout.
     *
     * @param alignment the default vertical component alignment
     *
     * @return method chain
     */
    public PropLayout setCompAlignment( VertAlign alignment ){

        if( alignment != null && alignment != VertAlign.CLEAR )
            this.compAlign = alignment;

        return this;
    }

    /**
     * Sets the preferred layout size to a fixed dimension. The default is null, which indicates the
     * layout size should be dynamically calculated.
     *
     * @param size the preferred layout size
     *
     * @return method chain
     */
    public PropLayout setPrefLayoutSize( Dimension size ){

        if( size == null )
            this.prefLayoutSize = null;

        else
            this.prefLayoutSize = new Dimension( size );

        return this;
    }

    /**
     * Sets the minimum layout size to a fixed dimension. The default is null, which indicates the
     * layout size should be dynamically calculated.
     *
     * @param size the minimum layout size
     *
     * @return method chain
     */
    public PropLayout setMinLayoutSize( Dimension size ){

        if( size == null )
            this.minLayoutSize = null;

        else
            this.minLayoutSize = new Dimension( size );

        return this;
    }

    /**
     * Sets the maximum layout size to a fixed dimension. The default is null, which indicates the
     * layout size should be dynamically calculated.
     *
     * @param size the maximum layout size
     *
     * @return method chain
     */
    public PropLayout setMaxLayoutSize( Dimension size ){

        if( size == null )
            this.maxLayoutSize = null;

        else
            this.maxLayoutSize = new Dimension( size );

        return this;
    }

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Inner Classes     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                Inner Class: ParentLayoutData                                 XXXXX
    //XXX                                                                                              XXXXX
    //XXX             An Object that contains volatile layout data for a parent container.             XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An Object that contains volatile layout data for a parent container.
     */
    private class ParentLayoutData{

        public GapState isGapProp;

        public int rowCount;
        public int[] columnCount;

        public double locationX;
        public double locationY;

        public double remainWidth[];
        public double remainHeight[];

        public double staticGapLeft;
        public double staticGapRight;
        public double staticGapTop;
        public double staticGapBottom;

        public double propRowHeight;

//        public boolean isHeightBounded;
//
//        public double totalPropHeight;
//        public double boundPropHeight;
//        public int unboundCountHeight;
//        public boolean boundStateHeight;

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a ParentLayoutData.
         */
        public ParentLayoutData( ){

            this.isGapProp = PropLayout.this.getGapPropState( PropLayout.this.parentGap );

            this.rowCount    = components.size( );
            this.columnCount = new int[rowCount];

            for( int row = 0; row < rowCount; row++ )
                columnCount[row] = components.get(row).size( );

            this.remainWidth  = new double[3];
            this.remainHeight = new double[3];
        }
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                  Inner Class: RowLayoutData                                  XXXXX
    //XXX                                                                                              XXXXX
    //XXX                   An Object that contains volatile layout data for a row.                    XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An Object that contains volatile layout data for a row.
     */
    private class RowLayoutData{

        public GapSize gap;
        public GapState isGapProp;
        public GapState isGapSpec;

        public HorzAlign align;

        public double locationX;
        public double locationY;

        public double remainWidth[];
        public double staticCompHeight;

        public double staticGapLeft;
        public double staticGapRight;
        public double staticGapTop;
        public double staticGapBottom;

        public double propGapWidth;
        public double propGapHeight;

        public double propCompWidth;
        public double propCompHeight;

//        public boolean isWidthBounded;
//
//        public double totalPropWidth;
//        public double boundPropWidth;
//        public int unboundCountWidth;
//        public boolean boundStateWidth;
//
//        public double staticCompWidth;
//        public double staticGapWidth;
//
//        public double staticCompHeight;
//        public double staticGapHeight;


    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a RowLayoutData.
         */
        public RowLayoutData( RowConstraint constraint ){

            this.gap = new GapSize( PropLayout.this.rowGap );
            this.gap.copyGapSize( constraint.gap, false );

            this.isGapProp = PropLayout.this.getGapPropState( this.gap );
            this.isGapSpec = GapState.getGapSpecState( constraint.gap );

            this.align = PropLayout.this.rowAlign;

            if( constraint.align != null )
                this.align = constraint.align;

            this.remainWidth = new double[2];
        }
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                 Inner Class: CompLayoutData                                  XXXXX
    //XXX                                                                                              XXXXX
    //XXX                An object that contains volatile layout data for a component.                 XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An object that contains volatile layout data for a component.
     */
    private class CompLayoutData{

        public CompSize size;
        public CompState isCompProp;

        public GapSize gap;
        public GapState isGapProp;
        public GapState isGapSpec;

        public VertAlign align;

        public double locationX;
        public double locationY;

        public double staticWidth;
        public double staticHeight;

        public double staticCompWidth;
        public double staticCompHeight;

        public double staticGapLeft;
        public double staticGapRight;
        public double staticGapTop;
        public double staticGapBottom;

        public double propWidth;
        public double propHeight;
//
//        public CompState sizeBoundState;
//        public GapState gapBoundState;

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a CompLayoutData.
         */
        public CompLayoutData( Component component, CompConstraint constraint ){

            this.size = new CompSize( constraint.size         );
            this.gap  = new GapSize ( PropLayout.this.compGap );

            Dimension compPrefSize = component.getPreferredSize( );

            if( this.size.width.pref < 0 )
                this.size.width.pref = compPrefSize.width;

            if( this.size.height.pref < 0 )
                this.size.height.pref = compPrefSize.height;

            if( component.isMinimumSizeSet( ) ){

                Dimension compMinSize = component.getMinimumSize( );

                if( this.size.width.min < 0 )
                    this.size.width.min = compMinSize.width;

                if( this.size.height.min < 0 )
                    this.size.height.min = compMinSize.height;
            }

            if( component.isMaximumSizeSet( ) ){

                Dimension compMaxSize = component.getMaximumSize( );

                if( this.size.width.max < 0 )
                    this.size.width.max = compMaxSize.width;

                if( this.size.height.max < 0 )
                    this.size.height.max = compMaxSize.height;
            }

            this.gap.copyGapSize( constraint.gap, false );

            this.isCompProp = PropLayout.this.getCompPropState( this.size );
            this.isGapProp  = PropLayout.this.getGapPropState ( this.gap  );
            this.isGapSpec  = GapState.getGapSpecState( constraint.gap );

            this.align = PropLayout.this.compAlign;

            if( constraint.align != null )
                this.align = constraint.align;
        }
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                  Inner Class: RowConstraint                                  XXXXX
    //XXX                                                                                              XXXXX
    //XXX                 An Object that contains non-volatile layout data for a row.                  XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An Object that contains non-volatile layout data for a row.
     */
    private class RowConstraint{

        public int row;
        public GapSize gap;
        public HorzAlign align;

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a RowConstraint.
         */
        public RowConstraint( int row ){

            this.row = row;
            this.gap = new GapSize( );
        }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Mutator Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Merges Constraint data into this RowConstraint.
         *
         * @param constraint the Constraint data
         */
        public void setData( Constraint constraint ){

            this.gap.copyGapSize( constraint.rowGap, true );

            if( constraint.rowAlign == HorzAlign.CLEAR )
                this.align = null;

            else if( constraint.rowAlign != null )
                this.align = constraint.rowAlign;
        }
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                 Inner Class: CompConstraint                                  XXXXX
    //XXX                                                                                              XXXXX
    //XXX              An Object that contains non-volatile layout data for a component.               XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An Object that contains non-volatile layout data for a component.
     */
    private class CompConstraint{

        public int column;

        public CompSize size;
        public GapSize gap;

        public VertAlign align;

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a CompConstraint.
         */
        public CompConstraint( int column ){

            this.column = column;

            this.size = new CompSize( );
            this.gap  = new GapSize( );
        }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Mutator Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Merges Constraint data into this CompConstraint.
         *
         * @param constraint the Constraint data
         */
        public void setData( Constraint constraint ){

            this.size.copyCompSize( constraint.compSize, true );
            this.gap .copyGapSize ( constraint.compGap , true );

            if( constraint.compAlign == VertAlign.CLEAR )
                this.align = null;

            else if( constraint.compAlign != null )
                this.align = constraint.compAlign;
        }
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                       Enum: VertAlign                                        XXXXX
    //XXX                                                                                              XXXXX
    //XXX                    An enumeration that contains vertical alignment modes.                    XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An enumeration that contains vertical alignment modes.
     */
    public enum VertAlign{

        /**
         * Top alignment.
         */
        TOP,

        /**
         * Middle alignment.
         */
        MIDDLE,

        /**
         * Bottom alignment.
         */
        BOTTOM,

        /**
         * Clear the child alignment and use default alignment.
         */
        CLEAR;
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                       Enum: HorzAlign                                        XXXXX
    //XXX                                                                                              XXXXX
    //XXX                   An enumeration that contains horizontal alignment modes.                   XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An enumeration that contains horizontal alignment modes.
     */
    public enum HorzAlign{

        /**
         * Left alignment.
         */
        LEFT,

        /**
         * Center alignment.
         */
        CENTER,

        /**
         * Right alignment.
         */
        RIGHT,

        /**
         * Clear the child alignment and use default alignment.
         */
        CLEAR;
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                      Inner Class: Size                                       XXXXX
    //XXX                                                                                              XXXXX
    //XXX             An Object that contains size data. All values are initialized to -1.             XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An Object that contains size data. All values are initialized to -1.
     */
    public static class Size{

        public double pref;
        public double min;
        public double max;

        public Boolean clear;

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a Size.
         */
        public Size( ){

            this.pref = -1;
            this.min  = -1;
            this.max  = -1;
        }

        /**
         * Constructs a Size.
         *
         * @param pref the preferred size
         */
        public Size( double pref ){

            this.pref = pref;
            this.min  = -1;
            this.max  = -1;
        }

        /**
         * Constructs a Size.
         *
         * @param pref the preferred size
         * @param min the minimum size
         * @param max the maximum size
         */
        public Size( double pref, double min, double max ){

            this.pref = pref;
            this.min  = min;
            this.max  = max;
        }

        /**
         * Constructs a Size.
         *
         * @param size the Size to clone
         */
        public Size( Size size ){

            this( );

            cloneSize( size );
        }

        /**
         * Constructs a Size.
         *
         * @param size the Size to copy
         * @param isChild <tt>true</tt> if this Size overrides a more general Size; <tt>false</tt> otherwise
         */
        public Size( Size size, boolean isChild ){

            this( );

            copySize( size, isChild );
        }

        /**
         * Constructs a Size. Use this constructor to clear all values. The behavior changes based on usage.
         * If used in a Size that overrides a more general Size, this Size will be cleared in favor of the
         * more general Size values. If used in a general Size, only the min,max values will be cleared.
         *
         * @param clear <tt>true</tt> to clear all values; <tt>false</tt> to ignore this Size
         */
        public Size( boolean clear ){

            this.clear = clear;
        }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Public Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Performs an identical deep copy of size. If size is null, this Size is cleared.
         *
         * @param size the Size to clone
         */
        public void cloneSize( Size size ){

            if( size == null ){

                pref = -1;
                min  = -1;
                max  = -1;
            }
            else{

                pref = size.pref;
                min  = size.min;
                max  = size.max;
            }
        }

        /**
         * Performs a deep copy of size. If size is null or the clear constructor was called with false,
         * this is a no-op. If the clear constructor was called with true, this Size is cleared with
         * behavior depending on <tt>isChild</tt>. If a value is -1, it is ignored. If a value < -1, this
         * value is cleared with behavior depending on <tt>isChild</tt>. If <tt>isChild</tt> is true, this
         * Size overrides a more general Size and all values can be cleared in favor of the more general
         * Size values. If <tt>false</tt>, this Size represents the most general Size and only the min,max
         * values can be cleared.
         *
         * @param size the Size to copy
         * @param isChild <tt>true</tt> if this Size overrides a more general Size; <tt>false</tt> otherwise
         */
        public void copySize( Size size, boolean isChild ){

            if( size == null || (size.clear != null && !size.clear) )
                return;

            if( isChild && ((size.pref < -1) || (size.clear != null && size.clear)) )
                pref = -1;

            else if( size.pref >= 0 )
                pref = size.pref;

            if( size.min < -1 || (size.clear != null && size.clear) )
                min = -1;

            else if( size.min >= 0 )
                min = size.min;

            if( size.max < -1 || (size.clear != null && size.clear) )
                max = -1;

            else if( size.max >= 0 )
                max = size.max;
        }
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                    Inner Class: SizeState                                    XXXXX
    //XXX                                                                                              XXXXX
    //XXX                           An Object that contains size state data.                           XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An Object that contains size state data.
     */
    public static class SizeState{

        public boolean pref;
        public boolean min;
        public boolean max;

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a SizeState.
         */
        public SizeState( ){}

        /**
         * Constructs a SizeState.
         *
         * @param all the common state
         */
        public SizeState( boolean all ){

            this.pref = all;
            this.min  = all;
            this.max  = all;
        }

        /**
         * Constructs a SizeState.
         *
         * @param pref the preferred size state
         * @param min the minimum size state
         * @param max the maximum size state
         */
        public SizeState( boolean pref, boolean min, boolean max ){

            this.pref = pref;
            this.min  = min;
            this.max  = max;
        }

        /**
         * Constructs a SizeState.
         *
         * @param sizeState the SizeState to clone
         */
        public SizeState( SizeState sizeState ){

            if( sizeState == null ){

                this.pref = false;
                this.min  = false;
                this.max  = false;

                return;
            }

            this.pref = sizeState.pref;
            this.min  = sizeState.min;
            this.max  = sizeState.max;
        }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Static Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a SizeState that represents whether the Size values are specified. A specified value
         * is flagged <tt>true</tt>.
         *
         * @param Size the Size to analyze for specified values
         */
        public static SizeState getSizeSpecState( Size size ){

            SizeState isSizeSpec = new SizeState( );

            if( size != null ){

                if( size.pref >= 0 )
                    isSizeSpec.pref = true;

                if( size.min >= 0 )
                    isSizeSpec.min = true;

                if( size.max >= 0 )
                    isSizeSpec.max = true;
            }

            return isSizeSpec;
        }
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                     Inner Class: GapSize                                     XXXXX
    //XXX                                                                                              XXXXX
    //XXX     An Object that contains gap size data. All values are initialized with the Size          XXXXX
    //XXX     default constructor.                                                                     XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An Object that contains gap size data. All values are initialized with the Size default constructor.
     */
    public static class GapSize{

        public Size left;
        public Size right;
        public Size top;
        public Size bottom;

        public Boolean clear;

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a GapSize.
         */
        public GapSize( ){

            this.left   = new Size( );
            this.right  = new Size( );
            this.top    = new Size( );
            this.bottom = new Size( );
        }

        /**
         * Constructs a GapSize.
         *
         * @param allPref the common preferred size
         */
        public GapSize( double allPref ){

            this.left   = new Size( allPref );
            this.right  = new Size( allPref );
            this.top    = new Size( allPref );
            this.bottom = new Size( allPref );
        }

        /**
         * Constructs a GapSize.
         *
         * @param leftPref the left preferred size
         * @param rightPref the right preferred size
         * @param topPref the top preferred size
         * @param bottomPref the bottom preferred size
         */
        public GapSize( double leftPref, double rightPref, double topPref, double bottomPref ){

            this.left   = new Size( leftPref   );
            this.right  = new Size( rightPref  );
            this.top    = new Size( topPref    );
            this.bottom = new Size( bottomPref );
        }

        /**
         * Constructs a GapSize.
         *
         * @param all the common Size
         */
        public GapSize( Size all ){

            this.left   = new Size( all );
            this.right  = new Size( all );
            this.top    = new Size( all );
            this.bottom = new Size( all );
        }

        /**
         * Constructs a GapSize.
         *
         * @param left the left Size
         * @param right the right Size
         * @param top the top Size
         * @param bottom the bottom Size
         */
        public GapSize( Size left, Size right, Size top, Size bottom ){

            this.left   = new Size( left   );
            this.right  = new Size( right  );
            this.top    = new Size( top    );
            this.bottom = new Size( bottom );
        }

        /**
         * Constructs a GapSize.
         *
         * @param gapSize the GapSize to clone
         */
        public GapSize( GapSize gapSize ){

            this( );

            cloneGapSize( gapSize );
        }

        /**
         * Constructs a GapSize.
         *
         * @param gapSize the GapSize to copy
         * @param isChild <tt>true</tt> if this GapSize overrides a more general GapSize; <tt>false</tt> otherwise
         */
        public GapSize( GapSize gapSize, boolean isChild ){

            this( );

            copyGapSize( gapSize, isChild );
        }

        /**
         * Constructs a GapSize. Use this constructor to clear all values. The behavior changes based on
         * usage. If used in a GapSize that overrides a more general GapSize, this GapSize will be cleared
         * in favor of the more general GapSize values. If used in a general GapSize, only the min,max
         * values will be cleared.
         *
         * @param clear <tt>true</tt> to clear all values; <tt>false</tt> to ignore this GapSize
         */
        public GapSize( boolean clear ){

            this.clear = clear;
        }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Public Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Performs an identical deep copy of gapSize. If gapSize is null, this gapSize is cleared.
         *
         * @param gapSize the GapSize to clone
         */
        public void cloneGapSize( GapSize gapSize ){

            if( gapSize == null ){

                left   = new Size( );
                right  = new Size( );
                top    = new Size( );
                bottom = new Size( );
            }
            else{

                left  .cloneSize( gapSize.left   );
                right .cloneSize( gapSize.right  );
                top   .cloneSize( gapSize.top    );
                bottom.cloneSize( gapSize.bottom );
            }
        }

        /**
         * Performs a deep copy of gapSize. If gapSize is null or the clear constructor was called with
         * false, this is a no-op. If the clear constructor was called with true, this GapSize is cleared
         * with behavior depending on <tt>isChild</tt>. If a Size is null, it is ignored. If
         * <tt>isChild</tt> is true, this GapSize overrides a more general GapSize and all values can be
         * cleared in favor of the more general GapSize values. If <tt>false</tt>, this GapSize represents
         * the most general GapSize and only the min,max values can be cleared.
         *
         * @param gapSize the GapSize to copy
         * @param isChild <tt>true</tt> if this GapSize overrides a more general GapSize; <tt>false</tt> otherwise
         */
        public void copyGapSize( GapSize gapSize, boolean isChild ){

            if( gapSize == null || (gapSize.clear != null && !gapSize.clear) )
                return;

            if( gapSize.clear != null && gapSize.clear ){

                gapSize.left   = new Size( true );
                gapSize.right  = new Size( true );
                gapSize.top    = new Size( true );
                gapSize.bottom = new Size( true );
            }

            left  .copySize( gapSize.left  , isChild );
            right .copySize( gapSize.right , isChild );
            top   .copySize( gapSize.top   , isChild );
            bottom.copySize( gapSize.bottom, isChild );
        }
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                    Inner Class: GapState                                     XXXXX
    //XXX                                                                                              XXXXX
    //XXX                         An Object that contains gap size state data.                         XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An Object that contains gap size state data.
     */
    public static class GapState{

        public SizeState left;
        public SizeState right;
        public SizeState top;
        public SizeState bottom;

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a GapState.
         */
        public GapState( ){

            this.left   = new SizeState( );
            this.right  = new SizeState( );
            this.top    = new SizeState( );
            this.bottom = new SizeState( );
        }

        /**
         * Constructs a GapState.
         *
         * @param allState the common state
         */
        public GapState( boolean allState ){

            this.left   = new SizeState( allState );
            this.right  = new SizeState( allState );
            this.top    = new SizeState( allState );
            this.bottom = new SizeState( allState );
        }

        /**
         * Constructs a GapState.
         *
         * @param leftState the left state
         * @param rightState the right state
         * @param topState the top state
         * @param bottomState the bottom state
         */
        public GapState( boolean leftState, boolean rightState, boolean topState, boolean bottomState ){

            this.left   = new SizeState( leftState   );
            this.right  = new SizeState( rightState  );
            this.top    = new SizeState( topState    );
            this.bottom = new SizeState( bottomState );
        }

        /**
         * Constructs a GapState.
         *
         * @param all the common SizeState
         */
        public GapState( SizeState all ){

            this.left   = new SizeState( all );
            this.right  = new SizeState( all );
            this.top    = new SizeState( all );
            this.bottom = new SizeState( all );
        }

        /**
         * Constructs a GapState.
         *
         * @param left the left SizeState
         * @param right the right SizeState
         * @param top the top SizeState
         * @param bottom the bottom SizeState
         */
        public GapState( SizeState left, SizeState right, SizeState top, SizeState bottom ){

            this.left   = new SizeState( left   );
            this.right  = new SizeState( right  );
            this.top    = new SizeState( top    );
            this.bottom = new SizeState( bottom );
        }

        /**
         * Constructs a GapState.
         *
         * @param gapState the GapState to clone
         */
        public GapState( GapState gapState ){

            if( gapState == null ){

                this.left   = new SizeState( );
                this.right  = new SizeState( );
                this.top    = new SizeState( );
                this.bottom = new SizeState( );

                return;
            }

            this.left   = new SizeState( gapState.left   );
            this.right  = new SizeState( gapState.right  );
            this.top    = new SizeState( gapState.top    );
            this.bottom = new SizeState( gapState.bottom );
        }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Static Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a GapState that represents whether the GapSize values are specified. A specified value
         * is flagged <tt>true</tt>.
         *
         * @param gapSize the GapSize to analyze for specified values
         */
        public static GapState getGapSpecState( GapSize gapSize ){

            GapState isGapSpec = new GapState( );

            if( gapSize != null ){

                isGapSpec.left   = SizeState.getSizeSpecState( gapSize.left   );
                isGapSpec.right  = SizeState.getSizeSpecState( gapSize.right  );
                isGapSpec.top    = SizeState.getSizeSpecState( gapSize.top    );
                isGapSpec.bottom = SizeState.getSizeSpecState( gapSize.bottom );
            }

            return isGapSpec;
        }
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                    Inner Class: CompSize                                     XXXXX
    //XXX                                                                                              XXXXX
    //XXX                         An Object that contains component size data.                         XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An Object that contains component size data.
     */
    public static class CompSize{

        public Size width;
        public Size height;

        public Boolean clear;

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a CompSize.
         */
        public CompSize( ){

            this.width  = new Size( );
            this.height = new Size( );
        }

        /**
         * Constructs a CompSize.
         *
         * @param allPref the common preferred size
         */
        public CompSize( double allPref ){

            this.width  = new Size( allPref );
            this.height = new Size( allPref );
        }

        /**
         * Constructs a CompSize.
         *
         * @param widthPref the width preferred size
         * @param heightPref the height preferred size
         */
        public CompSize( double widthPref, double heightPref ){

            this.width  = new Size( widthPref  );
            this.height = new Size( heightPref );
        }

        /**
         * Constructs a CompSize.
         *
         * @param all the common Size
         */
        public CompSize( Size all ){

            this.width  = new Size( all );
            this.height = new Size( all );
        }

        /**
         * Constructs a CompSize.
         *
         * @param width the width Size
         * @param height the height Size
         */
        public CompSize( Size width, Size height ){

            this.width  = new Size( width  );
            this.height = new Size( height );
        }

        /**
         * Constructs a CompSize.
         *
         * @param compSize the CompSize to clone
         */
        public CompSize( CompSize compSize ){

            this( );

            cloneCompSize( compSize );
        }

        /**
         * Constructs a CompSize.
         *
         * @param compSize the CompSize to copy
         * @param isChild <tt>true</tt> if this CompSize overrides a more general CompSize; <tt>false</tt> otherwise
         */
        public CompSize( CompSize compSize, boolean isChild ){

            this( );

            copyCompSize( compSize, isChild );
        }

        /**
         * Constructs a CompSize. Use this constructor to clear all values. The behavior changes based on
         * usage. If used in a CompSize that overrides a more general CompSize, this CompSize will be cleared
         * in favor of the more general CompSize values. If used in a general CompSize, only the min,max
         * values will be cleared.
         *
         * @param clear <tt>true</tt> to clear all values; <tt>false</tt> to ignore this CompSize
         */
        public CompSize( boolean clear ){

            this.clear = clear;
        }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Public Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Performs an identical deep copy of compSize. If compSize is null, this compSize is cleared.
         *
         * @param compSize the CompSize to clone
         */
        public void cloneCompSize( CompSize compSize ){

            if( compSize == null ){

                width  = new Size( );
                height = new Size( );
            }
            else{

                width .cloneSize( compSize.width  );
                height.cloneSize( compSize.height );
            }
        }

        /**
         * Performs a deep copy of compSize. If compSize is null or the clear constructor was called with
         * false, this is a no-op. If the clear constructor was called with true, this CompSize is cleared
         * with behavior depending on <tt>isChild</tt>. If a Size is null, it is ignored. If
         * <tt>isChild</tt> is true, this CompSize overrides a more general CompSize and all values can be
         * cleared in favor of the more general CompSize values. If <tt>false</tt>, this CompSize represents
         * the most general CompSize and only the min,max values can be cleared.
         *
         * @param compSize the CompSize to copy
         * @param isChild <tt>true</tt> if this CompSize overrides a more general CompSize; <tt>false</tt> otherwise
         */
        public void copyCompSize( CompSize compSize, boolean isChild ){

            if( compSize == null || (compSize.clear != null && !compSize.clear) )
                return;

            if( compSize.clear != null && compSize.clear ){

                compSize.width  = new Size( true );
                compSize.height = new Size( true );
            }

            width .copySize( compSize.width , isChild );
            height.copySize( compSize.height, isChild );
        }
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                    Inner Class: CompState                                    XXXXX
    //XXX                                                                                              XXXXX
    //XXX                      An Object that contains component size state data.                      XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An Object that contains component size state data.
     */
    public static class CompState{

        public SizeState width;
        public SizeState height;

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a CompState.
         */
        public CompState( ){

            this.width  = new SizeState( );
            this.height = new SizeState( );
        }

        /**
         * Constructs a CompState.
         *
         * @param allState the common state
         */
        public CompState( boolean allState ){

            this.width  = new SizeState( allState );
            this.height = new SizeState( allState );
        }

        /**
         * Constructs a CompState.
         *
         * @param widthState the width state
         * @param heightState the height state
         */
        public CompState( boolean widthState, boolean heightState ){

            this.width  = new SizeState( widthState  );
            this.height = new SizeState( heightState );
        }

        /**
         * Constructs a CompState.
         *
         * @param all the common SizeState
         */
        public CompState( SizeState all ){

            this.width  = new SizeState( all );
            this.height = new SizeState( all );
        }

        /**
         * Constructs a CompState.
         *
         * @param width the width SizeState
         * @param height the height SizeState
         */
        public CompState( SizeState width, SizeState height ){

            this.width  = new SizeState( width  );
            this.height = new SizeState( height );
        }

        /**
         * Constructs a CompState.
         *
         * @param compState the CompState to clone
         */
        public CompState( CompState compState ){

            if( compState == null ){

                this.width  = new SizeState( );
                this.height = new SizeState( );

                return;
            }

            this.width  = new SizeState( compState.width  );
            this.height = new SizeState( compState.height );
        }
    }





    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXX                                                                                              XXXXX
    //XXX                                   Inner Class: Constraint                                    XXXXX
    //XXX                                                                                              XXXXX
    //XXX           An Object that contains data necessary to add a component to the layout.           XXXXX
    //XXX                                                                                              XXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * An Object that contains data necessary to add a component to the layout.
     */
    public static class Constraint{

        private int row;
        private int column;

        private CompSize compSize;

        private GapSize rowGap;
        private GapSize compGap;

        private HorzAlign rowAlign;
        private VertAlign compAlign;

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Constructors     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Constructs a Constraint.
         */
        public Constraint( ){

            this( -1, -1 );
        }

        /**
         * Constructs a Constraint.
         *
         * @param row the row in which to add the component
         */
        public Constraint( int row ){

            this( row, -1 );
        }

        /**
         * Constructs a Constraint.
         *
         * @param row the row in which to add the component
         * @param column the column in the row in which to add the component
         */
        public Constraint( int row, int column ){

            this.row    = row;
            this.column = column;

            this.compSize = new CompSize( );

            this.rowGap  = new GapSize( );
            this.compGap = new GapSize( );
        }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Accessor Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Retrieves the row in which to add the component.
         *
         * @return the row in which to add the component
         */
        public Integer getRow( ){

            return row;
        }

        /**
         * Retrieves the column, within a row, in which to add the component
         *
         * @return the column, within a row, in which to add the component
         */
        public Integer getColumn( ){

            return column;
        }

        /**
         * Retrieves the component size.
         *
         * @return the component size
         */
        public CompSize getCompSize( ){

            return new CompSize( compSize );
        }

        /**
         * Retrieves the row gap.
         *
         * @return the row gap
         */
        public GapSize getRowGap( ){

            return new GapSize( rowGap );
        }

        /**
         * Retrieves the component gap.
         *
         * @return the component gap
         */
        public GapSize getCompGap( ){

            return new GapSize( compGap );
        }

        /**
         * Retrieves the horizontal row alignment relative to the parent container.
         *
         * @return the horizontal row alignment
         */
        public HorzAlign getRowAlignment( ){

            return rowAlign;
        }

        /**
         * Retrieves the vertical component alignment relative to the row.
         *
         * @return the vertical component alignment
         */
        public VertAlign getCompAlignment( ){

            return compAlign;
        }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX     Mutator Methods     XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        /**
         * Sets the row in which to add the component. A value of -1 adds the component to the existing last
         * row. Any other negative value increments the row count and adds the component to the last row.
         *
         * @param row the row in which to add the component
         *
         * @return method chain
         */
        public Constraint setRow( int row ){

            this.row = row;

            return this;
        }

        /**
         * Sets the column, within a row, in which to add the component. A negative value increments the column
         * count in the row and adds the component to the last column.
         *
         * @param column the column, within a row, in which to add the component
         *
         * @return method chain
         */
        public Constraint setColumn( int column ){

            this.column = column;

            return this;
        }

        /**
         * Sets the preferred component size. If a value is -1, it is ignored. If a value < -1, it is
         * cleared and the component preferred size is used.
         *
         * @param widthPref the preferred component width
         * @param heightPref the preferred component height
         *
         * @return method chain
         */
        public Constraint setCompSize( double widthPref, double heightPref ){

            if( widthPref >= 0 )
                this.compSize.width.pref = widthPref;

            else if( widthPref < -1 )
                this.compSize.width.pref = -1;

            if( heightPref >= 0 )
                this.compSize.height.pref = heightPref;

            else if( heightPref < -1 )
                this.compSize.height.pref = -1;

            return this;
        }

        /**
         * Sets the component size. If a Size is null or the Size clear constructor was called with false,
         * it is ignored. If the Size clear constructor was called with true, all values are cleared and the
         * component preferred size is used. If a value is -1, it is ignored. If a value < -1, it is
         * cleared.
         *
         * @param width the component width
         * @param height the component height
         *
         * @return method chain
         */
        public Constraint setCompSize( Size width, Size height ){

            this.compSize.width .copySize( width,  true );
            this.compSize.height.copySize( height, true );

            return this;
        }

        /**
         * Sets the component Size. If the CompSize is null or the CompSize clear constructor was called
         * with false, it is ignored. If the CompSize clear constructor was called with true, all values are
         * cleared and the component preferred size is used. The same goes for each Size value. If a value
         * is -1, it is ignored. If a value < -1, it is cleared.
         *
         * @param compSize the component size
         *
         * @return method chain
         */
        public Constraint setCompSize( CompSize compSize ){

            this.compSize.copyCompSize( compSize, true );
            return this;
        }

        /**
         * Sets the top preferred row gap. This value overrides the default row gap. Unlike the default row
         * gap, the gap preceeding the first row may be specified. If the value is -1, it is ignored. If the
         * value < -1, it is cleared and the default row gap value is used.
         *
         * @param topPref the top preferred row gap
         *
         * @return method chain
         */
        public Constraint setRowGap( double topPref ){

            return setRowGap( -1, -1, topPref, -1 );
        }

        /**
         * Sets the preferred row gaps. These values override the default row gaps. Unlike the default row
         * gaps, the gaps preceeding the first and trailing the last row may be specified. If a value is -1,
         * it is ignored. If a value < -1, it is cleared and the default row gap value is used.
         *
         * @param leftPref the left preferred row gap
         * @param rightPref the right preferred row gap
         * @param topPref the top preferred row gap
         * @param bottomPref the bottom preferred row gap
         *
         * @return method chain
         */
        public Constraint setRowGap( double leftPref, double rightPref, double topPref, double bottomPref ){

            if( leftPref >= 0 )
                this.rowGap.left.pref = leftPref;

            else if( leftPref < -1 )
                this.rowGap.left.pref = -1;

            if( rightPref >= 0 )
                this.rowGap.right.pref = rightPref;

            else if( rightPref < -1 )
                this.rowGap.right.pref = -1;

            if( topPref >= 0 )
                this.rowGap.top.pref = topPref;

            else if( topPref < -1 )
                this.rowGap.top.pref = -1;

            if( bottomPref >= 0 )
                this.rowGap.bottom.pref = bottomPref;

            else if( bottomPref < -1 )
                this.rowGap.bottom.pref = -1;

            return this;
        }

        /**
         * Sets the row gaps. These values override the default row gaps. Unlike the default row gaps, the
         * gaps preceeding the first and trailing the last row may be specified. If a Size is null or the
         * Size clear constructor was called with false, it is ignored. If the Size clear constructor was
         * called with true, all values for the Size are cleared and the default row gap is used. If a
         * value is -1, it is ignored. If a value < -1, it is cleared.
         *
         * @param left the left row gap
         * @param right the right row gap
         * @param top the top row gap
         * @param bottom the bottom row gap
         *
         * @return method chain
         */
        public Constraint setRowGap( Size left, Size right, Size top, Size bottom ){

            this.rowGap.left  .copySize( left  , true );
            this.rowGap.right .copySize( right , true );
            this.rowGap.top   .copySize( top   , true );
            this.rowGap.bottom.copySize( bottom, true );

            return this;
        }

        /**
         * Sets the row gaps. These values override the default row gaps. Unlike the default row gaps, the
         * gaps preceeding the first and trailing the last row may be specified. If the GapSize is null or
         * the GapSize clear constructor was called with false, it is ignored. If the GapSize clear
         * constructor was called with true, all values are cleared and the default row gaps are used. The
         * same goes for each Size value. If a value is -1, it is ignored. If a value < -1, it is cleared.
         *
         * @param rowGap the row gaps
         *
         * @return method chain
         */
        public Constraint setRowGap( GapSize rowGap ){

            this.rowGap.copyGapSize( rowGap, true );
            return this;
        }

        /**
         * Sets the left preferred component gap. This value overrides the default component gap. Unlike the
         * default component gap, the gap preceeding the first component may be specified. If the value
         * is -1, it is ignored. If the value < -1, it is cleared and the default component gap value is
         * used.
         *
         * @param leftPref the left preferred component gap
         *
         * @return method chain
         */
        public Constraint setCompGap( double prefLeft ){

            return setCompGap( prefLeft, -1, -1, -1 );
        }

        /**
         * Sets the preferred component gaps. These values override the default component gaps. Unlike the
         * default component gaps, the gaps preceeding the first and trailing the last component may be
         * specified. If a value is -1, it is ignored. If a value < -1, it is cleared and the default
         * component gap value is used.
         *
         * @param leftPref the left preferred component gap
         * @param rightPref the right preferred component gap
         * @param topPref the top preferred component gap
         * @param bottomPref the bottom preferred component gap
         *
         * @return method chain
         */
        public Constraint setCompGap( double leftPref, double rightPref, double topPref, double bottomPref ){

            if( leftPref >= 0 )
                this.compGap.left.pref = leftPref;

            else if( leftPref < -1 )
                this.compGap.left.pref = -1;

            if( rightPref >= 0 )
                this.compGap.right.pref = rightPref;

            else if( rightPref < -1 )
                this.compGap.right.pref = -1;

            if( topPref >= 0 )
                this.compGap.top.pref = topPref;

            else if( topPref < -1 )
                this.compGap.top.pref = -1;

            if( bottomPref >= 0 )
                this.compGap.bottom.pref = bottomPref;

            else if( bottomPref < -1 )
                this.compGap.bottom.pref = -1;

            return this;
        }

        /**
         * Sets the component gaps. These values override the default component gaps. Unlike the default
         * component gaps, the gaps preceeding the first and trailing the last component may be specified.
         * If a Size is null or the Size clear constructor was called with false, it is ignored. If the Size
         * clear constructor was called with true, all values for the Size are cleared and the default
         * component gap is used. If a value is -1, it is ignored. If a value < -1, it is cleared.
         *
         * @param left the left component gap
         * @param right the right component gap
         * @param top the top component gap
         * @param bottom the bottom component gap
         *
         * @return method chain
         */
        public Constraint setCompGap( Size left, Size right, Size top, Size bottom ){

            this.compGap.left  .copySize( left  , true );
            this.compGap.right .copySize( right , true );
            this.compGap.top   .copySize( top   , true );
            this.compGap.bottom.copySize( bottom, true );

            return this;
        }

        /**
         * Sets the component gaps. These values override the default component gaps. Unlike the default
         * component gaps, the gaps preceeding the first and trailing the last component may be specified.
         * If the GapSize is null or the GapSize clear constructor was called with false, it is ignored.
         * If the GapSize clear constructor was called with true, all values are cleared and the default
         * component gaps are used. The same goes for each Size value. If a value is -1, it is ignored.
         * If a value < -1, it is cleared.
         *
         * @param compGap the component gaps
         *
         * @return method chain
         */
        public Constraint setCompGap( GapSize compGap ){

            this.compGap.copyGapSize( compGap, true );
            return this;
        }

        /**
         * Sets the horizontal row alignment relative to the parent container. This value overrides the
         * default horizontal row alignment. If the HorzAlign is null, the value is ignored. If the
         * HorzAlign is clear, the value is cleared and the default horizontal row alignment is used.
         *
         *
         * @param alignment the horizontal row alignment
         *
         * @return method chain
         */
        public Constraint setRowAlignment( HorzAlign alignment ){

            this.rowAlign = alignment;
            return this;
        }

        /**
         * Sets the vertical component alignment relative to the containing row. This value overrides the
         * default vertical component alignment. If the VertAlign is null, the value is ignored. If the
         * VertAlign is clear, the value is cleared and the default vertical component alignment is used.
         *
         *
         * @param alignment the vertical component alignment
         *
         * @return method chain
         */
        public Constraint setCompAlignment( VertAlign alignment ){

            this.compAlign = alignment;
            return this;
        }
    }
}
