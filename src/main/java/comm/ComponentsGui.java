package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.lucene.queryparser.classic.ParseException;

public class ComponentsGui {
	
	public int i;
	
	class TextField extends JTextField{
		
		TextField(){
			this.setColumns(20);
			this.setPreferredSize(new Dimension(300,34));
			this.setFont(new Font("宋体",Font.PLAIN,15));
	
			this.addKeyListener(new KeysEvent());
			this.addFocusListener(new FocusEvent());
			
		}
		
		public void SetContent(String src){
			this.setText(src);
		}
		
	}
	
	class KeysEvent extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			int code=e.getKeyCode();
			if(code==KeyEvent.VK_ENTER){
				System.out.println("dd");
				JList list=new JList();
			}	
		}	
	}
	
	class FocusEvent implements FocusListener{
		
		public void focusGained(java.awt.event.FocusEvent e) {
			// TODO Auto-generated method stub
			TextField tf=new TextField();
			System.out.println("dd");
			tf.SetContent("ddd");
		}
		public void focusLost(java.awt.event.FocusEvent e) {
			// TODO Auto-generated method stub
			TextField tf=new TextField();
			System.out.println("tt");
			tf.SetContent("请输入关键词dd");
		}
		
	}
	
	class Table extends JTable{
		
		Table() throws IOException, ParseException{ 
			HandleLucene handle=new HandleLucene();
			Map<String,Integer> fre=new HashMap<String,Integer>();
			Vector columnName = new Vector();
			columnName.add("文件名");  
	        columnName.add("法条总数");   
	        Vector rowData = new Vector();  
	        
	        fre=handle.GetTermFreq("D:\\Lucene\\index\\");
	        
	        for(String key:fre.keySet()){
	        	Vector line=new Vector();
	        	line.add(key);
	        	line.add(fre.get(key));
	        	rowData.add(line);
	        }
	        
	        
		}
	}
	
	public static void main(String[] args) throws IOException {
		ComponentsGui c=new ComponentsGui();
 
		TextField j=c.new TextField();
		
		JFrame jf=new JFrame();
		
		Container contentpane=jf.getContentPane();
		contentpane.setLayout(new BorderLayout(20,20));
		
		contentpane.add(j);
		
	    jf.setTitle("Searching Of Laws");//窗体标签  
	    jf.setSize(963,588);//窗体大小  
	    jf.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出关闭JFrame  
	    jf.setVisible(true);//显示窗体
	    jf.setResizable(false); //锁定窗体	
	}
	
}
