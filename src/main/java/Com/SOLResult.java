package Com;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

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
}
