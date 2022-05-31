package us.malfeasant.ocrapp.sup;

import us.malfeasant.ocrapp.DecodeException;

public class BadMagicException extends DecodeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -975150291221631482L;

	public BadMagicException(String message) {
		super(message);
	}
}