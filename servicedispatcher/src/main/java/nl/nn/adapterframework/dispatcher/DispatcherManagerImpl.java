/*
   Copyright 2013, 2017 Nationale-Nederlanden

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package nl.nn.adapterframework.dispatcher;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

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

	private HashMap<String, RequestProcessor> requestProcessorMap = new HashMap<String, RequestProcessor>();

	public String processRequest(String serviceName, String message) throws DispatcherException, RequestProcessorException{
		return processRequest(serviceName, null, message, null);
	}

	public String processRequest(String serviceName, String message, HashMap requestContext) throws DispatcherException, RequestProcessorException{
		return processRequest(serviceName, null, message, requestContext);
	}

	public String processRequest(String serviceName, String correlationId, String message, HashMap requestContext) throws DispatcherException, RequestProcessorException {
		RequestProcessor listener=null;

		synchronized (requestProcessorMap) {
			listener = (RequestProcessor)requestProcessorMap.get(serviceName);
		}
		if (listener==null) {
			throw new DispatcherException("no RequestProcessor registered for ["+serviceName+"]");
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
			throw new RequestProcessorException("RequestProcessor ["+serviceName+"] caught exception",t);
		}
	}

	public void register(String serviceName, RequestProcessor listener) throws DispatcherException {
		synchronized (requestProcessorMap) {
			requestProcessorMap.put(serviceName, listener);
		}
	}

	public void unregister(String serviceName) {
		synchronized (requestProcessorMap) {
			if(requestProcessorMap.containsKey(serviceName))
				requestProcessorMap.remove(serviceName);
		}
	}

	public List<String> getRegisteredServices() {
		synchronized (requestProcessorMap) {
			return new ArrayList<String>(requestProcessorMap.keySet());
		}
	}
}

