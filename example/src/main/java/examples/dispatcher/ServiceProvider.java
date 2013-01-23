/*
 * $Log: ServiceProvider.java,v $
 * Revision 1.1  2007/04/26 07:21:54  europe\L190409
 * examples
 *
 */
package examples.dispatcher;

import nl.nn.adapterframework.dispatcher.DispatcherException;
import nl.nn.adapterframework.dispatcher.DispatcherManager;
import nl.nn.adapterframework.dispatcher.DispatcherManagerFactory;
import nl.nn.adapterframework.dispatcher.RequestProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Example of a class that provides a service via the service dispatcher.
 *
 * @author  Gerrit van Brakel
 * @version $Id: ServiceProvider.java,v 1.1 2007/04/26 07:21:54 europe\L190409 Exp $
 */
public class ServiceProvider extends HttpServlet implements RequestProcessor {

	public static String SERVICENAME="ExampleService";
	private int timesCalled=0;
	private DispatcherManager dm=null;

	public String processRequest(String correlationId, String message, HashMap requestContext) throws Exception {
		timesCalled++;
		String result="Your message with correlationId ["+correlationId+"] is message no ["+timesCalled+"] received and processed by the ServiceProvider";

		System.out.println("Example ServiceProvider called, correlationId ["+correlationId+"], message ["+message+"]");
		System.out.println("Example ServiceProvider will respond with ["+result+"]");

		return result;
	}

	public void init() throws ServletException {
		super.init();

		try {
			// obtain reference to DispatcherManager
			dm = DispatcherManagerFactory.getDispatcherManager();
			// register the service provider with DispatcherManager
			dm.register(SERVICENAME,this);
		} catch (DispatcherException e) {
			String msg="Could not obtain DispatcherManager or register provider; "+
				"Please check that IbisServiceDispatcher.jar is installed on the class path of the server";
			throw new ServletException(msg,e);
		}
		// now wait for requests to arrive
	}

	public void destroy() {
		if (dm!=null) {
			// unregister the service provider with DispatcherManager
			try {
				dm.register(SERVICENAME,null);
			} catch (DispatcherException e) {
				log("Exception unregistering with DispatcherManager", e);
			}
		}
		super.destroy();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		pw.print("<html>");
		pw.print("<body>");
		pw.print("ServiceProvider ["+SERVICENAME+"] called ["+timesCalled+"] times via service dispatcher");
		pw.print("</body>");
		pw.print("</html>");
		pw.close();
	}

}
