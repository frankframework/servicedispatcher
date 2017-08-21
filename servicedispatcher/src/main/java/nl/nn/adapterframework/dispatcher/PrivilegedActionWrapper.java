package nl.nn.adapterframework.dispatcher;

import java.security.PrivilegedAction;
import java.util.HashMap;

public class PrivilegedActionWrapper implements PrivilegedAction<RunResult> {

	private RequestProcessor listener;
	private String correlationId;
	private String message;
	private HashMap requestContext;

	public PrivilegedActionWrapper(RequestProcessor listener, String correlationId, String message, HashMap requestContext) {
		this.listener = listener;
		this.correlationId = correlationId;
		this.message = message;
		this.requestContext = requestContext;
	}

	@Override
	public RunResult run() {
		RunResult result = new RunResult();
		try {
			result.setResult(listener.processRequest(correlationId, message, requestContext));
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}
}
