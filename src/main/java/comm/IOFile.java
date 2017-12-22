package comm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IOFile {
	
	public void Writer(String s,String filename) throws IOException{
		
		File file=new File(filename);
		
		if(!file.exists()){   
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file,true);
		
		fw.write(s);
		fw.write('\r');
		fw.write('\n');
		fw.flush();
		fw.close();
		
	}
	
	public Map<String,String> Reader(String filename) throws IOException{
		
		Map<String,String> m=new HashMap<String,String>();
		
		File file=new File(filename);          
        if(file.isFile() && file.exists()){ //判断文件是否存在              
        	InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");//考虑到编码格式           	
            BufferedReader bufferedReader = new BufferedReader(read); 
            String line=null; 
            while((line=bufferedReader.readLine()) != null){               	            
            	String[] tmp=line.split(":");
            	m.put(tmp[0],tmp[1]); 	             
            }
        }
		
		return m;
		
	}

}
