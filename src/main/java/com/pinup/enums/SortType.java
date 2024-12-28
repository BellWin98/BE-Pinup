package com.pinup.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortType {

    NEAR("가까운 순"),
    LATEST("최신 순"),
    STAR_HIGH("별점 높은 순"),
    STAR_LOW("별점 낮은 순")

    ;

    private final String description;

    public static SortType getSortTypeByDescription(String sortDescription) {
        for (SortType sortType : SortType.values()) {
            if (sortType.description.equals(sortDescription)) {
                return sortType;
            }
        }
        return null;
    }

}
