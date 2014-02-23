import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a singleton class that will only read the DB Config contents only
 * once for the application from all configured properties files
 * 
 * Each properties file can have DB information for multiple db nodes -
 * essentially one properties.config file per cluster When system is being
 * scaled by multiple databases/clusters, each db node can be added within the
 * same properties file.
 * 
 * All nodes being added together need to be added in the same properties file
 * to be consistent with the hash code based URL insertion policy into the
 * databases
 * 
 * @author Shruti Gupta
 * @version 1.0
 */

public class LoadDBConfig
{
	public static final String STR_COMMENTS_CONST_PROP_FILE="#";
	/*
	 * Volatile to account for DCL
	 */
	private static volatile LoadDBConfig configObj=null;
	private static Object lock=new Object();
	/*
	 * Concurrent map implementation for efficiency in case of multi threaded
	 * access from the servlet container
	 */
	public final Map<String,List<String>> dbConfig;
	
	/**
	 * Made private on purpose for lazy initialization This is the default
	 * constructor.
	 * 
	 */
	private LoadDBConfig()
	{
		dbConfig=new ConcurrentHashMap<String,List<String>>();
		BufferedReader br=null;
		try
		{
			File folder=new File("./../config");
			List<String> lines;

			for(File file : folder.listFiles())
			{
				if(file.getName().matches("*.config"))
				{
					if(dbConfig.containsKey(file.getName()))
					{
						lines=dbConfig.get(file.getName());

					}
					else
					{
						lines=new ArrayList<String>();
						dbConfig.put(file.getName(),lines);
					}

					br=new BufferedReader(new FileReader(file));
					String line;
					while((line=br.readLine())!=null)
					{
						//check for comments within the properties file
						if(!line.startsWith(STR_COMMENTS_CONST_PROP_FILE))
							lines.add(line);
					}
				}
			}
		}
		catch(FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(br!=null)
			{
				try
				{
					br.close();
					br=null;
				}
				catch(IOException e)
				{
					//TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * public static factory method to lazily instantiate the singleton
	 * 
	 * @return
	 */
	public static LoadDBConfig getInstance()
	{
		if(configObj==null)
		{
			synchronized(lock)
			{
				if(configObj==null)
					configObj=new LoadDBConfig();
			}
		}
		return configObj;
	}
}
