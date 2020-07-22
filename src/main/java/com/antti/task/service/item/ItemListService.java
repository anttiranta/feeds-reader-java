package com.antti.task.service.item;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import com.antti.task.entity.Item;
import com.antti.task.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import com.antti.task.core.api.PageRequestBuilder;
import java.util.List;
import javax.persistence.TypedQuery;
import com.antti.task.core.api.PaginationProcessor;
import com.antti.task.core.api.FilterProcessor;
import com.antti.task.core.api.SearchCriteria;

@Service
public class ItemListService {

    private final PageRequestBuilder pageRequestBuilder;
    
    @PersistenceContext
    private final EntityManager entityManager;

    private final String mainTable = Item.class.getSimpleName();
    private final String mainTableAlias = String.valueOf(mainTable.charAt(0)).toLowerCase();
    private final String categoryTableAlias = String.valueOf(
            Category.class.getSimpleName().charAt(0)
        ).toLowerCase();

    @Autowired
    public ItemListService(
        EntityManager entityManager,
        PageRequestBuilder pageRequestBuilder
    ) {
        this.entityManager = entityManager;
        this.pageRequestBuilder = pageRequestBuilder;
    }
    
    @Transactional
    public Page<Item> getItemList(SearchCriteria searchCriteria) {
        List<Item> items = createQuery(searchCriteria).getResultList();

        return new PageImpl<>(
            items,
            pageRequestBuilder.build(searchCriteria),
            searchCriteria.getPageSize() != null 
                    ? getTotalCount(searchCriteria) 
                    : items.size()
        );
    }

    private long getTotalCount(SearchCriteria searchCriteria) {
        return (long)createTotalCountQuery(searchCriteria).getSingleResult();
    }

    private TypedQuery createQuery(SearchCriteria searchCriteria) {
        FilterProcessor filterProcessor = new FilterProcessor(); 
        PaginationProcessor paginationProcessor = new PaginationProcessor();
        
        StringBuilder hqlB = new StringBuilder("select " + mainTableAlias);
        hqlB.append(createBaseQueryFromPart());
        
        addCategoryJoinToHql(hqlB);
        
        filterProcessor.process(searchCriteria, hqlB);

        TypedQuery query = entityManager.createQuery(hqlB.toString(), Item.class);

        filterProcessor.process(searchCriteria, query);
        paginationProcessor.process(searchCriteria, query);
        
        // TODO: implement sorting?

        return query;
    }
    
    private TypedQuery createTotalCountQuery(SearchCriteria searchCriteria) {
        FilterProcessor filterProcessor = new FilterProcessor();
        
        StringBuilder hqlB = new StringBuilder(
            "select count(" + mainTableAlias + ".id)"
        );
        hqlB.append(createBaseQueryFromPart());
        
        addCategoryJoinToHql(hqlB);
        
        filterProcessor.process(searchCriteria, hqlB);
        
        TypedQuery query = entityManager.createQuery(hqlB.toString(), Long.class);
        
        filterProcessor.process(searchCriteria, query);

        return query;
    }
    
    private String createBaseQueryFromPart() {
        return " from " + mainTable + " " + mainTableAlias + " ";
    }

    private void addCategoryJoinToHql(StringBuilder hqlB) {
        hqlB.append("left join ");
        hqlB.append(mainTableAlias);
        hqlB.append(".category ");
        hqlB.append(categoryTableAlias);
        hqlB.append(" ");
    }
}