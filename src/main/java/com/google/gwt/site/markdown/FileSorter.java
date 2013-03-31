package com.google.gwt.site.markdown;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.gwt.site.markdown.fs.FileSystemTraverser;
import com.google.gwt.site.markdown.fs.MDNode;
import com.google.gwt.site.markdown.fs.MDParent;

public class FileSorter {

	public void sort(MDParent node) throws SortingException {
		if (node.isNeedsSorting()) {
			validateOrderStructure(node);
			sortChildren(node);
		}

		List<MDNode> children = node.getChildren();
		for (MDNode child : children) {
			if (child.isFolder()) {
				sort(child.asFolder());
			}
		}
	}

	private void validateOrderStructure(MDParent node) throws SortingException {
		Set<String> sortingStructureSet = new HashSet<String>(node.getSortingStructure());
		Set<String> nodeSet = createSetFromNodeList(node.getChildren());

		Set<String> diffNodeSet = new HashSet<String>(nodeSet);
		Set<String> diffStructureSet = new HashSet<String>(sortingStructureSet);

		diffNodeSet.removeAll(sortingStructureSet);
		if (diffNodeSet.size() > 0) {
			throw new SortingException("elements in " + FileSystemTraverser.ORDER_XML + " missing", diffNodeSet);
		}

		diffStructureSet.removeAll(nodeSet);
		if (diffStructureSet.size() > 0) {
			throw new SortingException("too many elements in " + FileSystemTraverser.ORDER_XML, diffStructureSet);
		}
	}

	// TODO: clean
	private void sortChildren(MDParent node) {
		List<String> sortingStructureNames = node.getSortingStructure();
		List<MDNode> children = node.getChildren();

		List<MDNode> sortedChildren = new LinkedList<MDNode>();
		for (String nodeName : sortingStructureNames) {
			boolean isFolder = nodeName.endsWith("/");
			if (isFolder) {
				nodeName = nodeName.substring(0, nodeName.length() - "/".length());
			}
			for (MDNode child : children) {
				String childName = child.getName();
				if (childName.endsWith(".md")) {
					childName = childName.substring(0, childName.length() - ".md".length());
				}

				if (nodeName.equals(childName) && isFolder == child.isFolder()) {
					sortedChildren.add(child);
					break;
				}
			}
		}
		node.setChildren(sortedChildren);
	}

	private Set<String> createSetFromNodeList(List<MDNode> children) {
		HashSet<String> nodeSet = new HashSet<String>();

		for (MDNode child : children) {
			if (child.isFolder()) {
				nodeSet.add(child.getName() + "/");
				continue;
			}
			if (child.getName().endsWith(".md")) {
				nodeSet.add(child.getName().substring(0, child.getName().length() - ".md".length()));
				continue;
			}
		}
		return nodeSet;
	}
}
