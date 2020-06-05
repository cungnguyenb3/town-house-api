package vn.com.pn.repository.host;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;
import vn.com.pn.domain.Host;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class HostRepositoryCustomImpl implements HostRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Host> search(String searchText, int pageNo, int resultsPerPage) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            System.out.println("Error occured trying to build Hibernate Search indexes "
                    + e.toString());
        }


        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Host.class).get();

        org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().onFields("name", "address")
                .matching(searchText).createQuery();

        FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Host.class);
        jpaQuery.setMaxResults(resultsPerPage);
        jpaQuery.setFirstResult((pageNo-1) * resultsPerPage);

        return jpaQuery.getResultList();
    }
}
