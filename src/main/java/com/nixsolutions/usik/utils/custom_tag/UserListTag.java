package com.nixsolutions.usik.utils.custom_tag;

import com.nixsolutions.usik.model.entity.User;
import com.nixsolutions.usik.model.repository.UserDao;
import com.nixsolutions.usik.model.service.hibernate.HibernateUserDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class UserListTag extends SimpleTagSupport {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public UserListTag() {

    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public void doTag() throws IOException {
        UserDao dao = new HibernateUserDao();
        JspWriter out = getJspContext().getOut();

        List<User> all = null;

        try {
            all = dao.findAll();
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/WEB-INF/views/access_denied.jsp");
            try {
                dispatcher.forward(request, response);
            } catch (ServletException servletException) {
                response.sendRedirect(request.getContextPath() + "/");
            }
        }

        if (all != null) {

            out.println("<table>" +
                    "   <tr>" +
                    "       <th>Login</th>" +
                    "       <th>First Name</th>" +
                    "       <th>Last Name</th>" +
                    "       <th>Age</th>" +
                    "       <th>Role</th>" +
                    "       <th>Actions</th>" +
                    "   </tr>");

            String ref = "<a href='" + request.getContextPath() + "/";

            for (User user : all) {
                int age = (Calendar.getInstance().get(Calendar.YEAR) -
                        user.getBirthday().toLocalDate().getYear());
                String role = user.getRoleId() == 1 ? "admin" : "user";
                out.println("<tr>" +
                        "   <td>" + user.getLogin() + "</td>" +
                        "   <td>" + user.getFirstName() + "</td>" +
                        "   <td>" + user.getLastName() + "</td>" +
                        "   <td>" + age + "</td>" +
                        "   <td>" + role + "</td>" +
                        "   <td>" +
                        "   " + ref + "editUser?login=" + user.getLogin() + "'>Edit</a>" +
                        "   " + ref + "deleteUser?login=" + user.getLogin() +
                        "' onclick=\"return confirm('Are you sure?')\">Delete</a>" + "</td>" +
                        "</tr>"
                );
            }

            out.print("</table>");
        }
    }
}
