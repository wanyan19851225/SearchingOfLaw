package comm;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.index.TieredMergePolicy;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;

public class FileIndexs {
	
	private static IndexReader indexreader;
	private static FSDirectory fsdir;
	private static RAMDirectory ramdir;
	private static IndexWriter indexwriter;
	private static IndexWriter ramwriter;
	
	/*
 	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-8-21 
	 *
	 * 创建IndexReader单例，以便重复利用，减少资源消耗
	 *
	 * @params 
	 * 		   indexpath
	 * 				索引文件存储位置
	 * @return boolean	
	 * 				如果创建IndexReader成功，返回true,如果为没有索引文件，则创建失败，返回false
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
	 *
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-8-21 
	 * 
	 * AddFiles方法在为文档名称、文档作者、创建日期、文档段落总数建立索引文件
	 *
	 * @param file 
	 * 				以Map<文档名称,[文档作者，创建日期，段落总数]>形式传入参数
	 * 				
	 * @param indexpath
	 * 				索引文件路径
	 * 
	 * @return Boolean
	 * 				返回添加索引是否成功
	 * 
	 */	
	public Boolean AddFiles(Map<String,String[]> file,String indexpath){
		Boolean f=true;
		try {
			Path path=Paths.get(indexpath);
			Analyzer analyzer = new StandardAnalyzer();	
			FSDirectory fdir = FSDirectory.open(path);
			RAMDirectory rdir=new RAMDirectory();		
			IndexWriterConfig rconf = new IndexWriterConfig(analyzer);
			IndexWriter rw = new IndexWriter(rdir,rconf);

			FieldType type=new FieldType();
			type.setIndexOptions(IndexOptions.DOCS);
			type.setStored(true);		
			type.setTokenized(true);
			
			FieldType dtype=new FieldType();
			dtype.setIndexOptions(IndexOptions.DOCS);
			dtype.setStored(true);		
			dtype.setTokenized(false);
			
			for(Entry<String, String[]> entry:file.entrySet()){
				String[] finfo=entry.getValue();
				Document doc=new Document();		
				doc.add(new Field("file",entry.getKey(),type));		//文档名称字段
				doc.add(new Field("author",finfo[0],type));		//文档作者字段
				doc.add(new Field("time",finfo[1],dtype));		//创建日期字段
				doc.add(new NumericDocValuesField("segments",Integer.valueOf(finfo[2])));
				doc.add(new IntPoint("segments",Integer.valueOf(finfo[2])));		//段落总数以Int类型存储
				doc.add(new StoredField("segments",Integer.valueOf(finfo[2])));
			    rw.addDocument(doc);			
			}
			rw.close();
		
			TieredMergePolicy ti=new TieredMergePolicy();
			ti.setForceMergeDeletesPctAllowed(0);		//设置删除索引的合并策略为0，有删除segment时，立即进行合并
		    IndexWriterConfig fconf=new IndexWriterConfig(analyzer); 
		    fconf.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		    fconf.setMergePolicy(ti);
		    IndexWriter fw=new IndexWriter(fdir,fconf);   
		    fw.addIndexes(rdir); 		//程序结束后，将内存索引写入到磁盘索引中
		    fw.commit();
		    fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			f=false;
		}		
		return f;	
	}
	
	/*
	 *
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-8-21 
	 * 
	 * GetAllFiles方法在文档信息索引中，查询所有的文件名称
	 *
	 * @params indexpath 
	 * 				索引文件存放目录
	 * 				
	 * @return List<String>
	 * 				返回文档名称		   				
	 */	
	public List<String> GetAllFiles(String indexpath){
		List<String> fname=new ArrayList<String>();
		try{
			this.CreateIndexReader(indexpath);
			if(indexreader!=null){
				Terms terms = MultiFields.getTerms(indexreader,"file");
				if(terms!=null){
					TermsEnum termsEnums = terms.iterator();
					while(termsEnums.next()!=null){
//						int num=termsEnums.docFreq();		//获取该Term出现的次数，即词频
						BytesRef byteRef=termsEnums.term();
						String term = new String(byteRef.bytes, byteRef.offset, byteRef.length,"UTF-8");
						fname.add(term);			 
					}
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			if(e.getClass().getSimpleName().equals("IndexNotFoundException"))		//当没有找到索引文件时，catch异常，并弹框提示
				JOptionPane.showMessageDialog(null, "未找到索引文件，请先创建索引文件", "警告", JOptionPane.ERROR_MESSAGE);
			else
				e.printStackTrace();
		}
		return fname;
	}

	/*
	 *
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-8-21 
	 * 
	 * GetFileInfo方法在文档信息索引中，检索文件信息
	 *
	 * @params indexpath 
	 * 				索引文件存放目录
	 * 				
	 * @return Map<String,String[]>
	 * 				返回Map<文档名称,[文档作者，创建日期，段落总数]>		   				
	 */	
	public Map<String,String[]> GetFileInfo(String indexpath){
		Map<String,String[]> finfo=new HashMap<String,String[]>();
		List<String> fname=this.GetAllFiles(indexpath);
		try{
			this.CreateIndexReader(indexpath);
			if(indexreader!=null){
				IndexSearcher indexsearcher=new IndexSearcher(indexreader);
				for(int i=0;i<fname.size();i++){
					Term term=new Term("file",fname.get(i));
					TermQuery termquery=new TermQuery(term);
				    TopDocs topdocs=indexsearcher.search(termquery,1);
				    ScoreDoc[] hits=topdocs.scoreDocs;
		    		Document hitdoc=indexsearcher.doc(hits[0].doc);
		    	    String infos[]=new String[3];
		    	    infos[0]=hitdoc.get("author");
		    	    infos[1]=hitdoc.get("time");
		    	    infos[2]=String.valueOf(hitdoc.get("segments"));
		    	    finfo.put(fname.get(i),infos);
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			if(e.getClass().getSimpleName().equals("IndexNotFoundException")){
				System.out.println("检索文档作者和文档创建日期时失败，原因：未检索到索引文件");
			//	e.printStackTrace();
				return finfo;
			}
			
			else
				e.printStackTrace();
		}
		
		return finfo;
	}
}
