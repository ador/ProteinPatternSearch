package protka.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import protka.FastaItem;
import protka.Protein;
import protka.SequencePart;

public class FastaWriter {

  private OutputStream output;

  public FastaWriter(OutputStream os) {
    output = os;
  }

  public void closeOS() throws IOException {
    output.flush();
    output.close();
  }
  
  public void flush() throws IOException {
    output.flush();
  }

  public void writeFastaItem(FastaItem fastaItem) throws IOException {
    output.write(fastaItem.headerRow.getBytes(Charset.forName("UTF-8")));
    output.write("\n".getBytes(Charset.forName("UTF-8")));
    for (String sequenceRow : fastaItem.getSequenceRows()) {
      output.write(sequenceRow.getBytes(Charset.forName("UTF-8")));
      output.write("\n".getBytes(Charset.forName("UTF-8")));
    }
  }

  public void writeFastaList(List<FastaItem> fastaList) throws IOException {
    for (FastaItem fastaItem : fastaList) {
      writeFastaItem(fastaItem);
    }
  }
}
