package Com;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/** 
 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
 * All right reserved. 
 * @author: wanyan 
 * date: 2017-10-26 
 */

public class IOWord{	
	private final String SCHAR="[a-zA-Z:/.\"_\\\\　　  ]";
	
	/*
	 * 将doc、docx文件里的全部内容读取到内存
	 *
	 * @params File
	 * 				传入文件
	 * @return
	 * 		  将文件内容存储到List表中
	 * @2017-11-17
	 * 		修复当File是目录时，报错的bug
	 * Modified Date:2017-12-26
	 * 		修复读入流is,docx,doc没有关闭的问题
	 * @Modified Date:2018-8-14
	 * 		新增对软回车字符的判断，如果有软回车字符，则按照软回车字符使用split方法分隔成段落
	 * 
	 */
	public List<String> GetParagraphText(File file) throws IOException{
		List<String> paragraphs=new ArrayList<String>();
		if(file.isFile()){
			String filename=file.getName();
			String filetype=filename.substring(filename.lastIndexOf(".") + 1,filename.length()).toLowerCase();

			FileInputStream is = new FileInputStream(file);
		
			if(filetype!=null&&!filetype.equals("")){
				if(filetype.equals("docx")){
					XWPFDocument docx=new XWPFDocument(is); 	  	      
					List<XWPFParagraph> lip=docx.getParagraphs();

					for(int i=0;i<lip.size();i++){
						String text=lip.get(i).getParagraphText().trim().replaceAll(SCHAR,"" );
						if(!text.contains("")){		//判断是否包含软回车，如果包含软回车，则使用split方法分隔成段落
							if(text.length()>3)
	    							paragraphs.add(text);
						}
						else{
							String[] pa=text.split("");
							for(int j=0;j<pa.length;j++){
								if(pa[j].length()>0)
									paragraphs.add(pa[j]);
							}
						}
					}
					docx.close();
	  	     	
				}else if(filetype.equals("doc")){
	    		
					POIFSFileSystem myFileSystem = new POIFSFileSystem(is);    
					HWPFDocument doc = new HWPFDocument(myFileSystem);
					Range range=doc.getRange();
					
					for(int i=0;i<range.numParagraphs();i++){
						String text=range.getParagraph(i).text().trim().replaceAll(SCHAR,"" );
						if(!text.contains("")){		//判断是否包含软回车，如果包含软回车，则使用split方法分隔成段落
							if(text.length()>3)
	    							paragraphs.add(text);
						}
						else{
							String[] pa=text.split("");
							for(int j=0;j<pa.length;j++){
								if(pa[j].length()>0)
									paragraphs.add(pa[j]);
							}
						}	
					}
					doc.close();
	    		
				}		
			}
			is.close();
		}
	    return paragraphs;    
	}
	
	public Map<Integer,String> CreateIndexOfLaw(File file) throws IOException{
		
		/*
		 * 将doc、docx文件里的全部发条读取到Hashmap里，为每一条发条建立索引值，将法条具体定位到第几章第几节第几条
		 *
		 * @params File
		 * 				传入文件
		 * @return
		 * 		  将法条以key-value的方式存储到HashMap里，方便使用key定位到某条具体的发条
		 * 
		 * @2017-11-2
		 * 		修改bug
		 * 
		 */
		
		List<String> content=this.GetParagraphText(file);
		Map<Integer,String> chapter=new HashMap<Integer,String>();
		Map<Integer,String> section=new HashMap<Integer,String>();
		Map<Integer,String> item=new HashMap<Integer,String>();
		StringBuffer buf=new StringBuffer();
		StringBuffer temp=new StringBuffer();
		UpdateString updatestring=new UpdateString();
		int chapterindex=0,sectionindex=0,itemindex=0,inputitemindex=0,inputchapterindex=0,inputsectionindex=0;
		
		if(content!=null){		
			for(int i=0;i<content.size();i++){
				
				/*** 读取章***/
				
				String temp1=content.get(i);
				System.out.println(temp1);
						
				if(updatestring.IsInTopThree(content.get(i),"章")){
					inputchapterindex=chapterindex;
					chapterindex++;
					chapter.put(chapterindex,content.get(i));	//添加到chapter里
					
	/*** 暂时将读取到的法条追加到temp里，直到当读取到章时，才将暂存的发条添加到item中***/		
					
					if(itemindex!=0){
						inputitemindex=itemindex;
						Integer index=inputchapterindex*100000+sectionindex*1000+inputitemindex;		//计算法条的索引值
//						System.out.println(index+"-"+temp.toString());
						item.put(index,temp.toString());	//添加到tiem里
						buf.delete(0,buf.length());		//清空buf,以继续读取下一个法条
						temp.delete(0,temp.length());	//清空temp，以继续暂存下一个发条
					}
					
					sectionindex=0;		//只要读取到章，就将节索引清零
					itemindex=0;		//只要读取到章，就将法条索引清零
				}
				
				/***读取节***/
				
				else if(updatestring.IsInTopThree(content.get(i),"节")){
					inputsectionindex=sectionindex;
					sectionindex++;
					Integer sindex=chapterindex*100000+sectionindex*1000;		//计算节索引值
					section.put(sindex,content.get(i));			//添加到section里
					
					if(itemindex!=0){
						inputitemindex=itemindex;
						Integer index=chapterindex*100000+inputsectionindex*1000+inputitemindex;
//						System.out.println(index+"-"+temp.toString());
						item.put(index,temp.toString());
						buf.delete(0,buf.length());
						temp.delete(0,temp.length());
					}
					
					itemindex=0;
				}
				else if(updatestring.IsInTopThree(content.get(i),"条")){
					inputitemindex=itemindex;
					itemindex++;
					if(itemindex!=1){		//非第一个发条走此分支
						Integer index=chapterindex*100000+sectionindex*1000+inputitemindex;
//						System.out.println(index+"-"+temp.toString());
						item.put(index,temp.toString());
						buf.delete(0,buf.length());
						temp.delete(0,temp.length());
					}
					
					/***章下面的第一个发条（没有节）或者节下面的第一个发条走此分支***/
					
					buf.append(content.get(i));
					temp.append(buf);
				}else if(!updatestring.IsInTopThree(content.get(i),"章")&&!updatestring.IsInTopThree(content.get(i),"节")&&!updatestring.IsInTopThree(content.get(i),"条")){
					
					/***发条下面的段落，属于同一发条的走此分支***/
					
					if(itemindex!=0){
						buf.append(content.get(i));	//追加发条段落
						temp.replace(0,temp.length(),buf.toString());	//覆盖原temp内容
					}
				}
				
				if(i==content.size()-1){	//文档的最后一个发条走此分支
					inputitemindex=itemindex;
					Integer index=chapterindex*100000+sectionindex*1000+inputitemindex;
//					System.out.println(index+"-"+temp.toString());
					item.put(index,temp.toString());
					buf.delete(0,buf.length());
					temp.delete(0,temp.length());
				}
				
			}		
			return item;
		}
		else
			return null;
		
	}
	
	/*
	 * 将doc、docx文件里的全部发条读取到Hashmap里，为每一条发条建立索引值，将法条具体定位到第几章第几节第几条
	 *
	 * @params File
	 * 				传入文件
	 * @return
	 * 		  将法条以key-value的方式存储到HashMap里，方便使用key定位到某条具体的发条
	 * 
	 * @2017-11-2
	 * 		修改bug
	 * @2017-11-4
	 * 		修改判断为IsInTop
	 * @2017-11-6
	 * 		增加判断，将读取到的法条存储到中文索引，每次读取法条判断中文索引中是否已经存在，如果存在则作为新法条，将上一法条保存，如果不存在，则追加到上一法条的内容中
	 * @2017-12-19
	 * 		增加判断，当读取到法条时，判断法条的位置是否在第一章之前，如果在第一章之前读取到的法条，则丢弃不存储
	 * Modefied Date:2018-1-3
	 * 		增加"第"和"章"之间字符的判断，判读是否含有"条"字符，如果含有"条"字符，则不按照"章"存储
	 * 
	 */

	public Map<Integer,String> GetIndexOflaw(File file) throws IOException{
		
		List<String> content=this.GetParagraphText(file);
		Map<Integer,String> chapter=new HashMap<Integer,String>();
		Map<Integer,String> section=new HashMap<Integer,String>();
		Map<Integer,String> item=new HashMap<Integer,String>();
		Map<String,String> legal=new HashMap<String,String>();
		StringBuffer buf=new StringBuffer();
		StringBuffer temp=new StringBuffer();
		UpdateString updatestring=new UpdateString();
		int chapterindex=0,sectionindex=0,itemindex=0,inputitemindex=0,inputchapterindex=0,inputsectionindex=0;
		
		if(!content.isEmpty()){		
			for(int i=0;i<content.size();i++){
				
				String temp1=content.get(i);
				
				/*** 读取章***/
						
				if(updatestring.IsInTop(temp1,"章")&&!updatestring.GetStringBetween(temp1,"章").contains("条")){
					
					if(updatestring.GetStringBetween(temp1,"章").equals("一"))	//针对文档中有目录的情况，当读取到正文中的第一章时，章索引号清零，重新开始计数索引号
						chapterindex=0;
					
					inputchapterindex=chapterindex;
					chapterindex++;
					chapter.put(chapterindex,content.get(i));	//添加到chapter里
					

					
	/*** 暂时将读取到的法条追加到temp里，直到当读取到章时，才将暂存的发条添加到item中***/		
					
					if(itemindex!=0){
						inputitemindex=itemindex;
						Integer index=inputchapterindex*100000+sectionindex*1000+inputitemindex;		//计算法条的索引值
//						System.out.println(index+"-"+temp.toString());
						item.put(index,temp.toString());	//添加到tiem里
						buf.delete(0,buf.length());		//清空buf,以继续读取下一个法条
						temp.delete(0,temp.length());	//清空temp，以继续暂存下一个发条
					}
					
					sectionindex=0;		//只要读取到章，就将节索引清零
					itemindex=0;		//只要读取到章，就将法条索引清零
				}
				
				/***读取节***/
				
				else if(updatestring.IsInTop(temp1,"节")){
					inputsectionindex=sectionindex;
					sectionindex++;
					Integer sindex=chapterindex*100000+sectionindex*1000;		//计算节索引值
					section.put(sindex,content.get(i));			//添加到section里
					
					if(itemindex!=0){
						inputitemindex=itemindex;
						Integer index=chapterindex*100000+inputsectionindex*1000+inputitemindex;
//						System.out.println(index+"-"+temp.toString());
						item.put(index,temp.toString());
						buf.delete(0,buf.length());
						temp.delete(0,temp.length());
					}
					
					itemindex=0;
				}
				else if(updatestring.IsInTop(temp1,"条")&&chapterindex!=0){		//增加chapterindex章索引号的判断，当章索引号为零时，即第一章之前读取到的法条不走此分支
					inputitemindex=itemindex;
					itemindex++;
					boolean f=legal.containsKey(updatestring.GetStringBetween(temp1,"条"));
					if(itemindex!=1){		//非第一个法条走此分支
						if(!f){		//判断法条的中文索引里是否已经有该法条，如果没有则作为新法条，走此分支，并将上一个法条存到法条索引中，如果有，则追加到到上一法条，成为上一法条内容
							Integer index=chapterindex*100000+sectionindex*1000+inputitemindex;
//							System.out.println(index+"-"+temp.toString());
							item.put(index,temp.toString());
							buf.delete(0,buf.length());
							temp.delete(0,temp.length());
						}else
							itemindex=itemindex-1;		//如果已经存在在法条中文索引中，则追加上一个法条中，法条索引号减1，回退索引号
						
					}
					
					/***章下面的第一个发条（没有节）或者节下面的第一个发条走此分支***/
					
					buf.append(temp1);
					if(f)	
						temp.replace(0,temp.length(),buf.toString());
					else
						temp.append(buf);
					legal.put(updatestring.GetStringBetween(temp1,"条"),"");		//将法条的中文索引存储到内存
				}else if(!updatestring.IsInTop(temp1,"章")&&!updatestring.IsInTop(temp1,"节")&&!updatestring.IsInTop(temp1,"条")){
					
					/***法条下面的段落，属于同一发条的走此分支***/
					
					if(itemindex!=0){
						buf.append(temp1);	//追加发条段落
						temp.replace(0,temp.length(),buf.toString());	//覆盖原temp内容
					}
				}
				
				if(i==content.size()-1){	//文档的最后一个发条走此分支
					inputitemindex=itemindex;
					Integer index=chapterindex*100000+sectionindex*1000+inputitemindex;
//					System.out.println(index+"-"+temp.toString());
					item.put(index,temp.toString());
					buf.delete(0,buf.length());
					temp.delete(0,temp.length());
				}
				
			}		
			return item;
		}
		else
			return item;
	}
	
	
	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-7 
	 * 
	 * 将doc、docx文件里起始位置标有$的段落读取到Hashmap里，主要用来读取非正规格式的法条文档
	 *
	 * @params File
	 * 				传入文件
	 * @return
	 * 		  将法条以key-value的方式存储到HashMap里，方便使用key定位到某条具体的法条
	 * 
	 * Modefied Date:2017-12-26
	 * 		修改方法名称，原方法名称：GetIndexOfdocment,修改后：GetIndexOfmarkdocment
	 * 
	 */
	
	public Map<Integer,String> GetIndexOfmarkdocment(File file) throws IOException{
		
		List<String> content=this.GetParagraphText(file);
		Map<Integer,String> item=new HashMap<Integer,String>();
		UpdateString updatestring=new UpdateString();
		int itemindex=0;
		
		if(!content.isEmpty()){		
			for(int i=0;i<content.size();i++){
				
				itemindex++;
				String temp1=content.get(i);
				int index=0*100000+0*1000+itemindex;
				
				if(updatestring.IsFirst$(temp1,"$"))
					item.put(index,temp1);
			}
		}
		return item;	
	}

	public Map<Integer,String> GetIndexOfgeneraldocment(File file) throws IOException{
		
		List<String> content=this.GetParagraphText(file);
		Map<Integer,String> item=new HashMap<Integer,String>();
		int itemindex=0;
	
		if(!content.isEmpty()){		
			for(int i=0;i<content.size();i++){				
				itemindex++;
				String temp1=content.get(i);
				int index=0*100000+0*1000+itemindex;
				item.put(index,temp1);
			}
		}	
		return item;	
	}

	public static void main(String[] args) throws Exception{
		
		IOWord word=new IOWord();
//		UpdateString us=new UpdateString();
		
//		List<String> paragraphs=new ArrayList<String>();
//		
//		FileInputStream is = new FileInputStream(new File("D:\\Lucene\\src\\北京市劳动局关于转发劳动部办公厅关于职工应征入伍后与企业劳动关系的复函》的通知.doc"));
//		POIFSFileSystem myFileSystem = new POIFSFileSystem(is);    
//		HWPFDocument doc = new HWPFDocument(myFileSystem);
//		Range range=doc.getRange();
//		
//		for(int i=0;i<range.numParagraphs();i++){
//			String text=range.getParagraph(i).text().trim();
//			text=text.replaceAll("[a-zA-Z:/.\"_\\\\　　 ]","" );
//			if(!text.contains("")){
//				if(text.length()>3)
//					paragraphs.add(text);
//			}
//			else{
//				String[] pa=text.split("");
//				for(int j=0;j<pa.length;j++){
//					if(pa[j].length()>0)
//						paragraphs.add(pa[j]);
//				}
//			}
//		}
//		doc.close();
		
		File file=new File("D:\\Lucene\\src\\北京市劳动局关于转发劳动部《关于发布〈企业职工患病或非因病负伤医疗期规定〉的通知》的通知.doc");//劳动人事争议仲裁办案规则（新）.doc   中华人民共和国劳动合同法.doc  （资料）变更劳动合同.doc  中华人民共和国劳动法.doc 北京市劳动局关于解除劳动合同计发经济补偿金有关问题处理意见的通知.doc
//		
//		
		List<String> paragraphs=word.GetParagraphText(file);
		
		for(int i=0;i<paragraphs.size();i++){
//			System.out.println(text.get(i));
//			String rgex="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】'；：”“’。，、？]";
			String str=paragraphs.get(i).replaceAll("[a-zA-Z:/.\"_\\\\　　]","" );//
			System.out.println(str);
//			System.out.println(str+us.IsInTop(str,"章")+us.CalChars(str, "章")+":"+us.GetStringBetween(str, "章"));		
		}
		
		
		
//		Map<Integer,String> item=word.GetIndexOflaw(file);
//		Map<Integer,String> item=word.GetIndexOfnotice(file);
//		Map<Integer,String> item=word.GetIndexOfmarkdocment(file);
//		
//		
//		System.out.println(item.get(301006));
//		
//		for (Integer key : item.keySet()) 
//			
//			System.out.println(key+"-"+item.get(key));
//	
//		
//		System.out.println(item.size());
//		
	}

}
