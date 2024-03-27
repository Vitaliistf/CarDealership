package org.vitaliistf.cardealership.service.implementation;

import org.springframework.stereotype.Service;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.CarOrder;
import org.vitaliistf.cardealership.service.DocumentGenerationService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Service for generating text files based on car orders.
 */
@Service
public class TextFileGenerationService extends DocumentGenerationService {

    /**
     * Writes the details of a car order to a text file.
     *
     * @param order        CarOrder object representing the order
     * @param outputStream ByteArrayOutputStream to which the text file is written
     * @throws IOException if an I/O error occurs while writing the text file
     */
    @Override
    public void writeOrderToDocument(CarOrder order, ByteArrayOutputStream outputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        appendOrderHeader(sb, order);
        appendSellerInfo(sb, order);
        appendBuyerInfo(sb, order);
        appendCarInfo(sb, order.getCar());
        outputStream.write(sb.toString().getBytes());
    }

    /**
     * Appends the order header information to the given StringBuilder.
     *
     * @param sb    StringBuilder to append the order header to
     * @param order CarOrder object containing the order information
     */
    private void appendOrderHeader(StringBuilder sb, CarOrder order) {
        sb.append("Order #").append(order.getId()).append("\n");
        sb.append("Date: ").append(order.getOrderDate()).append("\n");
        sb.append("Status: ").append(order.getOrderStatus()).append("\n\n");
    }

    /**
     * Appends the seller information to the given StringBuilder.
     *
     * @param sb    StringBuilder to append the seller information to
     * @param order CarOrder object containing the seller information
     */
    private void appendSellerInfo(StringBuilder sb, CarOrder order) {
        sb.append("Seller:\n");
        sb.append(order.getSeller().getFullName()).append("\n");
        sb.append("Email: ").append(order.getSeller().getEmail()).append("\n");
        sb.append("Phone number: ").append(order.getSeller().getPhoneNumber()).append("\n\n");
    }

    /**
     * Appends the buyer information to the given StringBuilder.
     *
     * @param sb    StringBuilder to append the buyer information to
     * @param order CarOrder object containing the buyer information
     */
    private void appendBuyerInfo(StringBuilder sb, CarOrder order) {
        sb.append("Buyer:\n");
        sb.append(order.getBuyer().getFullName()).append("\n");
        sb.append("Email: ").append(order.getBuyer().getEmail()).append("\n");
        sb.append("Phone number: ").append(order.getBuyer().getPhoneNumber()).append("\n\n");
    }

    /**
     * Appends the car information to the given StringBuilder.
     *
     * @param sb  StringBuilder to append the car information to
     * @param car Car object containing the car information
     */
    private void appendCarInfo(StringBuilder sb, Car car) {
        sb.append("Car:\n");
        sb.append(car.getBrand()).append(" ").append(car.getModel()).append(" (").append(car.getYear()).append(")\n");
        sb.append("Color: ").append(car.getColor()).append("\n");
        sb.append("Transmission: ").append(car.getTransmission()).append("\n");
        sb.append("Fuel type: ").append(car.getFuelType()).append("\n");
        sb.append("Body type: ").append(car.getBodyType()).append("\n");
        sb.append("Engine displacement: ").append(car.getEngineDisplacement()).append(" l\n");
        sb.append("Mileage: ").append(car.getMileage()).append(" km\n");
        sb.append("Price: ").append(car.getPrice()).append(" USD\n");
        sb.append("Technical condition: ").append(car.getCondition()).append("\n");
    }

}
