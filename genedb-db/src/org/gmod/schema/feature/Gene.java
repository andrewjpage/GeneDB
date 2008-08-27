package org.gmod.schema.feature;

import org.gmod.schema.cfg.FeatureType;
import org.gmod.schema.mapped.Feature;
import org.gmod.schema.mapped.Organism;
import org.gmod.schema.utils.StrandedLocation;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@FeatureType(cv="sequence", term="gene")
@Indexed
public class Gene extends AbstractGene {

    Gene () {
        // empty
    }

    public Gene(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

    @Transient
    public Collection<MRNA> getCodingTranscripts() {
        Collection<MRNA> ret = new ArrayList<MRNA>();

        for (Transcript transcript : getTranscripts()) {
            if (transcript instanceof MRNA) {
                ret.add((MRNA) transcript);
            }
        }

        return ret;
    }

    @Transient @Field(name = "protein", store = Store.YES)
    public String getProteinUniqueNamesTabSeparated() {
        StringBuilder ret = new StringBuilder();
        boolean first = true;
        for (ProductiveTranscript transcript: getCodingTranscripts()) {
            if (first) {
                first = false;
            } else {
                ret.append('\t');
            }
            ret.append(transcript.getProteinUniqueName());
        }
        return ret.toString();
    }

    @Override
    @Transient @Field(name = "product", index = Index.TOKENIZED, store = Store.YES)
    public String getProductsAsTabSeparatedString() {
        StringBuilder products = new StringBuilder();

        boolean first = true;
        for (ProductiveTranscript transcript : getCodingTranscripts()) {
            if (first) {
                first = false;
            } else {
                products.append('\t');
            }
            products.append(transcript.getProductsAsTabSeparatedString());
        }
        return products.toString();
    }


    public static Gene make(Feature sourceFeature, StrandedLocation location, String uniqueName, Timestamp now) {
        Gene gene = new Gene(sourceFeature.getOrganism(), uniqueName, false, false, now);
        sourceFeature.addLocatedChild(gene, location);
        return gene;
    }

    public void addTranscript(Transcript transcript) {
        addFeatureRelationship(transcript, "relationship", "part_of");
    }
}
