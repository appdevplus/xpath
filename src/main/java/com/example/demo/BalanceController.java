package com.example.demo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/balances")
public class BalanceController {

    private final BalanceReaderService service;

    public BalanceController(BalanceReaderService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BalanceReaderService.BalInfo> getBalancesFromClasspath() throws Exception {
        var resource = new ClassPathResource("03030364.xml");
        try (InputStream in = resource.getInputStream()) {
            return service.readBalances(in);
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BalanceReaderService.BalInfo> getBalancesFromUpload(@RequestPart("file") MultipartFile file) throws Exception {
        try (InputStream in = file.getInputStream()) {
            return service.readBalances(in);
        }
    }
}
