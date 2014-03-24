package edu.uncc.aside.phpcodeannotate.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import edu.uncc.aside.phpcodeannotate.Constants;
import edu.uncc.aside.phpcodeannotate.models.SensitiveMethod;

public class RulesUtils {
	public static HashSet<SensitiveMethod> getSensitiveOperations() {
		HashSet<SensitiveMethod> sensitiveOperations = new HashSet();

		String basePath = Utils.getPlugingBasePath();
		String path = basePath + Constants.DEFAULT_SENSITIVE_OPERATIONS_FILE;

		try {
			File file = new File(path);

			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String tmpStr = null;
			while ((tmpStr = br.readLine()) != null) {
				System.out.println("rule = " + tmpStr);
				sensitiveOperations.add(new SensitiveMethod(Constants.DB_VARIABLE, tmpStr));
			}
			br.close();
			fr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("sensitiveOperations size = "
				+ sensitiveOperations.size());
		if (sensitiveOperations.size() == 0) {
			System.err
					.println("ERROR: sensitive operations file was not read properly");

		}
		return sensitiveOperations;
	}

	public static HashSet<String> getSensitiveDBTables() {
		HashSet<String> sensitiveDBTables = new HashSet();

		String basePath = Utils.getPlugingBasePath();
		String path = basePath + Constants.DEFAULT_SENSITIVE_DB_TABLES_FILE;

		try {
			File file = new File(path);

			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String tmpStr = null;
			while ((tmpStr = br.readLine()) != null) {
				if(!tmpStr.equals("")) 
				sensitiveDBTables.add(tmpStr);
			}
			br.close();
			fr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (sensitiveDBTables.size() == 0) {
			System.err
					.println("ERROR: sensitive DB tables file was not read properly");

		}
		return sensitiveDBTables;
	}

	public static TreeSet<String> getSensitiveDBTablesRanked() {
		TreeSet<String> sensitiveDBTables = new TreeSet();

		String basePath = Utils.getPlugingBasePath();
		String path = basePath + Constants.DEFAULT_SENSITIVE_DB_TABLES_FILE;

		try {
			File file = new File(path);

			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String tmpStr = null;
			while ((tmpStr = br.readLine()) != null) {
				if(!tmpStr.equals("")) 
				sensitiveDBTables.add(tmpStr);
			}
			br.close();
			fr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (sensitiveDBTables.size() == 0) {
			System.err
					.println("ERROR: sensitive DB tables file was not read properly");

		}
		return sensitiveDBTables;
	}

}
