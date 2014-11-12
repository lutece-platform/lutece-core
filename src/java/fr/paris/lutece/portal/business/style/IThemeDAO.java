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
package fr.paris.lutece.portal.business.style;

import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 * This class provides Data Access methods for Theme objects
 * @deprecated Use the plugin-theme instead
 */
public interface IThemeDAO
{
    /**
     * Insert a new record in the table.
     *
     * @param mode The mode object
     */
    void insert( Theme mode );

    /**
     * load the data of Level from the table
     *
     * @param strCodeTheme The indentifier of the object Theme
     * @return The Instance of the object Theme
     */
    Theme load( String strCodeTheme );

    /**
     * Delete a record from the table
     *
     * @param strCodeTheme The indentifier of the object Theme
     */
    void delete( String strCodeTheme );

    /**
     * Update the record in the table
     *
     * @param theme The instance of the Theme to update
     */
    void store( Theme theme );

    /**
     * Returns a list of all the themes
     *
     * @return A collection of themes objects
     */
    Collection<Theme> selectThemesList(  );

    /**
     * Returns the list of the themes in form of a reference list
     *
     * @return the themes list in form of a ReferenceList object
     */
    ReferenceList getThemesList(  );

    /**
     * Sets the global theme
     * @param strGlobalTheme The Global Theme
     */
    void setGlobalTheme( String strGlobalTheme );

    /**
     * Returns the global theme
     * @return The Global Theme
     */
    String getGlobalTheme(  );
}
