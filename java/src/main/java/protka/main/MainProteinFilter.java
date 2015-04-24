package protka.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  
  FileInputStream inDat;
  FileInputStream inFasta;
  FileOutputStream outFasta;
  FileOutputStream outDat;
  Properties properties;
  int minSeqLen = 25;
  SwissprotReader swissprotReader;
  SwissprotWriter swissprotWriter;
  FastaReader fastaReader;
  FastaWriter fastaWriter;

  
  private static final String[] requiredProps = { "inputDatFile",
      "outputFastaFile", "inputFastaFile", "outputDatFile" };

  public void createDirsAndFiles(Properties properties) throws IOException {
    Path pathToFastaFile = Paths.get(properties.getProperty("outputFastaFile"));
    Files.createDirectories(pathToFastaFile.getParent());
    Files.createFile(pathToFastaFile);

    Path pathToDatFile = Paths.get(properties.getProperty("outputDatFile"));
    Files.createDirectories(pathToDatFile.getParent());
    Files.createFile(pathToDatFile);

    inDat = new FileInputStream(
        properties.getProperty("inputDatFile"));
    inFasta = new FileInputStream(
        properties.getProperty("inputFastaFile"));
    outFasta = new FileOutputStream(
        properties.getProperty("outputFastaFile"));
    outDat = new FileOutputStream(
        properties.getProperty("outputDatFile"));
  }
  
  public void closeFiles() throws IOException {
    swissprotWriter.closeOS();
    inDat.close();
    inFasta.close();
    outFasta.close(); 
  }
  
  private Map<String, FastaItem> readFasta() throws IOException {
    fastaReader = new FastaReader(inFasta);
    Map<String, FastaItem> fastaMap = new HashMap<String, FastaItem>(600000);

    int counter = 1;
    FastaItem fastaItem = fastaReader.getNextFastaItem();
    while (fastaItem != null) {
      fastaMap.put(fastaItem.acNum, fastaItem);
      fastaItem = fastaReader.getNextFastaItem();
      if (counter % 10000 == 0) {
        System.out.println("Read " + counter + " fasta items.");
      }
      ++counter;
    }
    return fastaMap;
  }
  
  
  public void compute() throws IOException {

    Map<String, FastaItem> fastaMap = readFasta();
    
    ProteinFilter proteinFilter = new ProteinFilter();

    // optional property
    if (properties.containsKey("minSeqLength")) {
      minSeqLen = Integer.parseInt(properties.getProperty("minSeqLength"));
      proteinFilter.setMinSeqLen(minSeqLen);
    }

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

    swissprotReader = new SwissprotReader(inDat);
    swissprotWriter = new SwissprotWriter(outDat);
    fastaWriter = new FastaWriter(outFasta);

    Protein protein;

    int counter = 1;
    while ((protein = swissprotReader.getNextProtein()) != null) {
      if (proteinFilter.match(protein)
          && fastaMap.containsKey(protein.getAcNum())) {
        swissprotWriter.writeProtein(protein);
        fastaWriter.writeFastaItem(fastaMap.get(protein.getAcNum()));
        
        if (counter % 1000 == 0) {
          System.out
              .println("Wrote " + counter + " fasta items and proteins.");
        }
        ++counter;
      }
    }

  }
  
  
  public static void main(String args[]) {
    if (args.length != 1) {
      System.out.println("Usage: MainProteinFilter settings.properties");
      return;
    }
    // create
    MainProteinFilter main = new MainProteinFilter();
    PropertyHandler propHandler = new PropertyHandler(requiredProps);

    main.properties = propHandler.readPropertiesFile(args[0]);

    try {
      if (propHandler.checkProps(main.properties)) {

        main.createDirsAndFiles(main.properties);        
        
        main.compute();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
