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
package fr.paris.lutece.util.password;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Utility class used to generate random passwords
 *
 */
public final class PasswordUtil
{
    private static final String PROPERTY_PASSWORD_SIZE = "randomPassword.size";
    private static final int CONSTANT_NUMBER_LETTERS = 26;
    private static final int CONSTANT_NUMBER_NUMBERS_BASE10 = 10;
    private static final int CONSTANT_ASCII_CODE_A_UPPERCASE = 65;
    private static final int CONSTANT_ASCII_CODE_A_LOWERCASE = 97;
    private static final int CONSTANT_ASCII_CODE_ZERO = 48;

    /** Private Constructor */
    private PasswordUtil(  )
    {
    }

    /**
     * Generate a new random password
     * @return the new password
     */
    public static String makePassword(  )
    {
        //		reinitialize password
        Random r = new Random(  );
        String strPassword = "";
        int nPasswordSize = AppPropertiesService.getPropertyInt( PROPERTY_PASSWORD_SIZE, 8 );

        ArrayList<Character> listCharacters = new ArrayList<Character>(  );

        //No of Big letters
        int nNumCapitalLetters = r.nextInt( nPasswordSize - 2 ) + 1; //choose a number beetwen 1 and CONSTANT_PASSWORD_SIZE -1

        //No of small
        int nNumSmallLetters = r.nextInt( nPasswordSize - 1 - nNumCapitalLetters ) + 1; //choose a number beetwen 1 and CONSTANT_PASSWORD_SIZE - a1

        //no on nos
        int nNumNumbers = nPasswordSize - nNumCapitalLetters - nNumSmallLetters; //choose a number to complete list of CONSTANT_PASSWORD_SIZE characters

        for ( int j = 0; j < nNumCapitalLetters; j++ )
        {
            char c1 = (char) ( r.nextInt( CONSTANT_NUMBER_LETTERS ) + CONSTANT_ASCII_CODE_A_UPPERCASE );
            listCharacters.add( new Character( c1 ) );
        }

        for ( int j = 0; j < nNumSmallLetters; j++ )
        {
            char c1 = (char) ( r.nextInt( CONSTANT_NUMBER_LETTERS ) + CONSTANT_ASCII_CODE_A_LOWERCASE );
            listCharacters.add( new Character( c1 ) );
        }

        for ( int j = 0; j < nNumNumbers; j++ )
        {
            char c1 = (char) ( r.nextInt( CONSTANT_NUMBER_NUMBERS_BASE10 - 1 ) + CONSTANT_ASCII_CODE_ZERO );
            listCharacters.add( new Character( c1 ) );
        }

        Collections.shuffle( listCharacters );

        for ( Character myChar : listCharacters )
        {
            strPassword += myChar;
        }

        return strPassword;
    }
}
