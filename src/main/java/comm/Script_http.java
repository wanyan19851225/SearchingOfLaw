package comm;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import comm.IOHttp;
import net.sf.json.JSONObject;

public class Script_http implements Runnable{
	
	private String phone,imei,sn,url;

    
    public Script_http(String[] data,String url){
    	this.phone=data[3];
    	this.imei=data[2];
    	this.sn=data[1];
    	this.url=url;
    }
	
	
	public void run() {
		String user_token;
		JSONObject cmd=new JSONObject();
		JSONObject data=new JSONObject();
		JSONObject response=new JSONObject();
		Map<String,String> send=new HashMap<String,String>();
       	Date date=new Date(System.currentTimeMillis());		
    	DateFormat dformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
        /**********************用户登录***************************/
	   	cmd.put("command","10102");
        data.put("user_name",phone);
        data.put("user_password","34c0200ecd6bfc8505b54ceeadf448b8");
        data.put("device_token","as1d5f49sd841we51f919as48dfasd");
        data.put("app_id","1");
        data.put("user_imei",imei);
        data.put("user_imsi","22222");
        data.put("user_phone",phone);
        data.put("phone_version","andriod5.0");
        data.put("phone_model","HTC_9Mu");
        data.put("app_version","1.0.0");
        send.put("cmd",cmd.toString());
        send.put("token","");		
        send.put("data",data.toString());
        long start_t=System.currentTimeMillis();
        IOHttp iohttp=null;
        try {
        	iohttp=new IOHttp(url);
			response=iohttp.sendPost(null);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        long total_t=System.currentTimeMillis()-start_t;  
        if(response.getString("result").equals("1")){
        	System.out.println("["+Thread.currentThread().getName()+":"
        			+phone+"]"
					+dformat.format(date)
					+" 用户登录：ok!"
					+ " 响应时间："+total_t);
        	user_token=response.getString("user_token");
        }
        else{
        	user_token=null;
        	System.out.println(response.toString()); 
        }
        send.clear();
        cmd.clear();
        data.clear();
        
        try {
			Thread.sleep(1000*30);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        /**********************远程关机***************************/
        if(user_token!=null){
        	cmd.put("command","10119");
           	data.put("user_phone",phone);
        	data.put("device_imei",imei);
        	send.put("cmd",cmd.toString());
        	send.put("token",user_token);		
        	send.put("data",data.toString()); 
    		start_t=System.currentTimeMillis();
    		try {
				response=iohttp.sendPost(null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		total_t=System.currentTimeMillis()-start_t;
    	    if(response.getString("result").equals("1")){
    	        	System.out.println("["+Thread.currentThread().getName()+":"
    	        			+phone+"]"
    						+dformat.format(date)
    						+" App端已发送 远程关机：ok!"
    						+ " 响应时间："+total_t);
    	        }
    	        else
    	        	System.out.println(response.toString()); 
    	        send.clear();
    	        cmd.clear();
    	        data.clear();
        }
        
         try {
			Thread.sleep(1000*30);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         
         /**********************定位***************************/
         
         if(user_token!=null){
         	cmd.put("command","10204");
         	data.put("device_imei",imei);
         	send.put("cmd",cmd.toString());
         	send.put("token",user_token);		
         	send.put("data",data.toString()); 
     		start_t=System.currentTimeMillis();
     		try {
				response=iohttp.sendPost(null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     		total_t=System.currentTimeMillis()-start_t;
     	    if(response.getString("result").equals("1")){
     	        	System.out.println("["+Thread.currentThread().getName()+":"
     	        			+phone+"]"
     						+dformat.format(date)
     						+" App端已发送 定位：ok!"
     						+ " 响应时间："+total_t);
     	        }
     	        else
     	        	System.out.println(response.toString()); 
     	        send.clear();
     	        cmd.clear();
     	        data.clear();
         }
         
          try {
 			Thread.sleep(1000*30);
 		} catch (InterruptedException e1) {
 			// TODO Auto-generated catch block
 			e1.printStackTrace();
 		}
         
        /**********************当前位置查询***************************/
        if(user_token!=null){
        	int i=1;
        	long ave_t=0;
        	while(true){
            	cmd.put("command","10104");
               	data.put("device_imei",imei);
            	data.put("start_time","0");
            	data.put("end_time",dformat.format(date));	
            	send.put("cmd",cmd.toString());
            	send.put("token",user_token);		
            	send.put("data",data.toString()); 
        		start_t=System.currentTimeMillis();
        		try {
					response=iohttp.sendPost(null);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		total_t=System.currentTimeMillis()-start_t;
        		ave_t=((i-1)*ave_t+total_t)/i;
        		if(response.getString("result").equals("1"))
    				System.out.println("["+Thread.currentThread().getName()+":"
    						+phone+"]"
							+dformat.format(date)
							+" App端已发送第"+i+++"次 位置查询：ok!"
							+ " 响应时间："+total_t
							+" 平均响应时间："+ave_t);
        		else
        			System.out.println(response.toString());
            	cmd.clear();
        		data.clear();
            	send.clear();
            	try {
					Thread.sleep(1000*60*4);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
 
	}

}
