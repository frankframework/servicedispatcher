/*
 * $Log: DispatcherException.java,v $
 * Revision 1.1  2007/04/25 15:38:53  europe\L190409
 * updated JavaDoc
 *
 * Revision 1.1  2006/03/20 10:05:29  europe\L190409
 * first version
 *
 */
package nl.nn.adapterframework.dispatcher;

/**
 * Exception signalling problems in the DispatcherManager. 
 * 
 * @author  Gerrit van Brakel
 * @since   Ibis 4.4.5
 * @version $Id: DispatcherException.java,v 1.1 2007/04/25 15:38:53 europe\L190409 Exp $
 */
public class DispatcherException extends Exception {

	public DispatcherException() {
		super();
	}
	public DispatcherException(String message) {
		super(message);
	}
	public DispatcherException(String message, Throwable cause) {
		super(message, cause);
	}
	public DispatcherException(Throwable cause) {
		super(cause);
	}

}
