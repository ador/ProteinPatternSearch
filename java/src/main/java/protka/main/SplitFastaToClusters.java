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
import protka.io.KClustResultReader;


// This class can discard clusters that are too small, use "minClusterSize"
public class SplitFastaToClusters {
  private static final String[] requiredProps = { "inputFastaFile",
      "inputKClustResultDir", "outputFastaDir",
      "outFileNameBeginnings", "minClusterSize" };

  private PropertyHandler propHandler = new PropertyHandler(requiredProps);
  private FileInputStream isFasta;
  private FileInputStream isKClusHeaders;
  private FileInputStream isKClusClusters;
  private FileOutputStream osFasta; // there will be many files
  private String outFastaDir;
  private String outFileNameBeginnings;
  private int minClusterSize;
  
  private Properties properties;
  private KClustResultReader kClusReader;
  private FastaWriter fastaWriter;

  private void createInputsAndOutDir() throws IOException {
    String inputKClusResultsPath = properties.getProperty("inputKClustResultDir");
    isFasta = new FileInputStream(properties.getProperty("inputFastaFile"));
    isKClusHeaders = new FileInputStream(inputKClusResultsPath + File.separator + "headers.dmp");
    isKClusClusters = new FileInputStream(inputKClusResultsPath + File.separator + "clusters.dmp");
    outFastaDir = properties.getProperty("outputFastaDir");
    minClusterSize = Integer.parseInt(properties.getProperty("minClusterSize"));
    outFileNameBeginnings = properties.getProperty("outFileNameBeginnings");
    File targetFile = new File(outFastaDir); 
    targetFile.mkdirs();    
  }

  private void initClusterReader() {
    kClusReader = new KClustResultReader();
    kClusReader.setClustersInput(isKClusClusters);
    kClusReader.setHeadersInput(isKClusHeaders);
    kClusReader.setFastaInput(isFasta);
  }
  
  // returns the number of clusters (=number of files) written
  private int writeClustersToFiles() throws IOException {
    kClusReader.readClusters();
    List<List<FastaItem> > allClusters = kClusReader.getClusters();
    System.out.println("ALL clusters read: " + allClusters.size() + "\n\n");
    int clustersWritten = 0;
    int clustersDiscarded = 0;
    int totalItems = 0;
    for (int i = 0; i < allClusters.size(); ++i) {
      List<FastaItem> clu = allClusters.get(i);
      int cluSize = clu.size();
      totalItems += cluSize;
      if (cluSize >= minClusterSize) {
        String outFileName = outFastaDir + File.separator +
            outFileNameBeginnings + "_" + (clustersWritten + 1) + ".fasta";
        FileOutputStream fos = new FileOutputStream(outFileName);
        if (clustersWritten > 0) {
          fastaWriter.closeOS();
        }
        fastaWriter = new FastaWriter(fos);
        fastaWriter.writeFastaList(clu);
        clustersWritten++;
      } else {
        clustersDiscarded++;
      }
    }
    fastaWriter.closeOS();
    System.out.println("FastaItems clustered in total: " + totalItems);
    System.out.println("Total num of clusters: " + (clustersDiscarded + clustersWritten));
    System.out.println("Clusters discarded (too small): " + clustersDiscarded);
    System.out.println("Clusters written: " + clustersWritten + " to path: " + outFastaDir);
    return clustersWritten;
  }
  
  private void closeFiles() throws IOException {
    isFasta.close();
    isKClusClusters.close();
    isKClusHeaders.close();
  }
  
  public static void main(String args[]) throws IOException {
    if (args.length != 1) {
      System.out.println("Usage: SplitFastaToClusters settings.properties");
      return;
    }
    SplitFastaToClusters splitter = new SplitFastaToClusters();
    try {
      splitter.properties = splitter.propHandler.readPropertiesFile(args[0]);
      splitter.createInputsAndOutDir();
      splitter.initClusterReader();
      splitter.writeClustersToFiles();
      splitter.closeFiles();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}