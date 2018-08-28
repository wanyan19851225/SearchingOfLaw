package comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;


/** 
 * Copyright @ 2016 Beijing Beidouht Co. Ltd. 
 * All right reserved. 
 * @author: wanyan 
 * date: 2016-6-24
 * 
 * @Modified Date:2017-11-20
 * 			����putDatas������ʹ��List<Strnig>�����ַ���
 * 		
 */

/** 
 * PutFileDatas�ཫ����д�뵽�ļ� 
 *  
 */

public class IOHistory {
	
	/*
	 * ������д�뵽�ļ���ʹ��׷�ӷ�ʽ
	 * 
	 * @params sb
	 * 				��Ҫд�������
	 * @params filename
	 * 				��д����ļ�
	 * 
	 * @params filename
	 * 				ָ���������д����ļ�
	 *
	 */
	
	public long putDatas(StringBuffer sb, String filename,boolean seek) throws InterruptedException, UnsupportedEncodingException, IOException{
		
		Boolean status=true;
		long start,end;
		
		File file=new File(filename);
		
		if(!file.getParentFile().exists()){    /*�ж�ָ����Ŀ¼�Ƿ���ڣ���������ڣ��򴴽�֮*/
			file.getParentFile().mkdirs();
		}
		
		start=System.currentTimeMillis();
		while(status){
			try{
				RandomAccessFile out= new RandomAccessFile(file,"rw");
				FileChannel fcout=out.getChannel();	
				FileLock flout=null;
				while(true){
					try {             
						flout = fcout.tryLock();              
						break;               
						} catch (Exception e) {            	
							System.out.println("�������߳����ڲ���:"+filename.substring(18,filename.length()-4)+"����ǰ�߳�����2000����");
							Thread.sleep(2000);               
							}           
					}
				if(seek){
					out.seek(out.length());		/*׷���ļ����Ա�����д���ļ�ĩβ*/
					}				
				out.write(sb.toString().getBytes("UTF-8"));		
				out.write('\r');		
				out.write('\n');		
				flout.release();		
				fcout.close();		
				out.close();
				status=false;
				}catch(FileNotFoundException e){
					System.out.println("Write "+filename.substring(18,filename.length()-4)+" Failed Cause by:"+e.getClass().getName());
					e.printStackTrace();
					continue;
					}
			}	
		end=System.currentTimeMillis();
		
		return end-start;
		}
	
	/*
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-21 
	 * 
	 * @params sb
	 * 				存入历史文件的关键词
	 * @params filename
	 * 				历史文件名称
	 * @return long
	 * 				返回存入历史文件耗时时间
	 * 
	 * @2017-11-24
	 * 			修复读取sb容器里的数据时，总是读取第一个数据的bug,修改创建变量n的位置
	 * 
	 * @2018-1-31
	 * 			修复当检索历史文件所在目录不存在时，报错的bug
	 *
	 */
	
	public long HistoryWriter(List<String> sb,String filename) throws IOException{	

		long start,end;
		
		String filepath=filename.substring(0,filename.lastIndexOf("\\")+1);
		File file=new File(filepath);
		
		if(!file.exists()){		//判断文件所在的目录是否存在，如果不存在则先创建该目录
			file.mkdir();
		}
		
		file=new File(filename);
		
		if(!file.exists()){   
			file.createNewFile();
		}
		
		start=System.currentTimeMillis();

//		Date date=new Date(System.currentTimeMillis());
//		DateFormat dformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String fdate=dformat.format(file.lastModified());
//		String ndate=dformat.format(date);

		List<String> content=this.HistoryReaderByList(filename);
		int cnum=content.size();		//现有容器有多少个数据
		int snum=sb.size();			//需要往现有数据中添加多少个数据
		int n=0;		//用来循环读取sb容器里的数据
//		long num=this.GetNumHistory(filename);		
		if(cnum+snum>20){
//			FileWriter fw = new FileWriter(file,false);
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename,false),"UTF-8"));
			int offset=cnum+snum-20;	//需要现有容量腾出多个位置
			int move=cnum-offset;		//需要移动多少次，才能腾出offset个位置
			for(int i=0,j=0;i<cnum;i++,j++){		//操作现有容器里的数据，腾出offset位置，同事将腾出来的位置用sb容器里的数据替换
				if(j<move){				//移动，将后面的往前移动
					content.set(i,content.get(i+offset));
//					System.out.println(content.get(i+offset));
				}
				else if(i>=move&&i<=cnum-1){		//移动完毕，将腾出来的位置用sb容器里的数据进行替换
					String tm=sb.get(n++);
					content.set(i,tm);
				}
				String tt=content.get(i)+"\r\n";
//				System.out.println(tt);
				fw.write(tt);	
			}
			for(int i=0;i<snum-offset;i++){			//往现有容器中添加新的数据，将sb容器里剩下的数据添加到content容器中剩余的位置上，snum-offset是sb容器中，把数据替换到腾出来的位置上后，剩下的数据数量，然后需要新添加现有容器中
				content.add(sb.get(offset));
				fw.write(content.get(content.size()-1)+"\r\n");
			}
			fw.flush();
			fw.close();
		}
		else{
//			FileWriter fw = new FileWriter(file,true);	
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename,true),"UTF-8"));
			for(int i=0;i<sb.size();i++)	
				fw.write(sb.get(i).toString()+"\r\n");
			fw.flush();
			fw.close();
		}
		end=System.currentTimeMillis();	
		return end-start;	
	}

	public Map<String,List<String>> HistoryReaderByDate(String filename) throws IOException{
		Map<String,List<String>> dhis=new LinkedMap<String,List<String>>();
		File file=new File(filename);          
        if(file.isFile() && file.exists()){ //判断文件是否存在              
        	InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");//考虑到编码格式           	
            BufferedReader bufferedReader = new BufferedReader(read);              
            String line=null;                           
            while((line=bufferedReader.readLine()) != null){               	            
            	String[] tmp=line.split(" ");
            	StringBuffer time=new StringBuffer(tmp[0]+" "+tmp[1]);
            	if(dhis.containsKey(time.toString())){
            		dhis.get(time.toString()).add(tmp[2]);
            	}else{
            		List<String> his=new ArrayList<String>();
            		his.add(tmp[2]);
            		dhis.put(time.toString(),his);
//            		System.out.println("dd");
            	}    	             
            }
            read.close();
        }
		return dhis;		
	}

	public List<String> HistoryReaderByList(String filename) throws IOException{
		List<String> his=new ArrayList<String>();
		File file=new File(filename);          
        if(file.isFile() && file.exists()){ //判断文件是否存在          
           	InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");//考虑到编码格式           	
            BufferedReader bufferedReader = new BufferedReader(read);              
            String line=null; 
            while((line=bufferedReader.readLine()) != null){ 
            	his.add(line);
            }  	
        }
        return his;
	}
	
	public Map<String,List<String>> HistoryReaderByTime(String filename) throws IOException, ParseException{
		
		Map<String,List<String>> his=this.HistoryReaderByDate(filename);
		Map<String,List<String>> tmhis=new LinkedMap<String,List<String>>();
		DateFormat dformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long oneday=1000*60*60*24;
		long twoday=2*oneday;
		long threeday=3*oneday;
		long oneweek=7*oneday;
		long onemoth=30*oneday;
		
		if(!his.isEmpty()){
		
			for(String key:his.keySet()){
			
				String[] tt=key.split("@");
				long dt=dformat.parse(tt[1]).getTime();
				long ct=System.currentTimeMillis();
				long dvt=ct-dt;

				if(dvt<=oneday){

					if(tmhis.containsKey("今天"))
						tmhis.get("今天").addAll(his.get(key));
					else{
						List<String> ll=new ArrayList<String>();
						ll.addAll(his.get(key));
						tmhis.put("今天",ll);
					}
				}
			else if(dvt>oneday&&dvt<=twoday){
					if(tmhis.containsKey("两天内"))
						tmhis.get("两天内").addAll(his.get(key));
					else{
						List<String> ll=new ArrayList<String>();
						ll.addAll(his.get(key));
						tmhis.put("两天内",ll);
					}
				}
				else if(dvt>twoday&&dvt<=threeday){
					if(tmhis.containsKey("三天内"))
						tmhis.get("三天内").addAll(his.get(key));
					else{
						List<String> ll=new ArrayList<String>();
						ll.addAll(his.get(key));
						tmhis.put("三天内",ll);
					}
				}
				else if(dvt>threeday&&dvt<=oneweek){
					if(tmhis.containsKey("一周内"))
						tmhis.get("一周内").addAll(his.get(key));
					else{
						List<String> ll=new ArrayList<String>();
						ll.addAll(his.get(key));
						tmhis.put("一周内",ll);
					}
				}
				else if(dvt>oneweek&&dvt<=onemoth){
					if(tmhis.containsKey("一月内"))
						tmhis.get("一月内").addAll(his.get(key));
					else{
						List<String> ll=new ArrayList<String>();
						ll.addAll(his.get(key));
						tmhis.put("一月内",ll);
					}
				}
				else if(dvt>onemoth){
					if(tmhis.containsKey("超过一个月"))
						tmhis.get("超过一个月").addAll(his.get(key));
					else{
						List<String> ll=new ArrayList<String>();
						ll.addAll(his.get(key));
						tmhis.put("超过一个月",ll);
					}
			
				}
		
			}
		}
		
		return tmhis;
		
	}
	
	public long GetNumHistory(String file) throws IOException{
		
		int num=0;
		Map<String,List<String>> dhis=this.HistoryReaderByDate(file);
		for(String key:dhis.keySet())
			num+=dhis.get(key).size();
		return num;	
	}


	public static void main(String[] args) throws UnknownHostException, IOException, ParseException { 
		 
		 IOHistory iohis=new IOHistory();
		 List<String> input=new ArrayList<String>();
		 for(int i=0;i<5;i++){
//			 input.add(String.valueOf(i));
			 input.add("第20批5个");
		 }
		 
		 iohis.HistoryWriter(input,"D:\\Lucene\\conf\\history.cf");
		 
		 List<String> his=iohis.HistoryReaderByList("D:\\Lucene\\conf\\history.cf");
		 for(int i=0;i<his.size();i++){
//			 System.out.println(his.get(i));
		 }
		 
//		 System.out.println(iohis.GetNumHistory("D:\\Lucene\\conf\\history.cf"));
		 
		 Map<String,List<String>> dhis=new HashMap<String,List<String>>();
		 dhis=iohis.HistoryReaderByDate("D:\\Lucene\\conf\\history.cf");
		 for(String key:dhis.keySet()){
			 System.out.println(key+dhis.get(key));
		 }
		 
		 Map<String,List<String>> tmhis=new HashMap<String,List<String>>();
		 tmhis=iohis.HistoryReaderByTime("D:\\Lucene\\conf\\history.cf");
		 for(String key:tmhis.keySet()){
			 System.out.println(key+tmhis.get(key));
		 }
	 }
}
