package org.genedb.web.mvc.controller.download;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.genedb.db.dao.SequenceDao;

import org.gmod.schema.feature.AbstractExon;
import org.gmod.schema.feature.AbstractGene;
import org.gmod.schema.feature.Exon;
import org.gmod.schema.feature.Gene;
import org.gmod.schema.feature.MRNA;
import org.gmod.schema.feature.ProductiveTranscript;
import org.gmod.schema.feature.Transcript;

public class DownloadUtils {

	private static int FEATURE_PREFIX_WIDTH = 22;
	private static int MAX_FEATURE_WIDTH = 18;
	private static final String FEATURE_TABLE_PREFIX = String.format("%-"+FEATURE_PREFIX_WIDTH+"s", "FT");
	private static final int FASTA_WIDTH = 60;
	private static final int BASES_WIDTH = 10;

	public static void writeFasta(PrintWriter out, String header, String sequence) {
		out.print("> ");
		out.println(header);
		
		int startPos = 0;
		int sequenceLen = sequence.length();
		while (startPos < sequenceLen) {
			int endPos = startPos + FASTA_WIDTH;
			if (endPos > sequenceLen) {
				endPos = sequenceLen;
			}
			out.println(sequence.substring(startPos, endPos));
			startPos += FASTA_WIDTH;
		}
		
	}
	
   public static String writeFasta(String header, String sequence) {
       StringBuilder fasta = new StringBuilder(); 
       fasta.append("> ");
       fasta.append(header);
       fasta.append("\n");
        
        int startPos = 0;
        int sequenceLen = sequence.length();
        while (startPos < sequenceLen) {
            int endPos = startPos + BASES_WIDTH;
            if (endPos > sequenceLen) {
                endPos = sequenceLen;
            }
            
            fasta.append(sequence.substring(startPos, endPos));
            fasta.append(" ");
            startPos += BASES_WIDTH;
            if(startPos % 60 == 0) {
                fasta.append("\n");
            }
        }
        
        return fasta.toString();
    }
	
	public static void writeEmblEntry(PrintWriter out, String featureType, 
			boolean forwardStrand, int min, int max,
			Map<String, String> qualifiers) {

		if (featureType.length() > MAX_FEATURE_WIDTH) {
			featureType = featureType.substring(0, MAX_FEATURE_WIDTH);
		}
		
		out.format("FT %-"+(FEATURE_PREFIX_WIDTH-3)+"s", featureType);
		if (!forwardStrand) {
			out.print("complement(");
		}
		
		out.print(min - 1 +".."+max); // Interbase conversion
		
		if (!forwardStrand) {
			out.print(")");
		}
		out.println();
		
		for (Map.Entry<String, String> qualifier: qualifiers.entrySet()) {
			out.println(FEATURE_TABLE_PREFIX+"/"+qualifier.getKey()+"=\""+qualifier.getValue()+"\"");
		}
		
	}
	
	public static String writeEmblEntry(String featureType, 
            boolean forwardStrand, int min, int max,
            Map<String, String> qualifiers) {

        StringBuilder embl = new StringBuilder();
	    
	    if (featureType.length() > MAX_FEATURE_WIDTH) {
            featureType = featureType.substring(0, MAX_FEATURE_WIDTH);
        }
        
        embl.append(String.format("FT %-"+(FEATURE_PREFIX_WIDTH-3)+"s", featureType));
        if (!forwardStrand) {
            embl.append("complement(");
        }
        
        embl.append(min - 1 +".."+max); // Interbase conversion
        
        if (!forwardStrand) {
            embl.append(")");
        }
        embl.append("\n");
        
        for (Map.Entry<String, String> qualifier: qualifiers.entrySet()) {
            embl.append(FEATURE_TABLE_PREFIX+"/"+qualifier.getKey()+"=\""+qualifier.getValue()+"\"");
            embl.append("\n");
        }
     
        return embl.toString();
    }
	
	public static String getSequence(AbstractGene gene,SequenceType sequenceType) {
	    String sequence = null;
	    boolean alternateSpliced = false;
	    Collection<Transcript> transcripts = gene.getTranscripts();
	    ProductiveTranscript transcript = null;
	    
	    if(transcripts.size() > 1 ) {
	        alternateSpliced = true;
	    } else {
	       Transcript t = transcripts.iterator().next();
	       if(t instanceof ProductiveTranscript) {
	           transcript = (ProductiveTranscript) t;
	       }
	    }
        
	    if(!alternateSpliced && transcript!=null) {
    	    switch (sequenceType) {
    	        case SPLICED_DNA:
    	            if(transcript.getResidues() != null) {
    	                sequence = new String(transcript.getResidues());
    	            }
    	            break;
    	        case UNSPLICED_DNA:
    	            if(transcript.getResidues() != null) {
    	                sequence = new String(gene.getResidues());
    	            }
    	            break;
    	        case PROTEIN:
    	            if(transcript.getProtein().getResidues() != null) {
    	                sequence = new String(transcript.getProtein().getResidues());
    	            }
    	            break;
    	        case INTRON:
    	            Object[] exons = transcript.getExons().toArray();
    	            if(exons.length > 1) {
        	            sequence = new String();
    	                for(int i=0;i<exons.length - 1;i++) {
        	                AbstractExon exon = (AbstractExon) exons[i];
        	                int start = exon.getStart();
        	                int stop = exon.getStop();
        	                String str = new String(gene.getRankZeroFeatureLoc().getSourceFeature()
        	                        .getResidues(start, stop));
        	                sequence = sequence.concat(str);   
        	            }
    	            }
    	            break;
    	    }
	    }
	    
	    return sequence;
	}
}
