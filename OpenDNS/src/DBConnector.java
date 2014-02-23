import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible to establish jdbc connectivity to one of the
 * configured databases. The configuration information for the database is read
 * from the properties file
 * 
 * 
 * @author Shruti Gupta
 * @version 1.0
 */

public class DBConnector
{
	private static final String CONST_SQL_QUERY="SELECT COUNT(1) FROM ? WHERE ?=? AND ?=?";

	// JDBC driver name and database URL
	private static String JDBC_DRIVER; // Example: "com.mysql.jdbc.Driver";
	private static String DB_URL; // Example: "jdbc:mysql://localhost/EMP";

	// Database credentials
	private static String USER;// Example: "username";
	private static String PASS;// Example: "password";

	private String tableName=null;
	private String hashCodeCol=null;
	private int hashCodeVal;
	private String urlVal;
	private int output;

	public String getUrlVal()
	{
		return urlVal;
	}

	public void setUrlVal(String urlVal)
	{
		this.urlVal=urlVal;
	}

	public int getOutput()
	{
		return output;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName=tableName;
	}

	public String getHashCodeCol()
	{
		return hashCodeCol;
	}

	public void setHashCodeCol(String hashCodeCol)
	{
		this.hashCodeCol=hashCodeCol;
	}

	public int getHashCodeVal()
	{
		return hashCodeVal;
	}

	public void setHashCodeVal(int hashCodeVal)
	{
		this.hashCodeVal=hashCodeVal;
	}

	public String getUrlCol()
	{
		return urlCol;
	}

	public void setUrlCol(String urlCol)
	{
		this.urlCol=urlCol;
	}

	private String urlCol=null;

	/**
	 * 
	 * This is the default constructor.
	 * 
	 * @param driver
	 * @param url
	 * @param user
	 * @param pwd
	 */
	public DBConnector(String driver,String url,String user,String pwd)
	{
		JDBC_DRIVER=driver;
		DB_URL=url;
		USER=user;
		PASS=pwd;
	}

	public static void setJDBC_DRIVER(String jDBC_DRIVER)
	{
		JDBC_DRIVER=jDBC_DRIVER;
	}

	public static void setDB_URL(String dB_URL)
	{
		DB_URL=dB_URL;
	}

	public static void setUSER(String uSER)
	{
		USER=uSER;
	}

	public static void setPASS(String pASS)
	{
		PASS=pASS;
	}

	/**
	 * This function will perform basic validation on the inputs required by the
	 * connect method in this class
	 */
	public void validateInputs()
	{
		if(JDBC_DRIVER==null||DB_URL==null||USER==null||PASS==null||JDBC_DRIVER.length()==0||DB_URL.length()==0||USER.length()==0||PASS.length()==0||this.hashCodeCol==null||this.tableName==null||this.urlCol==null||this.hashCodeCol.length()==0||this.tableName.length()==0||this.urlCol.length()==0)
		{
			throw new IllegalArgumentException("Cannot connect to the DB");
		}
	}

	/**
	 * This method will establish a connection to a database and perform a count
	 * query on the configured table and its column to see if a match for the
	 * URL and its hash code is found in the database of malicious URLs
	 */
	public void connect()
	{
		validateInputs();

		Connection conn=null;
		PreparedStatement pStmt=null;
		try
		{
			Class.forName(JDBC_DRIVER);

			System.out.println("Connecting to database...");
			conn=DriverManager.getConnection(DB_URL,USER,PASS);

			System.out.println("Creating statement...");
			pStmt=conn.prepareStatement(CONST_SQL_QUERY);
			pStmt.setString(1,this.tableName);
			pStmt.setString(2,this.hashCodeCol);
			pStmt.setInt(3,this.hashCodeVal);
			pStmt.setString(4,this.urlCol);
			pStmt.setString(5,this.urlVal);

			ResultSet rs=pStmt.executeQuery();

			while(rs.next())
			{
				// Retrieve by column name
				int count=rs.getInt(1);

				// Display values
				System.out.print("Count retrieved: "+count);
				this.output= count;
			}
			rs.close();
			pStmt.close();
			conn.close();
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			// release resources
			try
			{
				if(pStmt!=null)
					pStmt.close();
				pStmt=null;
			}
			catch(SQLException se2)
			{
				se2.printStackTrace();
			}
			try
			{
				if(conn!=null)
					conn.close();
				conn=null;
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}
		}
		System.out.println("Goodbye!");
	}
}
