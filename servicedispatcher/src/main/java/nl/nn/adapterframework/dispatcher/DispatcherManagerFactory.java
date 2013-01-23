/*
   Copyright 2013 IbisSource Project

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
