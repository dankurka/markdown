package de.kurka.markdown;

/**
 * Hello world!
 * 
 */
public class App {

  public static void main(String[] args) {

    MDHelper helper = new MDHelper();
    try {
      helper.setOutputDirectory("/Users/kurt/Documents/workspace-mgwt/markdown/target/html/")
          .setSourceDirectory("/Users/kurt/Documents/workspace-mgwt/markdown/src/main/md/")
          .setTemplateFile(
              "/Users/kurt/Documents/workspace-mgwt/markdown/src/main/resources/main.tpl")
          .create().translate();
    } catch (MDHelperException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TranslaterException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
