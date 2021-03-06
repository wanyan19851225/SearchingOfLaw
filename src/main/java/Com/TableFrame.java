package Com;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public abstract class TableFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextField stf;
	public IOTable t;
	public Vector<Vector<String>> data;
	public JButton sbt2,lbt;
	public JScrollPane jsp;

	public TableFrame(){
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));
		
        data = new Vector<Vector<String>>();
        
        stf=new JTextField(50);
		stf.setPreferredSize(new Dimension(300,30));
		sbt2=new JButton("搜索");
		sbt2.setPreferredSize(new Dimension(60,30));
        lbt=new JButton();
		lbt.setPreferredSize(new Dimension(60,30));
		JButton sbt=new JButton("全选");
		sbt.setPreferredSize(new Dimension(60,30));
		JButton sbt1=new JButton("反选");
		sbt1.setPreferredSize(new Dimension(60,30));
		jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-100));
		
		sbt.addActionListener(new SOLEvents.SelEvent(this));
		sbt1.addActionListener(new SOLEvents.UnselEvent(this));
		sbt2.addActionListener(new SOLEvents.FilterEvent(this));
		
		JPanel npane=new JPanel();		//搜索框、搜索按钮面板
	    npane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		JPanel cpane=new JPanel();		//列表面板
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
	    JPanel spane=new JPanel();		//全选，反选，删除按钮面板
	    spane.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
	    
		npane.add(stf);
		npane.add(sbt2);
	    cpane.add(jsp);
		spane.add(sbt);
		spane.add(sbt1);
		spane.add(lbt);
	    
		contentpane.add(npane,BorderLayout.NORTH);
		contentpane.add(cpane,BorderLayout.CENTER);
		contentpane.add(spane,BorderLayout.SOUTH);
 
	    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
	}
	
	public String GetKeywordsInputText(Boolean f){		//参数f判断是否使用模糊搜索方式
		StringBuffer s=new StringBuffer();
		String[] k=this.InputText2Keywords();
		if(k!=null){		//判断输入框是否为空
			if(k.length!=0){
				if(f)		//使用模糊搜索
					for(int i=0;i<k.length;i++){
						if(i==k.length-1)
							s.append(k[i]);
						else
							s.append(k[i]+" AND ");
					}
				else		//使用精确搜索
					for(int i=0;i<k.length;i++){
						if(i==k.length-1)
							s.append("\""+k[i]+"\"");
						else
							s.append("\""+k[i]+"\""+" AND ");
						
					}
			}
		}
		return s.toString();
	}
	public String[] InputText2Keywords(){
		String s=stf.getText().trim();
		String[] keywords=null;
		if(!s.isEmpty()){		//判断输入框是否为空
			UpdateString us=new UpdateString();
			String fk=us.FilterDoubleString(s," ");
			keywords=fk.split(" ");
		}
		return keywords;
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
