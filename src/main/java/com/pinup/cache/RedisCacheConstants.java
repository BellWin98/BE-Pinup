package com.pinup.cache;

import lombok.experimental.UtilityClass;
import java.time.Duration;

@UtilityClass
public class RedisCacheConstants {
    public static final String FEED_PREFIX = "feed:";
    public static final String MEMBER_NICKNAME_PREFIX = "member:nickname:";
    public static final String NICKNAME_EMAIL_PREFIX = "nickname:email:";
    public static final Duration DEFAULT_TTL = Duration.ofHours(24);
}