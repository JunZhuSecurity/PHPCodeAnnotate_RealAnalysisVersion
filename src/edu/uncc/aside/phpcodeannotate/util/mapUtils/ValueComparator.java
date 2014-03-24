package edu.uncc.aside.phpcodeannotate.util.mapUtils;
import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator {
	Map map;
	
	public ValueComparator(Map map){
		this.map = map;
	}
	
	public int compare(Object key1, Object key2){
		Comparable value1 = (Comparable)map.get(key1);
		Comparable value2 = (Comparable)map.get(key2);
		
		return value1.compareTo(value2);
	}

}
