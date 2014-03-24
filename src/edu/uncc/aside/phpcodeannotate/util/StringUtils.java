package edu.uncc.aside.phpcodeannotate.util;

public class StringUtils {
	
	public static String obtainPureTableName(String tableName){
		String pureTableName = null;
		if(tableName.startsWith("'"))
			pureTableName = stripSingleQuote(tableName);
		else if(tableName.startsWith("\""))
			pureTableName = stripDoubleQuote(tableName);
		else
			pureTableName = tableName;
		return pureTableName;
	}
	
	//if a string is 'test', after strip, it is test
	public static String stripSingleQuote(String singleQuotedStr){
		String str = null;
		if(singleQuotedStr.startsWith("'"))
		    str = singleQuotedStr.substring(1, singleQuotedStr.length() - 1);
		else{
			System.err.println("ERROR: it is not a single quoted string!");
		}
			
		return str;
	}
	
	//if a string is "test", after strip, it is test
		public static String stripDoubleQuote(String doubleQuotedStr){
			String str = null;
			if(doubleQuotedStr.startsWith("\""))
			    str = doubleQuotedStr.substring(1, doubleQuotedStr.length() - 1);
			else{
				System.err.println("ERROR: it is not a double quoted string!");
			}
				
			return str;
		}

}
