package comm;

public interface Store {
	public static interface Type{
		public static final String L  = "local";
		public static final String R  = "remote";
	}
	public static interface Docment{
		public static final String WORD  = "word";
		public static final String HTML  = "html";
	}
	public static interface Len{
		public static final int LEN  = 20;
	}

}
