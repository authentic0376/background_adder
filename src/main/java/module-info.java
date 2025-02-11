module com.sprain6628.background_adder {

    requires java.logging;

    // javafx.embeded.swing.SwingFXUtils / requires java.desktop (package javax.imageio), javafx.graphics(Border, Background)
    requires javafx.swing;
    requires javafx.controls; // javafx.scene.controll.Button... GUI Component / requires javafx.base

    requires java.xml; // javax.xml.transform. Transformer, TransformerFactory, dom.DOMSource
    requires jdk.xml.dom; // org.w3c.dom.css.DOMImplementationCSS

    requires batik.anim; // SAXSVGDocumentFactory
    requires batik.transcoder; // PNG Transcoder
    requires com.google.guice;
    requires jakarta.inject;

    exports com.sprain6628.background_adder;
    exports com.sprain6628.background_adder.config;
    exports com.sprain6628.background_adder.model;
    exports com.sprain6628.background_adder.service;
}