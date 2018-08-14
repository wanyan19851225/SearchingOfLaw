package comm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

public class SOLHistory extends JPanel{
	
	private JPanel onedaypanel,twodaypanel,threedaypanel,oneweekpanel,onemothpanel,morepanel;
	private JLabel oneday,twoday,threeday,oneweek,onemoth,more;
//	private SOLResult res;
//	private SOLStar star;
	private DisplayGui p;
	
	public SOLHistory(DisplayGui p) throws IOException, ParseException{
		this.p=p;
		
		oneday=new JLabel("今天");
		oneday.setText("<html><i><u><font color=blue face=\"微软雅黑\">今天</font></u></i></html>");
		twoday=new JLabel("两天内");
		twoday.setText("<html><u><i><font color=blue face=\"微软雅黑\">两天内</font></i></u></html>");
		threeday=new JLabel("三天内");
		threeday.setText("<html><u><i><font color=blue face=\"微软雅黑\">三天内</font></i></u></html>");
		oneweek=new JLabel("一周内");
		oneweek.setText("<html><u><i><font color=blue face=\"微软雅黑\">一周内</font></i></u></html>");
		onemoth=new JLabel("一月内");
		onemoth.setText("<html><u><i><font color=blue face=\"微软雅黑\">一月内</font></i></u></html>");
		more=new JLabel("超过一个月");
		more.setText("<html><u><i><font color=blue face=\"微软雅黑\">超过一个月</font></i></u></html>");
		
		
		onedaypanel=new JPanel();
//		onedaypanel.setBorder(BorderFactory.createLineBorder(Color.red));
		onedaypanel.setLayout(new BoxLayout(onedaypanel, BoxLayout.Y_AXIS));
		twodaypanel=new JPanel();
		twodaypanel.setLayout(new BoxLayout(twodaypanel, BoxLayout.Y_AXIS));
		threedaypanel=new JPanel();
		threedaypanel.setLayout(new BoxLayout(threedaypanel, BoxLayout.Y_AXIS));
		oneweekpanel=new JPanel();
		oneweekpanel.setLayout(new BoxLayout(oneweekpanel, BoxLayout.Y_AXIS));
		onemothpanel=new JPanel();
		onemothpanel.setLayout(new BoxLayout(onemothpanel, BoxLayout.Y_AXIS));
		morepanel=new JPanel();
		morepanel.setLayout(new BoxLayout(morepanel, BoxLayout.Y_AXIS));
	
		
		class OneDayShow extends MouseAdapter{
			public void mouseClicked(MouseEvent e){
				Boolean is=onedaypanel.isVisible();
				if(is==false)
					onedaypanel.setVisible(true);
				else
					onedaypanel.setVisible(false);
				
				if(oneday.getName().equals("c")){
					oneday.setText("<html><font color=black face=\"微软雅黑\">今天</font></html>");
					oneday.setName("s");
				}else{
					oneday.setText("<html><i><u><font color=blue face=\"微软雅黑\">今天</font></u></i></html>");
					oneday.setName("c");
				}
			}
			
		}
		oneday.addMouseListener(new OneDayShow());

		
		class TwoDayShow extends MouseAdapter{
			public void mouseClicked(MouseEvent e){
				Boolean is=twodaypanel.isVisible();
				if(is==false)
					twodaypanel.setVisible(true);
				else
					twodaypanel.setVisible(false);	
				
				if(twoday.getName().equals("c")){
					twoday.setText("<html><font color=black face=\"微软雅黑\">两天内</font></html>");
					twoday.setName("s");
				}else{
					twoday.setText("<html><i><u><font color=blue face=\"微软雅黑\">两天内</font></u></i></html>");
					twoday.setName("c");
				}
			}
			
		}
		twoday.addMouseListener(new TwoDayShow());
		
		class ThreeDayShow extends MouseAdapter{
			public void mouseClicked(MouseEvent e){
				Boolean is=threedaypanel.isVisible();
				if(is==false)
					threedaypanel.setVisible(true);
				else
					threedaypanel.setVisible(false);
				
				if(threeday.getName().equals("c")){
					threeday.setText("<html><font color=black face=\"微软雅黑\">三天内</font></html>");
					threeday.setName("s");
				}else{
					threeday.setText("<html><i><u><font color=blue face=\"微软雅黑\">三天内</font></u></i></html>");
					threeday.setName("c");
				}
			}
			
		}
		threeday.addMouseListener(new ThreeDayShow());
		
		class OneWeekShow extends MouseAdapter{
			public void mouseClicked(MouseEvent e){
				Boolean is=oneweekpanel.isVisible();
				if(is==false)
					oneweekpanel.setVisible(true);
				else
					oneweekpanel.setVisible(false);	
				
				if(oneweek.getName().equals("c")){
					oneweek.setText("<html><font color=black face=\"微软雅黑\">一周内</font></html>");
					oneweek.setName("s");
				}else{
					oneweek.setText("<html><i><u><font color=blue face=\"微软雅黑\">一周内</font></u></i></html>");
					oneweek.setName("c");
				}
			}
			
		}
		oneweek.addMouseListener(new OneWeekShow());
		
		class OneMothShow extends MouseAdapter{
			public void mouseClicked(MouseEvent e){
				Boolean is=onemothpanel.isVisible();
				if(is==false)
					onemothpanel.setVisible(true);
				else
					onemothpanel.setVisible(false);	
				
				if(onemoth.getName().equals("c")){
					onemoth.setText("<html><font color=black face=\"微软雅黑\">一月内</font></html>");
					onemoth.setName("s");
				}else{
					onemoth.setText("<html><i><u><font color=blue face=\"微软雅黑\">一月内</font></u></i></html>");
					onemoth.setName("c");
				}
			}
			
		}
		onemoth.addMouseListener(new OneMothShow());
		
		class MoreShow extends MouseAdapter{
			public void mouseClicked(MouseEvent e){
				Boolean is=morepanel.isVisible();
				if(is==false)
					morepanel.setVisible(true);
				else
					morepanel.setVisible(false);	
				
				if(more.getName().equals("c")){
					more.setText("<html><font color=black face=\"微软雅黑\">超过一个月</font></html>");
					more.setName("s");
				}else{
					more.setText("<html><i><u><font color=blue face=\"微软雅黑\">超过一个月</font></u></i></html>");
					more.setName("c");
				}
			}
			
		}
		more.addMouseListener(new MoreShow());
			

		BoxLayout layout=new BoxLayout(this, BoxLayout.Y_AXIS); 
		this.setLayout(layout);
		
		this.add(Box.createVerticalStrut(10));
/*		
		JScrollPane jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(2,5));
		jsp.setViewportView(this);
*/		
		this.add(oneday);
		oneday.setVisible(false);
		if(oneday.isVisible())
			this.add(Box.createVerticalStrut(5));
		oneday.setName("c");
		
		this.add(onedaypanel);
		onedaypanel.setVisible(false);


		this.add(twoday);
		twoday.setVisible(false);
		if(twoday.isVisible())
			this.add(Box.createVerticalStrut(5));
		twoday.setName("c");
		
		this.add(twodaypanel);
		twodaypanel.setVisible(false);
		
		this.add(threeday);
		threeday.setVisible(false);
		if(threeday.isVisible())
			this.add(Box.createVerticalStrut(5));
		threeday.setName("c");
		
		this.add(threedaypanel);
		threedaypanel.setVisible(false);
		
		this.add(oneweek);
		oneweek.setVisible(false);
		if(oneweek.isVisible())
			this.add(Box.createVerticalStrut(5));
		oneweek.setName("c");
		
		this.add(oneweekpanel);
		oneweekpanel.setVisible(false);
		
		this.add(onemoth);
		onemoth.setVisible(false);
		if(onemoth.isVisible())
			this.add(Box.createVerticalStrut(5));
		onemoth.setName("c");
		
		this.add(onemothpanel);
		onemothpanel.setVisible(false);
		
		this.add(more);
		more.setVisible(false);
		if(more.isVisible())
			this.add(Box.createVerticalStrut(5));
		more.setName("c");
		
		this.add(morepanel);
		morepanel.setVisible(false);
		
//		IOHistory iohis=new IOHistory();
//		Map<String,List<String>> tmhis=iohis.HistoryReaderByTime("D:\\Lucene\\conf\\history.cf");
		this.UpdateHistory(p.GetHistory());
		
		this.setPreferredSize(new Dimension(200,50));
//		this.setBorder(BorderFactory.createLineBorder(Color.red));
		this.setBorder(BorderFactory.createTitledBorder("检索历史"));
		
		
	}

	public void LoadHistory(Map<String,List<String>> tmhis){
		
//	   ListIterator<Map.Entry<String,List<String>>> it=new ArrayList<Map.Entry<String,List<String>>>(tmhis.entrySet()).listIterator(tmhis.size()); 
	   
//	   while(it.hasPrevious()) {  
//           Map.Entry<String,List<String>> entry=it.previous();  
//           System.out.println(entry.getKey()+":"+entry.getValue());  
//      }
		
		if(!tmhis.isEmpty()){
		
			for(Entry<String,List<String>> entry: tmhis.entrySet()){
			
				if(entry.getKey().equals("今天")){
					for(int i=entry.getValue().size()-1;i>=0;i--){
						JLabel jb=new JLabel();
						jb.setText("<html><u><i><font color=blue face=\"微软雅黑\">"+entry.getValue().get(i)+"</font></i></u></html>");
						onedaypanel.add(jb);
						onedaypanel.add(Box.createVerticalStrut(5));
					}
				}else if(entry.getKey().equals("两天内")){
					for(int i=entry.getValue().size()-1;i>=0;i--){
						JLabel jb=new JLabel();
						jb.setText("<html><u><i><font color=blue face=\"微软雅黑\">"+entry.getValue().get(i)+"</font></i></u></html>");
						twodaypanel.add(jb);
						twodaypanel.add(Box.createVerticalStrut(5));
					}
				}else if(entry.getKey().equals("三天内")){
					for(int i=entry.getValue().size()-1;i>=0;i--){
						JLabel jb=new JLabel();
						jb.setText("<html><u><i><font color=blue face=\"微软雅黑\">"+entry.getValue().get(i)+"</font></i></u></html>");
						threedaypanel.add(jb);
						threedaypanel.add(Box.createVerticalStrut(5));
					}
				}else if(entry.getKey().equals("一周内")){
					for(int i=entry.getValue().size()-1;i>=0;i--){
						JLabel jb=new JLabel();
						jb.setText("<html><u><i><font color=blue face=\"微软雅黑\">"+entry.getValue().get(i)+"</font></i></u></html>");
						oneweekpanel.add(jb);
						oneweekpanel.add(Box.createVerticalStrut(5));
					}
				}else if(entry.getKey().equals("一月内")){
					for(int i=entry.getValue().size()-1;i>=0;i--){
						JLabel jb=new JLabel();
						jb.setText("<html><u><i><font color=blue face=\"微软雅黑\">"+entry.getValue().get(i)+"</font></i></u></html>");
						onemothpanel.add(jb);
						onemothpanel.add(Box.createVerticalStrut(5));
					}
				}else if(entry.getKey().equals("超过一个月")){
					for(int i=entry.getValue().size()-1;i>=0;i--){
						JLabel jb=new JLabel();
						jb.setText("<html><u><i><font color=blue face=\"微软雅黑\">"+entry.getValue().get(i)+"</font></i></u></html>");
						morepanel.add(jb);
						morepanel.add(Box.createVerticalStrut(5));
					}
			
				}	
			}
		}
			
	}

	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-24 
	 * 
	 * 显示搜索应用的首页
	 *
	 * @params 
	 * 			List<String> 传入需要更新组件的检索历史 			
	 * 			
	 * @return void	   
	 * 						  
	 * @2017-10-31
	 * 				新增setToolTip,鼠标悬浮框,显示检索具体时间和全文
	 * @Modified 2018-8-14
	 * 				新增使用检索记录检索文档时，实现进度条功能
	 * 				在SearchEvent鼠标事件中，改为调用SOLSearchIndexsProgress类，实现进度条功能
	 * 
	 */
	
	public void UpdateHistory(List<String> sb) throws ParseException{
		
		DateFormat dformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		UpdateString us=new UpdateString();
		long onedayt=1000*60*60*24;
		long twodayt=2*onedayt;
		long threedayt=3*onedayt;
		long oneweekt=7*onedayt;
		long onemotht=30*onedayt;
		
		class SearchEvent extends MouseAdapter{
			public void mouseClicked(MouseEvent e){ 
				JLabel jj=(JLabel)e.getSource();
//				HandleLucene handle=new HandleLucene();
				UpdateString us=new UpdateString();
//				Map<String, List<String[]>> content=new HashMap<String, List<String[]>>();
				String dd=us.DelHtmlString(jj.getText());
				jj.setEnabled(false);
				SOLSearchIndexsProgress pb=new SOLSearchIndexsProgress(p,dd);
				pb.execute();
//				long start=System.currentTimeMillis();
//				try {
//					content = handle.GetSearch("D:\\Lucene\\index\\",dd);
//				} catch (org.apache.lucene.queryparser.classic.ParseException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (InvalidTokenOffsetsException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				long end=System.currentTimeMillis();
//				long total=p.solresult.UpdateText(content);
//				DisplayGui.star.setStatusText("检索完毕!"+" "+"耗时："+String.valueOf(end-start)+"ms"+" "+"共搜索到："+total);
				jj.setEnabled(true);
			}
			
			public void mouseEntered(MouseEvent e){
				JLabel jj=(JLabel)e.getSource();
				jj.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		}
		
		onedaypanel.removeAll();
		twodaypanel.removeAll();
		threedaypanel.removeAll();
		oneweekpanel.removeAll();
		onemothpanel.removeAll();
		morepanel.removeAll();
		
		for(int i=sb.size()-1;i>=0;i--){
			StringBuffer time=new StringBuffer();
			String[] s=sb.get(i).split("@")[1].split(" ");
			StringBuffer cn=new StringBuffer();
			for(int j=0;j<s.length;j++){
				if(j==0)
					time.append(s[j]+" ");
				if(j==1)
					time.append(s[j]);
				if(j>1&&j<s.length-1)
					cn.append(s[j]+" ");
				if(j==s.length-1)
					cn.append(s[j]);	
			}
//			time.append(sb.get(i).split("@")[1].split(" ")[0]+" ");
//			time.append(sb.get(i).split("@")[1].split(" ")[1]);

			
			long dt=dformat.parse(time.toString()).getTime();
			long ct=System.currentTimeMillis();
			long dvt=ct-dt;
			
			if(dvt<=onedayt){
				JLabel jb=new JLabel();
				jb.setText("<html><u><i><font color=blue face=\"微软雅黑\">"+us.SimpleString(cn.toString())+"</font></i></u></html>");
				jb.setToolTipText(time.toString()+" "+cn);
				jb.addMouseListener(new SearchEvent());
				onedaypanel.add(jb);
				onedaypanel.add(Box.createVerticalStrut(5));
				oneday.setVisible(true);
			}else if(dvt>onedayt&&dvt<=twodayt){
				JLabel jb=new JLabel();
				jb.setText("<html><u><i><font color=blue face=\"微软雅黑\">"+us.SimpleString(cn.toString())+"</font></i></u></html>");
				jb.setToolTipText(time.toString()+" "+cn);
				jb.addMouseListener(new SearchEvent());
				twodaypanel.add(jb);
				twodaypanel.add(Box.createVerticalStrut(5));
				twoday.setVisible(true);
			}else if(dvt>twodayt&&dvt<=threedayt){
				JLabel jb=new JLabel();
				jb.setText("<html><u><i><font color=blue face=\"微软雅黑\">"+us.SimpleString(cn.toString())+"</font></i></u></html>");
				jb.setToolTipText(time.toString()+" "+cn);
				jb.addMouseListener(new SearchEvent());
				threedaypanel.add(jb);
				threedaypanel.add(Box.createVerticalStrut(5));
				threeday.setVisible(true);
			}else if(dvt>threedayt&&dvt<=oneweekt){
				JLabel jb=new JLabel();
				jb.setText("<html><u><i><font color=blue face=\"微软雅黑\">"+us.SimpleString(cn.toString())+"</font></i></u></html>");
				jb.setToolTipText(time.toString()+" "+cn);
				jb.addMouseListener(new SearchEvent());
				oneweekpanel.add(jb);
				oneweekpanel.add(Box.createVerticalStrut(5));
				oneweek.setVisible(true);
			}else if(dvt>oneweekt&&dvt<=onemotht){
				JLabel jb=new JLabel();
				jb.setText("<html><u><i><font color=blue face=\"微软雅黑\">"+us.SimpleString(cn.toString())+"</font></i></u></html>");
				jb.setToolTipText(time.toString()+" "+cn);
				jb.addMouseListener(new SearchEvent());
				onemothpanel.add(jb);
				onemothpanel.add(Box.createVerticalStrut(5));
				onemoth.setVisible(true);
			}else if(dvt>onemotht){
				JLabel jb=new JLabel();
				jb.setText("<html><u><i><font color=blue face=\"微软雅黑\">"+us.SimpleString(cn.toString())+"</font></i></u></html>");
				jb.setToolTipText(time.toString()+" "+cn);
				jb.addMouseListener(new SearchEvent());
				morepanel.add(jb);
				morepanel.add(Box.createVerticalStrut(5));
				more.setVisible(true);
			}
			
		}
		

	}

/*	
	public void SetComponent(Object obj){
//2017-11-27
		if(obj instanceof comm.SOLResult){
			this.res=(SOLResult)obj;
		}else if(obj instanceof comm.SOLStar)
			this.star=(SOLStar)obj;
	}
*/	
	public Map<String,List<String>> GetHistory(JPanel jp){
		
		int count = jp.getComponentCount();
		StringBuffer sb=new StringBuffer();
		
		for (int i = 0; i < count; i++) {
		    Object obj = jp.getComponent(i);
		    if (obj instanceof JLabel) {
		        sb.append("Date@"+((JLabel)obj).getName()+" ");
		        sb.append(((JLabel)obj).getText());
		  }
		}
		
		
		return null;
		
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, ParseException { 
		
		JFrame jf=new JFrame();
		Container contentpane=jf.getContentPane();
		contentpane.setLayout(new BorderLayout(0,0));
		
		JPanel jp=new JPanel();
		jp.setBorder(BorderFactory.createLineBorder(Color.red));
		jp.setPreferredSize(new Dimension(750,588));
		
		IOHistory iohis=new IOHistory();
		
		 List<String> input=iohis.HistoryReaderByList("D:\\Lucene\\conf\\history.cf");

		 SOLHistory his=new SOLHistory(null);
	
		JScrollPane sjs=new JScrollPane(his);
		sjs.setViewportView(his);//给textArea创建一个视口 
		
		Date date=new Date(System.currentTimeMillis());
		DateFormat dformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String fdate=dformat.format(file.lastModified());
		String ndate=dformat.format(date);
		
		
		contentpane.add(jp,BorderLayout.WEST);
		contentpane.add(sjs,BorderLayout.EAST);
		
	    jf.setTitle("Searching Of Laws");//窗体标签  
	    jf.setSize(963,588);//窗体大小  
	    jf.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出关闭JFrame  
	    jf.setVisible(true);//显示窗体
	    jf.setResizable(false); //锁定窗体	
		
	}
	

}
