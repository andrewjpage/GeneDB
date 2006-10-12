package org.genedb.db.dao;

import org.gmod.schema.cv.Cv;
import org.gmod.schema.cv.CvTerm;
import org.gmod.schema.dao.CvDaoI;
import org.gmod.schema.general.Db;
import org.gmod.schema.general.DbXRef;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collection;
import java.util.List;

public class CvDao extends BaseDao implements CvDaoI {

    protected GeneralDao generalDao;

    /* (non-Javadoc)
     * @see org.genedb.db.dao.CvDaoI#getCvById(int)
     */
    public Cv getCvById(int id) {
	return (Cv) getHibernateTemplate().load(Cv.class, id);
    }

    /* (non-Javadoc)
     * @see org.genedb.db.dao.CvDaoI#getCvByName(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Cv> getCvByName(String name) {
	List<Cv> cvs = getHibernateTemplate().findByNamedParam(
		"from Cv cv where cv.name like :name", "name", name);
	return cvs;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.dao.CvDaoI#getCvTermById(int)
     */
    public CvTerm getCvTermById(int id) {
        return (CvTerm) getHibernateTemplate().load(CvTerm.class, id);
        }

        /* (non-Javadoc)
         * @see org.genedb.db.dao.CvDaoI#getCvTermByNameInCv(java.lang.String, org.genedb.db.hibernate3gen.Cv)
         */
        @SuppressWarnings("unchecked")
        public List<CvTerm> getCvTermByNameInCv(String cvTermName, Cv cv) {
        return getHibernateTemplate().findByNamedParam(
            "from CvTerm cvTerm where cvTerm.name like :cvTermName and cvTerm.cv = :cv",
            new String[]{"cvTermName", "cv"}, new Object[]{cvTermName, cv});
        }

        
        /* (non-Javadoc)
         * @see org.genedb.db.dao.CvDaoI#getGoCvTermByAcc(java.lang.String)
         */
        @SuppressWarnings("unchecked")
        public CvTerm getGoCvTermByAcc(String value) {
        List<CvTerm> terms = getHibernateTemplate().findByNamedParam(
            "from CvTerm cvTerm where cvTerm.dbxref.db.name='GO' and cvTerm.dbxref.accession=:acc", 
            "acc", value);
        return firstFromList(terms, "accession", value);
        }
        
        /* (non-Javadoc)
         * @see org.genedb.db.dao.CvDaoI#getGoCvTermByAccViaDb(java.lang.String)
         */
        @SuppressWarnings("unchecked")
        public CvTerm getGoCvTermByAccViaDb(final String id) {
            final Db DB_GO = generalDao.getDbByName("GO");
            
            // Find cvterm for db_xref
            TransactionTemplate tt = new TransactionTemplate(generalDao.getPlatformTransactionManager());
            CvTerm cvTerm = (CvTerm) tt.execute(
                    new TransactionCallback() {
                        public Object doInTransaction(TransactionStatus status) {
                            final DbXRef dbxref = generalDao.getDbXRefByDbAndAcc(DB_GO, id);
                            if (dbxref == null) {
                                logger.warn("Can't find dbxref for GO id of '"+id+"'");
                                return null;
                            }
                            Collection<CvTerm> cvTermDbXRefs = dbxref.getCvTerms();
                            if (cvTermDbXRefs.size() > 1) {
                                logger.warn("More than one cvTerm for go id of '"+id+"'");
                                return null;
                            }
                            if (cvTermDbXRefs.size() == 0) {
                                logger.warn("No cvTerm for go id of '"+id+"'");
                                return null;
                            }
                            return cvTermDbXRefs.iterator().next();
                        }
                    });
            return cvTerm;
        }

        public void setGeneralDao(GeneralDao generalDao) {
            this.generalDao = generalDao;
        }

		public List<CvTerm> getCvTerms() {
			// TODO Auto-generated method stub
			return null;
		}

}
