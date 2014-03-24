//package edu.uncc.aside.phpcodeannotate.visitors;
//
//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.resources.IResource;
//import org.eclipse.dltk.ast.references.VariableReference;
//import org.eclipse.dltk.compiler.problem.DefaultProblem;
//import org.eclipse.dltk.compiler.problem.IProblem;
//import org.eclipse.dltk.compiler.problem.ProblemSeverities;
//import org.eclipse.dltk.core.ISourceModule;
//import org.eclipse.dltk.core.builder.IBuildContext;
//import org.eclipse.php.internal.core.ast.nodes.FunctionInvocation;
//import org.eclipse.php.internal.core.ast.nodes.MethodInvocation;
//import org.eclipse.php.internal.core.ast.nodes.Program;
//import org.eclipse.php.internal.core.ast.nodes.Variable;
//import org.eclipse.php.internal.core.ast.nodes.VariableBase;
//import org.eclipse.php.internal.core.compiler.ast.nodes.ArrayVariableReference;
//import org.eclipse.php.internal.core.compiler.ast.nodes.PHPCallExpression;
//import org.eclipse.php.internal.core.compiler.ast.nodes.ReflectionStaticMethodInvocation;
//import org.eclipse.php.internal.core.compiler.ast.nodes.StaticMethodInvocation;
//import org.eclipse.php.internal.core.compiler.ast.visitor.PHPASTVisitor;
//
//import edu.uncc.aside.phpcodeannotate.models.ModelRegistry;
//import edu.uncc.aside.phpcodeannotate.models.Path;
//import edu.uncc.aside.phpcodeannotate.models.PathCollector;
//import edu.uncc.aside.phpcodeannotate.models.Point;
//import edu.uncc.aside.phpcodeannotate.util.*;
///**
// * This visitor searches for unsafe places that can be used for access control attacks, and
// * notifies developer about them. Unsafe places are considered to be:
// * <ul>
// *  <li>database operations, such as $db->query(). $db->exec()</li>
// *  <li>Add more...</li>
// * </ul>
// 
//public class SensitiveOperationVisitor extends PHPASTVisitor {
//
//	private IBuildContext context;
//	private PathCollector collector;
//	private IProject projectOfInterest;
//	private ISourceModule iSourceModule;
//	private Program unitOfInterest;
//	
//	public SensitiveOperationVisitor(IBuildContext context, ISourceModule sourceModule) {
//		this.context = context;
//		projectOfInterest = sourceModule.getResource().getProject();
//		this.iSourceModule = sourceModule; 
//		try {
//			this.unitOfInterest = Utils.getCompilationUnit(sourceModule);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		collector = ModelRegistry.getPathCollectorForProject(projectOfInterest);
//	}
//	
//	@SuppressWarnings("restriction")
//	public boolean visit(PHPCallExpression node) throws Exception {
//		System.out.println("enter visit");
//		// Check the parent: it should be either isset() or htmlentities() call
//		if (node.getReceiver() == null) { // if this is a function call, not method
//			String funcName = node.getName();
//			System.out.println("v =" + funcName);
//			if ("exec".equalsIgnoreCase(funcName) || "query".equalsIgnoreCase(funcName)) {
//
//				System.out.println("FOUND = " + funcName);
//			/*	context.getProblemReporter().reportProblem(
//					new DefaultProblem(
//						context.getFile().getName(),
//						"Unsafe use of " + ": possible access control attack",
//						IProblem.Unclassified,
//						new String[0],
//						ProblemSeverities.Warning,
//						node.sourceStart(),
//						node.sourceEnd(),
//						context.getLineTracker().getLineNumberOfOffset(node.sourceStart()))
//				);*/
//				IResource resource = context.getSourceModule().getUnderlyingResource();
//				Utils.markAccessor(node, resource, context);
//				//newly added
//				Point accessor = new Point(node, unitOfInterest, resource);
//				//since it is in the same file for php, so we use same 'resource' here, and use the root of the AST as the entrance
//				Point entrance = new Point(unitOfInterest, unitOfInterest, resource); 
//
//				final Path path = new Path(entrance, accessor, null);
//				collector.addPath(path);
//				System.out.println("Just created and added a new path "
//						+ path.getPathID());
//			}
//		}
//		///////add by myself
//		//System.out.println("-->" + node.toString() + " --name=" + node.getName());
//		////
//		return visitGeneral(node);
//	}
//	public boolean endvisit(PHPCallExpression node) throws Exception {
//
//		endvisitGeneral(node);
//		return true;
//	}
//	public boolean visit(ArrayVariableReference s) throws Exception {
//		return super.visit(s);
//		
//	}
//	
///*	@SuppressWarnings("restriction")
//	public boolean visit(FunctionInvocation node) throws Exception {
//		System.out.println("enter visit");
//		// Check the parent: it should be either isset() or htmlentities() call
//		//if (node.getReceiver() == null) { // if this is a function call, not method
//		String funcName = node.getFunctionName().toString();
//		
//			if ("exec".equalsIgnoreCase(funcName) || "query".equalsIgnoreCase(funcName)) {
//
//				System.out.println("FOUND = " + funcName);
//				context.getProblemReporter().reportProblem(
//					new DefaultProblem(
//						context.getFile().getName(),
//						"Unsafe use of " + ": possible access control attack",
//						IProblem.Unclassified,
//						new String[0],
//						ProblemSeverities.Warning,
//						node.getStart(),
//						node.getEnd(),
//						context.getLineTracker().getLineNumberOfOffset(node.getStart()))
//				);
//				IResource resource = context.getSourceModule().getUnderlyingResource();
//				Utils.markAccessor(node, resource, context);
//			}
//		//}
//		///////add by myself
//		//System.out.println("-->" + node.toString() + " --name=" + node.getName());
//		////
//		return false;
//	}*/
//	
//
//}

package edu.uncc.aside.phpcodeannotate.visitors;

/*
 Refactor4PDT an Eclipse PDT plugin to refactor php code.
 Copyright (C) 2008-2009  Vivek Narendra


 This program is free software: you can redistribute it and/or modify 
 it under the terms of the GNU General Public License as published by 
 the Free Software Foundation, either version 3 of the License, or 
 at your option any later version. 

 This program is distributed in the hope that it will be useful, 
 but WITHOUT ANY WARRANTY; without even the implied warranty of 
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 GNU General Public License for more details. 

 You should have received a copy of the GNU General Public License 
 along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.ast.references.VariableReference;
import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.IProblem;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.builder.IBuildContext;
import org.eclipse.php.internal.core.ast.nodes.ClassDeclaration;
import org.eclipse.php.internal.core.ast.nodes.FieldAccess;
import org.eclipse.php.internal.core.ast.nodes.FunctionInvocation;
import org.eclipse.php.internal.core.ast.nodes.MethodInvocation;
import org.eclipse.php.internal.core.ast.nodes.Program;
import org.eclipse.php.internal.core.ast.nodes.Variable;
import org.eclipse.php.internal.core.ast.nodes.VariableBase;

import edu.uncc.aside.phpcodeannotate.Constants;
import edu.uncc.aside.phpcodeannotate.Plugin;
import edu.uncc.aside.phpcodeannotate.models.MarkerRecord;
import edu.uncc.aside.phpcodeannotate.models.MarkerType;
import edu.uncc.aside.phpcodeannotate.models.ModelRegistry;
import edu.uncc.aside.phpcodeannotate.models.NodePositionInfo;
import edu.uncc.aside.phpcodeannotate.models.Path;
import edu.uncc.aside.phpcodeannotate.models.PathCollector;
import edu.uncc.aside.phpcodeannotate.models.Point;
import edu.uncc.aside.phpcodeannotate.models.SensitiveMethod;
import edu.uncc.aside.phpcodeannotate.util.*;
import edu.uncc.aside.phpcodeannotate.log.Log;
import org.eclipse.php.internal.core.ast.nodes.ASTNode;
import org.eclipse.php.internal.core.ast.nodes.FunctionDeclaration;
import org.eclipse.php.internal.core.ast.nodes.Identifier;
import org.eclipse.php.internal.core.ast.visitor.ApplyAll;

public class SensitiveOperationVisitor extends ApplyAll {

	private IBuildContext context;
	private PathCollector collector;
	private IProject projectOfInterest;
	private ISourceModule iSourceModule;
	private Program unitOfInterest;
	private HashSet<SensitiveMethod> sensitiveOperationsForAnotherIteration = new HashSet<SensitiveMethod>();
	private HashSet<SensitiveMethod> sensitiveOperationsForCurrentIteration = new HashSet<SensitiveMethod>();
	private HashSet<SensitiveMethod> sensitiveOperations = new HashSet<SensitiveMethod>();
	
	public SensitiveOperationVisitor(ISourceModule sourceModule, HashSet<SensitiveMethod> sensitiveOperationsForCurrentIteration, 
			HashSet<SensitiveMethod> sensitiveOperationsForAnotherIteration, 
			HashSet<SensitiveMethod> sensitiveOperations) {

		projectOfInterest = sourceModule.getResource().getProject();
		this.iSourceModule = sourceModule; 
		this.sensitiveOperationsForCurrentIteration = sensitiveOperationsForCurrentIteration;
		//System.out.println("this.sensitiveOperationsForCurrentIteration  size = " + this.sensitiveOperationsForCurrentIteration.size());
		this.sensitiveOperations = sensitiveOperations;
		this.sensitiveOperationsForAnotherIteration = sensitiveOperationsForAnotherIteration;
		try {
			this.unitOfInterest = Utils.getCompilationUnit(sourceModule);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		collector = ModelRegistry.getPathCollectorForProject(projectOfInterest);
	}
	
	public HashSet<SensitiveMethod> getSensitiveOperations(){
		return this.sensitiveOperations;
	}
	
	public HashSet<SensitiveMethod> getSensitiveOperationsForAnotherIteration(){
		return this.sensitiveOperationsForAnotherIteration;
	}
	
	@SuppressWarnings({ "restriction", "deprecation" })
	public boolean visit(FunctionInvocation node) {
	FunctionDeclaration functionDecl = null;
	ClassDeclaration classDecl = null;
	IResource resource = null;
	resource = iSourceModule.getResource();
//	System.out.println("enter visit = " + node.toString());
	// Check the parent: it should be either isset() or htmlentities() call
	//if (node.getReceiver() == null) { // if this is a function call, not method
	if(Utils.isSecurityInterest(resource, node, sensitiveOperationsForCurrentIteration)){
//		System.out.println("initially !!");
		if((functionDecl = Utils.getParentFunctionDeclaration(node)) == null && 
				(classDecl = Utils.getParentClassDeclaration(node)) == null){
//			System.out.println("parent function declaration and parent class declaration are ");
//		System.out.println("found one matched functioninvocation = " + node.getFunctionName().getName().toString());
		
		////////////////////probably, just keep all the nodes that should be marked, and create markers later, to prevent duplicated ones
		if (resource == null) {
			System.err.println("Check here...");
		}
		Utils.markAccessor(node, resource);
		
		//create markerRecord for this marker, and insert into the set of markers
		String fileDir = resource.getFullPath().toString();
//		System.out.println("fileDir of the new marker is: " + fileDir);
		int nodeStart = node.getStart();
		int nodeLength = node.getLength();
		NodePositionInfo nodePositionInfo = new NodePositionInfo(fileDir, nodeStart, nodeLength);
		MarkerRecord markerRecord = new MarkerRecord(nodePositionInfo, MarkerType.ACCESS_CONTROL, false);
		Plugin.allMarkerRecords.add(markerRecord);
		
		//newly added
		//gather statistics
		if(Plugin.numberOfWarningsInEachFile.containsKey(fileDir))
			Plugin.numberOfWarningsInEachFile.put(fileDir, Plugin.numberOfWarningsInEachFile.get(fileDir) + 1);
		else
			Plugin.numberOfWarningsInEachFile.put(fileDir, 1);
	//	System.out.println("fileDir!=" + fileDir);
		
		Point accessor = new Point(node, unitOfInterest, resource);
		//since it is in the same file for php, so we use same 'resource' here, and use the root of the AST as the entrance
		Point entrance = new Point(unitOfInterest, unitOfInterest, resource); 

		final Path path = new Path(entrance, accessor, null);
		collector.addPath(path);
//		System.out.println("Just created and added a new path "	+ path.getPathID());
		}
		
		else if((functionDecl = Utils.getParentFunctionDeclaration(node)) != null){ //within a functionDeclaration
			//get the parent function declaration, and then see if the parent function is called somewhere, 
			//if yes, then continue analyzing the parent one, get its call sites
			//then add them as the new rules, and begin another iteration
//		System.out.println("(functionDecl = Utils.getParentFunctionDeclaration(node)) != null");
//		System.out.println("functionDecl = " + functionDecl.getFunctionName().getName());
		
		IResource tmpResource = iSourceModule.getResource();
		if (tmpResource == null) {
			System.err.println("Check here...");
			
		}
		String fileDir = tmpResource.getFullPath().toString();
//		System.out.println("fileDir of the new marker is: " + fileDir);
		int nodeStart = functionDecl.getStart();
		int nodeLength = functionDecl.getLength();
		NodePositionInfo nodePositionInfo = new NodePositionInfo(
				fileDir, nodeStart, nodeLength);
		String functionDeclName = functionDecl.getFunctionName()
				.getName();
		String classDelName;
		if ((classDecl = Utils.getParentClassDeclaration(node)) != null)
			classDelName = classDecl.getName().getName();
		else
			classDelName = Constants.isPureFunction;
        SensitiveMethod testMethod = new SensitiveMethod(classDelName, functionDeclName);
//        System.out.println("sensfunction in functionDecl");
		if (!sensitiveOperations.contains(testMethod)
				&& !Utils
						.isInsensitiveAsSetInPreconfiguration(testMethod)) {
					
			sensitiveOperations.add(testMethod);
			if (!sensitiveOperationsForAnotherIteration
					.contains(testMethod)) {
				sensitiveOperationsForAnotherIteration
						.add(testMethod);
//				System.out.println("sensitiveOperationsForAnotherIteration.add(functionDeclName) = "+ functionDeclName);
				// System.out.println("sensitiveOperationsForAnotherIteration size"
				// + sensitiveOperationsForAnotherIteration.size());
			}
		} 
		}else if((classDecl = Utils.getParentClassDeclaration(node)) != null){ //not within a functionDeclation but within a class declaration
		//NOTE: do nothing for now, actually, if the class is newed or instantiated, it is possible that function invocation is executed,
			//not clear now, take a look later. 
		}
	}
	
	
	//temporarily added, to track the function calls of require_capability()!!!!!!!!!!!!!!!!!!!
/*	else if(Utils.isPossibleAccessControlRoutine(resource, node, Constants.ACCESS_CONTROL_FUNCTIONS)){

		if (resource == null) {
			System.err.println("Check here...");
			
		}
	
		String fileDir = resource.getFullPath().toString();
		int nodeStart = node.getStart();
		int nodeLength = node.getLength();
		NodePositionInfo nodePositionInfo = new NodePositionInfo(fileDir, nodeStart, nodeLength);
		MarkerRecord markerRecord = new MarkerRecord(nodePositionInfo, MarkerType.ACCESS_CONTROL, false);
		Plugin.allMarkerRecords.add(markerRecord);
		
		
		//newly added
		//gather statistics
		if(Plugin.numberOfAccessControlsInEachFile.containsKey(fileDir))
			Plugin.numberOfAccessControlsInEachFile.put(fileDir, Plugin.numberOfAccessControlsInEachFile.get(fileDir) + 1);
		else
			Plugin.numberOfWarningsInEachFile.put(fileDir, 1);
		}
*/
		
	return false;
}
	
	@SuppressWarnings({ "restriction", "deprecation" })
	public boolean visit(MethodInvocation node) {

		FunctionDeclaration functionDecl = null;
		ClassDeclaration classDecl = null;
		IResource resource = null;
		resource = iSourceModule.getResource();
	//	System.out.println("enter visit");
		// Check the parent: it should be either isset() or htmlentities() call
		// if (node.getReceiver() == null) { // if this is a function call, not
		// method
		if (Utils.isSecurityInterest(resource, node,
				sensitiveOperationsForCurrentIteration)) {

			if ((functionDecl = Utils.getParentFunctionDeclaration(node)) == null
					&& (classDecl = Utils.getParentClassDeclaration(node)) == null) {
				// System.out.println("parent function declaration and parent class declaration are ");
			//	System.out.println("found one matched functioninvocation = " + node.toString());
	//			System.out.println("found one matched functioninvocation = " + node.getMethod().getFunctionName().getName().toString());
				// //////////////////probably, just keep all the nodes that
				// should be marked, and create markers later, to prevent
				// duplicated ones

				if (resource == null) {
					System.err.println("Check here...");
				}
				Utils.markAccessor(node, resource);

				// create markerRecord for this marker, and insert into the set
				// of markers
				String fileDir = resource.getFullPath().toString();
				// System.out.println("fileDir of the new marker is: " +
				// fileDir);
				int nodeStart = node.getStart();
				int nodeLength = node.getLength();
				NodePositionInfo nodePositionInfo = new NodePositionInfo(
						fileDir, nodeStart, nodeLength);
				MarkerRecord markerRecord = new MarkerRecord(nodePositionInfo,
						MarkerType.ACCESS_CONTROL, false);
				Plugin.allMarkerRecords.add(markerRecord);
System.out.println("Plugin.allMarkerRecords size = " + Plugin.allMarkerRecords.size());
				// newly added
				// gather statistics
			/*	if (Plugin.numberOfWarningsInEachFile.containsKey(fileDir))
					Plugin.numberOfWarningsInEachFile.put(fileDir,
							Plugin.numberOfWarningsInEachFile.get(fileDir) + 1);
				else
					Plugin.numberOfWarningsInEachFile.put(fileDir, 1);*/
	//			System.out.println("fileDir!=" + fileDir);

				Point accessor = new Point(node, unitOfInterest, resource);
				// since it is in the same file for php, so we use same
				// 'resource' here, and use the root of the AST as the entrance
				Point entrance = new Point(unitOfInterest, unitOfInterest,
						resource);

				final Path path = new Path(entrance, accessor, null);
				collector.addPath(path);
//				System.out.println("Just created and added a new path " + path.getPathID());
			}

			else if ((functionDecl = Utils.getParentFunctionDeclaration(node)) != null) { // within
																							// a
																							// functionDeclaration
				// get the parent function declaration, and then see if the
				// parent function is called somewhere,
				// if yes, then continue analyzing the parent one, get its call
				// sites
				// then add them as the new rules, and begin another iteration
				// System.out.println("(functionDecl = Utils.getParentFunctionDeclaration(node)) != null");
				// System.out.println("functionDecl = " +
				// functionDecl.getFunctionName().getName());

				IResource tmpResource = iSourceModule.getResource();
				if (tmpResource == null) {
					System.err.println("Check here...");

				}
				String fileDir = tmpResource.getFullPath().toString();
				// System.out.println("fileDir of the new marker is: " +
				// fileDir);
				int nodeStart = functionDecl.getStart();
				int nodeLength = functionDecl.getLength();
				NodePositionInfo nodePositionInfo = new NodePositionInfo(
						fileDir, nodeStart, nodeLength);
				String functionDeclName = functionDecl.getFunctionName()
						.getName();
				String classDelName;
				if ((classDecl = Utils.getParentClassDeclaration(node)) != null)
					classDelName = classDecl.getName().getName();
				else
					classDelName = Constants.isPureFunction;
                SensitiveMethod testMethod = new SensitiveMethod(classDelName, functionDeclName);
                
				if (!sensitiveOperations.contains(testMethod)
						&& !Utils
								.isInsensitiveAsSetInPreconfiguration(testMethod)) {
					// if it is a brand new rule (not tried before)
					// put it in the Plugin.sensitiveOperations, as well as in
					// the sensitiveOperationsForAnotherIteration
					// which will be used in next iteration of scanning for
					// higher level of sensitive operations
					
					//need to get the class name, or else, set it as a pureFunction dispatcher type
					
					sensitiveOperations.add(testMethod);
					if (!sensitiveOperationsForAnotherIteration
							.contains(testMethod)) {
						sensitiveOperationsForAnotherIteration
								.add(testMethod);
						// System.out.println("sensitiveOperationsForAnotherIteration.add(functionDeclName) = "
						// + functionDeclName);
						// System.out.println("sensitiveOperationsForAnotherIteration size"
						// + sensitiveOperationsForAnotherIteration.size());
					}
				} else {

				}
			} else if ((classDecl = Utils.getParentClassDeclaration(node)) != null) { // not
																						// within
																						// a
																						// functionDeclation
																						// but
																						// within
																						// a
																						// class
																						// declaration
				// NOTE: do nothing for now, actually, if the class is newed or
				// instantiated, it is possible that function invocation is
				// executed,
				// not clear now, take a look later.
			}

		}
		return false;
	}
	
	
	/*@SuppressWarnings({ "restriction", "deprecation" })
	public boolean visit(MethodInvocation node) {
	System.out.println("enter visit");
	// Check the parent: it should be either isset() or htmlentities() call
	//if (node.getReceiver() == null) { // if this is a function call, not method
	if(Utils.isSecurityInterest(node)){
	    
		IResource resource = null;
		
		resource = iSourceModule.getResource();
		if (resource == null) {
			System.err.println("Check here...");
			
		}
		Utils.markAccessor(node, resource);
		
		//create markerRecord for this marker, and insert into the set of markers
		String fileDir = resource.getFullPath().toString();
		System.out.println("fileDir of the new marker is: " + fileDir);
		int nodeStart = node.getStart();
		int nodeLength = node.getLength();
		NodePositionInfo nodePositionInfo = new NodePositionInfo(fileDir, nodeStart, nodeLength);
		MarkerRecord markerRecord = new MarkerRecord(nodePositionInfo, MarkerType.ACCESS_CONTROL, false);
		Plugin.allMarkerRecords.add(markerRecord);
		
		
		//newly added
    	Point accessor = new Point(node, unitOfInterest, resource);
		//since it is in the same file for php, so we use same 'resource' here, and use the root of the AST as the entrance
		Point entrance = new Point(unitOfInterest, unitOfInterest, resource); 

		final Path path = new Path(entrance, accessor, null);
		collector.addPath(path);
		System.out.println("Just created and added a new path "	+ path.getPathID());
		
		}
	return false;
}*/
	
	
	
	/*Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L2, getClass(),
			"visit(MethodInvocation node)",
			"MethodInvocation getFunctionName type: ", node.getMethod()
					.getFunctionName().getFunctionName().getType());*/
	
	
	
	/*if (instanceIdentifier != null && methodIdentifier != null) {
		methodInvocation.put(methodIdentifier, instanceIdentifier);
		Log.write(net.sourceforge.refactor4pdt.log.Log.Level.INFO_L2, getClass(),
				"visit(MethodInvocation node)", (new StringBuilder(
						"MethodInvocation : ")).append(
						methodIdentifier.toString()).append("  ---->  ")
						.append(instanceIdentifier.toString()).toString());
	}
	return false;
	
	FunctionInvocation functionInvocation = node.getMethod();
	VariableBase dispatcher = node.getDispatcher();
	String dispatcherStr = null;
dispatcher.IDENTIFIER;
	if(dispatcher instanceof Variable)
	{
		((Variable)dispatcher).getName().
	}
	String dispatcherStr = dispatcher;
	Variable t = (Variable) dispatcher;
	String funcName = functionInvocation.getFunctionName().toString();
	
	System.out.println("dispatcherStr = " + dispatcherStr + " , funcName" + funcName);
	if(dispatcherStr.equals("db")){
	    
	    //////add by myself
		System.out.println(" --name=" + funcName);
		
		if ("exec".equalsIgnoreCase(funcName) || "query".equalsIgnoreCase(funcName)) {
			System.out.println("FOUND = " + funcName);
			context.getProblemReporter().reportProblem(
				new DefaultProblem(
					context.getFile().getName(),
					"Unsafe use of " + ": possible access control attack",
					IProblem.Unclassified,
					new String[0],
					ProblemSeverities.Warning,
					node.getStart(),
					node.getEnd(),
					context.getLineTracker().getLineNumberOfOffset(node.getStart()))
			);
			IResource resource = null;
			try {
				resource = context.getSourceModule().getUnderlyingResource();
			} catch (ModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Utils.markAccessor(node, resource, context);
		}
    
	}*/

	public void endVisit(FunctionInvocation node){
		super.endVisit(node);
	}
	
	public void endVisit(MethodInvocation node) {
		super.endVisit(node);
		//endVisitNode(node);
	}

	/*public boolean hasDangerousFunctionCall() {
		return dangerousFunctionCall.size() != 0;
	}

	public Collection<Identifier> getDangerousFunctionCalls() {
		return dangerousFunctionCall.values();
	}

	@SuppressWarnings("restriction")
	protected void checkFunction(FunctionInvocation node) {
		if (node.getFunctionName().getName().getType() == 33) {
			Identifier funcName = (Identifier) node.getFunctionName().getName();
			if (dangerousFunctions.contains(funcName.getName())) {
				dangerousFunctionCall.put(Integer.valueOf(funcName.getStart()),
						funcName);

			}
		} else {

		}
	}

	public void enableFunctionCheck() {
		checkFunction = true;
		functionDeclaration = new HashMap<Integer, FunctionDeclaration>();
	}

	@Override
	public boolean visit(FunctionDeclaration node) {
		if (checkFunction && node.getParent() != null
				&& node.getParent().getType() != 42)
			functionDeclaration.put(Integer.valueOf(node.getStart()), node);
		return checkFunction;
	}

	public FunctionDeclaration getFunctionDeclaration(String name) {
		if (checkFunction) {
			for (Iterator<FunctionDeclaration> iterator = functionDeclaration
					.values().iterator(); iterator.hasNext();) {
				FunctionDeclaration currentNode = iterator.next();
				if (currentNode.getFunctionName().getName().equals(name))
					return currentNode;
			}

		}
		return null;
	}

	protected static HashSet<String> dangerousFunctions;
	private HashMap<Integer, Identifier> dangerousFunctionCall;
	private HashMap<Integer, FunctionDeclaration> functionDeclaration;
	private boolean checkFunction;

	static {
		dangerousFunctions = new HashSet<String>();
		dangerousFunctions.add("eval");
	}
*/
	@Override
	public boolean apply(ASTNode node) {
		return false;
	}
}
