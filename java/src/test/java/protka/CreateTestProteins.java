package protka;

import java.util.ArrayList;
import java.util.List;

public class CreateTestProteins {

  public static List<Protein> getTestProteins() {
    ArrayList<Protein> ret = new ArrayList<Protein>();
    
    Protein protein1 = new Protein();
    Protein protein2 = new Protein();
    Protein protein3 = new Protein();
    Protein protein4 = new Protein();
    Protein protein5 = new Protein();
    Protein protein6 = new Protein();
    Protein protein7 = new Protein(); // fuzzy
    Protein protein8 = new Protein(); // fuzzy
    Protein protein9 = new Protein(); // intra-membrane
    
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
    lines5.add("AC   Q2HJ66; Q2HJ68;\n");
    lines5.add("AC   Q2HJ70; Q2HJ69\n");
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
    lines5.add("     MSWSFLTRLLEEIHNHSTFVGKIWLTVLIVFRIVLTAVGGESIYYDEQSKFVCNTEQPGC\n"); 
    lines5.add("     ENVCYDAFAPLSHVRFWVFQIILVATPSVMYLGYAIHKIAKMEHGDADKKAARSKPYAMR\n");
    lines5.add("     WKQHRALEETEEDHEEDPMMYPEMELESEKENKDQNQSKPKHDGRRRIREDGLMKIYVLQ\n"); 
    lines5.add("     LLARTVFEVGFLVGQYFLYGFQVHPFYVCSRLPCPHKIDCFISRPTEKTIFLLIMYGVTG\n"); 
    lines5.add("     LCLLLNIWEMLHLGFGTIRDSLNSKRRELEDPGAYNYPFTWNTPSAPPGYNIAVKPDQIQ\n"); 
    lines5.add("     YTELSNAKIAYKQNKANIAQEQQYGSHEDNLPPDLETLQREIRMAQERLDLAIQAYNHQN\n"); 
    lines5.add("     NPHGSREKKAKVGSKAGSNKSSASSKSGDGKTSVWI");
 
    List<String> lines6 = new ArrayList<String>();
    lines6.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    lines6.add("AC   Q6GZX4;");
    lines6.add("FT   TOPO_DOM     1    29       Cytoplasmic (Potential).");
    lines6.add("FT   TRANSMEM    30    50       Helical; (Potential).");
    lines6.add("FT   TOPO_DOM    51   256       Extracellular (Potential).");
    lines6.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    lines6.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    lines6.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    lines6.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    lines6.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    lines6.add("     SFRKIYTDLG WKFTPL");
    lines6.add("//");
    
    // same as protein 2 but some fuzzy values
    List<String> lines7 = new ArrayList<String>();
    lines7.addAll(lines2);
    lines7.set(2, "FT   TRANSMEM   >12    21       Helical; (Potential).");
    lines7.set(3, "FT   TRANSMEM    23   <25       Helical; (Potential).");
    // same as protein 6 but some fuzzy values
    List<String> lines8 = new ArrayList<String>();
    lines8.addAll(lines6);
    lines8.set(2, "FT   TOPO_DOM     1   >29       Cytoplasmic (Potential).");

    // same as protein 5 but has intramembrane (membrane part not crossing 
    // the membrane but curl back to the same side)
    List<String> lines9 = new ArrayList<String>();
    lines9.addAll(lines5);
    lines9.set(1, "AC   Q2HJ99; Q2HJ68;\n");
    lines9.set(9, "FT   TOPO_DOM      1     22       Extracellular (Potential).\\n");
    lines9.set(10, "FT   INTRAMEM     23     45       Helical; (Potential).\\n");

    
    protein1.setLines(lines1);
    protein2.setLines(lines2);
    protein3.setLines(lines3);
    protein4.setLines(lines4);
    protein5.setLines(lines5);
    protein6.setLines(lines6);
    protein7.setLines(lines7);
    protein8.setLines(lines8);
    protein9.setLines(lines9);

    ret.add(protein1);
    ret.add(protein2);
    ret.add(protein3);
    ret.add(protein4);
    ret.add(protein5);
    ret.add(protein6);
    ret.add(protein7);
    ret.add(protein8);
    ret.add(protein9);

    return ret;
  }
}

