package com.sprain6628.background_adder.model;

import com.google.inject.Singleton;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

@Singleton
public class ExceptionModel {
    private final ObjectProperty<Throwable> leftConvertException = new SimpleObjectProperty<>();
    private final ObjectProperty<Throwable> rightConvertException = new SimpleObjectProperty<>();
    private final ObjectProperty<Throwable> addBackgroundException = new SimpleObjectProperty<>();

    public Throwable getLeftConvertException() {
        return leftConvertException.get();
    }

    public ObjectProperty<Throwable> leftConvertExceptionProperty() {
        return leftConvertException;
    }

    public void setLeftConvertException(Throwable leftConvertException) {
        this.leftConvertException.set(leftConvertException);
    }

    public Throwable getRightConvertException() {
        return rightConvertException.get();
    }

    public ObjectProperty<Throwable> rightConvertExceptionProperty() {
        return rightConvertException;
    }

    public void setRightConvertException(Throwable rightConvertException) {
        this.rightConvertException.set(rightConvertException);
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
