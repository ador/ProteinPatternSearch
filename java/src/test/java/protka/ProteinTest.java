package protka;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ProteinTest {
  private Protein protein1 = new Protein();
  private Protein protein2 = new Protein();
  private Protein protein3 = new Protein();
  private Protein protein4 = new Protein();
  private Protein protein5 = new Protein();
  
  @Before
  public void setUp() {
    List<String> lines1 = new ArrayList<String>();
    lines1.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    lines1.add("AC   Q6GZX4;");
    lines1.add("FT   TRANSMEM    13    31       Helical; (Potential).");
    lines1.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    lines1.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    lines1.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    lines1.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    lines1.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    lines1.add("     SFRKIYTDLG WKFTPL");
    lines1.add("//");

    List<String> lines2 = new ArrayList<String>();
    lines2.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    lines2.add("AC   Q6GZX4;");
    lines2.add("FT   TRANSMEM    12    21       Helical; (Potential).");
    lines2.add("FT   TRANSMEM    23    25       Helical; (Potential).");
    lines2.add("FT   TRANSMEM   101   111       Helical; (Potential).");
    lines2.add("FT   TRANSMEM   123   123       Helical; (Potential).");
    lines2.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    lines2.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS "
        + "PPRVQVECPK APVEWNNPPS");
    lines2.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM "
        + "RRYQGIQTCK IPGKVLSDLD");
    lines2.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY "
        + "ENVNLIHYIL TDKRVDIQHL");
    lines2.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG "
        + "PDGPDILTVK TGSKGVLYDD");
    lines2.add("     SFRKIYTDLG WKFTPL");
    lines2.add("//");

    List<String> lines3 = new ArrayList<String>();
    lines3.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    lines3.add("AC   Q6GZX4;");
    lines3.add("FT   TOPO_DOM    88   239       Cytoplasmic (Potential).");
    lines3.add("FT   TRANSMEM   240   250       Helical; (Potential).");
    lines3.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    lines3.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    lines3.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    lines3.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    lines3.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    lines3.add("     SFRKIYTDLG WKFTPL");
    lines3.add("//");

    List<String> lines4 = new ArrayList<String>();
    lines4.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    lines4.add("AC   Q6GZX4;");
    lines4.add("FT   CHAIN         1    261       RPII140-upstream gene "
        + "protein.");
    lines4.add("FT                                /FTId=PRO_0000064352.");
    lines4.add("FT   TOPO_DOM     40     66       Extracellular (Potential).");
    lines4.add("FT   TRANSMEM     67     87       Helical; (Potential).");
    lines4.add("FT   TOPO_DOM     88    130       Cytoplasmic (Potential).");    
    lines4.add("FT   TRANSMEM    131    151       Helical; (Potential).");
    lines4.add("FT   TOPO_DOM    152    182       Extracellular (Potential).");
    lines4.add("FT   TRANSMEM    183    203       Helical; (Potential).");
    lines4.add("FT   CONFLICT     64     64       S -> F (in Ref. 1; "
        + "AAD40352).");
    lines4.add("SQ   SEQUENCE   261 AA;  29182 MW;  5DB78CF6CFC4435A CRC64;");
    lines4.add("     MNFLWKGRRF LIAGILPTFE GAADEIVDKE NKTYKAFLAS KPPEETGLER "
        + "LKQMFTIDEF");
    lines4.add("     GSISSELNSV YQAGFLGFLI GAIYGGVTQS RVAYMNFMEN NQATAFKSHF "
        + "DAKKKLQDQF");
    lines4.add("     TVNFAKGGFK WGWRVGLFTT SYFGIITCMS VYRGKSSIYE YLAAGSITGS "
        + "LYKVSLGLRG");
    lines4.add("     MAAGGIIGGF LGGVAGVTSL LLMKASGTSM EEVRYWQYKW RLDRDENIQQ "
        + "AFKKLTEDEN");
    lines4.add("     PELFKAHDEK TSEHVSLDTI K");
    lines4.add("//");

    List<String> lines5 = new ArrayList<String>();
    lines5.add("ID   CXG1_BOVIN              Reviewed;         396 AA.\n"); 
    lines5.add("AC   Q2HJ66;\n");
    lines5.add("OS   Pan troglodytes (Chimpanzee).\n"); 
    lines5.add("OC   Eukaryota; Metazoa; Chordata; Craniata; Vertebrata; Euteleostomi;\n"); 
    lines5.add("OC   Mammalia; Eutheria; Euarchontoglires; Primates; Haplorrhini;\n");
    lines5.add("OC   Catarrhini; Hominidae; Pan.");

    lines5.add("FT   CHAIN         1    396       Gap junction gamma-1 protein.\n"); 
    lines5.add("FT                                /FTId=PRO_0000244877.\n");
    lines5.add("FT   TOPO_DOM      1     22       Cytoplasmic (Potential).\n"); 
    lines5.add("FT   TRANSMEM     23     45       Helical; (Potential).\n");
    lines5.add("FT   TOPO_DOM     46     75       Extracellular (Potential).\n"); 
    lines5.add("FT   TRANSMEM     76     95       Helical; (Potential).\n");
    lines5.add("FT   TOPO_DOM     96    175       Cytoplasmic (Potential).\n"); 
    lines5.add("FT   TRANSMEM    176    198       Helical; (Potential).\n");
    lines5.add("FT   TOPO_DOM    199    228       Extracellular (Potential).\n"); 
    lines5.add("FT   TRANSMEM    229    248       Helical; (Potential).\n");
    lines5.add("FT   TOPO_DOM    249    300       Cytoplasmic (Potential).\n");
    lines5.add("FT   DOMAIN      301    396       Ig-like C1-type.\n");
    lines5.add("SQ   SEQUENCE   396 AA;  sdf\n");
    lines5.add("MSWSFLTRLLEEIHNHSTFVGKIWLTVLIVFRIVLTAVGGESIYYDEQSKFVCNTEQPGC\n"); 
    lines5.add("ENVCYDAFAPLSHVRFWVFQIILVATPSVMYLGYAIHKIAKMEHGDADKKAARSKPYAMR\n");
    lines5.add("WKQHRALEETEEDHEEDPMMYPEMELESEKENKDQNQSKPKHDGRRRIREDGLMKIYVLQ\n"); 
    lines5.add("LLARTVFEVGFLVGQYFLYGFQVHPFYVCSRLPCPHKIDCFISRPTEKTIFLLIMYGVTG\n"); 
    lines5.add("LCLLLNIWEMLHLGFGTIRDSLNSKRRELEDPGAYNYPFTWNTPSAPPGYNIAVKPDQIQ\n"); 
    lines5.add("YTELSNAKIAYKQNKANIAQEQQYGSHEDNLPPDLETLQREIRMAQERLDLAIQAYNHQN\n"); 
    lines5.add("NPHGSREKKAKVGSKAGSNKSSASSKSGDGKTSVWI");
 
    
    protein1.setLines(lines1);
    protein2.setLines(lines2);
    protein3.setLines(lines3);
    protein4.setLines(lines4);
    protein5.setLines(lines5);
    
    Protein.setMaxLength(70);
    Protein.setMinLength(40);
  }

  @Test
  public void testAcNum() {
    assertTrue(protein1.getAcNum().equals("Q6GZX4"));
    assertTrue(protein5.getAcNum().equals("Q2HJ66"));
  }

  @Test
  public void testReadTmNumbers() {
    List<SequencePart> tmNumbers = protein1.getTmNumbers();
    assertNotNull(tmNumbers);
    assertEquals(1, tmNumbers.size());
    // -1 because we index from 0, not from 1 as the data file
    assertEquals(12, tmNumbers.get(0).getFrom());
    assertEquals(30, tmNumbers.get(0).getTo());

    tmNumbers = protein2.getTmNumbers();
    assertNotNull(tmNumbers);
    assertEquals(4, tmNumbers.size());
  }

  @Test
  public void testReadMoreTmNumbers() {
    List<SequencePart> tmNumbers = protein2.getTmNumbers();
    assertNotNull(tmNumbers);
    assertEquals(4, tmNumbers.size());
    assertEquals(11, tmNumbers.get(0).getFrom());
    assertEquals(20, tmNumbers.get(0).getTo());
    assertEquals(122, tmNumbers.get(3).getFrom());
    assertEquals(122, tmNumbers.get(3).getTo());
  }

  @Test
  public void testGetSequencePart() {
    assertEquals("YKGEKA", protein1.getSequencePart(73, 78));
    assertEquals("L", protein1.getSequencePart(255, 255));
    assertNull(protein1.getSequencePart(255, 257));
  }

  @Test
  public void testGetSeqForTmPart() {
    protein1.setBeginsInside(false);
    SequencePart seqPart = protein1.getSeqForTmPart(0);
    assertNull(seqPart);
    protein1.setBeginsInside(true);
    seqPart = protein1.getSeqForTmPart(0);
    assertEquals(12, seqPart.getFrom());
    assertEquals(100, seqPart.getTo());

    protein3.setBeginsInside(true);
    seqPart = protein3.getSeqForTmPart(0);
    assertNull(seqPart);
    protein3.setBeginsInside(false);
    seqPart = protein3.getSeqForTmPart(0);
    assertEquals(169, seqPart.getFrom());
    assertEquals(249, seqPart.getTo());

    protein2.setBeginsInside(false);
    seqPart = protein2.getSeqForTmPart(0);
    assertNull(seqPart);
    seqPart = protein2.getSeqForTmPart(1);
    assertEquals(22, seqPart.getFrom());
    assertEquals(94, seqPart.getTo());

    protein2.setBeginsInside(true);
    seqPart = protein2.getSeqForTmPart(0);
    assertNull(seqPart);
    seqPart = protein2.getSeqForTmPart(1);
    assertNull(seqPart);
  }

  @Test
  public void testProt4() {
    protein4.setBeginsInside(false);
    List<SequencePart> tmNumbers = protein4.getTmNumbers();
    assertEquals(3, tmNumbers.size());
    assertEquals(66, protein4.getTmNumbers().get(0).getFrom());
    assertEquals(86, protein4.getTmNumbers().get(0).getTo());
    assertEquals(202, protein4.getTmNumbers().get(2).getTo());
    SequencePart seqPart = protein4.getSeqForTmPart(0);
    assertEquals(0, seqPart.getFrom());
    assertEquals(86, seqPart.getTo());
  }

  @Test
  public void testTmOriantation() {
    assertFalse(protein1.hasTmOrientationInfo());
    assertFalse(protein2.hasTmOrientationInfo());
    assertTrue(protein3.hasTmOrientationInfo());
    assertTrue(protein3.getBeginsInside());
    assertTrue(protein4.hasTmOrientationInfo());
    assertFalse(protein4.getBeginsInside());
  }

  @Test
  public void testProtein5A() {
    Protein.setMinLength(40);
    assertTrue(protein5.getBeginsInside());
    List<SequencePart> tmNumbers = protein5.getTmNumbers();
    
    assertEquals(4, tmNumbers.size());
    for (int i = 1; i < 4; i++) {
      assertNull(protein5.getSeqForTmPart(i));
    }
    List<SequencePart> domains = protein5.getFuncDomains();
    assertEquals(1, domains.size());
    
    SequencePart domain0 = protein5.getNearestDomain(12, 50);
    assertNull(domain0);

    SequencePart domain1 = protein5.getNearestDomain(300, 0);
    assertNull(domain1);

    SequencePart domain2 = protein5.getNearestDomain(300, 1);
    assertNotNull(domain2);

    SequencePart domain3 = protein5.getNearestDomain(251, 50);
    assertNotNull(domain3);
    assertEquals(domain2.getFrom(), domain3.getFrom());
    assertEquals(domain2.getTo(), domain3.getTo());
  }

  @Test
  public void testProtein5B() {
    Protein.setMinLength(30); // corner case
    
    assertTrue(protein5.getBeginsInside());
    List<SequencePart> tmNumbers = protein5.getTmNumbers();
    
    assertEquals(4, tmNumbers.size());
    int from = tmNumbers.get(0).getFrom();
    int to = tmNumbers.get(0).getTo();
    
    assertEquals(22, from);
    assertEquals(44, to);
    
    String tmSeq = protein5.getSequencePart(from, to);
    assertEquals("IWLTVLIVFRIVLTAVGGESIYY", tmSeq);

    SequencePart seqPart0 = protein5.getSeqForTmPart(0);
    assertEquals(22, seqPart0.getFrom());
    assertEquals(74, seqPart0.getTo());
    SequencePart seqPart1 = protein5.getSeqForTmPart(1);
    assertEquals(45, seqPart1.getFrom());
    assertEquals(94, seqPart1.getTo());
  }
  
  @Test
  public void testReadTaxonomy() {
    
    List<String> taxo1 = protein1.getTaxonomyList();
    assertEquals(0, taxo1.size());
    
    List<String> taxo5 = protein5.getTaxonomyList();
    assertEquals("Eukaryota", taxo5.get(0));
    assertEquals("Hominidae", taxo5.get(12));
    assertEquals(14, taxo5.size()); 
    
    String species = protein5.getSpecies();
    assertEquals("Pan troglodytes (Chimpanzee).", species);
    
  }

}
