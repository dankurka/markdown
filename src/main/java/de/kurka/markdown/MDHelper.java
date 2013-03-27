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

import java.io.File;

import de.kurka.markdown.fs.FileSystemTraverser;
import de.kurka.markdown.fs.MDParent;
import de.kurka.markdown.toc.TocCreator;

public class MDHelper {
  private String sourceDirectory;
  private String outputDirectory;

  private boolean created = false;

  public MDHelper setSourceDirectory(String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
    return this;
  }

  public MDHelper setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
    return this;
  }

  public MDHelper create() {
    // TODO some checking!
    created = true;
    return this;
  }

  public void translate() {

    if (!created) {
      throw new IllegalStateException();
    }

    FileSystemTraverser traverser = new FileSystemTraverser();
    MDParent mdRoot = traverser.traverse(new File(sourceDirectory));

    new MDTranslater(new TocCreator(), new MarkupWriter(new File(outputDirectory))).render(mdRoot);
  }

}
