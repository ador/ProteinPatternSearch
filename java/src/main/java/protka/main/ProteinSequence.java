package protka.main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import protka.Protein;
import protka.io.FastaWriter;
import protka.io.HmmTopReader;
import protka.io.SwissprotReader;

public class ProteinSequence {
  private static final String[] requiredProps = { "inputDatFile",
      "inputHmmTop", "outputFastaFile" };

  public static void main(String args[]) throws IOException {
    if (args.length != 1) {
      System.out.println("Usage: ProteinSequence prop.hmmfastaprop");
      return;
    }

    Protein.setMaxLength(70);
    Protein.setMinLength(40);

    PropertyHandler propHandler = new PropertyHandler(requiredProps);
    Properties properties = propHandler.readPropertiesFile(args[0]);

    if (propHandler.checkProps(properties)) {
      FileInputStream inDat;
      FileInputStream inHmm;
      FileOutputStream outFasta;

      inDat = new FileInputStream(
          properties.getProperty("inputDatFile"));
      inHmm = new FileInputStream(properties.getProperty("inputHmmTop"));
      outFasta = new FileOutputStream(
          properties.getProperty("outputFastaFile"));

      SwissprotReader swissReader = new SwissprotReader(inDat);
      HmmTopReader hmmReader = new HmmTopReader(inHmm);
      FastaWriter fastaWriter = new FastaWriter(outFasta);

      Protein prot = swissReader.getNextProtein();
      boolean beginsInsideHmmTopPred = hmmReader.getNextProteinBeginsInside();
      // TODO : overwrite hmmTop's result if the swissprot dat file contains 
      // information about this (FT  TOPO_DOM  Extracellular../Cytoplasmic..)
      boolean beginsInside = beginsInsideHmmTopPred;
      if (prot.hasTmOrientationInfo()) {
        //boolean beginsInside = prot.getBeginsInside;
      }
      
      int counter = 1;
      while (prot != null) {
        prot.setBeginsInside(beginsInside);
        fastaWriter.writeProtSeqInPieces(prot);
        prot = swissReader.getNextProtein();
        hmmReader.getNextProteinBeginsInside();
        System.out.println("Read " + counter + " protein.");
        ++counter;
      }

      hmmReader.closeReader();
      fastaWriter.closeOS();
      swissReader.closeReader();
    }
  }
}
