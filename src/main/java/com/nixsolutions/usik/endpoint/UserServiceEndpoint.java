package com.nixsolutions.usik.endpoint;

import com.nixsolutions.usik.exception.UserServiceException;
import com.nixsolutions.usik.model.entity.User;
import com.nixsolutions.usik.service.UserServiceImpl;
import com.nixsolutions.usik.ws.CreateRequest;
import com.nixsolutions.usik.ws.CreateResponse;
import com.nixsolutions.usik.ws.ReadAllRequest;
import com.nixsolutions.usik.ws.ReadAllResponse;
import com.nixsolutions.usik.ws.ReadRequest;
import com.nixsolutions.usik.ws.ReadResponse;
import com.nixsolutions.usik.ws.UpdateRequest;
import com.nixsolutions.usik.ws.UpdateResponse;
import com.nixsolutions.usik.ws.DeleteRequest;
import com.nixsolutions.usik.ws.DeleteResponse;
import com.nixsolutions.usik.ws.ObjectFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Endpoint
public class UserServiceEndpoint {

    private static final Logger logger =
            LogManager.getLogger(UserServiceEndpoint.class.getName());

    private static final String NAMESPACE_URI =
            "http://ws.usik.nixsolutions.com/";

    private final UserServiceImpl service;
    private final ObjectFactory factory;

    @Autowired
    public UserServiceEndpoint(UserServiceImpl service) {
        this.service = service;
        this.factory = new ObjectFactory();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateRequest")
    @ResponsePayload
    public CreateResponse create(@RequestPayload CreateRequest request) {
        com.nixsolutions.usik.ws.User requestUser = request.getUser();
        logger.info("Create request, user: {}", requestUser.getLogin());

        CreateResponse response = factory.createCreateResponse();

        try {
            if (service.create(convertWsUserToUser(requestUser))) {
                response.setResponseMessage("HttpStatus: " + HttpStatus.OK.toString());
            }
        } catch (UserServiceException e) {
            response.setResponseMessage(e.getMessage());
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ReadAllRequest")
    @ResponsePayload
    public ReadAllResponse readAll(@RequestPayload ReadAllRequest request) throws UserServiceException {
        logger.info("ReadAll request, info: {}", request.getInfo());

        ReadAllResponse response = factory.createReadAllResponse();


        List<com.nixsolutions.usik.ws.User> list = response.getUsers();
        service.readAll().forEach(user -> list.add(convertUserToWsUser(user)));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ReadRequest")
    @ResponsePayload
    public ReadResponse read(@RequestPayload ReadRequest request) throws UserServiceException {
        logger.info("Read request, login: {}", request.getLogin());

        ReadResponse response = factory.createReadResponse();
        response.setUser(convertUserToWsUser(service.read(request.getLogin())));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateRequest")
    @ResponsePayload
    public UpdateResponse update(@RequestPayload UpdateRequest request) {
        com.nixsolutions.usik.ws.User requestUser = request.getUser();
        logger.info("Update request, user: {}", requestUser.getLogin());

        UpdateResponse response = factory.createUpdateResponse();

        try {
            if (service.update(convertWsUserToUser(requestUser))) {
                response.setResponseMessage("HttpStatus: " + HttpStatus.OK.toString());
            }
        } catch (UserServiceException e) {
            response.setResponseMessage(e.getMessage());
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteRequest")
    @ResponsePayload
    public DeleteResponse delete(@RequestPayload DeleteRequest request) {
        logger.info("Delete request, login: {}", request.getLogin());

        DeleteResponse response = factory.createDeleteResponse();

        try {
            if (service.delete(request.getLogin())) {
                response.setResponseMessage("HttpStatus: " + HttpStatus.OK.toString());
            }
        } catch (UserServiceException e) {
            response.setResponseMessage(e.getMessage());
        }

        return response;
    }


    private com.nixsolutions.usik.ws.User convertUserToWsUser(User user) {
        com.nixsolutions.usik.ws.User newUser = factory.createUser();

        newUser.setId(user.getId());
        newUser.setLogin(user.getLogin());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setRoleId(user.getRoleId());

        try {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(user.getBirthday());
            newUser
                    .setBirthday(
                            DatatypeFactory
                                    .newInstance()
                                    .newXMLGregorianCalendar(c));
        } catch (DatatypeConfigurationException e) {
            logger.error(e.getMessage(), e);
        }

        return newUser;
    }

    private User convertWsUserToUser(com.nixsolutions.usik.ws.User user) {
        return new User(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                Date.valueOf(
                        user
                                .getBirthday()
                                .toGregorianCalendar()
                                .toZonedDateTime()
                                .toLocalDate()
                ),
                user.getRoleId()
        );
    }

}
