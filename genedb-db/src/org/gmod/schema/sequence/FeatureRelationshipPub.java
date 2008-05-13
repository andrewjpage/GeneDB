package org.gmod.schema.sequence;

import static javax.persistence.GenerationType.SEQUENCE;

import org.gmod.schema.pub.Pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "feature_relationship_pub")
public class FeatureRelationshipPub implements Serializable {

    // Fields
    @SequenceGenerator(name = "generator", sequenceName = "feature_relationship_pub_feature_relationship_pub_id_seq")
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "feature_relationship_pub_id", unique = false, nullable = false, insertable = true, updatable = true)
    private int featureRelationshipPubId;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id", unique = false, nullable = false, insertable = true, updatable = true)
    private Pub pub;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_relationship_id", unique = false, nullable = false, insertable = true, updatable = true)
    private FeatureRelationship featureRelationship;

    // Property accessors

    public int getFeatureRelationshipPubId() {
        return this.featureRelationshipPubId;
    }

    public void setFeatureRelationshipPubId(int featureRelationshipPubId) {
        this.featureRelationshipPubId = featureRelationshipPubId;
    }

    public Pub getPub() {
        return this.pub;
    }

    public void setPub(Pub pub) {
        this.pub = pub;
    }

    public FeatureRelationship getFeatureRelationship() {
        return this.featureRelationship;
    }

    public void setFeatureRelationship(FeatureRelationship featureRelationship) {
        this.featureRelationship = featureRelationship;
    }
}
