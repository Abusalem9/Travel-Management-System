package com.nymble.travelmanagementsystem.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
@AllArgsConstructor
public class FilterResponse<T> extends BaseResponse{
    private final long totalResults;
    private final long remainingPages;
    private final long currentPage;
    private final long currentPageResult;
    private final List<T> results;
}