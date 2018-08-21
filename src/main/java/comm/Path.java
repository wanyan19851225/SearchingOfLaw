package comm;

public interface Path {
	
//	javax.swing.filechooser.FileSystemView fsv = javax.swing.filechooser.FileSystemView.getFileSystemView();
//	String usrdocment=fsv.getDefaultDirectory().toString();	//获取"我的文档"路径，默认将检索文件存放在"我的文档"目录下
	
	public static final String indexpath  = "D:\\Lucene\\index\\";
//	public static final String indexpath  = usrdocment+"\\SearchingOfLaw\\index";
//	public static final String confpath = "D:\\Lucene\\conf\\";
	public static final String historypath = "D:\\Lucene\\conf\\history.db";
	public static final String userpath = "D:\\Lucene\\conf\\usr.db";
//	public static final String urlpath = "http://192.168.1.156:8080/swd/ServletDemo";
//	public static final String urlpath = "http://localhost.:8080/swd/ServletDemo";
//	public static final String urlpath = "http://101.132.166.3:8080/swd/ServletDemo";
	public static final String urlpath = "http://47.97.108.15:8080/swd/ServletDemo";
	//public static final String commitindeurlpath = "http://192.168.1.138:8080/swd/CommitIndex";
	//public static final String selectindeurlpath = "http://192.168.1.138:8080/swd/SelectIndex";
	//public static final String repositorypath  = "D:\\Lucene\\RemoteRepository\\index";

}
