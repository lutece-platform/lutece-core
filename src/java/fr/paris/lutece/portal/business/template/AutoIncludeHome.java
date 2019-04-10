/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
 package fr.paris.lutece.portal.business.template;

import fr.paris.lutece.portal.service.template.FreeMarkerTemplateService;
import fr.paris.lutece.portal.web.admin.AdminMenuJspBean;
import java.util.ArrayList;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for AutoInclude objects
 */
public final class AutoIncludeHome
{
    /**
     * Private constructor - this class need not be instantiated
     */
    private AutoIncludeHome(  )
    {
    }

    /**
     * Create an instance of the autoInclude class
     * @param autoInclude The instance of the AutoInclude which contains the informations to store
     * @return The  instance of autoInclude which has been created with its primary key.
     */
    public static AutoInclude create( AutoInclude autoInclude )
    {
        FreeMarkerTemplateService.getInstance().addAutoInclude( autoInclude.getFilePath() );
        AdminMenuJspBean.resetAdminStylesheets();
        
        return autoInclude;
    }

    /**
     * Remove the autoInclude whose identifier is specified in parameter
     * @param strFilePath The autoInclude path
     */
    public static void remove( String strFilePath )
    {
        FreeMarkerTemplateService.getInstance().removeAutoInclude( strFilePath );
        AdminMenuJspBean.resetAdminStylesheets();
    }

    /**
     * Load the data of all the autoInclude objects and returns them as a list
     * @return the list which contains the data of all the autoInclude objects
     */
    public static List<AutoInclude> getAutoIncludesList( )
    {
        List<AutoInclude> list = new ArrayList<>();
        for( String strAutoIncludePath : FreeMarkerTemplateService.getInstance().getAutoIncludes() )
        {
            AutoInclude include = new AutoInclude( strAutoIncludePath );
            list.add( include );
                    
        }
        return list;
    }
    
}

