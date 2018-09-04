package Update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;

public class UpdateProgress extends SwingWorker<Map<String,Boolean>,String>{
	
	private Update u;
	private File of,nf;
	
	public UpdateProgress(Update u) {
		// TODO Auto-generated constructor stub
		this.u=u;
		this.of=this.u.of;
		this.nf=this.u.nf;		
	}
	
	public long GetFileSize(){
		long size;
        size=nf.length();
		return size;
	}

	@Override
	protected Map<String, Boolean> doInBackground() throws Exception {
		// TODO Auto-generated method stub
        FileInputStream in = null;
        FileOutputStream out = null;
        
        long fsize=this.GetFileSize();
        
        try {
            if(of.exists()){
                of.delete();
            }
            in = new FileInputStream(nf);
            out = new FileOutputStream(of);

            byte[] buffer = new byte[1024 * 5];
            int size=0;
            int n=0;
            while ((size = in.read(buffer)) != -1) {
                out.write(buffer, 0, size);
                out.flush();
                n+=size;
                publish(String.valueOf(n)+"/"+String.valueOf(fsize));
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException ex1) {
            }
        }
		return null;
	}
	
	@Override  
	protected void process(List<String> chunks) {
		if(!chunks.isEmpty()){	
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMaximumFractionDigits(0);
			String s=chunks.get(chunks.size()-1);
			String s1=s.substring(0, s.indexOf("/")).trim();
			String s2=s.substring(s.indexOf("/")+1,s.length()).trim();
			double d1=Double.parseDouble(s1);
			double d2=Double.parseDouble(s2);
			double dd=d1/d2*100;
			String result = numberFormat.format(dd);
	        this.u.setProgeressBarLabelText("总进度： "+result+"%"); 
	        int x=Integer.parseInt(s1); 
	        this.u.setProgressBarValue(x);  
		}
	}
	
	@Override
	protected void done() {
		this.u.dispose();
	}
}
