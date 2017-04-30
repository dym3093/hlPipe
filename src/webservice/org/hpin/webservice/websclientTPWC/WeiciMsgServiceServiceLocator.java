/**
 * WeiciMsgServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.hpin.webservice.websclientTPWC;

public class WeiciMsgServiceServiceLocator extends org.apache.axis.client.Service implements org.hpin.webservice.websclientTPWC.WeiciMsgServiceService {

    public WeiciMsgServiceServiceLocator() {
    }


    public WeiciMsgServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WeiciMsgServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WeiciMsgServicePort
    private java.lang.String WeiciMsgServicePort_address = "http://wpttest.ubao123.com:8088/services/WeiciMsgService";

    public java.lang.String getWeiciMsgServicePortAddress() {
        return WeiciMsgServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WeiciMsgServicePortWSDDServiceName = "WeiciMsgServicePort";

    public java.lang.String getWeiciMsgServicePortWSDDServiceName() {
        return WeiciMsgServicePortWSDDServiceName;
    }

    public void setWeiciMsgServicePortWSDDServiceName(java.lang.String name) {
        WeiciMsgServicePortWSDDServiceName = name;
    }

    public org.hpin.webservice.websclientTPWC.WeiciMsgService getWeiciMsgServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WeiciMsgServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWeiciMsgServicePort(endpoint);
    }

    public org.hpin.webservice.websclientTPWC.WeiciMsgService getWeiciMsgServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.hpin.webservice.websclientTPWC.WeiciMsgServicePortBindingStub _stub = new org.hpin.webservice.websclientTPWC.WeiciMsgServicePortBindingStub(portAddress, this);
            _stub.setPortName(getWeiciMsgServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWeiciMsgServicePortEndpointAddress(java.lang.String address) {
        WeiciMsgServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.hpin.webservice.websclientTPWC.WeiciMsgService.class.isAssignableFrom(serviceEndpointInterface)) {
                org.hpin.webservice.websclientTPWC.WeiciMsgServicePortBindingStub _stub = new org.hpin.webservice.websclientTPWC.WeiciMsgServicePortBindingStub(new java.net.URL(WeiciMsgServicePort_address), this);
                _stub.setPortName(getWeiciMsgServicePortWSDDServiceName());
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
        if ("WeiciMsgServicePort".equals(inputPortName)) {
            return getWeiciMsgServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://web.healthlink.cn/", "WeiciMsgServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://web.healthlink.cn/", "WeiciMsgServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WeiciMsgServicePort".equals(portName)) {
            setWeiciMsgServicePortEndpointAddress(address);
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
