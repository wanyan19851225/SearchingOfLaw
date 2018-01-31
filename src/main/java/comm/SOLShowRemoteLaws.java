package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SOLShowRemoteLaws extends JFrame{
	
	public SOLShowRemoteLaws(String file,int top) throws IOException{
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));
		
		Vector<String> cname = new Vector<String>();
		cname.add("序号");
        cname.add("法条内容");
		cname.add("路径"); 
        cname.add("是否删除");
        Vector<Vector<String>> data = new Vector<Vector<String>>();
        
        try{
        	Map<String,List<String[]>> contentoflaw=this.GetRemoteLaws(Path.urlpath,file,top);

        	if(!contentoflaw.isEmpty()){
        		List<String[]> laws=contentoflaw.get(file);
        
        		for(int i=0;i<laws.size();i++){
        			Vector<String> line=new Vector<String>();
        			line.add(String.valueOf(i));
        			line.add(laws.get(i)[1]);
        			line.add(laws.get(i)[0]);
        			data.add(line);	
        		}
        	}
        }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,e.getMessage(), "警告", JOptionPane.ERROR_MESSAGE);
	}
        
        IOTable t=new IOTable(cname,data);
        t.InitTable(false);
        t.SetTableStyle(WindowSize.X);
		
		JButton lbt=new JButton("删除");
		lbt.setPreferredSize(new Dimension(60,35));
		
		JScrollPane jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(963,500));
		jsp.setViewportView(t);
		
/*
		class DeleteEvent implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				
				for(int i=0;i<jt.getRowCount();i++){			
					if(((Boolean)jt.getValueAt(i,2)).booleanValue()){
						file.add(jt.getValueAt(i, 0).toString());
					}							
				}
				if(!file.isEmpty()){
					for(int i=0;i<file.size();i++){
						try {
							handle.DeleteIndex(file.get(i),"D:\\Lucene\\index\\");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
					}
				rowdata.clear();
				Map<String,Integer> fret=new HashMap<String,Integer>();
		        fret=handle.GetTermFreq("D:\\Lucene\\index\\"); 
				
				if(!fret.isEmpty()){
					for(String key:fret.keySet()){
						Vector line=new Vector();
						line.add(key);
						line.add(fret.get(key));
						rowdata.add(line);
					}
				}
				tableModel.setDataVector(rowdata,columname);
				jt.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));
				for(int i=0;i<jt.getRowCount();i++){
					jt.setValueAt(false,i,2);
				}		
				}
				else
					JOptionPane.showMessageDialog(null, "请选择要删除的索引", "警告", JOptionPane.ERROR_MESSAGE);	
			}	
		}
		lbt.addActionListener(new DeleteEvent());
*/						
		JPanel cpane=new JPanel();
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    JPanel spane=new JPanel();
			
		cpane.add(jsp);
		spane.add(lbt);
		
		contentpane.add(cpane,BorderLayout.CENTER);
		contentpane.add(spane,BorderLayout.SOUTH);
		
	    this.setTitle("Searching Of Laws-"+file);//窗体标签  
	    this.setSize(WindowSize.X,WindowSize.Y);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
	}
	
	public Map<String,List<String[]>> GetRemoteLaws(String url,String file,int top) throws Exception{
		Map<String,List<String[]>> r=new HashMap<String,List<String[]>>();
		JSONObject body=new JSONObject();
		body.accumulate("token","");
		body.accumulate("command","106");
		body.accumulate("user","tmp");
		body.accumulate("file",file);
		body.accumulate("top",top);
		JSONObject response=new JSONObject();
		IOHttp http=new IOHttp(url);
		response=http.sendPost(body.toString());
		
		int res=response.getInt("result");		//获取服务器写入索引文件成功的法条总数
		if(res==1){
	        JSONArray objarry=response.getJSONArray("lawslist");
	        JSONObject tem=new JSONObject();

	        List<String[]> laws=new ArrayList<String[]>();
	        for(int i=0;i<objarry.size();i++){		
	        	tem=objarry.getJSONObject(i);
		        String[] law=new String[2];
	        	law[0]=tem.getString("path");
	        	law[1]=tem.getString("law");
	        	laws.add(law);
	        }
	        r.put(response.getString("file"),laws);
		}
		return r;
	}

}

