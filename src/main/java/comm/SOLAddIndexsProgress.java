package comm;

import java.io.File;
import java.util.ArrayList;
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
 * date: 2018-8-7 
 * 
 * SOLProgressBar类实现进度条功能
 */

public class SOLAddIndexsProgress extends SwingWorker<Map<String,Integer>,String>{

	private String url;
	private File[] f;
	private SOLAddIndex jf;

	public SOLAddIndexsProgress(SOLAddIndex jf){
		this.jf=jf;
		this.f=this.GetFile();
	}
	
	public File[] GetFile(){
		url=jf.GetFilePath();
		File[] files = null;
		File file=new File(url);
		if(file.isDirectory())		//url如果为文件夹则返回文件夹下的文件列表
			files = new File(url).listFiles();	
		return files;		//url如果为html网址或者单个文件，则返回null	
	}
	
	public int GetFileNum(){
		int size=0;
		if(f!=null)		//url为文件夹
			size=f.length;
		else		//url为html网址或者单个文件
			size=1;
		return size;
	}
	
	
	@Override
	protected Map<String,Integer> doInBackground() throws Exception {
		// TODO Auto-generated method stub
		Map<String,Integer> r=new HashMap<String,Integer>();
		HandleLucene handle=new HandleLucene();;
		jf.solstar.SetDoneLabelVisabel(false);
		jf.solstar.SetProgressBarVisabel(true);
		jf.solstar.SetProgressBarLabelVisabel(true);
		Long start=System.currentTimeMillis();
		if(f!=null){		//判断是否是文件夹，如果是，则走此分支
			int size=f.length;		//获取文件夹下文件总数
			if(size==0)			//判断文件夹下是否有文件，如果没有文件，则走此分支，返回-1
				r.put(url,-1);
			else{
				for(int i=0; i<size; i++){		//为文件夹下的文件添加索引
					Integer t=handle.AddIndexs(f[i].getPath(), Path.indexpath);
					r.put(f[i].getPath(),t);		//将创建索引结果返回，-1：文件内容为空，没有读取到段落。由于在GetFile方法中已经对传入的url进行了判断，只要走到此分支，必然不会出现返回-2和-3的情况
					String dd="("+(i+1)+"/"+size+")";
					publish(dd);   
				}
			}
		}
		else{		//如果url不是文件夹,即或者是html的网址，或者是单个文件，或者url格式有误，则走此分支
			Integer t=handle.AddIndexs(url, Path.indexpath);
			r.put(url,t);		//将创建索引结果返回，-1：文件内容为空，没有读取到段落；-2：网站地址无效或者无法访问；-3：输入路径url格式有误
//			publish("("+"1"+"/"+"1"+")");		//html网址，或者单个文件，或者url地址有误时，传给pross的参数为空，不在显示动态进度
		}
		Long end=System.currentTimeMillis();
		Long t=end-start;
		r.put("__time__",t.intValue());
		return r;
	}
	
	@Override  
	protected void process(List<String> chunks) {
		if(!chunks.isEmpty()){		
	        jf.solstar.setProgeressBarLabelText(chunks.get(chunks.size()-1)); 
	        int x=Integer.parseInt(chunks.get(chunks.size()-1).substring(chunks.get(chunks.size()-1).indexOf("(")+1,chunks.get(chunks.size()-1).indexOf("/")).trim()); 
	        jf.solstar.setProgressBarValue(x);  
		}
		else{		//参数为空时，为html网址，或者单个文件，或者url地址有误，不显示动态进度
			jf.solstar.setProgeressBarLabelText("正在创建索引...");
			jf.solstar.setProgressBarValue(1);
		}
	}  
	
	@Override
	protected void done() {
		
		jf.solstar.SetDoneLabelVisabel(true);
		jf.solstar.SetProgressBarVisabel(false);
		jf.solstar.SetProgressBarLabelVisabel(false);
			
		List<String> BlankIndexFiles=new ArrayList<String>();		//存储返回值-1的文件，即空文件夹或者内容为空的文件名称
		List<String> UnArrveHtmlFiles=new ArrayList<String>();		//存储返回值-2的html名称，即html的url地址无效，或无法访问
		List<String> UnFormatFiles=new ArrayList<String>();		//存储url格式错误的路径名称
		List<String> SuccesIndexFiles=new ArrayList<String>();		//存储成功建立索引的文件名称
		try {
				
			Map<String,Integer> r= get();	
			int n=0;
			for(Map.Entry<String,Integer> e:r.entrySet()){
				if(e.getKey()!="__time__"){
					if(e.getValue()==-1)
						BlankIndexFiles.add(e.getKey());
					else if(e.getValue()==-2)		//返回-2和-3时，必然会只有一个元素
						UnArrveHtmlFiles.add(e.getKey());							
					else if(e.getValue()==-3)		//返回-2和-3时，必然会只有一个元素
						UnFormatFiles.add(e.getKey());
					else{
							n+=e.getValue();
							SuccesIndexFiles.add(e.getKey());
						}			
					}
			}
			if(!BlankIndexFiles.isEmpty()){
				StringBuffer s=new StringBuffer();
				int nf=BlankIndexFiles.size();
				for(int i=0;i<nf;i++)
					s.append(BlankIndexFiles.get(i)+"\r\n");
				JOptionPane.showMessageDialog(null, s.toString()+"文档或网站中没有检索到内容，或文件夹下未找到文档", "警告", JOptionPane.ERROR_MESSAGE);
			}
			if(!UnArrveHtmlFiles.isEmpty())
				JOptionPane.showMessageDialog(null, "请输入有效网址，或确认网站是否可以正常访问", "警告", JOptionPane.ERROR_MESSAGE);
			if(!UnFormatFiles.isEmpty())
				JOptionPane.showMessageDialog(null, "请输入有效格式的路径", "警告", JOptionPane.ERROR_MESSAGE);
			if(!SuccesIndexFiles.isEmpty()){
				DisplayGui.defselect.clear();
				jf.solstar.setStatusText("添加检索完毕!"+"耗时："+r.get("__time__")+"ms "+"创建索引条数："+n);
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
