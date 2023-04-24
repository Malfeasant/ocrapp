package us.malfeasant.ocrapp.sub;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Reads in a .sub file- interesting to note, .idx file is generated from .sub, so not needed.
 * Seems to be more for convenience of reading & parsing by players- .sub contains everything .idx does.
 * Reference: https://dvd.sourceforge.net/dvdinfo/index.html
 */
public class ReadSUB {
    public ReadSUB(Path f) throws AccessDeniedException {
        if (Files.isReadable(f)) {
            try {
                var fc = FileChannel.open(f, StandardOpenOption.READ);
                ByteBuffer packHeader = ByteBuffer.allocate(0);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
			throw new AccessDeniedException("File " + f + " is not readable.");
        }
    }
}
