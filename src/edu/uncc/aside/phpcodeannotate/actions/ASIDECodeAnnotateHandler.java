package edu.uncc.aside.phpcodeannotate.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.search.SearchMatch;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.php.internal.core.ast.nodes.Program;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.uncc.aside.phpcodeannotate.Constants;
import edu.uncc.aside.phpcodeannotate.GatherStatistics;
import edu.uncc.aside.phpcodeannotate.InRunPluginDataSave;
import edu.uncc.aside.phpcodeannotate.Plugin;
import edu.uncc.aside.phpcodeannotate.PostRunPluginConfig;
import edu.uncc.aside.phpcodeannotate.models.AnnotationRecord;
import edu.uncc.aside.phpcodeannotate.models.MarkerRecord;
import edu.uncc.aside.phpcodeannotate.models.ModelRegistry;
import edu.uncc.aside.phpcodeannotate.models.Path;
import edu.uncc.aside.phpcodeannotate.models.PathCollector;
import edu.uncc.aside.phpcodeannotate.search.PHPSearchEngine;
import edu.uncc.aside.phpcodeannotate.util.RulesUtils;
import edu.uncc.aside.phpcodeannotate.util.Utils;
import edu.uncc.aside.phpcodeannotate.visitors.SensitiveOperationVisitor;
import edu.uncc.aside.phpcodeannotate.models.SensitiveMethod;

/**
 * 
 * @author Jun Zhu
 * 
 */
public class ASIDECodeAnnotateHandler extends AbstractHandler {

	private IWorkbenchPart targetPart;
	IProject selectProject = null;

	private PathCollector pathCollector;
	private List<Path> paths;

	@Override
	public Object execute(ExecutionEvent event) {
		
	//	Plugin.isManuallyStarted = true;

		System.out
				.println("ASIDECodeAnnotateHandler.java is ran ---first line");
		targetPart = HandlerUtil.getActivePart(event);

		IWorkbenchPartSite site = targetPart.getSite();
		ISelectionProvider selectionProvider = site.getSelectionProvider();
		if (selectionProvider == null) {
			return null;
		}
		ISelection selection = selectionProvider.getSelection();
		if (selection == null) {
			System.out.println("selectProject = ");
			return null;
		}
		IResource iRes = extractSelection(selection);
		if (iRes == null) {
			System.out.println("test == null");
			return null;
		}
		selectProject = iRes.getProject();
		if (selectProject == null) {
			System.out.println("selectProject == null");
			return null;
		}
		System.out.println("selectProject = " + selectProject.getName());

		// the following is temporarily added here
		pathCollector = ModelRegistry.getPathCollectorForProject(selectProject);

		if (pathCollector == null) {
			pathCollector = new PathCollector(selectProject);
		}

		paths = pathCollector.getAllPaths();

		if (paths == null)
			paths = Collections.synchronizedList(new ArrayList<Path>());

		System.out
				.println("ASIDECodeAnnotateHandler.java is ran -- start iterating files of the project");
		IScriptProject scriptProject = DLTKCore.create(selectProject);
		if(scriptProject == null){
			System.out.println("scirpt project == null");
			return null;
	    }
		int count = 1;
		
		//gather statistics
		//GatherStatistics.NumOfWarningsInEachFile();
		//while tablename = ...
		
		Utils.removeAllQuestionMarkers(iRes);
		Plugin.projectResource = iRes;
		Iterator ite = Plugin.sensitive_DB_Tables.iterator();
		String currentSensitiveTableName = null;
		//commented out Nov. 27
		/*while(ite.hasNext()){
		currentSensitiveTableName = (String) ite.next();
		Plugin.CurrentSensitiveDBTable = currentSensitiveTableName;	
		System.out.println("Current Table is=" + Plugin.CurrentSensitiveDBTable);*/
		
		String currentTableName;
		
		while(!Plugin.sensitive_DB_Tables_AlphRanked.isEmpty()){ //collect the warnings that comes from one table, one throughout iteration for each table, and put the results into the  

			currentTableName = Plugin.sensitive_DB_Tables_AlphRanked.first();
			count = 1;
			Plugin.allMarkerRecords.clear();
			
		while(Plugin.sensitiveOperationsForCurrentIteration != null && 
				Plugin.sensitiveOperationsForCurrentIteration.size() != 0){
		count++;
			System.out.println("-----------------begin round " + count);
			System.out.println("Plugin.sensitiveOperationsForCurrentIteration size =!!!" + Plugin.sensitiveOperationsForCurrentIteration.size());
			IScriptFolder[] folders = null;
		try {
			folders = scriptProject.getScriptFolders();
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("number of folders ==" + folders.length);
		
		/*String pattern = "Exec";
    	process(selectProject, pattern);*/
		
		////////////////
		Plugin.sensitiveOperationsForAnotherIteration.clear();
		Plugin.sensitiveOperationsForAnotherIteration = new HashSet();
	//	System.out.println("at the begining point: size of current " + Plugin.sensitiveOperationsForCurrentIteration.size());
	    int numOfFiles = 0;
	    for (IScriptFolder folder : folders) {
	    	String folderName = folder.getElementName();
	    	if(!Constants.PHPLibraryFolders.contains(folderName)){
				
				ISourceModule[] sourceModules = null;
				try {
					sourceModules = folder.getSourceModules();
				} catch (ModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				numOfFiles += sourceModules.length;
				
	    }
	    }
	/*    for(int i = 0; i < 20; i++)
	    System.out.println("files num = " + numOfFiles);*/
	    System.out.println("sum of folders =" + folders.length);
	    int currentFolderNum = 1;
		for (IScriptFolder folder : folders) {
        System.out.println("folder scanning = " + currentFolderNum + "/" + folders.length);
		String folderName = folder.getElementName();
			System.out.println("folder name = " + folderName);
			
			if(!Constants.PHPLibraryFolders.contains(folderName)){
			
			ISourceModule[] sourceModules = null;
			try {
				sourceModules = folder.getSourceModules();
			} catch (ModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (ISourceModule tmpSourceModule : sourceModules) {
				System.out.println("scanning " + tmpSourceModule.getElementName());
				// if it is not the first time to run CodeAnnotate on this project, then disable the scan function,
				//we have already pop the marker records from the file and displayed in Eclipse already. 
				if(false){} //temporarily testing
		/*		if(Plugin.FIRST_TIME_RUN == false){
					//simply display the markers based on the marker records in the file 
					String fileDir = tmpSourceModule.getResource().getFullPath().toString();
					System.out.println("all markers size = " + Plugin.allMarkerRecords.size());
					HashSet<MarkerRecord> markerRecordsInSingleFile = Utils.getMarkerRecordsForSingleFile(Plugin.allMarkerRecords, fileDir);
					Utils.createMarkersForSingleFile(markerRecordsInSingleFile, tmpSourceModule);
					
					HashSet<AnnotationRecord> annotationRecordsInSingleFile = Utils.getAnnotationRecordsForSingleFile(Plugin.allAnnotationRecords, fileDir);
					Utils.createAnnotationsForSingleFile(annotationRecordsInSingleFile, tmpSourceModule);
					
					System.out.println("finished creating markers for fileDir = " + fileDir + ", markerRecordsInSingleFile size = " + markerRecordsInSingleFile.size());
				}*/
			else{ //start scanning the files for sensitive operations
				
	//			System.out.println("isourcemodule being built = " + tmpSourceModule.getElementName().toLowerCase());
//				System.out.println("full path of the source module is ---" + tmpSourceModule.getResource().getFullPath().toString());
				
				SensitiveOperationVisitor visitor = new SensitiveOperationVisitor(
						tmpSourceModule, Plugin.sensitiveOperationsForCurrentIteration, Plugin.sensitiveOperationsForAnotherIteration, Plugin.sensitiveOperations);
				Program root = null;
				try {
					root = Utils.getCompilationUnit(tmpSourceModule);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.err.println("root = util.getcompilationUnit() throws exception!");
					e.printStackTrace();
				}
		//		System.out.println("begin of traverseTopDown");
				if(root == null){
					System.err.println("tmpSourceModule name = " + tmpSourceModule.getElementName() + " in " + tmpSourceModule.getPath().toString());
					System.err.println("root == null");
					//return null;
				}
				root.traverseTopDown(visitor);
				
				Plugin.sensitiveOperations = visitor.getSensitiveOperations();
				Plugin.sensitiveOperationsForAnotherIteration = visitor.getSensitiveOperationsForAnotherIteration();
			}
			}
			}
			currentFolderNum ++;
		}
		Plugin.sensitiveOperationsForCurrentIteration.clear();
		Plugin.sensitiveOperationsForCurrentIteration = new HashSet();
		
	//	System.out.println("Plugin.sensitiveOperationsForAnotherIteration size after iteration =" + Plugin.sensitiveOperationsForAnotherIteration.size());
		Plugin.sensitiveOperationsForCurrentIteration = (HashSet<SensitiveMethod>) Plugin.sensitiveOperationsForAnotherIteration.clone();
	//	System.out.println("after assignment, Plugin.sensitiveOperationsForCurrentIteratio size = " + Plugin.sensitiveOperationsForCurrentIteration.size());
		
		/*String newRuleFileName = "newRulesForIteration" + count + "th.txt";
		InRunPluginDataSave.writeNewSensitiveRulesIntoFile(newRuleFileName, Plugin.sensitiveOperationsForCurrentIteration);
		
		String mappingFileName = "numOfWarningsInEachFileInIteration" + (count-1) + "th.txt";;
		InRunPluginDataSave.writeMappingBetweenWarningsAndFiles(mappingFileName, Plugin.numberOfWarningsInEachFile);
		
		if(count == 2){
		String newTableNamesFileName = "tableNamesEncounteredInIteration" + (count-1) + "th.txt";
		InRunPluginDataSave.writeTableNamesIntoFile(newTableNamesFileName, Plugin.allTableNames);
		
		String mappingFileName2 = "numOfWarningsRelatedToEachTableForIteration" + (count-1) + "th.txt";
		InRunPluginDataSave.writeMappingBetweenWarningsAndFiles(mappingFileName2, Plugin.numberOfWarningsRelatedToEachTable);
		}*/
		
		PostRunPluginConfig.writeMarkerRecordIntoFile(Plugin.allMarkerRecords, count, currentTableName);
		}
		
		
		////newly added
		//remove the first table name in the treeset so that we focus on the next table in the next iteration.
		Plugin.sensitive_DB_Tables_AlphRanked.pollFirst();
		Plugin.sensitiveOperationsForCurrentIteration = RulesUtils.getSensitiveOperations();
	}
		
		//commented out Nov. 27
		/*GatherStatistics.writeMarkersForEachTable(Plugin.allMarkerRecords, Plugin.CurrentSensitiveDBTable);
		Plugin.allMarkerRecords.clear();
		Plugin.allMarkerRecords = new HashSet();
		count = 1;
		Plugin.sensitiveOperationsForCurrentIteration.clear();
		Plugin.sensitiveOperationsForCurrentIteration = new HashSet();
		Plugin.sensitiveOperationsForCurrentIteration = (HashSet<SensitiveMethod>) Plugin.sensitiveOperationsForCurrentIteration_backup.clone();
		Plugin.sensitiveOperations.clear();
		Plugin.sensitiveOperations = new HashSet();
		Plugin.sensitiveOperations = (HashSet<SensitiveMethod>)Plugin.sensitiveOperations_backup.clone();
		}*/
		// above is temporarily added.

		//below are temporarily added for the analysis use
//		GatherStatistics.filesWithoutRequiredAccessControls(Plugin.numberOfWarningsInEachFile, Plugin.numberOfAccessControlsInEachFile);

		
		/*
		 * Use a Job to attach a {@link CodeAnnotateDocumentEditListener} to
		 * each and every IDocument that is related to a ICompilationUnit in the
		 * selected project
		 */
		/*
		 * Job job = new MountListenerJob("Mount listener to Java file",
		 * JavaCore.create(selectProject)); job.setPriority(Job.INTERACTIVE);
		 * job.schedule();
		 */

		/* Delegates all heavy lifting to {@link PathFinder} */
		/*Job heavy_job = new Job("Finding paths in Project: "
				+ selectProject.getName()) {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				try {
					Plugin.getDefault().getWorkbench().getDisplay()
							.asyncExec(new Runnable() {

								@Override
								public void run() {
									// PathFinder.getInstance(selectProject).run(monitor);

								}

							});

				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}

		};
		heavy_job.setPriority(Job.LONG);
		heavy_job.schedule();
*/
		System.out.println("finished scanning, marker records saved");
		//PostRunPluginConfig.config(Plugin.allMarkerRecords, Plugin.allAnnotationRecords);
	//	PostRunPluginConfig.writeMarkerRecordIntoFile(Plugin.allMarkerRecords, count);
//		GatherStatistics.readWarningStatistics(Plugin.sensitive_DB_Tables_AlphRanked, "Update_Level5.txt");
		return null;
	}
	public void process(IProject project, String pattern){
		//test code, right now, I used the PHPSearchEngine class cut from another project
				/////////Note: there is an real library API org.eclipse.php.internal.core.util.PHPSearchEngine. I need to check details
		System.out.println("begining a iteration for pattern " + pattern);
				String elementName = null;
				SearchMatch[] newMatches = null;
				SearchMatch[] matches = PHPSearchEngine.findMethodCall(
						pattern, PHPSearchEngine
								.createProjectScope(project));
				//must distinguish between method declaration and method call!!!!
				IModelElement iModelElement;
				Object obj = null;
				if(matches.length > 0){
				for (SearchMatch match : matches) {
					obj = match.getElement();
					if(obj instanceof IModelElement){
						iModelElement = (IModelElement)obj;
						elementName = iModelElement.getElementName();
						System.out.println("iModelElement.getElementName() = " + elementName + " --path= " + iModelElement.getPath().toString());
					    if(!elementName.equals(pattern)){ //avoid recursive, or iterative method, temp implementation, should be modified!!!!
						pattern = elementName;
						System.out.println("begining next iteration");
					    process(project, pattern);
					    System.out.println("return from an iteration");
					    }else{//it is recursive/iterative, we regard it as the top level method call (temp implementation!!!!!)
					    	System.out.println("Top Level(recursive reason): " + "iModelElement.getElementName() = " + elementName + " --path= " + iModelElement.getPath().toString());
					    }
					}
					
				}
				}else{ //if there is no callers, then means it is the top level 
					
					System.out.println("Top Level(real reason): " + "pattern = " + pattern);
					
				}
				System.out.println("end of an iteration");
				
	}

	public IResource extractSelection(ISelection sel) {
		if (!(sel instanceof IStructuredSelection))
			return null;
		IStructuredSelection ss = (IStructuredSelection) sel;
		Object element = ss.getFirstElement();
		if (element instanceof IResource)
			return (IResource) element;
		if (!(element instanceof IAdaptable))
			return null;
		IAdaptable adaptable = (IAdaptable) element;
		Object adapter = adaptable.getAdapter(IResource.class);
		return (IResource) adapter;
	}
	/*
	 * class MountListenerJob extends Job {
	 * 
	 * IJavaProject projectOfInterest; IBufferChangedListener listener;
	 * ArrayList<ICompilationUnit> totalUnits;
	 * 
	 * public MountListenerJob(String name, IJavaProject project) { super(name);
	 * projectOfInterest = project; listener = new
	 * CodeAnnotateDocumentEditListener(); totalUnits = new
	 * ArrayList<ICompilationUnit>(); }
	 * 
	 * @Override protected IStatus run(IProgressMonitor monitor) { try {
	 * monitor.beginTask(
	 * "Mounting a CodeAnnotateDocumentEditListener to a Java file",
	 * numberOfJavaFiles(projectOfInterest));
	 * 
	 * for (ICompilationUnit unit : totalUnits) {
	 * 
	 * if (unit == null || !unit.exists()) continue;
	 * 
	 * if (!unit.isOpen()) { unit.open(monitor); }
	 * 
	 * unit.becomeWorkingCopy(monitor);
	 * 
	 * IBuffer buffer = (unit).getBuffer(); if (buffer != null) {
	 * buffer.addBufferChangedListener(listener); } if (monitor.isCanceled()) {
	 * return Status.CANCEL_STATUS; } }
	 * 
	 * } catch (JavaModelException e) { e.printStackTrace(); return
	 * Status.CANCEL_STATUS; } finally { monitor.done(); }
	 * 
	 * return Status.OK_STATUS; }
	 * 
	 * private int numberOfJavaFiles(IJavaProject project) throws
	 * JavaModelException {
	 * 
	 * int count = 0; IPackageFragment[] fragments = projectOfInterest
	 * .getPackageFragments(); for (IPackageFragment fragment : fragments) {
	 * ICompilationUnit[] units = fragment.getCompilationUnits(); for
	 * (ICompilationUnit unit : units) { totalUnits.add(unit); count++; } }
	 * 
	 * return count; } }
	 */
}
