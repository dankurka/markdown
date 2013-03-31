package com.google.gwt.site.markdown;

/**
 * Hello world!
 * 
 */
public class MarkDown {

	public static void main(String[] args) throws MDHelperException, TranslaterException {

		if (args.length != 3) {
			System.out.println("Usage MarkDown <sourceDir> <outputDir> <templateFile>");
			throw new IllegalArgumentException("Usage MarkDown <sourceDir> <outputDir> <templateFile>");
		}

		String sourceDir = args[0];
		System.out.println(sourceDir);

		String outputDir = args[1];
		System.out.println(outputDir);

		String templateFile = args[2];
		System.out.println(templateFile);

		MDHelper helper = new MDHelper();
		try {
			helper.setOutputDirectory(outputDir).setSourceDirectory(sourceDir).setTemplateFile(templateFile).create().translate();
		} catch (MDHelperException e) {
			e.printStackTrace();
			throw e;
		} catch (TranslaterException e) {
			e.printStackTrace();
			throw e;
		}

	}

}
