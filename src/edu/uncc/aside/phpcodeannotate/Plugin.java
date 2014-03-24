package edu.uncc.aside.phpcodeannotate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.php.internal.core.ast.nodes.ASTNode;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.uncc.aside.phpcodeannotate.models.AnnotationRecord;
import edu.uncc.aside.phpcodeannotate.models.MarkerRecord;
import edu.uncc.aside.phpcodeannotate.models.NodePositionInfo;
import edu.uncc.aside.phpcodeannotate.models.Path;
import edu.uncc.aside.phpcodeannotate.models.SensitiveMethod;
import edu.uncc.aside.phpcodeannotate.util.RulesUtils;

/*import edu.uncc.aside.codeannotate.models.NodeAllPositionInfo;
import edu.uncc.aside.codeannotate.models.Path;
import edu.uncc.aside.codeannotate.models.Point;
import edu.uncc.aside.phpcodeannotate.listeners.CodeAnnotateElementChangeListener;*/


/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Jun Zhu (jzhu16 at uncc dot edu)
 */
public class Plugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "PHPCodeAnnotate"; //$NON-NLS-1$

	//public static final String JAVA_WEB_APP_NATURE = "org.eclipse.jdt.core.javanature";

	public static final String ANNOTATION_RELATIONSHIP_VIEW_ID = "relationships";

	public static final String ANNOTATION_ANSWER = "PHPCodeAnnotate.annotationAnswer";
	public static final String ANNOTATION_QUESTION = "PHPCodeAnnotate.annotationQuestion";
	public static final String ANNOTATION_QUESTION_CHECKED = "PHPCodeAnnotate.annotationQuestionChecked";
	public static final String SENSITIVE_ACCESSORS_CONFIG = "SensitiveInfoAccessors.xml";
	public static final String CONTROL_FLOW_EXCEPTIONS_CONFIG = "ControlFlowExceptions.xml";
	public static final String ASIDE_NODE_PROP_START = "aside_start";
	public static final String ASIDE_NODE_PROP_END = "aside_end";
	
	public static final String SENSITIVE_METHODS_PROCESSOR = "SensitiveMethodsProcess";
	public static final String EXCEPTION_METHODS_PROCESSOR = "ExceptionMethodsProcess";
	public static final String ANNOTATION_METHODS_PROCESSOR = "AnnotationMethodsProcess";
	
	public static final String COMMA = ", ";
	public static final String ITEM_SEPERATOR = " # ";
	
	public static final String CONFIG_FILE = "PHPCodeAnnotateConfig.txt";
	public static final String MARKER_RECORD_FILE = "MarkerRecords.txt";

	public static final int MARKER_FILE_NUM_OF_ITEMS = 7;
	
	public static final int ANNOTATON_FILE_NUM_OF_ITEMS = 6;

	public static final String ANNOTATION_RECORD_FILE = "AnnotationRecords.txt";

	public static String CurrentSensitiveDBTable = null;

	public static NodePositionInfo currentMarkerWorkedOnPositionInfo = null;

	public static String STATE_LOCATION = null;
	public static boolean FIRST_TIME_RUN = true;

	//public static boolean isManuallyStarted = false;
	//newly added Aug 4th
	public static List<ASTNode> annotatedASTNodes = new LinkedList<ASTNode>();
	//
	//public static final String TEST_CLASS_FILE = "/Roller4.0.1/src/org/apache/roller/weblogger/webservices/adminprotocol/AdminServlet.java";//, "/Roller4.0.1/src/org/apache/roller/weblogger/webservices/adminprotocol/AdminServlet.java/Handler.java", "/Roller4.0.1/src/org/apache/roller/weblogger/webservices/adminprotocol/BasicAuthenticator.java", "/Roller4.0.1/src/org/apache/roller/weblogger/webservices/adminprotocol/Authenticator.java"};
	// The shared instance
	private static Plugin plugin;

	public static Map<ImageDescriptor, Image> imageCache;

	public static Path annotationPath = null;

	public static IResource projectResource = null;
	
	public static HashSet<AnnotationRecord> allAnnotationRecords = new HashSet<AnnotationRecord>();
	public static HashSet<MarkerRecord> allMarkerRecords = new HashSet<MarkerRecord>();

	public static HashSet<SensitiveMethod> sensitiveOperations = new HashSet<SensitiveMethod>();   //all sensitive operations will be kept in this set
	
	public static HashSet<SensitiveMethod> sensitiveOperations_backup = new HashSet<SensitiveMethod>();   //backup of the initial all sensitive operations will be kept in this set
	
	public static HashSet<SensitiveMethod> sensitiveOperationsForAnotherIteration = new HashSet<SensitiveMethod>();
    
	public static HashSet<SensitiveMethod> sensitiveOperationsForCurrentIteration = new HashSet<SensitiveMethod>();
	
	public static HashSet<SensitiveMethod> sensitiveOperationsForCurrentIteration_backup = new HashSet<SensitiveMethod>();
	
	public static TreeSet<String> allTableNames = new TreeSet<String>();
	
	public static TreeMap<String, Integer> numberOfWarningsInEachFile = new TreeMap<String, Integer>(); //fileDir mapped to number of warnings
	
    public static TreeMap<String, Integer> numberOfWarningsRelatedToEachTable = new TreeMap<String, Integer>(); //table name mapped to number of warnings 
	
    public static HashSet<String> sensitive_DB_Tables = new HashSet<String>();
    
    public static TreeSet<String> sensitive_DB_Tables_AlphRanked = new TreeSet<String>();
    
    public static IMarker currentMarkerWorkedOn = null;

	public static TreeMap<String, Integer> numberOfAccessControlsInEachFile = new TreeMap<String, Integer>(); //keep access control functions in each file
	/**
	 * The constructor
	 */
	public Plugin() {
		imageCache = new HashMap<ImageDescriptor, Image>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		STATE_LOCATION = getDefault().getStateLocation().toString();
		
		sensitiveOperations = RulesUtils.getSensitiveOperations();
		sensitive_DB_Tables = RulesUtils.getSensitiveDBTables();
		
		sensitive_DB_Tables_AlphRanked = RulesUtils.getSensitiveDBTablesRanked();
		
		sensitiveOperationsForCurrentIteration = RulesUtils.getSensitiveOperations();
		System.out.println("sensitive_DB_Tables size = " + sensitive_DB_Tables.size());
		System.out.println("!!!sensitiveOperationsForCurrentIteration size = " + sensitiveOperationsForCurrentIteration.size());
		
		sensitiveOperations_backup = RulesUtils.getSensitiveOperations(); //(HashSet<SensitiveMethod>) sensitiveOperations.clone();
		sensitiveOperationsForCurrentIteration_backup = (HashSet<SensitiveMethod>) sensitiveOperationsForCurrentIteration.clone();
		if(getDefault() == null)
			System.out.println("in start() -- getDefault() == null");
		
		//gather statistics
				Iterator it = Plugin.sensitive_DB_Tables_AlphRanked.iterator();
				String tmp;
				System.out.println("The following is the sensitiveTables ranked alph:");
				while(it.hasNext()){
					tmp  = (String)it.next();
					System.out.println(tmp);
				}
				
				
		//before run, check whether the project has been run with the plugin before
	    
//		PreRunPluginConfig.config();
		
	/*	 JavaCore.addElementChangedListener(CodeAnnotateElementChangeListener
		 .getListener());*/
//		 ISaveParticipant saveParticipant = new CodeAnnotateSaveParticipant();
//		   ISavedState lastState =
//		      ResourcesPlugin.getWorkspace().addSaveParticipant(PLUGIN_ID, saveParticipant);
//
//		   if (lastState != null) {
//		      String saveFileName = lastState.lookup(new org.eclipse.core.runtime.Path("CodeAnnotate")).toString();
//		      File f = plugin.getStateLocation().append(saveFileName).toFile();
//		
//		   }

//		ResourcesPlugin.getWorkspace().addResourceChangeListener(
//				CodeAnnotateMarkerChangeListener.getListener());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		/*JavaCore.removeElementChangedListener(CodeAnnotateElementChangeListener
				.getListener());*/
//		ResourcesPlugin.getWorkspace().removeResourceChangeListener(
//				CodeAnnotateMarkerChangeListener.getListener());
		//testing use
		/*IMarker[] markers = projectResource.findMarkers(
				Plugin.ANNOTATION_QUESTION, false, IResource.DEPTH_ONE);
		System.out.println("before closing, number of question annotations is : " + markers.length);
		*/
		
		///save markers if applicable, and save annotations
	//	PostRunPluginConfig.config(allMarkerRecords, allAnnotationRecords); //temporarily moved to ...hander.java
		
		//scan the project, locate the file with the fileDir, and then create the marker,
		
		//////////////////
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Plugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */

	public static ImageDescriptor getImageDescriptor(String name) {
		String iconPath = "icons/";
		try {
			if(getDefault() == null)
				System.out.println("getDefault() == null");
			if(getDefault().getBundle() == null)
				System.out.println("getDefault().getBundle() == null");
			URL installURL = getDefault().getBundle().getEntry("/");
			URL url = new URL(installURL, iconPath + name);
			//System.out.println("URL = " + url);
			return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			// should not happen
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

/*	public AnnotationView getAnnotationView() {

		IWorkbench workbench = this.getWorkbench();

		if (workbench == null) {
			return null;
		}
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window == null) {
			return null;
		}

		IWorkbenchPage page = window.getActivePage();

		if (page == null) {
			return null;
		}

		IViewReference reference = page
				.findViewReference(Plugin.ANNOTATION_RELATIONSHIP_VIEW_ID);

		if (reference != null)
			return (AnnotationView) reference.getView(true);

		return null;
	}*/

	public void writeImportantState(File f) {
		System.err.println("writing in to " + f.getName());	
	}

	public static String getConfigFile() {
		// TODO Auto-generated method stub
		return CONFIG_FILE;
	}
}
