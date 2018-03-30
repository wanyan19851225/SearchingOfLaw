package comm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

	/** 
	 * Copyright @ 2016 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2016-6-23 
	 */

	/** 
	 * ConvertBytes��Ҫ����Byte�͸�����������֮���ת�� 
	 * �Ѵﵽ�ܹ�ʹ��Socket�ֽ����������� 
	 */

public class ConvertBytes {
	
	/*
	 * ָ��String��ת��ΪBCD����ʽ���ֽ�����
	 *
	 * @params ascii
	 * 				��ת�����ַ���
	 * @return
	 * 		  ����ת������ֽ�����
	 * 
	 */
	
	public byte[] str2Bcd(String ascii){	
		String asc=ascii.replace(" ","");		
		int len=asc.length();		
		int mod=len%2;		
		if (mod!= 0) {			
			asc = "0" + asc;			
			len = asc.length();			
		}		
		byte abt[] = new byte[len];		
		if (len >= 2) {			
			len = len / 2;			
		}		
		byte bbt[] = new byte[len];		
		abt = asc.getBytes();		
		int j, k;		
		for (int p = 0; p < asc.length() / 2; p++) {			
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {				
				j = abt[2 * p] - '0';				
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {				
				j = abt[2 * p] - 'a' + 0x0a;				
			} else {				
				j = abt[2 * p] - 'A' + 0x0a;				
			}			
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {				
				k = abt[2 * p + 1] - '0';				
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {	
				k = abt[2 * p + 1] - 'a' + 0x0a;				
			} else {				
				k = abt[2 * p + 1] - 'A' + 0x0a;				
			}			
			int a = (j << 4) + k;			
			byte b = (byte) a;			
			bbt[p] = b;			
		}		
		return bbt;	
	}
	
	/*
	 * ָ��String��ת��ΪASC����ʽ���ֽ�����
	 * 
	 * @params sr
	 * 				��ת�����ַ���
	 * @return
	 * 			����ת������ֽ�����
	 */
		
	public byte[] str2Asc(String sr) throws UnsupportedEncodingException{		
		byte[] b=sr.getBytes("UTF-8");				
		return b;
	}
	
	/*
	 * ָ��long��ת��Ϊ�ֽ�����
	 * 
	 * @params data
	 * 				��ת���ĳ�����
	 * @return
	 * 			����ת������ֽ�����
	 */
	
	public byte[] long2byte(long data){		
		ByteBuffer buffer=ByteBuffer.allocate(8);	
		buffer.putLong(data);
		byte[] b=buffer.array();		
		return b;		
	}
	
	/*
	 * ָ��int��ת��Ϊ�ֽ�����
	 * 
	 * @params dec
	 * 				��ת��������
	 * @params length
	 * 					ת�����ֽ����鳤�ȣ�Ӧ������2����4
	 * @return
	 * 			����ת������ֽ�����
	 */
	
	public byte[] int2byte(int dec,int length){
		byte[] b=new byte[length];
		if(length==2){
			for(int i=0;i<length;i++)
				b[i]=(byte)((dec>>(8-i*8))&0xFF);
		}
		if(length==4){
			for(int i=0;i<length;i++){
				b[i]=(byte)((dec>>(24-i*8))&0xFF);
			}
		}
		return b;		
	}
	
	public byte[] utc2byte(){
		byte[] b=new byte[4];
		final java.util.Calendar cal = java.util.Calendar.getInstance();		
	    //ȡ��ʱ��ƫ������    		
	    final int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);	    
	    //3��ȡ������ʱ�    	    
	    final int dstOffset = cal.get(java.util.Calendar.DST_OFFSET); 	    
	    //4���ӱ���ʱ����۳���Щ������������ȡ��UTCʱ�䣺    	    
	    cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
	    b[0]=(byte)((cal.getTimeInMillis()/1000)>>24);	    
	    b[1]=(byte)((cal.getTimeInMillis()/1000)>>16);	    
	    b[2]=(byte)((cal.getTimeInMillis()/1000)>>8);		
	    b[3]=(byte)(cal.getTimeInMillis()/1000);	    
	    return b;
	}
	
	public byte[] localtime2byte(){
		byte[] b=new byte[4];
		final java.util.Calendar cal = java.util.Calendar.getInstance();
	    b[0]=(byte)((cal.getTimeInMillis()/1000)>>24);	    
	    b[1]=(byte)((cal.getTimeInMillis()/1000)>>16);	    
	    b[2]=(byte)((cal.getTimeInMillis()/1000)>>8);		
	    b[3]=(byte)(cal.getTimeInMillis()/1000);	    
	    return b;
	}
	
	public byte[] uuid2byte(){		
		byte[] b=null;		
		String uid=UUID.randomUUID().toString().replace("-","");		
		try {			
			b=uid.getBytes("GBK");			
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();			
		}
		return b;
	}
	
	public byte[] image2byte(String path){
		  byte[] data=null;
		  FileInputStream input = null;
		  try {
			  input=new FileInputStream(new File(path));
			  ByteArrayOutputStream output=new ByteArrayOutputStream();
			  byte[] buf=new byte[1024];
			  int numBytesRead=0;
			  while ((numBytesRead=input.read(buf))!=-1){
				  output.write(buf,0,numBytesRead);
			  }
			  data=output.toByteArray();      
			  output.close();      
			  input.close();      
		    }catch (FileNotFoundException ex1) {
		      ex1.printStackTrace(); 
		    }catch (IOException ex1) {
		      ex1.printStackTrace();
		    }
		    return data;    
	}
	
	 public byte[] byte2bit(byte b) {  
	        byte[] array = new byte[8];  
	        for (int i = 7; i >= 0; i--) {  
	            array[i] = (byte)(b&1);  
	            b=(byte)(b>>1);  
	        }  
	        return array;  
	    }
	 
	  public long bytes2long(byte[] bytes) {
		  ByteBuffer buffer=ByteBuffer.allocate(8);
	      buffer.put(bytes, 0, bytes.length);  
	      buffer.flip();//need flip   
	      return buffer.getLong();  
	   }  
	
	public void byte2image(byte[] data,String path){
		if(data.length<3||path.equals("")) return;
		try{
			FileOutputStream imageOutput=new FileOutputStream(new File(path));
			imageOutput.write(data, 0, data.length);
		    imageOutput.close();
		    System.out.println("Make Picture success,Please find image in " + path);
		    } catch(Exception ex) {
		      System.out.println("Exception: " + ex);
		      ex.printStackTrace();
		    }
	}
	
	public String byte2locatime(byte[] byteArray) {
		int time=(byteArray[0]<<24)&0xFF000000|(byteArray[1]<<16)&0xFF0000|(byteArray[2]<<8)&0xFF00|(byteArray[3])&0xFF;
		Calendar c= new GregorianCalendar();
		c.setTimeInMillis(time*1000L);
		SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return s.format(c.getTime());
	}

	public int byte2int(byte[] byteArray){		
		return Integer.parseInt(Integer.toString((byteArray[0]<<8|(byteArray[1])&0xFF),10));		
	}
	
	public int byte2int(byte b){
		return Integer.parseInt(Integer.toString(b&0xFF),10);
	}
	
	public String byte2StrDec(byte[] byteArray,String separator){
		StringBuffer sb = new StringBuffer();		
		for(int i=0;i<byteArray.length;i++){
			if(i==byteArray.length-1)
				sb.append(Integer.toString(byteArray[i]&0xFF));
			else
				sb.append(Integer.toString(byteArray[i]&0xFF)+separator);
		}		
		return sb.toString().toUpperCase();
	}
	
	public String byte2StrHex(byte[] byteArray,String separator){
		StringBuffer sb = new StringBuffer();
		String hs;
		for(int i=0;i<byteArray.length;i++){
			hs=Integer.toHexString(byteArray[i]&0xFF);
			
			if(hs.length()<2)
				sb.append("0"+hs+separator);
			else
				sb.append(hs+separator);
		}		
		return sb.toString().toUpperCase();	
	}
	
	public String Asc2Str(byte[] byteArray) throws UnsupportedEncodingException{
		
		String str=new String(byteArray,"UTF-8");
		
		return str;
	}
	
	public void printByteHex(String mark,byte[] byteArray){
		StringBuffer sb = new StringBuffer(mark);		
		String hs;		
		for(int i=0;i<byteArray.length;i++){
			hs=Integer.toHexString(byteArray[i]&0xFF);			
			if(hs.length()<2)
				sb.append("0"+hs+" ");		
			else
				sb.append(hs+" ");		
		}		
			System.out.println(sb.toString().toUpperCase());		
	}
	
	public void printByteHexFormat(String messages,String label,String type,String command,byte[] byteArray){
		
		Date date=new Date(System.currentTimeMillis());
		
		DateFormat date_format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		this.printByteHex(messages+":"+"["+label+"]"+"["+type+"]"+"["+command+"]"+date_format.format(date)+"\n",byteArray);		
	}	
}
