package us.malfeasant.ocrapp.sup;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import us.malfeasant.ocrapp.DecodeException;
import us.malfeasant.ocrapp.ReadFile;

public class ReadSUP extends ReadFile {
	
	private final ObservableList<Segment> segments = FXCollections.observableArrayList();
	private final MappedByteBuffer buffer;
	
	/**
	 * Only constructor- not meant to be called directly, but inheritance is hard...
	 * called by static factory method in ReadFile with already open
	 * and readable FileChannel
	 * @param fc
	 * @throws IOException 
	 */
	public ReadSUP(FileChannel fc) throws IOException, DecodeException {
		super(fc);
   		buffer = fc.map(MapMode.READ_ONLY, 0, fc.size());
   		var offset = 0;
   		while (offset < buffer.limit()) {
   			segments.add(Segment.getSegmentFrom(buffer));
   			offset = buffer.position();
   		}
	}
}
