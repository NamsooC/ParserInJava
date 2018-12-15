import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.w3c.tidy.Tidy;

public class HtmlValidator {

	public int checkHtml(String fileName){
		Tidy tidy = new Tidy();
		OutputStream out = null;
		try {
			InputStream in = new FileInputStream("src/"+fileName);
			
			tidy.parse(in,out);
			in.close();
			return tidy.getParseErrors();
		}  catch (IOException e) {e.printStackTrace();}
		
		//Code should not reach here
		return -1;
	}
}
