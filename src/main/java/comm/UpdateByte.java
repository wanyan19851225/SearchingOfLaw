package comm;

import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Timer;

public class UpdateByte {
	
	public void Add(String imei,byte[] byteArray,int offset){
		
		ConvertBytes convertbyte=new ConvertBytes();
		
		byte[] b=convertbyte.str2Bcd(imei);
		
		for(int i=0;i<b.length;i++){
		
		byteArray[i+offset]=b[i];
		
		}
		
	}
	
	public void AddSn(String sn,byte[] byteArray,int offset) throws UnsupportedEncodingException{
		
		ConvertBytes convertbyte=new ConvertBytes();
		
		byte[] b=convertbyte.str2Asc(sn);
		
		for(int i=0;i<b.length;i++){
			
			byteArray[i+offset]=b[i];
		}
	}
	
	public void AddUUId(byte[] byteArray,int offset){
		ConvertBytes convertbyte=new ConvertBytes();
		byte[] b=convertbyte.uuid2byte();
		for(int i=0;i<b.length;i++){
			byteArray[i+offset]=b[i];
		}
	}
	
	public byte getbyte(byte[] byteArray,int offset){
		
		return byteArray[offset];
	}
	
	public void cal_check(byte[] byteArray){
		
		byteArray[byteArray.length-3]=0x00;
		
		for(int i=0;i<byteArray.length;i++){
			
			if(i!=byteArray.length-3){
				
				byteArray[byteArray.length-3]=(byte)(byteArray[byteArray.length-3]+byteArray[i]);
			}
		}
	}
	
	public void show_ResultData(String mark,byte[] byteArray){
		
		String show=mark;
		
		String hs="";
		
		for(int i=0;i<byteArray.length;i++){
			
			hs=Integer.toHexString(byteArray[i]&0xFF);
			
			if(hs.length()<2)
			
				show+="0"+hs+" ";
			
			else
				show+=hs+" ";
			
		}
		
			System.out.println(show);
		
}
	
	public void cal_lenthg(byte[] byteArray,int offset){
		
		ConvertBytes convertbyte=new ConvertBytes();
		
		byte[] b=convertbyte.int2byte(byteArray.length,2);
		
		byteArray[offset++]=b[0];
		
		byteArray[offset]=b[1];
		
	}
	
	public void cal_filetime(byte[] byteArray,int offset){
		
		ConvertBytes convertbyte=new ConvertBytes();
		
		byte[] b=convertbyte.localtime2byte();

	    byteArray[offset++]=b[0];
	    
	    byteArray[offset++]=b[1];
	    
	    byteArray[offset++]=b[2];
		
	    byteArray[offset]=b[3];
    
	}
	
	
	
	public byte[] cal_fileid(){
		
		ConvertBytes convertbyte=new ConvertBytes();
		
		return convertbyte.uuid2byte();

	}
	
	public void cal_fileid(byte[] Arraybyte,int offset){
		ConvertBytes convertbyte=new ConvertBytes();
		byte[] b=convertbyte.uuid2byte();
		
		for(int i=0;i<b.length;i++){
			
			Arraybyte[offset++]=b[i];
		}
	}
	
	public void cal_userid(long userid,byte[] byteArray,int offset){
		
		ConvertBytes convertbyte=new ConvertBytes();
				
		byte[] b=convertbyte.long2byte(userid);
		
		for(int i=0;i<b.length;i++){
			
			byteArray[i+offset]=b[i];
		}
	
	}
	
	public void cal_userid_asc(String userid,byte[] byteArray,int offset) throws UnsupportedEncodingException{
		ConvertBytes convertbyte=new ConvertBytes();
		byte[] b=convertbyte.str2Asc(userid);
		for(int i=0;i<b.length;i++){
			byteArray[i+offset]=b[i];
		}
	}
	
	public void copy_fileid(byte[] des,byte[] sor,int offset,int start,int end){
		
		for(int i=start,j=0;i<=end;i++,j++){
			des[offset+j]=sor[i];
		}
	}
	
	public double dsum(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }
	
	public void updatesend(String sn,String imei,byte[] b) throws UnsupportedEncodingException{
		switch (b[12]){
		
		case (byte)0x01:
			if(!sn.isEmpty()){
				this.AddSn(sn,b,55);
				this.Add(imei,b,4);
			}
			break;	
		case (byte)0x03:
			if(!imei.isEmpty())
				this.Add(imei,b,4);
			break;		
		}
		this.cal_lenthg(b,2);
		this.cal_check(b);		
	}
	public double cal_earth_point(double lat1, double lng1, double lat2, double lng2){
		
		 
		double EARTH_RADIUS = 6378.137;
		
		double radLat1=rad(lat1);
		 
		double radLat2=rad(lat2);	
		
		double a=radLat1-radLat2;
		
		double b=rad(lng1)-rad(lng2);
		   
		double s=2*Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		
		s=s*EARTH_RADIUS;
		   
		s=Math.round(s*10000)/10000.00;
		   
		return s;
			 
	}
/*	
	public void create_random_file(Map<String,ArrayList<Integer>> qu,int count,String filename,String filepath) throws IOException{
		
		int temp;
		
		String str="";
		
		Random random = new Random();
		
		ArrayList<String> name=new ArrayList<String>();
		
		ArrayList<Integer> number=new ArrayList<Integer>();
		
		ArrayList<Integer> range=new ArrayList<Integer>();
		
		File f=new File(filepath+filename);
		
		OutputStream out=new FileOutputStream(f,true);
		
		for(String sr:qu.keySet()){
			
//			System.out.println(sr);
			
			name.add(sr);
		}
			
		for(int n=0;n<count;n++){
			
			for(ArrayList<Integer> ar:qu.values()){
				
				for(int i=0;i<ar.size();i=i+3){
				
					number.add(ar.get(i));
					
//					System.out.println("number"+ar.get(i));
				
					range.add(ar.get(i+1));
				
					range.add(ar.get(i+2));
					
//					System.out.println("max"+ar.get(i+1));
					
//					System.out.println("min"+ar.get(i+2));
					
					for (int m=0;m<ar.get(i);m++){
						
						temp=random.nextInt(ar.get(i+1))%(ar.get(i+1)-ar.get(i+2)+1)+ar.get(i+2);
						
//						System.out.println(temp);
						
						str+=Integer.toString(temp);
						
					}
					
				}
				
				out.write(str.getBytes());
				
				str="";
				
				out.write(",".getBytes());
				
			}
			
			out.write('\r');
			
			out.write('\n');
			
		}
							
		out.close();
	
	}
*/		
	private static double rad(double d){
		
	   return d * Math.PI / 180.0;
	   
	}
	
	public String JM(String inStr) {   
		  char[] a = inStr.toCharArray();   
		  for (int i = 0; i < a.length; i++) {   
		   a[i] = (char) (a[i] ^ 't');   
		  }   
		  String k = new String(a);   
		  return k;   
		 } 
	
}
