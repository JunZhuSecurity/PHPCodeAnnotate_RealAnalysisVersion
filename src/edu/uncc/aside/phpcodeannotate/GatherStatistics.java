package edu.uncc.aside.phpcodeannotate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.uncc.aside.phpcodeannotate.models.AnnotationRecord;
import edu.uncc.aside.phpcodeannotate.models.MarkerRecord;
import edu.uncc.aside.phpcodeannotate.util.mapUtils.MapSort;

public class GatherStatistics {
	
	public static void NumOfWarningsInEachFile(){
		TreeMap<String, Integer> map1 = readMappingData("numOfWarningsInEachFileInIteration7th.txt");
		TreeMap<String, Integer> map2 = readMappingData("numOfWarningsInEachFileInIteration16th.txt");
		Set<String> keyset = map2.keySet();
		Iterator<String> iter = keyset.iterator();
		String key = null;
		int value;
		while(iter.hasNext()){
			key = iter.next();
			value = map2.get(key);
			if(map1.containsKey(key)){
				map1.put(key, value + map1.get(key));
			}else{
				map1.put(key, value);
			}
		}
		writeMappingData("summaryOfNumOfWarningsEachFile", map1);
		writeMappingData("SortedByNumOfWarnings-summaryOfNumOfWarningsEachFile", MapSort.sortByValue(map1));
			  
	}
	private static void writeMappingData(String fileName, Map<String, Integer> map) {
		
		String filename = Plugin.STATE_LOCATION + "/" + fileName;
		
		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();
		
		String tmpFileDir = null;
	    int numOfWarnings = 0;
		String seperator = Plugin.COMMA;
		StringBuffer strBuf = new StringBuffer();
		int sum = 0;
		
		while(iter.hasNext()){
			tmpFileDir = iter.next();
			numOfWarnings = map.get(tmpFileDir);
			sum += numOfWarnings;
			strBuf.append(tmpFileDir + seperator  + numOfWarnings + "\n");
		//	System.out.println("fileDir=" + fileDir + ", nodeStart=" + nodeStart + ",nodeLength=" + nodeLength + ", markerType=" + markerType);
		}
		strBuf.append("sum of warnings = " + sum);
		
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(filename);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(strBuf.toString());
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static TreeMap<String, Integer> readMappingData(String filename){
		TreeMap<String, Integer> map = new TreeMap<String, Integer> ();
        filename = Plugin.STATE_LOCATION + "/" + filename;
        
		File file = new File(filename);
		FileReader fr = null;
		String regex = Plugin.COMMA;
		
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String str = null;

			while ((str = br.readLine()) != null) {
				String[] strs = str.split(regex);
				
				String fileDir = strs[0];
				int numOfWarnings = Integer.valueOf(strs[1]);
				
				map.put(fileDir, numOfWarnings);
			}

			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	public static void filesWithoutRequiredAccessControls(TreeMap<String, Integer> numberOfWarningsInEachFile, TreeMap<String, Integer> numberOfAccessControlsInEachFile) {
		Set<String> keySet = numberOfWarningsInEachFile.keySet();
		Iterator<String> iter = keySet.iterator();
		String tmpStr = null;
		StringBuffer strBuf = new StringBuffer();
		String filename = Plugin.STATE_LOCATION + "/" + Constants.FILES_WITHOUT_REQUIRED_CHECKS_FILE;
		
		while(iter.hasNext()){
			tmpStr = iter.next();
			if(!numberOfAccessControlsInEachFile.containsKey(tmpStr)){
	//			System.out.println("One missing access control checks found!");
				strBuf.append(tmpStr + "\n");
			}
		}
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(filename);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(strBuf.toString());
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
public static void writeMarkersForEachTable(HashSet<MarkerRecord> allMarkerRecords, String currentSensitiveDBTable){
		
		String fileName = Plugin.STATE_LOCATION + "/" + currentSensitiveDBTable + "_" + Plugin.MARKER_RECORD_FILE;
		
		//System.out.println("size of markers =========== " + Plugin.allMarkerRecords.size());
		Iterator<MarkerRecord> iter = allMarkerRecords.iterator();
		MarkerRecord tmpMarkerRecord = null;
		String fileDir = null;
		int nodeStart, nodeLength;
		String markerType;
		boolean isAnnotated;
		String seperator = Plugin.COMMA;
		StringBuffer strBuf = new StringBuffer();
		
		
		while(iter.hasNext()){
			StringBuffer annotationsStrBuf = new StringBuffer();
			Iterator<AnnotationRecord> iterAnnotation = null;
			AnnotationRecord annotationRecord = null;
			int markerHashCode;
			
			tmpMarkerRecord = iter.next();
			markerHashCode = tmpMarkerRecord.hashCode();
			markerType = tmpMarkerRecord.getMarkerType();
			isAnnotated = tmpMarkerRecord.isAnnotated();
			fileDir = tmpMarkerRecord.getNodePositionInfo().getFileDir();
			nodeStart = tmpMarkerRecord.getNodePositionInfo().getStartPosition();
			nodeLength = tmpMarkerRecord.getNodePositionInfo().getLength();
			
			iterAnnotation = tmpMarkerRecord.getAnnotationRecords().iterator();
			while(iterAnnotation.hasNext()){
				annotationRecord = iterAnnotation.next();
				if(iterAnnotation.hasNext())
				annotationsStrBuf.append(annotationRecord.hashCode() + Plugin.ITEM_SEPERATOR);
				else
					annotationsStrBuf.append(annotationRecord.hashCode());
			}
			//store the Marker information in the persistent file
			String str = markerHashCode + seperator + fileDir + seperator + nodeStart + seperator + nodeLength + seperator + markerType + seperator + isAnnotated + seperator + annotationsStrBuf.toString() + "\n";
			strBuf.append(str);
		//	System.out.println("fileDir=" + fileDir + ", nodeStart=" + nodeStart + ",nodeLength=" + nodeLength + ", markerType=" + markerType + ",isAnnotated=" + isAnnotated);
		}
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(strBuf.toString());
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

public static void readWarningStatistics(TreeSet<String> inFilenames, String outputFileName){
	TreeMap<String, Integer> map = new TreeMap<String, Integer> ();
	Iterator iter = inFilenames.iterator();
	String filename;
	String actualFileName;
	while(iter.hasNext()){
		
	filename = (String)iter.next();
	actualFileName = Plugin.STATE_LOCATION + "/" + filename + "_MarkerRecords.txt";
    
	File file = new File(actualFileName);
	FileReader fr = null;
	String regex = Plugin.COMMA;
	int lineNum = 0;
	
	try {
		fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String str = null;


		while ((str = br.readLine()) != null) {
			lineNum ++;
		}
		map.put(filename, lineNum);

		br.close();
		fr.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	Set<Entry<String, Integer>> entrySet = map.entrySet();
	Iterator it = entrySet.iterator();
	StringBuffer strBuf = new StringBuffer();
	StringBuffer strBuf1 = new StringBuffer();
	while(it.hasNext()){
		Entry<String, Integer> entry = (Entry<String, Integer>) it.next();
		String fileName = entry.getKey();
		int numOfWarnings = entry.getValue();
		strBuf.append(fileName + "\t" + numOfWarnings + "\n");
		strBuf1.append(numOfWarnings + "\n");
	}
	FileWriter fw = null;
	try {
		fw = new FileWriter(Plugin.STATE_LOCATION + "/" + outputFileName);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(strBuf.toString());
		bw.close();
		fw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	FileWriter fw1 = null;
	try {
		fw1 = new FileWriter(Plugin.STATE_LOCATION + "/" + "OnlyNum_" + outputFileName);
		BufferedWriter bw1 = new BufferedWriter(fw1);
		bw1.write(strBuf1.toString());
		bw1.close();
		fw1.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
