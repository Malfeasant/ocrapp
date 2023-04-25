package us.malfeasant.ocrapp.sub;

import java.nio.channels.FileChannel;

import us.malfeasant.ocrapp.ReadFile;

/**
 * Reads in a .sub file- interesting to note, .idx file is generated from .sub, so not needed.
 * Seems to be more for convenience of reading & parsing by players- .sub contains everything .idx does.
 * Reference: https://dvd.sourceforge.net/dvdinfo/index.html
 */
public class ReadSUB extends ReadFile {
    public ReadSUB(FileChannel fc) {
        super(fc);
    }
}
