import nl.nn.adapterframework.dispatcher.DllDispatcherManagerInterface;

public class DllDispatcherManager implements DllDispatcherManagerInterface {

	@Override
	native public String processRequest(String functionName, String correlationId, String requestMessage);

	@Override
	native public String getServices();

	static {
		System.loadLibrary("Wrapper");
	}
}