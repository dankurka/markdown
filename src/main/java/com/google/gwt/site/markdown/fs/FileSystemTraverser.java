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

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FileSystemTraverser {

	private static final String ORDER_XML = "order.xml";

	public MDParent traverse(File file) {
		MDParent root = traverse(null, file, 0, "");

		sort(root);
		return root;
	}

	private void sort(MDParent node) {
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

	private MDParent traverse(MDParent parent, File file, int depth, String path) {

		if (ignoreFile(file)) {
			return null;
		}

		if (file.isDirectory()) {
			MDParent mdParent;

			if (parent == null) {
				mdParent = new MDParent(null, "ROOT", null, depth, "");
			} else {
				mdParent = new MDParent(parent, file.getName(), file.getAbsolutePath(), depth, path + file.getName() + "/");
				parent.addChild(mdParent);

			}

			File[] listFiles = file.listFiles();
			for (File newFile : listFiles) {
				traverse(mdParent, newFile, depth + 1, mdParent.getRelativePath());
			}

			return mdParent;

		} else if (file.isFile()) {
			if (file.getName().equals(ORDER_XML)) {
				List<String> sortingOrder = parseSortingOrder(file);
				parent.setSortingOrder(sortingOrder);

			} else {
				MDNode mdNode = new MDNode(parent, file.getName(), file.getAbsolutePath(), depth, path + changeExtension(file.getName()));
				parent.addChild(mdNode);
			}

		} else {
			System.out.println("how did we get here?");
		}

		return null;

	}

	private List<String> parseSortingOrder(File file) {
		DocumentBuilder builder;
		List<String> list = new LinkedList<String>();
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document document = builder.parse(file);
			Element documentElement = document.getDocumentElement();

			System.out.println(documentElement.getTagName());

			NodeList childNodes = documentElement.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {

				Node node = childNodes.item(i);
				if (node.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}
				Element entryNode = (Element) node;

				if (entryNode.getChildNodes().getLength() != 1) {
					// TODO error
				}
				list.add(entryNode.getChildNodes().item(0).getNodeValue());
			}

		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	private String changeExtension(String fileName) {
		return fileName.substring(0, fileName.length() - ".md".length()) + ".html";
	}

	private boolean ignoreFile(File file) {
		// ignore all files that do not end with .md
		return !file.isDirectory() && !file.getName().endsWith(".md") && !file.getName().equals(ORDER_XML);
	}

}
