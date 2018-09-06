package Com;

import Demo.Demo;

public interface Path {
	
	javax.swing.filechooser.FileSystemView fsv = javax.swing.filechooser.FileSystemView.getFileSystemView();
	String usrdocment=fsv.getDefaultDirectory().toString();	//获取"我的文档"路径，默认将检索文件存放在"我的文档"目录下
	StringBuilder currentpath=new StringBuilder(Demo.class.getProtectionDomain().getCodeSource().getLocation().getPath());

	public static final String indexpath  = usrdocment+"\\SearchingOfLaw\\index";
	public static final String filepath = usrdocment+"\\SearchingOfLaw\\file";
	public static final String historypath = usrdocment+"\\SearchingOfLaw\\DB\\history.db";
	public static final String userpath = usrdocment+"\\SearchingOfLaw\\DB\\usr.db";
	public static final String urlpath = "http://localhost:8080/swd/ServletDemo";
//	public static final String urlpath = "http://47.97.108.15:8080/swd/ServletDemo";
	public static final String tmpfilepath=currentpath.append("TEMP").toString();
	public static final String downloadpath="http://47.97.108.15:8080/swd/download/SearchingOfLaw.exe";
	public static final String upath=currentpath.append("SearchingOfLaw_64bit.exe").toString();
}
