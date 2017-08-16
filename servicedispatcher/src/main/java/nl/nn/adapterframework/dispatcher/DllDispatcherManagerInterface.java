package nl.nn.adapterframework.dispatcher;

/**
 * DllDispatcherManager allows to register services within a DLL, and to execute requests via them.
 * 
 * @author	Niels Meijer
 * @since	v1.4
 */
public interface DllDispatcherManagerInterface {

	/**
	 * Execute a request on a registered service.
	 * 
	 * @param serviceName			name of the RequestProcessor to process the request on. Must match a name of a RequestProcessor {@link #register(String name, RequestProcessor listener) registered} with the DllDispatcherManager.
	 * @param correlationId			correlationId passed on to RequestProcessor. May be used to track processing of the message through the business chain.  
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

	/**
	 * Registers a DLL in the DllDispatchManager
	 * 
	 * @param						name of the DLL to load
	 */
	public void registerDll(String dll);
}
