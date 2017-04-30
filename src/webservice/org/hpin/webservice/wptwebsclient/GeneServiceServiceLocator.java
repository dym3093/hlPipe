/**
 * GeneServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.hpin.webservice.wptwebsclient;



public class GeneServiceServiceLocator extends org.apache.axis.client.Service implements org.hpin.webservice.wptwebsclient.GeneServiceService {

	private static GeneServiceServiceLocator instance = null;
	public static GeneServiceServiceLocator getInstance() {
		if (instance == null) {
			System.out.println("init--------");
			instance = init();
		}
		return instance;
	}

	private static GeneServiceServiceLocator init() {
		instance = new GeneServiceServiceLocator();
		return instance;
	}
    public GeneServiceServiceLocator() {
    }


    public GeneServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GeneServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GeneServicePort
    private java.lang.String GeneServicePort_address = "http://web.healthlink.cn:8088/services/GeneService";

    public java.lang.String getGeneServicePortAddress() {
        return GeneServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GeneServicePortWSDDServiceName = "GeneServicePort";

    public java.lang.String getGeneServicePortWSDDServiceName() {
        return GeneServicePortWSDDServiceName;
    }

    public void setGeneServicePortWSDDServiceName(java.lang.String name) {
        GeneServicePortWSDDServiceName = name;
    }

    public org.hpin.webservice.wptwebsclient.GeneService getGeneServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GeneServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGeneServicePort(endpoint);
    }

    public org.hpin.webservice.wptwebsclient.GeneService getGeneServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.hpin.webservice.wptwebsclient.GeneServicePortBindingStub _stub = new org.hpin.webservice.wptwebsclient.GeneServicePortBindingStub(portAddress, this);
            _stub.setPortName(getGeneServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGeneServicePortEndpointAddress(java.lang.String address) {
        GeneServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.hpin.webservice.wptwebsclient.GeneService.class.isAssignableFrom(serviceEndpointInterface)) {
                org.hpin.webservice.wptwebsclient.GeneServicePortBindingStub _stub = new org.hpin.webservice.wptwebsclient.GeneServicePortBindingStub(new java.net.URL(GeneServicePort_address), this);
                _stub.setPortName(getGeneServicePortWSDDServiceName());
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
        if ("GeneServicePort".equals(inputPortName)) {
            return getGeneServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://web.healthlink.cn/", "GeneServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://web.healthlink.cn/", "GeneServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GeneServicePort".equals(portName)) {
            setGeneServicePortEndpointAddress(address);
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
