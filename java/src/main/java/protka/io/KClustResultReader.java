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
      System.out.println("HR: " + line);
      String[] idAndHeader = line.split(" > ");
      if (idAndHeader.length != 2) {
        System.err.println("Invalid header line in header file: \n" + line);
      } else {
        Integer id = Integer.parseInt(idAndHeader[0]);
        String header = idAndHeader[1];
        ret.put(id, header);
      }
    }
    return ret;
  }
  
  public void readClusters() throws IOException {
    if (!checIfkAllInputsSet()) {
      System.err.println("Stopping.");
      return;
    }
    allFastaItems = readFastaInput();
    fastaItemIdToHeaderMap = readHeadersInput();
    //readClustersInput();
  
  }
  
  public int getSizeOfHeadersMap() {
    return fastaItemIdToHeaderMap.size();
  }

  public int getNumOfFastaItems() {
    return allFastaItems.size();
  }

  public int getNumOfClusters() {
    return 3;
    // todo
  }

  public List<List<FastaItem> > getClusters() {
    return null;
    // todo
  }
  
}