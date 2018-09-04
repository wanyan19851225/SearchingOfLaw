package Com;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UpdateString {
	
	public boolean IsTopThree(String src,String value){
		
		if(src.substring(0,2).equals(value))
			return true;
		else
			return false;
	}
	
	public boolean IsInTopThree(String src,String value){
					
		String head=src.substring(0,1);		//截取字符串的第一个字符
		String text=src.substring(1,7);		//从字符串的第二个字符开始截取，截取到字符串结尾
		
//		System.out.println("head:"+head+"-"+"text:"+text);
		
		if(head.contains("第")){
			if(text.contains(value))
				return true;
			else
				return false;			
		}
		else
			return false;	
	}
	
	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-10-29 
	 * 
	 * 根据传入的String，去除扩展名，添加书名号
	 *
	 * @params filename
	 * 				字符串
			
	 * @return String
	 * 				返回带书名号的字符串		   					  
	 * 
	 */
	
	public String addBookTitleMark(String filename){
		

		
		return "《"+filename.split("\\.")[0]+"》";
		
	}
		
	public int chineseNumber2Int(String chineseNumber){
	        int result = 0;
	        int temp = 1;//存放一个单位的数字如：十万
	        int count = 0;//判断是否有chArr
	        char[] cnArr = new char[]{'一','二','三','四','五','六','七','八','九'};
	        char[] chArr = new char[]{'十','百','千','万','亿'};
	        for (int i = 0; i < chineseNumber.length(); i++) {
	            boolean b = true;//判断是否是chArr
	            char c = chineseNumber.charAt(i);
	            for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
	                if (c == cnArr[j]) {
	                    if(0 != count){//添加下一个单位之前，先把上一个单位值添加到结果中
	                        result += temp;
	                        temp = 1;
	                        count = 0;
	                    }
	                    // 下标+1，就是对应的值
	                    temp = j + 1;
	                    b = false;
	                    break;
	                }
	            }
	            if(b){//单位{'十','百','千','万','亿'}
	                for (int j = 0; j < chArr.length; j++) {
	                    if (c == chArr[j]) {
	                        switch (j) {
	                        case 0:
	                            temp *= 10;
	                            break;
	                        case 1:
	                            temp *= 100;
	                            break;
	                        case 2:
	                            temp *= 1000;
	                            break;
	                        case 3:
	                            temp *= 10000;
	                            break;
	                        case 4:
	                            temp *= 100000000;
	                            break;
	                        default:
	                            break;
	                        }
	                        count++;
	                    }
	                }
	            }
	            if (i == chineseNumber.length() - 1) {//遍历到最后一个字符
	                result += temp;
	            }
	        }
	        return result;
	    }

	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-3 
	 * 
	 * 返回“第”字符与查找字符之间的字符数
	 *
	 * @params src
	 * 				字符串
	 * 		   value
	 * 				要查找的字符	
	 * 	
	 * @return int
	 * 				返回“第”与要查找字符之间间隔的字符数，返回-1：没有查找到“第”字符，返回0：没有查找到要查找的字符		
	 * 
	 * @2017-11-06
	 * 				修改，查找“第”字符，然后在查找要查找字符，并返回两个之间间隔的字符数					  
	 * 
	 */
	
	public int CalChars(String src,String value){
		
		int offset=0;
		int num=-1;
		boolean f=true;
		for(int i=0;i<src.length();i++){
			String text=src.substring(i,i+1);
			if(f){
				if(text.equals("第")){
					f=false;
					offset=i;
				}
			}
			
			if(!f){			
				if(text.equals(value)){
					num=i-1;
					break;
				}else
					num=0;			
			}
		}
		
		return num-offset;
	}
	
	public boolean IsInTop(String src,String value){
		int num=CalChars(src,value);
		if(num<=5&&num>0)
			return true;
		else
			return false;
	}
	
	public String GetStringBetween(String src,String value){
		String str=null;
		int end=CalChars(src,value);
		if(end>0&&end<=5){
			int sta=src.indexOf("第")+1;
			str=src.substring(sta,sta+end);
//			System.out.println(str);
		}	
		return str;
	}
	
	public boolean IsFirst$(String src,String s){
		
		String head=src.substring(0,1);
		if(head.equals(s))
			return true;
		else
			return false;		
	}
	
	public boolean IsEnd(String src,String s){
		String head=src.substring(src.length()-1,src.length());
		if(head.equals(s))
			return true;
		else
			return false;		
	}

	
	public boolean IsFirstChinanNum(String src){
		
		String tm="一二三四五六七八九十零〇";
		boolean f=false;
		
		for(int i=0;i<src.length();i++){
			String text=src.substring(i,i+1);
			
			if(tm.contains(text)){
				if(i==0){
					f=true;
					break;
				}
				else{
					String s=src.substring(0,i);
			        Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
			        Matcher m=p.matcher(s);
			        if (m.find())
			        	f=false;
			        else
			        	f=true;
			        break;					
				}
			}
			
		}
		
		return f;
		
		
	}
	
	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-24 
	 * 
	 * 返回“第”字符与查找字符之间的字符数
	 *
	 * @params src
	 * 				字符串
	 * 		   value
	 * 				要查找的字符	
	 * 	
	 * @return int
	 * 				返回“第”与要查找字符之间间隔的字符数，返回-1：没有查找到“第”字符，返回0：没有查找到要查找的字符		
	 * 
	 * Modefied Date:2017-12-28
	 * 				修复当搜索关键词正好是14个字符时，截取字符串时报错的bug					  
	 * 
	 */

	public String SimpleString(String src){
		
		int len=src.length();
		StringBuffer s=new StringBuffer();
		if(len>=15){
			s.append(src.substring(0,15));
			s.append("...");
		}
		else
			s.append(src);
		
		return s.toString();
		
	}
	
	public String DelHtmlString(String src){
		String s;
		String regex_html="<[^>]+>";
		s=src.replaceAll(regex_html,"");
		return s.trim();
	}
	
	public String FilterDoubleString(String os,String ds){
		
		String regx="("+ds+"){2,}";
//        Pattern p=Pattern.compile("(AND){2,}");
		Pattern p=Pattern.compile(regx); 
        Matcher m=p.matcher(os);
        while(m.find()){ 
 //           System.out.println("重复的字符："+dd);   
            os=os.replace(m.group(0),ds);  
//            System.out.println(os);
        } 
		return os;
	}
	
	public Boolean ifSEString(){
		return true;
	}
	
	public static void main(String[] args){
		UpdateString us=new UpdateString();
		System.out.println(us.chineseNumber2Int("七十一"));
		System.out.println(us.FilterDoubleString("\"当事人\"ANDANDANDAND\"小小\"","AND"));
	}

}
