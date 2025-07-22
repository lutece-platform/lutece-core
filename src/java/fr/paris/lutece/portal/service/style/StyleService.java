package fr.paris.lutece.portal.service.style;

import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import fr.paris.lutece.portal.business.style.IModeRepository;
import fr.paris.lutece.portal.business.style.IPageTemplateRepository;
import fr.paris.lutece.portal.business.style.IStyleRepository;
import fr.paris.lutece.portal.business.style.Mode;
import fr.paris.lutece.portal.business.style.PageTemplate;
import fr.paris.lutece.portal.business.style.Style;
import fr.paris.lutece.portal.business.stylesheet.IStyleSheetRepository;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.util.ReferenceList;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StyleService implements IStyleService
{

    @Inject
    private IStyleRepository _styleRepository;
    @Inject
    private IStyleSheetRepository _styleSheetRepository;
    @Inject
    private IPageTemplateRepository _pageTemplateRepository;
    @Inject
    private IModeRepository _modeRepository;

    @Override
    public Optional<Style> findStyleById( int nId )
    {
        return _styleRepository.load( nId );
    }

    @Override
    public Collection<Style> findStyles( )
    {
        return _styleRepository.findAll( );
    }

    @Override
    public Optional<StyleSheet> findStyleSheetById( int nId )
    {
        return _styleSheetRepository.load( nId );
    }

    @Override
    public Collection<StyleSheet> findStyleSheetsByMode( int nModeId )
    {
        return _styleSheetRepository.findByMode( nModeId );
    }

    @Override
    public Collection<StyleSheet> findStyleSheetsByStyle( int nStyleId )
    {
        return _styleRepository.findStyleSheetsByStyle( nStyleId );
    }

    @Override
    public Collection<PageTemplate> findPageTemplates( )
    {
        return _pageTemplateRepository.findAll( );
    }

    @Override
    public Optional<Mode> findModeById( int nId )
    {
        return Optional.ofNullable( _modeRepository.load( nId ) );
    }

    @Override
    public ReferenceList findModesToReferenceList( )
    {
        return _modeRepository.findAllToReferenceList( );
    }

    @Override
    public Properties findModeXlsOutputProperties( int nModeId )
    {
        return _modeRepository.findOuputXslProperties( nModeId );
    }

}
