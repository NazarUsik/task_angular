package com.nixsolutions.usik.model.repository;

import com.nixsolutions.usik.model.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao {

    void create(Role role);

    void update(Role role);

    void remove(Role role);

    Role findByName(String name);

}
