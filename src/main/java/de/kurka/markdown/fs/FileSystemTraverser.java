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
package de.kurka.markdown.fs;

import java.io.File;

public class FileSystemTraverser {

  public MDRoot traverse(File file) {
    MDRoot mdRoot = new MDRoot();
    traverse(mdRoot, file);
    return mdRoot;
  }

  private void traverse(MDParent parent, File file) {
    if (file.isDirectory()) {
      MDParent mdParent = new MDParent(file.getName());
      parent.addChild(mdParent);
      // TODO filter!
      String[] files = file.list();
      for (String newFile : files) {
        traverse(mdParent, new File(newFile));
      }
    } else if (file.isFile()) {
      // TODO more infos
      MDNode mdNode = new MDNode(file.getName());
      parent.addChild(mdNode);
    } else {
      System.out.println("how did we get here?");
    }
  }

}
