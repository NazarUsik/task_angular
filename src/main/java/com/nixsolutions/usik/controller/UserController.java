package com.nixsolutions.usik.controller;

import com.nixsolutions.usik.constants.ViewsContent;
import com.nixsolutions.usik.model.entity.User;
import com.nixsolutions.usik.model.service.hibernate.HibernateUserDao;
import com.nixsolutions.usik.utils.CaptchaUtils;
import com.nixsolutions.usik.utils.RedirectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    private HibernateUserDao service;

    @Autowired
    private PasswordEncoder encoder;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage() {
        return new ModelAndView(ViewsContent.JSP_LOGIN);
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registrationPage() {
        ModelAndView mav = new ModelAndView(ViewsContent.JSP_REGISTRATION);
        mav.addObject("user", new User());
        return mav;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registration(
            HttpServletRequest request,
            @ModelAttribute("user") User user,
            @RequestParam String confirmPassword
    ) {
        return new ModelAndView(
                newUser(
                        request,
                        user,
                        confirmPassword,
                        ViewsContent.JSP_REGISTRATION
                )
        );
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.GET)
    public ModelAndView createUserPage() {
        ModelAndView mav = new ModelAndView(ViewsContent.JSP_ADD_USER);
        mav.addObject("user", new User());
        return mav;
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public ModelAndView createUser(
            HttpServletRequest request,
            @ModelAttribute("user") User user,
            @RequestParam String confirmPassword
    ) {
        return new ModelAndView(
                newUser(
                        request,
                        user,
                        confirmPassword,
                        ViewsContent.JSP_ADD_USER
                )
        );
    }

    @RequestMapping(value = "/editUser", method = RequestMethod.GET)
    public ModelAndView updateUserPage(
            HttpServletRequest request,
            @RequestParam String login
    ) {
        ModelAndView mav = new ModelAndView(ViewsContent.REDIRECT);

        User user = null;

        try {
            user = service.findByLogin(login);
        } catch (Exception e) {
            mav.setViewName(RedirectUtils.redirectTo(
                    request,
                    ViewsContent.ERROR,
                    e.getMessage(),
                    ViewsContent.JSP_ACCESS_DENIED
            ));
        }

        if (user != null) {
            user.setPassword("");
            mav.addObject("user", new User());
            request.setAttribute(
                    "us",
                    user
            );
            mav.setViewName(ViewsContent.JSP_EDIT_USER);
        }

        return mav;
    }

    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    public ModelAndView updateUser(
            HttpServletRequest request,
            @ModelAttribute("user") User user,
            @RequestParam String confirmPassword
    ) {
        String captcha = checkCaptcha(request, user, ViewsContent.JSP_EDIT_USER);
        if (captcha != null) {
            return new ModelAndView(captcha);
        }

        User oldUser;

        try {
            oldUser = service.findByLogin(user.getLogin());
        } catch (Exception e) {
            return new ModelAndView(RedirectUtils.redirectTo(
                    request,
                    ViewsContent.ERROR,
                    e.getMessage(),
                    ViewsContent.JSP_ACCESS_DENIED
            ));
        }

        String result;
        if ((result = checkUserPassword(
                request,
                user,
                confirmPassword,
                ViewsContent.JSP_EDIT_USER)) != null) {
            return new ModelAndView(result);
        }

        User byEmail;
        try {
            byEmail = service.findByEmail(user.getEmail());
        } catch (Exception e) {
            return new ModelAndView(RedirectUtils.redirectTo(
                    request,
                    ViewsContent.ERROR,
                    e.getMessage(),
                    ViewsContent.JSP_ACCESS_DENIED
            ));
        }

        if (byEmail != null &&
                !user.getEmail().equals(oldUser.getEmail())) {
            request.setAttribute("us", user);
            request.setAttribute("input", "email");

            return new ModelAndView(
                    RedirectUtils.redirectTo(
                            request,
                            ViewsContent.ERROR,
                            "Email already exist: " + user.getEmail(),
                            ViewsContent.JSP_EDIT_USER
                    )
            );
        }

        user.setId(oldUser.getId());
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            service.update(user);
        } catch (Exception e) {
            return new ModelAndView(RedirectUtils.redirectTo(
                    request,
                    ViewsContent.ERROR,
                    e.getMessage(),
                    ViewsContent.JSP_ACCESS_DENIED
            ));
        }
        return new ModelAndView(ViewsContent.REDIRECT);
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public ModelAndView deleteUser(
            HttpServletRequest request,
            @RequestParam String login
    ) {

        User user;

        try {
            user = service.findByLogin(login);
        } catch (Exception e) {
            return new ModelAndView(RedirectUtils.redirectTo(
                    request,
                    ViewsContent.ERROR,
                    e.getMessage(),
                    ViewsContent.JSP_ACCESS_DENIED
            ));
        }

        if (user != null) {
            try {
                service.remove(user);
            } catch (Exception e) {
                return new ModelAndView(RedirectUtils.redirectTo(
                        request,
                        ViewsContent.ERROR,
                        e.getMessage(),
                        ViewsContent.JSP_ACCESS_DENIED
                ));
            }

            UserDetails userDetails =
                    (UserDetails) SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getPrincipal();

            if (login.equals(userDetails.getUsername())) {
                request.getSession().invalidate();
            }

        }

        return new ModelAndView(ViewsContent.REDIRECT);
    }

    private String newUser(
            HttpServletRequest request,
            User user,
            String confirmPassword,
            String jsp
    ) {
        String captcha = checkCaptcha(request, user, jsp);
        if (captcha != null) {
            return captcha;
        }

        String result;
        if ((result = checkUserPassword(
                request,
                user,
                confirmPassword,
                jsp
        )) != null) {
            return result;
        }

        User byLogin;

        try {
            byLogin = service.findByLogin(user.getLogin());
        } catch (Exception e) {
            return RedirectUtils.redirectTo(
                    request,
                    ViewsContent.ERROR,
                    e.getMessage(),
                    ViewsContent.JSP_ACCESS_DENIED
            );
        }


        if (byLogin != null) {
            request.setAttribute("us", user);
            request.setAttribute("input", "login");

            return RedirectUtils.redirectTo(
                    request,
                    ViewsContent.ERROR,
                    "Login already exist: " + user.getLogin(),
                    jsp);
        }

        User byEmail;
        try {
            byEmail = service.findByEmail(user.getEmail());
        } catch (Exception e) {
            return RedirectUtils.redirectTo(
                    request,
                    ViewsContent.ERROR,
                    e.getMessage(),
                    ViewsContent.JSP_ACCESS_DENIED
            );
        }
        if (byEmail != null) {
            request.setAttribute("us", user);
            request.setAttribute("input", "email");

            return RedirectUtils.redirectTo(
                    request,
                    ViewsContent.ERROR,
                    "Email already exist: " + user.getEmail(),
                    jsp);
        }

        user.setPassword(encoder.encode(user.getPassword()));
        try {
            service.create(user);
        } catch (Exception e) {
            return RedirectUtils.redirectTo(
                    request,
                    ViewsContent.ERROR,
                    e.getMessage(),
                    ViewsContent.JSP_ACCESS_DENIED
            );
        }
        return ViewsContent.REDIRECT;
    }


    private String checkUserPassword(
            HttpServletRequest request,
            User user,
            String confirmPassword,
            String redirectTo
    ) {
        if (!user.getPassword().equals(confirmPassword)) {
            request.setAttribute("us", user);
            request.setAttribute("input", "password");

            return RedirectUtils.redirectTo(
                    request,
                    ViewsContent.ERROR,
                    "Password mismatch!",
                    redirectTo);
        }

        return null;
    }

    private String checkCaptcha(
            HttpServletRequest request,
            User user,
            String jsp
    ) {
        String gRecaptchaResponse =
                request.getParameter("g-recaptcha-response");

        if (gRecaptchaResponse == null) {
            return null;
        }

        if (!CaptchaUtils.verify(gRecaptchaResponse)) {
            request.setAttribute("us", user);

            return RedirectUtils.redirectTo(
                    request,
                    ViewsContent.ERROR,
                    "Captcha invalid!",
                    jsp);
        }

        return null;
    }
}
