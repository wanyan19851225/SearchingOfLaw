package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TabelShowFrame extends JFrame{

	protected IOTable t;
	private Vector<Vector<String>> data;
	private JButton lbt;
	
	public TabelShowFrame(Vector<String> name){
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));
		data = new Vector<Vector<String>>();
		t=new IOTable(name,data);
        t.InitTable(false);
    	lbt=new JButton("删除");
		lbt.setPreferredSize(new Dimension(60,35));
		
		JButton sbt=new JButton("全选");
		sbt.setPreferredSize(new Dimension(60,35));
		
		JButton sbt1=new JButton("反选");
		sbt1.setPreferredSize(new Dimension(60,35));
		
		JScrollPane jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-88));
		jsp.setViewportView(t);
		
		sbt.addActionListener(new SOLEvents.SelEvent(this));
		sbt1.addActionListener(new SOLEvents.UnselEvent(this));
//		t.addMouseListener(new SOLEvents.ShowSOLShowLawsEvent(this));
		
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
	
	public void AddData(Vector<String> d){
		data.add(d);
	}
	
	public void RemoveData(Vector<String> obj){
		data.remove(obj);
	}
	
	public Vector<Vector<String>> GetData(){
		return data;
	}
	
	public void setButtonText(String s){
		lbt.setText(s);
	}
	
	public void setButtonEvent(ActionListener e){
		lbt.addActionListener(e);
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
