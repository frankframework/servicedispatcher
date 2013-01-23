/*
 * $Log: DispatcherManagerImpl.java,v $
 * Revision 1.3  2011/07/18 08:33:01  L190409
 * avoid NPE in searching class loader hierarchy
 *
 * Revision 1.2  2011/02/24 15:17:47  L190409
 * more flexibile implementation.
 * It just requires to be somewhere on a classpath of a common classloader
 *
 * Revision 1.1  2007/04/25 15:38:53  europe\L190409
 * updated JavaDoc
 *
 * Revision 1.2  2006/07/14 14:11:21  europe\L190409
 * set contextClassLoader to that of listener, to run in right context
 *
 * Revision 1.1  2006/03/20 10:05:29  europe\L190409
 * first version
 *
 */
package nl.nn.adapterframework.dispatcher;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * Actual implementation of {@link DispatcherManager}.
 * 
 * @author  Gerrit van Brakel
 * @version $Id: DispatcherManagerImpl.java,v 1.3 2011/07/18 08:33:01 L190409 Exp $
 */
class DispatcherManagerImpl implements DispatcherManager {

    private static final boolean DEBUG=true;
    
    /**
     *  This is effectively an instance of this class (although actually it may be instead a
     *  java.lang.reflect.Proxy wrapping an instance from the original classloader).
     */
    private static DispatcherManager instance = null;
    
	/*
	 * There can be only one - ie. even if the class is loaded in several different classloaders,
	 * there will be only one instance of the object.
	 */
    static DispatcherManager getInstance(ClassLoader classLoader) throws DispatcherException {
		DispatcherManager result = null;
		ClassLoader parentclassLoader = classLoader.getParent();
		// search for an instance as high in the classloader hierarchy as possible
		if (parentclassLoader != null) {
			result = getInstance(parentclassLoader);
			if (result == null) {
				ClassLoader myClassLoader = DispatcherManagerImpl.class.getClassLoader();
				Class classInstance;
				try {
					classInstance = parentclassLoader.loadClass(DispatcherManagerImpl.class.getName());
					// And call its getInstance method - this gives the correct
					// instance of ourself
					Method getInstanceMethod = classInstance.getDeclaredMethod("getInstance", new Class[] {});
					Object otherDispatcherManager = getInstanceMethod.invoke(null,new Object[] {});
					// But, we can't cast it to our own interface directly because classes loaded from
					// different classloaders implement different versions of an interface.
					// So instead, we use java.lang.reflect.Proxy to wrap it in an object that *does*
					// support our interface, and the proxy will use reflection to pass through all calls
					// to the object.
					result = (DispatcherManager) Proxy.newProxyInstance(myClassLoader, new Class[] { DispatcherManager.class },
																			new PassThroughProxyHandler(otherDispatcherManager));
					if (DEBUG) System.out.println("DispatcherManagerImpl INFO  created DispatcherManager using ClassLoader ["+ parentclassLoader.getClass().getName() + "] that is parent of ["+ classLoader.getClass().getName() + "]");
				} catch (Exception e) {
					if (DEBUG) System.out.println("DispatcherManagerImpl DEBUG "+ e.getClass().getName()+ " when trying to load DispatcherManager using ClassLoader ["+ parentclassLoader.getClass().getName() + "] that is parent of ["+ classLoader.getClass().getName() + "]: "+ e.getMessage());
					return null;
				}
			}
		}
		return result;
	}
    
    /**
     * Retrieve an instance of DispatcherManager from the original classloader. This is a true
     * Singleton, in that there will only be one instance of this object in the virtual machine,
     * even though there may be several copies of its class file loaded in different classloaders.
     */
    synchronized static DispatcherManager getInstance() throws DispatcherException {
    	if (instance==null) {
    		ClassLoader myClassLoader = DispatcherManagerImpl.class.getClassLoader();
    		if (myClassLoader!=null) {
    			instance = getInstance(myClassLoader);
    		} else {
    			System.out.println("DispatcherManagerImpl WARN  could not obtain ClassLoader for ["+ DispatcherManagerImpl.class.getName() + "], instantiated DispatcherManager might be too low in class path tree (not on a common branch)");
    		}
    		if (instance==null) {
    			instance = new DispatcherManagerImpl();
    		}
    	}
    	return instance;
    }

    private DispatcherManagerImpl() {
    }

	private HashMap requestProcessorMap = new HashMap();

	public String processRequest(String clientName, String message) throws DispatcherException, RequestProcessorException{
		return processRequest(clientName, null, message, null);
	}
	
	public String processRequest(String clientName, String message, HashMap requestContext) throws DispatcherException, RequestProcessorException{
		return processRequest(clientName, null, message, requestContext);
	}
	
	public String processRequest(String clientName, String correlationId, String message, HashMap requestContext) throws DispatcherException, RequestProcessorException {
		RequestProcessor listener=null;
		synchronized (requestProcessorMap) {
			listener = (RequestProcessor)requestProcessorMap.get(clientName);
		}
		if (listener==null) {
			throw new DispatcherException("no RequestProcessor registered for ["+clientName+"]");
		}
		try {
			ClassLoader callersClassLoader = Thread.currentThread().getContextClassLoader(); 
			try {
				// set contextClassLoader, in order to really switch to the application called.
				// This enables new classes to be loaded from proxy's own classpath, enabling
				// Xalan extension functions, like 'build-node()' 
				Thread.currentThread().setContextClassLoader(listener.getClass().getClassLoader());
				return listener.processRequest(correlationId,message,requestContext);
			} finally {
				Thread.currentThread().setContextClassLoader(callersClassLoader);
			}
		} catch (Throwable t) {
			throw new RequestProcessorException("RequestProcessor ["+clientName+"] caught exception",t);
		}
	}

	public void register(String name, RequestProcessor listener) throws DispatcherException {
		synchronized (requestProcessorMap) {
			requestProcessorMap.put(name, listener);
		}
	}

	
}

