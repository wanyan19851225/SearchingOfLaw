package comm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

public class SOLResult extends JPanel{
	
	JEditorPane ste;
	
	public SOLResult(){
		
		ste=new JEditorPane();
		ste.setEditable(false);
		ste.setContentType("text/html");
		ste.setPreferredSize(new Dimension(740,421));
//		ste.setBorder(new LineBorder(new Color(127,157,185),0, false));
		ste.setFont(new Font("宋体",Font.PLAIN,15));
		
		JScrollPane sjs=new JScrollPane(ste);
		sjs.setPreferredSize(new Dimension(740,421));
		sjs.setViewportView(ste);
		
		class OpenFile implements HyperlinkListener{
			public void hyperlinkUpdate(HyperlinkEvent e) {
				 if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
					 String command = "explorer.exe "+e.getURL().toString();
					 try {
						Runtime.getRuntime().exec(command);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				 }
			}
		}	
		ste.addHyperlinkListener(new OpenFile());
		
		this.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		this.add(sjs);
	}
	
	public SOLResult(Boolean f){
		BoxLayout layout=new BoxLayout(this, BoxLayout.Y_AXIS); 
		this.setLayout(layout);
		this.setPreferredSize(new Dimension(740,421));
		this.setBorder(BorderFactory.createLineBorder(Color.red));
	}
	
	public long UpdateTable(Map<String,List<String[]>> content){
		
		long total=0;
//		ste.setText("");
		
		if(content==null){
			JLabel jt=new JLabel();
			jt.setText("<html><i><b>未搜索到关键词</b></i></html>");
			this.add(jt);		
		}
		else{
			StringBuffer text=new StringBuffer();
			this.add(Box.createVerticalStrut(10));
			for(Entry<String,List<String[]>> entry: content.entrySet()){
				
				JLabel jt=new JLabel();
				jt.setText("<html><i><u><b><font color=blue face=\"微软雅黑\" size=4>"+entry.getKey()+"&emsp"+"结果:"+entry.getValue().size()+"条"+"</font></b></u></i></html>");
				this.add(jt);
				this.add(Box.createVerticalStrut(7));
				/*
				for(int i=0;i<entry.getValue().size();i++){
					text.append("&emsp&emsp");
					String[] item=entry.getValue().get(i);
					text.append(item[1]);
					text.append("&emsp"+"<i>"+"--摘录自");
					text.append("<a href=\"file:///D:/Lucene/src/"+entry.getKey()+"\">"+entry.getKey()+"</a>");
					text.append("&emsp"+item[0]+"</i>");
					text.append("<br/>");
					text.append("<br/>");
					total++;
					}
					*/
				}
			//ste.setText(text.toString());
		}
		
		
		return total;
		
	}
	
	public long UpdateText(Map<String,List<String[]>> content,String type){
		
		long total=0;
		ste.setText("");
		
		if(content.isEmpty())
			ste.setText("<i><b>未搜索到关键词</b></i>");
		else{
			StringBuffer text=new StringBuffer();
			for(Entry<String,List<String[]>> entry: content.entrySet()){
				FileIndexs findexs=new FileIndexs();
				Map<String,String[]> finfo=new HashMap<String,String[]>();
				finfo=findexs.QueryFiles(Path.filepath,"\""+entry.getKey()+"\"");
				String url="";
				if(type.equals(Store.Type.L)){
					for(String[] v : finfo.values()){
						if(v[5].equals(Store.Docment.WORD))
							url="<a href=\"file:///"+v[4]+"\">"+entry.getKey()+"</a>";
						else
							url="<a href="+v[4]+">"+entry.getKey()+"</a>";
					}
				}	
				for(int i=0;i<entry.getValue().size();i++){
					text.append("&emsp&emsp ");
					String[] item=entry.getValue().get(i);
					text.append(item[1]);
					if(type.equals(Store.Type.R)){
						if(item[3].equals(Store.Docment.WORD))
							url="<a href=\"file:///"+item[2]+"\">"+entry.getKey()+"</a>";
						else
							url="<a href="+item[2]+">"+entry.getKey()+"</a>";
					}
					text.append("&emsp"+"<i>"+"--摘录自");
					text.append(url);
					text.append("&emsp"+item[0]+"</i>");
					text.append("<br/>");
					text.append("<br/>");
					total++;
					}
				}
			ste.setText(text.toString());
		}
		ste.setCaretPosition(0);
		return total;
	}

//	public static void main(String[] args) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException, InvalidTokenOffsetsException{
//		
//		JFrame jf=new JFrame();
//		Container contentpane=jf.getContentPane();
//		contentpane.setLayout(new BorderLayout(0,0));
//		
//		HandleLucene handle=new HandleLucene();
//		
//		SOLResult res=new SOLResult(true);
//		
//		Map<String,List<String[]>> content=handle.GetSearch("D:\\Lucene\\index\\","当事人");
//		
////		res.UpdateText(content);
//		res.UpdateTable(content);
//		
//		IOHistory iohis=new IOHistory();
//		
//		List<String> input=iohis.HistoryReaderByList("D:\\Lucene\\conf\\history.cf");
//
//		SOLHistory his=new SOLHistory(null);
//	
//	
////		Date date=new Date(System.currentTimeMillis());
////		DateFormat dformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////		String fdate=dformat.format(file.lastModified());
////		String ndate=dformat.format(date);
//		
//		
//		contentpane.add(his,BorderLayout.WEST);
//		contentpane.add(res,BorderLayout.EAST);
//		
//	    jf.setTitle("Searching Of Laws");//窗体标签  
//	    jf.setSize(963,588);//窗体大小  
//	    jf.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
//	    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出关闭JFrame  
//	    jf.setVisible(true);//显示窗体
//	    jf.setResizable(false); //锁定窗体	
//	}

}
