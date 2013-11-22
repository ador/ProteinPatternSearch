package protka.filter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import protka.Protein;

public class ProteinFilterTest {

  @Test
  public void testMatch() {
    ProteinFilter proteinFilter = new ProteinFilter();
    proteinFilter.addPattern("KW", "Transmembrane;");
    
    List<String> data1 = new ArrayList<String>();

    data1.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    data1.add("AC   Q6GZX4;");
    data1.add("KW   Transmembrane;");
    data1.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    data1.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    data1.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    data1.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");

    Protein tmProtein = new Protein();
    tmProtein.setLines(data1);
    assertTrue(proteinFilter.match(tmProtein));
    
    List<String> data2 = new ArrayList<String>();

    data2.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    data2.add("AC   Q6GZX4;");
    data2.add("KW   Other;");
    data2.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    data2.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    data2.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    data2.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");


    Protein nonTmProtein = new Protein();
    nonTmProtein.setLines(data2);
    assertFalse(proteinFilter.match(nonTmProtein));
  }
  
  @Test
  public void testMatch2() {
    ProteinFilter proteinFilter = new ProteinFilter();
    proteinFilter.addPattern("KW", "Transmembrane;");
    
    List<String> data = new ArrayList<String>();

    data.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    data.add("AC   Q6GZX4;");
    data.add("KW   asas;Amls");
    data.add("KW   Transmembrane;Amls");
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
    assertTrue(proteinFilter.match(protein));
    proteinFilter.addPattern("OC", "Metazoa;");
    assertTrue(proteinFilter.match(protein));

    proteinFilter.addPattern("AC", "W");
    assertFalse(proteinFilter.match(protein));
  }

  @Test
  public void testMatch3() {
    ProteinFilter proteinFilter = new ProteinFilter();
    proteinFilter.addPattern("KW", "Transmembrane;");
    proteinFilter.addPattern("KW", "Metal");
    
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
    assertTrue(proteinFilter.match(protein));
  }

  @Test
  public void testMatch4() {
    ProteinFilter proteinFilter = new ProteinFilter();
    proteinFilter.addPattern("KW", "Metal");
    proteinFilter.addPattern("KW", "Transmembrane2;");
    
    List<String> data = new ArrayList<String>();

    data.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    data.add("AC   Q6GZX4;");
    data.add("KW   asas;Amls");
    data.add("KW   Transmembrane;Amls");
    data.add("KW   asas;Amls");
    data.add("KW   Metal");
    data.add("KW   asas;Amls");
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
    assertFalse(proteinFilter.match(protein));
  }

}
