package application;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.json.JSONObject;

public class CurrencyService {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    public static double getRate(String base, String target) throws Exception {
        String url = API_URL + base;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonObject = new JSONObject(response.body());
        JSONObject rates = jsonObject.getJSONObject("rates");
        return rates.getDouble(target);
    }
}
