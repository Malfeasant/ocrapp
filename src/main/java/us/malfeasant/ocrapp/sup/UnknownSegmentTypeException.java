package us.malfeasant.ocrapp.sup;

import us.malfeasant.ocrapp.DecodeException;

public class UnknownSegmentTypeException extends DecodeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7128779628783885160L;

	public UnknownSegmentTypeException(String message) {
		super(message);
	}
}