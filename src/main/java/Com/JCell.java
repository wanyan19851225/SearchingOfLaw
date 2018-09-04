package Com;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class JCell extends JLabel implements TableCellRenderer{
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
	     
		int maxPreferredHeight = 30; 
	    for (int i = 0; i < table.getColumnCount(); i++) {   
	    	setText("" + table.getValueAt(row, i)); 
	        setSize(table.getColumnModel().getColumn(column).getWidth(), 0); 
	        maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);    
	    }
//	    if(maxPreferredHeight<30)
//	    	maxPreferredHeight=30;
	    if (table.getRowHeight(row) != maxPreferredHeight)  
	    	table.setRowHeight(row, maxPreferredHeight); 

		this.setText(value == null ? "" : value.toString());
	    if(row%2 == 0)
	    	setBackground(Color.decode("#E0FFFF")); // 设置奇数行底色
	    else
	    	setBackground(Color.decode("#FFFAFA")); // 设置奇数行底色
		return this;
	}

}
