package comm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/*
 * 
 * 
 * 
 * 
 * "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; AcooBrowser; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
 *"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Acoo Browser; SLCC1; .NET CLR 2.0.50727; Media Center PC 5.0; .NET CLR 3.0.04506)",
*"Mozilla/4.0 (compatible; MSIE 7.0; AOL 9.5; AOLBuild 4337.35; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
*"Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)",
*"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 2.0.50727; Media Center PC 6.0)",
*"Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 1.0.3705; .NET CLR 1.1.4322)",
"Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 5.2; .NET CLR 1.1.4322; .NET CLR 2.0.50727; InfoPath.2; .NET CLR 3.0.04506.30)",
"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN) AppleWebKit/523.15 (KHTML, like Gecko, Safari/419.3) Arora/0.3 (Change: 287 c9dfb30)",
"Mozilla/5.0 (X11; U; Linux; en-US) AppleWebKit/527+ (KHTML, like Gecko, Safari/419.3) Arora/0.6",
"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.2pre) Gecko/20070215 K-Ninja/2.1.1",
"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9) Gecko/20080705 Firefox/3.0 Kapiko/3.0",
"Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5",
"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.8) Gecko Fedora/1.9.0.8-1.fc10 Kazehakase/0.5.6",
"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.20 (KHTML, like Gecko) Chrome/19.0.1036.7 Safari/535.20",
"Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52",
"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11",
"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER",
"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; LBBROWSER)",
"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E; LBBROWSER)",
"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 LBBROWSER",
"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)",
"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; QQBrowser/7.0.3698.400)",
"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; 360SE)",
"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)",
"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1",
"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1",
"Mozilla/5.0 (iPad; U; CPU OS 4_2_1 like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5",
"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:2.0b13pre) Gecko/20110307 Firefox/4.0b13pre",
"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:16.0) Gecko/20100101 Firefox/16.0",
"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11",
"Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10",
"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36",
 * 
 * 
 * 
 */

public class IOHtml {
	

	public Document GetDocoument(String url) throws IOException{
		
		Connection conn=Jsoup.connect(url);
		conn.header("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9) Gecko/20080705 Firefox/3.0 Kapiko/3.0");// 设置 User-Agent
		
		Document doc=conn.timeout(3000).get();
		return doc;
	}
	
	public List<String> GetHtmlP(String url) throws IOException{
		List<String> paragraphs=new ArrayList<String>();
		Document doc=this.GetDocoument(url);
		Elements items =doc.getElementsByTag("p");
		if(!items.isEmpty()){
			for(Element item:items){
				String text=item.text().trim();
				text=text.replaceAll("[a-zA-Z:/.\"_\\\\　　]","" );
				if(text.length()>3)
						paragraphs.add(text);
			}	
		}
		else
			paragraphs=null;
		return paragraphs;
	}
	
	public void GetHtmlTitle(String url) throws IOException{
		Document doc=this.GetDocoument(url);
		Elements items;
		String t=doc.select("meta[name=WebId]").attr("content");

		if(!t.equals("")){
	    		System.out.println(t);
			}
		else{
			items =doc.getElementsByTag("subtitle");
			if(!items.isEmpty()){
				StringBuffer buffer =new StringBuffer();
		    		for(Element item:items){
		    			buffer.append(item.text().trim());
		    			buffer.append("\r\n");
		    		}
		    		System.out.println(buffer.toString());
				}
			else{
				items =doc.getElementsByTag("title");
				if(!items.isEmpty()){
					StringBuffer buffer =new StringBuffer();
			    		for(Element item:items){
			    			buffer.append(item.text().trim());
			    			buffer.append("\r\n");
			    		}
			    		System.out.println(buffer.toString());
					}
				else
					System.out.println("未解析到文章标题");
			}
		}
	}
	
	public void GetHtmlH(String url) throws IOException{
		Document doc=this.GetDocoument(url);
		Elements items;
		items=doc.getElementsByTag("h1");
		if(!items.isEmpty()){
			StringBuffer buffer =new StringBuffer();
	    		for(Element item:items){
	    			buffer.append(item.text().trim());
	    			buffer.append("\r\n");
	    		}
	    		System.out.println(buffer.toString());
			}
		else{
			items=doc.getElementsByTag("h2");
			if(!items.isEmpty()){
				StringBuffer buffer =new StringBuffer();
		    		for(Element item:items){
		    			buffer.append(item.text().trim());
		    			buffer.append("\r\n");
		    		}
		    		System.out.println(buffer.toString());
				}
			else{
				items=doc.getElementsByTag("h3");
				if(!items.isEmpty()){
					StringBuffer buffer =new StringBuffer();
			    		for(Element item:items){
			    			buffer.append(item.text().trim());
			    			buffer.append("\r\n");
			    		}
			    		System.out.println(buffer.toString());
					}
				else{
					items=doc.getElementsByTag("h4");
					if(!items.isEmpty()){
						StringBuffer buffer =new StringBuffer();
				    		for(Element item:items){
				    			buffer.append(item.text().trim());
				    			buffer.append("\r\n");
				    		}
				    		System.out.println(buffer.toString());
						}
					else{
						items=doc.getElementsByTag("h5");
						if(!items.isEmpty()){
							StringBuffer buffer =new StringBuffer();
					    		for(Element item:items){
					    			buffer.append(item.text().trim());
					    			buffer.append("\r\n");
					    		}
					    		System.out.println(buffer.toString());
							}
						else{
							items=doc.getElementsByTag("h6");
							if(!items.isEmpty()){
								StringBuffer buffer =new StringBuffer();
						    		for(Element item:items){
						    			buffer.append(item.text().trim());
						    			buffer.append("\r\n");
						    		}
						    		System.out.println(buffer.toString());
								}
							else
								System.out.println("未解析到文章标题");
						}
							
					}
						
				}
			}
		}
			
	}
	
	public Map<Integer,String> GetIndexOflaw(String url) throws IOException{
		
		List<String> content=this.GetHtmlP(url);
		Map<Integer,String> chapter=new HashMap<Integer,String>();
		Map<Integer,String> section=new HashMap<Integer,String>();
		Map<Integer,String> item=new HashMap<Integer,String>();
		Map<String,String> legal=new HashMap<String,String>();
		StringBuffer buf=new StringBuffer();
		StringBuffer temp=new StringBuffer();
		UpdateString updatestring=new UpdateString();
		int chapterindex=0,sectionindex=0,itemindex=0,inputitemindex=0,inputchapterindex=0,inputsectionindex=0;
		
		if(content!=null){		
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
			return null;

	}
	
	
	public static void main(String[] args) throws Exception{
		//IOSpider IOHtml=new IOHtml("http://www.panda.tv/agreement.html");
		IOHtml html=new IOHtml();
		//html.GetHtmlP();
		//html.GetHtmlTitle();
		//html.GetHtmlH();
		
		//List<String> text=html.GetHtmlP("http://www.chinalaw.gov.cn/art/2018/6/20/art_11_208525.html");
		
		//for(int i=0;i<text.size();i++){
//			System.out.println(text.get(i));
//			String rgex="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】'；：”“’。，、？]";
//			String str=text.get(i).replaceAll("[a-zA-Z:/.\"_\\\\　　]","" );
			//String str=text.get(i);
			//System.out.println(str);		
		//}
		
		Map<Integer,String> item=html.GetIndexOflaw("http://www.chinalaw.gov.cn/art/2018/6/20/art_11_208525.html");
		for (Integer key : item.keySet()) 
			
			System.out.println(key+"-"+item.get(key));
	
		
		System.out.println(item.size());
	}

}


