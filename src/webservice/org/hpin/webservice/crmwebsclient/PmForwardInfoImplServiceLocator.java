/**
 * PmForwardInfoImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.hpin.webservice.crmwebsclient;

public class PmForwardInfoImplServiceLocator extends org.apache.axis.client.Service implements PmForwardInfoImplService {

    public PmForwardInfoImplServiceLocator() {
    }


    public PmForwardInfoImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PmForwardInfoImplServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PmForwardInfoImplPort
    private String PmForwardInfoImplPort_address = "http://192.168.1.16:8088/webservice01/hbspmservice";

    public String getPmForwardInfoImplPortAddress() {
        return PmForwardInfoImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String PmForwardInfoImplPortWSDDServiceName = "PmForwardInfoImplPort";

    public String getPmForwardInfoImplPortWSDDServiceName() {
        return PmForwardInfoImplPortWSDDServiceName;
    }

    public void setPmForwardInfoImplPortWSDDServiceName(String name) {
        PmForwardInfoImplPortWSDDServiceName = name;
    }

    public PmForwardInfoImpl getPmForwardInfoImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PmForwardInfoImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPmForwardInfoImplPort(endpoint);
    }

    public PmForwardInfoImpl getPmForwardInfoImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            PmForwardInfoImplServiceSoapBindingStub _stub = new PmForwardInfoImplServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getPmForwardInfoImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPmForwardInfoImplPortEndpointAddress(String address) {
        PmForwardInfoImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (PmForwardInfoImpl.class.isAssignableFrom(serviceEndpointInterface)) {
                PmForwardInfoImplServiceSoapBindingStub _stub = new PmForwardInfoImplServiceSoapBindingStub(new java.net.URL(PmForwardInfoImplPort_address), this);
                _stub.setPortName(getPmForwardInfoImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
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
        String inputPortName = portName.getLocalPart();
        if ("PmForwardInfoImplPort".equals(inputPortName)) {
            return getPmForwardInfoImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservice.hl/", "PmForwardInfoImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservice.hl/", "PmForwardInfoImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("PmForwardInfoImplPort".equals(portName)) {
            setPmForwardInfoImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
