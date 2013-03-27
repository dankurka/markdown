/*
 * Copyright 2013 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.kurka.markdown.toc;

import java.util.List;

import de.kurka.markdown.fs.MDNode;
import de.kurka.markdown.fs.MDParent;

public class TocCreator {

  public String createTocForNode(MDParent root, MDNode node) {

    StringBuffer buffer = new StringBuffer();

    recurse(root, buffer, node);

    return buffer.toString();
  }

  private void recurse(MDNode node, StringBuffer buffer, MDNode tocNode){
    

    if(node instanceof MDParent){
      MDParent mdParent = (MDParent) node;
      buffer.append("<ul>");

      if (node.getDepth() != 0) {
        buffer.append("<li>");
        buffer.append(node.getName());
        buffer.append("<ul>");

      }

      List<MDNode> children = mdParent.getChildren();
      for (MDNode child : children) {
        recurse(child, buffer, tocNode);
      }
      
      if (node.getDepth() != 0) {

        buffer.append("</li>");
        buffer.append("</ul>");

      }

      buffer.append("</ul>");

    }else{



      StringBuffer url = new StringBuffer();
      if (tocNode.getDepth() > 0) {
        for (int i = 1; i < tocNode.getDepth(); i++) {
          url.append("../");
        }
      }

      url.append(node.getRelativePath());

      buffer.append("<li>");
      //TODO escape HTML
      buffer.append("<a href='" + url.toString() + "'>" + node.getName()
          + "</a>");
      buffer.append("</li>");
    }
    
  }


}
