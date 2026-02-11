package com.marketplace.catalogservice.adapters;

import com.marketplace.catalogservice.adapters.config.AbstractPostgresIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ApplicationTest extends AbstractPostgresIT {

	@Test
	void contextLoads() {
	}
}