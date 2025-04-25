package com.github.rubenqba.databursatil.models;

import java.util.List;

public record CommodityGroup(String name, List<Commodity> commodities) {
}
