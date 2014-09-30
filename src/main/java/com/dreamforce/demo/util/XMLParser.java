package com.dreamforce.demo.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		parseXMLResponse("id");
		System.out.println("done");
	}

	/**
	 * Fetching the node value only from the top of the node. Does not support nested node query yet
	 * @param nodeToSearch
	 * @return
	 */
	public static String parseXMLResponse(String nodeToSearch) {
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = (Document) builder.build(new File("./template/CreateJobResponse.xml"));
			Element rootNode = document.getRootElement();
			List<Element> list = rootNode.getChildren();
			for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
				System.out.println(node.getName());
				if (node.getName().equals(nodeToSearch)) {
					System.out.println("Node: " + nodeToSearch + " value: " + node.getText());
					return node.getChildText(nodeToSearch);
				}
			}
		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return null;
	}
}
