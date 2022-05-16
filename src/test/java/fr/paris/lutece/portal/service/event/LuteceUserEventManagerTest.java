/**
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.portal.service.event;

import java.util.function.Consumer;

import fr.paris.lutece.portal.business.event.LuteceUserEvent;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.security.MokeLuteceAuthentication;
import fr.paris.lutece.portal.service.security.MokeLuteceUser;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * DatastoreService Test
 */
public class LuteceUserEventManagerTest extends LuteceTestCase
{
	private static final String USER_NAME = "user-test";
	private static final String KEY1 = "key_LuteceUserEventManagerTest";
    private static final String VALUE_DEFAULT = "default_value";
    private static final String NEW_VALUE = "new_value";

    public void test( )
    {
    	DatastoreService.setDataValue( KEY1, VALUE_DEFAULT );
  
    	MokeLuteceUser user = new MokeLuteceUser( USER_NAME, new MokeLuteceAuthentication( ) );

    	// listener consumer function 
    	Consumer<LuteceUserEvent> myfuncion = (LuteceUserEvent event) -> {
    		DatastoreService.setDataValue( KEY1, NEW_VALUE );
    	};

    	// register listener
    	LuteceUserEventManager.getInstance( ).register( "mylistener" , myfuncion );

    	// noptify an event
    	LuteceUserEventManager.getInstance( ).notifyListeners( new LuteceUserEvent( user, LuteceUserEvent.EventType.LOGIN_SUCCESSFUL ) );

    	// check if the datastore value has been changed
    	String strValue = DatastoreService.getDataValue( KEY1, VALUE_DEFAULT );
    	assertEquals( strValue, NEW_VALUE );

    	DatastoreService.removeData( KEY1 );
    }
}
