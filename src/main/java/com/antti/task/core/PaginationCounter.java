package com.antti.task.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import java.util.Collections;
import java.util.LinkedHashMap;

public class PaginationCounter<T> {

    protected String pageVarName = "p";
    protected String limitVarName = "limit";
    protected Integer displayPages = 5;
    protected Boolean showPerPage = true;
    protected Integer limit;
    private final HttpServletRequest request;
    private Page<T> page;
    private List<Integer> pages;
    private Integer lastPageNum;
    private Integer currentPage;
    protected Map<Integer, Integer> availableLimit = new HashMap<Integer, Integer>() {
        {
            put(5, 5);
            put(10, 10);
            put(20, 20);
            put(50, 50);
        }
    };

    public PaginationCounter(HttpServletRequest request) {
        this.request = request;
    }

    public PaginationCounter(HttpServletRequest request, Page<T> page) {
        this.request = request;
        this.page = page;
    }

    public PaginationCounter setPage(Page<T> page) {
        this.page = page;
        clearCache();
        
        return this;
    }

    public Page<T> getPage() {
        return page;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public Integer getCurrentPage() {
        if (currentPage == null) {
            Integer page = getRequest().getParameter(getPageVarName()) != null
                ? Integer.parseInt(getRequest().getParameter(getPageVarName()))
                : null;

            currentPage = page != null && (int)page >= 1 ? page : 1;
        }
        return currentPage;
    }

    public Integer getLimit() {
        if (limit != null) {
            return limit;
        }

        Map<Integer, Integer> limits = getAvailableLimit();

        Integer parsedLimit = null;
        try {
            parsedLimit = Integer.parseInt(getRequest().getParameter(getLimitVarName()));
        } catch (NumberFormatException nfe) {
            // Ignore
        }

        if (parsedLimit != null) {
            if (limits.containsKey(parsedLimit)) {
                return parsedLimit;
            }
        }

        return Collections.min(limits.keySet());
    }

    public PaginationCounter setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public PaginationCounter setPageVarName(String varName) {
        pageVarName = varName;
        return this;
    }

    public String getPageVarName() {
        return pageVarName;
    }

    public PaginationCounter setShowPerPage(Boolean varName) {
        showPerPage = varName;
        return this;
    }

    public boolean isShowPerPage() {
        if (getAvailableLimit().size() <= 1) {
            return false;
        }
        return showPerPage;
    }

    public PaginationCounter setLimitVarName(String varName) {
        limitVarName = varName;
        return this;
    }

    public String getLimitVarName() {
        return limitVarName;
    }

    public PaginationCounter setAvailableLimit(Map<Integer, Integer> limits) {
        availableLimit = limits;
        return this;
    }

    public Map<Integer, Integer> getAvailableLimit() {
        return getAvailableLimit(false);
    }

    public Map<Integer, Integer> getAvailableLimit(boolean sorted) {
        if (sorted) {
            return availableLimit.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        }
        return availableLimit;
    }
    
    public Integer getDisplayPages() {
        return displayPages;
    }

    public Integer getFirstNum() {
        return getPage().getPageable().getPageSize()
                * (getCurrentPage() - 1) + 1;
    }

    public Long getLastNum() {
        return getPage().getPageable().getPageSize()
                * (getCurrentPage() - 1)
                + (long)getPage().getPageable().getPageSize();
    }


    public long getTotalNum() {
        return getPage().getTotalElements();
    }

    public Boolean isFirstPage() {
        return getCurrentPage().equals(getFirstPageNum());
    }
    
    public Integer getFirstPageNum() {
        return 1;
    }
    
    public Integer getPreviousPageNum() {
        return getCurrentPage() - 1;
    }
    
    public Integer getNextPageNum() {
        return getCurrentPage() + 1;
    }

    public Integer getLastPageNum() {
        if (lastPageNum == null) {
            lastPageNum = (int) Math.ceil(((double)getPage().getTotalElements()
                / (double)getPage().getPageable().getPageSize()));
           lastPageNum = lastPageNum > 0 ? lastPageNum : 1;
        }
        return lastPageNum;
    }

    public Boolean isLastPage() {
        return getCurrentPage() >= getLastPageNum();
    }

    public Boolean isLimitCurrent(Integer limit) {
        return limit.equals(getLimit());
    }

    public Boolean isPageCurrent(Integer page) {
        return page == getCurrentPage().intValue();
    }
    
    public List<Integer> getPages() {
        if (pages == null) {
            pages = computePages();
        }
        return pages;
    }
    
    private List<Integer> computePages() {
        if (getLastPageNum() <= getDisplayPages()) {
            return IntStream.rangeClosed(1, getLastPageNum()).boxed().collect(Collectors.toList());
        } else {
            double half = Math.ceil(getDisplayPages() / 2);

            if (getCurrentPage() >= half
                    && getCurrentPage() <= getLastPageNum() - half) {
                int start = (getCurrentPage() - half) > 0 ? (int)(getCurrentPage() - half) : 1;
                int finish = start + getDisplayPages() - 1;
                return IntStream.rangeClosed(start, finish).boxed().collect(Collectors.toList());
            } else if (getCurrentPage() < half) {
                int start = 1;
                int finish = start + getDisplayPages() - 1;
                return IntStream.rangeClosed(start, finish).boxed().collect(Collectors.toList());
            } else if (getCurrentPage() > getLastPageNum() - half) {
                int finish = getLastPageNum();
                int start = finish - getDisplayPages() + 1;
                return IntStream.rangeClosed(start, finish).boxed().collect(Collectors.toList());
            }

            return IntStream.rangeClosed(1, 1).boxed().collect(Collectors.toList());
        }
    }
    
    private void clearCache() {
        pages = null;
        lastPageNum = null;
        currentPage = null;
    }
}
