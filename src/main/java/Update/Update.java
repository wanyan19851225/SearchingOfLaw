package Update;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.NebulaBrickWallSkin;
import org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel;

import Com.FrameSize;

public class Update extends JFrame{
	
	public File of,nf;
	private JProgressBar jpb;
	private JLabel bl;
	
	public Update(File of,File nf){
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,1));
		
		this.of=of;
		this.nf=nf;
		
		jpb = new JProgressBar();		//进度条
	    jpb.setMinimum(0);  
	    jpb.setPreferredSize(new Dimension(FrameSize.X-60,(FrameSize.Y/4-100)/2));
	        
	    bl=new JLabel();		//进度条提示标签
	    bl.setPreferredSize(new Dimension(FrameSize.X-60,(FrameSize.Y/4-100)/2));
			
		JLabel ll=new JLabel();
		ll.setHorizontalAlignment(JLabel.CENTER);
		ll.setText("<html><font size=4>请勿中止安装，否则软件将无法正常启动</font></html>");
		ll.setPreferredSize(new Dimension(FrameSize.X-60,(FrameSize.Y/4-100)));
				
		JPanel cpane=new JPanel();		//创建进度条、进度条提示标签面板
		cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		    
		cpane.add(ll);
		cpane.add(jpb);
		cpane.add(bl);
	
		contentpane.add(cpane,BorderLayout.CENTER);
		
		this.setTitle("正在安装更新");//窗体标签  
		this.setSize(FrameSize.X-36,FrameSize.Y/4);//窗体大小  
		this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//禁止关闭JFrame  
		this.setVisible(true);//显示窗体
		this.setResizable(false); //锁定窗体
//		    this.setAlwaysOnTop(true);
		    
			UpdateProgress ub=new UpdateProgress(this);
			jpb.setMaximum(Integer.valueOf(String.valueOf(ub.GetFileSize())));
			ub.execute();
	 }
	
	public void CopyFile(File of, File nf) {
         FileInputStream in = null;
         FileOutputStream out = null;
         
         try {
             if(of.exists()){
                 of.delete();
             }
             in = new FileInputStream(nf);
             out = new FileOutputStream(of);

             byte[] buffer = new byte[1024 * 5];
             int size;
             while ((size = in.read(buffer)) != -1) {
                 out.write(buffer, 0, size);
                 out.flush();
             }
         } catch (FileNotFoundException ex) {
         } catch (IOException ex) {
         } finally {
             try {
                 out.close();
                 in.close();
             } catch (IOException ex1) {
             }
         }
	 }
	 
	public void setProgressBarValue(int n){
		jpb.setValue(n);
	}
	
	public void setProgeressBarLabelText(String s){
		bl.setText(s);
	}
	
	 public static void main(String[] args){
			
		 final File of=new File(args[0]);
		 final File nf=new File(args[1]);
		 
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){ 
				try {
					UIManager.setLookAndFeel(new SubstanceNebulaBrickWallLookAndFeel());
					SubstanceLookAndFeel.setSkin(new NebulaBrickWallSkin());
					new Update(of,nf);
				} catch (UnsupportedLookAndFeelException e) {
						// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}); 
	 }
}
