import java.io.InputStream;
import java.util.HashMap;

import nl.nn.adapterframework.dispatcher.DispatcherException;
import nl.nn.adapterframework.dispatcher.DispatcherManager;
import nl.nn.adapterframework.dispatcher.DispatcherManagerFactory;
import nl.nn.adapterframework.dispatcher.RequestProcessorException;

/*
 * $Log: Example.java,v $
 * Revision 1.3  2011/02/24 15:18:59  L190409
 * cosmetic change
 *
 * Revision 1.2  2007/07/13 11:32:37  europe\L190409
 * update structure
 *
 * Revision 1.1  2007/03/14 14:27:50  europe\L190409
 * voorbeeld voor streaming
 *
 */

/**
 * 
 * 
 * @author L190409
 * @since  
 * @version $Id: Example.java,v 1.3 2011/02/24 15:18:59 L190409 Exp $
 */
public class Example {



	// calling ibis 
	public void call(String metadata, InputStream stream) throws DispatcherException, RequestProcessorException {
		String clientName="ibis4wgp-importFile";
		
		DispatcherManager dm = DispatcherManagerFactory.getDispatcherManager();
		
		String message = metadata;
		HashMap context = new HashMap();
		context.put("stream",stream);
		
		String result = dm.processRequest(clientName, message, context);
		
	}
	
	
	
//	PipeRunResult doPipe (Object input, PipeLineSession session) throws PipeRunException; {
//		String metadata=(String)input;
//		InputStream stream = (InputStream)session.getSessionVariable("stream"); 
//		
//	}

	
}
