module com.sprain6628.background_adder {

    requires java.logging;

    // javafx.embeded.swing.SwingFXUtils / requires java.desktop (package javax.imageio), javafx.graphics(Border, Background)
    requires javafx.swing;
    requires javafx.controls; // javafx.scene.controll.Button... GUI Component / requires javafx.base

    requires java.xml; // javax.xml.transform. Transformer, TransformerFactory, dom.DOMSource
    requires jdk.xml.dom; // org.w3c.dom.css.DOMImplementationCSS

    requires batik.anim; // SAXSVGDocumentFactory
    requires batik.transcoder; // PNG Transcoder

    exports com.sprain6628.background_adder;
}