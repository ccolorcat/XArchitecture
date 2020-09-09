package x.common;

import androidx.annotation.NonNull;

import java.util.Objects;
import java.util.Random;

/**
 * Author: cxx
 * Date: 2020-09-08
 * GitHub: https://github.com/ccolorcat
 */
public class User {
    private static final Random RANDOM = new Random();

    private long id = RANDOM.nextLong();
    private String name;
    private int age = RANDOM.nextInt(1000);
    private boolean isMale = RANDOM.nextBoolean();
    private long birthday = System.currentTimeMillis();

    public User(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                age == user.age &&
                isMale == user.isMale &&
                birthday == user.birthday &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, isMale, birthday);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", isMale=" + isMale +
                ", birthday=" + birthday +
                '}';
    }
}
