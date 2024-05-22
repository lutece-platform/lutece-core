package fr.paris.lutece.portal.mocks;

import fr.paris.lutece.portal.service.mail.IMailQueue;
import fr.paris.lutece.portal.service.mail.MailItem;

@TestAlternative
public class AlternativeMockMailQueue implements IMailQueue
{

    @Override
    public MailItem consume()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void send(MailItem item)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int size()
    {
        // must return zero, in order to trigger nothing
        return 0;
    }

}
