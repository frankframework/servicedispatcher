package nl.nn.adapterframework.dispatcher;

public interface DllDispatcherManagerInterface {

	/**
	 * Execute a request on a registered service.
	 * 
	 * @param serviceName			name of the RequestProcessor to process the request on. Must match a name of a RequestProcessor {@link #register(String name, RequestProcessor listener) registered} with the DispatcherManager.
	 * @param correlationId			correlationId passed on to RequestProcessor. May be used to track processing of the message throug the business chain.  
	 * @param message				main message passed on to RequestProcessor. 
	 * @return						result of RequestProcessor
	 */
	public String processRequest(String serviceName, String correlationId, String requestMessage);

	/**
	 * Returns a comma separated list of all available services.
	 * 
	 * @return						available services
	 */
	public String getServices();
}
