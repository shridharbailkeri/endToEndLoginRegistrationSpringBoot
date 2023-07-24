package com.dailycodework.sbend2endapplication.utility;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtil {

    //Overall, this method aims to retrieve the application URL by removing the servlet path from the full URL of the request
    //
    public static String getApplicationUrl(HttpServletRequest request) {
        String appUrl = request.getRequestURL().toString();
        //This line retrieves the full URL of the current request using the getRequestURL() method of the HttpServletRequest object
        // The getRequestURL() method returns a StringBuffer object representing the URL.
        //The toString() method is called on the StringBuffer object to convert it to a String representation and assigns it to the appUrl variable.
        // This line replaces the servlet path in the appUrl string with an empty string.
        // The servlet path is the part of the URL that corresponds to the mapping of the servlet or controller handling the request
        // Full URL: http://example.com/myapp/servlets/myservlet
        //
        //In this example, let's assume that:
        //
        //http://example.com is the domain and root of the website.
        ///myapp is the context path of the application.
        ///servlets is the servlet mapping.
        //myservlet is the servlet handling the request.
        //The servlet path, in this case, would be /servlets/myservlet.
        return appUrl.replace(request.getServletPath(), "");
    }
}
