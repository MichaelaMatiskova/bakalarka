package sk.tuke.backend.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sk.tuke.backend.service.QrCodesService;

@Controller
@RequestMapping("/api/qrcode/")
public class QrCodeJPAController {

    @Autowired
    private QrCodesService  qrCodesService;

    @ResponseBody
    @GetMapping("/used/{id}")
    public boolean getUsed(@PathVariable String id){
        return qrCodesService.isUsed(Integer.parseInt(id));
    }
}
