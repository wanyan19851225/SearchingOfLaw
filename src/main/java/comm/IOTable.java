package comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class IOTable extends JTable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DefaultTableModel tm;
	private Vector<String> cname;
	private int cnum;
	private Map<String,Vector<String>> id;

	public IOTable(Vector<String> c,Vector<Vector<String>> data){
		this.cname=c;
		cnum=cname.size();
		int dnum=data.size();
		tm=new DefaultTableModel();
        tm.setDataVector(data,cname);
		this.setModel(tm);
		this.SetTableStyle(FrameSize.X);
		this.id=new HashMap<String,Vector<String>>();
		for(int i=0;i<dnum;i++){
			id.put(data.elementAt(i).elementAt(1),data.elementAt(i));
		}
	}
	
	
	public void LoadData(Vector<Vector<String>> data){
        tm.setDataVector(data,cname);
        this.SetTableStyle(FrameSize.X);
	}
	
	public void InitTable(Boolean f){
		for(int i=0;i<this.getRowCount();i++){
			this.setValueAt(f,i,cnum-1);
		}
	}
	
	public void InitTable(List<Boolean> f){
		if(f.isEmpty()){
			for(int i=0;i<this.getRowCount();i++){
				this.setValueAt(true,i,cnum-1);
				f.add(true);
			}
		}
		else{
			for(int i=0;i<this.getRowCount();i++){
				this.setValueAt(f.get(i),i,cnum-1);
			}
		}
		
	}
	
	public List<String> GetAllRowsDatasAtColumn(int column){
		List<String> f=new ArrayList<String>();
		for(int i=0;i<this.getRowCount();i++){			
			if(((Boolean)this.getValueAt(i,cnum-1)).booleanValue()){
				f.add(this.getValueAt(i,column).toString());
			}							
		}
		
		return f;
	}
	
	public String GetOnceRowDataAtCloumn(int c,int r){
		String s=new String();
        if(c!=cnum-1)
        	s=this.getValueAt(r,c).toString();
        return s;
	}
	
	
	public void SetTableStyle(int size){
		
		for(int i=0;i<cnum;i++){		//设置列宽	
			if(i==0){
				this.getColumnModel().getColumn(i).setPreferredWidth((new Double(size*0.09)).intValue());
			}
			if(i==1){
				this.getColumnModel().getColumn(i).setPreferredWidth((new Double(size*0.61)).intValue());
			}
			if(i>1){
				this.getColumnModel().getColumn(i).setPreferredWidth((new Double(size*0.15)).intValue());
			}
			if(i==cnum-1){		//最后一列
				this.getColumnModel().getColumn(i).setCellEditor(this.getDefaultEditor(Boolean.class));		//设置checkebox是否可以被选中
				this.getColumnModel().getColumn(i).setCellRenderer(this.getDefaultRenderer(Boolean.class));		//设置显示复选框框体
			}
		}
		
		this.setRowHeight(30);		//设置行高
		
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();  	//设置单元格对齐方式为居中  
		r.setHorizontalAlignment(JLabel.CENTER);   
		this.setDefaultRenderer(Object.class,r);
		
	}
	
	public Vector<String> GetDataID(String s){
		String src=s.replaceAll("<[^>]+>","");		//删除html标签
		return id.get(src);
	}
	
	public void RemoveDataID(String s){
		id.remove(s);
	}

}
