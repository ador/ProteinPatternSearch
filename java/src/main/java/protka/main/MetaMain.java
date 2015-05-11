package protka.main;

import java.io.IOException;

public class MetaMain {

  public enum Task {
    COMPUTE_STATS, FILTER_DAT_FEATURES, FILTER_ROWS, FASTA_AND_DAT_FILTER, TM_STATS,
    TM_EXT_FRAGMENTS, SPLIT_FASTA_TO_CLUSTERS_KCLUST, STATS_ARFF_FOR_WEKA, WEKA_CLUSTERING,
    SPLIT_FASTA_TO_CLUSTERS_WEKA
  }

  public static void printHelp() {
    System.out.println("Usage: java -jar PPsearch.jar <taskname> <settings.properties file path>");
    System.out.println("\t Available tasks are:");
    for (Task task : Task.values()) {
      System.out.println(" " + task.toString());
    }
  }

  public static void main(String args[]) throws IOException {
    if (args.length != 2) {
      MetaMain.printHelp();
      return;
    }
    String whichTaskStr = args[0];
    Task whichTask = Task.valueOf(whichTaskStr);
    String propFileName = args[1];
    String[] argsToPass = {propFileName};

    switch (whichTask) {
      case COMPUTE_STATS: ComputeHDStats.main(argsToPass); break;
      case FILTER_DAT_FEATURES: FilteringDat.main(argsToPass); break;
      case FILTER_ROWS: FilterRows.main(argsToPass); break;
      case FASTA_AND_DAT_FILTER: MainProteinFilter.main(argsToPass); break;
      case TM_STATS: TransmemStats.main(argsToPass); break;
      case TM_EXT_FRAGMENTS: CutTmOutFragments.main(argsToPass); break;
      case STATS_ARFF_FOR_WEKA: CreateArffFromFragmentStats.main(argsToPass); break;
      case WEKA_CLUSTERING: WekaClustering.main(argsToPass); break;
      case SPLIT_FASTA_TO_CLUSTERS_WEKA: CreateClusterFiles.main(argsToPass); break;
      case SPLIT_FASTA_TO_CLUSTERS_KCLUST: SplitFastaToClusters.main(argsToPass); break;
      default: printHelp(); break;
    }
  }
  
}
