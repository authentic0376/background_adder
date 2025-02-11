package com.sprain6628.background_adder.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import com.google.inject.Singleton;

@Singleton
public class ExceptionModel {
    private final ObjectProperty<Throwable> convertException = new SimpleObjectProperty<>();
    private final ObjectProperty<Throwable> addBackgroundException = new SimpleObjectProperty<>();

    public Throwable getConvertException() {
        return convertException.get();
    }

    public ObjectProperty<Throwable> convertExceptionProperty() {
        return convertException;
    }

    public void setConvertException(Throwable convertException) {
        this.convertException.set(convertException);
    }

    public Throwable getAddBackgroundException() {
        return addBackgroundException.get();
    }

    public ObjectProperty<Throwable> addBackgroundExceptionProperty() {
        return addBackgroundException;
    }

    public void setAddBackgroundException(Throwable addBackgroundException) {
        this.addBackgroundException.set(addBackgroundException);
    }
}
