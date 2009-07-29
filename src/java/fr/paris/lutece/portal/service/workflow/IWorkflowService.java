/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
package fr.paris.lutece.portal.service.workflow;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.workflow.Action;
import fr.paris.lutece.portal.business.workflow.State;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * WorkflowService
 */
public interface IWorkflowService
{
    /**
     * returns a list of actions possible for a given document based on the status
     * of the document in the workflow and the user role
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param user the adminUser
     * @param nIdWorkflow the workflow id
     * @return a list of Action
     */
    Collection<Action> getActions( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user );

    /**
     * returns the state of a  given document
     * of the document in the workflow and the user role
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param user the adminUser
     * @param nIdWorkflow the workflow id
     * @return the state of a given document
     */
    State getState( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user );

    /**
     * return true if a form is associate to the action
     *
     * @param nIdAction the action id
     * @param locale the loacle
     * @return true if a form is associate to the action
     */
    boolean isDisplayTasksForm( int nIdAction, Locale locale );

    /**
     * Proceed action given in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param request the request
     * @param nIdAction the action id
     * @param locale locale
     *
     */
    void doProcessAction( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request,
        Locale locale );

    /**
     * returns the  actions history performed on a resource
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param request the request
     * @param nIdWorkflow the workflow id
     * @param locale the locale
     * @return the history of actions performed on a resource
     */
    String getDisplayDocumentHistory( int nIdResource, String strResourceType, int nIdWorkflow,
        HttpServletRequest request, Locale locale );

    /**
     * returns a xml wich contains the  actions history performed on a resource
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param request the request
     * @param nIdWorkflow the workflow id
     * @param locale the locale
     * @return a xml wich contains  the history of actions performed on a resource
     */
    String getDocumentHistoryXml( int nIdResource, String strResourceType, int nIdWorkflow, HttpServletRequest request,
        Locale locale );

    /**
     * Perform the information on the various tasks associated with the given action specified in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     * @param request the request
     * @param nIdAction the action id
     * @param locale the locale
     * @return null if there is no error in the task form
     *                    else return the error message url
    
     */
    String doSaveTasksForm( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request,
        Locale locale );

    /**
     * Remove in all workflows the resource specified in parameter
     * @param nIdResource the resource id
     * @param strResourceType the resource type
     */
    void doRemoveWorkFlowResource( int nIdResource, String strResourceType );

    /**
     * returns the tasks form
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param request the request
     * @param nIdAction the action id
     * @param locale the locale
     * @return the tasks form associated to the action
     *
     */
    String getDisplayTasksForm( int nIdResource, String strResourceType, int nIdAction, HttpServletRequest request,
        Locale locale );

    /**
     * Check that a given user is allowed to view a resource depending the state of the resource
     * @param nIdResource the document id
     * @param strResourceType the document type
     * @param  user the AdminUser
     * @param nIdWorkflow the workflow id
     * @return a list of Action
     */
    boolean isAuthorized( int nIdResource, String strResourceType, int nIdWorkflow, AdminUser user );

    /**
     * return a referencelist wich contains a list enabled workflow
     * @param user the AdminUser
     * @param locale the locale
     * @return a referencelist wich contains a list enabled workflow
     */
    ReferenceList getWorkflowsEnabled( AdminUser user, Locale locale );

    /**
     * returns all state of a  given workflow
     * @param user the adminUser
     * @param nIdWorkflow the workflow id
     * @return the state of a given document
     */
    Collection<State> getAllStateByWorkflow( int nIdWorkflow, AdminUser user );

    /**
     * return a list wich contains idRessource for a given state
     * @param nIdState the id State
     * @param strResourceType the resource type
     * @param user the AdminUser
     * @return a list wich contains idRessource
     */
    Collection<Integer> getListIdRessourceByState( int nIdState, String strResourceType, AdminUser user );
}
