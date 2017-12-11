package comm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import net.sf.json.JSONObject;


public class IOHttp {
	
	private HttpURLConnection httpconn;
	private String url;
	
	public IOHttp(String url){
		this.url=url;
	}
	
    public JSONObject sendPost(String body) throws Exception {
        
    	OutputStream outputstream=null;
    	OutputStreamWriter outputstreamwriter=null;
    	
    	InputStream inputstream=null;
    	InputStreamReader inputstreamreader=null;
    	BufferedReader reader=null;
    	
    	JSONObject obj=new JSONObject();
    	StringBuffer result=new StringBuffer();

        
        try {
        	
            // 设置是否向httpconn输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
        	
        	httpconn.setDoOutput(true);
        	httpconn.setDoInput(true);

        	httpconn.setRequestProperty("Content-Length", String.valueOf(body.length()));
            
            outputstream = httpconn.getOutputStream();
            outputstreamwriter = new OutputStreamWriter(outputstream);
            outputstreamwriter.write(body);
            outputstreamwriter.flush();// 刷新
            
            if (httpconn.getResponseCode() >= 300){
            	 throw new Exception("HTTP Request is not success, Response code is " + httpconn.getResponseCode());
            }
            
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK){
            
            	inputstream = httpconn.getInputStream();
            	inputstreamreader= new InputStreamReader(inputstream,"utf-8");
            	reader = new BufferedReader(inputstreamreader);
            
            	String line;

            
            	while ((line = reader.readLine()) != null)     	
            		result.append(line);            
         
            }
            
        }
        finally{
        	
            try{ 
            	if(outputstream!=null)
            		outputstream.close();      
                if(outputstreamwriter!=null)
                	outputstreamwriter.close(); 
                if(inputstream!=null)
                	inputstream.close();
                if(inputstreamreader!=null)
                	inputstreamreader.close();
                if(reader!=null)
                	reader.close();       
            }catch(IOException ex){
                ex.printStackTrace(); 
            }
        }
        
//        obj=(JSONObject) JSONObject.stringToValue(result.toString());
        obj=JSONObject.fromObject(result.toString());
        return obj;
        
    }

    public URLConnection GetConnect(){
    	return httpconn;
    }

    public Boolean Connect() throws IOException{
		
    	Boolean f=null;
    	URL rurl = new URL(url);
    	URLConnection conn=rurl.openConnection();
    	httpconn=(HttpURLConnection) conn;
    	
    	httpconn.setDoInput(true);
    	httpconn.setConnectTimeout(1000*5);
    	httpconn.setReadTimeout(1000*5);
    	httpconn.setUseCaches(false);
    	httpconn.setRequestProperty("Accept-Charset","utf-8");
    	httpconn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        httpconn.setRequestProperty("accept", "*/*");
        httpconn.setRequestProperty("connection", "Keep-Alive");
        
        httpconn.connect();
        
        if (httpconn.getResponseCode() >= 300)
        	f=false; 

        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
        	f=true;
    	
        return f;  	
    }
    
    public String Map2From(Map<String,String> param){
		StringBuffer sb=new StringBuffer();
		
		for (Map.Entry<String, String> e : param.entrySet()) {  
            sb.append(e.getKey());  
            sb.append("=");  
            sb.append(e.getValue());  
            sb.append("&");  
        }  
        sb.substring(0, sb.length() - 1);
        
        return sb.toString(); 	
    }

}
