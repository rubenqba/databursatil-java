package com.github.rubenqba.databursatil.models.filter;

import java.util.EnumSet;

public record SearchFilter(String query, EnumSet<MarketScope> scopes) {
    public SearchFilter(String query) {
        this(query, null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String query;
        private EnumSet<MarketScope> scopes;

        private Builder() {
        }

        public Builder query(String val) {
            query = val;
            return this;
        }

        public Builder onlyLocalMarket() {
            scopes = EnumSet.of(MarketScope.LOCAL);
            return this;
        }

        public Builder onlyGlobalMarket() {
            scopes = EnumSet.of(MarketScope.GLOBAL);
            return this;
        }

        public Builder allMarkets() {
            scopes = EnumSet.of(MarketScope.LOCAL, MarketScope.GLOBAL);
            return this;
        }

        public SearchFilter build() {
            return new SearchFilter(this.query, this.scopes);
        }
    }
}
