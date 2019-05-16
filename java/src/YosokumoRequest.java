// YosokumoRequest.java

package com.yosokumo.core;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.Header; 
import org.apache.http.HeaderIterator;  // Only needed for debug output
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.DateUtils; 

import java.io.*;
import java.util.Date;

/**
 * Implements all HTTP requests to the Yosokumo web service.  These are the 
 * basic methods:
 * <ul>
 * <li>getFromServer()
 * <li>postToServer()
 * <li>deleteFromServer()
 * <li>putToServer()
 * </ul>
 * These above methods all return false in case of a problem.  The caller can 
 * then use these methods to determine the cause of the failure:
 * <ul>
 * <li>getStatusCode()
 * <li>getEntity()
 * <li>getException()
 * </ul>
 * @author  Roger House
 * @version 0.9
 */

class YosokumoRequest
{
    private boolean trace = false;  // Set true to get debug trace 

    private Credentials credentials;
    private String      hostName;
    private int         port;
    private String      contentType;

    private String      auxHeaderName  = null;    // Auxiliary header name
    private String      auxHeaderValue = null;    //   and value

    private int statusCode             = 0;
    private byte [] entity             = null;
    private ServiceException exception = null;

    /**
     * Initializes a newly created {@code YosokumoRequest} object with 
     * attributes specified by the input parameters.
     *
     * @param   credentials specifies user id and key for authentication.
     * @param   hostName is the name of the Yosokumo server.
     * @param   port is the port to use to access the Yosokumo service.
     * @param   contentType is the content type to use in HTTP communications
     *          with the Yosokumo server (e.g., application/yosokumo+protobuf).
     */
    public YosokumoRequest(
        Credentials credentials,
        String      hostName,
        int         port,
        String      contentType)
    {
        this.credentials = credentials;
        this.hostName    = hostName;
        this.port        = port;
        this.contentType = contentType;
    }

    /**
     * Initialize for an HTTP operation.  Init various data fields to prepare 
     * for execution of a service request.
     */
    public void initForOperation()
    {
    	auxHeaderName  = null;
    	auxHeaderValue = null;
        statusCode     = 0;
        entity         = null;
     	exception      = null;
    }

    /**
     * Set the credentials.
     *
     * @param  credentials to use in HTTP communications to authorize the 
     *             user on the Yokosumo server.
     */
    public void setCredentials(Credentials credentials)
    {
        this.credentials = credentials;
    }

    /**
     * Set the auxiliary header.  Some HTTP requests use an auxiliary header
     * such as "x-yosokumo-full-entries: on" for Get Catalog.
     *
     * @param  name is the name of the auxiliary header, e.g., 
     *             "x-yosokumo-full-entries".
     * @param  value is the value of the auxiliary header, e.g., "on".
     */
    public void setAuxHeader(String name, String value)
    {
        auxHeaderName  = name;
        auxHeaderValue = value;
    }

    /**
     * Set the trace flag.  When trace is on, text is written to System.out 
     * showing the progress of HTTP requests and reponses.
     *
     * @param  traceOn is the value to assign to the trace flag.
     */
    public void setTrace(boolean traceOn)
    {
        this.trace = traceOn;
    }

    /**
     * Return the trace flag.
     *
     * @return  the current setting of the trace flag.
     */
    public boolean getTrace()
    {
        return trace;
    }

    /**
     * Return the status code from an HTTP response.
     *
     * @return the status code from an HTTP response.
     */
    public int getStatusCode()
    {
        return statusCode;
    }

    /**
     * Return the entity from an HTTP response.
     *
     * @return the entity from an HTTP response.
     */
    public byte [] getEntity()
    {
        return entity;
    }

    /**
     * Return the exception from an HTTP process.
     *
     * @return {@code null} means there is no exception.  Otherwise the return
     *             value is an exception from an HTTP response.
     */
    public ServiceException getException()
    {
        return exception;
    }

    /**
     * Issue an HTTP GET request.
     *
     * @param  resourceUri is the URI of the resource to get.
     * @return {@code false} means there was a problem (call 
     *             {@code getStatusCode()}, {@code getEntity()}, and
     *             {@code getException()} for more information).
     *         {@code true} means the request was successful.  Call 
     *             {@code getStatusCode()} and {@code getEntity()}
     *             to obtain the data returned from the server.
     */
    public boolean getFromServer(String resourceUri)
    {
        resourceUri = normalizeResourceUri(resourceUri, hostName, port);
    
        return makeRequest(new HttpGet(resourceUri), null, 
                                                        "getFromServer");
    }

    /**
     * Issue an HTTP POST request.
     *
     * @param  resourceUri is the URI of the resource to post to.
     * @return {@code false} means there was a problem (call 
     *             {@code getStatusCode()}, {@code getEntity()}, and
     *             {@code getException()} for more information).
     *         {@code true} means the request was successful.  Call 
     *             {@code getStatusCode()} and {@code getEntity()}
     *             to obtain the data returned from the server.
     */
    public boolean postToServer(String resourceUri, byte [] entityToPost)
    {
        resourceUri = normalizeResourceUri(resourceUri, hostName, port);
        return makeRequest(new HttpPost(resourceUri), entityToPost, 
                                                            "postToServer");
    }

    /**
     * Issue an HTTP DELETE request.
     *
     * @param  resourceUri is the URI of the resource to delete.
     * @return {@code false} means there was a problem (call 
     *             {@code getStatusCode()}, {@code getEntity()}, and
     *             {@code getException()} for more information).
     *         {@code true} means the request was successful.  Call 
     *             {@code getStatusCode()} and {@code getEntity()}
     *             for more information.
     */
    public boolean deleteFromServer(String resourceUri)
    {
        resourceUri = normalizeResourceUri(resourceUri, hostName, port);
    
        return makeRequest(new HttpDelete(resourceUri), null, 
                                                        "deleteFromServer");
    }

    /**
     * Issue an HTTP PUT request.
     *
     * @param  resourceUri is the URI where to put the resource.
     * @param  entityToPut is entity to put to the server.
     * @return {@code false} means there was a problem (call 
     *             {@code getStatusCode()}, {@code getEntity()}, and
     *             {@code getException()} for more information).
     *         {@code true} means the request was successful.  Call 
     *             {@code getStatusCode()} and {@code getEntity()}
     *             for more information.
     */
    public boolean putToServer(String resourceUri, byte [] entityToPut)
    {
        resourceUri = normalizeResourceUri(resourceUri, hostName, port);
    
        return makeRequest(new HttpPut(resourceUri), entityToPut, 
                                                            "putToServer");
    }

    /**
     * Make an HTTP request.  This is the workhorse method which does all the 
     * work of making an HTTP request and processing the response.
     *
     * @param  httpRequest is HttpGet, HttpPut, HttpPost, or HttpDelete.
     * @param  entityToSend is an entity to put to the server.
     * @param  traceName is the name of the request to be used in trace output.
     * @return {@code false} means there was a problem (call 
     *             {@code getStatusCode()}, {@code getEntity()}, and
     *             {@code getException()} for more information).
     *         {@code true} means the request was successful.  Call 
     *             {@code getStatusCode()} and {@code getEntity()}
     *             for more information.
     */
    private boolean makeRequest(
        HttpRequestBase httpRequest, 
        byte [] entityToSend,
        String traceName)
    {
        if (trace)
        {
            System.out.println(traceName+":");
            System.out.println(credentials.toString());
        }

        statusCode = 0;
        entity     = null;
        exception  = null;

        // Add headers to the request

        httpRequest.addHeader("Host",   hostName);
        httpRequest.addHeader("Date",   DateUtils.formatDate(new Date()));
        httpRequest.addHeader("Accept", contentType);

        if (auxHeaderName != null && !auxHeaderName.isEmpty())
        {
            httpRequest.addHeader(auxHeaderName, auxHeaderValue);
            auxHeaderName  = null;
            auxHeaderValue = null;
        }

        if (entityToSend != null)
        {
            httpRequest.addHeader("Content-Type", contentType);
            httpRequest.addHeader("Content-Length", 
                                    Integer.toString(entityToSend.length));
        }

        String requestDigest = makeDigest(httpRequest);
        if (requestDigest == null)
            return false;

        httpRequest.addHeader("Authorization", "yosokumo " + 
                            credentials.getUserId() + ":" + requestDigest);

        if (entityToSend != null)
        {
            // We need the Content-Length header to compute the digest, but 
            // we can't keep it as a header because the call of setEntity 
            // below causes a Content-Length header to be automatically 
            // generated, and if there are two of them, an exception is 
            // thrown.

            httpRequest.removeHeaders("Content-Length");
            ByteArrayEntity baEntity = new ByteArrayEntity(entityToSend);
            ((HttpEntityEnclosingRequestBase)httpRequest).setEntity(baEntity);
        }

        if (trace)
        {
            System.out.println("  Request:");
            System.out.println("    Request line: " + 
                                                httpRequest.getRequestLine());
            HeaderIterator it = httpRequest.headerIterator(null);
            while (it.hasNext())
                System.out.println("    " + it.next());
        }

        // Execute the request and get the response

        return getResponse(httpRequest, traceName);
        

    }   //  end makeRequest

    /**
     * Execute an HTTP request and process the response.
     *
     * @param  httpRequest is an HTTP request, ready to be executed
     * @param  traceName is the name of the request to be used in trace output.
     * @return {@code false} means there was a problem (call 
     *             {@code getStatusCode()}, {@code getEntity()}, and
     *             {@code getException()} for more information).
     *         {@code true} means the request was successful.  Call 
     *             {@code getStatusCode()} and {@code getEntity()}
     *             for more information.
     */
    private boolean getResponse(HttpRequestBase httpRequest, String traceName) 
    {
        HttpClient httpclient = new DefaultHttpClient();
    
        try
        {
            HttpResponse response = httpclient.execute(httpRequest);
    
            statusCode = response.getStatusLine().getStatusCode();
    
            HttpEntity theEntity = response.getEntity();
    
            if (theEntity != null)
            { 
                int contentLen = (int)theEntity.getContentLength();
    
                if (contentLen < 0)
                {
                    //  Content length unknown so reading content is awkward
                    final int BUFF_SIZE = 16384;
                    byte [] buffer = new byte[BUFF_SIZE];
                    int totalBytesRead = 0;
                    InputStream is = theEntity.getContent();
                    while (true)
                    {
                        if (totalBytesRead == buffer.length)
                        {
                            byte [] tempArray = new byte[4*buffer.length];
                            System.arraycopy(buffer, 0, tempArray, 0,  
                                                            totalBytesRead);
                            buffer = tempArray;
                        }
    
                        int numRead = is.read(buffer, totalBytesRead, 
                                                buffer.length-totalBytesRead);
                        if (numRead == -1)
                            break;
                        totalBytesRead += numRead;
                    }
                    if (totalBytesRead > 0)
                    {
                        entity = new byte[totalBytesRead];
                        System.arraycopy(buffer, 0, entity, 0, totalBytesRead);
                    }
                }
    
                if (contentLen >= 0)
                {
                    // Content length is known so reading content is easy
                    entity = new byte[contentLen];
                    InputStream is = theEntity.getContent();
                    int offset = 0;
                    while (contentLen > 0)
                    {
                        int numRead = is.read(entity, offset, contentLen);
                        if (numRead == -1)
                            break;
                        offset += numRead;
                        contentLen -= numRead;
                    }
                    if (contentLen > 0)
                    {
                        exception = new ServiceException("Attempt to read " + 
                            "last " + contentLen + " bytes of entity failed"); 
                        return false;
                    }
                }
            }
            if (trace)
            {
                System.out.println("  Response:");
                System.out.println("    Status line: " + 
                                                response.getStatusLine());
                HeaderIterator it = response.headerIterator(null);
                while (it.hasNext())
                    System.out.println("    " + it.next());
            }
    
        }
        catch (IOException e)
        {
            exception = new ServiceException("Fatal transport error in " + 
                                                            traceName, e);
            return false;
        }
        finally
        {
            // When the HttpClient instance is no longer needed, shut down the 
            // connection manager to ensure immediate deallocation of all 
            // system resources
    
            httpclient.getConnectionManager().shutdown();
        }

        return true;

    }   //  end getResponse


    /**
     * Normalize a resource URI.  There are several cases:
     * <ul>
     * <li>The input URI begins with {@code "http://"} but the next part of 
     *         the URI is not the host name:  Return the URI as is.
     * <li>The input URI begins with {@code "http://"}+hostname:  Return the 
     *         URI with {@code ":"+port} inserted after the host name.
     * <li>The input URI begins with the host name:  Return the URI with 
     *          {@code "http://"} prepended.
     * <li>None of the above:  Return the URI with 
     *          {@code "http://"+hostName+":"+port} prepended.
     * </ul>
     * One reason this method exists is that the HttpClient classes HttpGet, 
     * HttpPost, etc., require that the resource URIs passed to their 
     * constructors have the {@code "http://"+hostName} prefix, despite the 
     * fact that the HTTP request lines created by these classes strip the 
     * prefix and use only the resource URI.  This normalization method 
     * allows the programmer to use only the URI, or the fully-prefixed 
     * URI, whichever is more convenient.
     *
     * @param   resourceUri  the input URI to normalize.
     * @param   hostName     the host name to use.
     * @param   port         the port to use.
     * @return  a normalized version of the input URI.
     */
    private String normalizeResourceUri(
        String resourceUri, 
        String hostName,
        int port)
    {
        String scheme = "http://";

        if (resourceUri.startsWith(scheme))
        {
            String schemeHostName = scheme + hostName;
            if (!resourceUri.startsWith(schemeHostName))
                return resourceUri;
            int len = schemeHostName.length();
            return resourceUri.substring(0, len) + ":" + port + 
                   resourceUri.substring(len);
        }

        StringBuilder newUri = new StringBuilder("http://");

        if (!resourceUri.startsWith(hostName))
            newUri.append(hostName + ":" + port);

        newUri.append(resourceUri);

        return newUri.toString();
    }


    /**
     * Make a digest of an HTTP request.
     *
     * @param   request is the HTTP request to digest.
     * @return  {@code null} means there was a problem; {@code exception} 
     *              is set.  Otherwise the return value is a digest of the 
     *              input request.
     */
    private String makeDigest(HttpRequestBase request)
    {
        String requestString = makeRequestString(request);

        if (trace)
            System.out.println("    requestString: " + requestString);

        String requestDigest;

        try
        {
            requestDigest = DigestRequest.makeDigest(requestString, 
                                                     credentials.getKey());
        }
        catch (ServiceException e)
        {
            exception = e;
            requestDigest = null;
        }

        return requestDigest; 

    }   //  end makeDigest


    /**
     * Make a string from an HTTP request.  This string is used for Yosokumo
     * authentication.
     *
     * @param   r is the input HTTP request.
     * @return  is a string containing a number of fields from r. 
     */
    private String makeRequestString(HttpRequestBase r)
    {
        StringBuilder s = new StringBuilder();
        Header h;

        s.append(r.getRequestLine().getMethod());       // method
        appendHeaderValue(r, "Host", s);                // host
        s.append("+" + r.getURI().getPath());           // uri 
        appendHeaderValue(r, "Date",             s);    // date
        appendHeaderValue(r, "Content-Type",     s);    // content type
        appendHeaderValue(r, "Content-Length",   s);    // content length
        appendHeaderValue(r, "Content-Encoding", s);    // content encoding
        appendHeaderValue(r, "Content-MD5",      s);    // content MD5
        return s.toString();

    }   //  end makeRequestString

    /**
     * Append an HTTP header value.  This is a helper method for 
     * makeRequestString.
     *
     * @param   r is the input HTTP request.
     * @param   headerName is the name of the header whose value is wanted.
     * @param   s is the string to append the header value to.
     */
    private void appendHeaderValue(
        HttpRequest   r,
        String        headerName,
        StringBuilder s)
    {
        Header h;

        s.append("+");
        h = r.getFirstHeader(headerName);
        if (h != null)
            s.append(h.getValue());
    }

}   //  end YosokumoRequest

// end YosokumoRequest.java
