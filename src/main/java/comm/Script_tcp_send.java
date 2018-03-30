package comm;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class Script_tcp_send{
		
	public Object send(Object obj,String host,String[] data,byte[]b) throws IOException, InterruptedException{
	   	String imei=data[2];
    	String sn=data[1];
		switch(b[12]){
		case (byte)0x01:
			String address=(String)obj;
			if(address!=null){
				byte[] recv=new byte[45];
			   	String[] buf=address.split(":");
			   	String ip=buf[0];
		    	int port=Integer.parseInt(buf[1]);
		    	InetAddress localhost=InetAddress.getByName(host);
		    	Random random=new Random();
				UpdateByte dd=new UpdateByte();
				ConvertBytes convertbyte=new ConvertBytes();
				dd.updatesend(sn,imei,b);
				Socket s=null;
				while(true){
				try {
			    	int localport=random.nextInt(6500)%(6500-1+1)+1;
					s = new Socket(ip,port,localhost,localport);
					break;
				} catch (IOException e) {
//					e.printStackTrace();
					if(e.getClass().getSimpleName().equals("BindException"))
						continue;
				}
				
				}
				DataOutputStream dos=new DataOutputStream(s.getOutputStream()); 
				DataInputStream dis=new DataInputStream(s.getInputStream());
				dos.write(b);
				dos.flush();
				dis.read(recv);
				byte[] pbyte=new byte[]{recv[10],recv[11]};
				port=convertbyte.byte2int(pbyte);	
				byte[] ibyte=new byte[]{recv[6],recv[7],recv[8],recv[9]};	
				ip=convertbyte.byte2StrDec(ibyte,".");
				
				s.close();
				dos.close();
				dis.close();
				
				while(true){
				try {
			    	int localport=random.nextInt(6500)%(6500-1+1)+1;
					s = new Socket(ip,port,localhost,localport);
					return s;
				} catch (IOException e) {
//					e.printStackTrace();
					if(e.getClass().getSimpleName().equals("BindException"))
						continue;
				}
				}
			}
			break;	
		case (byte)0x03:
			DataOutputStream dos=null;
			try {
				Socket s=(Socket)obj;
				dos=new DataOutputStream(s.getOutputStream());
				UpdateByte dd=new UpdateByte();
				dd.updatesend(null,imei,b);
				dos.write(b);
				dos.flush();
			} catch (IOException e) {
				dos.close();
				return 0;
			}
				return System.currentTimeMillis();
				
		default:
			Socket s1=(Socket)obj;
			DataOutputStream dos1=new DataOutputStream(s1.getOutputStream());
			try {
				dos1.write(b);
				dos1.flush();
			} catch (IOException e) {
				dos1.close();
				return 0;
			}
				return System.currentTimeMillis();
		}
		return null;
	}
}
