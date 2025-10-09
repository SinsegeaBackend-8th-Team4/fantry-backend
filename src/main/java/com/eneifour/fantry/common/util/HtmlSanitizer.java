package com.eneifour.fantry.common.util;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Component;

@Component
public class HtmlSanitizer {
    // 정책을 미리 정의
    private final PolicyFactory defaultPolicy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
    private final PolicyFactory imagePolicy = defaultPolicy.and(Sanitizers.IMAGES);

    /**
     * 기본 정책(서식, 링크)으로 HTML을 소독합니다.
     * @param dirtyHtml 소독이 필요한 HTML 문자열
     * @return 안전하게 소독된 HTML 문자열
     */
    public String sanitize(String dirtyHtml) {
        if (dirtyHtml == null) {
            return null;
        }
        return defaultPolicy.sanitize(dirtyHtml);
    }

    /**
     * 이미지까지 허용하는 정책으로 HTML을 소독합니다.
     * @param dirtyHtml 소독이 필요한 HTML 문자열
     * @return 안전하게 소독된 HTML 문자열 (이미지 포함)
     */
    public String sanitizeWithImages(String dirtyHtml) {
        if (dirtyHtml == null) {
            return null;
        }
        return imagePolicy.sanitize(dirtyHtml);
    }
}
