package us.malfeasant.ocrapp.sup;

import us.malfeasant.ocrapp.SubPicture;
import us.malfeasant.ocrapp.SubtitleFile;

/**
 * Reference: https://blog.thescorpius.com/index.php/2017/07/15/presentation-graphic-stream-sup-files-bluray-subtitle-format/
 */
public class ReadSUP extends SubtitleFile.FileReader {
	public ReadSUP(SubtitleFile file) {
		super(file);
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'hasNext'");
	}

	@Override
	public SubPicture next() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'next'");
	}
}
