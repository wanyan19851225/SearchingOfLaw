package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;


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
		
		JScrollPane jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-88));
		jsp.setViewportView(t);

		
		lbt.addActionListener(new SOLEvents.DeleteIndexEvent(this));

		/*					
		class MouseEvents extends MouseAdapter{
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 1){
					int column=jt.columnAtPoint(e.getPoint()); //获取点击的列
		            if(column!=2){
		            	int row=jt.rowAtPoint(e.getPoint()); //获取点击的行
		            	int top=Integer.valueOf(jt.getValueAt(row,1).toString());
		            	String file=jt.getValueAt(row,0).toString();
		            	ShowLaws showlaws=new ShowLaws();
		            	try {
		            		showlaws.dispaly(file,top);
		            	} catch (IOException e1) {
		            		// TODO Auto-generated catch block
		            		e1.printStackTrace();
		            	} catch (ParseException e1) {
		            		// TODO Auto-generated catch block
		            		e1.printStackTrace();
		            	} catch (InvalidTokenOffsetsException e1) {
		            		// TODO Auto-generated catch block
		            		e1.printStackTrace();
		            	}
					}
				}
			}
		}
*/		
		t.addMouseListener(new SOLEvents.ShowSOLShowLawsEvent(this));
		
		JPanel cpane=new JPanel();
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    JPanel spane=new JPanel();
		
		
		cpane.add(jsp);
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
}
