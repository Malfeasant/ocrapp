package us.malfeasant.ocrapp.sub;

import us.malfeasant.ocrapp.SubPicture;
import us.malfeasant.ocrapp.SubtitleFile;

/**
 * Reference: https://dvd.sourceforge.net/dvdinfo/index.html
 */
public class ReadSUB extends SubtitleFile.FileReader {
    public ReadSUB(SubtitleFile file) {
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
