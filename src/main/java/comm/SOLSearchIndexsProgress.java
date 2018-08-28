package comm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/** 
 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
 * All right reserved. 
 * @author: wanyan 
 * date: 2018-8-13 
 * 
 * SOLSearchIndexsProgress类实现当检索索引时，展示进度条功能
 */

public class SOLSearchIndexsProgress extends SwingWorker<Map<String,Long>,String>{

	private DisplayGui p;
	private String keywords;
	
	public SOLSearchIndexsProgress(DisplayGui p,String keywords){
		this.p=p;
		this.keywords=keywords;
	}
			
	@Override
	protected Map<String, Long> doInBackground() throws Exception {
		// TODO Auto-generated method stub
		Map<String,Long> r=new HashMap<String,Long>();
		Map<String,List<String[]>> content=new HashMap<String,List<String[]>>();
		HandleLucene handle=new HandleLucene();
		DisplayGui.star.SetDoneLabelVisabel(false);
		DisplayGui.star.SetProgressBarVisabel(true);
		DisplayGui.star.SetProgressBarLabelVisabel(true);
		DisplayGui.star.setProgeressBarIndeterminate(true);		//设置为不确定进度模式的进度条
		
		if(p.GetRage())
			DisplayGui.range.clear();
		if(!this.keywords.isEmpty()){
			Boolean f=p.GetIsRemote();
			long start=System.currentTimeMillis();
			if(DisplayGui.range.isEmpty()){
				if(f)
					content=p.QueryRemoteSegments(Path.urlpath,this.keywords);
				else
					content=handle.QuerySegments(Path.indexpath,this.keywords);
			}
			else{					
//				多条件查询，指定在某个法条文档中查询	
				if(f){
					System.out.println("keywords:"+this.keywords+","+"address:");
				}
				else{
					String[] fields=new String[]{"file","law"};
					content=handle.QuerySegments(Path.indexpath,fields,DisplayGui.range,this.keywords);
				}
			}
					
			long end=System.currentTimeMillis();
			long total=p.solresult.UpdateText(content);
			r.put(String.valueOf(end-start),total);
		}
		return r;
	}
	
	
	protected void process(List<String> chunks) {
		DisplayGui.star.setProgeressBarLabelText("正在检索...");
	}
	
	protected void done() {
		DisplayGui.star.SetDoneLabelVisabel(true);
		DisplayGui.star.SetProgressBarVisabel(false);
		DisplayGui.star.SetProgressBarLabelVisabel(false);
		
		try {
			Map<String,Long> r= get();
			if(r==null)
				JOptionPane.showMessageDialog(null, "关键词不允许为空", "警告", JOptionPane.ERROR_MESSAGE);
			else{
				for(Map.Entry<String,Long> e:r.entrySet())
					p.SetStatusText(e.getKey(),e.getValue());	
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
