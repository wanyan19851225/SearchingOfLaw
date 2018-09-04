package SOLAddIndexs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import Com.DisplayGui;
import Com.FileIndexs;
import Com.HandleLucene;
import Com.Path;
import Com.ResultsType;


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
		jf.setFrameEnable(false);
		jf.setTableVisabel(false);
		
		Long start=System.currentTimeMillis();
		if(f!=null){		//判断是否是文件夹，如果是，则走此分支
			int size=f.length;		//获取文件夹下文件总数
			if(size==0)			//判断文件夹下是否有文件，如果没有文件，则走此分支，返回-1
				r.put(url,ResultsType.Directory_NONE);
			else{
				FileIndexs findexs=new FileIndexs();
				List<String> files=findexs.GetAllFiles(Path.filepath);
				for(int i=0; i<size; i++){		//为文件夹下的文件添加索引
					if(!files.contains(f[i].getName())){
						Integer t=handle.AddIndexs(f[i].getPath(), Path.indexpath,Path.filepath);
						r.put(f[i].getPath(),t);		//将创建索引结果返回，-1：文件内容为空，没有读取到段落。由于在GetFile方法中已经对传入的url进行了判断，只要走到此分支，必然不会出现返回-2和-3的情况
					}
					else
						r.put(f[i].getPath(),ResultsType.DOC_Already_Exist);
					publish("("+(i+1)+"/"+size+")"+" "+f[i].getName());   
				}
			}
		}
		else{		//如果url不是文件夹,即或者是html的网址，或者是单个文件，或者url格式有误，则走此分支
			FileIndexs findexs=new FileIndexs();
			List<String> files=findexs.GetAllFiles(Path.filepath);
			File file=new File(url);
			if(!files.contains(file.getName())){
				Integer t=handle.AddIndexs(url, Path.indexpath,Path.filepath);
				r.put(url,t);		//将创建索引结果返回，-1：文件内容为空，没有读取到段落；-2：网站地址无效或者无法访问；-3：输入路径url格式有误
			}
			else
				r.put(url,ResultsType.DOC_Already_Exist);
		}
		Long end=System.currentTimeMillis();
		Long t=end-start;
		r.put("__time__",t.intValue());
		return r;
	}
	
	@Override  
	protected void process(List<String> chunks) {
		if(!chunks.isEmpty()){
			String s=chunks.get(chunks.size()-1);
			if(s.length()>40){
				StringBuffer sb=new StringBuffer(s.substring(0,40)+"...");
				s=sb.toString();
			}	
	        jf.solstar.setProgeressBarLabelText(s); 
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
		jf.setFrameEnable(true);

		
		jf.ClearData();
			
//		List<String> BlankIndexFiles=new ArrayList<String>();		//存储返回值-1的文件，即空文件夹或者内容为空的文件名称
//		List<String> UnArrveHtmlFiles=new ArrayList<String>();		//存储返回值-2的html名称，即html的url地址无效，或无法访问
//		List<String> UnFormatFiles=new ArrayList<String>();		//存储url格式错误的路径名称
//		List<String> SuccesIndexFiles=new ArrayList<String>();		//存储成功建立索引的文件名称
		Boolean formaterror=false;
		Boolean	success=false;
		try {	
			Map<String,Integer> r= get();	
			int n=0,j=0;
			Vector<Vector<String>> data = new Vector<Vector<String>>();
			for(Map.Entry<String,Integer> e:r.entrySet()){
				if(e.getKey()!="__time__"){
					Vector<String> line=new Vector<String>();
					if(e.getValue()==ResultsType.Index_NONE){
						line.add(String.valueOf(j++));
						line.add(e.getKey());
						line.add(String.valueOf(0));
						line.add("<html><font color=red>未检索到内容</font></html>");
					}
					else if(e.getValue()==ResultsType.Directory_NONE){
						line.add(String.valueOf(j++));
						line.add(e.getKey());
						line.add(String.valueOf(0));
						line.add("<html><font color=red>未找到文档</font></html>");
					}
					else if(e.getValue()==ResultsType.DOC_Already_Exist){
						line.add(String.valueOf(j++));
						line.add(e.getKey());
						line.add(String.valueOf(0));
						line.add("<html><font color=red>文档已存在</font></html>");
					}
					else if(e.getValue()==ResultsType.URL_UNArrive){
						line.add(String.valueOf(j++));
						line.add(e.getKey());
						line.add(String.valueOf(0));
						line.add("<html><font color=red>网站无法访问</font></html>");
					}
					else if(e.getValue()==ResultsType.DOC_Bad){
						line.add(String.valueOf(j++));
						line.add(e.getKey());
						line.add(String.valueOf(0));
						line.add("<html><font color=red>文档已经损坏</font></html>");
					}
					else if(e.getValue()==ResultsType.URL_Format_Error)		//返回-3时，必然会只有一个元素
						formaterror=true;
					else{
							n+=e.getValue();
							success=true;
							line.add(String.valueOf(j++));
							line.add(e.getKey());
							line.add(String.valueOf(e.getValue()));
							line.add("成功");
						}
					if(line.size()>0)
						data.add(line);					
				}
			}
			if(formaterror)
				JOptionPane.showMessageDialog(null, "请输入有效格式的路径", "警告", JOptionPane.ERROR_MESSAGE);
			if(success)
				DisplayGui.defselect.clear();
			jf.solstar.setStatusText("添加检索完毕!"+"耗时："+r.get("__time__")+"ms "+"创建索引条数："+n);
			if(data.size()>0){
				jf.t.LoadData(data);
				jf.setTableVisabel(true);
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
