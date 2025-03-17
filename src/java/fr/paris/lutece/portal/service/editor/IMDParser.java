package fr.paris.lutece.portal.service.editor;

/**
 * Interface for Markdown to HTML parser.
 */
public interface IMDParser {
	/**
	 * The bean name for the Markdown parser.
	 */
	String BEAN_NAME = "MDParser";

	/**
	 * Converts Markdown content to HTML.
	 *
	 * @param content the Markdown content to be converted
	 * @return the converted HTML content
	 */
	public String getHtmlFromMD(String content);
}
