package base;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Test {
	
	public static void main(String[] args) {
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://www.baidu.com")).GET().build();
        HttpResponse<String> response;
		try {
			Long aLong = System.nanoTime();
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(System.nanoTime() -aLong);
			System.out.println(response.statusCode());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
