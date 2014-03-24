/*******************************************************************************
 * Copyright (c) 2009, 2010 Sven Kiera
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package edu.uncc.aside.phpcodeannotate.search;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.search.SearchMatch;
import org.eclipse.dltk.core.search.SearchRequestor;

public class PHPMethodSearchRequestor extends SearchRequestor {
	private ArrayList<SearchMatch> searchMatches;

	public void beginReporting() {
		searchMatches = new ArrayList<SearchMatch>();
		super.beginReporting();
	}

	public void acceptSearchMatch(SearchMatch match) throws CoreException {
		//this method could be overrided.
		/*e.g. 
		 * if (currentFile != match.getResource())
			{
				currentFile = (IFile)match.getResource();
				collector.acceptFile(currentFile);
			}
			collector.acceptPatternMatch(new SearchMatchAccess(match));*/
		if (match.isExact()) {
			searchMatches.add(match);
		}
	}

	public SearchMatch[] getMatches() {
		return searchMatches.toArray(new SearchMatch[0]);
	}
}
