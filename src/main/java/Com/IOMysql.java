package Com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class IOMysql {
	
	public Connection ConnectMysql(String driver,String url,String user,String pwd) throws ClassNotFoundException, SQLException{
		  Class.forName(driver);
		  Connection connect=null;
	      connect=DriverManager.getConnection(url,user,pwd);
	      return connect;
	}
	
	public ResultSet QueryMysql(Connection connect,String sql) throws SQLException {
	      Statement statement= connect.createStatement();
          ResultSet rs=statement.executeQuery(sql.toString());
          return rs;
	}
	
	public void InsertMysql(Connection connect,String sql,Map<String,String> data) throws SQLException{
		int i=1;
		PreparedStatement statement=connect.prepareStatement(sql);
		for (Map.Entry<String,String> entry : data.entrySet())
			statement.setString(i++,entry.getValue());
		statement.executeUpdate();	
	}
	
	public void UpdateMysql(Connection connect,String sql){

	}
	
	public String QuerySql(String[] field,String db,String where){
		StringBuffer sql=new StringBuffer();
		int pasum=field.length;
		sql.append("SELECT ");
		for(int i=0;i<pasum;i++){
			if(i!=pasum-1)
				sql.append(field[i]+",");
			else
				sql.append(field[i]+" "); 
		}
		sql.append("FROM ");
		sql.append(db+" WHERE "+where);	
		return sql.toString();		
	}
	
	public String InsertSql(String db,String[] field){
		StringBuffer sql=new StringBuffer();
		int pasum=field.length;
		sql.append("INSERT INTO ");
		sql.append(db);
		sql.append("(");
		for(int i=0;i<pasum;i++){
			if(i!=pasum-1)
				sql.append(field[i]+",");
			else
				sql.append(field[i]); 
		}
		sql.append(")");
		sql.append("VALUES(");
		for(int i=0;i<pasum;i++){
			if(i!=pasum-1)
				sql.append("?,");
			else
				sql.append("?)"); 
		}	
		return sql.toString();
	}
	
	public Map<Integer,String> GetFeild (Connection connect,String sql) throws SQLException{
		
		Map<Integer,String> feild=new HashMap<Integer,String>();
		Statement statement= connect.createStatement();
        ResultSet rs=statement.executeQuery(sql.toString());
        ResultSetMetaData data=rs.getMetaData();
        
        for(int i=1;i<=data.getColumnCount();i++)
        	feild.put(i-1,data.getColumnName(i));
       
		return feild;	
	}

}
