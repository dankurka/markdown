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
package de.kurka.markdown.fs;

import java.io.File;

public class FileSystemTraverser {

  public MDParent traverse(File file) {

    return traverse(null, file, 0, "");

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
        mdParent =
            new MDParent(parent, file.getName(), file.getAbsolutePath(), depth, path
                + file.getName() + "/");
        parent.addChild(mdParent);

      }

      File[] listFiles = file.listFiles();
      for (File newFile : listFiles) {
        traverse(mdParent, newFile, depth + 1, mdParent.getRelativePath());
      }

      return mdParent;

    } else if (file.isFile()) {
      // TODO more infos
      MDNode mdNode =
          new MDNode(parent, file.getName(), file.getAbsolutePath(), depth, path + file.getName());
      parent.addChild(mdNode);

    } else {
      System.out.println("how did we get here?");
    }

    return null;

  }

  private boolean ignoreFile(File file) {
    // ignore all files that do not end with .md
    return !file.isDirectory() && !file.getName().endsWith(".md");
  }

}
