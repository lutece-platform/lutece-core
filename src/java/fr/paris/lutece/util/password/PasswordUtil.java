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
package fr.paris.lutece.util.password;

import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.date.DateUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Utility class used to generate random passwords
 */
public final class PasswordUtil
{
    private static final String PROPERTY_PASSWORD_SIZE = "randomPassword.size";
    private static final int CONSTANT_NUMBER_LETTERS = 26;
    private static final int CONSTANT_NUMBER_NUMBERS_BASE10 = 10;
    private static final int CONSTANT_ASCII_CODE_A_UPPERCASE = 65;
    private static final int CONSTANT_ASCII_CODE_A_LOWERCASE = 97;
    private static final int CONSTANT_ASCII_CODE_ZERO = 48;
    private static final char[] CONSTANT_SPECIAL_CHARACTERS = 
        {
            '!', ',', ':', '?', '$', '-', '@', '}', '{', '(', ')', '*', '+', '=', '[', ']', '%', '.',
        };
    private static final String CONSTANT_PASSWORD_BEGIN_REGEX = "^";
    private static final String CONSTANT_PASSWORD_REGEX_NUM = "(?=.*[0-9])";
    private static final String CONSTANT_PASSWORD_REGEX_SPECIAL = "(?=.*[^a-zA-Z0-9])";
    private static final String CONSTANT_PASSWORD_REGEX_UPPER_LOWER = "(?=.*[a-z])(?=.*[A-Z])";
    private static final String CONSTANT_PASSWORD_END_REGEX = "(.*)$";
    private static final String PARAMETER_PASSWORD_MINIMUM_LENGTH = "password_minimum_length";

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
        // reinitialize password
        int nPasswordSize = AppPropertiesService.getPropertyInt( PROPERTY_PASSWORD_SIZE, 8 );
        int nMinPasswordSize = AdminUserService.getIntegerSecurityParameter( PARAMETER_PASSWORD_MINIMUM_LENGTH );

        if ( nMinPasswordSize > nPasswordSize )
        {
            nPasswordSize = nMinPasswordSize;
        }

        return makePassword( nPasswordSize, true, true, true );
    }

    /**
     * Generate a new random password
     * @param nPasswordSize the password size
     * @param bUpperAndLowerCase true if the password must contain upper and
     *            lower case
     * @param bNumbers if the password must contain numbers
     * @param bSpecialCaracters if the password must contain special characters
     *
     * @return the new password
     */
    public static String makePassword( int nPasswordSize, boolean bUpperAndLowerCase, boolean bNumbers,
        boolean bSpecialCaracters )
    {
        // reinitialize password
        Random r = new Random(  );

        ArrayList<Character> listCharacters = new ArrayList<Character>( nPasswordSize );

        // No of Big letters
        int nNumCapitalLetters = bUpperAndLowerCase ? ( r.nextInt( nPasswordSize - 3 ) + 1 ) : 0; // choose a number between 1 and CONSTANT_PASSWORD_SIZE -1

        // no on special characters
        int nNumSpecial = bSpecialCaracters ? ( r.nextInt( nPasswordSize - 2 - nNumCapitalLetters ) + 1 ) : 0; // choose a number beetwen 1 and CONSTANT_PASSWORD_SIZE - a1

        // no of nos
        int nNumNumbers = bNumbers ? ( r.nextInt( nPasswordSize - 1 - nNumCapitalLetters - nNumSpecial ) + 1 ) : 0; // choose a number to complete list of CONSTANT_PASSWORD_SIZE characters

        // no of small
        int nNumSmallLetters = nPasswordSize - nNumCapitalLetters - nNumSpecial - nNumNumbers; // choose a number to complete list of CONSTANT_PASSWORD_SIZE characters

        for ( int j = 0; j < nNumCapitalLetters; j++ )
        {
            char c1 = (char) ( r.nextInt( CONSTANT_NUMBER_LETTERS ) + CONSTANT_ASCII_CODE_A_UPPERCASE );
            listCharacters.add( Character.valueOf( c1 ) );
        }

        for ( int j = 0; j < nNumSmallLetters; j++ )
        {
            char c1 = (char) ( r.nextInt( CONSTANT_NUMBER_LETTERS ) + CONSTANT_ASCII_CODE_A_LOWERCASE );
            listCharacters.add( Character.valueOf( c1 ) );
        }

        for ( int j = 0; j < nNumNumbers; j++ )
        {
            char c1 = (char) ( r.nextInt( CONSTANT_NUMBER_NUMBERS_BASE10 - 1 ) + CONSTANT_ASCII_CODE_ZERO );
            listCharacters.add( Character.valueOf( c1 ) );
        }

        for ( int j = 0; j < nNumSpecial; j++ )
        {
            char c1 = CONSTANT_SPECIAL_CHARACTERS[r.nextInt( CONSTANT_SPECIAL_CHARACTERS.length )];
            listCharacters.add( Character.valueOf( c1 ) );
        }

        Collections.shuffle( listCharacters );

        StringBuilder sbPassword = new StringBuilder( listCharacters.size(  ) );

        for ( Character myChar : listCharacters )
        {
            sbPassword.append( myChar );
        }

        return sbPassword.toString(  );
    }

    /**
     * Check whether a password contains upper and lower case letters, special
     * characters and numbers.
     * @param strPassword The password to check
     * @return True if the password format is correct, false otherwise
     */
    public static boolean checkPasswordFormat( String strPassword )
    {
        return checkPasswordFormat( strPassword, true, true, true );
    }

    /**
     * Check whether a password contains upper and lower case letters, special
     * characters and numbers.
     * @param strPassword The password to check
     * @param bUpperAndLowerCase true if the password must contain upper and lower case
     * @param bNumero if the password must contain numero
     * @param bSpecialCaracters if the password must contain special characters
     *
     * @return True if the password format is correct, false otherwise
     */
    public static boolean checkPasswordFormat( String strPassword, boolean bUpperAndLowerCase, boolean bNumero,
        boolean bSpecialCaracters )
    {
        if ( ( strPassword == null ) || strPassword.isEmpty(  ) )
        {
            return false;
        }

        StringBuilder sbRegex = new StringBuilder( CONSTANT_PASSWORD_BEGIN_REGEX );

        if ( bUpperAndLowerCase )
        {
            sbRegex.append( CONSTANT_PASSWORD_REGEX_UPPER_LOWER );
        }

        if ( bNumero )
        {
            sbRegex.append( CONSTANT_PASSWORD_REGEX_NUM );
        }

        if ( bSpecialCaracters )
        {
            sbRegex.append( CONSTANT_PASSWORD_REGEX_SPECIAL );
        }

        sbRegex.append( CONSTANT_PASSWORD_END_REGEX );

        return strPassword.matches( sbRegex.toString(  ) );
    }

    /**
     * Get the maximum valid date of a password starting from now with the given
     * number of days.
     * @param nNumberDay The number of days the password is valid
     * @return The maximum valid date of a password
     */
    public static Timestamp getPasswordMaxValidDate( int nNumberDay )
    {
        if ( nNumberDay <= 0 )
        {
            return null;
        }

        long nMilliSeconds = DateUtil.convertDaysInMiliseconds( nNumberDay );
        Timestamp maxValidDate = new Timestamp( new java.util.Date(  ).getTime(  ) + nMilliSeconds );

        return maxValidDate;
    }
}
