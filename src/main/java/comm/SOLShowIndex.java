package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class SOLShowIndex extends JFrame{
	
	public IOTable t;
	private Vector<Vector<String>> data;
//	public Map<String,Integer> id;
	
	public SOLShowIndex(){
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));
		
		final HandleLucene handle=new HandleLucene();
		Map<String,Integer> fre=new HashMap<String,Integer>();

		Vector<String> cname = new Vector<String>();
		cname.add("序号");
		cname.add("文件名");  
        cname.add("法条总数");
        cname.add("是否删除");
        data = new Vector<Vector<String>>();
    
        fre=handle.GetTermFreq(Path.indexpath);
        if(!fre.isEmpty()){
        	int i=0;
 //       	id=new HashMap<String,Integer>();
        	for(Entry<String,Integer> entry: fre.entrySet()){
        		Vector<String> line=new Vector<String>();
        		line.add(String.valueOf(i++));
        		line.add(entry.getKey());
        		line.add(String.valueOf(entry.getValue()));
//        		id.put(entry.getKey(),i++);
        		data.add(line);
        	}
        }
        
        t=new IOTable(cname,data);
        t.InitTable(false);
		
		final JButton lbt=new JButton("删除");
		lbt.setPreferredSize(new Dimension(60,35));
		
		JButton sbt=new JButton("全选");
		sbt.setPreferredSize(new Dimension(60,35));
		
		JButton sbt1=new JButton("反选");
		sbt1.setPreferredSize(new Dimension(60,35));
		
		JScrollPane jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-88));
		jsp.setViewportView(t);

		
		lbt.addActionListener(new SOLEvents.DeleteIndexEvent(this));
		
		sbt.addActionListener(new SOLEvents.SelEvent(this));
		
		sbt1.addActionListener(new SOLEvents.UnselEvent(this));

		t.addMouseListener(new SOLEvents.ShowSOLShowLawsEvent(this));
		
		JPanel cpane=new JPanel();		//列表面板
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));	
	    JPanel spane=new JPanel();		//状态栏面板
		
		cpane.add(jsp);
		spane.add(sbt);
		spane.add(sbt1);
		spane.add(lbt);
		
		contentpane.add(cpane,BorderLayout.CENTER);
		contentpane.add(spane,BorderLayout.SOUTH);
		
	    this.setTitle("Searching Of Laws");//窗体标签  
	    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
	}
	
	public void RemoveData(Vector<String> obj){
		data.remove(obj);
	}

	public Vector<Vector<String>> GetData(){
		return data;
	}
	
	public void SelectAll(){
		int cnum=t.getColumnCount();
		int rnum=t.getRowCount();
		for(int i=0;i<rnum;i++){
			t.setValueAt(true,i,cnum-1);
		}
	}
	
	public void SelectInvert(){
		int cnum=t.getColumnCount();
		int rnum=t.getRowCount();
		for(int i=0;i<rnum;i++){
			
			if(((Boolean)t.getValueAt(i,cnum-1)).booleanValue())
				t.setValueAt(false,i,cnum-1);
			else
				t.setValueAt(true,i,cnum-1);
		}	
	}
}
