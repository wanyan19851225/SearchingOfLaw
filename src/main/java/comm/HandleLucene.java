package comm;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.index.TieredMergePolicy;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;


/** 
 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
 * All right reserved. 
 * @author: wanyan 
 * date: 2017-10-27 
 */

public class HandleLucene {
	
	private static IndexReader indexreader;
	private static FSDirectory fsdir;
	private static RAMDirectory ramdir;
	private static IndexWriter indexwriter;
	private static IndexWriter ramwriter;
	
	/*
	 * 扫描文件，建立索引文件
	 *
	 * @params fdir
	 * 				文件夹
	 * 		   indexpath
	 * 				索引文件存储位置
	 * @return Integer	
	 * 				返回所有文件的索引数
	 * 
	 * @2017-11-1
	 * 			修改返回参数为Integer,返回所有文件的索引数
	 * @2017-11-7
	 * 			增加对自定义文档和正式格式法条的判断  
	 * @2017-11-10
	 *          修改file字段的索引选项，由IndexOptions.NONE改为IndexOptions.DOCS,不建立索引改为只对doc建立索引，这样能够根据file字段删除索引
	 * @2017-11-17
	 * 			修复当文件夹中没有法条文档时，创建空索引的bug
	 * 			增加调用GetIndexOflaw或者GetIndexOfdocment后，是否为空的判断，如果为空则跳过不建立索引
	 *			增加对path字段，增加NumericDocValuesField字段，用于排序
	 * Modeified Date:2017-12-24
	 * 			修改合并策略TieredMergePolicy，将删除索引时的默认合并策略值10%，修改为0，由于删除索引的默认合并策略10%，即删除的索引数达到总索引数(maxDo)的10%时，才会执行forceMergeDeletes
	 * Modiefied Date:2017-12-26
	 * 			增加读取法条文档时，读取全部内容，按照段落存储法条
	 * Modefied Date:2018-1-2
	 * 			将indexwriter修改为类的私有类，以调用IndexReader的单例函数openifchange(Directory,IndexWriter方法)
	 * 			将indexwriter更改为静态类，在创建前判断indexwriter是否处于打开状态，如果打开，则关闭以释放资源
	 *          
	 */
	/*
	public Integer CreateIndex(String fdir,String indexpath) throws IOException{
		
		File[] files = new File(fdir).listFiles();
		
		int filesnum=files.length;
		
		int totalofindex=0;
		
		if(filesnum!=0){		//判断文件夹下是否有法条文档
		
			Path inpath=Paths.get(indexpath);
				
			Analyzer analyzer = new StandardAnalyzer();		//创建标准分词器
	
			FSDirectory fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
		
			RAMDirectory ramdir=new RAMDirectory();		//创建内存索引文件
	
			IndexWriterConfig ramconfig = new IndexWriterConfig(analyzer);
		
			IndexWriter ramiwriter = new IndexWriter(ramdir,ramconfig);		//创建内存IndexWriter
		
			IOWord ioword=new IOWord();
		
			for(int i=0;i<files.length;i++){
			
				String fname=files[i].getName().split("\\.")[0];
				String check=fname.substring(fname.length()-2,fname.length());
				Map<Integer,String> law=new HashMap<Integer,String>();
			
				if(check.contains("法")||check.contains("条例")||check.contains("草案")||check.contains("规则")||check.contains("通则")){
			
					law=ioword.GetIndexOflaw(files[i]);
				}
				else if(check.contains("M")){
					law=ioword.GetIndexOfmarkdocment(files[i]);
				}
				else{
					law=ioword.GetIndexOfgeneraldocment(files[i]);
				}
				
			
				if(totalofindex==-1)
					totalofindex=0;
				
				totalofindex+=law.size();
				
				if(!law.isEmpty()){
			
					for (Integer key:law.keySet()){ 
				
//						String laws=law.get(302011);
				
//						System.out.println(laws);
					
						Document doc=new Document();		//创建Document,每一个发条新建一个		
						FieldType fieldtype=new FieldType();
						fieldtype.setIndexOptions(IndexOptions.DOCS);
						fieldtype.setStored(true);		
						fieldtype.setTokenized(false);
						doc.add(new Field("file",files[i].getName(),fieldtype));		//文档名称存储，不分词
						doc.add(new NumericDocValuesField("path",key));
						doc.add(new IntPoint("path",key));		//法条索引以Int类型存储
						doc.add(new StoredField("path",key));
						doc.add(new Field("law",law.get(key),TextField.TYPE_STORED));		//法条内容索引、分词，存储
		    	
						ramiwriter.addDocument(doc);		//将法条索引添加到内存索引中			  
					}
				
			}
				else if(totalofindex==0)
					totalofindex=-1;
		
			}	
			
			
			ramiwriter.close();
			

			TieredMergePolicy ti=new TieredMergePolicy();
			ti.setForceMergeDeletesPctAllowed(0);		//设置删除索引的合并策略为0，有删除segment时，立即进行合并
			IndexWriterConfig fsconfig=new IndexWriterConfig(analyzer); 
			fsconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			fsconfig.setMergePolicy(ti);		//设置合并策略
//			System.out.println(ti.getForceMergeDeletesPctAllowed());
//			IndexWriter fsiwriter=new IndexWriter(fsdir,fsconfig);   
//			fsiwriter.addIndexes(ramdir); 		//程序结束后，将内存索引写入到磁盘索引中
			
			if(indexwriter!=null){		//判断indexwriter是否处于打开状态，如果是，则关闭已释放资源
				if(indexwriter.isOpen())
					indexwriter.close();
			}
			
			indexwriter=new IndexWriter(fsdir,fsconfig);   
			indexwriter.addIndexes(ramdir); 		//程序结束后，将内存索引写入到磁盘索引中
			
			indexwriter.commit();
			
//			indexwriter.close();
		}
		else
			totalofindex=-1;
        return totalofindex;
	}
	*/
	
	/*
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-1 
	 *
	 *该方法扫描文件，创建索引
	 *该方法是对CreateIndex方法进行优化，增加了使用超链接URL从网站抓取html文档，然后使用Jsoup工具解析html文档中的内容，并为解析的内容建立索引
	 *
	 * @param url
	 * 				文件夹路径或者url地址
	 * 		   indexpath
	 * 				索引文件存储位置
	 * @return Integer	
	 * 				返回所有文件的索引数
	 * 
	 * @2017-11-1
	 * 			修改返回参数为Integer,返回所有文件的索引数
	 * @2017-11-7
	 * 			增加对自定义文档和正式格式法条的判断  
	 * @2017-11-10
	 *          修改file字段的索引选项，由IndexOptions.NONE改为IndexOptions.DOCS,不建立索引改为只对doc建立索引，这样能够根据file字段删除索引
	 * @2017-11-17
	 * 			修复当文件夹中没有法条文档时，创建空索引的bug
	 * 			增加调用GetIndexOflaw或者GetIndexOfdocment后，是否为空的判断，如果为空则跳过不建立索引
	 *			增加对path字段，增加NumericDocValuesField字段，用于排序
	 * Modified Date:2017-12-24
	 * 			修改合并策略TieredMergePolicy，将删除索引时的默认合并策略值10%，修改为0，由于删除索引的默认合并策略10%，即删除的索引数达到总索引数(maxDo)的10%时，才会执行forceMergeDeletes
	 * Modified Date:2017-12-26
	 * 			增加读取法条文档时，读取全部内容，按照段落存储法条
	 * Modified Date:2018-1-2
	 * 			将indexwriter修改为类的私有类，以调用IndexReader的单例函数openifchange(Directory,IndexWriter方法)
	 * 			将indexwriter更改为静态类，在创建前判断indexwriter是否处于打开状态，如果打开，则关闭以释放资源
	 * Modified Date:2018-8-3
	 * 			修改原参数fdir为url，可以传入文件夹路径或者url地址
	 * Modified Date:2018-8-6
	 * 			修改为使用正则表达式过滤传入的url地址是否符合要求
	 * Modified Date:2018-8-7
	 * 			修改为使用IOHtml的构造函数抓取html文档，并捕获异常
	 * Modified Date:2018-08-08
	 * 			删除创建索引方法，统一使用AddIndexs方法添加索引
	 *          
	 */
	
//	public Integer CreateIndexs(String url,String indexpath) throws IOException{
//		int totalofindex=0;
//		Path inpath=Paths.get(indexpath);
//		Analyzer analyzer = new StandardAnalyzer();		//创建标准分词器
//		FSDirectory fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
//		RAMDirectory ramdir=new RAMDirectory();		//创建内存索引文件
//		IndexWriterConfig ramconfig = new IndexWriterConfig(analyzer);
//		IndexWriter ramiwriter = new IndexWriter(ramdir,ramconfig);		//创建内存IndexWriter
//		
//		if(url.matches("(http|https)://.*")){		//判断路径是否以http://开头，如果是，则抓取html文档，解析html文档内容
//			try{
//				IOHtml html=new IOHtml(url);		//使用构造函数抓取html文档，捕捉异常，如果报异常，totalofindex赋值-1，不在建立索引文件
//				Map<Integer,String> law=html.GetIndexOflaw();
//				String fname=html.GetHtmlH();		//解析html文档标题
//				totalofindex=law.size();		//获取读到的法条总数
//
//				if(!law.isEmpty()){		//判断是否读取到了法条，如果有读到法条，则为每条法条创建索引
//					for (Map.Entry<Integer,String> e:law.entrySet()){ 
//						Document doc=new Document();		//创建Document,每一个发条新建一个		
//						FieldType fieldtype=new FieldType();
//						fieldtype.setIndexOptions(IndexOptions.DOCS);
//						fieldtype.setStored(true);		
//						fieldtype.setTokenized(false);
//						doc.add(new Field("file",fname,fieldtype));		//文档名称存储，不分词
//						doc.add(new NumericDocValuesField("path",e.getKey()));
//						doc.add(new IntPoint("path",e.getKey()));		//法条索引以Int类型存储
//						doc.add(new StoredField("path",e.getKey()));
//						doc.add(new Field("law",e.getValue(),TextField.TYPE_STORED));		//法条内容索引、分词，存储
//		    	
//						ramiwriter.addDocument(doc);		//将法条索引添加到内存索引中			  
//					}
//				}
//				else if(totalofindex==0)		//如果没有读到法条，则totalofindex赋值-1
//					totalofindex=-1;
//			}catch(IOException e){		//捕捉异常，如果报异常，totalofindex赋值-2，不在建立索引文件
//				totalofindex=-2;
//			}	
//		}else if(url.matches("[a-zA-Z]:\\\\.*")){			//使用正在表达式判断路径是否是文件目录形式，如C:\,D:\
//			File[] files = new File(url).listFiles();
//			int filesnum=files.length;
//			if(filesnum!=0){		//判断文件夹下是否有法条文档，如果有文档，则为文档创建索引
//				IOWord ioword=new IOWord();
//				for(int i=0;i<files.length;i++){
//					String fname=files[i].getName().split("\\.")[0];		//获取文档名称
//					//String check=fname.substring(fname.length()-2,fname.length());		//截取文档名称的最后两个字符
//					Map<Integer,String> law=new HashMap<Integer,String>();
//					if(fname.matches("[\u4e00-\u9fa5《》]*(法|条例|草案|规则|通则)$")){		//依据文档名称是否以法、条例、草案、规则、通则结尾，判断该文档是否是规范法条文档，如果是则调用GetIndexOflaw方法
//						law=ioword.GetIndexOflaw(files[i]);
//					}
//					else if(fname.matches("[\u4e00-\u9fa5《》]*M$")){		//依据文档名称是否以M结尾，则调用GetIndexOfmarkdocment方法
//						law=ioword.GetIndexOfmarkdocment(files[i]);
//					}
//					else{			//如果文档既不是规范法条文档，也没有M标记，则调用GetIndexOfgeneraldocment方法
//						law=ioword.GetIndexOfgeneraldocment(files[i]);
//					}
//					if(totalofindex==-1)
//						totalofindex=0;
//					
//					totalofindex+=law.size();		//获取读到的法条总数
//					
//					if(!law.isEmpty()){		//判断是否读取到了法条，如果有读到法条，则为每条法条创建索引
//						for (Map.Entry<Integer,String> e:law.entrySet()){ 
//							Document doc=new Document();		//创建Document,每一个发条新建一个		
//							FieldType fieldtype=new FieldType();
//							fieldtype.setIndexOptions(IndexOptions.DOCS);
//							fieldtype.setStored(true);		
//							fieldtype.setTokenized(false);
//							doc.add(new Field("file",files[i].getName(),fieldtype));		//文档名称存储，不分词
//							doc.add(new NumericDocValuesField("path",e.getKey()));
//							doc.add(new IntPoint("path",e.getKey()));		//法条索引以Int类型存储
//							doc.add(new StoredField("path",e.getKey()));
//							doc.add(new Field("law",e.getValue(),TextField.TYPE_STORED));		//法条内容索引、分词，存储
//			    	
//							ramiwriter.addDocument(doc);		//将法条索引添加到内存索引中			  
//						}
//					}
//					else if(totalofindex==0)		//如果没有读到法条，则totalofindex赋值-1
//						totalofindex=-1;
//				}
//			}
//			else		//如果文件夹下没有文档，则totalofindex赋值-1
//				totalofindex=-1;
//			
//		}else{		//路径既不是以http://开头，也不是以文件目录形式，则totalofindex赋值-3
//			totalofindex=-3;
//		}
//		
//		ramiwriter.close();		//法条索引创建完毕，关闭内存IndexWriter
//		TieredMergePolicy ti=new TieredMergePolicy();
//		ti.setForceMergeDeletesPctAllowed(0);		//设置删除索引的合并策略为0，有删除segment时，立即进行合并
//		IndexWriterConfig fsconfig=new IndexWriterConfig(analyzer); 
//		fsconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
//		fsconfig.setMergePolicy(ti);		//设置合并策略
//		if(indexwriter!=null){		//判断indexwriter是否处于打开状态，如果是，则关闭已释放资源
//			if(indexwriter.isOpen())
//				indexwriter.close();
//		}
//		indexwriter=new IndexWriter(fsdir,fsconfig);   
//		indexwriter.addIndexes(ramdir); 		//程序结束后，将内存索引写入到磁盘索引中
//		indexwriter.commit();
//		
//        return totalofindex;
//	}
	
	
	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-10-27 
	 * 
	 * 根据带搜索字符，对扫描文件进行搜索，并将搜索存在HashMap中返回
	 *
	 * @params indexpath
	 * 				索引文件存储位置
	 * 			keywords 
	 * 				从界面获取用户输入的关键字，在索引文件中搜索该keywords
	 * 			top
	 * 				从界面获取用户选择的搜索条数，在索引文件中根据相关度排序，返回前top条
	 * 			
	 * @return Map<String,List<Integer>>
	 * 				将搜索结果以键值对的方式保存到Map中，文件名为Key,法条索引保存到List<Integer>中，作为value		   
	 * 						  
	 * 该方法修改于2017.10.29，添加 keywords和top两个参数
	 * 
	 */
	
	public Map<String,List<Integer>> ExecuteSearch(String indexpath,String keywords,int top) throws IOException, ParseException{
		
		Map<String,List<Integer>> files=new LinkedMap<String,List<Integer>>();
		
		Path inpath=Paths.get(indexpath);
		
		FSDirectory fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
		
		IOContext iocontext=new IOContext();

		RAMDirectory ramdir=new RAMDirectory(fsdir,iocontext);		//创建内存索引文件，并将磁盘索引文件放到内存中
		
		Analyzer analyzer=new StandardAnalyzer();		//创建标准分词器

		IndexReader indexreader=DirectoryReader.open(ramdir);

		IndexSearcher indexsearcher=new IndexSearcher(indexreader);
		
        QueryParser parser=new QueryParser("law", analyzer);
        
        Query query=parser.parse(keywords);
        
        TopDocs topdocs=indexsearcher.search(query,top); 
        
        ScoreDoc[] hits=topdocs.scoreDocs;
        
        int num=hits.length;
        
        if(num==0){
        	return null;
        }
              
        for(int i=0;i<num;i++){
        	
        	Document hitdoc=indexsearcher.doc(hits[i].doc);
        	
    		String temp=hitdoc.get("file");
    		Integer index=Integer.valueOf(hitdoc.get("path"));
        	
        	if(i==0){
        		List<Integer> path=new ArrayList<Integer>();
        		path.add(index);
        		files.put(temp,path);
        	}else{
        		if(files.containsKey(temp)){
        			files.get(temp).add(index);
        		}else{
        			List<Integer> path=new ArrayList<Integer>();
            		path.add(index);
            		files.put(temp,path);
        		}     		
        	}
        }
        indexreader.close();
        fsdir.close();
        return files;		   
	}
	
	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-10-28 
	 * 
	 * 利用ExecuteSearch方法执行返回的Map<文件名，法条索引>，打开相应的文件，生成Map<法条索引，法条内容>映射，读取该Map，生成String[文件名，章，节，法条内容]，存到List中,返回该List,DisplayGui类的home()方法分析该List,显示在JTextArea组件中
	 * 该方法无法使用Lucene的高亮功能，因为JTextArea无法解析HTML格式的文档
	 *
	 * @params resultset
	 * 				<文件名，法条索引>的映射关系
	 * 			fdir
	 * 				传入被搜索文件的目录
	 * 			
	 * @return List<String[]>
	 * 				将搜索结果以[文件名，章，节，法条]的形式存到String[]中，将String[]存到List中，返回List		   
	 * 						  
	 * @2017.10.29
	 * 			修改成：先读取文件目录中的文件，查找和Map<文件名，法条索引>中文件名匹配的文档，再利用法条索引搜索法条内容
	 * 
	 */

//	public List<String[]> GetContentOfLawByIndex(Map<String,List<Integer>> resultset,String fdir) throws IOException{
//		
//		Map<String,List<Integer>> temp=resultset;
//		if(temp==null){
//			return null;
//		}
//		List<String[]> contentoflaw=new ArrayList<String[]>();
//		
//		IOWord iword=new IOWord();
//		
//		File[] files = new File(fdir).listFiles();
//		
///*		
//		for(String key:temp.keySet()){
//			StringBuffer filepath=new StringBuffer(fdir);
//			String filename=filepath.append(key).toString();
//			File file=new File(filename);
//			Map<Integer,String> law=iword.CreateIndexOfLaw(file);
//			List<Integer> indexoflaw=temp.get(key);
//			for(int j=0;j<indexoflaw.size();j++){
//				String[] content=new String[4];
//				content[0]=filename;
//				Integer index=indexoflaw.get(j);
//				content[1]="第"+index/100000+"章";
//				index=index%100000;
//				content[2]="第"+index/1000+"节";
//				content[3]=law.get(indexoflaw.get(j));
//				contentoflaw.add(content);//can i help you?呵呵，你们还没下班啊。正准备走呢，看你电脑自己在动！我电脑不是黑屏待机么，怎么亮了，奇怪。不晓得呀！bye。王老师嗯，好的
//			}
//			filepath.delete(0,filepath.length());
//		}
//*/	
//
//		for(int i=0;i<files.length;i++){
//			String filename=files[i].getName();
//			if(temp.containsKey(filename)){
//				Map<Integer,String> law=iword.CreateIndexOfLaw(files[i]);
//				List<Integer> indexoflaw=temp.get(filename);
//				for(int j=0;j<indexoflaw.size();j++){
//					String[] content=new String[4];
//					content[0]=new UpdateString().addBookTitleMark(filename);
//					Integer index=indexoflaw.get(j);
//					content[1]="第"+index/100000+"章";
//					index=index%100000;
//					content[2]="第"+index/1000+"节";
//					content[3]=law.get(indexoflaw.get(j));
//					contentoflaw.add(content);
//				}
//			}
//			
//		}
//		return contentoflaw;		
//	}
	
	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-10-29 
	 * 
	 * 该方法与使用单一查询方式
	 *
	 * @params indexpath
	 * 				索引文件所在目录
	 * 			keywords 
	 * 				从JTextField获取用户输入的关键字
	 * 			top
	 * 				从JRadio获取用户选择的搜索条数，在索引文件中根据相关度排序，返回前top条
	 * 			
	 * @return Map<Stirng,List<String[]>>
	 * 				将搜索结果以Map<文件名，[章节，法条]>的映射关系，返回查询结果		   
	 * 						  
	 * @2017-10-31
	 * 				修改高亮截取的默认字符数，修改为根据法条内容的字符数显示
	 * Modiefied Date:2017-12-25
	 * 				取消top参数，改为默认使用索引文档中有效索引文档总数
	 * Modiefied Date:2017-12-26
	 * 				修改当索引文件有效文档数为0时，报错的bug
	 * Modiefied Date:2018-1-23
	 * 				删除捕捉IndexNotFoundException后，弹出提示框
	 * Modified Date:2018-8-9
	 * 				判断章段落索引号是否为999，如果是，则表明没有章段落，章段落索引号设置为0
	 * 
	 */
	
	public Map<String,List<String[]>> GetSearch(String indexpath,String keywords) throws ParseException, IOException, InvalidTokenOffsetsException{
		
		Map<String,List<String[]>> files=new LinkedMap<String,List<String[]>>();
		
		try{
		
			this.CreateIndexReader(indexpath);
			
				if(indexreader!=null){	
					Analyzer analyzer=new StandardAnalyzer();		//创建标准分词器
					IndexSearcher indexsearcher=new IndexSearcher(indexreader);
				//		Term term=new Term("law",keywords);
				//		TermQuery termquery=new TermQuery(term);
				//		模糊查询		
				//		FuzzyQuery fuzzquery=new FuzzyQuery(term);
				// 		短语查询		
				//		 PhraseQuery.Builder builder = new PhraseQuery.Builder();
				//		 builder.add(term);
				//		 PhraseQuery phrasequery=builder.build();
				//		查询分析器	
			
					QueryParser parser=new QueryParser("law", analyzer);
           
					Query query=parser.parse(keywords.toString());
        
					int top=indexreader.numDocs();		//获取索引文件中有效文档总数
       
					if(top==0)	//判断索引文件中的有效文档总数是否为0，如果为零则退出该方法，返回null
						files=null;
					else{
						TopDocs topdocs=indexsearcher.search(query,top); 
        
						ScoreDoc[] hits=topdocs.scoreDocs;
        
						int num=hits.length;
        
						if(num==0){
							return null;
						}
        
					//此处加入的是搜索结果的高亮部分
						SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color=red>","</font></b>"); //如果不指定参数的话，默认是加粗，即<b><b/>
						QueryScorer scorer = new QueryScorer(query);//计算得分，会初始化一个查询结果最高的得分
					//			Fragmenter fragmenter = new SimpleSpanFragmenter(scorer); //根据这个得分计算出一个片段
						Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
					//      	 highlighter.setTextFragmenter(fragmenter); //设置一下要显示的片段		
						for(int i=0;i<num;i++){	
							Document hitdoc=indexsearcher.doc(hits[i].doc);
							String temp=hitdoc.get("file");
							String indexlaws[]=new String[2];
							Integer index=Integer.valueOf(hitdoc.get("path"));		
				    		if(index/100000==999)		//判断章段落的索引号是否为999,当索引号为999时，表明没有章段落，索引号设置为0
				    			indexlaws[0]="第"+0+"章"+"&emsp";
				    		else
				    			indexlaws[0]="第"+index/100000+"章"+"&emsp";
							index=index%100000;
							indexlaws[0]+="第"+index/1000+"节";
							String laws=hitdoc.get("law");
							if(laws!=null){
								TokenStream tokenStream = analyzer.tokenStream("laws",new StringReader(laws));
								Fragmenter displaysize= new SimpleFragmenter(laws.length());
								highlighter.setTextFragmenter(displaysize);
								String highlaws=highlighter.getBestFragment(tokenStream,laws);
								indexlaws[1]=highlaws;
								if(i==0){
									List<String[]> path=new ArrayList<String[]>();
									path.add(indexlaws);
									files.put(temp,path);
								}else{
									if(files.containsKey(temp)){
										files.get(temp).add(indexlaws);
									}else{
										List<String[]> path=new ArrayList<String[]>();
										path.add(indexlaws);
										files.put(temp,path);
									}     		           	 			
								}
							}
						}
					}

				//      ramdir.close();
				//      indexreader.close();
				//      fsdir.close();
				}	
		}catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
		}
        return files;
		
	}

	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-10-31 
	 * 
	 * 该方法与GetSearch功能一致，直接返回法条内容，只是可以使用多条件、多域进行查询
	 *
	 * @params indexpath
	 * 				索引文件所在目录
	 * 			keywords 
	 * 				从JTextField获取用户输入的多个关键字,使用String[]方式传递，[关键字，查询条件]
	 * 			top
	 * 				从JRadio获取用户选择的搜索条数，在索引文件中根据相关度排序，返回前top条
	 * 			
	 * @return Map<Stirng,List<String[]>>
	 * 				将搜索结果以Map<文件名，[章节，法条]>的映射关系，返回查询结果		   
	 * 						  
	 * @2017-10-31
	 * 			修改使用BooleanQuery方式实现多条件查询
	 * @2017-11-1
	 * 			修改同时支持单条件查询和多条件查询
	 * 			修改使用查询分析器QueryParser实现多域、多字段、多条件查询
	 * @2017-11-16
	 * 			修改使用MultiFieldQueryParser类实现多字段、多条件查询
	 * @2018-8-9
	 * 			修改判断章段落索引号，如果章段落索引号为999时，标明没有章段落
	 * 
	 */
	
	public Map<String,List<String[]>> GetMultipleSearch(String indexpath,List<String[]> keywordset,int top) throws IOException, ParseException, InvalidTokenOffsetsException{
		
		Map<String,List<String[]>> files=new LinkedMap<String,List<String[]>>();
		
		Path inpath=Paths.get(indexpath);
		
		FSDirectory fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
		
		IOContext iocontext=new IOContext();

		RAMDirectory ramdir=new RAMDirectory(fsdir,iocontext);		//创建内存索引文件，并将磁盘索引文件放到内存中
		
		Analyzer analyzer=new StandardAnalyzer();		//创建标准分词器

		IndexReader indexreader=DirectoryReader.open(ramdir);

		IndexSearcher indexsearcher=new IndexSearcher(indexreader);
		
		int total=keywordset.size();
		
//		if(total==0)
//			return null;
		
		String[] keywords=new String[total];
		String[] fields=new String[total];
		BooleanClause.Occur[] flags=new BooleanClause.Occur[total];
	
		for(int i=0;i<total;i++){
			keywords[i]=keywordset.get(i)[2];
			fields[i]=keywordset.get(i)[1];
			if(keywordset.get(i)[0].equals("AND")){
				flags[i]=BooleanClause.Occur.MUST;	
			}else if(keywordset.get(i)[0].equals("OR")){
				flags[i]=BooleanClause.Occur.SHOULD;
			}else if(keywordset.get(i)[0].equals("NOT")){
				flags[i]=BooleanClause.Occur.MUST_NOT;
			}
		}
		
		Query query=MultiFieldQueryParser.parse(keywords,fields,flags,analyzer);

/*
		BooleanClause.Occur[] flags=new BooleanClause.Occur[total];
		Term[] term=null;
		TermQuery[] termquery=null;
		BooleanClause[] flags;
		BooleanQuery.Builder builder=new BooleanQuery.Builder();
		
*/  
        
       
/*		
		for(int i=0;i<total;i++){
			term[i]=new Term("law",keywordset.get(i)[0]);
			termquery[i]=new TermQuery(term[i]);
			if(keywordset.get(i)[1].equals("AND")||keywordset.get(i)[1].equals("")){
				builder.add(termquery[i],BooleanClause.Occur.MUST);
			}else if(keywordset.get(i)[i].equals("OR")){
				builder.add(termquery[i],BooleanClause.Occur.SHOULD);
			}else if(keywordset.get(i)[i].equals("NOT")){
				builder.add(termquery[i],BooleanClause.Occur.MUST_NOT);
			}	
		}

		BooleanQuery  booleanquery=builder.build();
*/        
        TopDocs topdocs=indexsearcher.search(query,top); 
        
        ScoreDoc[] hits=topdocs.scoreDocs;
        
        int num=hits.length;
        
        if(num==0){
        	return null;
        }
        
        //此处加入的是搜索结果的高亮部分
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color=red>","</font></b>"); //如果不指定参数的话，默认是加粗，即<b><b/>
        QueryScorer scorer = new QueryScorer(query);//计算得分，会初始化一个查询结果最高的得分
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer); //根据这个得分计算出一个片段
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter); //设置一下要显示的片段
  
        for(int i=0;i<num;i++){
        	
        	Document hitdoc=indexsearcher.doc(hits[i].doc);
        	
    		String temp=hitdoc.get("file");
    		String indexlaws[]=new String[2];
    		Integer index=Integer.valueOf(hitdoc.get("path"));
    		if(index/100000==999)		//判断章段落的索引号是否为999,当索引号为999时，标明没有章段落
    			indexlaws[0]="第"+0+"章"+"&emsp";
    		else
    			indexlaws[0]="第"+index/100000+"章"+"&emsp";
    		index=index%100000;
    		indexlaws[0]+="第"+index/1000+"节";
    		String laws=hitdoc.get("law");
    		if(laws!=null){
    			TokenStream tokenStream=analyzer.tokenStream("laws",new StringReader(laws));
    			String highlaws=highlighter.getBestFragment(tokenStream,laws);
        		indexlaws[1]=highlaws;
               	if(i==0){
            		List<String[]> path=new ArrayList<String[]>();
            		path.add(indexlaws);
            		files.put(temp,path);
            	}else{
            		if(files.containsKey(temp)){
            			files.get(temp).add(indexlaws);
            		}else{
            			List<String[]> path=new ArrayList<String[]>();
                		path.add(indexlaws);
                		files.put(temp,path);
            		}     		
            	}
    		}
        }
        indexreader.close();
        fsdir.close();
        return files;
		
	}

	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-18 
	 * 
	 * 该方法与GetSearch功能一致，直接返回法条内容，只是可以使用多条件、多域进行查询
	 *
	 * @params indexpath
	 * 				索引文件所在目录
	 * 			fields
	 * 				指定从哪几个字段查询，使用String[]方式传参数
	 * 			range
	 * 				指定从哪些文档里搜索keywords
	 * 			keywords 
	 * 				从JTextField获取用户输入的多个关键字,使用String方式传递
	 * 			top
	 * 				从JRadio获取用户选择的搜索条数，在索引文件中根据相关度排序，返回前top条
	 * 			
	 * @return Map<Stirng,List<String[]>>
	 * 				将搜索结果以Map<文件名，[章节，法条]>的映射关系，返回查询结果		   
	 *
	 * @2017-11-18
	 * 			增加List<String>参数，用于指定在哪些文档内查询法条
	 * 			修改keywords参数类型为String	
	 * @Modified Date:2017-12-25
	 * 			取消top参数，改为默认使用索引文档中有效文档总数	
	 * @Modified Date:2018-8-9			  
	 * 			判断章段落索引号是否为999，如果是，则标明没有章段落
	 */

	public Map<String,List<String[]>> GetMultipleSearch(String indexpath,String[] fields,List<String> range,String keywords) throws IOException, ParseException, InvalidTokenOffsetsException{
		
		Map<String,List<String[]>> files=new LinkedMap<String,List<String[]>>();
/*		
		Path inpath=Paths.get(indexpath);
		
		FSDirectory fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
		
		IOContext iocontext=new IOContext();

		RAMDirectory ramdir=new RAMDirectory(fsdir,iocontext);		//创建内存索引文件，并将磁盘索引文件放到内存中
*/		
		Analyzer analyzer=new StandardAnalyzer();		//创建标准分词器

//		IndexReader indexreader=DirectoryReader.open(ramdir);
		
		this.CreateIndexReader(indexpath);
		
		if(indexreader!=null){

			IndexSearcher indexsearcher=new IndexSearcher(indexreader);
		
			BooleanQuery.Builder fbuilder=new BooleanQuery.Builder();
		
			int rnum=range.size();
		
			for(int i=0;i<rnum;i++){
		
				Term term=new Term(fields[0],range.get(i));
		
				TermQuery termquery=new TermQuery(term);
		
				fbuilder.add(termquery,BooleanClause.Occur.SHOULD);
		
			}
		
			BooleanQuery  fbooleanquery=fbuilder.build();
	    
			BooleanQuery.Builder lbuilder=new BooleanQuery.Builder();
		
			QueryParser parser=new QueryParser(fields[1], analyzer);
	           
			Query query=parser.parse(keywords);
	        
			lbuilder.add(query,BooleanClause.Occur.MUST);
			
			BooleanQuery  lbooleanquery=lbuilder.build();
	    
			BooleanQuery.Builder builder=new BooleanQuery.Builder();
		
			builder.add(fbooleanquery,BooleanClause.Occur.MUST);
		
			builder.add(lbooleanquery,BooleanClause.Occur.MUST);
		
			BooleanQuery  booleanquery=builder.build();
		
			int top=indexreader.numDocs();		//获取有效索引文档总数
		
			if(top==0){		//判断有效索引文档数是否为0，如果为零则退出该方法，返回null
				files=null;
			}
			else{
			
				TopDocs topdocs=indexsearcher.search(booleanquery,top); 
			
				ScoreDoc[] hits=topdocs.scoreDocs;
		        
				int num=hits.length;
		        
				if(num==0){
		        	
					return null;
		        }
		        
		    	//此处加入的是搜索结果的高亮部分
				SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color=red>","</font></b>"); //如果不指定参数的话，默认是加粗，即<b><b/>
		    
				QueryScorer scorer = new QueryScorer(query);//计算得分，会初始化一个查询结果最高的得分
		    
				//			Fragmenter fragmenter = new SimpleSpanFragmenter(scorer); //根据这个得分计算出一个片段
		    
				Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);

				//       	highlighter.setTextFragmenter(fragmenter); //设置一下要显示的片段
		  
				for(int i=0;i<num;i++){	
					Document hitdoc=indexsearcher.doc(hits[i].doc);
					String temp=hitdoc.get("file");
					String indexlaws[]=new String[2];
					Integer index=Integer.valueOf(hitdoc.get("path"));
		    		if(index/100000==999)		//判断章段落的索引号是否为999,当索引号为999时，标明没有章段落
		    			indexlaws[0]="第"+0+"章"+"&emsp";
		    		else
		    			indexlaws[0]="第"+index/100000+"章"+"&emsp";
					index=index%100000;
					indexlaws[0]+="第"+index/1000+"节";
					String laws=hitdoc.get("law");
					if(laws!=null){
						TokenStream tokenStream = analyzer.tokenStream("laws",new StringReader(laws));
						Fragmenter displaysize= new SimpleFragmenter(laws.length());
						highlighter.setTextFragmenter(displaysize);
						String highlaws=highlighter.getBestFragment(tokenStream,laws);
						indexlaws[1]=highlaws;
						if(i==0){	
							List<String[]> path=new ArrayList<String[]>();
							path.add(indexlaws);
							files.put(temp,path);	
						}else{	
							if(files.containsKey(temp)){
								files.get(temp).add(indexlaws);
							}else{	
								List<String[]> path=new ArrayList<String[]>();
								path.add(indexlaws);
								files.put(temp,path);		
							}     		    	
						}    
				
					}        
				}
			}
//	        ramdir.close();
//	        indexreader.close();
//	        fsdir.close();
	
		}
		return files;
		
	}
		
	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-16 
	 * 
	 * 该方法与GetMultipleSearch功能一致，直接返回法条内容，只能查询单条件、单字段，针对不分词字段进行查询
	 *
	 * @params indexpath
	 * 				索引文件所在目录
	 * 			keywords 
	 * 				从JTextField获取用户输入的关键字
	 * 			top
	 * 				从JRadio获取用户选择的搜索条数，在索引文件中根据相关度排序，返回前top条
	 * 			
	 * @return Map<Stirng,List<String[]>>
	 * 				将搜索结果以Map<文件名，[章节，法条]>的映射关系，返回查询结果		   
	 * @2017-11-17
	 * 			使用SortField和Sort调用IndexSearch方法,对搜索结果用path字段升序排序						  
	 * @2018-8-9
	 * 			增加判断章段落索引号是否为999，如果是，则表明没有章段落，索引号设置为0
	 * 
	 */
	
	public Map<String,List<String[]>> GetTermSearch(String indexpath,String keywords,int top) throws IOException{
		
		Map<String,List<String[]>> files=new LinkedMap<String,List<String[]>>();
    	List<String[]> path=new ArrayList<String[]>();
/*		
		Path inpath=Paths.get(indexpath);
		
		FSDirectory fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
		
		IOContext iocontext=new IOContext();

		RAMDirectory ramdir=new RAMDirectory(fsdir,iocontext);		//创建内存索引文件，并将磁盘索引文件放到内存中
		
//		Analyzer analyzer=new StandardAnalyzer();		//创建标准分词器

		IndexReader indexreader=DirectoryReader.open(ramdir);
*/
    	
    	this.CreateIndexReader(indexpath);
    	
		IndexSearcher indexsearcher=new IndexSearcher(indexreader);
		
		Term term=new Term("file",keywords);
		
		TermQuery termquery=new TermQuery(term);
		
		SortField sortfield=new SortField("path",SortField.Type.INT,false);		//false为升序
       
		Sort sort=new Sort(sortfield);
	
        TopDocs topdocs=indexsearcher.search(termquery,top,sort); 
        
        ScoreDoc[] hits=topdocs.scoreDocs;
        
        String temp=null;
        
        int num=hits.length;
        
        if(num==0){
        	return null;
        }
        
        for(int i=0;i<num;i++){
        	
        	Document hitdoc=indexsearcher.doc(hits[i].doc);
        	
    		temp=hitdoc.get("file");
    		String indexlaws[]=new String[2];
    		Integer index=Integer.valueOf(hitdoc.get("path"));
    		if(index/100000==999)		//判断章段落的索引号是否为999,当索引号为999时，标明没有章段落
    			indexlaws[0]="第"+0+"章"+" ";
    		else
    			indexlaws[0]="第"+index/100000+"章"+" ";
    		index=index%100000;
    		indexlaws[0]+="第"+index/1000+"节";
    		String laws=hitdoc.get("law");
    		if(laws!=null){
        		indexlaws[1]=laws;
            	path.add(indexlaws);
    		}
        }
    	files.put(temp,path);
		
//        ramdir.close();
//        indexreader.close();
//        fsdir.close();
        return files;
		
	}
	
	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-1-5 
	 * 
	 * 该方法与GetTermSearch功能一致，返回查询到的文档中所有法条，主要用于将法条提交的服务器
	 *
	 * @params indexpath
	 * 				索引文件所在目录
	 * 			keywords 
	 * 				从JTextField获取用户输入的关键字
	 * 			
	 * @return Map<Stirng,List<String[]>>
	 * 				将搜索结果以Map<文件名，[章节，法条]>的映射关系，返回查询结果
	 * 
	 * Modefied Date:2017-1-9
	 * 			修复string[] indexlaws初始化位置   					  
	 * 
	 */
	
	public Map<String,List<String[]>> GetTermSearch(String indexpath,String keywords) throws IOException{
		
		Map<String,List<String[]>> files=new LinkedMap<String,List<String[]>>();
    	List<String[]> path=new ArrayList<String[]>();
    	
    	this.CreateIndexReader(indexpath);
    	
    	if(indexreader!=null){
    	
    		IndexSearcher indexsearcher=new IndexSearcher(indexreader);
    		
			int top=indexreader.numDocs();		//获取索引文件中有效文档总数
		       
			if(top==0)		//判断索引文件中的有效文档总数是否为0，如果为零则退出该方法，返回null
				files=null;
			else{
		
				Term term=new Term("file",keywords);
				
				TermQuery termquery=new TermQuery(term);
		
				SortField sortfield=new SortField("path",SortField.Type.INT,false);		//false为升序
       
				Sort sort=new Sort(sortfield);
	
				TopDocs topdocs=indexsearcher.search(termquery,top,sort); 
        
				ScoreDoc[] hits=topdocs.scoreDocs;
 
				int num=hits.length;
        
				if(num==0)		//查询结果为空
					files=null;
				else{
					String temp=null;
					String laws;
					for(int i=0;i<num;i++){
						String indexlaws[]=new String[2];
						Document hitdoc=indexsearcher.doc(hits[i].doc);
						temp=hitdoc.get("file");	
						indexlaws[0]=hitdoc.get("path");
						laws=hitdoc.get("law");
						if(laws!=null){
							indexlaws[1]=laws;
							path.add(indexlaws);						
						}    
					}  	
					files.put(temp,path);			
				}
			
			}
    	
    	}
        return files;
		
	}
	
	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-10 
	 * 
	 * DeleteIndex方法根据给定文档名，删除索引
	 *
	 * @params filename
	 * 				文档名称
	 * 			indexpath 
	 * 				索引文件存放目录	
	 * @return void
	 * 
	 * @2017-11-15
	 * 			使用方法forceMergeDeletes()，实现立即删除
	 * Modiefied Date:2017-12-24
	 * 			修改删除索引时的默认合并策略值10%，需要和创建索引时设置的值保持一致
	 * Modefied Date:2018-1-2
	 * 			将indexwriter修改为类的私有类，以调用IndexReader的单例函数openifchange(Directory,IndexWriter方法)
	 * 			将indexwriter更改为静态类，在创建前判断indexwriter是否处于打开状态，如果打开，则关闭以释放资源		   				
	 * 
	 */

	public void DeleteIndex(String filename,String indexpath) throws IOException{
		
		Path inpath=Paths.get(indexpath);
		FSDirectory fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
		
//		IOContext iocontext=new IOContext();

//		RAMDirectory ramdir=new RAMDirectory(fsdir,iocontext);		//创建内存索引文件，并将磁盘索引文件放到内存中
		
		Analyzer analyzer=new StandardAnalyzer();		//创建标准分词器
		
//		IndexWriterConfig ramconfig=new IndexWriterConfig(analyzer);
		
//		IndexWriter ramiwriter=new IndexWriter(ramdir,ramconfig);		//创建内存IndexWriter
		
	    IndexWriterConfig fsconfig=new IndexWriterConfig(analyzer);
		TieredMergePolicy ti=new TieredMergePolicy();
		ti.setForceMergeDeletesPctAllowed(0);		//设置删除索引时的默认合并策略值为0
		fsconfig.setMergePolicy(ti);		//设置合并策略
//		System.out.println(ti.getForceMergeDeletesPctAllowed());
//	    IndexWriter fsiwriter=new IndexWriter(fsdir,fsconfig); 
		if(indexwriter!=null){
			if(indexwriter.isOpen())
				indexwriter.close();
		}
		indexwriter=new IndexWriter(fsdir,fsconfig);
		
		Term t=new Term("file",filename);
		

	
//		fsiwriter.deleteDocuments(t);
//		fsiwriter.forceMergeDeletes();		//删除索引时并不是立即从磁盘删除，而是放入回收站，可回滚操作，调用该方法后，是立即删除	
//		fsiwriter.commit();		
//		fsiwriter.close();
		
		indexwriter.deleteDocuments(t);
		indexwriter.forceMergeDeletes();		//删除索引时并不是立即从磁盘删除，而是放入回收站，可回滚操作，调用该方法后，是立即删除
		indexwriter.commit();
//		indexwriter.close();
		
  
//	    fsiwriter.addIndexes(ramdir); 		//程序结束后，将内存索引写入到磁盘索引中
//	    fsiwriter.close();	        	
	}

	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-10 
	 * 
	 * GetTermFreq方法在根据file字段中，文档名称出现的次数，获取该文档中索引的法条总数
	 *
	 * @params indexpath 
	 * 				索引文件存放目录
	 * 				
	 * @return Map<String,Integer>
	 * 				返回文档中法条总数，<文档名称，法条总数>
	 * 
	 * @2017-11-15
	 * 			修改为使用TermsEnum类获取词频
	 * 			修复当没有索引文件时，catch报错异常，并进行弹框提示			   				
	 * 
	 */
	
	public Map<String,Integer> GetTermFreq(String indexpath){
		Map<String,Integer> res=new HashMap<String,Integer>();
//		Path inpath=Paths.get(indexpath);
//		Analyzer analyzer=new StandardAnalyzer();		//创建标准分词器
		try{
/*			
			FSDirectory fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
			IOContext iocontext=new IOContext();
			RAMDirectory ramdir=new RAMDirectory(fsdir,iocontext);		//创建内存索引文件，并将磁盘索引文件放到内存中
			IndexReader indexreader=DirectoryReader.open(ramdir);
*/
			this.CreateIndexReader(indexpath);
//			IndexSearcher indexsearcher=new IndexSearcher(indexreader);
//			int dd=indexreader.numDocs();
//			int aa=indexreader.maxDoc();
//			int ww=indexreader.numDeletedDocs();
//		System.out.println("有效文档数："+dd);
//		System.out.println("总文档数："+aa);
//		System.out.println("删除文档数："+ww);
			if(indexreader!=null){
				Terms terms = MultiFields.getTerms(indexreader,"file");
				if(terms!=null){
					TermsEnum termsEnums = terms.iterator();
					while(termsEnums.next()!=null){
						int num=termsEnums.docFreq();
//			 	        System.out.println();
						BytesRef byteRef=termsEnums.term();
						String term = new String(byteRef.bytes, byteRef.offset, byteRef.length,"UTF-8");
//						System.out.println(term);
						res.put(term,num);			 
					}
				}
			}
/*		
		int top=indexreader.numDocs();
		if(top!=0){
			QueryParser parser=new QueryParser("file", analyzer); 
			parser.setAllowLeadingWildcard(true);
			Query query=parser.parse("*.*");
			TopDocs topdocs=indexsearcher.search(query,top);     
			ScoreDoc[] hits=topdocs.scoreDocs;
			int num=hits.length;
			for(int i=0;i<num;i++){	
				Document hitdoc=indexsearcher.doc(hits[i].doc);
				String temp=hitdoc.get("file");
				if(!res.containsKey(temp)) {
					res.put(temp,0);
				}
			}
        
//			int dd=indexreader.docFreq(new Term("file","劳动人事争议仲裁办案规则（新）法.doc"));
			
//			System.out.println(dd);
			for(String key:res.keySet()){
				res.put(key,indexreader.docFreq(new Term("file",key)));
			}
		} 
*/		
//        ramdir.close();
//       indexreader.close();
//        fsdir.close();
		
		}catch (IOException e) {
			// TODO Auto-generated catch block
			if(e.getClass().getSimpleName().equals("IndexNotFoundException"))		//当没有找到索引文件时，catch异常，并弹框提示
				JOptionPane.showMessageDialog(null, "未找到索引文件，请先创建索引文件", "警告", JOptionPane.ERROR_MESSAGE);
			else
				e.printStackTrace();
		}

		return res;
	}
	

	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-10 
	 * 
	 * AddIndex方法在根据传递的file字段，将法条文档追加到已存在的索引文件中，如果没有索引文件，则创建索引文件
	 * AddIndex方法进行了优化，可以获取html文档内容追加索引到已存在的索引文件中
	 *
	 * @param url 
	 * 				文件路径或者html文档的url地址
	 * 				
	 * @param indexpath
	 * 				索引文件路径
	 * 
	 * @return Integer
	 * 				返回添加到索引文件中的法条数
	 * 
	 * @2017-12-19
	 * 				增加对path字段，增加NumericDocValuesField字段，用于排序
	 * Modified Date:2017-12-26
	 * 				增加读取全部法条内容，按照段落存储法条
	 * 				修改合并策略TieredMergePolicy，将删除索引时的默认合并策略值10%，修改为0，由于删除索引的默认合并策略10%，即删除的索引数达到总索引数(maxDo)的10%时，才会执行forceMergeDeletes
	 * Modified Date:2018-1-2
	 * 			将indexwriter修改为类的私有类，以调用IndexReader的单例函数openifchange(Directory,IndexWriter方法)
	 * 			将indexwriter更改为静态类，在创建前判断indexwriter是否处于打开状态，如果打开，则关闭以释放资源 					   				
	 * Modified Date:2018-8-7
	 * 			修改原参数fdir为url,可以传入文件路径或者html文档的url地址
	 * 			增加使用url地址获取html文档，为html文档内容创建索引
	 * 
	 */
	
	public Integer AddIndexs(String url,String indexpath) throws IOException{
		int totalofindex=0;
//		Path inpath=Paths.get(indexpath);
//		Analyzer analyzer = new StandardAnalyzer();		//创建标准分词器
//		FSDirectory fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
//		RAMDirectory ramdir=new RAMDirectory();		//创建内存索引文件
//		IndexWriterConfig ramconfig = new IndexWriterConfig(analyzer);
//		IndexWriter ramiwriter = new IndexWriter(ramdir,ramconfig);		//创建内存IndexWriter
		this.CreateIndexWriter(indexpath);
		
		if(url.matches("(http|https)://.*")){
			try{
				IOHtml html=new IOHtml(url);		//使用构造函数抓取html文档，捕捉异常，如果报异常，totalofindex赋值-1，不在建立索引文件
				Map<Integer,String> law=html.GetIndexOflaw();
				String filename=html.GetHtmlH();		//解析html文档标题
				totalofindex=law.size();		//获取读到的法条总数
				if(!law.isEmpty()){		//判断是否读取到了法条，如果有读到法条，则为每条法条创建索引
					for (Map.Entry<Integer,String> e:law.entrySet()){ 
						Document doc=new Document();		//创建Document,每一个发条新建一个		
						FieldType fieldtype=new FieldType();
						fieldtype.setIndexOptions(IndexOptions.DOCS);
						fieldtype.setStored(true);		
						fieldtype.setTokenized(false);
						doc.add(new Field("file",filename,fieldtype));		//文档名称存储，不分词
						doc.add(new NumericDocValuesField("path",e.getKey()));
						doc.add(new IntPoint("path",e.getKey()));		//法条索引以Int类型存储
						doc.add(new StoredField("path",e.getKey()));
						doc.add(new Field("law",e.getValue(),TextField.TYPE_STORED));		//法条内容索引、分词，存储
						ramwriter.addDocument(doc);		//将法条索引添加到内存索引中			  
					}
				}
				else		//如果没有读到段落，则totalofindex赋值-1
					totalofindex=-1;
			}catch(IOException e){		//捕捉异常，如果报异常，totalofindex赋值-2，不在建立索引文件
				totalofindex=-2;
			}	
		}else if(url.matches("[a-zA-Z]:\\\\.*")){
			File file=new File(url);
			IOWord ioword=new IOWord();
			String fname=file.getName().split("\\.")[0];
			//String check=fname.substring(fname.length()-2,fname.length());
			Map<Integer,String> law=new HashMap<Integer,String>();
			if(fname.matches("[\u4e00-\u9fa5《》]*(法|条例|草案|规则|通则)$")){		//规范的法条内容
				
				law=ioword.GetIndexOflaw(file);
			}
			else if(fname.matches("[\u4e00-\u9fa5《》]*M$")){		//带$的法条内容
				law=ioword.GetIndexOfmarkdocment(file);
			}
			else{		//按段落的法条内容
				law=ioword.GetIndexOfgeneraldocment(file);
			}
			totalofindex+=law.size();
			if(!law.isEmpty()){
				for (Map.Entry<Integer,String> e:law.entrySet()){ 
					Document doc=new Document();		//创建Document,每一个法条新建一个		
					FieldType fieldtype=new FieldType();
					fieldtype.setIndexOptions(IndexOptions.DOCS);
					fieldtype.setStored(true);		
					fieldtype.setTokenized(false);
					doc.add(new Field("file",file.getName(),fieldtype));		//文档名称存储，不分词
					doc.add(new NumericDocValuesField("path",e.getKey()));
					doc.add(new IntPoint("path",e.getKey()));		//法条索引以Int类型存储
					doc.add(new StoredField("path",e.getKey()));
					doc.add(new Field("law",e.getValue(),TextField.TYPE_STORED));		//发条内容索引、分词，存储
					ramwriter.addDocument(doc);		//将法条索引添加到内存索引中			  
				}
			}
			else
				totalofindex=-1;
		}else{
			totalofindex=-3;
		}
		
		ramwriter.close();	
//		TieredMergePolicy ti=new TieredMergePolicy();
//		ti.setForceMergeDeletesPctAllowed(0);		//设置删除索引的合并策略为0，有删除segment时，立即进行合并
//        IndexWriterConfig fsconfig=new IndexWriterConfig(analyzer); 
//    	fsconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
//    	fsconfig.setMergePolicy(ti);
//    	
//    	if(indexwriter!=null){
//    		if(indexwriter.isOpen())
//    			indexwriter.close();
//    	}
//        
//    	indexwriter=new IndexWriter(fsdir,fsconfig);  
        
		indexwriter.addIndexes(ramdir); 		//程序结束后，将内存索引写入到磁盘索引中
        indexwriter.commit();
        
        return totalofindex;
	}
	
//	public Integer AddIndex(String fdir,String indexpath) throws IOException{
		
//		File[] files = new File(fdir).listFiles();
		
//		File file=new File(fdir);
		
//		Path inpath=Paths.get(indexpath);
				
//		Analyzer analyzer = new StandardAnalyzer();		//创建标准分词器
	
//		FSDirectory fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
/*		
		try{
			lock=fsdir.obtainLock(IndexWriter.WRITE_LOCK_NAME);
		}catch (LockObtainFailedException e) {
			if(lock!=null)
				lock.close();
		}
*/		
//		RAMDirectory ramdir=new RAMDirectory();		//创建内存索引文件
	
//		IndexWriterConfig ramconfig = new IndexWriterConfig(analyzer);
		
//		IndexWriter ramiwriter = new IndexWriter(ramdir,ramconfig);		//创建内存IndexWriter
		
//		IOWord ioword=new IOWord();
		
//		int totalofindex=0;
		
//		for(int i=0;i<files.length;i++){
			
//			String fname=file.getName().split("\\.")[0];
//			String check=fname.substring(fname.length()-2,fname.length());
//			Map<Integer,String> law=new HashMap<Integer,String>();
			
//			if(check.contains("法")||check.contains("条例")||check.contains("草案")||check.contains("规则")||check.contains("通则")){		//规范的法条内容
//			
//				law=ioword.GetIndexOflaw(file);
//			}
//			else if(check.contains("M")){		//带$的法条内容
//				law=ioword.GetIndexOfmarkdocment(file);
//			}
//			else{		//按段落的法条内容
//				law=ioword.GetIndexOfgeneraldocment(file);
//			}
//				
//			
//			totalofindex+=law.size();
//			
//			for (Integer key:law.keySet()){ 
//				
//				String laws=law.get(302011);
				
//				System.out.println(laws);
//				
//				Document doc=new Document();		//创建Document,每一个法条新建一个		
//				FieldType fieldtype=new FieldType();
//				fieldtype.setIndexOptions(IndexOptions.DOCS);
//				fieldtype.setStored(true);		
//				fieldtype.setTokenized(false);
//				doc.add(new Field("file",file.getName(),fieldtype));		//文档名称存储，不分词
//				doc.add(new NumericDocValuesField("path",key));
//				doc.add(new IntPoint("path",key));		//法条索引以Int类型存储
//				doc.add(new StoredField("path",key));
//		    	doc.add(new Field("law",law.get(key),TextField.TYPE_STORED));		//发条内容索引、分词，存储
//		    	
//		    	ramiwriter.addDocument(doc);		//将法条索引添加到内存索引中			  
//			}
//		
//		}
//		ramiwriter.close();	
//		
//		TieredMergePolicy ti=new TieredMergePolicy();
//		ti.setForceMergeDeletesPctAllowed(0);		//设置删除索引的合并策略为0，有删除segment时，立即进行合并
//        IndexWriterConfig fsconfig=new IndexWriterConfig(analyzer); 
//    	fsconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
//    	fsconfig.setMergePolicy(ti);
//        IndexWriter fsiwriter=new IndexWriter(fsdir,fsconfig);   
//       fsiwriter.addIndexes(ramdir); 		//程序结束后，将内存索引写入到磁盘索引中
//    	
//    	if(indexwriter!=null){
//    		if(indexwriter.isOpen())
//    			indexwriter.close();
//    	}
//        
//    	indexwriter=new IndexWriter(fsdir,fsconfig);  
//        indexwriter.addIndexes(ramdir); 		//程序结束后，将内存索引写入到磁盘索引中
// 
//        indexwriter.commit();
//        indexwriter.close();
//        
//        return totalofindex;
//	}

	/*
	 *
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-1-23 
	 * 
	 * AddIndex方法在根据传递的Map，将Map中的索引追加到已存在的索引文件中，如果没有索引文件，则创建索引文件
	 *
	 * @params content 
	 * 				文档索引，以Map<文档名称，List<法条路径，法条内容>>形式传参
	 * 				
	 * 		   indexpath
	 * 				索引文件路径
	 * 
	 * @return Integer
	 * 				返回添加到索引文件中的法条数
	 * 	   				
	 */
	
	public Integer AddIndex(Map<String,List<String[]>> content,String indexpath) throws IOException{
		
		Path inpath=Paths.get(indexpath);

		Analyzer analyzer = new StandardAnalyzer();		//鍒涘缓鏍囧噯鍒嗚瘝鍣?
	
		FSDirectory fsdir=FSDirectory.open(inpath);		//鍒涘缓纾佺洏绱㈠紩鏂囦欢
		
		RAMDirectory ramdir=new RAMDirectory();		//鍒涘缓鍐呭瓨绱㈠紩鏂囦欢
	
		IndexWriterConfig ramconfig = new IndexWriterConfig(analyzer);
		
		IndexWriter ramiwriter = new IndexWriter(ramdir,ramconfig);		//鍒涘缓鍐呭瓨IndexWriter
		
		
		int totalofindex=0;
		List<String[]> laws=new ArrayList<String[]>();
		
		for(Entry<String, List<String[]>> entry:content.entrySet()){ 
				
				//FieldType filetype=new FieldType();
				//filetype.setIndexOptions(IndexOptions.DOCS);
				//filetype.setStored(true);		
				//filetype.setTokenized(false);
				//FieldType lawtype=new FieldType();
				//lawtype.setIndexOptions(IndexOptions.NONE);
				//lawtype.setStored(true);		
				//lawtype.setTokenized(false);
				
				FieldType filetype=new FieldType();
				filetype.setIndexOptions(IndexOptions.DOCS);
				filetype.setStored(true);		
				filetype.setTokenized(false);
				
				laws=entry.getValue();
				int count=laws.size();
				totalofindex=count;
				for(int i=0;i<count;i++){
					Document doc=new Document();		//鍒涘缓Document,姣忎竴涓彂鏉℃柊寤轰竴涓?
					doc.add(new Field("file",entry.getKey(),filetype));		//鏂囨。鍚嶇О瀛樺偍锛屼笉鍒嗚瘝
					//doc.add(new IntPoint("path",Integer.valueOf(laws.get(i)[0])));		//娉曟潯绱㈠紩浠nt绫诲瀷瀛樺偍
					//doc.add(new StoredField("path",Integer.valueOf(laws.get(i)[0])));
					doc.add(new NumericDocValuesField("path",Integer.valueOf(laws.get(i)[0])));
					doc.add(new IntPoint("path",Integer.valueOf(laws.get(i)[0])));		//法条索引以Int类型存储
					doc.add(new StoredField("path",Integer.valueOf(laws.get(i)[0])));
					doc.add(new Field("law",laws.get(i)[1],TextField.TYPE_STORED));		//鍙戞潯鍐呭绱㈠紩銆佸垎璇嶏紝涓嶅瓨鍌?
			    	ramiwriter.addDocument(doc);		//灏嗘硶鏉＄储寮曟坊鍔犲埌鍐呭瓨绱㈠紩涓?	
				}	    		  
			}
	
		ramiwriter.close();
	
		TieredMergePolicy ti=new TieredMergePolicy();
		ti.setForceMergeDeletesPctAllowed(0);		//设置删除索引的合并策略为0，有删除segment时，立即进行合并
        IndexWriterConfig fsconfig=new IndexWriterConfig(analyzer); 
    	fsconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
    	fsconfig.setMergePolicy(ti);
//        IndexWriter fsiwriter=new IndexWriter(fsdir,fsconfig);   
//       fsiwriter.addIndexes(ramdir); 		//程序结束后，将内存索引写入到磁盘索引中
    	
    	if(indexwriter!=null){
    		if(indexwriter.isOpen())
    			indexwriter.close();
    	}
        
    	indexwriter=new IndexWriter(fsdir,fsconfig);  
        indexwriter.addIndexes(ramdir); 		//程序结束后，将内存索引写入到磁盘索引中
 
        indexwriter.commit();
//        indexwriter.close();
        
        return totalofindex;
	}
	
	
	/*
 	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-29 
	 *
	 * 创建IndexReader单例，以便重复利用，减少资源消耗
	 *
	 * @params 
	 * 		   indexpath
	 * 				索引文件存储位置
	 * @return boolean	
	 * 				如果创建IndexReader成功，返回true,如果为没有索引文件，则创建失败，返回false
	 * Modefied Date:2018-1-2
	 * 			修改为使用openifChange(DirectoryReader,IndexWriter)方法
	 * Modefied Date:2018-1-23
	 * 			修改为返回值为布尔类型，如果创建IndexReader成功，返回true,如果为没有索引文件，则创建失败，返回false
	 * @Modified Data:2018-8-10
	 * 			修改为先判断IndexReader是否已经创建，如果已经创建，则不再创建磁盘索引fsdir和内存索引ramdir
	 *          增加创建磁盘索引fsdir和内存索引ramdir前先判断是否已经创建，如果创建则先close后，再创建
	 */

	public boolean CreateIndexReader(String indexpath) throws IOException{
		boolean f = false;
		try{
			if(indexreader==null){
				Path inpath=Paths.get(indexpath);
				if(fsdir==null)		//判断是否已经创建，如果没有创建则新建，否则复用
					fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
				IOContext iocontext=new IOContext();
				if(ramdir!=null)		//如果已经创建，则关闭
					ramdir.close();
				ramdir=new RAMDirectory(fsdir,iocontext);		//创建内存索引文件，并将磁盘索引文件放到内存中
				indexreader=DirectoryReader.open(ramdir);
			}
			else{
			
				if(indexwriter!=null){		//判断indexwriter是否实例化
					IndexReader tr=DirectoryReader.openIfChanged((DirectoryReader)indexreader,indexwriter);	
					if(tr!=null){	
						indexreader.close();	
						indexreader=tr;	
					}
				}
		
			}
			f=true;
		}catch (IOException e) {
		// TODO Auto-generated catch block
			if(e.getClass().getSimpleName().equals("IndexNotFoundException"))		//当没有找到索引文件时，catch异常，并弹框提示
				//JOptionPane.showMessageDialog(null, "未找到索引文件，请先创建索引文件", "警告", JOptionPane.ERROR_MESSAGE);
				f=false;
			else
				e.printStackTrace();
		}
		return f;
	}
	
	/*
 	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-29 
	 *
	 * 销毁indexwriter,indexreader,ramdir,fsdir,该方法在关闭程序事件中调用
	 *
	 * @params 
	 * 		    				
	 * @return void	
	 * 				
	 * Modefied Date:2018-8-10
	 * 			修改为先关闭indexwriter后，在关闭ramdir和fsdir
	 * 			增加关闭前indexwrtier前，先判断是否处于打开状态

	 */
	
	public static void CloseIndexReader() throws IOException{
		if(indexwriter!=null)
			if(indexwriter.isOpen())
				indexwriter.close();
		if(indexreader!=null)
			indexreader.close();
		if(ramdir!=null)
			ramdir.close();
		if(fsdir!=null)
			fsdir.close();
	}
	
	/*
 	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-8-9 
	 *
	 * 该方法创建IndexWriter，如果已经创建则不在创建，如果未创建，则创建
	 *
	 * @params 
	 * 		   indexpath
	 * 				索引文件存储位置
	 * @return void	
	 * 				
	 * Modefied Date:2018-8-10
	 * 			修改返回值为空
	 *          
	 */
	public void CreateIndexWriter(String indexpath) throws IOException{
		
//		Boolean f=true;
		
		Path inpath=Paths.get(indexpath);
		Analyzer analyzer = new StandardAnalyzer();		//创建标准分词器
		if(fsdir==null)		//判断磁盘索引是否创建，如果已经创建，则不再重新创建
			fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
		if(ramdir!=null)		//判断内存索引是否创建，如果已经创建则关闭内存索引，清空占用的内存
			ramdir.close();
		ramdir=new RAMDirectory();		//创建内存索引文件
		IndexWriterConfig ramconfig = new IndexWriterConfig(analyzer);
		ramwriter = new IndexWriter(ramdir,ramconfig);		//创建内存IndexWriter
		
		if(indexwriter==null){
			TieredMergePolicy ti=new TieredMergePolicy();
			ti.setForceMergeDeletesPctAllowed(0);		//设置删除索引的合并策略为0，有删除segment时，立即进行合并
			IndexWriterConfig fsconfig=new IndexWriterConfig(analyzer); 
			fsconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			fsconfig.setMergePolicy(ti);
			indexwriter=new IndexWriter(fsdir,fsconfig); 
		}
    	
//    	if(indexwriter!=null){
//    		if(indexwriter.isOpen())
//    			indexwriter.close();
//    	}
        
    	 
		
//		return f;
		
	}
	

	public static void main(String[] args) throws Exception{
		HandleLucene handle=new HandleLucene();
//		Map<String,List<Integer>> files=new HashMap<String,List<Integer>>();
		Map<String,List<String[]>> contentoffiles=new HashMap<String,List<String[]>>();
//		Map<String,Integer> fre=new HashMap<String,Integer>();
//		List<String[]> contentoflaw=new ArrayList<String[]>();
//		int num=handle.CreateIndex("D:\\Lucene\\src\\","D:\\Lucene\\index\\");
//		System.out.println(num);
//		handle.DeleteIndex("劳动人事争议仲裁办案规则（新）法.doc","D:\\Lucene\\index\\");
//		fre=handle.GetTermFreq("D:\\Lucene\\index\\");
//		for(String key:fre.keySet()){
//			System.out.println(key+":"+fre.get(key));
//		}
/*
		files=handle.ExecuteSearch("D:\\Lucene\\index\\","当事人",4);
		
		if(files==null){
			System.out.println("未搜索到关键词");
		}else{
			for(String key:files.keySet()){
				System.out.println("文件名："+key);
				List<Integer> path=files.get(key);
				System.out.println(path.size());	
			}
		}


		contentoflaw=handle.GetContentOfLawByIndex(files,"D:\\Lucene\\src\\");
		
		if(contentoflaw==null){
			System.out.println("未搜索到关键词");
		}else{	
			for(int i=0;i<contentoflaw.size();i++)
				System.out.println(contentoflaw.get(i)[0]+" "+contentoflaw.get(i)[1]+" "+contentoflaw.get(i)[2]+" "+contentoflaw.get(i)[3]);
			}
*/	
		
		List<String[]> keywordset=new ArrayList<String[]>();
		keywordset.add(new String[]{"AND","file","劳动人事争议仲裁办案规则（新）法.doc"});
//		contentoffiles=handle.GetSearch("D:\\Lucene\\index\\","file:劳动人事争议仲裁办案规则（新）法.doc",1000);
//		contentoffiles=handle.GetMultipleSearch("D:\\Lucene\\index\\",keywordset,1000);
		contentoffiles=handle.GetTermSearch("D:\\Lucene\\index\\","劳动人事争议仲裁办案规则（新）法.doc",1000);
		StringBuffer text=new StringBuffer();
		for(String key:contentoffiles.keySet()){
			for(int i=0;i<contentoffiles.get(key).size();i++){
				text.append("&emsp&emsp");
				text.append(contentoffiles.get(key).get(i)[1]);
				text.append("&emsp"+"<i>"+"--摘录自");
				text.append(key);;
				text.append("&emsp"+contentoffiles.get(key).get(i)[0]+"</i>");
				text.append("<br/>");
				text.append("<br/>");
				}
			}
	System.out.println(text.toString());
	
	}
	
	

}
