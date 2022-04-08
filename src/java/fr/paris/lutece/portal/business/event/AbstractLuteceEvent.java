package fr.paris.lutece.portal.business.event;

public abstract class AbstractLuteceEvent<T> 
{
	   private T _param;
	   
	   /**
	    * constructor 
	    * @param _param
	    */	   
	   public AbstractLuteceEvent(T param) 
	   {
			super();
			this._param = param;
		}

	   /**
	    * Sets the event Param.
	    * 
	    * @param param
	    */
	   public void setParam( T param )
	   {
	       _param = param;
	   }

	   /**
	    * Gets the event param
	    * 
	    * @return the param
	    */
	   public T getParam( )
	   {
	       return _param;
	   }
}
