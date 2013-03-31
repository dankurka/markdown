/*
 * Copyright 2013 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.site.markdown.fs;

import java.util.LinkedList;
import java.util.List;

public class MDParent extends MDNode {

	public MDParent(MDParent parent, String name, String path, int depth, String relativePath) {
		super(parent, name, path, depth, relativePath);
	}

	private List<MDNode> children = new LinkedList<MDNode>();
	private List<String> sortingStructure;

	@Override
	public String toString() {
		return "MDParent [getName()=" + getName() + ", getDepth()=" + getDepth() + "]";
	}

	public void setChildren(List<MDNode> children) {
		this.children = children;
	}

	public void addChild(MDNode node) {
		children.add(node);
	}

	public List<String> getSortingStructure() {
		return sortingStructure;
	}

	public List<MDNode> getChildren() {
		return children;
	}

	public void setSortingStructure(List<String> sortingOrder) {
		this.sortingStructure = sortingOrder;
	}

	public boolean isNeedsSorting() {
		return sortingStructure != null;
	}

	@Override
	public boolean isFolder() {
		return true;
	}
}
