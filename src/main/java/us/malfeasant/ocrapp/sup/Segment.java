package us.malfeasant.ocrapp.sup;

import java.nio.ByteBuffer;

public class Segment {
	private static final int MAGIC = 0x5047;
	
	enum SegmentType {
		PDS(0x14), ODS(0x15), PCS(0x16), WDS(0x17), END(0x80);
		final int id;
		SegmentType(int t) {
			id = t;
		}
		static SegmentType getTypeFor(int id) {
			switch (id) {
			case 0x14:
				return PDS;
			case 0x15:
				return ODS;
			case 0x16:
				return PCS;
			case 0x17:
				return WDS;
			case -0x80:	// funky byte stuff
				return END;
			default:
				throw new IllegalArgumentException("Invalid Segment Type: " + id);	// TODO: be more specific?
			}
		}
	}
	
	
	private final int presentStamp;	// in milliseconds
	private final int decodeStamp;
	private final SegmentType type;
	private final ByteBuffer data;
	
	private Segment(int pts, int dts, SegmentType t, ByteBuffer d) {
		presentStamp = pts;
		decodeStamp = dts;
		type = t;
		data = d;
		System.out.println("New Segment: type = " + type + "\tsize = " + d.limit());
	}
	
	public static Segment getSegmentFrom(ByteBuffer buf) {
		var magic = buf.getShort();
		if (magic != MAGIC) throw new Error("Bad magic number: " + magic);
		var pts = buf.getInt() / 90;
		var dts = buf.getInt() / 90;
		var type = SegmentType.getTypeFor(buf.get());
		var size = buf.getShort();
		var offset = buf.position();
		buf.position(offset + size);	// set new position for next segment
		return new Segment(pts, dts, type, buf.slice(offset, size));
	}
}
