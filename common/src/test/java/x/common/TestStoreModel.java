package x.common;

import java.util.List;

import x.common.component.annotation.StoreModel;
import x.common.component.store.Clear;
import x.common.component.store.Read;
import x.common.component.store.Remove;
import x.common.component.store.Write;

/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
@StoreModel(name = "test_model")
public interface TestStoreModel {
    String PERSONS = "persons";
    String USER = "user";

    boolean savePersons(@Write(PERSONS) List<Person> persons);

    @Read(PERSONS)
    List<Person> loadPersons();

    @Remove(PERSONS)
    boolean removePersons();

    @Clear
    boolean clear();


    void saveUser(@Write(USER) User user);

    @Read(USER)
    User readUser();

    @Remove(USER)
    boolean removeUser();
}
