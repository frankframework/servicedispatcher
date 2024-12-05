[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/ibissource/ibis-servicedispatcher/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/org.ibissource/service-dispatcher-parent.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:org.ibissource)
[![Build Status](https://travis-ci.org/ibissource/ibis-servicedispatcher.png?branch=master)](https://travis-ci.org/ibissource/ibis-servicedispatcher)

ibis-servicedispatcher
=======================


The IbisServiceDispatcher can be used to communicate between applications that reside in the same
JVM or to communicate with a DLL.
It was developed to provide a means of communication in native Java between the Everest 
Knowledge Framework (EKF) and the adapters build using the Ibis Framework.

<h2>usage</h2>
To use the IbisServiceDispatcher, an instance of the DispatcherManager 
must be obtained by calling the static method getDispatcherManager()
on DispatcherManagerFactory. Then: 
<ul>
  <li>to call a service, call processRequest(String clientName, String correlationId, String message, HashMap requestContext) on the DispatcherManager</li>
  <li>to provide a service, register a RequestProcessor using 
  register(RequestProcessor)</li>
</ul>

<h2>Configuration</h2>
Use the following code in an Ibis-configuration to provide a service using the service dispatcher:<br>
(Any parameter passed in the requestContext is made available to the Adapter as a PipeLineSession-variable)
<pre>
  &lt;receiver name="JavaListener" returnIfStopped="Service not available"&gt;
    &lt;listener 
      className="nl.nn.adapterframework.receivers.JavaListener" 
      serviceName="ibis4xxx-myadapter" 
    /&gt;
  &lt;/receiver&gt;
</pre>
<br>
To call a service provided by the service dispatcher from within a Ibis-configuration, use the JavaSender, like this:<br>
(Any parameter defined with the sender is passed on to the processing service in the requestContext)
<pre>
  &lt;pipe name="PipeCallingService"&gt;
    &lt;sender 
      className="nl.nn.adapterframework.pipes.IbisJavaSender" 
      serviceName="nameOfService" &gt;
      &lt;param name="param1" value="valueOfThisParameter"/&gt;
    &lt;sender&gt;
  &lt;/pipe&gt;
</pre>
<br>
When using the dispatcher to call system libraries, you have to add the `dispatchType="DLL"` attribute.
<pre>
  &lt;pipe name="PipeCallingService"&gt;
    &lt;sender 
      className="nl.nn.adapterframework.pipes.IbisJavaSender" 
      dispatchType="DLL"
      serviceName="nameOfService" &gt;
    &lt;sender&gt;
  &lt;/pipe&gt;
</pre>
NOTE: In order te register system libraries in the Service Dispatcher you have to set the system property `ibis-servicedispatcher.dlls=path/to/library.dll`
<br>
<br>

<h2>operation</h2>
The IbisServiceDispatcher provides a map of RequestProcessors. This map is created by applications
that register themselves with the servicedispatcher. Other applications can use the 
servicedispatcher to execute a request by a named RequestProcessor. The servicedispatcher
locates the RequestProcessor in its map, set the appropriate classloader context, and executes
the request as if it was done within the application the ReqestProcessor resides.
To be able to do so, the servicedispatcher must be deployed on the classpath of the server, that 
is shared by all applications.

<h2>installation</h2>
To install the IbisServiceDispatcher, the library must be
placed on the classpath of the server. Due to the way of operation, it is not sufficient to have
it only in a lib directory of a .war file or similar.
