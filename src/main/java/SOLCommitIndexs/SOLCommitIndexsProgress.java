package SOLCommitIndexs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import Com.Path;

public class SOLCommitIndexsProgress extends SwingWorker<Map<String,int[]>,String>{
	
	private List<String> file;
	private SOLShowConfirmCommitIndexs cp;
	private SOLCommitIndex sp;
	
	public SOLCommitIndexsProgress(SOLShowConfirmCommitIndexs p,List<String> file){
		this.file=file;
		this.cp=p;
		sp=cp.GetSOLShowCommitIndexFrame();
	}

	@Override
	protected Map<String, int[]> doInBackground() throws Exception {
		// TODO Auto-generated method stub
		cp.SetProgressBarVisabel(true);
		cp.SetProgressBarLabelVisabel(true);
		cp.SetConfirmInfomationVisabel(false);
		cp.setEnable(false);
		
		Map<String,int[]> m=new HashMap<String,int[]>();
		int size=file.size();
		for(int i=0;i<size;i++){
			String s=file.get(i).replaceAll("<[^>]+>","");		//删除html标签
			Vector<String> obj=sp.t.GetDataID(s);
			int[] res=sp.CommitIndex(Path.urlpath,s,Path.indexpath);
			m.put(s,res);
			if(res[0]==res[1]&&res[0]!=-1&&res[1]!=-1){
				sp.RemoveData(obj);
				sp.t.RemoveDataID(s);
			}
			publish("("+(i+1)+"/"+size+")"+" "+s);
		}
		return m;
	}
	
	@Override  
	protected void process(List<String> chunks) {
		if(!chunks.isEmpty()){		
	        cp.setProgeressBarLabelText(chunks.get(chunks.size()-1)); 
	        int x=Integer.parseInt(chunks.get(chunks.size()-1).substring(chunks.get(chunks.size()-1).indexOf("(")+1,chunks.get(chunks.size()-1).indexOf("/")).trim()); 
	        cp.setProgressBarValue(x);  
		}
	}
	
	@Override
	protected void done() {
		
		cp.SetProgressBarVisabel(false);
		cp.SetProgressBarLabelVisabel(false);
		cp.SetConfirmInfomationVisabel(true);
		cp.setEnable(true);
		
		try {
			Map<String,int[]> m= get();
	        new SOLShowCommitIndexsResults(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			sp.t.LoadData(sp.GetData());
	        sp.t.InitTable(false);
	        
	        cp.dispose();
		}
	}

}
