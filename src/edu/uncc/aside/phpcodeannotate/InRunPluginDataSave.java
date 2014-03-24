package edu.uncc.aside.phpcodeannotate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.uncc.aside.phpcodeannotate.models.AnnotationRecord;
import edu.uncc.aside.phpcodeannotate.models.MarkerRecord;

public class InRunPluginDataSave {
	//write Markers info to persistent file
	public static void save(HashSet<MarkerRecord> allMarkerRecords, HashSet<AnnotationRecord> allAnnotationRecords){
	if(Plugin.FIRST_TIME_RUN == true){  //if the plugin is run the first time, then the marker records need to be stored, or else, don't store marker records
		writeMarkerRecordIntoFile(allMarkerRecords);
	//	writeAnnotationRecordIntoFile(allAnnotationRecords);
	}
	}
	
	//after one iteration of finding callers of the sensitive operation rules, set them as the new set of 
	//sensitive operations (sensitive rules), writing them into file 
	//roundNum n means it is the n-th iteration. The initial rule is the sensitiveOperation.txt we pre-config
	//it is the 1-th iteration, when n is bigger, it means it is in higher level in the call hierarchy.
	public static void writeNewSensitiveRulesIntoFile(String newRuleFileName,
			HashSet<String> sensitiveOperations) {
        String fileName = Plugin.STATE_LOCATION + "/" + newRuleFileName;
		
		Iterator<String> iter = sensitiveOperations.iterator();
		String tmpStr = null;
		String fileDir = null;
		int nodeStart, nodeLength;
		String markerType;
		boolean isAnnotated;
		String seperator = Plugin.COMMA;
		StringBuffer strBuf = new StringBuffer();
		
		while(iter.hasNext()){
			tmpStr = iter.next();
			strBuf.append(tmpStr + "\n");
		//	System.out.println("fileDir=" + fileDir + ", nodeStart=" + nodeStart + ",nodeLength=" + nodeLength + ", markerType=" + markerType);
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

	public static void writeMarkerRecordIntoFile(HashSet<MarkerRecord> allMarkerRecords){
		
		String fileName = Plugin.STATE_LOCATION + "/" + Plugin.MARKER_RECORD_FILE;
		
		System.out.println("size of markers =========== " + Plugin.allMarkerRecords.size());
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
			System.out.println("fileDir=" + fileDir + ", nodeStart=" + nodeStart + ",nodeLength=" + nodeLength + ", markerType=" + markerType + ",isAnnotated=" + isAnnotated);
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

	public static void writeTableNamesIntoFile(String newTableNamesFileName,
			TreeSet<String> allTableNames) {
		  String fileName = Plugin.STATE_LOCATION + "/" + newTableNamesFileName;
			
			Iterator<String> iter = allTableNames.iterator();
			String tmpStr = null;
			String fileDir = null;
			int nodeStart, nodeLength;
			String markerType;
			boolean isAnnotated;
			String seperator = Plugin.COMMA;
			StringBuffer strBuf = new StringBuffer();
			
			while(iter.hasNext()){
				tmpStr = iter.next();
				strBuf.append(tmpStr + "\n");
			//	System.out.println("fileDir=" + fileDir + ", nodeStart=" + nodeStart + ",nodeLength=" + nodeLength + ", markerType=" + markerType);
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
	
	public static void writeMappingBetweenWarningsAndFiles(String newFileName,
			TreeMap<String, Integer> map) {
		  String fileName = Plugin.STATE_LOCATION + "/" + newFileName;
			
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
		//	strBuf.append("sum of calls = " + sum);
			
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
	
}
