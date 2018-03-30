package bacy_testscript;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.JButton;

public class Mysql_gui {
	
	public static void main(String[] args) {
		
		
		SelectAction select=new SelectAction();
		Frame f=new Frame("my awt");  
		f.setSize(500,400);  
		f.setLocation(300,200);  
		f.setLayout(new FlowLayout());  
		JButton b=new JButton("select");  
		b.addActionListener(select);
		f.add(b);  
        f.addWindowListener(new MyWin());           
        f.setVisible(true);  
	}
	
}

class MyWin extends WindowAdapter{
	   public void windowClosing(WindowEvent e)
	   {
	       System.exit(0);//关闭窗口处理关闭动作监听事件
	     }
	}

class SelectAction implements ActionListener{

	public void actionPerformed(ActionEvent e) {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("start");		
			Connection connect=DriverManager.getConnection("jdbc:mysql://192.168.1.31:3306/BACY?useSSL=false","root","bd@bdht.C0M");
			PreparedStatement statement=connect.prepareStatement("SELECT * from BACY_DEVICE WHERE imei=862623030095562");
			ResultSet rs=statement.executeQuery();
			while(rs.next()){
				System.out.println(rs.getString(1));
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	
}


