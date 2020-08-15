package com.nixsolutions.usik.controller;

import com.nixsolutions.usik.constants.ViewsContent;
import com.nixsolutions.usik.model.entity.User;

import com.nixsolutions.usik.model.service.hibernate.HibernateUserDao;
import com.nixsolutions.usik.utils.HibernateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private PasswordEncoder encoder;
    @Mock
    private HibernateUserDao dao;
    private User user;
    @InjectMocks
    private UserController controller;


    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        user = new User(
                1,
                "test",
                "test",
                "test",
                "test",
                "test",
                Date.valueOf("2000-10-09"),
                2
        );

    }

    @Test
    public void testRegistration() throws Exception {
        when(dao.findByLogin(user.getLogin())).thenReturn(null);
        when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());

        mockMvc.perform(
                post("/registration")
                        .flashAttr("user", user)
                        .param("confirmPassword", user.getPassword())
        )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name(ViewsContent.REDIRECT));

        verify(dao).findByLogin(user.getLogin());
        verify(encoder).encode(any());
    }

    @Test
    public void testEditUser() throws Exception {
        when(dao.findByLogin(user.getLogin())).thenReturn(user);
        when(dao.findByEmail(user.getEmail())).thenReturn(null);
        when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());

        User newUser = this.user;
        newUser.setPassword("qqqq");

        mockMvc.perform(
                post("/editUser")
                        .flashAttr("user", newUser)
                        .param("confirmPassword", newUser.getPassword())
        )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name(ViewsContent.REDIRECT));

        verify(dao).findByLogin(user.getLogin());
        verify(dao).findByEmail(user.getEmail());
        verify(encoder).encode(any());
    }


    @Test
    public void testDeleteUser() throws Exception {
        when(dao.findByLogin(user.getLogin())).thenReturn(user);
        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                new org.springframework.security.core.userdetails.User(
                                        user.getLogin(),
                                        user.getPassword(),
                                        Collections.singleton(new SimpleGrantedAuthority("USER"))
                                ), null)
                );

        mockMvc.perform(
                get("/deleteUser")
                        .param("login", user.getLogin())
        )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name(ViewsContent.REDIRECT));

        verify(dao).findByLogin(user.getLogin());
    }

    @Test
    public void testAddUserPage() throws Exception {
        mockMvc.perform(get("/addUser"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(ViewsContent.JSP_ADD_USER))
                .andExpect(model().attribute("user", new User()));
    }

    @Test
    public void testEditUserPageWithoutParameters() throws Exception {
        mockMvc.perform(get("/editUser"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testDeleteUsersWithoutParameters() throws Exception {
        mockMvc.perform(get("/deleteUser"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUser() throws Exception {
        when(dao.findByLogin(user.getLogin())).thenReturn(null);
        when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());

        mockMvc
                .perform(
                        post("/addUser")
                                .flashAttr("user", user)
                                .param("confirmPassword", user.getPassword()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name(ViewsContent.REDIRECT));

        verify(dao).findByLogin(user.getLogin());
        verify(encoder).encode(any());
    }
}