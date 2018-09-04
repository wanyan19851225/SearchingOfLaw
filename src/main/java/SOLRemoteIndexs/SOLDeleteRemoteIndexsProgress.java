//package SOLRemoteIndexs;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Vector;
//
//import javax.swing.SwingWorker;
//
//import Com.Path;
//
//public class SOLDeleteRemoteIndexsProgress extends SwingWorker<Map<String,Boolean>,String>{
//	
//	private List<String> file;
//	private SOLShowConfirmDeleteRemoteIndexs cp;
//	private SOLRemoteIndex sp;
//	
//	public SOLDeleteRemoteIndexsProgress(SOLShowConfirmDeleteRemoteIndexs p,List<String> file){
//		this.file=file;
//		this.cp=p;
//		sp=cp.GetSOLShowIndexFrame();
//	}
//
//	@Override
//	protected Map<String, Boolean> doInBackground() throws Exception {
//		// TODO Auto-generated method stub
//		cp.SetProgressBarVisabel(true);
//		cp.SetProgressBarLabelVisabel(true);
//		cp.SetConfirmInfomationVisabel(false);
//		cp.setEnable(false);
//		
//		Map<String,int[]> m=new HashMap<String,int[]>();
//		int size=file.size();
//
//		Boolean f=sp.DeleteRemoteIndex(Path.urlpath,file);
//		if(f){
//			for(int i=0;i<file.size();i++){
//				String s=file.get(i).replaceAll("<[^>]+>","");		//删除html标签
//				Vector<String> obj=sp.t.GetDataID(s);
//				sp.RemoveData(obj);
//				sp.t.RemoveDataID(s);
//			}
//		}
//		
//		return null;
//	}
//
//}
