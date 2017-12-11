package comm;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SOLStar extends JPanel{
	private JLabel sll;
	
	SOLStar(Dimension size){

		this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
//		this.setBorder(BorderFactory.createLineBorder(Color.red));
//		this.setBackground(Color.LIGHT_GRAY);
//		this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		sll=new JLabel();
		sll.setPreferredSize(size);
		
		this.add(sll);
		this.setPreferredSize(new Dimension(400,24));
	}
	
	public void setStatusText(String sr){
		sll.setText(sr);
	}
	
	public String getStatusText(){
		return sll.getText();
	}

}
