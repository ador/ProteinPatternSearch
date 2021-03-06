ProteinPatternSearch
====================

This repository includes Java classes that do the pre-processing steps
of filtering transmembrane protein fragments from the SwissProt protein database.
Project details can be read in Chapter 5 of [my dissertation](https://www.dropbox.com/s/cow9vys7wrl1kqj/thesis_aszabo.pdf?dl=0)

#### The tasks this software package is able to perform are:

 * `COMPUTE_STATS`: Expects a Swiss-Prot .dat file and counts ’H’, ’D’ and ’HD’
frequencies, and prints an expected ’HD’ frequency, assuming an uniform and
independent random distribution of amino acids.
 * `FILTER_DAT_ROWS`: Expects a .dat file and codes of Swiss-Prot lines to throw
out; writes a shorter .dat file.
 * `FASTA_AND_DAT_FILTER`: Filters both a .dat and a .fasta file, keeping
only proteins that match a pre-defined pattern of keywords and other features,
as well as an optional minimal sequence length.
 * `TM_STATS`: Prints statistics of transmembrane proteins of a .dat file (number
of total proteins, number of TM proteins, and number of TM proteins with
extra- or intracellular annotation).
 * `TM_EXT_FRAGMENTS`: Expects a .dat and a .fasta file, writes a .fasta
file with extracellular protein fragments and the neighboring transmembrane
sections. The minimum and maximum fragment lengths can be specified via the
minLenOfExtracellularPart and maxLenOfExtracellularPart properties.
 * `SPLIT_FASTA_TO_CLUSTERS_KCLUST`: Reads kClust output files and a
.fasta input; writes many .fasta files corresponding to the clusters. Discards
too small clusters, if the minClusterSize parameter is set.
 * `STATS_ARFF_FOR_WEKA`: Generates an .arff file (the input format of Weka)
from a .fasta file of proteins or protein fragments; the attributes (columns of
the .arff) will be the different amino acid counts, or amino acid group frequen-
cies for each protein(fragment).
 * `WEKA_CLUSTERING`: Runs k-means clustering, built into Weka. Besides the
input and output paths, mandatory parameters are: numberOfClusters and
randomSeed.
 * `SPLIT_FASTA_TO_CLUSTERS_WEKA`: This task is very similar to the
task "`SPLIT_FASTA_TO_CLUSTERS_KCLUST`", but it works with Weka
output files instead of kClust results.

## How to use

### A) Compile

To compile the java project, first get [Gradle](http://gradle.org/) (version 1.6 or above), and the run:

    gradle build


### B) Run the whole pipeline

The easiest way to get a working example is to follow the scripts and configs as described in my [dissertation](https://www.dropbox.com/s/cow9vys7wrl1kqj/thesis_aszabo.pdf?dl=0). There you will see where are the lines in the configs that should be changed to execute the programs on your files, with your parameters.




