package org.example.user.view;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.component.ModelFunctionFactory;
import org.example.user.model.UsersModel;
import org.example.user.services.UserService;

@RequestScoped
@Named
public class UserList {
    /**
     * Service for managing users.
     */
    private final UserService service;

    /**
     * Characters list exposed to the view.
     */
    private UsersModel users;

    /**
     * Factory producing functions for conversion between models and entities.
     */
    private final ModelFunctionFactory factory;

    /**
     * @param service user service
     * @param factory factory producing functions for conversion between models and entities
     */
    @Inject
    public UserList(UserService service, ModelFunctionFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    /**
     * In order to prevent calling service on different steps of JSF request lifecycle, model property is cached using
     * lazy getter.
     *
     * @return all users
     */
    public UsersModel getUsers() {
        if (users == null) {
            users = factory.usersToModel().apply(service.findAll());
        }
        return users;
    }

    /**
     * Action for clicking delete action.
     *
     * @param user user to be removed
     * @return navigation case to list_users
     */
    public String deleteAction(UsersModel.User user) {
        service.delete(user.getId());
        return "user_list?faces-redirect=true";
    }

}
