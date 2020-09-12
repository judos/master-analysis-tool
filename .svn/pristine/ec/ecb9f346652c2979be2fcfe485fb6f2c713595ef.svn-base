
package model;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javares.data.xml.XmlJS;
import javares.files.FileUtils;
import javares.graphics.ImageJS;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;


/**
 * @since 10.02.2014
 * @author Julian Schelker
 */
public class Minimap {

    private double endX;
    private double endY;
    private Image image;
    private String name;
    private double startX;

    private double startY;
    private boolean loading;
    private File mapFolder;
    private Object loadingImage;

    public Minimap(final File map) throws IOException {
        this.name = map.getName();
        this.image = null;
        this.loadingImage = new Object();
        this.mapFolder = map;
        initXmlData(new File(map, "0.xml"));
    }

    private double attrGetDoubleValue(NamedNodeMap attr, String name) {
        return Double.valueOf(attr.getNamedItem(name).getNodeValue());
    }

    /**
     * @return the endX
     */
    public double getEndX() {
        return endX;
    }

    /**
     * @return the endY
     */
    public double getEndY() {
        return endY;
    }

    /**
     * @return the image
     */
    public Image getImage() {
        if (image == null) {
            startLoadingImage();
        }
        return image;
    }

    /**
     * @return the image
     */
    public Image getImageSynchronous() {
        if (image == null) {
            startLoadingImage();
            synchronized (this.loadingImage) {
                try {
                    this.loadingImage.wait();
                } catch (InterruptedException e) {}
            }
        }
        return image;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the startX
     */
    public double getStartX() {
        return startX;
    }

    /**
     * @return the startY
     */
    public double getStartY() {
        return startY;
    }

    private void initXmlData(File file) throws IOException {
        String xml = FileUtils.readAllFile(file.getPath());
        Document xmlDoc = XmlJS.text2Document(xml);
        NodeList map = xmlDoc.getDocumentElement().getElementsByTagName("MiniMap");
        NamedNodeMap attr = map.item(0).getAttributes();
        this.startX = attrGetDoubleValue(attr, "startX");
        this.endX = attrGetDoubleValue(attr, "endX");
        this.startY = attrGetDoubleValue(attr, "startY");
        this.endY = attrGetDoubleValue(attr, "endY");
    }

    private synchronized void startLoadingImage() {
        if (this.loading)
            return;
        this.loading = true;
        Thread t = new Thread() {

            @Override
            public void run() {
                for (File candidate : mapFolder.listFiles()) {
                    image = ImageJS.loadBufferedImage(candidate);
                    if (image != null) {
                        synchronized (loadingImage) {
                            loadingImage.notifyAll();
                        }
                        break;
                    }
                }

            }
        };
        t.start();
    }

    @Override
    public String toString() {
        return this.name;
    }

    public void waitUntilImageLoaded() {
        getImageSynchronous();
    }
}
