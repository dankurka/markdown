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
package de.kurka.markdown;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;

import de.kurka.markdown.fs.MDNode;
import de.kurka.markdown.fs.MDParent;

public class MarkupWriter {

  private final File rootFile;

  public MarkupWriter(File rootFile) {
    this.rootFile = rootFile;
  }

  public void writeHTML(MDNode node, String html) {

    if (node instanceof MDParent) {
     throw new IllegalArgumentException();
    } 
    
    Stack<MDParent> stack = new Stack<MDParent>();
    
    MDParent tmp = node.getParent();
    stack.add(tmp);
    

    while (tmp.getParent() != null) {
      tmp = tmp.getParent();
      stack.add(tmp);
    }
    
    //get rootnode from stack
    stack.pop();

    File currentDir = rootFile;
    ensureDirectory(currentDir);
    while(!stack.isEmpty()){
      MDParent pop = stack.pop();
      currentDir = new File(currentDir, pop.getName());
      ensureDirectory(currentDir);
    }

    String fileName =
        node.getName().substring(0, node.getName().length() - ".md".length()) + ".html";
    File fileToWrite = new File(currentDir, fileName);

    FileOutputStream fileOutputStream;
    try {
      fileOutputStream = new FileOutputStream(fileToWrite);
      IOUtils.write(html, fileOutputStream);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }



  }

  private void ensureDirectory(File dir) {
    if (!dir.exists()) {
      boolean created = dir.mkdir();
      if (!created) {
        // TODO
        System.out.println("DOH!!!!s");
      }
    }

  }

}
