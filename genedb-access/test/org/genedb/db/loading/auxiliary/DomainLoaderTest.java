package org.genedb.db.loading.auxiliary;

import static org.junit.Assert.*;

import org.gmod.schema.feature.Polypeptide;
import org.gmod.schema.feature.PolypeptideDomain;
import org.gmod.schema.mapped.DbXRef;
import org.gmod.schema.mapped.FeatureDbXRef;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class DomainLoaderTest {
	private static final Logger logger = Logger.getLogger(DomainLoaderTest.class);
    private static DomainLoader loader;

    @BeforeClass
    public static void setup() throws IOException, HibernateException, SQLException, ClassNotFoundException, Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
            new String[] {"Load.xml", "AuxTest.xml"});

        loader = ctx.getBean("domainloader", DomainLoader.class);
       
        assertTrue(loader.processOptionIfValid("key-type", "polypeptide")); 
        assertTrue(loader.processOptionIfValid("program", "pfam_scan"));       
        loader.analysisProgramVersion = "unknown";
        loader.notFoundNotFatal = true;

        loader.clear("Pfalciparum", "pfam_scan");
        new Load(loader).load("test/data/Pfalciparum.pfam");
	
    }

    @Test
    public void testPfamDomains() {
    	
        SessionFactory sessionFactory = loader.getSessionFactory();
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        try {
            Polypeptide polypeptide = (Polypeptide) session.createQuery(
                "from Polypeptide where uniquename = 'MAL13P1.114:pep'"
            ).uniqueResult();

            assertNotNull(polypeptide);
            assertEquals("MAL13P1.114:pep", polypeptide.getUniqueName());

            SortedSet<PolypeptideDomain> allDomains = polypeptide.getDomains();
            assertNotNull(allDomains);
            SortedSet<PolypeptideDomain> pfamDomains =  new TreeSet<PolypeptideDomain>();
            
            for(PolypeptideDomain domain : allDomains) {
            	assertNotNull(domain);
            	if (domain.getAnalysisFeature() != null && domain.getAnalysisFeature().getAnalysis().getProgram().equals("pfam_scan")) {
            		pfamDomains.add(domain);
            	}
            }
            
            assertEquals(2, pfamDomains.size());
            PolypeptideDomain domain = pfamDomains.first();
            assertNotNull(domain);
            
            assertEquals("MAL13P1.114:pep:Pfam:PF00308", domain.getUniqueName());

            DbXRef dbxref = domain.getDbXRef();
            assertNotNull(dbxref);
            assertEquals("Pfam", dbxref.getDb().getName());
            assertEquals("PF00308", dbxref.getAccession());
            assertEquals("Bac_DnaA", dbxref.getDescription());
            
            // No InterPro dbxrefs
            Collection<FeatureDbXRef> featureDbXRefs = domain.getFeatureDbXRefs();
            assertNotNull(featureDbXRefs);
            assertEquals(0, featureDbXRefs.size());
            
        } finally {
            SessionFactoryUtils.releaseSession(session, sessionFactory);
        }
    }
    
    @AfterClass
    @Transactional
    public static void shutdownDatabase() throws HibernateException, SQLException {
       Session session = SessionFactoryUtils.getSession(loader.getSessionFactory(), true);
       session.doWork(new Work() {
        		public void execute(Connection connection) {
        			try {
        				connection.createStatement().execute("shutdown");
        			} catch (SQLException e) {
        				logger.error("Failed to shutdown database", e);
        			}
        		}
        });
    }
}
