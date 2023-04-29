package us.malfeasant.ocrapp;

import java.time.Duration;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.WritableImage;

/**
 * Encompasses a given subtitle- its image and start/stop times, a bounding box, and possibly text
 */
public class SubPicture {
    private Duration showTime;  // timestamp when this image begins to display
    private Duration hideTime;  // timestamp to stop display
    private Rectangle2D totalBounds;    // size of the full source image
    private Rectangle2D croppedBounds;  // size of just the non-transparent part of the image
    private WritableImage image;
    private String text;
}
