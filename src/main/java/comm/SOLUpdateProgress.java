package comm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;

public class SOLUpdateProgress extends SwingWorker<Map<String,Boolean>,String>{

	private SOLShowUpdate p;
	private URL url=null;
	HttpURLConnection http=null;
	
	public SOLUpdateProgress(SOLShowUpdate p) {
		this.p=p;
		String download="http://47.97.108.15:8080/swd/download/SearchingOfLaw.exe";
		try {
			url = new URL(download);
			http = (HttpURLConnection) url.openConnection();
	        http.connect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int GetFileSize(){
		int size=0;
        size=http.getContentLength();
		return size;
	}

	@Override
	protected Map<String, Boolean> doInBackground(){
		// TODO Auto-generated method stub
		
		Map<String,Boolean> m=new HashMap<String,Boolean>();
		
		File tmp = new File(Path.downloadtmpfilepath);

		FileOutputStream fos=null;
		BufferedInputStream bis=null;
		InputStream is=null;

		int fsize=this.GetFileSize();
		
		try {
	        byte[] buffer = new byte[1024];
	        int size=0;
	        
	        is = http.getInputStream();
	        bis =new BufferedInputStream(is);
	        fos= new FileOutputStream(tmp);
	        
	        while ((size = bis.read(buffer)) != -1) {
	            fos.write(buffer,0,size);
	            fos.flush();
	            publish(String.valueOf(size)+"/"+String.valueOf(fsize));
	        }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				if(fos!=null)
					fos.close();
				if(bis!=null) 
					bis.close();
				if(is!=null)
					is.close();
				if(http!=null)
					http.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return m;
	}
	
	@Override  
	protected void process(List<String> chunks) {
		if(!chunks.isEmpty()){	
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMaximumFractionDigits(0);
			String result = numberFormat.format((float)chunks.get(chunks.size()-1).charAt(0)/(float) chunks.get(chunks.size()-1).charAt(chunks.get(chunks.size()-1).length()-1) * 100);
	        this.p.setProgeressBarLabelText("总进度：  "+result); 
	        int x=Integer.parseInt(chunks.get(chunks.size()-1).substring(0,chunks.get(chunks.size()-1).indexOf("/")).trim()); 
	        this.p.setProgressBarValue(x);  
		}
	}
	
	@Override
	protected void done() {
		this.p.SetProgressBarVisabel(false);
		this.p.SetProgressBarLabelVisabel(false);
		this.p.SetConfirmInfomationVisabel(true);
		this.p.SetCancelButtonVisabel(false);
		this.p.SetRebootButtonVisabel(true);
	}
}
