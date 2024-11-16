package com.pinup.service;

import com.pinup.dto.request.ArticleCreateRequest;
import com.pinup.dto.response.ArticleResponse;
import com.pinup.entity.Article;
import com.pinup.entity.Member;
import com.pinup.global.s3.S3Service;
import com.pinup.repository.ArticleRepository;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.pinup.global.exception.PinUpException.ARTICLE_NOT_FOUND;
import static com.pinup.global.exception.PinUpException.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private static final String FILE_TYPE = "articles";

    private final S3Service s3Service;

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ArticleResponse create(ArticleCreateRequest articleCreateRequest, MultipartFile multipartFile) {
        String uploadedFileUrl = s3Service.uploadFile(FILE_TYPE, multipartFile);
        Member currentMember = getCurrentMember();

        Article newArticle = Article.builder()
                .imageUrl(uploadedFileUrl)
                .title(articleCreateRequest.getArticleTitle())
                .author(currentMember)
                .build();
        articleRepository.save(newArticle);

        return ArticleResponse.from(newArticle);
    }

    @Transactional(readOnly = true)
    public ArticleResponse findById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> ARTICLE_NOT_FOUND);

        return ArticleResponse.from(article);
    }

    private Member getCurrentMember() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> MEMBER_NOT_FOUND);
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Article> articlePage = articleRepository.findAllByOrderByCreatedAtDesc(pageable);

        return articlePage.stream()
                .map(ArticleResponse::from)
                .collect(Collectors.toList());
    }
}

