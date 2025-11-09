package org.wa.user.service.dto.common;

import lombok.Data;
import org.springframework.data.domain.Page;
import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean first;
    private boolean last;

    public static <T, R> PageResponse<R> of(Page<T> page, List<R> content) {
        PageResponse<R> response = new PageResponse<>();
        response.setContent(content);
        response.setCurrentPage(page.getNumber());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setPageSize(page.getSize());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());
        return response;
    }
}