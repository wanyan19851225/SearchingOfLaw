package Com;

public interface ResultsType {
	public static final int Index_NONE  = -1;		//文档或网站中没有检索到内容，或文件夹下未找到文档
//	public static final int Index_Exception =-2;		//捕获到异常
	public static final int URL_Format_Error=-3;		//路径格式错误
	public static final int DOC_Already_Exist=-4;		//文档已经存在
	public static final int Directory_NONE=-5;		//目录为空
	public static final int URL_UNArrive=-6;		//网站无法访问
	public static final int DOC_Bad=-7;		//网站无法访问
	
}
