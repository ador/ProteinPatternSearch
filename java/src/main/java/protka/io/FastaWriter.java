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

  public void writeProtSeqInPieces(Protein protein) throws IOException {
    if (protein.getBeginsInside() == null)
      return;
    for (int i = 0; i < protein.getTmNumbers().size(); ++i) {

      SequencePart sequencePart = protein.getSeqForTmPart(i);
      if (sequencePart != null) {
        int from = sequencePart.getFrom();
        int to = sequencePart.getTo();
        // +1 because in the output we index from 1 again
        String line = ">sp|" + protein.getAcNum() + "|" + i + "|" + (from + 1)
            + "|" + (to + 1) + "\n";
        output.write(line.getBytes(Charset.forName("UTF-8")));
        line = protein.getSequencePart(from, to) + "\n";
        output.write(line.getBytes(Charset.forName("UTF-8")));
      }
    }
  }
}
