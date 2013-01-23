/*
 * $Log: PassThroughProxyHandler.java,v $
 * Revision 1.1  2007/04/25 15:38:52  europe\L190409
 * updated JavaDoc
 *
 * Revision 1.1  2006/03/20 10:05:30  europe\L190409
 * first version
 *
 */
package nl.nn.adapterframework.dispatcher;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * An invocation handler that passes on any calls made to it directly to its delegate.
 * This is useful to handle identical classes loaded in different classloaders - the
 * VM treats them as different classes, but they have identical signatures.
 * <p>
 * Note this is using class.getMethod, which will only work on public methods.
 *
 * @since   Ibis 4.4.5
 * @author Inigo Surguy
 */
class PassThroughProxyHandler implements InvocationHandler {
    private final Object delegate;
    public PassThroughProxyHandler(Object delegate) {
        this.delegate = delegate;
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method delegateMethod = delegate.getClass().getMethod(method.getName(), method.getParameterTypes());
        return delegateMethod.invoke(delegate, args);
    }
}
