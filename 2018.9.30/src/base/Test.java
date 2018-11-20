package base;

import java.io.IOException;
import java.net.URI;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

public class Test {
	
	public static void main(String[] args) {
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://www.baidu.com")).GET().build();
        HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandler.asString());
			System.out.println(response.statusCode());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
