package com.nixsolutions.usik.controller;

import com.nixsolutions.usik.model.entity.User;
import com.nixsolutions.usik.model.service.hibernate.HibernateUserDao;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class UserRestControllerTest {

    @Mock
    private PasswordEncoder encoder;
    @Mock
    private HibernateUserDao dao;
    private User user;
    private String jsonUser;
    @InjectMocks
    private UserRestController controller;


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

        jsonUser = "{" +
                "\"id\":1," +
                "\"login\":\"test\"," +
                "\"password\":\"test\"," +
                "\"email\":\"test\"," +
                "\"firstName\":\"test\"," +
                "\"lastName\":\"test\"," +
                "\"birthday\":971049600000," +
                "\"roleId\":2" +
                "}";
    }

    @Test
    public void testCreate() throws Exception {
        when(dao.findByLogin(user.getLogin())).thenReturn(null);
        when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());

        mockMvc
                .perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isCreated());

        verify(dao).findByLogin(user.getLogin());
        verify(encoder).encode(any());
    }

    @Test
    public void testReadAll() throws Exception {
        when(dao.findAll()).thenReturn(Collections.singletonList(user));

        mockMvc
                .perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().string('[' + jsonUser + ']'));

        verify(dao).findAll();
    }

    @Test
    public void testRead() throws Exception {
        when(dao.findByLogin(user.getLogin())).thenReturn(user);

        mockMvc
                .perform(get("/user/" + user.getLogin()))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonUser));

        verify(dao).findByLogin(any());
    }

    @Test
    public void testEdit() throws Exception {
        when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());

        mockMvc
                .perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk());

        verify(encoder).encode(any());
    }

    @Test
    public void testDelete() throws Exception {
        when(dao.findByLogin(user.getLogin())).thenReturn(user);
        mockMvc
                .perform(delete("/user/" + user.getLogin()))
                .andExpect(status().isOk());
        verify(dao).findByLogin(user.getLogin());
    }


    @Test
    public void testCreateError() throws Exception {
        when(dao.findByLogin(user.getLogin())).thenReturn(user);

        mockMvc
                .perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isNotModified());

        verify(dao).findByLogin(user.getLogin());
    }

    @Test
    public void testReadAllError() throws Exception {
        when(dao.findAll()).thenReturn(null);

        mockMvc
                .perform(get("/user"))
                .andExpect(status().isNotFound());

        verify(dao).findAll();
    }

    @Test
    public void testReadError() throws Exception {
        when(dao.findByLogin(user.getLogin())).thenReturn(null);

        mockMvc
                .perform(get("/user/" + user.getLogin()))
                .andExpect(status().isNotFound());

        verify(dao).findByLogin(any());
    }

    @Test
    public void testEditError() throws Exception {
        when(encoder.encode(user.getPassword())).thenThrow(new HibernateException(""));

        mockMvc
                .perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isNotModified());

        verify(encoder).encode(any());
    }

}