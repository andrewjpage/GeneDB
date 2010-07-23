package org.genedb.web.mvc.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.genedb.db.taxon.TaxonNode;
import org.genedb.db.taxon.TaxonNodeManager;
import org.springframework.util.StringUtils;

public class TaxonUtils {

    private static TaxonNodeManager tnm;

    public static String getTaxonListFromNodes(TaxonNode[] nodes) {
        return StringUtils.arrayToDelimitedString(nodes, " ");
    }

    public static String getOrgNamesInHqlFormat(TaxonNode[] nodes) {
        Set<String> orgNames = new HashSet<String>();
        for (TaxonNode node : nodes) {
            orgNames.addAll(node.getAllChildrenNames());
        }
        StringBuilder ret = new StringBuilder();
        boolean notFirst = false;
        for (String orgName : orgNames) {
            if (notFirst) {
                ret.append(", ");
            } else {
                notFirst = true;
            }
            ret.append('\'');
            ret.append(orgName);
            ret.append('\'');
        }
        return ret.toString();
    }

    public static Collection<String> getOrgNames(String org) {
        String sections[] = org.split(",");
        Set<String> orgNames = new HashSet<String>();
        for (String orgLabel : sections) {
            TaxonNode node = tnm.getTaxonNodeForLabel(orgLabel);
            if (node.getAllChildrenNames().size() > 0) {
                orgNames.addAll(node.getAllChildrenNames());
            } else {
                orgNames.add(node.getLabel());
            }
        }

        return orgNames;
    }

    public static String getOrgNamesInHqlFormat(String org) {
        StringBuilder ret = new StringBuilder();
        boolean notFirst = false;
        for (String orgName : getOrgNames(org)) {
            if (notFirst) {
                ret.append(", ");
            } else {
                notFirst = true;
            }
            ret.append('\'');
            ret.append(orgName);
            ret.append('\'');
        }
        return ret.toString();
    }

    public static Taxon getTaxonFromList(List<String> answers, int i) {
        // TODO Auto-generated method stub
        return new Taxon();
    }

    public void setTnm(TaxonNodeManager tnm) {
        TaxonUtils.tnm = tnm;
    }

}
