package org.genedb.querying.tmpquery;

import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.ConstantScoreRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.genedb.querying.core.QueryClass;
import org.genedb.querying.core.QueryParam;

//import org.hibernate.validator.Min;
import org.springframework.validation.Errors;

@QueryClass(
        title="Coding and pseudogenes by protein mass",
        shortDesc="Get a list of transcripts ",
        longDesc=""
    )
public class ProteinMassQuery extends OrganismLuceneQuery {

    @QueryParam(
            order=1,
            title="Minimum mass of protein"
    )
    private int min = 10000;

    @QueryParam(
            order=2,
            title="Maximum mass of protein"
    )
    private int max = 50000;

    private String type = "polypeptide";


    @Override
    protected String getluceneIndexName() {
        return "org.gmod.schema.mapped.Feature";
    }
    
    @Override
    public String getQueryDescription() {
    	return "Returns proteins of a certain mass.";
    }
    
    @Override
    public String getQueryName() {
        return "Molecular Mass";
    }

    @Override
    protected void getQueryTermsWithoutOrganisms(List<Query> queries) {
        //Get the range
        queries.add(
                new ConstantScoreRangeQuery(
                        "mass",
                        String.format("%06d",  min),
                        String.format("%06d",  max),
                        true, true));

        //Get the type
        queries.add(
                new TermQuery(
                        new Term("type.name", type)));

    }


    // ------ Autogenerated code below here

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    protected String[] getParamNames() {
        return new String[] {"min", "max"};
    }


    @Override
    protected void extraValidation(Errors errors) {

        //validate dependent properties
        if (!errors.hasErrors()) {
            if (getMin() > getMax()) {
                errors.reject("min.greater.than.max");
            }
        }
    }

}
