/*
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */

package psiprobe.controllers.sessions;

import org.apache.catalina.Context;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import psiprobe.controllers.ContextHandlerController;
import psiprobe.model.ApplicationSession;
import psiprobe.tools.ApplicationUtils;
import psiprobe.tools.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Retrieves the list of attributes for given session.
 * 
 * @author Vlad Ilyushchenko
 */
public class ListSessionAttributesController extends ContextHandlerController {

  @Override
  protected ModelAndView handleContext(String contextName, Context context,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    boolean privileged = SecurityUtils.hasAttributeValueRole(getServletContext(), request);
    boolean calcSize =
        ServletRequestUtils.getBooleanParameter(request, "size", false) && privileged;
    String sid = ServletRequestUtils.getStringParameter(request, "sid");

    ApplicationSession appSession =
        ApplicationUtils.getApplicationSession(context.getManager().findSession(sid), calcSize,
            true);

    if (appSession != null) {
      appSession.setAllowedToViewValues(privileged);
      return new ModelAndView(getViewName(), "session", appSession);
    }
    return new ModelAndView(getViewName());
  }

}
