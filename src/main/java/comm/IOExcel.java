package comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mysql.jdbc.Connection;

public class IOExcel {
	
	
	public void MakeExcel(Workbook book,String filename){

		try{		
			FileOutputStream outputStream=new FileOutputStream(filename,true);
			book.write(outputStream);       
			outputStream.flush();        
			outputStream.close();        
			book.close();
		}catch (FileNotFoundException e) {
			   e.printStackTrace();
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void WriteCell(Workbook wb, Row row, int column,String value, Font font) {  
			
		Cell cell = row.createCell(column);  
		
		cell.setCellValue(value);  
		
		CellStyle cellStyle = wb.createCellStyle();  
			
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
			
		cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);  
			
		cellStyle.setFont(font);  
			
		cell.setCellStyle(cellStyle); 
		
}
	
    @SuppressWarnings("deprecation")
	private Font CreateFonts(Workbook wb, boolean bold, String fontName,boolean isItalic, short hight){  

    	Font font = wb.createFont();  

    	font.setFontName(fontName);  

    	font.setBold(bold);  

    	font.setItalic(isItalic);  

    	font.setFontHeight(hight);  

    	return font;  

    }  

    public Workbook CreatExcelWorkbook(){
    	
    	Workbook book = null;  
  		book = new XSSFWorkbook();
        return book;
        
    }
    
    public void WriteTopRow(Workbook book,Row row,Map<Integer,String>title,Font font){
    	
    	if(row!=null){
			
			for (Map.Entry<Integer, String> entry : title.entrySet()){
				
				IOExcel.WriteCell(book,row,entry.getKey(),entry.getValue(),font);
			}	
		}
    }
    
    public Row CreateRow(Sheet sheet,int i){  	
		Row row=sheet.createRow(i);
    	return row;	
    }
    
    public Sheet CreateSheet(Workbook book,String sheetname){
    	Sheet sheet = book.createSheet(sheetname);
    	return sheet;
    }
    
    public void UpdateExcel(String filename,String value) throws FileNotFoundException{
    	
    	FileInputStream outputStream=new FileInputStream(filename);
    	Workbook book = null;
    	book.getCellStyleAt(0);
    }
	
	  public static void main(String[] args) throws Exception{
		  
		  Map<Integer,String> title=new HashMap<Integer,String>();
	  
		  IOExcel excel = new IOExcel();
		  IOMysql mysql=new IOMysql();
  
		  Workbook book=excel.CreatExcelWorkbook();				//创建工作簿
		  
		  Sheet sheet=excel.CreateSheet(book,"testdatas");		//创建sheet页
		
		  Font font=excel.CreateFonts(book,true,"宋体",false,(short)200);		//设置字体格式
		  
		  Row row=excel.CreateRow(sheet,0);				//创建行
		  	  
		  java.sql.Connection connect=mysql.ConnectMysql("com.mysql.jdbc.Driver","jdbc:mysql://192.168.1.31:3306/BACY?useSSL=false","root","bd@bdht.C0M");		//连接数据库

		  String sql=mysql.QuerySql(args,"BACY_DEVICE","imei=862623030095703");			//拼接sql查询语句
		  
		  System.out.println(sql.toString());
	      
          ResultSet rs=mysql.QueryMysql(connect,sql.toString());		//执行查询语句
          
          title=mysql.GetFeild(connect, sql.toString());		//获取数据库查询结果的字段名称
	      				  
		  excel.WriteTopRow(book,row,title,font);		//写入首行
		  
		  int i=0;		  
		  while(rs.next()){
			  i++;
			  Row drow=excel.CreateRow(sheet,i);
			 
			  for (Map.Entry<Integer, String> entry : title.entrySet())  
				  excel.WriteCell(book,drow,entry.getKey(),rs.getString(entry.getValue()),font);		//写入数据
				  
		  }
		  
		  excel.MakeExcel(book,"E:\\hello.xlsx");			//写入excel文件
		
}
	  
	

}
