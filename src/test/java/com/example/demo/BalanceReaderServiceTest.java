package com.example.demo;

import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BalanceReaderServiceTest {

    @Test
    void readBalances_fromClasspath() throws Exception {
        BalanceReaderService service = new BalanceReaderService();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("03030364.xml")) {
            assertNotNull(in, "Test XML not found on classpath");
            List<BalanceReaderService.BalInfo> balances = service.readBalances(in);
            assertTrue(balances.size() >= 2, "Should parse at least 2 <Bal> nodes");
        }
    }
}
