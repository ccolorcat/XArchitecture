package x.common;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
public class Child extends Person {
    public Child() {
    }

    public Child(String name, int age, boolean isMale) {
        super(name, age, isMale);
    }

    @NonNull
    @Override
    public String toString() {
        return "Child{} " + super.toString();
    }
}
