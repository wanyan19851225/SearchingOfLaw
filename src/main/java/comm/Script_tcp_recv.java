package comm;

import java.io.*;
import java.net.Socket;
import java.text.*;
import java.util.Date;
import java.util.concurrent.Callable;

public class Script_tcp_recv implements Callable<Integer>{
	
	private static long sendtime;
	private String name,imei;
	private Socket s;
	
	public Script_tcp_recv(Socket s){
		this.s=s;

	}
	
	public Script_tcp_recv(){
		
	}
	
	public void setSendTime(long time){
		this.sendtime=time;
	}
	
	public long getSendTime(){
		return this.sendtime;
	}
	
	public void setName(String name,String imei){
		this.name=name;
		this.imei=imei;
	}

	public Integer call() throws IOException {
		DataInputStream dis=null;
		try {
			s.setSoTimeout(1000*60*5);
			int i=1;
			long avetime=0;
			dis=new DataInputStream(s.getInputStream());
			while(true){
				byte[] recv=new byte[45];
				dis.read(recv);
				switch (recv[5]){
				case (byte)0x14:
					System.out.println("["+name+":"+imei+"]"+"手表端已接收到 远程关机 命令：ok!");
					break;
				case (byte)0x03:
					long time=System.currentTimeMillis()-this.getSendTime();
					avetime=((i-1)*avetime+time)/i;
					DateFormat dformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					System.out.println("["+name+":"+imei+"]"
										+dformat.format(new Date(this.getSendTime()))
										+"服务器端已接收第"+i+++"包 心跳 命令：ok!"
										+ " 响应时间："+time
										+" 平均响应时间："+avetime);
					break;
					
				case (byte)0x05:
					System.out.println("["+name+":"+imei+"]"+"手表端已接收到 定位 命令：ok!");
					break;	
				default:
					ConvertBytes c=new ConvertBytes();
					c.printByteHex("recv",recv);
					break;
				}
			}
		} catch (IOException e) {
			dis.close();
			return 0;
		}		
	}

}
