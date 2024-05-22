package fr.paris.lutece.portal.mocks;

import fr.paris.lutece.test.MyInterface;

@TestAlternative
public class AlternativeMyInterface implements MyInterface
{
    @Override
    public int theNumber()
    {
        return 6545654;
    }
}