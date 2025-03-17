package fr.paris.lutece.portal.service.editor;

/**
 * Service pour gérer le contenu de texte enrichi.
 */
public class RichTextContentService {
	
	/**
	 * Récupère le contenu en fonction de son type (Markdown, BBCode ou texte brut).
	 *
	 * @param content Le contenu à traiter.
	 * @return Le contenu converti en HTML si nécessaire, ou le contenu original.
	 */
	public static String getContent(String content) {
		if ( content == null ) {
			return "";
		}
		if ( content.length( ) < 3 ) {
			return content;
		}

		if( content.startsWith( "MD:" ) ) {
			return getHtmlContentFromMD( content.substring( 3 ) );
		}
		else if ( content.startsWith( "BB:" ) ) {
			return getHtmlContentFromBB( content.substring( 3 ) );
		}
		else {
			return content;
		}
	}
	
	/**
	 * Convertit le contenu Markdown en HTML.
	 *
	 * @param content Le contenu Markdown à convertir.
	 * @return Le contenu converti en HTML.
	 */
	public static String getHtmlContentFromMD( String content ) {
		return Md2HtmlService.getContent( content );
	}

	/**
	 * Convertit le contenu BBCode en HTML.
	 *
	 * @param content Le contenu BBCode à convertir.
	 * @return Le contenu converti en HTML.
	 */
	public static String getHtmlContentFromBB( String content ) {
		return EditorBbcodeService.getInstance( ).parse( content );
	}
}
