package Com;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.lucene.store.AlreadyClosedException;
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
	private static Boolean ramdiriscolse=false;
	private static final long BUF_SIZE=1024*1024;
	
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
	
	public Map<String,List<String[]>> QuerySegments(String indexpath,String keywords) throws ParseException, IOException, InvalidTokenOffsetsException{
		
		Map<String,List<String[]>> files=new LinkedMap<String,List<String[]>>();
		try{
			this.CreateIndexReader(indexpath);
			if(indexreader!=null){	
				Analyzer analyzer=new StandardAnalyzer();		//创建标准分词器
				IndexSearcher indexsearcher=new IndexSearcher(indexreader);
				QueryParser parser=new QueryParser("law", analyzer);         
				Query query=parser.parse(keywords.toString());        
				int top=indexreader.numDocs();		//获取索引文件中有效文档总数      
				if(top==0)	//判断索引文件中的有效文档总数是否为0，如果为零则退出该方法，返回null
					return files;					
				TopDocs topdocs=indexsearcher.search(query,top);         
				ScoreDoc[] hits=topdocs.scoreDocs;       
				int num=hits.length;        
				if(num==0)
					return files;											
				SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color=red>","</font></b>"); //如果不指定参数的话，默认是加粗，即<b><b/>
				QueryScorer scorer = new QueryScorer(query);//计算得分，会初始化一个查询结果最高的得分		
				Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);						
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

	public Map<String,List<String[]>> QuerySegments(String indexpath,String[] fields,List<String> range,String keywords) throws IOException, ParseException, InvalidTokenOffsetsException{
		
		Map<String,List<String[]>> files=new LinkedMap<String,List<String[]>>();	
		Analyzer analyzer=new StandardAnalyzer();		//创建标准分词器
		
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
			if(top==0)		//判断有效索引文档数是否为0，如果为零则退出该方法，返回null
				return files;
			TopDocs topdocs=indexsearcher.search(booleanquery,top); 			
			ScoreDoc[] hits=topdocs.scoreDocs;		        
			int num=hits.length;		       
			if(num==0)		   	
				return files;
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color=red>","</font></b>"); //如果不指定参数的话，默认是加粗，即<b><b/>		    
			QueryScorer scorer = new QueryScorer(query);//计算得分，会初始化一个查询结果最高的得分		    
			Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);		  
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
	 * @2018-8-22
	 * 			修改当检索结果为空时，将原来返回null，改为返回files
	 * 
	 */
	
	public Map<String,List<String[]>> GetAllSegments(String indexpath,String keywords,int top) throws IOException{
		
		String s=keywords.replaceAll("<[^>]+>","");
		Map<String,List<String[]>> files=new LinkedMap<String,List<String[]>>();
    	List<String[]> path=new ArrayList<String[]>();
    	
    	this.CreateIndexReader(indexpath);
    	
    	if(indexreader!=null){
    		IndexSearcher indexsearcher=new IndexSearcher(indexreader);		
    		Term term=new Term("file",s);		
    		TermQuery termquery=new TermQuery(term);		
    		SortField sortfield=new SortField("path",SortField.Type.INT,false);		//false为升序      
    		Sort sort=new Sort(sortfield);	
    		TopDocs topdocs=indexsearcher.search(termquery,top,sort);         
    		ScoreDoc[] hits=topdocs.scoreDocs;        
    		String temp=null;        
    		int num=hits.length;        
    		if(num==0)
    			return files;        
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
    	}
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
	
	public Map<String,List<String[]>> GetAllSegments(String indexpath,String keywords) throws IOException{
		
		Map<String,List<String[]>> files=new LinkedMap<String,List<String[]>>();
    	List<String[]> path=new ArrayList<String[]>();
    	
    	this.CreateIndexReader(indexpath);    	
    	if(indexreader!=null){   	
    		IndexSearcher indexsearcher=new IndexSearcher(indexreader);   		
			int top=indexreader.numDocs();		//获取索引文件中有效文档总数		       
			if(top==0)		//判断索引文件中的有效文档总数是否为0，如果为零则退出该方法，返回files
				return files;
			Term term=new Term("file",keywords);
			TermQuery termquery=new TermQuery(term);
			SortField sortfield=new SortField("path",SortField.Type.INT,false);		//false为升序
			Sort sort=new Sort(sortfield);
			TopDocs topdocs=indexsearcher.search(termquery,top,sort); 
			ScoreDoc[] hits=topdocs.scoreDocs;
			int num=hits.length;
			if(num==0)		//查询结果为空
				return files;
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
	 * 			filepath
	 * 				文档信息索引路径
	 * 			i
	 * 				删除的第几个文档
	 * 			fn
	 * 				总共需要删除多少个文档
	 * @return Boolean
	 * 			删除成功返回true
	 * 
	 * @2017-11-15
	 * 			使用方法forceMergeDeletes()，实现立即删除
	 * Modiefied Date:2017-12-24
	 * 			修改删除索引时的默认合并策略值10%，需要和创建索引时设置的值保持一致
	 * Modefied Date:2018-1-2
	 * 			将indexwriter修改为类的私有类，以调用IndexReader的单例函数openifchange(Directory,IndexWriter方法)
	 * 			将indexwriter更改为静态类，在创建前判断indexwriter是否处于打开状态，如果打开，则关闭以释放资源		   				
	 * Modefied Date:2018-8-21
	 * 			修改删除文档内容索引后，调用findexs.DeleteIndex删除文档信息索引
	 * Modefied Date:2018-8-22
	 * 			增加当filename有html标签时，删除html标签的功能
	 * Modified 2018-8-30
	 * 			新增Boolean类型返回值
	 * Modified 2018-9-5
	 * 			新增i和fn参数，当删除最后一个文档时，才执行forceMergeDeletes()和commit()方法，提升效率
	 */

	public Boolean DeleteIndex(String filename,int i,int fn,String indexpath,String filepath){
		Boolean f=true;
		String s=filename.replaceAll("<[^>]+>","");
		try {
			this.CreateDeleteIndexWriter(indexpath);
			Term t=new Term("file",s);

			indexwriter.deleteDocuments(t);
	
			if(i==fn){		//当删到最后一个文档时，才执行提交
				indexwriter.forceMergeDeletes();		//删除索引时并不是立即从磁盘删除，而是放入回收站，可回滚操作，调用该方法后，是立即删除
				indexwriter.commit();  
			}
			FileIndexs findexs=new FileIndexs();
			Boolean ff=false;
			while(!ff)
				ff=findexs.DeleteIndex(s,i,fn,filepath);	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			f=false;
			e.printStackTrace();
		}
		return f;
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
	 * 				段落索引文件路径
	 * @param indexpath
	 * 				文档信息索引路径
	 * @param j
	 * 			添加的第几个文档
	 * @param fn
	 * 			总共需要添加的文档数
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
	 * Modified Date:2018-8-21
	 * 			新增当写入段落索引成功后，创建文档信息索引文件
	 * 			新增filepath参数，传入文档信息索引文件路径
	 * Modified Date:2018-8-29
	 * 			新增当为html文档创建段落索引前，先判断该文档在文档信息索引中是否已经存在
	 * 			新增创建word文档段落索引时，捕捉IOWord.GetIndexOfgeneraldocment方法异常，捕获异常后，totalofindex赋值-3
	 * 			新增当创建文档信息索引时，新增文档路径，文档类型，文档来源三个字段
	 * Modified Date:2018-9-5
	 * 			新增参数j和fn，当添加索引到最后一个文档时，才执行commit操作，优化性能
	 * Modified Date:2018-9-6
	 * 			修改，当读到最后一个文档时，totalofindex<=0，即读取内容为空或者报错时，不创建文档信息索引的bug
	 */
	
	public Integer AddIndexs(String url,String indexpath,String filepath,int j,int fn) throws IOException{
		int totalofindex=0;
		String filename="";
		this.CreateAddIndexWriter(indexpath);
		if(url.matches("(http|https)://.*")){
			try{
				IOHtml html=new IOHtml(url);		//使用构造函数抓取html文档，捕捉异常，如果报异常，totalofindex赋值-1，不在建立索引文件
				filename=html.GetHtmlH();		//解析html文档标题
				List<String> files=new FileIndexs().GetAllFiles(filepath);
				if(!files.contains(filename)){
					Map<Integer,String> law=html.GetIndexOflaw();
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
						totalofindex=ResultsType.Index_NONE;
				}
				else
					totalofindex=ResultsType.DOC_Already_Exist;
			}catch(IOException e){		//捕捉异常，如果报异常，totalofindex赋值-2，不在建立索引文件
				totalofindex=ResultsType.URL_UNArrive;
			}
			if(totalofindex>0){		//当段落数大于0时，创建文档信息索引
				Date d=new Date(System.currentTimeMillis());
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				FileIndexs findexs=new FileIndexs();
				Map<String,String[]> finfo=new HashMap<String,String[]>();
				String[] infos=new String[6];
				infos[0]="";		//文档作者
				infos[1]=df.format(d);		//创建日期
				infos[2]=String.valueOf(totalofindex);		//段落总数
				infos[3]=url;		//文档路径
				infos[4]=Store.Docment.HTML;		//文档类型
				infos[5]=Store.Type.L;		//文档来源
				finfo.put(filename,infos);
				findexs.AddFiles(finfo,filepath,j,fn,totalofindex);
			}
		}else if(url.matches("[a-zA-Z]:\\\\.*")){
			File file=new File(url);
			try{
				IOWord ioword=new IOWord();
				String fname=file.getName().split("\\.")[0];
				Map<Integer,String> law=new HashMap<Integer,String>();
				if(fname.matches("[\u4e00-\u9fa5《》]*(法|条例|草案|规则|通则)$"))		//规范的法条内容
					law=ioword.GetIndexOflaw(file);
				else if(fname.matches("[\u4e00-\u9fa5《》]*M$"))		//带$的法条内容
					law=ioword.GetIndexOfmarkdocment(file);
				else		//按段落的法条内容
					law=ioword.GetIndexOfgeneraldocment(file);
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
					totalofindex=ResultsType.Index_NONE;
			}catch(IOException e){		//捕捉异常，如果报异常，totalofindex赋值-2，不在建立索引文件
				totalofindex=ResultsType.DOC_Bad;
			}
			if(totalofindex>0||j==fn){		//当段落数大于0,或者当读取到最后一个文档时，创建文档信息索引
				Date d=new Date(System.currentTimeMillis());
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				FileIndexs findexs=new FileIndexs();
				Map<String,String[]> finfo=new HashMap<String,String[]>();
				String[] infos=new String[6];
				infos[0]="";		//文档作者
				infos[1]=df.format(d);		//创建日期
				infos[2]=String.valueOf(totalofindex);		//段落总数
				infos[3]=url;		//文档路径
				infos[4]=Store.Docment.WORD;		//文档类型
				infos[5]=Store.Type.L;		//文档来源
				finfo.put(file.getName(),infos);
				findexs.AddFiles(finfo,filepath,j,fn,totalofindex);
			}
		}
		else
			totalofindex=ResultsType.URL_Format_Error;
		if(totalofindex>0&&ramdir.ramBytesUsed()>=BUF_SIZE){
			ramwriter.close();
			indexwriter.addIndexes(ramdir);
		}
		if(j==fn){
			ramwriter.close();
			indexwriter.addIndexes(ramdir);
			indexwriter.commit();
			ramdiriscolse=true;
		}
        return totalofindex;
	}
	
	/*
	 *
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-1-23 
	 * 
	 * AddIndex方法在根据传递的Map，将Map中的索引追加到已存在的索引文件中，如果没有索引文件，则创建索引文件，该方法用于从服务器索引导入到本地时使用
	 *
	 * @params content 
	 * 				文档索引，以Map<文档名称，List<法条路径，法条内容>>形式传参				
	 * 		   indexpath
	 * 				索引文件路径
	 * 		   filepath
	 * 				文档信息索引路径
	 * 		   finfos
	 * 				以[文档路径，文档类型]形式传入参数
	 * @param j
	 * 			添加的第几个文档
	 * @param fn
	 * 			总共需要添加的文档数
	 * @return Integer
	 * 				返回添加到索引文件中的法条数
	 * @Modified 2018-8-13
	 * 				修改为调用CreateAddIndexWriter方法，实现IndexWriter单例化
	 * @Modified 2018-8-23
	 * 				为文档段落创建索引成功后，创建文档信息索引
	 * @Modified 2018-8-29
	 *  			新增finfos参数，用来传入[文档路径，文档类型]
	 * @Modified 2018-8-29
	 * 				如果content为空，则ramwriter不在关闭，不在执行indexwriter.commit方法 
	 * @Modified Date:2018-9-5
	 * 			新增参数j和fn，当添加索引到最后一个文档时，才执行commit操作，优化性能 	   				
	 */
	
	public Integer AddIndexs(Map<String,List<String[]>> content,String[] finfos,String indexpath,String filepath,int j,int fn) throws IOException{
		int totalofindex=0;
		this.CreateAddIndexWriter(indexpath);
		
		List<String[]> laws=new ArrayList<String[]>();
		
		for(Entry<String, List<String[]>> entry:content.entrySet()){ 
			FieldType filetype=new FieldType();
			filetype.setIndexOptions(IndexOptions.DOCS);
			filetype.setStored(true);		
			filetype.setTokenized(false);
				
			laws=entry.getValue();
			int count=laws.size();
			totalofindex=count;
			for(int i=0;i<count;i++){
				Document doc=new Document();		
				doc.add(new Field("file",entry.getKey(),filetype));		
				doc.add(new NumericDocValuesField("path",Integer.valueOf(laws.get(i)[0])));
				doc.add(new IntPoint("path",Integer.valueOf(laws.get(i)[0])));		//法条索引以Int类型存储
				doc.add(new StoredField("path",Integer.valueOf(laws.get(i)[0])));
				doc.add(new Field("law",laws.get(i)[1],TextField.TYPE_STORED));	
			    ramwriter.addDocument(doc);	
			}
			if(totalofindex>0||j==fn){		//当段落数大于0时，创建文档信息索引
				Date d=new Date(System.currentTimeMillis());
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				FileIndexs findexs=new FileIndexs();
				Map<String,String[]> finfo=new HashMap<String,String[]>();
				String[] infos=new String[6];
				infos[0]="";
				infos[1]=df.format(d);
				infos[2]=String.valueOf(totalofindex);
				infos[3]=finfos[0];		//文档路径
				infos[4]=finfos[1];		//文档类型
				infos[5]=Store.Type.R;		//文档来源
				finfo.put(entry.getKey(),infos);
				findexs.AddFiles(finfo,filepath,j,fn,totalofindex);
			}
		}
		if(totalofindex>0&&ramdir.ramBytesUsed()>=BUF_SIZE){
			ramwriter.close();  
			indexwriter.addIndexes(ramdir);
		}
		if(j==fn){
			ramwriter.close();  
			indexwriter.addIndexes(ramdir);
			indexwriter.commit();
			indexwriter.close();
		}
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
				ramdiriscolse=true;
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
	 * 该方法创建AddIndexWriter，如果已经创建则不在创建，如果未创建，则创建
	 *
	 * @params 
	 * 		   indexpath
	 * 				索引文件存储位置
	 * @return void	
	 * 				
	 * Modefied Date:2018-8-10
	 * 			修改返回值为空
	 * Modified Date:2018-9-5
	 * 			修改，判断当ramwriter为null，或者关闭，或者ramdircolse为真，或者占用ramdir大于等于1024*1204字节后，重新分配内存空间，并重新初始化ramwriter
	 *          
	 */
	public void CreateAddIndexWriter(String indexpath) throws IOException{
		
//		Boolean f=true;
		
		Path inpath=Paths.get(indexpath);
		Analyzer analyzer = new StandardAnalyzer();
		if(fsdir==null)		
			fsdir=FSDirectory.open(inpath);	
		if(ramwriter==null||!ramwriter.isOpen()||ramdiriscolse||ramdir.ramBytesUsed()>=BUF_SIZE) {		//判断ramwriter是否为空或者关闭，如果为空或已关闭，则创建内存索引，重新创建ramwriter
			if(ramwriter==null||ramdiriscolse||ramdir.ramBytesUsed()>=BUF_SIZE){
				if(ramdir!=null)
					ramdir.close();
				ramdir=new RAMDirectory();
			}
			if(ramwriter!=null){
				if(!ramwriter.isOpen()){
					if(ramdir!=null){
						if(ramdir.ramBytesUsed()>=BUF_SIZE){
							ramdir.close();
							ramdir=new RAMDirectory();
						}
					}
					else
						ramdir=new RAMDirectory();
				}
			}
			IndexWriterConfig ramconfig = new IndexWriterConfig(analyzer);
			ramwriter = new IndexWriter(ramdir,ramconfig);
			ramdiriscolse=false;
		}
		
		if(indexwriter==null||!indexwriter.isOpen()){
			TieredMergePolicy ti=new TieredMergePolicy();
			ti.setForceMergeDeletesPctAllowed(0);		//设置删除索引的合并策略为0，有删除segment时，立即进行合并
			IndexWriterConfig fsconfig=new IndexWriterConfig(analyzer); 
			fsconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			fsconfig.setMergePolicy(ti);
			indexwriter=new IndexWriter(fsdir,fsconfig); 
		}		
	}
	
	/*
 	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-8-10 
	 *
	 * 该方法创建DeleteIndexWriter，如果已经创建则不在创建，如果未创建，则创建
	 *
	 * @params 
	 * 		   indexpath
	 * 				索引文件存储位置
	 * @return void	
	 * 
	 * Modified 2018-8-30
	 * 			修改增加对indexwriter是否关闭的判断，如果已经处于关闭状态，则重新创建indexwriter
	 * 				
	 */
	
	public void CreateDeleteIndexWriter(String indexpath) throws IOException{
		
		Path inpath=Paths.get(indexpath);
		if(fsdir==null)		//判断磁盘索引是否创建，如果已经创建，则不再重新创建
			fsdir=FSDirectory.open(inpath);		//创建磁盘索引文件
		
		if(indexwriter==null||!indexwriter.isOpen()){
			Analyzer analyzer=new StandardAnalyzer();		//创建标准分词器
			TieredMergePolicy ti=new TieredMergePolicy();
			ti.setForceMergeDeletesPctAllowed(0);		//设置删除索引时的默认合并策略值为0
			IndexWriterConfig fsconfig=new IndexWriterConfig(analyzer);
			fsconfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			fsconfig.setMergePolicy(ti);		//设置合并策略
			indexwriter=new IndexWriter(fsdir,fsconfig);
		}	
	}
	

	public static void main(String[] args) throws Exception{
//		HandleLucene handle=new HandleLucene();
//		Map<String,List<Integer>> files=new HashMap<String,List<Integer>>();
//		Map<String,List<String[]>> contentoffiles=new HashMap<String,List<String[]>>();
//		Map<String,Integer> fre=new HashMap<String,Integer>();
//		List<String[]> contentoflaw=new ArrayList<String[]>();
//		int num=handle.CreateIndex("D:\\Lucene\\src\\","D:\\Lucene\\index\\");
//		System.out.println(num);
//		handle.DeleteIndex("劳动人事争议仲裁办案规则（新）法.doc","D:\\Lucene\\index\\");
//		fre=handle.GetTermFreq("D:\\Lucene\\index\\");
//		for(String key:fre.keySet()){
//			System.out.println(key+":"+fre.get(key));
//		}
//
//		files=handle.ExecuteSearch("D:\\Lucene\\index\\","当事人",4);
//		
//		if(files==null){
//			System.out.println("未搜索到关键词");
//		}else{
//			for(String key:files.keySet()){
//				System.out.println("文件名："+key);
//				List<Integer> path=files.get(key);
//				System.out.println(path.size());	
//			}
//		}
//
//
//		contentoflaw=handle.GetContentOfLawByIndex(files,"D:\\Lucene\\src\\");
//		
//		if(contentoflaw==null){
//			System.out.println("未搜索到关键词");
//		}else{	
//			for(int i=0;i<contentoflaw.size();i++)
//				System.out.println(contentoflaw.get(i)[0]+" "+contentoflaw.get(i)[1]+" "+contentoflaw.get(i)[2]+" "+contentoflaw.get(i)[3]);
//			}
//	
//		
//		List<String[]> keywordset=new ArrayList<String[]>();
//		keywordset.add(new String[]{"AND","file","劳动人事争议仲裁办案规则（新）法.doc"});
//		contentoffiles=handle.GetSearch("D:\\Lucene\\index\\","file:劳动人事争议仲裁办案规则（新）法.doc",1000);
//		contentoffiles=handle.GetMultipleSearch("D:\\Lucene\\index\\",keywordset,1000);
//		contentoffiles=handle.GetTermSearch("D:\\Lucene\\index\\","劳动人事争议仲裁办案规则（新）法.doc",1000);
//		StringBuffer text=new StringBuffer();
//		for(String key:contentoffiles.keySet()){
//			for(int i=0;i<contentoffiles.get(key).size();i++){
//				text.append("&emsp&emsp");
//				text.append(contentoffiles.get(key).get(i)[1]);
//				text.append("&emsp"+"<i>"+"--摘录自");
//				text.append(key);;
//				text.append("&emsp"+contentoffiles.get(key).get(i)[0]+"</i>");
//				text.append("<br/>");
//				text.append("<br/>");
//				}
//			}
//	System.out.println(text.toString());
	}
	
	

}
