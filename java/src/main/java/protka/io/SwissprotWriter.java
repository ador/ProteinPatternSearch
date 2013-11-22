package protka.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import protka.Protein;

public class SwissprotWriter {

  private OutputStream output;

  public SwissprotWriter(OutputStream os) {
    output = os;
  }

  public void closeOS() throws IOException {
    output.flush();
    output.close();
  }

  public void writeProtein(Protein protein) throws IOException {
    for (String line : protein.getLines()) {
      output.write(line.getBytes(Charset.forName("UTF-8")));
      output.write("\n".getBytes(Charset.forName("UTF-8")));
    }
    output.write("//\n".getBytes(Charset.forName("UTF-8")));
  }

  public void writeProteinList(List<Protein> proteinList) throws IOException {
    for (Protein protein : proteinList) {
      writeProtein(protein);
    }
  }

}
