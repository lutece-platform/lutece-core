/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;


/**
 * This Class is the main Test Suite class to launch all the unit tests of Lutece
 */
public final class AllTests
{
    private static Logger _logger = Logger.getLogger( AllTests.class );

    /**
     * Constructor
     *
     */
    private AllTests(  )
    {
    }

    /**
     * A set of tests
     * @return Test the tests
     */
    public static Test suite(  )
    {
        _logger.info( "UNIT TESTS FOR LUTECE" );
        _logger.info( "=====================" );
        _logger.info( "Please do not forget to customize the property files in /lutece/WEB-INF/conf" );

        TestSuite suite = new TestSuite( "Lutece JUnit tests" );

        //$JUnit-BEGIN$
        suite.addTest( fr.paris.lutece.util.AllTests.suite(  ) );
        suite.addTest( fr.paris.lutece.portal.AllTests.suite(  ) );

        //suite.addTest( fr.paris.lutece.plugins.AllTests.suite(  ) );

        //$JUnit-END$
        return suite;
    }
}
