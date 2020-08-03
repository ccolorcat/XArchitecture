package cc.colorcat.xarchitecture.sample;

import x.common.component.annotation.StoreModel;
import x.common.component.store.Clear;
import x.common.component.store.Read;
import x.common.component.store.Remove;
import x.common.component.store.Write;

/**
 * Author: cxx
 * Date: 2020-08-03
 * GitHub: https://github.com/ccolorcat
 */
@StoreModel(name = "person.store")
public interface PersonStore {
    String KEY = "person";

    void save(@Write(KEY) Person person);

    @Read(KEY)
    Person read();

    @Remove(KEY)
    boolean remove();

    @Clear()
    boolean clear();
}
