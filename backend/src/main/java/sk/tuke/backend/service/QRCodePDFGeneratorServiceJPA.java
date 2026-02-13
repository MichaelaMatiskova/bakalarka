package sk.tuke.backend.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.backend.core.QRCode;
import sk.tuke.backend.entity.Competitor;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import com.lowagie.text.Image;
import sk.tuke.backend.entity.QrCodes;


@Service
@Transactional
public class QRCodePDFGeneratorServiceJPA {

    @PersistenceContext
    private EntityManager entityManager;

    public void generatePDFWithQRCodes(int count) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("/backend/output/qr_codes.pdf"));
        document.open();

        QRCode qrCode = new QRCode();

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        int addedToCurrentPage = 0;

        for (int i = 0; i < count; i++) {
            QrCodes qrCodes = new QrCodes();
            entityManager.persist(qrCodes);
            entityManager.flush();

            Long id = qrCodes.getId();

            BufferedImage qrImage = qrCode.generate_qr_code(id.toString());
            ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", imageOut);

            Image image = Image.getInstance(imageOut.toByteArray());

            PdfPCell cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(10);


            cell.addElement(image);

            table.addCell(cell);
            addedToCurrentPage++;

            if (addedToCurrentPage == 4) {
                document.add(table);

                if (i < count - 1) {
                    document.newPage();
                    table = new PdfPTable(2);
                    table.setWidthPercentage(100);
                    table.setSpacingBefore(10f);
                    table.setSpacingAfter(10f);
                }
                addedToCurrentPage = 0;
            }
        }

        if (addedToCurrentPage > 0) {
            while (addedToCurrentPage < 4) {
                PdfPCell emptyCell = new PdfPCell();
                emptyCell.setBorder(0);
                table.addCell(emptyCell);
                addedToCurrentPage++;
            }
            document.add(table);
        }

        document.close();
    }
}