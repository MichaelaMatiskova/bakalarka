package sk.tuke.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
public class QRCodePDFGeneratorServiceRestClient {

    @Autowired
    private QRCodePDFGeneratorServiceJPA qrCodeService;

    @GetMapping("/generate")
    public ResponseEntity<String> generateQRs(@RequestParam(defaultValue = "10") int count) {
        System.out.println(count);
        try {
            qrCodeService.generatePDFWithQRCodes(count);
            return ResponseEntity.ok("QR kódy úspešne vygenerované do qr_codes.pdf");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Chyba pri generovaní QR kódov: " + e.getMessage());
        }
    }
}
