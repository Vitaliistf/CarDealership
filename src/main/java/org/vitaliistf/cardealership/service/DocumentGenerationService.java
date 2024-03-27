package org.vitaliistf.cardealership.service;

import org.springframework.core.io.ByteArrayResource;
import org.vitaliistf.cardealership.data.CarOrder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Abstract class for generating documents based on car orders.
 */
public abstract class DocumentGenerationService {

    /**
     * Generates a document based on the provided car order.
     *
     * @param order CarOrder object representing the order
     * @return ByteArrayResource containing the generated document
     * @throws RuntimeException if an error occurs during document generation
     */
    public ByteArrayResource generateDocumentByOrder(CarOrder order) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            writeOrderToDocument(order, byteArrayOutputStream);

            return new ByteArrayResource(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Abstract method to write the order details to the document.
     *
     * @param order  CarOrder object representing the order
     * @param stream ByteArrayOutputStream to which the document is written
     * @throws IOException if an I/O error occurs while writing the document
     */
    protected abstract void writeOrderToDocument(CarOrder order, ByteArrayOutputStream stream) throws IOException;
}
