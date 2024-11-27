package com.pinup.controller;


import com.pinup.dto.request.ArticleCreateRequest;
import com.pinup.dto.response.ArticleResponse;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiSuccessResponse<ArticleResponse>> createArticle(
            ArticleCreateRequest articleCreateRequest,
            @RequestParam(value = "multipartFiles") MultipartFile multipartFile
    ) {
        ArticleResponse result = articleService.create(articleCreateRequest, multipartFile);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(result));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<ArticleResponse>> getArticleById(@PathVariable("id") Long id) {
        ArticleResponse result = articleService.findById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(result));
    }

    @GetMapping
    public ResponseEntity<ApiSuccessResponse<List<ArticleResponse>>> getArticles(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<ArticleResponse> articles = articleService.getArticles(page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(articles));
    }
}
