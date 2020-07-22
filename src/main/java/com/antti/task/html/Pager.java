package com.antti.task.html;

import java.util.HashMap;
import java.util.Map;
import com.antti.task.core.UrlBuilder;
import com.antti.task.core.PaginationCounter;

public class Pager<T> 
{
    private final UrlBuilder urlBuilder;
    private final PaginationCounter paginationCounter;
    
    public Pager(PaginationCounter paginationCounter) {
        this(paginationCounter, new UrlBuilder());
    }
    
    public Pager(PaginationCounter paginationCounter, UrlBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
        this.paginationCounter = paginationCounter;
    }
    
    public PaginationCounter getPaginationCounter() {
        return paginationCounter;
    }
    
    public boolean canRender() {
        if (paginationCounter.getTotalNum() == 0) {
            return false;
        }
        if (paginationCounter.getLastPageNum() > 1) {
            return true;
        }
        if (paginationCounter.getCurrentPage() > paginationCounter.getLastPageNum()) {
            return true;
        }
        
        return false;
    }
    
    public boolean canRenderPreviousPageUrl() {
        return !paginationCounter.isFirstPage();
    }
    
    public boolean canRenderNextPageUrl() {
        return !paginationCounter.isLastPage();
    }
    
    public boolean canRenderLeftEllipsisPageUrl() {
        return hasMorePagesThanCanDisplay() 
                && paginationCounter.getPages().get(0)
                != paginationCounter.getFirstPageNum();
    }
    
    public boolean canRenderRightEllipsisPageUrl() {
        return hasMorePagesThanCanDisplay() 
                && paginationCounter.getPages().get(paginationCounter.getPages().size()-1)
                != paginationCounter.getLastPageNum();
    }
    
    private boolean hasMorePagesThanCanDisplay () {
        return paginationCounter.getLastPageNum() > paginationCounter.getDisplayPages();
    }
    
    public String getFirstPageUrl() {
        return getPageUrl(paginationCounter.getFirstPageNum());
    }
    
    public String getPreviousPageUrl() {
        return getPageUrl(paginationCounter.getPreviousPageNum());
    }

    public String getNextPageUrl() {
        return getPageUrl(paginationCounter.getNextPageNum());
    }
    
    public String getLastPageUrl() {
        return getPageUrl(paginationCounter.getLastPageNum());
    }
    
    public String getLeftEllipsisPageUrl() {
        return getPageUrl((int)paginationCounter.getPages().get(0) - 1);
    }
    
    public String getRightEllipsisPageUrl() {
        return getPageUrl((int)paginationCounter.getPages().get(
                paginationCounter.getPages().size()-1
        ) + 1);
    }
    
    public String getPageUrl(Integer page) {
        return getPageUrl(new Long(page));
    }
    
    public String getPageUrl(Long page) {
        Map<String, String> params = new HashMap<>();
        params.put(paginationCounter.getPageVarName(), page.toString());
        
        return getPagerUrl(params);
    }

    public String getLimitUrl(Integer limit) {
        Map<String, String> params = new HashMap<>();
        params.put(paginationCounter.getLimitVarName(), limit.toString());
        
        if (paginationCounter.getCurrentPage() * limit > paginationCounter.getPage().getTotalElements()) {
            // return back to first page
            params.put(paginationCounter.getPageVarName(), "1"); 
        }
        
        return getPagerUrl(params);
    }
    
    public String getPagerUrl(Map<String, String> params) {
        Map<String, String[]> queryParams = new HashMap<>(paginationCounter.getRequest().getParameterMap()); 
        
        if (params.containsKey(paginationCounter.getPageVarName())) {
            queryParams.remove(paginationCounter.getPageVarName());
            queryParams.put(paginationCounter.getPageVarName(), new String[]{params.get(paginationCounter.getPageVarName())});
        }
        if (params.containsKey(paginationCounter.getLimitVarName())) {
            queryParams.remove(paginationCounter.getLimitVarName());
            queryParams.put(paginationCounter.getLimitVarName(), new String[]{params.get(paginationCounter.getLimitVarName())});
        }

        return urlBuilder.getUrl(paginationCounter.getRequest(), queryParams);
    }
}
