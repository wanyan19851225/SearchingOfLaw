package Com;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.apache.pdfbox.jbig2.util.log.Logger;
//import org.apache.logging.log4j.Level;
//import org.apache.logging.log4j.Level;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class IOPdf {
	public List<String> GetParagraphText(File file) throws IOException{
		List<String> paragraphs=new ArrayList<String>();


		if(file.isFile()){
			String filename=file.getName();
			String filetype=filename.substring(filename.lastIndexOf(".") + 1,filename.length()).toLowerCase();
			List<String> r=new ArrayList<String>();
			if(filetype!=null&&!filetype.equals("")){
				if(filetype.equals("pdf")||filetype.equals("PDF")){  
					 PDDocument doc= PDDocument.load(file);
					 int pages=doc.getNumberOfPages();
					 PDFTextStripperByArea text = new PDFTextStripperByArea();
					 text.setSortByPosition( true );
					 text.setStartPage(1);
			         text.setEndPage(pages);
			         String content = text.getText(doc);
		                System.out.println( content );

//					 System.out.println(text.getParagraphStart());
					 
					 doc.close();
				}
			}
		}
		return paragraphs;
	}
	
	public static void main(String[] args) throws Exception{
		IOPdf pdf=new IOPdf();
		File file=new File("E:\\自己的\\毕业论文\\参考论文2\\论仲裁员履职考评.pdf");
		List<String> p=pdf.GetParagraphText(file);
		for(int i=0;i<p.size();i++){
			System.out.println(p.get(i));
		}
	}
}
