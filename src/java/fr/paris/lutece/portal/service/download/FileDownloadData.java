package fr.paris.lutece.portal.service.download;

import java.time.LocalDateTime;

public class FileDownloadData
{
    private final int _idResource;
    private final String _resourceType;
    private final int _idFile;
    private LocalDateTime endValidity;
    
    public FileDownloadData( int idResource, String resourceType, int idFile )
    {
        _idResource = idResource;
        _resourceType = resourceType;
        _idFile = idFile;
    }

    /**
     * @return the idResource
     */
    public int getIdResource( )
    {
        return _idResource;
    }

    /**
     * @return the resourceType
     */
    public String getResourceType( )
    {
        return _resourceType;
    }

    /**
     * @return the idFile
     */
    public int getIdFile( )
    {
        return _idFile;
    }

    /**
     * @return the endValidity
     */
    public LocalDateTime getEndValidity( )
    {
        return endValidity;
    }

    /**
     * @param endValidity the endValidity to set
     */
    public void setEndValidity( LocalDateTime endValidity )
    {
        this.endValidity = endValidity;
    }
    
}
