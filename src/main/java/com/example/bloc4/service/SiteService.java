package com.example.bloc4.service;

import com.example.bloc4.model.Site;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class SiteService {
	private static final String API_KEY = "my-secure-api-key";

	public List<Site> fetchSitesFromAPI() throws IOException {
		return fetchFromAPI("http://localhost:8080/api/sites", new TypeToken<List<Site>>() {});
	}

	public void postSiteToAPI(Site site) throws IOException {
		sendToAPI("http://localhost:8080/api/sites", "POST", site);
	}

	public void putSiteToAPI(Site site) throws IOException {
		sendToAPI("http://localhost:8080/api/sites/" + site.getId(), "PUT", site);
	}

	public void deleteSiteFromAPI(int siteId) throws IOException {
		URL url = new URL("http://localhost:8080/api/sites/" + siteId);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("DELETE");
		conn.setRequestProperty("API-Key", API_KEY);

		int responseCode = conn.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_NO_CONTENT && responseCode != HttpURLConnection.HTTP_OK) {
			throw new IOException("Failed to delete site: HTTP response code " + responseCode);
		}
	}

	private <T> List<T> fetchFromAPI(String urlString, TypeToken<List<T>> typeToken) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("API-Key", API_KEY);
		conn.connect();

		int responseCode = conn.getResponseCode();
		if (responseCode != 200) {
			throw new IOException("Failed to fetch data: HTTP response code " + responseCode);
		}

		Scanner scanner = new Scanner(conn.getInputStream());
		StringBuilder json = new StringBuilder();
		while (scanner.hasNext()) {
			json.append(scanner.nextLine());
		}
		scanner.close();

		return new Gson().fromJson(json.toString(), typeToken.getType());
	}

	private void sendToAPI(String urlString, String method, Site site) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setRequestProperty("API-Key", API_KEY);
		conn.setDoOutput(true);

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Site.class, new JsonSerializer<Site>() {
					@Override
					public JsonElement serialize(Site src, Type typeOfSrc, JsonSerializationContext context) {
						JsonObject jsonObject = new JsonObject();
						jsonObject.addProperty("name", src.getName());
						return jsonObject;
					}
				})
				.create();

		String jsonInputString = gson.toJson(site);
		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
			os.write(input, 0, input.length);
		}

		int responseCode = conn.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
			throw new IOException("Failed to " + method.toLowerCase() + " site: HTTP response code " + responseCode);
		}
	}
}