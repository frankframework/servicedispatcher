package nl.nn.adapterframework.dispatcher;

public class RunResult {

	private String result = null;
	private Exception exception = null;

	public void setResult(String result) {
		this.result = result;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public boolean hasException() {
		return (exception != null);
	}

	public Exception getException() {
		return exception;
	}

	public String getResult() {
		return result;
	}
}
