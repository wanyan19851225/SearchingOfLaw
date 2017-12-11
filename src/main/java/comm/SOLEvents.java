package comm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;


public class  SOLEvents {
	
	public static class LocalEvent extends MouseAdapter{
		JPanel jp;
		
		public LocalEvent(JPanel jp){
			this.jp=jp;
		}
		
		public void mouseClicked(MouseEvent e){	
			int count = jp.getComponentCount();
			jp.setEnabled(false);
			for (int i = 0; i < count; i++) {
			    Object obj = jp.getComponent(i);
			    if (obj instanceof JTextField) {
			    	JTextField jt=(JTextField)obj;
			        jt.setEnabled(false);
			    }
			    if(obj instanceof JLabel){
			    	JLabel jl=(JLabel)obj;
			        jl.setEnabled(false);
			    }
			}	
		}
		
	}
	
	public static class HostEvent extends MouseAdapter{
		JPanel jp;
		
		public HostEvent(JPanel jp){
			this.jp=jp;
		}
		
		public void mouseClicked(MouseEvent e){	
			int count = jp.getComponentCount();
			jp.setEnabled(true);
			for (int i = 0; i < count; i++) {
			    Object obj = jp.getComponent(i);
			    if (obj instanceof JTextField) {
			    	JTextField jt=(JTextField)obj;
			        jt.setEnabled(true);
			    }
			    if(obj instanceof JLabel){
			    	JLabel jl=(JLabel)obj;
			        jl.setEnabled(true);
			    }
			}	
		}
		
	}

	public static class ExeEvent extends MouseAdapter{
		
		List<String> range;
		JTable jt;
		List<Boolean> defselect;
		JFrame p;
		
		public ExeEvent(List<String> range,JTable jt,List<Boolean> defselect,JFrame p){
			this.range=range;
			this.jt=jt;
			this.defselect=defselect;
			this.p=p;
			
		}
		public void mouseClicked(MouseEvent e){
			range.clear();
			for(int i=0;i<jt.getRowCount();i++){			
				if(((Boolean)jt.getValueAt(i,2)).booleanValue()){
					range.add(jt.getValueAt(i, 0).toString());	
				}
				defselect.set(i,((Boolean)jt.getValueAt(i,2)).booleanValue());
			}
			p.dispose();//退出关闭JFrame 
		}	
	} 

	public static class SelEvent implements ActionListener{
		JTable jt;
		
		public SelEvent(JTable jt){
			this.jt=jt;
		}
		
		public void actionPerformed(ActionEvent e) {
			for(int i=0;i<jt.getRowCount();i++){
				jt.setValueAt(true,i,2);
			}
		}
	}

	public static class UnselEvent implements ActionListener{
		JTable jt;
		
		public UnselEvent(JTable jt){
			this.jt=jt;
		}
		public void actionPerformed(ActionEvent e) {
			for(int i=0;i<jt.getRowCount();i++){
			
				if(((Boolean)jt.getValueAt(i,2)).booleanValue())
					jt.setValueAt(false,i,2);
				else
					jt.setValueAt(true,i,2);
			}	
		}
	}
	
	public static class SelectEvent extends MouseAdapter{
		
		String ipath;
		List<String> range;
		List<Boolean> defselect;
		public SelectEvent(String ipath,List<String> range,List<Boolean> defselect){
			this.ipath=ipath;
			this.range=range;
			this.defselect=defselect;
		}
		public void mouseClicked(MouseEvent e){
			SOLSelectIndex selectindex=new SOLSelectIndex(ipath,range,defselect);
		}
		
	}
	
	public static class AboutEvent implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SOLAbout about=new SOLAbout();
		}		
	}

	public static class SearchEvent extends MouseAdapter{
		
		JButton sbt;
		JTextField stf,adr,port;
//		JPanel npaneofnorth,npaneofcenter;
		int top;
		Boolean rangef=true;
		Boolean srf;
		List<String> range,tmphis,history;
		SOLHistory solhis;
		SOLResult res;
		SOLStar star;
		String ipath;

		public SearchEvent(JPanel npaneofnorth,JPanel npaneofcenter,List<String> range,List<String> tmphis,List<String> history,SOLHistory solhis,String ipath,SOLResult res,SOLStar star){
			
//			this.npaneofnorth=npaneofnorth;
//			this.npaneofcenter=npaneofcenter;
			this.range=range;
			this.tmphis=tmphis;
			this.history=history;
			this.solhis=solhis;
			this.ipath=ipath;
			this.res=res;
			this.star=star;
			
			int c=npaneofnorth.getComponentCount();
			
			for(int i=0;i<c;i++){
			    Object obj =npaneofnorth.getComponent(i);
			    if (obj instanceof JTextField)
			    	this.stf=(JTextField)obj;
			    if(obj instanceof JButton)
			    	this.sbt=(JButton)obj;   
			}
			
			int s=npaneofcenter.getComponentCount();
			for(int i=0;i<s;i++){
			    Object obj =npaneofcenter.getComponent(i);
			    JPanel jp=(JPanel)obj;
			    int a=jp.getComponentCount();
			    for(int j=0;j<a;j++){
				    Object obj1 =npaneofcenter.getComponent(j);
				    if (obj1 instanceof JRadioButton){
				    	JRadioButton rb=(JRadioButton)obj1;
				    	if(rb.isSelected()){
				    		if(rb.getName().equals("1000"))
				    			this.top=1000;
				    		else if(rb.getName().equals("2000"))
				    			this.top=2000;
				    		else if(rb.getName().equals("3000"))
				    			this.top=3000;
				    		else if(rb.getName().equals("other"))
				    			this.rangef=false;
				    		else if(rb.getName().equals("remote"))
				    			this.srf=false;
				    	}
				    }
				    
				    if(obj instanceof JTextField){
				    	JTextField jt=(JTextField)obj;
				    	if(jt.getName().equals("adr"))
				    		this.adr=jt;
				    	else if(jt.getName().equals("port"))
				    		this.port=jt;
				    } 
			    }

			}
		
		}
		
		public void mouseClicked(MouseEvent e){

			HandleLucene handle=new HandleLucene();
			Map<String,List<String[]>> content=new HashMap<String,List<String[]>>();
			StringBuffer keywords=new StringBuffer();
			Date date=new Date(System.currentTimeMillis());
			DateFormat dformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			sbt.setEnabled(false);
			keywords.append(stf.getText());
			
			try {
	
				if(rangef)
					range.clear();
				
				if(!keywords.toString().isEmpty()){
					IOList iolist=new IOList();
					String ndate=dformat.format(date);
					iolist.add("Date@"+ndate+" "+keywords.toString(),tmphis);
					iolist.add("Date@"+ndate+" "+keywords.toString(),history);
					solhis.UpdateHistory(tmphis);
					long start=System.currentTimeMillis();
					if(range.isEmpty()){
						content=handle.GetSearch(ipath,keywords.toString(),10);
					}
					else{					
//						多条件查询，指定在某个法条文档中查询						
						String[] fields=new String[]{"file","law"};
						content=handle.GetMultipleSearch(ipath,fields,range,keywords.toString(),top);
					}
							
					long end=System.currentTimeMillis();
					long total=res.UpdateText(content);
					star.setStatusText("检索完毕!"+" "+"耗时："+String.valueOf(end-start)+"ms"+" "+"共搜索到："+total);
					
				}else
					JOptionPane.showMessageDialog(null, "关键词不允许为空", "警告", JOptionPane.ERROR_MESSAGE);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidTokenOffsetsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (java.text.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}					
			sbt.setEnabled(true);		
		}
	}
	
}
