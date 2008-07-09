package org.gmod.schema.sequence;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.genedb.db.helpers.LocationBridge;
import org.gmod.schema.analysis.AnalysisFeature;
import org.gmod.schema.cv.CvTerm;
import org.gmod.schema.general.DbXRef;
import org.gmod.schema.organism.Organism;
import org.gmod.schema.phylogeny.Phylonode;
import org.gmod.schema.utils.CollectionUtils;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_id")
@Table(name = "feature")
@FilterDef(name="excludeObsoleteFeatures")
@Filter(name="excludeObsoleteFeatures", condition="not is_obsolete")
@Indexed
public class Feature implements java.io.Serializable {

    @GenericGenerator(name = "generator", strategy = "seqhilo", parameters = {
            @Parameter(name = "max_lo", value = "100"),
            @Parameter(name = "sequence", value = "feature_feature_id_seq") })
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "feature_id", unique = false, nullable = false, insertable = true, updatable = true)
    @DocumentId
    private int featureId;

    @ManyToOne(cascade = {})
    @JoinColumn(name = "organism_id", unique = false, nullable = false, insertable = true, updatable = true)
    @IndexedEmbedded(depth = 1)
    private Organism organism;

    @ManyToOne(cascade = {})
    @JoinColumn(name = "type_id", unique = false, nullable = false, insertable = false, updatable = false)
    @IndexedEmbedded(depth = 2)
    private CvTerm cvTerm;

    @Column(name = "name", unique = false, nullable = true, insertable = true, updatable = true)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    private String name;

    @Column(name = "uniquename", unique = false, nullable = false, insertable = true, updatable = true)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    private String uniqueName;

    @Column(name = "seqlen", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer seqLen = -1;

    @Column(name = "md5checksum", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
    private String md5Checksum;

    @Column(name = "is_analysis", unique = false, nullable = false, insertable = true, updatable = true)
    private boolean analysis;

    @Column(name = "is_obsolete", unique = false, nullable = false, insertable = true, updatable = true)
    private boolean obsolete;

    @Column(name = "timeaccessioned", unique = false, nullable = false, insertable = true, updatable = true, length = 29)
    private Timestamp timeAccessioned;

    @Column(name = "timelastmodified", unique = false, nullable = false, insertable = true, updatable = true, length = 29)
    private Timestamp timeLastModified;

    // -------------------------------------------------------------------------------
    // Unsorted properties below here

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "feature")
    private Collection<Phylonode> phylonodes;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "dbxref_id", unique = false, nullable = true, insertable = true, updatable = true)
    private DbXRef dbXRef;

    @Column(name = "residues", unique = false, nullable = true, insertable = true, updatable = true)
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.genedb.db.helpers.TextByteType")
    private byte[] residues;

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "featureBySrcFeatureId")
    private Collection<FeatureLoc> featureLocsForSrcFeatureId;

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "featureByObjectId")
    private Collection<FeatureRelationship> featureRelationshipsForObjectId;

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "featureBySubjectId")
    private Collection<FeatureRelationship> featureRelationshipsForSubjectId;

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "feature")
    private Collection<FeatureDbXRef> featureDbXRefs;

    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "featureByFeatureId")
    @OrderBy("rank ASC")
    private List<FeatureLoc> featureLocsForFeatureId;

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "feature")
    private Collection<FeatureCvTerm> featureCvTerms;

    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "feature")
    // @Cascade( {CascadeType.ALL, CascadeType.DELETE_ORPHAN} )
    private Collection<FeatureProp> featureProps;

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "feature")
    private Collection<FeaturePub> featurePubs;

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "feature")
    private Collection<AnalysisFeature> analysisFeatures;

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "feature")
    private Collection<FeatureSynonym> featureSynonyms;

    /**
     * This featureLoc field does not participate in the Hibernate mapping. It's
     * provided as a convenience for the client, and can be used to cache a
     * FeatureLoc of interest, but is neither automatically populated nor
     * persisted. It is (at the time of writing) used only by Artemis.
     */
    @Transient
    private FeatureLoc featureLoc;

    @Transient
    private Logger logger = Logger.getLogger(Feature.class);

    // Constructors

    /** default constructor */
    public Feature() {
    }

    /** minimal constructor */
    public Feature(Organism organism, CvTerm cvTerm, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp timeAccessioned, Timestamp timeLastModified) {
        this.organism = organism;
        this.cvTerm = cvTerm;
        this.uniqueName = uniqueName;
        this.analysis = analysis;
        this.obsolete = obsolete;
        this.timeAccessioned = timeAccessioned;
        this.timeLastModified = timeLastModified;
    }

    // Property accessors

    public int getFeatureId() {
        return this.featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public Organism getOrganism() {
        return this.organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }

    public CvTerm getCvTerm() {
        return this.cvTerm;
    }

    public void setCvTerm(CvTerm cvTerm) {
        this.cvTerm = cvTerm;
    }

    public DbXRef getDbXRef() {
        return this.dbXRef;
    }

    public void setDbXRef(DbXRef dbXRef) {
        this.dbXRef = dbXRef;
    }

    /**
     * Get the human-readable form of the feature eg the gene name
     *
     * @return the name, may be null
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the human-readable form of the feature eg the gene name
     *
     * @param name the human-readable name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Fetch the unique name (systematic id) for the feature
     *
     * @return the unique name, not null
     */
    public String getUniqueName() {
        return this.uniqueName;
    }

    /**
     * Set the unique name (systematic id) for the feature
     *
     * @param uniqueName the unique name, not null
     */
    public void setUniqueName(String uniqueName) {
        if (uniqueName == null)
            throw new IllegalArgumentException("setUniqueName: the unique name cannot be null");
        this.uniqueName = uniqueName;
    }

    public byte[] getResidues() {
        return this.residues;
    }

    /**
     * Fetch a subset of the sequence (may be lazy)
     *
     * @param min the lower bound, in interbase coordinates
     * @param max the upper bound, in interbase coordinates
     * @return
     */
    public byte[] getResidues(int min, int max) {
        int length = max - min;
        byte[] results = new byte[length+1];
        System.arraycopy(getResidues(), min,results,0,length);
        return results;
    }

    public void setResidues(byte[] residues) {
        this.residues = residues;
        if (residues == null) {
            seqLen = 0;
            md5Checksum = "";
            return;
        }
        seqLen = residues.length;
        this.md5Checksum = calcMD5(this.residues);
    }

    /**
     * Fetch the length of the sequence. Find it from the parent if necessary
     *
     * @return the length
     */
    public int getSeqLen() {
        if (this.seqLen.intValue() == -1 && residues != null) {
            return getResidues().length;
        }
        return this.seqLen.intValue();
    }

    public void setSeqLen(Integer seqLen) {
        this.seqLen = seqLen;
    }

    public String getMd5Checksum() {
        return this.md5Checksum;
    }

    public void setMd5Checksum(String md5Checksum) {
        this.md5Checksum = md5Checksum;
    }

    public boolean isAnalysis() {
        return this.analysis;
    }

    public void setAnalysis(boolean analysis) {
        this.analysis = analysis;
    }

    public boolean isObsolete() {
        return this.obsolete;
    }

    public void setObsolete(boolean obsolete) {
        this.obsolete = obsolete;
    }

    public Date getTimeAccessioned() {
        return this.timeAccessioned;
    }

    public void setTimeAccessioned(Timestamp timeAccessioned) {
        this.timeAccessioned = timeAccessioned;
    }

    public Timestamp getTimeLastModified() {
        return this.timeLastModified;
    }

    public void setTimeLastModified(Timestamp timeLastModified) {
        this.timeLastModified = timeLastModified;
    }

    public Collection<FeatureLoc> getFeatureLocsForSrcFeatureId() {
        return (featureLocsForSrcFeatureId = CollectionUtils.safeGetter(featureLocsForSrcFeatureId));
    }

    /**
     * Returns the unique rank=0 FeatureLoc associated with this feature. Every
     * feature should have one, so this method will not return null unless
     * something is wrong in the database.
     *
     * @return the unique rank=0 FeatureLoc associated with this feature
     */
    @Transient
    public FeatureLoc getRankZeroFeatureLoc() {
        List<FeatureLoc> featureLocs = getFeatureLocsForFeatureId();
        if (featureLocs.size() == 0) {
            logger.error(String.format("getRankZeroFeatureLoc: Feature '%s' has no FeatureLocs",
                uniqueName));
            return null;
        }
        for (FeatureLoc featureLoc : featureLocs) {
            if (featureLoc == null)
                logger.warn(String.format("Feature '%s' has a null featureLoc", uniqueName));
            else
                return featureLoc;
        }
        logger.error(String.format("Feature '%s' has no non-null featureLocs", uniqueName));
        return null;
    }

    public void setFeatureLocsForSrcFeatureId(Collection<FeatureLoc> featureLocsForSrcFeatureId) {
        this.featureLocsForSrcFeatureId = featureLocsForSrcFeatureId;
    }

    public Collection<FeatureRelationship> getFeatureRelationshipsForObjectId() {
        return (featureRelationshipsForObjectId = CollectionUtils
                .safeGetter(featureRelationshipsForObjectId));
    }

    public void setFeatureRelationshipsForObjectId(
            Collection<FeatureRelationship> featureRelationshipsForObjectId) {
        this.featureRelationshipsForObjectId = featureRelationshipsForObjectId;
    }

    public Collection<FeatureRelationship> getFeatureRelationshipsForSubjectId() {
        return (featureRelationshipsForSubjectId = CollectionUtils
                .safeGetter(featureRelationshipsForSubjectId));
    }

    public void setFeatureRelationshipsForSubjectId(
            Collection<FeatureRelationship> featureRelationshipsForSubjectId) {
        this.featureRelationshipsForSubjectId = featureRelationshipsForSubjectId;
    }

    public void addFeatureRelationshipsForSubjectId(
            FeatureRelationship featureRelationshipForSubjectId) {
        featureRelationshipForSubjectId.setFeatureBySubjectId(this);
        this.featureRelationshipsForSubjectId.add(featureRelationshipForSubjectId);
    }

    public Collection<FeatureDbXRef> getFeatureDbXRefs() {
        return this.featureDbXRefs;
    }

    public void setFeatureDbXRefs(Collection<FeatureDbXRef> featureDbXRefs) {
        this.featureDbXRefs = featureDbXRefs;
    }

    public void addFeatureDbXRef(FeatureDbXRef featureDbXRef) {
        if (this.featureDbXRefs == null)
            this.featureDbXRefs = new HashSet<FeatureDbXRef>();
        this.featureDbXRefs.add(featureDbXRef);
        featureDbXRef.setFeature(this);
    }

    public List<FeatureLoc> getFeatureLocsForFeatureId() {
        return (featureLocsForFeatureId = CollectionUtils.safeGetter(featureLocsForFeatureId));
    }

    public void addFeatureLocsForFeatureId(FeatureLoc featureLocForFeatureId) {
        featureLocForFeatureId.setFeatureByFeatureId(this);
        getFeatureLocsForFeatureId().add(featureLocForFeatureId);
    }

    public Collection<FeatureCvTerm> getFeatureCvTerms() {
        return this.featureCvTerms;
    }

    public void addFeatureCvTerm(FeatureCvTerm featureCvTerm) {
        if (this.featureCvTerms == null) {
            featureCvTerms = new HashSet<FeatureCvTerm>();
        }
        featureCvTerms.add(featureCvTerm);
    }

    public void setFeatureCvTerms(Collection<FeatureCvTerm> featureCvTerms) {
        this.featureCvTerms = featureCvTerms;
    }

    public Collection<FeatureProp> getFeatureProps() {
        return (featureProps = CollectionUtils.safeGetter(featureProps));
    }

    public void addFeatureProp(FeatureProp featureProp) {
        featureProp.setFeature(this);
        getFeatureProps().add(featureProp);
    }

    public Collection<FeaturePub> getFeaturePubs() {
        return this.featurePubs;
    }

    public void setFeaturePubs(Collection<FeaturePub> featurePubs) {
        this.featurePubs = featurePubs;
    }

    public Collection<AnalysisFeature> getAnalysisFeatures() {
        return this.analysisFeatures;
    }

    public void setAnalysisFeatures(Collection<AnalysisFeature> analysisFeatures) {
        this.analysisFeatures = analysisFeatures;
    }

    public Collection<FeatureSynonym> getFeatureSynonyms() {
        return (featureSynonyms = CollectionUtils.safeGetter(featureSynonyms));
    }

    public void setFeatureSynonyms(Collection<FeatureSynonym> featureSynonyms) {
        this.featureSynonyms = featureSynonyms;
    }

    /**
     * Get the display name for the gene: preferably the name, otherwise the
     * display name
     *
     * @return the preferred display name, never null
     */
    @Transient
    public String getDisplayName() {
        return (getName() != null) ? getName() : getUniqueName();
    }

    /**
     * Get the current systematic ID or temporary systematic ID, if there is one.
     * If not, returns the unique name.
     *
     * @return the systematic ID, temporary systematic ID, or unique name
     */
    @Transient
    public String getSystematicId() {
        for (FeatureSynonym featureSynonym: getFeatureSynonyms()) {
            Synonym synonym = featureSynonym.getSynonym();
            if (("systematic_id".equals(synonym.getCvTerm().getName())
            || "temporary_systematic_id".equals(synonym.getCvTerm().getName()))
            && featureSynonym.isCurrent())
                return synonym.getSynonymSgml();
        }
        return getUniqueName();
    }

    @Transient
    public Collection<String> getPreviousSystematicIds() {
        Set<String> ret = new HashSet<String>();
        for (FeatureSynonym featureSynonym: getFeatureSynonyms()) {
            Synonym synonym = featureSynonym.getSynonym();
            if (("systematic_id".equals(synonym.getCvTerm().getName())
            || "temporary_systematic_id".equals(synonym.getCvTerm().getName()))
            && !featureSynonym.isCurrent())
                ret.add(synonym.getSynonymSgml());
        }
        return ret;
    }

    public void setFeatureLocsForFeatureId(List<FeatureLoc> featureLocsForFeatureId) {
        this.featureLocsForFeatureId = featureLocsForFeatureId;
    }

    public void setFeatureProps(Collection<FeatureProp> featureProps) {
        this.featureProps = featureProps;
    }

    public Collection<Phylonode> getPhylonodes() {
        return this.phylonodes;
    }

    public void setPhylonodes(Collection<Phylonode> phylonodes) {
        this.phylonodes = phylonodes;
    }

    private String calcMD5(byte[] residue) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(residue);

            StringBuilder hexValue = new StringBuilder();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = md5Bytes[i] & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (NoSuchAlgorithmException exp) {
            // Shouldn't happen - MD5 is a supported algorithm
            throw new RuntimeException("Could not find MD5 algorithm", exp);
        }
    }

    /**
     * Returns the value of the featureLoc field.
     *
     * This is probably not what you want: unless you are doing something
     * special, use the {@link #getRankZeroFeatureLoc()} method.
     *
     * This featureLoc field does not participate in the Hibernate mapping. It's
     * provided as a convenience for the client, and can be used to cache a
     * FeatureLoc of interest, but is neither automatically populated nor
     * persisted. It is (at the time of writing) used only by Artemis.
     */
    public FeatureLoc getFeatureLoc() {
        return featureLoc;
    }

    /**
     * Sets the value of the featureLoc field.
     *
     * This is probably not what you were looking for: see
     * {@link #setFeatureLocsForFeatureId()}.
     *
     * This featureLoc field does not participate in the Hibernate mapping. It's
     * provided as a convenience for the client, and can be used to cache a
     * FeatureLoc of interest, but is neither automatically populated nor
     * persisted. It is (at the time of writing) used only by Artemis.
     */
    public void setFeatureLoc(FeatureLoc featureLoc) {
        this.featureLoc = featureLoc;
    }

    @Transient
    @Field(name = "synonym", index = Index.TOKENIZED, store = Store.YES)
    public String getSynonymsAsTabSeparatedString() {
        StringBuilder ret = new StringBuilder();
        boolean first = true;
        for (FeatureSynonym featureSynonym : getFeatureSynonyms()) {
            if (first)
                first = false;
            else
                ret.append('\t');

           ret.append(featureSynonym.getSynonym().getName());
        }
        return ret.toString();
    }

    /**
     * A string containing all the names by which this feature is known;
     * indexed by Lucene and used for searches.
     *
     * @return
     */
    @Transient
    @Field(name = "allNames", index = Index.TOKENIZED, store = Store.NO)
    public String getAllNames() {
        StringBuilder allNames = new StringBuilder();
        if (getName() != null) {
            allNames.append(getName());
            allNames.append(' ');
        }
        allNames.append(getDisplayName());
        allNames.append(' ');
        allNames.append(getSynonymsAsTabSeparatedString());
        return allNames.toString();
    }

    @Transient
    @Field(name = "start", store = Store.YES)
    @FieldBridge(impl = LocationBridge.class)
    public int getStart() {
        return getRankZeroFeatureLoc().getFmin();
    }

    @Transient
    @Field(name = "stop", store = Store.YES)
    @FieldBridge(impl = LocationBridge.class)
    public int getStop() {
        return getRankZeroFeatureLoc().getFmax();
    }

    @Transient
    @Field(name = "strand", index=Index.UN_TOKENIZED, store = Store.YES)
    public int getStrand() {
        FeatureLoc loc = getRankZeroFeatureLoc();
        if (loc == null)
            return 0;
        return loc.getStrand();
    }

    @Transient
    @Field(name = "chr", index=Index.UN_TOKENIZED, store = Store.YES)
    public String getChr() {
        FeatureLoc loc = getRankZeroFeatureLoc();
        if (loc == null)
            return null;
        return loc.getFeatureBySrcFeatureId().getUniqueName();
    }

    @Transient
    @Field(name = "chrlen", store = Store.YES)
    public int getChrLen() {
        FeatureLoc loc = getRankZeroFeatureLoc();
        if (loc == null)
            return 0;
        return loc.getFeatureBySrcFeatureId().getSeqLen();
    }
}
