package com.antti.task.service.item;

import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import com.antti.task.entity.Item;
import com.antti.task.repository.ItemRepository;
import com.antti.task.util.api.DataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class ItemList {

    @Autowired
    DataHelper apiDataHelper;
    
    @Autowired
    private ItemRepository itemRepository;

    @PersistenceContext
    private EntityManager entityManager;
    
    private final Map<String,String> mapColumns = new HashMap<String, String>() 
    {{
        put("category_name", "cat.name");
        put("title", "i.title");
        put("description", "i.description");
        put("link", "i.link");
        put("category_domain", "cat.domain");
    }};
    
    @Transactional
    public Page<Item> getList(Map<String, String> filters, PageRequest pageable)
    {
        Map<String,String> parsedFilters = apiDataHelper.parseFilters(
                filters, 
                this.mapColumns
        ); 
        
        if (parsedFilters.isEmpty()) {
            return this.itemRepository.findAll(pageable);
	}
        
        // TODO: replace hql with jpql
        String hql = "select i from " + Item.class.getSimpleName() + " i ";
        hql += ("left join i.category cat ");
        hql += "where 1=1";
        
        for(Map.Entry<String, String> entry : parsedFilters.entrySet()) {
            hql += " and " + entry.getKey() + " like :" + entry.getKey().replace(".", "_");
        }
        
        Iterator<Order> orderIterator = pageable.getSort().iterator();
        while(orderIterator.hasNext()) {
            Order order = orderIterator.next();
            hql += " order by ";
            hql += " " + order.getProperty() + " ";
            hql += order.getDirection();
        }

        TypedQuery<Item> query = this.entityManager.createQuery(hql, Item.class);
        Query queryTotal = this.entityManager.createQuery(this.createTotalQueryFromOriginalQuery(hql));
        
        for(Map.Entry<String, String> entry : parsedFilters.entrySet()) {
            String key = entry.getKey().replace(".", "_");
            String value = ("%" + entry.getValue() + "%");
            
            query.setParameter(key, value);
            queryTotal.setParameter(key, value);
	}
        
        List<Item> items = query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        
        return new PageImpl<>(items, pageable, (long)queryTotal.getSingleResult());
    }
    
    private String createTotalQueryFromOriginalQuery(String hql) throws RuntimeException 
    {
        String[] split = hql.split(" from ");
        if (split.length != 2){
            throw new RuntimeException("Invalid hql query given to function createTotalQuery.");
        }
        return "Select count(i.id) from " + split[1];
    }
}
