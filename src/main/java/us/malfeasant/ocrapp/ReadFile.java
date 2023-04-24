package us.malfeasant.ocrapp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.tinylog.Logger;

/**
 * Parent class of subtitle file readers- reads a file whose type is potentially
 * unknown- static factory method reads first few bytes of file, determines its
 * type, and constructs the appropriate subclass (or barfs).
 */
public abstract class ReadFile {
    protected final FileChannel channel;
    /**
     * Sole constructor- not meant to be used directly-
     * instead use static method 
     * @param filePath
     */
    protected ReadFile(FileChannel fc) {
        channel = fc;
    }

    public static ReadFile getReaderFor(Path filePath) throws IOException {
        // Easiest first- if extension is .idx, substitute .sub & recurse
        if (filePath.toString().endsWith(".idx")) {
            var newPath = Path.of(filePath.toString().replaceAll("\\.idx$", ".sub"));
            Logger.info("Got .idx file- looking for corresponding .sub file {}", newPath);
            return getReaderFor(newPath);
        }
        if (Files.isRegularFile(filePath) && Files.isReadable(filePath)) {
            Logger.debug("Trying to determine type of file {}", filePath);
            var buf = ByteBuffer.allocate(8);  // should be enough to determine file type...
            var channel = FileChannel.open(filePath, StandardOpenOption.READ);
            var bytes = channel.read(buf, 0);
        }
        return null;    // TODO placeholder
    }
}
