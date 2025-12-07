package com.menudigital.menuapi.menu.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.menudigital.menuapi.menu.domain.Menu;
import com.menudigital.menuapi.menu.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuPdfService {

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD);
    private static final Font HEADING_FONT = new Font(Font.HELVETICA, 14, Font.BOLD);
    private static final Font TEXT_FONT = new Font(Font.HELVETICA, 12, Font.NORMAL);
    private static final Font SMALL_ITALIC_FONT = new Font(Font.HELVETICA, 11, Font.ITALIC);

    private final MenuService menuService;
    private final ProductService productService;
    private final CompanyService companyService;

    public byte[] generateCompanyMenuPdf(UUID companyId) {
        var company = companyService.get(companyId);
        var menus = menuService.listActive(companyId);

        try (var outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            var companyName = company.getCommercialName() != null && !company.getCommercialName().isBlank()
                    ? company.getCommercialName()
                    : company.getBusinessName();
            document.add(new Paragraph(companyName != null ? companyName : "Menú", TITLE_FONT));
            document.add(new Paragraph(" "));

            if (menus.isEmpty()) {
                document.add(new Paragraph("No hay menús disponibles para esta empresa.", SMALL_ITALIC_FONT));
            } else {
                for (Menu menu : menus) {
                    addMenuSection(document, menu, companyId);
                }
            }

            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new IllegalStateException("No se pudo generar el PDF del menú", e);
        }
    }

    private void addMenuSection(Document document, Menu menu, UUID companyId) throws DocumentException {
        document.add(new Paragraph(menu.getName(), HEADING_FONT));

        List<Product> products = productService.list(companyId, null, menu.getId());
        if (products.isEmpty()) {
            document.add(new Paragraph("Sin productos asociados.", SMALL_ITALIC_FONT));
            document.add(new Paragraph(" "));
            return;
        }

        for (Product product : products) {
            var price = NumberFormat.getCurrencyInstance(new Locale("es", "ES")).format(product.getPrice());
            document.add(new Paragraph(product.getName() + " - " + price, TEXT_FONT));
            if (product.getDescription() != null && !product.getDescription().isBlank()) {
                var description = new Paragraph(product.getDescription(), SMALL_ITALIC_FONT);
                description.setSpacingAfter(4f);
                document.add(description);
            } else {
                document.add(new Paragraph(" "));
            }
        }

        document.add(new Paragraph(" "));
    }
}

