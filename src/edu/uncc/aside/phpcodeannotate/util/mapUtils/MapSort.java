package edu.uncc.aside.phpcodeannotate.util.mapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapSort {
	public static Map sortByValue(Map unsortedMap){
		/*Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;*/
		List mapKeys = new ArrayList(unsortedMap.keySet());
		   List mapValues = new ArrayList(unsortedMap.values());
		   Collections.sort(mapValues);
		   Collections.sort(mapKeys);

		   LinkedHashMap sortedMap = new LinkedHashMap();

		   Iterator valueIt = mapValues.iterator();
		   while (valueIt.hasNext()) {
		       Object val = valueIt.next();
		       Iterator keyIt = mapKeys.iterator();

		       while (keyIt.hasNext()) {
		           Object key = keyIt.next();
		           String comp1 = unsortedMap.get(key).toString();
		           String comp2 = val.toString();

		           if (comp1.equals(comp2)){
		        	   unsortedMap.remove(key);
		               mapKeys.remove(key);
		               sortedMap.put((String)key, val);
		               break;
		           }

		       }

		   }
		   return sortedMap;
	}
	public static Map sortByKey(Map unsortedMap){
		Map sortedMap = new TreeMap();
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}

}
