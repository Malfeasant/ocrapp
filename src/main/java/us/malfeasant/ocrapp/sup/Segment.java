package us.malfeasant.ocrapp.sup;

import java.nio.ByteBuffer;

public class Segment {
	private static final int MAGIC = 0x5047;
	
	enum SegmentType {
		PDS, ODS, PCS, WDS, END;
		static SegmentType getTypeFor(int id) throws UnknownSegmentTypeException {
			switch (id) {
			case 0x14:
				return PDS;
			case 0x15:
				return ODS;
			case 0x16:
				return PCS;
			case 0x17:
				return WDS;
			case 0x80:
				return END;
			default:
				throw new UnknownSegmentTypeException("Invalid Segment Type: " + id);	// TODO: be more specific?
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
	
	public static Segment getSegmentFrom(ByteBuffer buf) throws BadMagicException, UnknownSegmentTypeException {
		var magic = buf.getShort();
		if (magic != MAGIC) throw new BadMagicException(
				"Bad magic number: " + Integer.toHexString(magic) + " at position " + buf.position());
		var pts = buf.getInt() / 90;
		var dts = buf.getInt() / 90;
		var type = SegmentType.getTypeFor(Byte.toUnsignedInt(buf.get()));
		var size = Short.toUnsignedInt(buf.getShort());	// otherwise we sometimes get negative sizes...
		var offset = buf.position();
		buf.position(offset + size);	// set new position for next segment
		return new Segment(pts, dts, type, buf.slice(offset, size));
	}
}
