package protka.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import protka.FastaItem;
import protka.Protein;
import protka.io.FastaReader;
import protka.io.FastaWriter;
import protka.io.SwissprotReader;
import protka.filter.TmFragmentFilter;

public class CutTmOutFragments {
  private static final String[] requiredProps = { "inputDatFile",
      "inputFastaFile", "outputFastaFile",
      "minLenOfExtracellularPart", "maxLenOfExtracellularPart" };
  private PropertyHandler propHandler = new PropertyHandler(requiredProps);
  private FileInputStream isDat;
  private FileInputStream isFasta;
  private FileOutputStream osFasta;
  private Properties properties;
  private FastaReader fastaReader;
  private FastaWriter fastaWriter;
  private int minLenOfExtracellularPart;
  private int maxLenOfExtracellularPart;
  private TmFragmentFilter filter;
  private SwissprotReader swissprotReader;
  
  private void createInputs() throws IOException {
    isDat = new FileInputStream(properties.getProperty("inputDatFile"));
    isFasta = new FileInputStream(properties.getProperty("inputFastaFile"));
    fastaReader = new FastaReader(isFasta);
    swissprotReader = new SwissprotReader(isDat);
  }
  
  private void createOutDirsAndFile(String outFastaPath) throws IOException {
    String outFastaDir = outFastaPath.substring(0, outFastaPath.lastIndexOf(File.separator));
    File targetFile = new File(outFastaDir); 
    targetFile.mkdirs();
    osFasta = new FileOutputStream(outFastaPath);
    fastaWriter = new FastaWriter(osFasta);
  }
  
  private Map<String, FastaItem> readFasta()  throws IOException {
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
  
  private void cutFragments() throws FileNotFoundException, IOException {
    if (propHandler.checkProps(properties)) {
      createInputs();
      createOutDirsAndFile(properties.getProperty("outputFastaFile"));
      minLenOfExtracellularPart = Integer.parseInt(properties.
                getProperty("minLenOfExtracellularPart"));
      maxLenOfExtracellularPart = Integer.parseInt(properties.
                getProperty("maxLenOfExtracellularPart"));
      Map<String, FastaItem> fastaMap = readFasta();
                
      filter = new TmFragmentFilter();
      filter.setMinFragmentLength(minLenOfExtracellularPart);
      filter.setMaxFragmentLength(maxLenOfExtracellularPart);
      
      Protein protein;
      int counter = 1;
      while ((protein = swissprotReader.getNextProtein()) != null) {
        if (fastaMap.containsKey(protein.getAcNum())) {
          List<FastaItem> fastaItems = filter.getFragments(protein);
          for (FastaItem fi : fastaItems) {
            fastaWriter.writeFastaItem(fi);
          }
          
        }
        if (counter % 1000 == 0) {
          System.out
                .println("Wrote " + counter + " fasta items and proteins.");
        }
        ++counter;
      }
      closeFiles();
    }
  }
  
  private void closeFiles() throws IOException {
    isDat.close();
    isFasta.close();
    osFasta.flush();
    osFasta.close(); 
  }
  
  public static void main(String args[]) throws IOException {
    if (args.length != 1) {
      System.out.println("Usage: CutTmOutFragments settings.properties");
      return;
    }
    CutTmOutFragments fr = new CutTmOutFragments();
    try {
      fr.properties = fr.propHandler.readPropertiesFile(args[0]);
      fr.cutFragments();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
