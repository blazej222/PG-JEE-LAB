package org.example.user.repository.memory;

import org.example.datastore.component.DataStore;
import org.example.user.entity.User;
import org.example.user.repository.api.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserInMemoryRepository implements UserRepository {
    /**
     * Underlying data store. In future should be replaced with database connection.
     */
    private final DataStore store;

    /**
     * @param store data store
     */
    public UserInMemoryRepository(DataStore store) {
        this.store = store;
    }

    @Override
    public Optional<User> find(UUID id) {
        return store.findAllUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return store.findAllUsers();
    }

    @Override
    public void create(User entity) {
        store.createUser(entity);
    }

    @Override
    public void delete(User entity) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void update(User entity) {
        store.updateUser(entity);
    }

    @Override
    public Optional<User> findByName(String name) {
        return store.findAllUsers().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

}
