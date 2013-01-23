/*
 * $Log: CallingParty.java,v $
 * Revision 1.3  2008/09/10 09:28:23  europe\L190409
 * small bugfix...
 *
 * Revision 1.2  2008/09/10 09:25:47  europe\L190409
 * added display of context upon return
 *
 * Revision 1.1  2007/04/26 07:21:54  europe\L190409
 * examples
 *
 */
package examples.dispatcher;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.nn.adapterframework.dispatcher.DispatcherException;
import nl.nn.adapterframework.dispatcher.DispatcherManager;
import nl.nn.adapterframework.dispatcher.DispatcherManagerFactory;


/**
 * Example of servlet calling a service via the service dispatcher.
 * 
 * @author  Gerrit van Brakel
 * @version $Id: CallingParty.java,v 1.3 2008/09/10 09:28:23 europe\L190409 Exp $
 */
public class CallingParty extends HttpServlet {

	public static String SERVICENAME="ExampleService"; 

	private DispatcherManager dm=null;

	public void init() throws ServletException {
		super.init();
		
		try {
			// obtain reference to DispatcherManager
			dm = DispatcherManagerFactory.getDispatcherManager();
		} catch (DispatcherException e) {
			String msg="Could not obtain DispatcherManager; "+
				"Please check that IbisServiceDispatcher.jar is installed on the class path of the server";
			throw new ServletException(msg,e);
		}
	}


	protected void doGet(HttpServletRequest requuest, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		String message="testmessage from CallingParty";
		HashMap context=new HashMap();
	
		pw.print("<html>");
		pw.print("<body>");
		pw.print("Calling service ["+SERVICENAME+"] via service dispatcher ...");
		try {
			pw.print("result =["+dm.processRequest(SERVICENAME,message,context)+"]");
		} catch (Exception e) {
			log("Exception calling service", e);
			pw.print("Exception calling service: "+ e.getMessage());
		}
		pw.print("<br/><br/>");
		if (context.size()==0) {
			pw.print("No parameters present in context");
		} else {
			pw.print("parameters in context:<br/>");
			for (Iterator it=context.keySet().iterator();it.hasNext();) {
				String key=(String)it.next();
				Object value=context.get(key);
				pw.print("key ["+key+"] value ["+value+"]<br/>");
			}
		}
		pw.print("</body>");
		pw.print("</html>");
		pw.close();
	}


}
