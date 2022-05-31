package us.malfeasant.ocrapp;

public class DecodeException extends Exception {
	public DecodeException(String message, Throwable cause) {
		super(message, cause);
	}
	public DecodeException(String message) {
		super(message);
	}
	private static final long serialVersionUID = -4776198930289188253L;
}
