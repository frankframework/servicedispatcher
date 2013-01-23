/*
 * $Log: RequestProcessorException.java,v $
 * Revision 1.1  2007/04/25 15:38:53  europe\L190409
 * updated JavaDoc
 *
 * Revision 1.1  2006/03/20 10:05:29  europe\L190409
 * first version
 *
 */
package nl.nn.adapterframework.dispatcher;

/**
 * Exception signalling problems while processing a request. 
 * 
 * @author  Gerrit van Brakel
 * @since   Ibis 4.4.5
 * @version $Id: RequestProcessorException.java,v 1.1 2007/04/25 15:38:53 europe\L190409 Exp $
 */
public class RequestProcessorException extends Exception {

	public RequestProcessorException() {
		super();
	}
	public RequestProcessorException(String message) {
		super(message);
	}
	public RequestProcessorException(String message, Throwable cause) {
		super(message, cause);
	}
	public RequestProcessorException(Throwable cause) {
		super(cause);
	}

}
