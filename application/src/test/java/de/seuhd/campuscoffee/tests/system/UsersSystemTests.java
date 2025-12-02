package de.seuhd.campuscoffee.tests.system;
import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.tests.TestFixtures;
import org.junit.jupiter.api.Test;

import static de.seuhd.campuscoffee.tests.SystemTestUtils.Requests.userRequests;


import java.util.List;

public class UsersSystemTests extends AbstractSysTest {
   @Test
   void createUser() {
       User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
       User createdUser = userDtoMapper
               .toDomain(userRequests.create(List.of(userDtoMapper.fromDomain(userToCreate))).getFirst());

       assertEqualsIgnoringIdAndTimestamps(createdUser, userToCreate);
   }
    
    @Test
    void getAllCreatedUsers() {
        List<User> createdUserList = TestFixtures.createUsers(userService);

        List<User> retrievedUsers = userRequests.retrieveAll()
                .stream()
                .map(userDtoMapper::toDomain)
                .toList();
        assertEqualsIgnoringTimestamps(retrievedUsers, createdUserList);
    }

    @Test
    void getUserById() {
        List<User> createdUserList = TestFixtures.createUsers(userService);
        User createdUser = createdUserList.getFirst();

        User retrievedUser = userDtoMapper.toDomain(
                userRequests.retrieveById(createdUser.id())
        );

        assertEqualsIgnoringTimestamps(retrievedUser, createdUser);
    }
}