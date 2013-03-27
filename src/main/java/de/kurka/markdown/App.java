package de.kurka.markdown;

/**
 * Hello world!
 * 
 */
public class App {

  public static void main(String[] args) {

    MDHelper helper = new MDHelper();
    helper.setOutputDirectory("/Users/kurt/Documents/workspace-mgwt/markdown/target/html/")
        .setSourceDirectory("/Users/kurt/Documents/workspace-mgwt/markdown/src/main/md/").create()
        .translate();

  }

}
