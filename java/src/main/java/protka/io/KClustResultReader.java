package protka.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import protka.FastaItem;

public class KClustResultReader {

  private InputStream isFasta;
  private InputStream isClusters;
  private InputStream isHeaders;
  private BufferedReader fastaReader;
  private BufferedReader clustersReader;
  private BufferedReader headersReader;
//  private String nextHeaderRow;
  
  
  public void setClustersInput(InputStream clustersStream) {
    isClusters = clustersStream;
    clustersReader = new BufferedReader(new InputStreamReader(isClusters));
  }

  public void setFastaInput(InputStream fastaStream) {
    isFasta = fastaStream;
    fastaReader = new BufferedReader(new InputStreamReader(isFasta));
  }

  public void setHeadersInput(InputStream headersStream) {
    isHeaders = headersStream;
    headersReader = new BufferedReader(new InputStreamReader(isHeaders));
  }
  
  public void readClusters() {
    // todo
  
  }
  
  public int getNumOfClusters() {
    return 3;
    // todo
  }

  
}