package protka.annotations;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import protka.Protein;
import protka.annotations.Annotation;
import protka.annotations.Annotation.Type;
import protka.annotations.AnnotationListMerger;


public class SwissprotAnnotParser {
  TransmemAnnotation annotation;
  private final static Logger LOGGER = 
      Logger.getLogger(SwissprotAnnotParser.class.getName());


  public TransmemAnnotation getAnnotations(Protein p) {
    String annotString = parseAnnotStringFromProtein(p);
    
    TransmemAnnotation annotation = new TransmemAnnotation(annotString);
    if (!annotation.isValid()) {
      LOGGER.warning("SWISSPROT annot was not valid: " + p.getAcNum());
    }
    return annotation;
  }
  
  public String parseAnnotStringFromProtein(Protein p) {
    List<Annotation> tmParts = readTmDomains(p);
    List<Annotation> inParts = readInnerDomains(p);
    List<Annotation> outParts = readOuterDomains(p);
    List<List<Annotation> > listOflists =
        new ArrayList<List<Annotation> >();
    listOflists.add(tmParts);
    listOflists.add(inParts);
    listOflists.add(outParts);
    AnnotationListMerger merger = new AnnotationListMerger();
    List<Annotation> allParts = merger.mergeParts(listOflists);
    StringBuilder sb = new StringBuilder();
    for (Annotation a : allParts) {
      sb.append(Annotation.typeToChar(a.type) + ":");
      if (a.hasFuzzyStart()) {
        sb.append("~");
      }
      sb.append(a.from + "-");
      if (a.hasFuzzyEnd()) {
        sb.append("~");
      }      
      sb.append(a.to + ";");
    }
    //System.out.println(sb.toString());
    return sb.toString();
  }

  private List<Annotation> readTmDomains(Protein p) {
    List<String> ftLinesTm = p.getLines("FT   TRANSMEM" , "");
    List<String> ftLinesIm = p.getLines("FT   INTRAMEM" , "");
    List<Annotation> tmParts = parseDomainParts(ftLinesTm, Type.TRANSMEM);
    List<Annotation> imParts = parseDomainParts(ftLinesIm, Type.TRANSMEM);
    List<Annotation> merged = TransmemAnnotation.mergeAnnots(tmParts, imParts);
    return merged;
  }

  private List<Annotation> readInnerDomains(Protein p) {
    List<String> ftLines = p.getLines("FT   TOPO_DOM", "Cytoplasmic");
    List<Annotation> tmParts = parseDomainParts(ftLines, Type.INNER);
    return tmParts;
  }
  
  private List<Annotation> readOuterDomains(Protein p) {
    List<String> ftLines = p.getLines("FT   TOPO_DOM", "Extracellular");
    List<Annotation> tmParts = parseDomainParts(ftLines, Type.OUTER);
    return tmParts;
  }
  
  private List<Annotation> parseDomainParts(List<String> lines, Type type) {
    ArrayList<Annotation> ret = new ArrayList<Annotation>();
    for (String line : lines) {
      String fromStr = line.substring(15, 20).trim();
      String toStr = line.substring(21, 27).trim();
      boolean fuzzyStart = false;
      boolean fuzzyEnd = false;
      if (fromStr.contains("?") || toStr.contains("?")) {
        continue;
      }
      if (fromStr.startsWith("<") || fromStr.startsWith(">")) {
        fromStr = fromStr.substring(1);
        fuzzyStart = true;
      }
      if (toStr.startsWith("<") || toStr.startsWith(">")) {
        toStr = toStr.substring(1);
        fuzzyEnd = true;
      }
      int from = Integer.parseInt(fromStr); 
      int to = Integer.parseInt(toStr);
      if (fuzzyStart || fuzzyEnd) {
        ret.add(new Annotation(type, from, to, fuzzyStart, fuzzyEnd));
      } else {
        ret.add(new Annotation(type, from, to));
      }
    }
    return ret;
  }
}
