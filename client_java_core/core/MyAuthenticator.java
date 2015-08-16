/***************************************************************************
 *                            MyAuthenticator.java                         *
 *                    --------------------------                           *
 *   date          : Oct 18, 2005                                          *
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
import java.net.*;

public class MyAuthenticator extends Authenticator {
protected PasswordAuthentication getPasswordAuthentication() {
char [] passwd = new char["MM".length()];
for (int i = 0; i < "MM".length(); i++) {
passwd[i] = "MM".charAt(i);
}
return new PasswordAuthentication ("MM", passwd);
}
}
