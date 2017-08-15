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

import java.util.HashMap;

/**
 * Defines interface to be implemented by service provider. 
 * 
 * @author  Gerrit van Brakel
 * @since   Ibis 4.4.5
 */
public interface RequestProcessor {

	/**
	 * Called by {@link DispatcherManager} to execute a request.
	 * 
	 * @param correlationId     String to identify the request 
	 * @param message			Actual message to be processed, mostly XML
	 * @param requestContext    Map of name value pairs, to assist in the processing of the message. 
	 * 							Values can be Strings or any other Java object, as required by the processor.
	 * 							Upon return (even in the case of an exception), values may be set by the processor.
	 * @return					The result message, mostly XML
	 * @throws Exception		Any exception may be thrown by the request processor. It will be captured and
	 * 							wrapped into a {@link RequestProcessorException} by the Dispatcher
	 */
	public String processRequest(String correlationId, String message, HashMap requestContext) throws Exception;
}