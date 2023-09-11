package com.trendyol.shipment;

public class Product {

    private ShipmentSize size;

    public static Product create(ShipmentSize shipmentSize) {
        Product productVO = new Product();
        productVO.setSize(shipmentSize);
        return productVO;
    }

    public ShipmentSize getSize() {
        return size;
    }

    public void setSize(ShipmentSize size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return size.name();
    }
}
