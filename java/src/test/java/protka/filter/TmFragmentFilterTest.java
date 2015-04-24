package protka.filter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import protka.Protein;
import protka.SequencePart;
import protka.FastaItem;

public class TmFragmentFilterTest {

  @Test
  public void testFilter0() {
    TmFragmentFilter tmFilter = new TmFragmentFilter();
    assertNotNull(tmFilter);
    tmFilter.setMinFragmentLength(30);
    tmFilter.setMaxFragmentLength(40);
    assertEquals(30, tmFilter.getMinFragmentLength());
    assertEquals(40, tmFilter.getMaxFragmentLength());    
  }

  @Test
  public void testFilter1() {
    TmFragmentFilter tmFilter = new TmFragmentFilter();
    tmFilter.setMinFragmentLength(30);
    tmFilter.setMaxFragmentLength(40);
    List<String> data = new ArrayList<String>();
    data.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    data.add("AC   Q6GZX4;");
    data.add("KW   asas;Amls");
    data.add("KW   Transmembrane;Amls");
    data.add("KW   Metal");
    data.add("OC   AKDKD;sf");
    data.add("OC   Metazoa;sf");
    data.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    data.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    data.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    data.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    data.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    data.add("     SFRKIYTDLG WKFTPL");

    Protein protein = new Protein();
    protein.setLines(data);
    List<FastaItem> result = tmFilter.getFragments(protein);
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void testFilter2() {
    TmFragmentFilter tmFilter = new TmFragmentFilter();
    tmFilter.setMinFragmentLength(30);
    tmFilter.setMaxFragmentLength(40);
    List<String> data = new ArrayList<String>();
    data.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    data.add("AC   Q6GZX4;");
    data.add("KW   asas;Amls");
    data.add("KW   Transmembrane;Amls");
    data.add("KW   Metal");
    data.add("FT   SIGNAL        1     24       {ECO:0000250}.");
    data.add("FT   CHAIN        25    365       Class I histocompatibility antigen, Gogo-");
    data.add("FT                                A*0101 alpha chain.");
    data.add("FT                                /FTId=PRO_0000018897.");
    data.add("FT   TOPO_DOM     25    308       Extracellular. {ECO:0000255}.");
    data.add("FT   TRANSMEM    309    332       Helical. {ECO:0000255}.");
    data.add("FT   TOPO_DOM    333    365       Cytoplasmic. {ECO:0000255}.");
    data.add("OC   AKDKD;sf");
    data.add("OC   Metazoa;sf");
    data.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    data.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    data.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    data.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    data.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    data.add("     SFRKIYTDLG WKFTPL");

    Protein protein = new Protein();
    protein.setLines(data);
    List<FastaItem> result = tmFilter.getFragments(protein);
    assertNotNull(result);
    // expecting 0 results because the extracellular part is BEFORE the TM part
    assertEquals(0, result.size());
  }
  
  
  @Test
  public void testFilter3() {
    TmFragmentFilter tmFilter = new TmFragmentFilter();
    tmFilter.setMinFragmentLength(10);
    tmFilter.setMaxFragmentLength(40);
    List<String> data = new ArrayList<String>();
    data.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    data.add("AC   Q6GZX4;");
    data.add("KW   asas;Amls");
    data.add("KW   Transmembrane;Amls");
    data.add("KW   Metal");
    data.add("FT   SIGNAL        1     24       {ECO:0000250}.");
    data.add("FT   CHAIN        25    365       Class I histocompatibility antigen, Gogo-");
    data.add("FT                                A*0101 alpha chain.");
    data.add("FT                                /FTId=PRO_0000018897.");
    data.add("FT   TOPO_DOM     25    208       Cytoplasmic. {ECO:0000255}.");
    data.add("FT   TRANSMEM    209    232       Helical. {ECO:0000255}.");
    data.add("FT   TOPO_DOM    233    256       Extracellular. {ECO:0000255}.");
    data.add("OC   AKDKD;sf");
    data.add("OC   Metazoa;sf");
    data.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    data.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    data.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    data.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    data.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    data.add("     SFRKIYTDLG WKFTPL");

    Protein protein = new Protein();
    protein.setLines(data);
    List<FastaItem> result = tmFilter.getFragments(protein);
    assertTrue(protein.getBeginsInside());
    assertEquals(1, result.size());
    assertNotNull(result.get(0));
    assertEquals("> Q6GZX4|209-256", result.get(0).getHeader());
  }

  @Test
  public void testFilter4() {
    TmFragmentFilter tmFilter = new TmFragmentFilter();
    tmFilter.setMinFragmentLength(30);
    tmFilter.setMaxFragmentLength(40);
    List<String> data = new ArrayList<String>();
    data.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    data.add("AC   Q6GZX4;");
    data.add("KW   asas;Amls");
    data.add("KW   Transmembrane;Amls");
    data.add("KW   Metal");
    data.add("FT   SIGNAL        1     24       {ECO:0000250}.");
    data.add("FT   CHAIN        25    365       Class I histocompatibility antigen, Gogo-");
    data.add("FT                                A*0101 alpha chain.");
    data.add("FT                                /FTId=PRO_0000018897.");
    data.add("FT   TOPO_DOM     25     38       Cytoplasmic. {ECO:0000255}.");
    data.add("FT   TRANSMEM     39     60       Helical. {ECO:0000255}.");
    data.add("FT   TOPO_DOM     61    146       Extracellular. {ECO:0000255}.");
    data.add("FT   TRANSMEM    147    168       Helical. {ECO:0000255}.");
    data.add("FT   TOPO_DOM    169    208       Cytoplasmic. {ECO:0000255}.");
    data.add("FT   TRANSMEM    209    232       Helical. {ECO:0000255}.");
    data.add("FT   TOPO_DOM    233    256       Extracellular. {ECO:0000255}.");
    data.add("OC   AKDKD;sf");
    data.add("OC   Metazoa;sf");
    data.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    data.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    data.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    data.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    data.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    data.add("     SFRKIYTDLG WKFTPL");

    Protein protein = new Protein();
    protein.setLines(data);
    List<FastaItem> result = tmFilter.getFragments(protein);
    assertEquals(2, result.size());
    
    assertEquals("> Q6GZX4|39-100", result.get(0).getHeader());
    assertEquals("WSPPRVQVECPKAPVEWNNPPSEKGLIVGHFSGIKYKGEKAQASEVDVNKMCCWVSKFKDAM", result.get(0).getSequenceRows().get(0));
  }

}
