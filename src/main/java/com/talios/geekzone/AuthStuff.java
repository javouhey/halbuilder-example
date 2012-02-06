package com.talios.geekzone;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.theoryinpractise.halbuilder.spi.ReadableResource;
import spark.Request;

import javax.annotation.Nullable;
import java.util.Set;

public class AuthStuff {

    public Predicate<ReadableResource> makeAuthPredicate(final String roleTypes, final Request request) {
        return new AutenticationPredicate(roleTypes, request);
    }

    private static class AutenticationPredicate implements Predicate<ReadableResource> {
        private Set<String> requiredRoles;
        private Set<String> userAuthSet;

        public AutenticationPredicate(String roleTypes, Request request) {
            this.requiredRoles = Sets.newHashSet(Splitter.on(" ").split(roleTypes));
            this.userAuthSet = Sets.newHashSet(Splitter.on(" ").split(Objects.firstNonNull(request.headers("auth"), "")));
        }

        public boolean apply(@Nullable ReadableResource resource) {
            return userAuthSet.containsAll(requiredRoles);
        }
    }
}
