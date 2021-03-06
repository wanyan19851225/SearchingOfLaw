package Com;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;


public class IOHttp {
	
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
    	String s=null;
        try {
        	
           	URL rurl = new URL(url);
        	URLConnection conn=rurl.openConnection();
        	HttpURLConnection httpconn=(HttpURLConnection) conn;
        	
//        	httpconn.setDoInput(true);
        	httpconn.setDoOutput(true);
        	httpconn.setDoInput(true);
        	httpconn.setConnectTimeout(1000*5);
        	httpconn.setReadTimeout(1000*5);
        	httpconn.setUseCaches(false);
        	httpconn.setRequestProperty("Accept-Charset","utf-8");
        	//httpconn.setRequestProperty("contentType","utf-8");
        	httpconn.setRequestProperty("Content-Type","application/json;charset=utf-8");
        	httpconn.setRequestProperty("Content-Length", String.valueOf(body.length()));
            httpconn.setRequestProperty("accept", "*/*");
            httpconn.setRequestProperty("connection", "Keep-Alive");
            
            httpconn.connect();
        	
            // 设置是否向httpconn输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
        	
            outputstream = httpconn.getOutputStream();
            outputstreamwriter = new OutputStreamWriter(outputstream,"utf-8");
            outputstreamwriter.write(body);
            outputstreamwriter.flush();// 刷新
            
            if (httpconn.getResponseCode() >= 300){
            	 throw new Exception("HTTP Request is not success, Response code is " + httpconn.getResponseCode());
            }
            
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK){
            
            	inputstream = httpconn.getInputStream();
            	inputstreamreader= new InputStreamReader(inputstream,"utf-8");		//设置编码格式utf-8,修改时间：2018-1-31
            	reader = new BufferedReader(inputstreamreader);
            
            	String line;
            	while ((line = reader.readLine()) != null)     	
            		result.append(line);            
            	GZipUntils gz=new GZipUntils();
            	s=gz.Gzip2S(result.toString());
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
        if(s!=null)
        	obj=JSONObject.fromObject(s);
        return obj;
    }
    
	/*
	 * 发送post请求，带文件，且带其他参数
	 *
	 * @params filepath
	 * 				需要发送的文件路径
	 * @params body
	 * 				需要发送的参数，以map<String,String>传入，参数名称-参数值
	 * @return
	 * 		  返回服务器响应
	 * @2019-12-17
	 * 		创建该方法
	 * 
	 */

    @SuppressWarnings("resource")
	public JSONObject sendPost(String filepath,Map<String,String>body) throws Exception{
    	DataOutputStream outputstream=null;
        DataInputStream in = null;
        
        InputStream inputstream=null;
    	InputStreamReader inputstreamreader=null;
    	BufferedReader reader=null;
    	
    	JSONObject obj=new JSONObject();
    	StringBuffer result=new StringBuffer();
    	File file=new File(filepath);
    	
    	final String boudary=java.util.UUID.randomUUID().toString();
        final String newline="\r\n";
        final String prefix="--";
       	URL rurl;
       	
		try {
			rurl = new URL(url);
	    	URLConnection conn=rurl.openConnection();
	    	HttpURLConnection httpconn=(HttpURLConnection) conn;
            
	    	httpconn.setRequestMethod("POST");
	    	httpconn.setDoOutput(true);
        	httpconn.setDoInput(true);
        	httpconn.setConnectTimeout(1000*50);
        	httpconn.setReadTimeout(1000*50);
        	httpconn.setUseCaches(false);
        	conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boudary);
            httpconn.setRequestProperty("accept", "*/*");
            httpconn.setRequestProperty("connection", "Keep-Alive");
        	
            httpconn.connect();
            
            outputstream=new DataOutputStream(httpconn.getOutputStream());
            
            StringBuilder f = new StringBuilder();
            f.append(prefix);
            f.append(boudary);
            f.append(newline);
            f.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"" + newline);
            f.append("Content-Type:application/octet-stream");
            f.append(newline);
            f.append(newline);
            outputstream.write(f.toString().getBytes());
            
            in = new DataInputStream(new FileInputStream(file));
            byte[] buf= new byte[1024];
            int bytes = 0;
            while ((bytes = in.read(buf)) != -1) {
            	outputstream.write(buf, 0, bytes);
            }
            outputstream.write(newline.getBytes());
            
            StringBuilder q= new StringBuilder();
            int k = 1;
            for (Map.Entry<String,String> e:body.entrySet()) {
                if (k != 1) {
                    q.append(newline);
                }
                q.append(prefix);
                q.append(boudary);
                q.append(newline);
                q.append("Content-Disposition: form-data;name=" + e.getKey() + "");
                q.append(newline);
                q.append(newline);
                q.append(e.getValue());
                outputstream.write(q.toString().getBytes());
                q.delete(0, q.length());
                k++;
            }
            
            byte[] end_data = ("\r\n--" + boudary + "--\r\n").getBytes();
            outputstream.write(end_data);
            outputstream.flush();
            
            inputstream = httpconn.getInputStream();
        	inputstreamreader= new InputStreamReader(inputstream,"utf-8");
        	reader = new BufferedReader(inputstreamreader);
        	
        	String line;
        	while ((line = reader.readLine()) != null)     	
        		result.append(line);            
//        	GZipUntils gz=new GZipUntils();
//        	s=gz.Gzip2S(result.toString());
            
		} finally{
            try{ 
            	if(outputstream!=null)
            		outputstream.close();      
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
      if(result!=null)
    	  obj=JSONObject.fromObject(result.toString());
    	return obj;
    }
    
    public Boolean Connect() throws IOException{
		
    	Boolean f=null;
    	URL rurl = new URL(url);
    	URLConnection conn=rurl.openConnection();
    	HttpURLConnection httpconn=(HttpURLConnection) conn;
    	
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
    
    public String Map2String(Map<String,String> param){
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

    public JSONObject Map2Json(Map<String,String>m){
    	return JSONObject.fromObject(m);
    }
    
    public static void main(String[] args) throws Exception{
    	
    	IOHttp http=new IOHttp("http://pro.bdguanai.com:9998/file/import/device");
    	
    	JSONObject response;
    	
//    	String username="13466528371";
//    	
//    	StringBuffer sb=new StringBuffer();
//    	
//    	sb.append("{\"query\":\"mutation{\\n  login(loginInput:{\\n    grantType:\\\"password\\\"\\n    username:\\");
//    	sb.append("\""+username+"\\\"");
//    	sb.append("\\n    password:\\\"528371\\\"\\n    client:\\\"client\\\"\\n    secret:\\\"secret\\\"\\n    refreshToken:\\\"\\\"\\n    \\n  }){\\n    platform\\n    tokenInfo{\\n      accessToken\\n      refreshToken\\n      expiresIn\\n      scope\\n      tokenType\\n    }\\n    userErrors{\\n      field\\n      message\\n    } \\n  }\\n}\"}");
//    	
//    	//服务商移动端登录接口
//    	String body="{\"query\":\"mutation{\\n  login(loginInput:{\\n    grantType:\\\"password\\\"\\n    username:\\\"13466528371\\\"\\n    password:\\\"528371\\\"\\n    client:\\\"client\\\"\\n    secret:\\\"secret\\\"\\n    refreshToken:\\\"\\\"\\n    \\n  }){\\n    platform\\n    tokenInfo{\\n      accessToken\\n      refreshToken\\n      expiresIn\\n      scope\\n      tokenType\\n    }\\n    userErrors{\\n      field\\n      message\\n    } \\n  }\\n}\"}";
//    	
//    	//运营中心登录接口
//    	String body1="{\"query\":\"mutation{login(loginInput:{\\n  grantType:\\\"password\\\"\\n  secret:\\\"webapp\\\"\\n  client:\\\"webapp\\\"\\n  username:\\\"wy_yunying\\\"\\n  password:\\\"123456\\\"\\n   \\n}){\\n  platform\\n  tokenInfo{\\n    accessToken\\n    tokenType\\n    refreshToken\\n    expiresIn\\n    scope\\n    jti\\n  }\\n  userErrors{\\n    field\\n    message\\n  }\\n}\\n  \\n}\"}";
//    	//创建驿站接口
//    	String body2="{\"query\":\"mutation{org8nCreate(org8nInput:{\\n  \\n  id:\\\"\\\"\\n  parentId:\\\"5b0f724f7d560d00012d9f1d\\\"\\n  type:STATION\\n  name:\\\"王岩的接口测试驿站\\\"\\n  theme:\\\"default\\\"\\n  tel:\\\"13466528372\\\"\\n  linkMan:\\\"王岩\\\"\\n  phone:\\\"13466528372\\\"\\n  status:1\\n  serviceRanges:[\\n    {\\n      province:\\\"北京市\\\"\\n      provinceCode:\\\"11\\\"\\n      city:\\\"市辖区\\\"\\n      cityCode:\\\"110100000000\\\"\\n      country:\\\"东城区\\\"\\n      countryCode:\\\"110101000000\\\"\\n      town:\\\"东华门街道办事处\\\"\\n      townCode:\\\"110101001000\\\"\\n      village:\\\"多福巷社区居委会\\\"\\n      villageCode:\\\"110101001001\\\"\\n    }\\n  ]\\n  address:{\\n    address1:\\\"北京市丰台区新村街道中驰商务酒店\\\"\\n    address2:\\\"1\\\"\\n    latitude:39.8352\\n    longitude:116.3262\\n  }\\n  opType:SELF\\n  hotPhone:\\\"\\\"\\n  email:\\\"\\\"\\n  memo:\\\"\\\"\\n}){\\n  org8n{\\n    id\\n    name \\n}\\n  userErrors{\\n    field\\n    message\\n  }\\n  \\n}\\n}\"}";
//    	//创建驿站管理员接口
//    	String body3="{\"query\":\"mutation{employeeCreate(employeeInput:{\\n  \\n  platform:\\\"STATION\\\"\\n  org8nId:\\\"5b2879c7cff47e0001f752aa\\\"\\n  name:\\\"王岩\\\"\\n  idNo:\\\"130204198511140614\\\"\\n  phone:\\\"13466528387\\\"\\n  group:\\\"\\\"\\n  address:{\\n    address1:\\\"\\\"\\n    address2:\\\"\\\"\\n  }\\n  role:\\\"王岩的角色\\\"\\n  rights:[]\\n  memo:\\\"\\\"\\n  login:\\\"wy_yizhan_201806193\\\"\\n  pwd:\\\"111111\\\"\\n  usertype:ADMIN\\n  visuallogin:\\\"\\\"\\n  visualpwd:\\\"\\\"\\n}){\\n  employee{\\n    id\\n    name\\n    idNo\\n    phone\\n    role\\n}\\n  userErrors{\\n    field\\n    message\\n  }\\n  \\n}\\n}\"}";
//    	
//    	System.out.println(body3);
    	//System.out.println(sb.toString());
    	//response=http.sendPost(sb.toString());
    	//response=http.sendPost(body1);
    	

    	
    	Map<String,String> body=new HashMap<String,String>();
    	body.put("entId", "101");
    	response=http.sendPost("C:\\Users\\BDHT\\Desktop\\device20191211.xlsx", body);
    	
    	System.out.println(response);
    	
    }
}
