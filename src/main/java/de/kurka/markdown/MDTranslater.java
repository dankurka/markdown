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
package de.kurka.markdown;

import org.apache.commons.io.IOUtils;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import de.kurka.markdown.fs.MDNode;
import de.kurka.markdown.fs.MDParent;
import de.kurka.markdown.toc.TocCreator;

public class MDTranslater {

  private PegDownProcessor pegDownProcessor = new PegDownProcessor();

  private final TocCreator tocCreator;

  private final MarkupWriter writer;

  private String template;

  private MDParent root;

  public MDTranslater(TocCreator tocCreator, MarkupWriter writer) {
    this.tocCreator = tocCreator;
    this.writer = writer;

  }

  public void render(MDParent root) {
    this.root = root;
    template = readTemplate();

    renderTree(root);

  }

  private String readTemplate() {
    // TODO
    return "<html><head></head><body> <div class='toc'> $toc </div><div class='content'> $content </div> </body> </html>";
  }

  private void renderTree(MDNode node) {

    if (node instanceof MDParent) {
      // TODO toc!
      MDParent mdParent = (MDParent) node;

      List<MDNode> children = mdParent.getChildren();
      for (MDNode mdNode : children) {
        renderTree(mdNode);
      }

    } else {

      try {

        String markDown = getNodeContent(node.getPath());

        // parse description for TOC

        String htmlMarkDown = pegDownProcessor.markdownToHtml(markDown);
        RootNode rootNode = pegDownProcessor.parseMarkdown(markDown.toCharArray());

        String toc = tocCreator.createTocForNode(root, node);

        String html = fillTemplate(htmlMarkDown, toc);

        writer.writeHTML(node, html);

      } catch (TranslaterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        // TODO
        throw new RuntimeException("TODO");
      } finally {

      }

    }

  }

  private String fillTemplate(String html, String toc) {
    return template.replace("$content", html).replace("$toc", toc);
  }

  private String getNodeContent(String path) throws TranslaterException {
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream(new File(path));

      String string = IOUtils.toString(fileInputStream, "UTF-8");
      return string;

    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      // TODO
      throw new TranslaterException();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      // TODO
      throw new TranslaterException();
    } finally {
      if (fileInputStream != null) {
        try {
          fileInputStream.close();
        } catch (IOException ignored) {

        }
      }
    }
  }
}
