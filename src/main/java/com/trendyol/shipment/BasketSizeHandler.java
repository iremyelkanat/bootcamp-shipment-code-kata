package com.trendyol.shipment;

import java.util.*;

import static java.util.stream.Collectors.*;

public class BasketSizeHandler {

    private final int BASKET_SHIPMENT_THRESHOLD = 3;
    private final List<Product> products;
    private TreeMap<ShipmentSize, Integer> productSizeCounts;

    public BasketSizeHandler(Basket basket) {
        products = basket.getProducts();
    }

    public ShipmentSize getBasketShipmentSize() {

        if (products.isEmpty()) {
            throw new RuntimeException("Basket is empty");
        }

        findProductSizeCounts();

        if (doesBasketContainMoreThanThresholdAndAllXLarge()) {
            return ShipmentSize.X_LARGE;
        }

        Optional<ShipmentSize> sizeOccuringMoreThanThreshold = getSizeOccuringMoreThanThreshold();

        if (sizeOccuringMoreThanThreshold.isPresent()) {
            return sizeOccuringMoreThanThreshold.get().getLargerSize();
        } else {
            return getLargestShipmentSizeInBasket();
        }
    }

    private void findProductSizeCounts() {
        productSizeCounts = products.stream()
                .sorted(Comparator.comparing(Product::getSize).reversed())
                .collect(groupingBy(Product::getSize, () -> new TreeMap<>(Collections.reverseOrder()), collectingAndThen(counting(), Long::intValue)));
    }

    private ShipmentSize getLargestShipmentSizeInBasket() {
        return productSizeCounts.keySet().stream().findFirst().get();
    }

    private Optional<ShipmentSize> getSizeOccuringMoreThanThreshold() {
        return productSizeCounts.entrySet().stream()
                .filter(sizeCountEntry -> sizeCountEntry.getValue() >= BASKET_SHIPMENT_THRESHOLD)
                .map(Map.Entry::getKey)
                .findFirst();
    }

    private boolean doesBasketContainMoreThanThresholdAndAllXLarge() {
        int xLargeSizeCount = productSizeCounts.getOrDefault(ShipmentSize.X_LARGE, 0);
        return xLargeSizeCount >= BASKET_SHIPMENT_THRESHOLD && productSizeCounts.entrySet().size() == 1;
    }

}
