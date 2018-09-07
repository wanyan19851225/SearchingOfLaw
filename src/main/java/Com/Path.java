package Com;

import Demo.Demo;

public interface Path {
	
	javax.swing.filechooser.FileSystemView fsv = javax.swing.filechooser.FileSystemView.getFileSystemView();
	String usrdocment=fsv.getDefaultDirectory().toString();	//获取"我的文档"路径，默认将检索文件存放在"我的文档"目录下
	StringBuilder cpath=new StringBuilder(Demo.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	String currentpath=cpath.toString().substring(0,cpath.toString().lastIndexOf("/")+1);

	public static final String indexpath  = new StringBuilder(usrdocment).append("\\SearchingOfLaw\\index").toString();
	public static final String filepath = new StringBuilder(usrdocment).append("\\SearchingOfLaw\\file").toString();
	public static final String historypath = new StringBuilder(usrdocment).append("\\SearchingOfLaw\\DB\\history.db").toString();
	public static final String userpath = new StringBuilder(usrdocment).append("\\SearchingOfLaw\\DB\\usr.db").toString();
	public static final String urlpath = "http://localhost:8080/swd/ServletDemo";
//	public static final String urlpath = "http://47.97.108.15:8080/swd/ServletDemo";
	public static final String tmpfilepath=new StringBuilder(currentpath).append("TEMP").toString();
	public static final String downloadpath="http://47.97.108.15:8080/swd/download/SearchingOfLaw.exe";
	public static final String upath=new StringBuilder(currentpath).append("SearchingOfLaw_64bit.exe").toString();
}
