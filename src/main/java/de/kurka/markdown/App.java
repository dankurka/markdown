package de.kurka.markdown;

import org.pegdown.ast.ExpLinkNode;
import org.pegdown.ast.Node;

import java.io.File;
import java.util.List;

import de.kurka.markdown.fs.FileSystemTraverser;
import de.kurka.markdown.fs.MDNode;
import de.kurka.markdown.fs.MDParent;
import de.kurka.markdown.toc.TocCreator;

/**
 * Hello world!
 * 
 */
public class App {



  public static void main(String[] args) {

    FileSystemTraverser traverser = new FileSystemTraverser();
    MDParent mdRoot =
        traverser.traverse(new File(
"/Users/kurt/Documents/workspace-mgwt/markdown/src/main/md/"));

    display("", mdRoot);

    new MDTranslater(new TocCreator(), new MarkupWriter(new File(
        "/Users/kurt/Documents/workspace-mgwt/markdown/target/html/"))).render(mdRoot);


  }



  private static void display(String path, MDNode node) {

    if (node == null) {
      return;
    }

    System.out.println(path + node.getName());

    if (node instanceof MDParent) {
      MDParent mdParent = (MDParent) node;
      String name = mdParent.getName();

      String newPath = path + name + "/";

      List<MDNode> children = mdParent.getChildren();
      for (MDNode mdNode : children) {
        display(newPath, mdNode);
      }

    }

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
