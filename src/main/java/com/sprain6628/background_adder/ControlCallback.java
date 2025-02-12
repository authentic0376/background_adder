package com.sprain6628.background_adder;

import java.io.File;
import java.io.IOException;

public interface ControlCallback {
    File save() throws IOException;
}

