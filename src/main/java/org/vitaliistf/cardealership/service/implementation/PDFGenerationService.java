package org.vitaliistf.cardealership.service.implementation;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.CarOrder;
import org.vitaliistf.cardealership.service.DocumentGenerationService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Service for generating PDF documents based on car orders.
 */
@Service
public class PDFGenerationService extends DocumentGenerationService {

    /**
     * Writes the details of a car order to a PDF document.
     *
     * @param order        CarOrder object representing the order
     * @param outputStream ByteArrayOutputStream to which the document is written
     * @throws IOException if an I/O error occurs while writing the document
     */
    @Override
    public void writeOrderToDocument(CarOrder order, ByteArrayOutputStream outputStream) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        PDFont font = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
        contentStream.setFont(font, 12);

        addText(contentStream, "Car order", 45, 700);

        addText(contentStream, "Order #" + order.getId(), 45, 675);
        addText(contentStream, "Date: " + order.getOrderDate(), 45, 660);
        addText(contentStream, "Order status: " + order.getOrderStatus(), 45, 645);

        addText(contentStream, "Seller:", 45, 620);
        addText(contentStream, order.getSeller().getFirstName() + " " + order.getSeller().getLastName(), 60, 605);
        addText(contentStream, "Email: " + order.getSeller().getEmail(), 60, 590);
        addText(contentStream, "Phone number: " + order.getSeller().getPhoneNumber(), 60, 575);

        addText(contentStream, "Buyer:", 45, 550);
        addText(contentStream, order.getBuyer().getFirstName() + " " + order.getBuyer().getLastName(), 60, 535);
        addText(contentStream, "Email: " + order.getBuyer().getEmail(), 60, 520);
        addText(contentStream, "Phone number: " + order.getBuyer().getPhoneNumber(), 60, 505);

        Car car = order.getCar();
        addText(contentStream, "Car:", 45, 480);
        addText(contentStream, car.getBrand() + " " + car.getModel() + " (" + car.getYear() + ")", 60, 465);
        addText(contentStream, "Color: " + car.getColor(), 60, 450);
        addText(contentStream, "Transmission: " + car.getTransmission(), 60, 435);
        addText(contentStream, "Fuel type: " + car.getFuelType(), 60, 420);
        addText(contentStream, "Body type: " + car.getBodyType(), 60, 405);
        addText(contentStream, "Engine displacement: " + car.getEngineDisplacement() + " l", 60, 390);
        addText(contentStream, "Mileage: " + car.getMileage() + " km", 60, 375);
        addText(contentStream, "Price: " + car.getPrice() + " USD", 60, 360);
        addText(contentStream, "Technical condition: " + car.getCondition(), 60, 345);
        contentStream.close();

        document.save(outputStream);
        document.close();
    }

    /**
     * Adds text to the PDF document.
     *
     * @param contentStream PDPageContentStream for adding text to the document
     * @param text          Text to add
     * @param x             X-coordinate of the text
     * @param y             Y-coordinate of the text
     * @throws IOException if an I/O error occurs while adding text
     */
    private void addText(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);

        //Next line was done to prevent errors when writing cyrillic or other characters that are not supported by font
        contentStream.showText(new String(text.getBytes(StandardCharsets.US_ASCII), StandardCharsets.US_ASCII));
        contentStream.endText();
    }
}
