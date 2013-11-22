package protka.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import protka.FastaItem;

public class FastaReader {

  private final InputStream input;
  private final BufferedReader reader;
  private String nextHeaderRow;

  public FastaReader(InputStream in) {
    input = in;
    reader = new BufferedReader(new InputStreamReader(input));    
  }

  public FastaItem getNextFastaItem() throws IOException {
    String line;
    String headerRow;
    String acNum;
    int counter = 0;
    FastaItem fastaItem = null;
    while ((line = reader.readLine()) != null) {
      if (line.charAt(0) == '>' && counter == 0) {
        headerRow = line;
        acNum = line.split("\\|")[1];
        fastaItem = new FastaItem(headerRow, acNum);
        ++counter;
      }
      else if(line.charAt(0) != '>' && counter == 0){
        headerRow = nextHeaderRow;
        acNum = nextHeaderRow.split("\\|")[1];
        fastaItem = new FastaItem(headerRow, acNum);
        fastaItem.addSeqRow(line);
        ++counter;
      }
      else if (line.charAt(0) == '>' && counter == 1){
        nextHeaderRow = line;
        return fastaItem;
      }
      else {
       fastaItem.addSeqRow(line); 
      }
    }
    return fastaItem;
  }

}
