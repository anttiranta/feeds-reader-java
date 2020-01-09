package com.antti.task.html;

import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Page;
import javax.servlet.http.HttpServletRequest;
import java.lang.Math; 
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.antti.task.core.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class Pager<T> 
{
    protected String pageVarName = "p";

    protected String limitVarName = "limit";
    
    /**
     * The list of available pager limits
     */
    protected Map<Integer, Integer> availableLimit = new HashMap<Integer, Integer>() 
    {{
        put(5, 5);
        put(10, 10);
        put(20, 20);
        put(50, 50);
    }};

    protected Integer displayPages = 5;
    
    protected Boolean showPerPage = true;

    protected Integer limit;
    
    private final HttpServletRequest request;
    
    private Page<T> page;
    
    @Autowired
    UrlBuilder urlBuilder;
    
    public Pager(HttpServletRequest request) 
    {
        this.request = request;
    }
    
    public Pager(HttpServletRequest request, Page<T> page) 
    {
        this.request = request;
        this.page = page;
    }
    
    public Pager setPage(Page<T> page)
    {
        this.page = page;
        return this;
    }
    
    public Page<T> getPage()
    {
        return this.page;
    }
    
    public HttpServletRequest getRequest()
    {
        return this.request;
    }
    
    /**
     * Return current page
     *
     * @return
     */
    public Integer getCurrentPage()
    {
        if(this.page != null && this.getPage().getPageable() != null) {
            return this.getPageNumber();
        }
        
        return this.getRequest().getParameter(this.getPageVarName()) != null 
                ? Integer.parseInt(this.getRequest().getParameter(this.getPageVarName()))
                : 1;
    }
    
    /**
     * Return current page limit
     *
     * @return
     */
    public Integer getLimit()
    {
        if (this.limit != null) {
            return this.limit;
        }
        
        Map <Integer, Integer> limits = this.getAvailableLimit();
        
        Integer parsedLimit = null;
        try {
            parsedLimit = Integer.parseInt(this.getRequest().getParameter(this.getLimitVarName()));
        }catch (NumberFormatException nfe) {
            // Ignore
        }
        
        if (parsedLimit != null) {
            if (limits.containsKey(parsedLimit)) {
                return parsedLimit;
            }
        }
        
        return limits.keySet().iterator().next();
    }
    
    /**
     * Setter for limit items per page
     *
     * @param limit
     * @return
     */
    public Pager setLimit(Integer limit)
    {
        this.limit = limit;
        return this;
    }
    
    public Pager setPageVarName(String varName)
    {
        this.pageVarName = varName;
        return this;
    }
    
    public String getPageVarName()
    {
        return this.pageVarName;
    }
    
    public Pager setShowPerPage(Boolean varName)
    {
        this.showPerPage = varName;
        return this;
    }
    
    public Boolean isShowPerPage()
    {
        if (this.getAvailableLimit().size() <= 1) {
            return false;
        }
        return this.showPerPage;
    }
    
    /**
     * Set the name for pager limit data
     *
     * @param varName
     * @return
     */
    public Pager setLimitVarName(String varName)
    {
        this.limitVarName = varName;
        return this;
    }
    
    /**
     * Retrieve name for pager limit data
     *
     * @return 
     */
    public String getLimitVarName()
    {
        return this.limitVarName;
    }
    
    /**
     * Set pager limit
     *
     * @param limits
     * @return
     */
    public Pager setAvailableLimit(Map<Integer, Integer> limits)
    {
        this.availableLimit = limits;
        return this;
    }
    
    /**
     * Retrieve pager limit
     *
     * @return
     */
    public Map<Integer, Integer> getAvailableLimit()
    {
        return this.availableLimit;
    }
    
    public Integer getFirstNum()
    {
        return this.getPage().getPageable().getPageSize() 
                * (this.getPageNumber() - 1) + 1;
    }
    
    public Long getLastNum()
    {
        return this.getPage().getPageable().getPageSize() 
                * (this.getPageNumber() - 1) 
                + (long)this.getPage().getNumberOfElements();
    }
    
    /**
     * Retrieve total number of pages
     *
     * @return
     */
    public Integer getTotalNum()
    {
        return this.getPage().getTotalPages();
    }
    
    /**
     * Check if current page is a first page in collection
     *
     * @return
     */
    public Boolean isFirstPage()
    {
        return this.getPageNumber() == 1;
    }
    
    /**
     * Retrieve number of last page
     *
     * @return
     */
    public Integer getLastPageNum()
    {
        int lastPageNum = (int)Math.ceil(((double)this.getPage().getTotalElements() 
                / (double)this.getPage().getPageable().getPageSize()));
        return lastPageNum > 0 ? lastPageNum : 1;
    }
    
    /**
     * Check if current page is a last page in collection
     *
     * @return
     */
    public Boolean isLastPage()
    {
        return this.getPageNumber() >= this.getLastPageNum();
    }
    
    public Boolean isLimitCurrent(Integer limit)
    {
        return limit.equals(this.getLimit());
    }
    
    public Boolean isPageCurrent(Integer page)
    {
        return page.equals(this.getCurrentPage());
    }
    
    public List<Integer> getPages()
    {
        if (this.getPage().getTotalPages() <= this.displayPages) {
            return IntStream.rangeClosed(1, this.getPage().getTotalPages()).boxed().collect(Collectors.toList());
        } else {
            double half = Math.ceil(this.displayPages / 2);
            
            if (this.getPageNumber() >= half &&
                this.getPageNumber() <= this.getPage().getTotalPages() - half
            ) {
                int start = (int)(this.getPageNumber() - half + 1);
                int finish = start + this.displayPages - 1;
                return IntStream.rangeClosed(start, finish).boxed().collect(Collectors.toList());
            } else if (this.getPageNumber() < half) {
                int start = 1;
                int finish = this.displayPages;
                return IntStream.rangeClosed(start, finish).boxed().collect(Collectors.toList());
            } else if (this.getPageNumber() > this.getPage().getTotalPages() - half) {
                int finish = this.getPage().getTotalPages();
                int start = finish - this.displayPages + 1;
                return IntStream.rangeClosed(start, finish).boxed().collect(Collectors.toList());
            }
            
            return IntStream.rangeClosed(1, 1).boxed().collect(Collectors.toList());
        }
    }
    
    public String getFirstPageUrl()
    {
        return this.getPageUrl(1);
    }
    
    /**
     * Retrieve previous page URL
     *
     * @return
     */
    public String getPreviousPageUrl()
    {
        return this.getPageUrl(this.getPageNumber() - 1);
    }
    
    /**
     * Retrieve next page URL
     *
     * @return
     */
    public String getNextPageUrl()
    {
        return this.getPageUrl(this.getPageNumber() + 1);
    }
    
    /**
     * Retrieve last page URL
     *
     * @return
     */
    public String getLastPageUrl()
    {
        return this.getPageUrl(this.getLastPageNum());
    }
    
    /**
     * Retrieve page URL
     *
     * @param page
     * @return
     */
    public String getPageUrl(Integer page)
    {
        return this.getPageUrl(new Long(page));
    }
    
    /**
     * Retrieve page URL
     *
     * @param page
     * @return
     */
    public String getPageUrl(Long page)
    {
        Map<String, String> params = new HashMap<>();
        params.put(this.getPageVarName(), page.toString());
        
        return this.getPagerUrl(params);
    }

    public String getLimitUrl(Integer limit)
    {
        Map<String, String> params = new HashMap<>();
        params.put(this.getLimitVarName(), limit.toString());
        
        if (this.getCurrentPage() * limit > this.getPage().getTotalElements()) {
            // Return back to first page
            params.put(this.getPageVarName(), "1"); 
        }
        
        return this.getPagerUrl(params);
    }
    
    public String getPagerUrl(Map<String, String> params) 
    {
        Map<String, String[]> queryParams = new HashMap<>(this.getRequest().getParameterMap()); 
        
        if (params.containsKey(this.getPageVarName())) {
            queryParams.remove(this.getPageVarName());
            queryParams.put(this.getPageVarName(), new String[]{params.get(this.getPageVarName())});
        }
        if (params.containsKey(this.getLimitVarName())) {
            queryParams.remove(this.getLimitVarName());
            queryParams.put(this.getLimitVarName(), new String[]{params.get(this.getLimitVarName())});
        }

        return this.getUrlBuilder().getUrl(this.getRequest(), queryParams);
    }
    
    /**
     * Convinience function to get correct page number even if 
     * page.getPageable().getPageNumber() returns zero (0)
     * @return
     */
    private Integer getPageNumber()
    {
        int pageNumber = this.getPage().getPageable().getPageNumber();
        return pageNumber > 0 ? pageNumber + 1 : 1;
    }
    
    /**
     * Workaround to get UrlBuilder
     * @return
     * @todo: remove this function
     */
    private UrlBuilder getUrlBuilder()
    {
        if (this.urlBuilder == null) {
            this.urlBuilder = new UrlBuilder();
        }
        return this.urlBuilder;
    }
}
