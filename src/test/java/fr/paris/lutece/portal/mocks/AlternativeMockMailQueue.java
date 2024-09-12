package fr.paris.lutece.portal.mocks;

import fr.paris.lutece.portal.service.mail.IMailQueue;
import fr.paris.lutece.portal.service.mail.MailItem;

@TestAlternative
public class AlternativeMockMailQueue implements IMailQueue
{

    @Override
    public MailItem consume()
    {
        return null;
    }

    @Override
    public void send(MailItem item)
    {

    }

    @Override
    public int size()
    {
        return 0;
    }

}
