/***************************************************************************
 *                            MyX509TrustManager.java                                 *
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
import java.security.cert.X509Certificate;
import javax.net.ssl.*;

/*
import com.sun.net.ssl.X509TrustManager;
*/
/**
* A trust manager which trusts a client and server certificates.
* Used by SSLSetup class.
*/
public class MyX509TrustManager implements X509TrustManager {
public X509Certificate[] getAcceptedIssuers() {
return null;
}
public boolean isClientTrusted(X509Certificate[] chain) {
return true;
}
public boolean isServerTrusted(X509Certificate[] chain) {
return true;
}
public void checkServerTrusted(X509Certificate[] chain, String authType) {}
public void checkClientTrusted(X509Certificate[] chain, String authType) {}

}
