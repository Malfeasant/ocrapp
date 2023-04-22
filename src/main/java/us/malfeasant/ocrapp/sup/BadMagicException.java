package us.malfeasant.ocrapp.sup;

import us.malfeasant.ocrapp.DecodeException;

public class BadMagicException extends DecodeException {
	/**
	 * Signifies an attempt to open a file that is missing its magic number.
	 * Meaningless to try to decode the rest of the file after this.
	 */
	private static final long serialVersionUID = -975150291221631482L;

	public BadMagicException(String message) {
		super(message);
	}
}