import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolutionTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Solution withBody(String json) {
        return new Solution(country -> MAPPER.readTree(json));
    }

    @Test
    void singleCallingCode() {
        Solution s = withBody("{\"data\":[{\"name\":\"Afghanistan\",\"callingCodes\":[\"93\"]}]}");
        assertEquals("+93 656445445", s.getPhoneNumbers("Afghanistan", "656445445"));
    }

    @Test
    void multipleCallingCodesUsesHighestIndex() {
        Solution s = withBody("{\"data\":[{\"name\":\"Puerto Rico\",\"callingCodes\":[\"1787\",\"1939\"]}]}");
        assertEquals("+1939 564593986", s.getPhoneNumbers("Puerto Rico", "564593986"));
    }

    @Test
    void notFoundReturnsMinusOne() {
        Solution s = withBody("{\"data\":[]}");
        assertEquals("-1", s.getPhoneNumbers("Oceania", "987574876"));
    }
}
