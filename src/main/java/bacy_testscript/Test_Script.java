package bacy_testscript;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comm.Script_http;
import comm.Script_tcp;


public class Test_Script {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		String datas=null;
		Test_Script test=new Test_Script();
		
		ExecutorService threadpool = Executors.newCachedThreadPool();

		InputStreamReader in=new InputStreamReader(new FileInputStream("E:\\Datas.CSV"));
		BufferedReader buf=new BufferedReader(in);
		
	    Properties conf = new Properties();
	    conf.load(test.getClass().getClassLoader().getResourceAsStream("Configure.properties"));
	    String host[]=conf.getProperty("Host").split(";");
		int j=0,threadcount=Integer.parseInt(conf.getProperty("ThreadCount"));
		int intime=Integer.parseInt(conf.getProperty("InTime"));
		for(int i=0;i<threadcount;i++){
			if((datas=buf.readLine())!=null){
				String[] data=datas.split(",");
//				Script_http http=new Script_http(data,conf.getProperty("URL"));
//				threadpool.execute(http);
				Script_tcp tcp=new Script_tcp(data,conf.getProperty("IP"),host[j++]);
				threadpool.execute(tcp);
				if(j==host.length)
					j=0;
			}
			
			Thread.sleep(1000*intime/threadcount);
		}
		System.out.println("***********************************************");
		System.out.println("*                "+threadcount+"              *");
		System.out.println("***********************************************");
		in.close();	
		threadpool.shutdown();	
	}
}
