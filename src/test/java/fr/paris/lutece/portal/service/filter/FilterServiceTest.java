/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.portal.service.filter;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.test.LuteceTestCase;
import java.util.List;
import static org.junit.Assert.*;

/**
 * FilterService Test
 */
public class FilterServiceTest extends LuteceTestCase
{

    /**
     * Test of getInstance method, of class FilterService.
     */
    public void testGetInstance()
    {
        System.out.println( "getInstance" );
        FilterService result = FilterService.getInstance();
        assertNotNull( result );
    }

    /**
     * Test of registerFilter method, of class FilterService.
     */
    public void testRegisterFilter()
    {
        System.out.println( "registerFilter" );
        FilterEntry entry = new FilterEntry();
        entry.setName( "filter" );
        entry.setFilterClass( "" );
        entry.setMapping( "/jsp/" );
        Plugin plugin = null;
        FilterService instance = FilterService.getInstance();
        instance.registerFilter( entry, plugin );
    }

    /**
     * Test of getFilters method, of class FilterService.
     */
    public void testGetFilters()
    {
        System.out.println( "getFilters" );
        FilterService instance = FilterService.getInstance();
        List<LuteceFilter> result = instance.getFilters();
        assertNotNull( result );
    }
}