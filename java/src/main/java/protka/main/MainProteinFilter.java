package protka.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import protka.FastaItem;
import protka.Protein;
import protka.filter.ProteinFilter;
import protka.io.FastaReader;
import protka.io.FastaWriter;
import protka.io.SwissprotReader;
import protka.io.SwissprotWriter;

public class MainProteinFilter {
  private static final String[] requiredProps = { "inputDatFile",
      "outputFastaFile", "inputFastaFile", "outputDatFile" };

  public static void main(String args[]) {
    if (args.length != 1) {
      System.out.println("Usage: MainOperation prop.properties");
      return;
    }

    PropertyHandler propHandler = new PropertyHandler(requiredProps);

    FileInputStream inDat;
    FileInputStream inFasta;
    FileOutputStream outFasta;
    FileOutputStream outDat;
    Properties properties = propHandler.readPropertiesFile(args[0]);

    try {
      if (propHandler.checkProps(properties)) {
        inDat = new FileInputStream(
            properties.getProperty("inputDatFile"));
        inFasta = new FileInputStream(
            properties.getProperty("inputFastaFile"));
        outFasta = new FileOutputStream(
            properties.getProperty("outputFastaFile"));
        outDat = new FileOutputStream(
            properties.getProperty("outputDatFile"));

        FastaReader fastaReader = new FastaReader(inFasta);
        Map<String, FastaItem> fastaMap = new HashMap<String, FastaItem>(600000);

        int counter = 1;
        FastaItem fastaItem = fastaReader.getNextFastaItem();
        while (fastaItem != null) {
          fastaMap.put(fastaItem.acNum, fastaItem);
          fastaItem = fastaReader.getNextFastaItem();
          if (counter % 10000 == 0) {
            System.out.println("Read " + counter + " fastaItem.");
          }
          ++counter;
        }

        ProteinFilter proteinFilter = new ProteinFilter();

        if (properties.containsKey("matchProteinPatterns")) {
          String[] patterns = properties.getProperty(
              "matchProteinPatterns").split("\\|");
          for (String pattern : patterns) {
            String[] kvPair = pattern.split(":");
            String key = kvPair[0];
            String value = kvPair[1];
            proteinFilter.addPattern(key, value);
          }
        }

        SwissprotReader swissprotReader = new SwissprotReader(inDat);
        SwissprotWriter swissprotWriter = new SwissprotWriter(outDat);
        FastaWriter fastaWriter = new FastaWriter(outFasta);
        Protein protein;

        counter = 1;
        while ((protein = swissprotReader.getNextProtein()) != null) {
          if (proteinFilter.match(protein)
              && fastaMap.containsKey(protein.acNum)) {
            swissprotWriter.writeProtein(protein);
            fastaWriter.writeFastaItem(fastaMap.get(protein.acNum));
            
            if (counter % 1000 == 0) {
              System.out
                  .println("Write " + counter + " fastaItem and protein.");
            }
            ++counter;
          }
        }

        swissprotWriter.closeOS();
        inDat.close();
        inFasta.close();
        outFasta.close();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
