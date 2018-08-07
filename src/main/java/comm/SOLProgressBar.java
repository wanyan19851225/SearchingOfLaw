package comm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;


/** 
 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
 * All right reserved. 
 * @author: wanyan 
 * date: 2018-8-7 
 * 
 * SOLProgressBar类实现进度条功能
 */

public class SOLProgressBar extends SwingWorker<Map<String,Boolean>,String>{

	private JProgressBar p;
	private JLabel l;
	private List<String> fname;

	public SOLProgressBar(JProgressBar p,JLabel l,List<String> fname){
		this.p=p;
		this.l=l;
		this.fname=fname;
	}
	
	
	@Override
	protected Map<String,Boolean> doInBackground() throws Exception {
		// TODO Auto-generated method stub
		int size=this.fname.size();
		Map<String,Boolean> r=new HashMap<String,Boolean>();
		for(int i=0; i<size; i++){
			r.put(fname.get(i),true);
			publish("正在创建索引的文件: "+fname.get(i)+"."+"文件总数:"+size+"."+"剩余文件数:"+(size-(i+1))+"."+"正在创建第:"+(i+1)+"个文件");   
		}
		return r;
	}
	
	@Override  
	protected void process(List<String> chunks) {  
	        l.setText(chunks.get(chunks.size()-1)); 
	        int x=Integer.parseInt(chunks.get(chunks.size()-1).substring(chunks.get(chunks.size()-1).indexOf("第")+2,chunks.get(chunks.size()-1).indexOf("个")).trim()); 
	        p.setValue(x);  
	          
//	        for(String str : chunks){  
//	            System.out.println(str);  
//	        }  
	    }  
	
	@Override
	protected void done() {
	
			try {
				Map<String,Boolean> r= get();
				
				for(Map.Entry<String,Boolean> e:r.entrySet()){
					if(!e.getValue())
						JOptionPane.showMessageDialog(null, e.getKey()+"创建索引成功", "警告", JOptionPane.ERROR_MESSAGE);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	}

	

}
