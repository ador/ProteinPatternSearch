package protka.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import protka.FastaItem;
import protka.io.FastaReader;

public class KClustResultReader {

  private InputStream isFasta;
  private InputStream isClusters;
  private InputStream isHeaders;
  private FastaReader fastaReader;
  private BufferedReader clustersReader;
  private BufferedReader headersReader;
  private Map<String, FastaItem> allFastaItems; // keys are headers
  private Map<Integer, String> fastaItemIdToHeaderMap;
  private List<List<FastaItem> > clusteringResult;
  
  public void setClustersInput(InputStream clustersStream) {
    isClusters = clustersStream;
    clustersReader = new BufferedReader(new InputStreamReader(isClusters));
  }

  public void setFastaInput(InputStream fastaStream) {
    isFasta = fastaStream;
    fastaReader = new FastaReader(isFasta);
  }

  public void setHeadersInput(InputStream headersStream) {
    isHeaders = headersStream;
    headersReader = new BufferedReader(new InputStreamReader(isHeaders));
  }
  
  // returns true if all OK
  private boolean checIfkAllInputsSet() {
    if (null == isHeaders) {
      System.err.println("KClus output 'headers' file is not set!");
      return false;
    }
    if (null == isFasta) {
      System.err.println("All 'fasta' file is not set!");
      return false;
    }
    if (null == isClusters) {
      System.err.println("KClus output 'clusters' file is not set!");
      return false;
    }
    return true;
  }
  
  private Map<String, FastaItem> readFastaInput() throws IOException {
    Map<String, FastaItem> ret = new HashMap<String, FastaItem>();
    FastaItem fastaItem = fastaReader.getNextFastaItem();
    while (null != fastaItem) {
      ret.put(fastaItem.getHeader(), fastaItem);
      fastaItem = fastaReader.getNextFastaItem();
    }
    return ret;
  }

  private Map<Integer, String> readHeadersInput() throws IOException {
    String line;
    Map<Integer, String> ret = new HashMap<Integer, String>();
    while ((line = headersReader.readLine()) != null) {
      String[] idAndHeader = line.split(" > ");
      if (idAndHeader.length != 2) {
        System.err.println("Invalid header line in header file: \n" + line);
      } else {
        Integer id = Integer.parseInt(idAndHeader[0]);
        String header = "> " + idAndHeader[1].trim();
        ret.put(id, header);
      }
    }
    return ret;
  }
 
  private boolean checkClustersHeader(String headerOfKClustClustersFile) {
    if (headerOfKClustClustersFile.charAt(0) != '#') {
      System.err.println("Not a valid KClust result file of clusters!");
      return false;
    }
    int strLen = headerOfKClustClustersFile.length();
    String numStr = headerOfKClustClustersFile.substring(1, strLen).trim();
    int num = Integer.parseInt(numStr);
    if (getNumOfFastaItems() != num) {
      System.err.println("Sizes differ! (fasta items from fasta file: " +
          getNumOfFastaItems() + ", and kclust clustering result: " +
          num + ")");
      return false;
    }
    if (getSizeOfHeadersMap() != num) {
      System.err.println("Sizes differ! (headers from kclust: " +
          getSizeOfHeadersMap() + ", and kclust clustering result:" +
          num +")");
      return false;
    }
    return true;
  }
 
  private List<List<FastaItem> > readClustersInput() throws IOException {
    String line = clustersReader.readLine(); // header with num of lines
    checkClustersHeader(line);
    List<List<FastaItem> > ret = new ArrayList<List<FastaItem> >();
    Integer id;
    Integer clusId = -1;
    List<FastaItem> lastCluster = null;
    while ((line = clustersReader.readLine()) != null) {
      String[] idAndClusId = line.split(" ");
      if (idAndClusId.length != 2) {
        System.err.println("Invalid line in clusters file: \n" + line);
      } else {
        id = Integer.parseInt(idAndClusId[0]);
        Integer clusIdNow = Integer.parseInt(idAndClusId[1].trim());
        if (clusId != clusIdNow) {
          lastCluster = new ArrayList<FastaItem>();
          ret.add(lastCluster);
        }
        clusId = clusIdNow;
        String headerForItem = fastaItemIdToHeaderMap.get(id);
        FastaItem item = allFastaItems.get(headerForItem);
        if (null == item) {
          System.err.println("null item added: for header ");
        }
        lastCluster.add(item);        
      }
    }
    return ret;
  }
 
  public void readClusters() throws IOException {
    if (!checIfkAllInputsSet()) {
      System.err.println("Error. Stopping.");
      return;
    }
    allFastaItems = readFastaInput();
    fastaItemIdToHeaderMap = readHeadersInput();
    clusteringResult = readClustersInput();
  }

  
  public int getSizeOfHeadersMap() {
    return fastaItemIdToHeaderMap.size();
  }

  public int getNumOfFastaItems() {
    return allFastaItems.size();
  }

  public int getNumOfClusters() {
    return clusteringResult.size();
  }

  public List<List<FastaItem> > getClusters() {
    return clusteringResult;
  }
  
}