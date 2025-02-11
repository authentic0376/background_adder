package com.sprain6628.background_adder.config;

import com.google.inject.AbstractModule;
import com.sprain6628.background_adder.ControlCallback;
import com.sprain6628.background_adder.Controller;

public class CustomModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ControlCallback.class).to(Controller.class);
    }
}
