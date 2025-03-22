/*******************************************************************************
 * Class        ：PdfGeneratorUtils
 * Created date ：2025/03/13
 * Lasted date  ：2025/03/13
 * Author       ：vinhNQ2
 * Change log   ：2025/03/13：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.infrastructure.utils;

import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * PdfGeneratorUtils
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public class PdfGeneratorUtils {

    public static byte[] generateTicketPdf(String htmlContent) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            FontFactory.register("src/main/resources/static/fonts/Arial.ttf", "Arial");
            renderer.getFontResolver().addFont("src/main/resources/static/fonts/Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IOException("Failed to generate PDF", e);
        }
    }
}