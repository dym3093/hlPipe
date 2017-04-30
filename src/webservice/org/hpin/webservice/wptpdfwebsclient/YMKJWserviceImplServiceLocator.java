/**
 * YMKJWserviceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.hpin.webservice.wptpdfwebsclient;



public class YMKJWserviceImplServiceLocator extends org.apache.axis.client.Service implements org.hpin.webservice.wptpdfwebsclient.YMKJWserviceImplService {

	private static YMKJWserviceImplServiceLocator instance = null;
	public static YMKJWserviceImplServiceLocator getInstance() {
		if (instance == null) {
			System.out.println("init--------");
			instance = init();
		}
		return instance;
	}

	private static YMKJWserviceImplServiceLocator init() {
		instance = new YMKJWserviceImplServiceLocator();
		return instance;
	}
	
    public YMKJWserviceImplServiceLocator() {
    }


    public YMKJWserviceImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public YMKJWserviceImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for YMKJWserviceImplPort
    //private java.lang.String YMKJWserviceImplPort_address = "http://wptnew.redcome.com:80/ymkjService/webservice/YMKJWservice";//测试
      private java.lang.String YMKJWserviceImplPort_address = "http://web.healthlink.cn:82/ymkjService/webservice/YMKJWservice";//生产
    public java.lang.String getYMKJWserviceImplPortAddress() {
        return YMKJWserviceImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String YMKJWserviceImplPortWSDDServiceName = "YMKJWserviceImplPort";

    public java.lang.String getYMKJWserviceImplPortWSDDServiceName() {
        return YMKJWserviceImplPortWSDDServiceName;
    }

    public void setYMKJWserviceImplPortWSDDServiceName(java.lang.String name) {
        YMKJWserviceImplPortWSDDServiceName = name;
    }

    public org.hpin.webservice.wptpdfwebsclient.YMKJWserviceImpl getYMKJWserviceImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(YMKJWserviceImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getYMKJWserviceImplPort(endpoint);
    }

    public org.hpin.webservice.wptpdfwebsclient.YMKJWserviceImpl getYMKJWserviceImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.hpin.webservice.wptpdfwebsclient.YMKJWserviceImplPortBindingStub _stub = new org.hpin.webservice.wptpdfwebsclient.YMKJWserviceImplPortBindingStub(portAddress, this);
            _stub.setPortName(getYMKJWserviceImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setYMKJWserviceImplPortEndpointAddress(java.lang.String address) {
        YMKJWserviceImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.hpin.webservice.wptpdfwebsclient.YMKJWserviceImpl.class.isAssignableFrom(serviceEndpointInterface)) {
                org.hpin.webservice.wptpdfwebsclient.YMKJWserviceImplPortBindingStub _stub = new org.hpin.webservice.wptpdfwebsclient.YMKJWserviceImplPortBindingStub(new java.net.URL(YMKJWserviceImplPort_address), this);
                _stub.setPortName(getYMKJWserviceImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("YMKJWserviceImplPort".equals(inputPortName)) {
            return getYMKJWserviceImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("com.ymkjM.WSService.impl", "YMKJWserviceImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("com.ymkjM.WSService.impl", "YMKJWserviceImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("YMKJWserviceImplPort".equals(portName)) {
            setYMKJWserviceImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
