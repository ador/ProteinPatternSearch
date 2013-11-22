package protka.filter;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

public class SwissprotRowFilterTest {
  private final String oneShortItem1 = "ID   001R_FRG3G              Reviewed;         256 AA.\n"
      + "AC   Q6GZX4;\n"
      + "DT   28-JUN-2011, integrated into UniProtKB/Swiss-Prot.\n"
      + "DT   18-APR-2012, entry version 24.\n"
      + "OS   Frog virus 3 (isolate Goorha) (FV-3).\n"
      + "OC   Viruses; dsDNA viruses, no RNA stage; Iridoviridae; Ranavirus.\n"
      + "RN   [1]\n"
      + "RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].\n"
      + "RX   PubMed=15165820; DOI=10.1016/j.virol.2004.02.019;\n"
      + "RA   Tan W.G., Barkman T.J., Gregory Chinchar V., Essani K.;\n"
      + "RT   \"Comparative genomic analyses of frog virus 3, type species of the\n"
      + "RT   genus Ranavirus (family Iridoviridae).\";\n"
      + "RL   Virology 323:70-84(2004).\n"
      + "CC   -!- FUNCTION: Transcription activation (Potential).\n"
      + "CC   -----------------------------------------------------------------------\n"
      + "CC   Copyrighted by the UniProt Consortium, see http://www.uniprot.org/terms\n"
      + "KW   Activator; Complete proteome; Reference proteome; Transcription;\n"
      + "KW   Transcription regulation.\n"
      + "FT   CHAIN         1    256       Putative transcription factor 001R.\n"
      + "FT                                /FTId=PRO_0000410512.\n"
      + "FT   COMPBIAS     14     17       Poly-Arg.\n"
      + "SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;\n"
      + "     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK APVEWNNPPS\n"
      + "     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK IPGKVLSDLD\n"
      + "     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL TDKRVDIQHL\n"
      + "     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK TGSKGVLYDD\n"
      + "     SFRKIYTDLG WKFTPL\n" + "//\n";

  private final String oneShortItem2 = "ID   013L_IIV3               Reviewed;          90 AA.\n"
      + "AC   Q197E7;\n"
      + "DT   16-JUN-2009, integrated into UniProtKB/Swiss-Prot.\n"
      + "DE   RecName: Full=Uncharacterized protein IIV3-013L;\n"
      + "GN   ORFNames=IIV3-013L;\n"
      + "OS   Invertebrate iridescent virus 3 (IIV-3) (Mosquito iridescent virus).\n"
      + "OC   Metazoa; \n"
      + "OX   NCBI_TaxID=345201;\n"
      + "OH   NCBI_TaxID=7163; Aedes vexans (Inland floodwater mosquito) (Culex vexans).\n"
      + "RN   [1]\n"
      + "RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].\n"
      + "RX   PubMed=16912294; DOI=10.1128/JVI.00464-06;\n"
      + "RA   Kutish G.F., Rock D.L.;\n"
      + "RT   \"Genome of invertebrate iridescent virus type 3 (mosquito iridescent\n"
      + "RT   virus).\";\n"
      + "RL   J. Virol. 80:8439-8449(2006).\n"
      + "CC   -!- SUBCELLULAR LOCATION: Host membrane; Single-pass membrane protein\n"
      + "CC       (Potential).\n"
      + "CC   -----------------------------------------------------------------------\n"
      + "DR   EMBL; DQ643392; ABF82043.1; -; Genomic_DNA.\n"
      + "DR   RefSeq; YP_654585.1; NC_008187.1.\n"
      + "DR   GeneID; 4156262; -.\n"
      + "DR   GO; GO:0033644; C:host cell membrane; IEA:UniProtKB-SubCell.\n"
      + "DR   GO; GO:0016021; C:integral to membrane; IEA:UniProtKB-KW.\n"
      + "PE   4: Predicted;\n"
      + "KW   Complete proteome; Host membrane; Membrane; Reference proteome;\n"
      + "KW   Transmembrane; Transmembrane helix.\n"
      + "FT   CHAIN         1     90       Uncharacterized protein IIV3-013L.\n"
      + "FT                                /FTId=PRO_0000377942.\n"
      + "FT   TRANSMEM     52     72       Helical; (Potential).\n"
      + "SQ   SEQUENCE   90 AA;  10851 MW;  077C22D16315DB07 CRC64;\n"
      + "     MYYRDQYGNV KYAPEGMGPH HAASSSHHSA QHHHMTKENF SMDDVHSWFE KYKMWFLYAL\n"
      + "     ILALIFGVFM WWSKYNHDKK RSLNTASIFY\n" + "//\n";

  private InputStream is1;
  private InputStream isAll;

  @Before
  public void setUp() {
    is1 = new ByteArrayInputStream(oneShortItem1.getBytes());
    isAll = new ByteArrayInputStream((oneShortItem1 + oneShortItem2).getBytes());
  }

  @Test
  public void testIdentityFiltering() {
    SwissprotRowFilter filter = new SwissprotRowFilter();
    filter.setInputDat(is1);
    final StringBuilder outputCollector = new StringBuilder();
    OutputStream os = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        outputCollector.append((char) b);
      }
    };
    filter.setOutputDat(os);
    try {
      filter.filter();
    } catch (IOException e) {
      e.printStackTrace();
    }
    String output = outputCollector.toString();
    assertEquals(oneShortItem1, output);
  }

  @Test
  public void testFilteringDate() {
    SwissprotRowFilter filter = new SwissprotRowFilter();
    filter.setInputDat(is1);
    final StringBuilder outputCollector = new StringBuilder();
    OutputStream os = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        outputCollector.append((char) b);
      }
    };
    filter.setOutputDat(os);
    // set which rows to throw out
    filter.addFilterPattern("DT");
    try {
      filter.filter();
    } catch (IOException e) {
      e.printStackTrace();
    }
    String output = outputCollector.toString();
    // the original, without the rows beginning with the pattern(s)
    String expected = "ID   001R_FRG3G              Reviewed;         256 AA.\n"
        + "AC   Q6GZX4;\n"
        + "OS   Frog virus 3 (isolate Goorha) (FV-3).\n"
        + "OC   Viruses; dsDNA viruses, no RNA stage; Iridoviridae; Ranavirus.\n"
        + "RN   [1]\n"
        + "RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].\n"
        + "RX   PubMed=15165820; DOI=10.1016/j.virol.2004.02.019;\n"
        + "RA   Tan W.G., Barkman T.J., Gregory Chinchar V., Essani K.;\n"
        + "RT   \"Comparative genomic analyses of frog virus 3, type species of the\n"
        + "RT   genus Ranavirus (family Iridoviridae).\";\n"
        + "RL   Virology 323:70-84(2004).\n"
        + "CC   -!- FUNCTION: Transcription activation (Potential).\n"
        + "CC   -----------------------------------------------------------------------\n"
        + "CC   Copyrighted by the UniProt Consortium, see http://www.uniprot.org/terms\n"
        + "KW   Activator; Complete proteome; Reference proteome; Transcription;\n"
        + "KW   Transcription regulation.\n"
        + "FT   CHAIN         1    256       Putative transcription factor 001R.\n"
        + "FT                                /FTId=PRO_0000410512.\n"
        + "FT   COMPBIAS     14     17       Poly-Arg.\n"
        + "SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;\n"
        + "     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK APVEWNNPPS\n"
        + "     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK IPGKVLSDLD\n"
        + "     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL TDKRVDIQHL\n"
        + "     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK TGSKGVLYDD\n"
        + "     SFRKIYTDLG WKFTPL\n" + "//\n";

    assertEquals(expected, output);
  }

  @Test
  public void testFilteringMore() {
    SwissprotRowFilter filter = new SwissprotRowFilter();
    filter.setInputDat(is1);
    final StringBuilder outputCollector = new StringBuilder();
    OutputStream os = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        outputCollector.append((char) b);
      }
    };
    filter.setOutputDat(os);
    // set which rows to throw out
    filter.addFilterPattern("DT");
    filter.addFilterPattern("FT");
    filter.addFilterPattern("CC");
    filter.addFilterPattern("R");
    filter.addFilterPattern("OS");
    filter.addFilterPattern("OC");

    try {
      filter.filter();
    } catch (IOException e) {
      e.printStackTrace();
    }
    String output = outputCollector.toString();
    // the original, without the rows beginning with the pattern(s)
    String expected = "ID   001R_FRG3G              Reviewed;         256 AA.\n"
        + "AC   Q6GZX4;\n"
        + "KW   Activator; Complete proteome; Reference proteome; Transcription;\n"
        + "KW   Transcription regulation.\n"
        + "SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;\n"
        + "     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK APVEWNNPPS\n"
        + "     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK IPGKVLSDLD\n"
        + "     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL TDKRVDIQHL\n"
        + "     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK TGSKGVLYDD\n"
        + "     SFRKIYTDLG WKFTPL\n" + "//\n";

    assertEquals(expected, output);
  }

  @Test
  public void testFilteringMoreInputs() {
    SwissprotRowFilter filter = new SwissprotRowFilter();
    filter.setInputDat(isAll);
    final StringBuilder outputCollector = new StringBuilder();
    OutputStream os = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        outputCollector.append((char) b);
      }
    };
    filter.setOutputDat(os);
    // set which rows to throw out
    filter.addFilterPattern("DT");
    filter.addFilterPattern("FT");
    filter.addFilterPattern("CC");
    filter.addFilterPattern("R");
    try {
      filter.filter();
    } catch (IOException e) {
      e.printStackTrace();
    }
    String output = outputCollector.toString();
    // the original, without the rows beginning with the pattern(s)
    String expected = "ID   001R_FRG3G              Reviewed;         256 AA.\n"
        + "AC   Q6GZX4;\n"
        + "OS   Frog virus 3 (isolate Goorha) (FV-3).\n"
        + "OC   Viruses; dsDNA viruses, no RNA stage; Iridoviridae; Ranavirus.\n"
        + "KW   Activator; Complete proteome; Reference proteome; Transcription;\n"
        + "KW   Transcription regulation.\n"
        + "SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;\n"
        + "     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK APVEWNNPPS\n"
        + "     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK IPGKVLSDLD\n"
        + "     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL TDKRVDIQHL\n"
        + "     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK TGSKGVLYDD\n"
        + "     SFRKIYTDLG WKFTPL\n"
        + "//\n"
        + "ID   013L_IIV3               Reviewed;          90 AA.\n"
        + "AC   Q197E7;\n"
        + "DE   RecName: Full=Uncharacterized protein IIV3-013L;\n"
        + "GN   ORFNames=IIV3-013L;\n"
        + "OS   Invertebrate iridescent virus 3 (IIV-3) (Mosquito iridescent virus).\n"
        + "OC   Metazoa; \n"
        + "OX   NCBI_TaxID=345201;\n"
        + "OH   NCBI_TaxID=7163; Aedes vexans (Inland floodwater mosquito) (Culex vexans).\n"
        + "DR   EMBL; DQ643392; ABF82043.1; -; Genomic_DNA.\n"
        + "DR   RefSeq; YP_654585.1; NC_008187.1.\n"
        + "DR   GeneID; 4156262; -.\n"
        + "DR   GO; GO:0033644; C:host cell membrane; IEA:UniProtKB-SubCell.\n"
        + "DR   GO; GO:0016021; C:integral to membrane; IEA:UniProtKB-KW.\n"
        + "PE   4: Predicted;\n"
        + "KW   Complete proteome; Host membrane; Membrane; Reference proteome;\n"
        + "KW   Transmembrane; Transmembrane helix.\n"
        + "SQ   SEQUENCE   90 AA;  10851 MW;  077C22D16315DB07 CRC64;\n"
        + "     MYYRDQYGNV KYAPEGMGPH HAASSSHHSA QHHHMTKENF SMDDVHSWFE KYKMWFLYAL\n"
        + "     ILALIFGVFM WWSKYNHDKK RSLNTASIFY\n" + "//\n";

    assertEquals(expected, output);
  }

  // @Test
  public void testFilteringForClassification() {
    SwissprotRowFilter filter = new SwissprotRowFilter();
    filter.setInputDat(isAll);
    final StringBuilder outputCollector = new StringBuilder();
    OutputStream os = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        outputCollector.append((char) b);
      }
    };
    filter.setOutputDat(os);
    // set which rows to throw out
    filter.addFilterPattern("FT");
    filter.addFilterPattern("CC");
    filter.addFilterPattern("D");
    filter.addFilterPattern("R");
    try {
      filter.filter();
    } catch (IOException e) {
      e.printStackTrace();
    }
    String output = outputCollector.toString();
    String expected = "ID   013L_IIV3               Reviewed;          90 AA.\n"
        + "AC   Q197E7;\n"
        + "GN   ORFNames=IIV3-013L;\n"
        + "OS   Invertebrate iridescent virus 3 (IIV-3) (Mosquito iridescent virus).\n"
        + "OC   Metazoa; \n"
        + "OX   NCBI_TaxID=345201;\n"
        + "OH   NCBI_TaxID=7163; Aedes vexans (Inland floodwater mosquito) (Culex vexans).\n"
        + "PE   4: Predicted;\n"
        + "KW   Complete proteome; Host membrane; Membrane; Reference proteome;\n"
        + "KW   Transmembrane; Transmembrane helix.\n"
        + "SQ   SEQUENCE   90 AA;  10851 MW;  077C22D16315DB07 CRC64;\n"
        + "     MYYRDQYGNV KYAPEGMGPH HAASSSHHSA QHHHMTKENF SMDDVHSWFE KYKMWFLYAL\n"
        + "     ILALIFGVFM WWSKYNHDKK RSLNTASIFY\n" + "//\n";

    assertEquals(expected, output);
  }

}
