package us.malfeasant.ocrapp.sup;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReadSUP {
	
	private final ObservableList<Segment> segments = FXCollections.observableArrayList();
	private final MappedByteBuffer buffer;
	
	/**
	 * Only constructor- must always pass a file
	 * @param f .sup file to import
	 * @throws IOException 
	 */
	public ReadSUP(Path f) throws IOException {
		if (Files.isReadable(f)) {
			var fc = FileChannel.open(f);
    		buffer = fc.map(MapMode.READ_ONLY, 0, fc.size());
    		var offset = 0;
    		while (offset < buffer.limit()) {
    			segments.add(Segment.getSegmentFrom(buffer));
    			offset = buffer.position();
    		}
		} else {
			throw new Error("Problem!");	// TODO: be more specific
		}
	}
}
