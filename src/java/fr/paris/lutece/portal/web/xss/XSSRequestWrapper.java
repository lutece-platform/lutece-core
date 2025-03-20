package fr.paris.lutece.portal.web.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import fr.paris.lutece.portal.service.html.XSSSanitizerService;

public class XSSRequestWrapper extends HttpServletRequestWrapper 
{

    public XSSRequestWrapper(HttpServletRequest request ) 
    {
        super(request);
    }

    @Override
    public String getParameter(String name) 
    {
   		return XSSSanitizerService.sanitize( super.getParameter(name) );
    }

    @Override
    public String[] getParameterValues(String name) 
    {
        String[] values = super.getParameterValues(name);
        if (values == null) 
        {
            return null;
        }
        for (int i = 0; i < values.length; i++) 
        {
            values[i] = XSSSanitizerService.sanitize( (values[i] ) );
        }
        return values;
    }
}
