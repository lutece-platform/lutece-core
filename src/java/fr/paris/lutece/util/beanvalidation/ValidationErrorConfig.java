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
package fr.paris.lutece.util.beanvalidation;


/**
 * Interface for ValidationError Configs
 */
public interface ValidationErrorConfig
{
    /**
     * Returns the list of constraint descriptor attribute that will appear
     * in the value#1 of the message
     * @return A list of attributes separated with spaces or commas
     */
    String getValue1Attributes(  );

    /**
     * Returns the list of constraint descriptor attribute that will appear
     * in the value#2 of the message
     * @return A list of attributes separated with spaces or commas
     */
    String getValue2Attributes(  );

    /**
     * The array of all variables prefix
     * @return The array
     */
    String[] getVariablesPrefix(  );

    /**
     * The prefix of fields name in the resource file (ex : myplugin.model.entity.product.field." )
     * @return the _strFieldKeysPrefix
     */
    String getFieldKeysPrefix(  );

    /**
     * Returns the begin wrapper string for fieldname rendering (ex : "'" or "&lt;strong"&gt;" )
     * @return The begin string
     */
    String getFieldWrapperBegin(  );

    /**
     * Returns the end wrapper string for fieldname rendering (ex : "'" or "&lt;/strong"&gt;" )
     * @return The end string
     */
    String getFieldWrapperEnd(  );
}
