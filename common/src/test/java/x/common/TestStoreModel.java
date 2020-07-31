package x.common;

import java.util.List;

import x.common.component.annotation.StoreModel;
import x.common.component.store.Read;
import x.common.component.store.Write;

/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
@StoreModel(name = "test_model")
public interface TestStoreModel {
    String PERSONS = "persons";

    boolean savePersons(@Write(PERSONS) List<Person> persons);

    @Read(PERSONS)
    List<Person> loadPersons();
}
