package SOLDownloadIndexs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import Com.Path;
import SOLShowIndexs.SOLShowDeleteIndexsResults;

public class SOLDownloadIndexsProgress extends SwingWorker<Map<String,Boolean>,String>{
	private List<String> file;
	private SOLShowConfirmDownloadIndexs cp;
	private SOLDownloadIndex sp;
	public SOLDownloadIndexsProgress(SOLShowConfirmDownloadIndexs p,List<String> file){
		this.file=file;
		this.cp=p;
		sp=cp.GetSOLShowCommitIndexFrame();
	}
	
	@Override
	protected Map<String, Boolean> doInBackground() throws Exception {
		// TODO Auto-generated method stub
		cp.SetProgressBarVisabel(true);
		cp.SetProgressBarLabelVisabel(true);
		cp.SetConfirmInfomationVisabel(false);
		cp.setEnable(false);
		
		Map<String,Boolean> m=new HashMap<String,Boolean>();
		int size=file.size();
		for(int i=0;i<size;i++){
			String s=file.get(i).replaceAll("<[^>]+>","");
			boolean f=sp.DownloadIndex(Path.urlpath, s);
			m.put(s,f);
			if(f){
				Vector<String> obj=sp.t.GetDataID(s);
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
			Map<String,Boolean> m= get();
	        new SOLShowDeleteIndexsResults(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			sp.t.LoadData(sp.GetData());
	        sp.t.InitTable(true);
	        cp.dispose();
		}
	}

}
