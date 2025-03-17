package fr.paris.lutece.portal.service.editor;

import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * Service for converting Markdown content to HTML.
 */
public class Md2HtmlService {
    private static IMDParser _parser = (IMDParser) SpringContextService.getBean(IMDParser.BEAN_NAME);
    
    /**
     * Private constructor to prevent instantiation.
     */
    private Md2HtmlService() {}

    /**
     * Converts the given Markdown content to HTML.
     *
     * @param content the Markdown content to convert
     * @return the converted HTML content
     */
    public static String getContent(String content) {
        return _parser.getHtmlFromMD(content);
    }
}
