package bacy_testscript;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import comm.DisplayGui;

public class Demo{
	
	public void Start(String indexpath) throws IOException, ParseException{
		final DisplayGui display=new DisplayGui();
		display.home(indexpath);
		
		class ColseEvent extends WindowAdapter{
			public void windowClosing(WindowEvent e)
	          {
//	            super.windowClosing(e); 
				try {
					display.StoreHistory();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	          }	
		}
		display.addWindowListener(new ColseEvent());
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		
		String path=System.getProperty("java.class.path");
		String s[]=path.split("/");
		StringBuffer ppa=new StringBuffer();
		ppa.append(s[0]+"\\"+"index"+"\\");
//		System.out.println(ppa.toString());
		Demo dem=new Demo();
			
//		dem.Start(ppa.toString());
		dem.Start("D:\\Lucene\\index\\");
		
	}

}
