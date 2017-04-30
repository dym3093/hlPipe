/**
 * GeneEventServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.hpin.webservice.wptwebsclient;

import org.apache.axis.client.Service;

public class GeneEventServiceServiceLocator extends Service implements GeneEventServiceService {

	public GeneEventServiceServiceLocator() {
    }


    public GeneEventServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GeneEventServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GeneEventServicePort
    //测试环境
//    private java.lang.String GeneEventServicePort_address = "http://weifuwu.redcome.com:80/services/GeneEventService";
    //正式环境  add by YoumingDeng 2016-08-30
    private java.lang.String GeneEventServicePort_address = "http://web.healthlink.cn:8088/services/GeneEventService";

    public java.lang.String getGeneEventServicePortAddress() {
        return GeneEventServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GeneEventServicePortWSDDServiceName = "GeneEventServicePort";

    public java.lang.String getGeneEventServicePortWSDDServiceName() {
        return GeneEventServicePortWSDDServiceName;
    }

    public void setGeneEventServicePortWSDDServiceName(java.lang.String name) {
        GeneEventServicePortWSDDServiceName = name;
    }

    public GeneEventService getGeneEventServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GeneEventServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGeneEventServicePort(endpoint);
    }

    public GeneEventService getGeneEventServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            GeneEventServicePortBindingStub _stub = new GeneEventServicePortBindingStub(portAddress, this);
            _stub.setPortName(getGeneEventServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGeneEventServicePortEndpointAddress(java.lang.String address) {
        GeneEventServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (GeneEventService.class.isAssignableFrom(serviceEndpointInterface)) {
                GeneEventServicePortBindingStub _stub = new GeneEventServicePortBindingStub(new java.net.URL(GeneEventServicePort_address), this);
                _stub.setPortName(getGeneEventServicePortWSDDServiceName());
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
        if ("GeneEventServicePort".equals(inputPortName)) {
            return getGeneEventServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://web.healthlink.cn/", "GeneEventServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://web.healthlink.cn/", "GeneEventServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GeneEventServicePort".equals(portName)) {
            setGeneEventServicePortEndpointAddress(address);
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
