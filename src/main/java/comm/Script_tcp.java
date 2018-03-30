package comm;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Script_tcp implements Runnable{
	
	private String ip,imei,host;
	private String[] data=null;

	public Script_tcp(String[] data,String ip,String host){
    	this.data=data;
    	this.ip=ip;
    	this.host=host;
    	this.imei=data[2];
	}
	
	public void disconnect(Socket s) throws IOException{	
		s.close();
	}
		
	public void run() {
		
		ExecutorService threadpool = Executors.newCachedThreadPool();
		ConvertBytes convertbyte=new ConvertBytes();
	    Properties prop = new Properties();
		try {
		    prop.load(this.getClass().getClassLoader().getResourceAsStream("Sends.properties"));
			byte[] b1=convertbyte.str2Bcd(prop.getProperty("b1"));
			byte[] b3=convertbyte.str2Bcd(prop.getProperty("b3"));
			while(true){
				Script_tcp_send send=new Script_tcp_send();
				Object obj=send.send(ip,host,data,b1);
				Socket s=(Socket)obj;
				Script_tcp_recv recv=new Script_tcp_recv(s);
				recv.setName(Thread.currentThread().getName(),imei);
				Future<Integer> frecv=threadpool.submit(recv);
				while(true){	
					obj=send.send(s,null,data,b3);
					long sendtime=(Long)obj;
					if(sendtime!=0)
						recv.setSendTime(sendtime);
					else{
						this.disconnect(s);
						break;	
					}

					if(frecv.isDone()){
						if(frecv.get()==0){
							System.out.println("Spare Timeout in 5 Min!Socket Close...");
							this.disconnect(s);
							break;
							}					
					}					
					Thread.sleep(1000*60*4);
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
