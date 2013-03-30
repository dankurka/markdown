package com.google.gwt.site.markdown;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.gwt.site.markdown.fs.MDNode;
import com.google.gwt.site.markdown.fs.MDParent;

public class FileSorter {

	public void sort(MDParent node) {
		if (node.isNeedsSorting()) {
			validateOrdering(node);
			sortList(node);
		}

		List<MDNode> children = node.getChildren();
		for (MDNode child : children) {
			if (child.isFolder()) {
				sort(child.asFolder());
			}
		}
	}

	private void validateOrdering(MDParent node) {
		Set<String> orderSet = new HashSet<String>(node.getSortingOrder());
		Set<String> nodeSet = createSetFromNodeList(node.getChildren());

		Set<String> diffNodeSet = new HashSet<String>(nodeSet);
		Set<String> diffOrderSet = new HashSet<String>(orderSet);

		diffNodeSet.removeAll(orderSet);
		diffOrderSet.removeAll(nodeSet);

		validateOrderEntries(diffNodeSet, diffOrderSet);

	}

	private void validateOrderEntries(Set<String> diffNodeSet, Set<String> diffOrderSet) {
		if (diffNodeSet.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (String string : diffNodeSet) {
				buffer.append(string + " ");
			}
			System.out.println("Warning: elements in order.xml missing " + buffer.toString());
		}

		if (diffOrderSet.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (String string : diffOrderSet) {
				buffer.append(string + " ");
			}
			System.out.println("Warning: Too much elements in order.xml " + buffer.toString());
		}
	}

	private Set<String> createSetFromNodeList(List<MDNode> children) {
		HashSet<String> nodeSet = new HashSet<String>();

		for (MDNode child : children) {
			if (child.getName().endsWith(".md") || child.isFolder()) {
				if (child.isFolder()) {
					nodeSet.add(child.getName() + "/");
				} else {
					nodeSet.add(child.getName().substring(0, child.getName().length() - 3));
				}
			}

		}
		return nodeSet;
	}

	private void sortList(MDParent node) {
		List<String> sortingOrder = node.getSortingOrder();
		List<MDNode> children = node.getChildren();

		List<MDNode> sortedChildren = new LinkedList<MDNode>();
		for (String nodeName : sortingOrder) {
			boolean folder = nodeName.endsWith("/");
			if (folder) {
				nodeName = nodeName.substring(0, nodeName.length() - 1);
			}
			for (MDNode child : children) {
				String childName = child.getName();
				if (childName.endsWith(".md")) {
					childName = childName.substring(0, childName.length() - ".md".length());
				}

				if (nodeName.equals(childName) && folder == child.isFolder()) {
					sortedChildren.add(child);
					break;
				}
			}
		}
		node.setChildren(sortedChildren);
	}

}
