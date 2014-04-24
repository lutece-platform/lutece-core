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
package fr.paris.lutece.portal.business.right;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.List;


public class FeatureGroupTest extends LuteceTestCase
{
    private static final String ID = "JUNIT_FG";
    private final static String LABELKEY1 = "LabelKey 1";
    private final static String LABELKEY2 = "LabelKey 2";
    private final static String DESCRIPTIONKEY1 = "DescriptionKey 1";
    private final static String DESCRIPTIONKEY2 = "DescriptionKey 2";
    private final static int ORDER1 = 1;
    private final static int ORDER2 = 2;

    public void testBusinessFeatureGroup(  )
    {
        // Initialize an object
        FeatureGroup featureGroup = new FeatureGroup(  );
        featureGroup.setId( ID );
        featureGroup.setLabelKey( LABELKEY1 );
        featureGroup.setDescriptionKey( DESCRIPTIONKEY1 );
        featureGroup.setOrder( ORDER1 );

        // Create test
        FeatureGroupHome.create( featureGroup );

        FeatureGroup featureGroupStored = FeatureGroupHome.findByPrimaryKey( featureGroup.getId(  ) );
        assertEquals( featureGroupStored.getLabelKey(  ), featureGroup.getLabelKey(  ) );
        assertEquals( featureGroupStored.getDescriptionKey(  ), featureGroup.getDescriptionKey(  ) );
        assertEquals( featureGroupStored.getOrder(  ), featureGroup.getOrder(  ) );

        // Update test
        featureGroup.setLabelKey( LABELKEY2 );
        featureGroup.setDescriptionKey( DESCRIPTIONKEY2 );
        featureGroup.setOrder( ORDER2 );
        FeatureGroupHome.update( featureGroup );
        featureGroupStored = FeatureGroupHome.findByPrimaryKey( featureGroup.getId(  ) );
        assertEquals( featureGroupStored.getLabelKey(  ), featureGroup.getLabelKey(  ) );
        assertEquals( featureGroupStored.getDescriptionKey(  ), featureGroup.getDescriptionKey(  ) );
        assertEquals( featureGroupStored.getOrder(  ), featureGroup.getOrder(  ) );

        // List test
        List listGroups = FeatureGroupHome.getFeatureGroupsList(  );
        assertTrue( listGroups.size(  ) > 0 );

        assertTrue( FeatureGroupHome.getFeatureGroupsCount(  ) > 0 );

        // Delete test
        FeatureGroupHome.remove( featureGroup.getId(  ) );
        featureGroupStored = FeatureGroupHome.findByPrimaryKey( featureGroup.getId(  ) );
        assertNull( featureGroupStored );
    }
}
