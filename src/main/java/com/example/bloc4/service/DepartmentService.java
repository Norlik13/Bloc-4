package com.example.bloc4.service;

import com.example.bloc4.model.Department;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class DepartmentService {

	public List<Department> fetchDepartmentsFromAPI() throws IOException {
		return fetchFromAPI("http://localhost:8080/api/departments", new TypeToken<List<Department>>() {});
	}

	public void postDepartmentToAPI(Department department) throws IOException {
		sendToAPI("http://localhost:8080/api/departments", "POST", department);
	}

	public void updateDepartmentInAPI(Department department) throws IOException {
		sendToAPI("http://localhost:8080/api/departments/" + department.getId(), "PUT", department);
	}

	public void deleteDepartmentFromAPI(int departmentId) throws IOException {
		URL url = new URL("http://localhost:8080/api/departments/" + departmentId);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("DELETE");

		int responseCode = conn.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
			throw new IOException("Failed to delete department: HTTP response code " + responseCode);
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

	private void sendToAPI(String urlString, String method, Department department) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json; utf-8");
		conn.setDoOutput(true);

		String jsonInputString = new Gson().toJson(department);
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