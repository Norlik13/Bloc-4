package com.example.bloc4.service;

import com.example.bloc4.model.Department;
import com.example.bloc4.model.Site;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class DepartmentService {
	private static final String API_KEY = "my-secure-api-key";

	public List<Department> fetchDepartmentsFromAPI() throws IOException {
		return fetchFromAPI("http://localhost:8080/api/departments", new TypeToken<List<Department>>() {});
	}

	public void postDepartmentToAPI(Department department) throws IOException {
		sendToAPI("http://localhost:8080/api/departments", "POST", department);
	}

	public void putDepartmentToAPI(Department department) throws IOException {
		sendToAPI("http://localhost:8080/api/departments/" + department.getId(), "PUT", department);
	}

	public void deleteDepartmentFromAPI(int departmentId) throws IOException {
		URL url = new URL("http://localhost:8080/api/departments/" + departmentId);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("DELETE");
		conn.setRequestProperty("API-Key", API_KEY);

		int responseCode = conn.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
			throw new IOException("Failed to delete department: HTTP response code " + responseCode);
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

	private void sendToAPI(String urlString, String method, Department department) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setRequestProperty("API-Key", API_KEY);
		conn.setDoOutput(true);

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Department.class, new JsonSerializer<Department>() {
					@Override
					public JsonElement serialize(Department src, Type typeOfSrc, JsonSerializationContext context) {
						JsonObject jsonObject = new JsonObject();
						jsonObject.addProperty("name", src.getName());
						return jsonObject;
					}
				})
				.create();

		String jsonInputString = gson.toJson(department);
		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
			os.write(input, 0, input.length);
		}

		int responseCode = conn.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
			throw new IOException("Failed to " + method.toLowerCase() + " department: HTTP response code " + responseCode);
		}
	}
}