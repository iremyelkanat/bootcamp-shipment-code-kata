package com.trendyol.shipment;

import java.util.*;

import static java.util.stream.Collectors.*;

public class BasketSizeHandler {

    private final int BASKET_SHIPMENT_THRESHOLD = 3;
    private final List<Product> products;

    public BasketSizeHandler(Basket basket) {
        products = basket.getProducts();
    }

    public ShipmentSize getBasketShipmentSize() {

        if (products.isEmpty()) {
            throw new RuntimeException("Basket is empty");
        }

        Map<ShipmentSize, Integer> productSizeCounts = findProductSizeCounts();

        Optional<ShipmentSize> sizeOccuringMoreThanThreshold = getSizeOccuringMoreThanThreshold(productSizeCounts);

        if (sizeOccuringMoreThanThreshold.isPresent()) {
            return sizeOccuringMoreThanThreshold.get().getLargerSize();
        } else {
            return getLargestShipmentSizeInBasket();
        }
    }

    private Map<ShipmentSize, Integer> findProductSizeCounts() {
        return products.stream()
                .collect(groupingBy(Product::getSize, collectingAndThen(counting(), Long::intValue)));
    }

    private ShipmentSize getLargestShipmentSizeInBasket() {
        return products.stream()
                .sorted((firstProduct, secondProduct) -> secondProduct.getSize().compareTo(firstProduct.getSize()))
                .findFirst()
                .get().getSize();
    }

    private Optional<ShipmentSize> getSizeOccuringMoreThanThreshold(Map<ShipmentSize, Integer> productSizeCounts) {
        return productSizeCounts.entrySet().stream()
                .filter(sizeCountEntry -> sizeCountEntry.getValue() >= BASKET_SHIPMENT_THRESHOLD)
                .map(Map.Entry::getKey)
                .findFirst();
    }

}
