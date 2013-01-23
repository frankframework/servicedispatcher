/*
 * $Log: RequestProcessor.java,v $
 * Revision 1.2  2007/10/16 15:11:21  europe\L190409
 * updated javadoc
 *
 * Revision 1.1  2007/04/25 15:38:52  europe\L190409
 * updated JavaDoc
 *
 * Revision 1.1  2006/03/20 10:05:29  europe\L190409
 * first version
 *
 */
package nl.nn.adapterframework.dispatcher;

import java.util.HashMap;

/**
 * Defines interface to be implemented by service provider. 
 * 
 * @author  Gerrit van Brakel
 * @since   Ibis 4.4.5
 * @version $Id: RequestProcessor.java,v 1.2 2007/10/16 15:11:21 europe\L190409 Exp $
 */
public interface RequestProcessor {

	/**
	 * Called by {@link DispatcherManager} to execute a request.
	 * 
	 * @param correlationId     String to identify the request 
	 * @param message			Acutal message to be processed, mostly XML
	 * @param requestContext    Map of name value pairs, to assist in the processing of the message. 
	 * 							Values can be Strings or any other Java object, as required by the processor.
	 * 							Upon return (even in the case of an exception), values may be set by the processor.
	 * @return					The result message, mostly XML
	 * @throws Exception		Any exception may be thrown by the request processor. It will be captured and
	 * 							wrapped into a {@link RequestProcessorException} by the Dispatcher
	 */
	public String processRequest(String correlationId, String message, HashMap requestContext) throws Exception;
}