/***************************************************************************
 *                            SSLSetup.java                                 *
 *                    --------------------------                           *
 *   date          : Oct 18, 2005                                               *
 *   copyright     : (C) 2005 by Bticino S.p.A. Erba (CO) - Italy          *
 *                   Embedded Software Development Laboratory              *
 *   license       : GPL                                                   *
 *   email         :                                                       *
 *   web site      : www.bticino.it; www.myhome-bticino.it                 *
 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package client_java_core.core;

/**
 * Description:
 * 
 * 
 */
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import javax.net.ssl.*;

/*
SSLSocketFactory;
import com.sun.net.ssl.SSLContext;
import com.sun.net.ssl.TrustManager;
import com.sun.net.ssl.KeyManager;
import com.sun.net.ssl.X509TrustManager;
import com.sun.net.ssl.HttpsURLConnection;
*/
/**
* Setup a JVM for SSL connections.
* <BR><BR>
* Normal usage:<BR>
* 1) call initializeForSSL() to get SSL to work against most servers<BR>
* 2) call overrideTrustManager() to get SSL to work against all
servers<BR>
* 3) call an https URL using your custom URLConnection code<BR>
*<BR>
* Call setDebug() to see SSL debug info
*/
public class SSLSetup {

public static void initializeForSSL() {
Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
System.setProperty(
"java.protocol.handler.pkgs",
"com.sun.net.ssl.internal.www.protocol");
}
public static void overrideTrustManager() {
//use our own trust manager so we can always trust
//the URL entered in the configuration.
X509TrustManager tm = new MyX509TrustManager();
KeyManager[] km = null;
X509TrustManager[] tma = new X509TrustManager[] { tm };
try {
SSLContext sslContext = SSLContext.getInstance("TLS");
//SSLv3");
sslContext.init(km, tma, new java.security.SecureRandom());
SSLSocketFactory sf1 = sslContext.getSocketFactory();
HttpsURLConnection.setDefaultSSLSocketFactory(sf1);

HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());

} catch (NoSuchAlgorithmException e) {
e.printStackTrace(System.out);
} catch (KeyManagementException e) {
e.printStackTrace(System.out);
}
}
public static void setDebug() {
System.setProperty("javax.net.debug",
"ssl,handshake,data,trustmanager");
}

}
