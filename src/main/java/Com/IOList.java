package Com;

import java.util.List;

class IOList{
	
	public void add(String sb,List<String> ll){
		int len=20;
		int size=ll.size();
		
		if(size<len){
			ll.add(sb);
		}
		else{
			for(int i=0;i<size-1;i++){
				ll.set(i,ll.get(i+1));
			}
			ll.set(size-1,sb);
		}
		
	}

}
