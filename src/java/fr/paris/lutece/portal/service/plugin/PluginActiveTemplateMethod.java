package fr.paris.lutece.portal.service.plugin;

import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

import java.util.List;

public class PluginActiveTemplateMethod implements TemplateMethodModelEx
{
    @Override
    public Object exec( @SuppressWarnings( "rawtypes" ) List arguments ) throws TemplateModelException
    {
        int argsSize = arguments.size( );

        if ( argsSize != 1 )
        {
            throw new TemplateModelException( "Must be called with exactly one argument (the plugin name)" );
        }

        String pluginName = ( (TemplateScalarModel) arguments.get( 0 ) ).getAsString( );

        boolean isPluginActive = PluginService.isPluginEnable( pluginName );

        if ( isPluginActive )
        {
            return TemplateBooleanModel.TRUE;
        }
        else
        {
            return TemplateBooleanModel.FALSE;
        }
    }
}
