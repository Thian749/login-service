package com.team4.demoauth.service;

import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

    @Service
    public class TokenBlacklistService {
        private Set<String> blacklistedTokens = new HashSet<>();

        public void addToBlacklist(String token) {
            blacklistedTokens.add(token);
        }

        public void removeFromBlacklist(String token) {
            blacklistedTokens.remove(token);
        }

        public boolean isTokenBlacklisted(String token) {
            return blacklistedTokens.contains(token);
        }
    }