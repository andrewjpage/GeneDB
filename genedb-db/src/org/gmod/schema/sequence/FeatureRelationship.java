package org.gmod.schema.sequence;


import static javax.persistence.GenerationType.SEQUENCE;

import org.gmod.schema.cv.CvTerm;
import org.gmod.schema.utils.propinterface.PropertyI;

import org.hibernate.annotations.Filter;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="feature_relationship")
@Filter(name="excludeObsoleteFeatures", condition="2 = (select count(*) from feature where feature.feature_id in (subject_id, object_id) and not feature.is_obsolete)")
public class FeatureRelationship implements Serializable,PropertyI {

    // Fields

    @Id @GeneratedValue(strategy=SEQUENCE, generator="generator")
    @Column(name = "feature_relationship_id", unique = false, nullable = false, insertable = true, updatable = true)
    @SequenceGenerator(name = "generator", sequenceName = "feature_relationship_feature_relationship_id_seq")
    private int featureRelationshipId;

    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", unique = false, nullable = false, insertable = true, updatable = true)
    private Feature featureBySubjectId;

    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "object_id", unique = false, nullable = false, insertable = true, updatable = true)
    private Feature featureByObjectId;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", unique = false, nullable = false, insertable = true, updatable = true)
    private CvTerm cvTerm;

    @Column(name = "value", unique = false, nullable = true, insertable = true, updatable = true)
    private String value;

    @Column(name = "rank", unique = false, nullable = false, insertable = true, updatable = true)
    private int rank;

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "featureRelationship")
    private Collection<FeatureRelationshipProp> featureRelationshipProps;

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "featureRelationship")
    private Collection<FeatureRelationshipPub> featureRelationshipPubs;

    // Constructors

    /** default constructor */
    private FeatureRelationship() {
    	// Deliberately empty default constructor
    }

    /** minimal constructor */
    public FeatureRelationship(Feature featureBySubjectId, Feature featureByObjectId, CvTerm cvTerm, int rank) {
        this.featureBySubjectId = featureBySubjectId;
        this.featureByObjectId = featureByObjectId;
        this.cvTerm = cvTerm;
        this.rank = rank;
    }


    // Property accessors

    private int getFeatureRelationshipId() {
        return this.featureRelationshipId;
    }

    private void setFeatureRelationshipId(int featureRelationshipId) {
        this.featureRelationshipId = featureRelationshipId;
    }

    public Feature getFeatureBySubjectId() {
        return this.featureBySubjectId;
    }

    void setFeatureBySubjectId(Feature featureBySubjectId) {
        this.featureBySubjectId = featureBySubjectId;
    }

    public Feature getFeatureByObjectId() {
        return this.featureByObjectId;
    }

    public void setFeatureByObjectId(final Feature featureByObjectId) {
        this.featureByObjectId = featureByObjectId;
    }

    public CvTerm getCvTerm() {
        return this.cvTerm;
    }

    private void setCvTerm(final CvTerm cvTerm) {
        this.cvTerm = cvTerm;
    }

    private String getValue() {
        return this.value;
    }

    private void setValue(final String value) {
        this.value = value;
    }

    private int getRank() {
        return this.rank;
    }

    private void setRank(final int rank) {
        this.rank = rank;
    }

    private Collection<FeatureRelationshipProp> getFeatureRelationshipProps() {
        return featureRelationshipProps;
    }

    private void setFeatureRelationshipProps(Collection<FeatureRelationshipProp> featureRelationshipProps) {
        this.featureRelationshipProps = featureRelationshipProps;
    }

    private Collection<FeatureRelationshipPub> getFeatureRelationshipPubs() {
        return featureRelationshipPubs;
    }

    private void setFeatureRelationshipPubs(Collection<FeatureRelationshipPub> featureRelationshipPubs) {
        this.featureRelationshipPubs = featureRelationshipPubs;
    }
}


