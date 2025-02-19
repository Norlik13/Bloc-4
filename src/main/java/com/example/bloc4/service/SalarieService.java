package com.example.bloc4.service;

import com.example.bloc4.model.Salarie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class SalarieService {

	public List<Salarie> fetchSalariesFromAPI() throws IOException {
		return fetchFromAPI("http://localhost:8080/api/salaries", new TypeToken<List<Salarie>>() {});
	}

	public void postSalarieToAPI(Salarie salarie) throws IOException {
		sendToAPI("http://localhost:8080/api/salaries", "POST", salarie);
	}

	public void updateSalarieInAPI(Salarie salarie) throws IOException {
		sendToAPI("http://localhost:8080/api/salaries/" + salarie.getId(), "PUT", salarie);
	}

	public void deleteSalarieFromAPI(int salarieId) throws IOException {
		String urlString = "http://localhost:8080/api/salaries/" + salarieId;
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("DELETE");

		int responseCode = connection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			throw new IOException("Failed to delete salarie: HTTP response code " + responseCode);
		}
	}

	private <T> List<T> fetchFromAPI(String urlString, TypeToken<List<T>> typeToken) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();

		int responseCode = conn.getResponseCode();
		if (responseCode != 200) {
			throw new IOException("Failed to fetch data: HTTP response code " + responseCode);
		}

		Scanner scanner = new Scanner(url.openStream());
		StringBuilder json = new StringBuilder();
		while (scanner.hasNext()) {
			json.append(scanner.nextLine());
		}
		scanner.close();

		return new Gson().fromJson(json.toString(), typeToken.getType());
	}

	private void sendToAPI(String urlString, String method, Salarie salarie) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setDoOutput(true);

		String jsonInputString = new Gson().toJson(salarie);
		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
			os.write(input, 0, input.length);
		}

		int responseCode = conn.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
			throw new IOException("Failed to " + method.toLowerCase() + " salarie: HTTP response code " + responseCode);
		}
	}
}