package com.nixsolutions.usik.utils;

import com.nixsolutions.usik.constants.ViewsContent;
import com.nixsolutions.usik.model.entity.User;

import javax.servlet.http.HttpServletRequest;

public class RedirectUtils {

    public static String redirectTo(
            HttpServletRequest request,
            String attribute,
            String message,
            String jspPage
    ) {
        request.setAttribute(attribute, message);
        return jspPage;
    }

}
