/**
 * PmForwardInfoImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.hpin.webservice.crmwebsclient;

public interface PmForwardInfoImpl extends java.rmi.Remote {
    public String getClientInfo() throws java.rmi.RemoteException;
    public String pushAdviceServiceMessage(String arg0, String arg1, String arg2) throws java.rmi.RemoteException;
    public String productMemberImmediateApi(String arg0) throws java.rmi.RemoteException;
    public boolean checkIsPhoneMember(String arg0) throws java.rmi.RemoteException;
    public String pushFirstAidServiceMessage(String arg0, String arg1, String arg2) throws java.rmi.RemoteException;
    public String productMemberImmediate(String arg0) throws java.rmi.RemoteException;
    public String productMemberImmediateTest(String arg0) throws java.rmi.RemoteException;
    public String cancelServiceMessage(String arg0, String arg1, String arg2) throws java.rmi.RemoteException;
    public String productMemberImmediateCustomTest(String arg0, String arg1) throws java.rmi.RemoteException;
    public String productMemberImmediateApiTest(String arg0) throws java.rmi.RemoteException;
    public String pushGuideFormServiceMessage(String arg0, String arg1, String arg2) throws java.rmi.RemoteException;
    public String pushCriticalServiceMessage(String arg0, String arg1, String arg2) throws java.rmi.RemoteException;
    public String productMemberImmediateCustom(String arg0, String arg1) throws java.rmi.RemoteException;
    public String pushGuideServiceMessage(String arg0, String arg1, String arg2) throws java.rmi.RemoteException;
}
