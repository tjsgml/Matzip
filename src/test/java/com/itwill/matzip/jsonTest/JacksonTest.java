package com.itwill.matzip.jsonTest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwill.matzip.dto.BusinessHourUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class JacksonTest {
    @Test
    public final void givenJsonWithInvalidList_whenDeserializing_thenThrowException() throws JsonParseException, IOException {
        String json = "{{\"bhourId\":\"1\",\"isHoliday\":\"true\"}}";
        ObjectMapper mapper = new ObjectMapper();
        BusinessHourUpdateDto businessHourUpdateDto= mapper.reader()
                .forType(BusinessHourUpdateDto.class)
                .readValue(json);

        log.info("BusinessHourUpdateDto = {}", businessHourUpdateDto);

        Exception exception = assertThrows(JsonMappingException.class, () -> mapper.reader()
                .forType(BusinessHourUpdateDto.class)
                .readValue(json));

        log.info("exception={}", exception);
        assertTrue(exception.getMessage()
                .contains("Cannot deserialize value of type `java.util.ArrayList<java.lang.String>`"));
    }

}
