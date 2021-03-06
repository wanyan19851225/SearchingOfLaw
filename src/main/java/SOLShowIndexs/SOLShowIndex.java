package SOLShowIndexs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Com.FileIndexs;
import Com.FrameSize;
import Com.TableFrame;
import Com.IOTable;
import Com.Path;
import Com.SOLEvents;
import Com.UpdateString;
import Com.SOLEvents.ConfirmDeleteEvent;
import Com.SOLEvents.FilterEvent;
import Com.SOLEvents.SelEvent;
import Com.SOLEvents.ShowSOLShowLawsEvent;
import Com.SOLEvents.UnselEvent;
import SOLAddIndexs.SOLAddIndex;


public class SOLShowIndex extends TableFrame{
	
//	public IOTable t;
//	private Vector<Vector<String>> data;
//	private JTextField stf;
	private static SOLShowIndex single=null;
	
	public SOLShowIndex(){
//		Container contentpane=this.getContentPane();
//		contentpane.setLayout(new BorderLayout(3,1));
		
		FileIndexs findexs=new FileIndexs();
		Map<String,String[]> finfo=findexs.GetFileInfo(Path.filepath);

		Vector<String> cname = new Vector<String>();
		cname.add("序号");
		cname.add("文件名");  
        cname.add("法条总数");
        cname.add("是否删除");
//        data = new Vector<Vector<String>>();
        
        if(!finfo.isEmpty()){
        	int i=0;
        	for(Entry<String,String[]> entry: finfo.entrySet()){
        		Vector<String> line=new Vector<String>();
        		String[] infos=entry.getValue();
        		line.add(String.valueOf(i++));
        		line.add(entry.getKey());
        		line.add(infos[2]);
        		data.add(line);
        	}
        }
        
        t=new IOTable(cname,data);
        t.InitTable(false);
        
//        stf=new JTextField(50);
//		stf.setPreferredSize(new Dimension(300,30));
//		
//		JButton sbt2=new JButton("搜索");
//		sbt2.setPreferredSize(new Dimension(60,30));
		
//		final JButton lbt=new JButton("删除");
//		lbt.setPreferredSize(new Dimension(60,30));
//		
//		JButton sbt=new JButton("全选");
//		sbt.setPreferredSize(new Dimension(60,30));
//		
//		JButton sbt1=new JButton("反选");
//		sbt1.setPreferredSize(new Dimension(60,30));
//		
//		JScrollPane jsp=new JScrollPane();
//		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-100));
        lbt.setText("删除");
		jsp.setViewportView(t);

		
		lbt.addActionListener(new SOLEvents.ConfirmDeleteEvent(this));
//		sbt.addActionListener(new SOLEvents.SelEvent(this));
//		sbt1.addActionListener(new SOLEvents.UnselEvent(this));
//		sbt2.addActionListener(new SOLEvents.FilterEvent(this));
		t.addMouseListener(new SOLEvents.ShowSOLShowLawsEvent(this));
		
//		JPanel npane=new JPanel();		//搜索框、搜索按钮面板
////		npane.setBorder(BorderFactory.createLineBorder(Color.red));
//	    npane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
//		JPanel cpane=new JPanel();		//列表面板
//	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
////	    cpane.setBorder(BorderFactory.createLineBorder(Color.black));
//	    JPanel spane=new JPanel();		//全选、反选、删除按钮面板
//	    spane.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
////	    spane.setBorder(BorderFactory.createLineBorder(Color.blue));
//		
//		npane.add(stf);
//		npane.add(sbt2);
//	    cpane.add(jsp);
//		spane.add(sbt);
//		spane.add(sbt1);
//		spane.add(lbt);
//		
//		contentpane.add(npane,BorderLayout.NORTH);
//		contentpane.add(cpane,BorderLayout.CENTER);
//		contentpane.add(spane,BorderLayout.SOUTH);
		
	    this.setTitle("查看本地仓库");//窗体标签  
//	    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
//	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
//	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
//	    this.setVisible(true);//显示窗体
//	    this.setResizable(false); //锁定窗体
	}
	
	public void RemoveData(Vector<String> obj){
		data.remove(obj);
	}

	public Vector<Vector<String>> GetData(){
		return data;
	}
	
//	public void SelectAll(){
//		int cnum=t.getColumnCount();
//		int rnum=t.getRowCount();
//		for(int i=0;i<rnum;i++){
//			t.setValueAt(true,i,cnum-1);
//		}
//	}
//	
//	public void SelectInvert(){
//		int cnum=t.getColumnCount();
//		int rnum=t.getRowCount();
//		for(int i=0;i<rnum;i++){
//			
//			if(((Boolean)t.getValueAt(i,cnum-1)).booleanValue())
//				t.setValueAt(false,i,cnum-1);
//			else
//				t.setValueAt(true,i,cnum-1);
//		}	
//	}
	
//	public String GetKeywordsInputText(Boolean f){		//参数f判断是否使用模糊搜索方式
//
//		StringBuffer s=new StringBuffer();
//		String[] k=this.InputText2Keywords();
//		if(k!=null){		//判断输入框是否为空
//			if(k.length!=0){
//				if(f)		//使用模糊搜索
//					for(int i=0;i<k.length;i++){
//						if(i==k.length-1)
//							s.append(k[i]);
//						else
//							s.append(k[i]+" AND ");
//					}
//				else		//使用精确搜索
//					for(int i=0;i<k.length;i++){
//						if(i==k.length-1)
//							s.append("\""+k[i]+"\"");
//						else
//							s.append("\""+k[i]+"\""+" AND ");
//						
//					}
////					s.append("\""+k+"\"");
//			}
//		}
//		return s.toString();
//	}
//	
//	public String[] InputText2Keywords(){
//		String s=stf.getText().trim();
//		String[] keywords=null;
//		if(!s.isEmpty()){		//判断输入框是否为空
//			UpdateString us=new UpdateString();
//			String fk=us.FilterDoubleString(s," ");
//			keywords=fk.split(" ");
//		}
//		return keywords;
//	}
	
	public static SOLShowIndex getInstance(){
		if(single!=null){
			if(!single.isShowing())
				single=new SOLShowIndex();			
			else
				single.requestFocus();		
		}
		else
			single=new SOLShowIndex();
		return single;	
	}
}
