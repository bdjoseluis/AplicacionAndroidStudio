package com.backend.TGF;

import com.backend.TGF.Controller.TFGController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class TgfApplicationTests {
	@InjectMocks
	private TFGController tfgController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void contextLoads() {
	}

}
