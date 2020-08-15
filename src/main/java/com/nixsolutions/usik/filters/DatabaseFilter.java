package com.nixsolutions.usik.filters;

import com.nixsolutions.usik.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

import static com.nixsolutions.usik.Main.readQuery;

@WebFilter(filterName = "databaseFilter", urlPatterns = "/*")
public class DatabaseFilter implements Filter {

    private static boolean exist;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        if (!exist) {
            String startQuery = readQuery(
                    "https://raw.githubusercontent.com/" +
                            "NazarUsik/OnlineShop/master/sql/insert.sql");

            Session session = HibernateUtils.getSession();
            NativeQuery sqlQuery = session.createSQLQuery(startQuery);
            sqlQuery.executeUpdate();
            session.getTransaction().commit();
            session.close();

            exist = true;
        }


        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
