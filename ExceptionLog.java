package collection;



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExceptionLog {
	
	String error = new String();
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	
	public void insertSQLLog(String e){ //logs all action and issues conducted by SQL insertions
		
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter ("SQL_log.txt", true)));
		    out.println(dateFormat.format(date)+ "--" + e);
		    out.close();
		} 
		catch (IOException err) {
		}
	}
	
	public void insertGUILog(String e) { //logs all action and issues conducted by GUI 
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("GUI_log.txt", true)));
		    out.println(dateFormat.format(date)+ "--Exception: " + e);
		    out.close();
		} 
		catch (IOException err) {

		}
	}


}
