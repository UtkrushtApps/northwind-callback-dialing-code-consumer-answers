import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Resolves a country's international calling code and prefixes a phone number.
 */
public class Solution {

    private static final String API_BASE = "https://jsonmock.hackerrank.com/api/countries";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** Retrieves the upstream JSON body for a country name. */
    public interface Fetcher {
        JsonNode fetch(String country) throws Exception;
    }

    private final Fetcher fetcher;

    /** Production constructor: queries the live country service. */
    public Solution() {
        this.fetcher = country -> {
            String url = API_BASE + "?name=" + URLEncoder.encode(country, StandardCharsets.UTF_8);
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(HttpRequest.newBuilder(URI.create(url)).GET().build(),
                            HttpResponse.BodyHandlers.ofString());
            return MAPPER.readTree(response.body());
        };
    }

    /** Test constructor: injects a stub fetcher so tests stay offline. */
    public Solution(Fetcher fetcher) {
        this.fetcher = fetcher;
    }

    /**
     * Resolve the country's calling code and return "+<Calling Code> <Phone Number>".
     * The upstream body has a "data" array holding exactly one record when the
     * country is found and empty when it is not. A record has "name" and
     * "callingCodes" (array of strings). When callingCodes has more than one
     * entry, use the one at the highest index. When data is empty, return the
     * literal string "-1".
     */
    public String getPhoneNumbers(String country, String phoneNumber) {
        try {
            JsonNode body = fetcher.fetch(country);
            JsonNode data = body == null ? null : body.get("data");

            if (data == null || !data.isArray() || data.isEmpty()) {
                return "-1";
            }

            JsonNode firstRecord = data.get(0);
            JsonNode callingCodes = firstRecord == null ? null : firstRecord.get("callingCodes");

            if (callingCodes == null || !callingCodes.isArray() || callingCodes.isEmpty()) {
                return "-1";
            }

            String callingCode = callingCodes.get(callingCodes.size() - 1).asText();
            return "+" + callingCode + " " + phoneNumber;
        } catch (Exception e) {
            return "-1";
        }
    }
}
