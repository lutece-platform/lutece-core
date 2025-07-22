package fr.paris.lutece.portal.service.style;

import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import fr.paris.lutece.portal.business.style.Mode;
import fr.paris.lutece.portal.business.style.PageTemplate;
import fr.paris.lutece.portal.business.style.Style;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.util.ReferenceList;

public interface IStyleService
{

    Optional<Style> findStyleById( int nId );

    Collection<Style> findStyles( );

    Optional<StyleSheet> findStyleSheetById( int nId );

    Collection<StyleSheet> findStyleSheetsByMode( int nModeId );

    Collection<StyleSheet> findStyleSheetsByStyle( int nStyleId );

    Collection<PageTemplate> findPageTemplates( );

    Optional<Mode> findModeById( int nId );

    ReferenceList findModesToReferenceList( );

    Properties findModeXlsOutputProperties( int nModeId );
}
