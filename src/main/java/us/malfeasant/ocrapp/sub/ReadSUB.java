package us.malfeasant.ocrapp.sub;

import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reads in a .sub file- interesting to note, .idx file is generated from .sub, so not needed.
 * Seems to be more for convenience of reading & parsing by players- .sub contains everything .idx does.
 * Reference: https://dvd.sourceforge.net/dvdinfo/index.html
 */
public class ReadSUB {
    public ReadSUB(Path f) throws AccessDeniedException {
        if (Files.isReadable(f)) {
            
        } else {
			throw new AccessDeniedException("File " + f + " is not readable.");
        }
    }
}
