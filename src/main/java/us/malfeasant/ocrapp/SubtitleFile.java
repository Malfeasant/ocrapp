package us.malfeasant.ocrapp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

import org.tinylog.Logger;

/**
 * Represents a subtitle file- opens it for reading, but delegates the interpretation
 * of the file to a separate class.  Only knows enough about each format to pick the
 * appropriate reader.
 */
public class SubtitleFile implements Iterable<SubPicture> {
    private final FileChannel channel;
    private final FileType type;

    /**
     * Constructor opens file & identifies its type
     * @param filePath
     * @throws IOException
     * @throws UnknownFileTypeException
     */
    public SubtitleFile(Path filePath) throws IOException, UnknownFileTypeException {
        // Easiest first- if extension is .idx, substitute .sub & proceed
        if (filePath.toString().endsWith(".idx")) {
            filePath = Path.of(filePath.toString().replaceAll("\\.idx$", ".sub"));
            Logger.info("Got .idx file- looking for corresponding .sub file {}", filePath);
        }
        channel = FileChannel.open(filePath, StandardOpenOption.READ);
        if (Files.isRegularFile(filePath)) {
            Logger.debug("Trying to determine type of file {}", filePath);
            var buf = ByteBuffer.allocate(8);  // should be enough to determine file type...
            var channel = FileChannel.open(filePath, StandardOpenOption.READ);
            channel.read(buf, 0);

            if (buf.getInt(0) == 0x01ba) {  // using absolute position otherwise bytes are consumed
                Logger.info("Looks like a SUB file (DVD)");
                type = FileType.SUB;
            } else if (buf.getShort(0) == 0x5047) {
                Logger.info("Looks like a SUP file (Bluray)");
                type = FileType.SUP;
            } else {
                // If file type hasn't been deduced by here, throw an exception
                throw new UnknownFileTypeException("Could not deduce type of file " + filePath);
            }
        } else throw new IOException("Unknown problem opening file.");
    }

    private enum FileType {
        SUB, SUP;   // TODO any other file types?  .mkv, .vob, .iso?
    }

    @Override
    public Iterator<SubPicture> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }
}
