package com.swygbro.housemate.util.condition;

public interface Condition<T> {

    Boolean isSatisfiedBy(T t);

}
