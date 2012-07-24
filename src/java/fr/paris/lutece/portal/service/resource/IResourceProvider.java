/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.portal.service.resource;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * IResourceProvider
 *
 */
public interface IResourceProvider
{
	/**
	 * Checks if is invoked.
	 *
	 * @param strResourceType the str resource type
	 * @return true, if is invoked
	 */
	boolean isInvoked( String strResourceType );

    /**
     * Add datas to the create document model use in the template.
     *
     * @param strResourceType the str resource type
     * @param model the map use in the template
     * @param mapParameters the map parameters
     */
    void getCreateResourceModelAddOn( String strResourceType, Map<String, Object> model, Map<String, String[]> mapParameters );

    /**
     * Perform actions associated to the document creation.
     *
     * @param strResourceType the str resource type
     * @param request The HTTP request
     * @param mapParameters the map parameters
     */
    void doCreateResourceAddOn( String strResourceType, HttpServletRequest request, Map<String, String[]> mapParameters );

    /**
     * Add datas to the modify document model use in the template.
     *
     * @param strResourceType the str resource type
     * @param model the map use in the template
     * @param mapParameters the map parameters
     */
    void getModifyResourceModelAddOn( String strResourceType, Map<String, Object> model, Map<String, String[]> mapParameters );

    /**
     * Perform actions associated to the document modification.
     *
     * @param strResourceType the str resource type
     * @param request The HTTP request
     * @param mapParameters the map parameters
     */
    void doModifyResourceAddOn( String strResourceType, HttpServletRequest request, Map<String, String[]> mapParameters );

    /**
     * Perform actions associated to the document deletion.
     *
     * @param strResourceType the str resource type
     * @param request The HTTP request
     * @param mapParameters the map parameters
     */
    void doDeleteResourceAddOn( String strResourceType, HttpServletRequest request, Map<String, String[]> mapParameters );

    /**
     * Perform actions associated to the document download.
     *
     * @param strResourceType the str resource type
     * @param request The HTTP request
     * @param mapParameters the map parameters
     */
    void doDownloadResourceAddOn( String strResourceType, HttpServletRequest request, Map<String, String[]> mapParameters );

    /**
     * Do process.
     *
     * @param strResourceType the str resource type
     * @param request the request
     * @param mapParameters the map parameters
     * @param nMode the mode
     */
    void doProcess( String strResourceType, HttpServletRequest request, Map<String, String[]> mapParameters, int nMode );
}
