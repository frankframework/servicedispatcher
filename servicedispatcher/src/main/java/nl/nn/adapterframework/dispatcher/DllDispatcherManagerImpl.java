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
import java.util.List;
import java.util.StringTokenizer;

import nl.nn.adapterframework.dispatcher.DispatcherException;

public class DllDispatcherManagerImpl implements DispatcherManager {

	private DllDispatcherManagerInterface DllInstance;
	private static final boolean DEBUG=true;
	private List<String> availableServices = new ArrayList<String>();

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
				ClassLoader myClassLoader = DllDispatcherManagerImpl.class.getClassLoader();
				Class classInstance;
				try {
					classInstance = parentclassLoader.loadClass(DllDispatcherManagerImpl.class.getName());
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
			ClassLoader myClassLoader = DllDispatcherManagerImpl.class.getClassLoader();
			if (myClassLoader!=null) {
				instance = getInstance(myClassLoader);
			} else {
				System.out.println("DllDispatcherManagerImpl WARN  could not obtain ClassLoader for ["+ DllDispatcherManagerImpl.class.getName() + "], instantiated DispatcherManager might be too low in class path tree (not on a common branch)");
			}
			if (instance==null) {
				instance = new DllDispatcherManagerImpl();
			}
		}
		return instance;
	}

	private DllDispatcherManagerImpl() throws DispatcherException {
		try {
			Class<?> dll = Class.forName("DllDispatcherManager");
			DllInstance = (DllDispatcherManagerInterface) dll.newInstance();
		}
		catch (Exception e) {
			throw new DispatcherException("Failed to initialize DllDispatcherManager!", e);
		}
		String services = DllInstance.getServices();
		if(!services.isEmpty()) {
			StringTokenizer st = new StringTokenizer(services, ",");  
			while (st.hasMoreTokens()) {
				register(st.nextToken());
			}
		}
		else {
			throw new DispatcherException("Successfully loaded DllDispatcherManager, but no services were found!");
		}
	}

	@Override
	public String processRequest(String serviceName, String message) throws DispatcherException, RequestProcessorException {
		return processRequest(serviceName, null, message, null);
	}

	@Override
	public String processRequest(String serviceName, String message, HashMap requestContext) throws DispatcherException, RequestProcessorException{
		return processRequest(serviceName, null, message, requestContext);
	}

	@Override
	public String processRequest(String serviceName, String correlationId, String message, HashMap requestContext) throws DispatcherException, RequestProcessorException {
		try {
			boolean registeredService = false;
			synchronized (availableServices) {
				registeredService = availableServices.contains(serviceName);
			}
			if (registeredService==false) {
				throw new DispatcherException("no service registered for ["+serviceName+"]");
			}
			return DllInstance.processRequest(serviceName, correlationId, message);
		}
		catch (Exception e) {
			throw new DispatcherException("Error while processing service ["+serviceName+"]", e);
		}
	}

	@Override
	public void register(String serviceName, RequestProcessor listener) throws DispatcherException {
		register(serviceName);
	}

	public void register(String serviceName) {
		synchronized (availableServices) {
			availableServices.add(serviceName);
		}
	}
}