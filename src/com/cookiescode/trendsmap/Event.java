package com.cookiescode.trendsmap;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Created by Ahmed Ali on 10/03/2016.
 * www.cookiescode.com
 */
public class Event<T> {

    private HashMap<Object, Consumer<T>> methodsList = new HashMap<>();

    /**
     * add listener method to this event.
     * for example : MyClass::listenerMethod
     * @param method : method to invoke when user
     *               calls the event.
     * @param object : object to identify the listener.
     */
    public void addListener(Object object, Consumer<T> method){
        methodsList.put(object, method);
    }

    /**
     * remove existing listener by it's object
     * @param object
     */
    public void removeListener(Object object){
        if(methodsList.containsKey(object))
            methodsList.remove(object);
    }


    /**
     * invoke this event. this will call all
     * listeners methods.
     * @param param : data to pass to the listener method
     *            must be the same type as listener method.
     */
    public void invoke(T param){
        for(Consumer<T> consumer : methodsList.values()){
            consumer.accept(param);
        }
    }
}
