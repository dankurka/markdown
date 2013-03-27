package de.kurka.markdown;

import org.pegdown.PegDownProcessor;
import org.pegdown.ast.ExpLinkNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.RootNode;

import java.util.List;

/**
 * Hello world!
 * 
 */
public class App {
  public static void main(String[] args) {
    String markdownToHtml =
        new PegDownProcessor().markdownToHtml("asdf [link](target)".toCharArray());
    System.out.println(markdownToHtml);

    RootNode rootNode = new PegDownProcessor().parseMarkdown("asdf [link](target)".toCharArray());

    List<Node> children = rootNode.getChildren();

    recurse(children);
  }

  private static void recurse(List<Node> children) {
    for (Node node : children) {
      // System.out.println(node.toString());

      if (node instanceof ExpLinkNode) {
        ExpLinkNode expLinkNode = (ExpLinkNode) node;
        System.out.println("url: " + expLinkNode.url);

      }
      recurse(node.getChildren());

    }
    
  }
}
