/*
 * $Log: DispatcherManagerFactory.java,v $
 * Revision 1.1  2007/04/25 15:38:53  europe\L190409
 * updated JavaDoc
 *
 * Revision 1.1  2006/03/20 10:05:29  europe\L190409
 * first version
 *
 */
package nl.nn.adapterframework.dispatcher;

/**
 * Factory to obtain an instance of the {@link DispatcherManager}.
 * 
 * @author  Gerrit van Brakel
 * @since   Ibis 4.4.5
 * @version $Id: DispatcherManagerFactory.java,v 1.1 2007/04/25 15:38:53 europe\L190409 Exp $
 */
public class DispatcherManagerFactory {
	
	/**
	 * Obtain an instance of the {@link DispatcherManager}.
	 */
	public static DispatcherManager getDispatcherManager() throws DispatcherException {
		return DispatcherManagerImpl.getInstance();
	}

}
