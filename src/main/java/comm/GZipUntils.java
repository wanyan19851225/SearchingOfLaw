package comm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;

public class GZipUntils {
	
	public String S2Gzip(String s) throws UnsupportedEncodingException{
		
		if (s == null || s.length() == 0)
			return s;
//		String res=null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip=null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(s.getBytes("utf-8"));
//			res=new String(Base64.encodeBase64(out.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(gzip!=null){
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new String(Base64.encodeBase64(out.toByteArray()));
	}
	public String Gzip2S(String s){
		if (s == null || s.length() == 0)
			return s;
		String res=null;
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		ByteArrayInputStream in=null;
		GZIPInputStream gzip=null;
//		byte[] compressed=null;
		try {
//			compressed = new sun.misc.BASE64Decoder().decodeBuffer(s);
			in=new ByteArrayInputStream(Base64.decodeBase64(s));
			gzip=new GZIPInputStream(in);
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = gzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
				}
			res=new String(out.toByteArray(),"utf-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (gzip != null) {
				try {
					gzip.close();
				
				} catch (IOException e) {
					}
				
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					}
				}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					}
				}
		}
		
		return res;
	}
	public static void main(String[] args) throws IOException{
//		IOHtml html=new IOHtml("http://www.panda.tv/agreement.html");
		GZipUntils gz=new GZipUntils();
//		List<String> text=html.GetHtmlP();
//		StringBuffer sb=new StringBuffer();
//		for(int i=0;i<text.size();i++){
//			sb.append(text.get(i));
//			sb.append(text.get(i));
//			sb.append(text.get(i));
//		}
		String tt="中华人民共和国";
		System.out.println(tt);
		System.out.println("原始字符长度:"+tt.length());
//		
		String dd=gz.S2Gzip(tt);
//		System.out.println(dd);
		System.out.println("压缩后字符长度:"+dd.length());
		
		String ss=gz.Gzip2S(dd);
		System.out.println(ss);
		System.out.println("解压后后字符长度:"+dd.length());
		
//		if(ss.equals(d)){
//			System.out.println("相等");
//		}
	}

}
