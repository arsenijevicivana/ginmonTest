package com.ivana.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;
import com.ivana.model.Repository;
import com.ivana.model.User;
import com.ivana.model.UserLanguageCount;

public class JsonHelper {

	/** Method for parsing and mapping json to Repository class **/

	public static List<Repository> parseRepositories(String jsonStr) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Repository> object = mapper.readValue(jsonStr, new TypeReference<List<Repository>>() {
			});
			return object;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** Method for parsing and mapping multiple jsons to model UserLanguageCount **/

	public static List<UserLanguageCount> populateLanguages(List<String> jsonStr, User userId) {

		Map<String, UserLanguageCount> map = new HashMap<String, UserLanguageCount>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			for (String str : jsonStr) {
				JsonNode node = mapper.readTree(str);
				Iterator<String> names = node.getFieldNames();
				while (names.hasNext()) {
					String name = (String) names.next();
					Integer value = node.get(name).asInt();

					if (map.containsKey(name)) {
						UserLanguageCount oldVal = map.get(name);
						oldVal.setCount(oldVal.getCount() + value);
						map.put(name, oldVal);

					} else {

						UserLanguageCount newVal = new UserLanguageCount();
						newVal.setLang(name);
						newVal.setCount(value);
						newVal.setUser(userId);
						map.put(name, newVal);
					}
				}
			}
			List<UserLanguageCount> userLangs = new ArrayList<UserLanguageCount>(map.values());
			return userLangs;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** Creating json from db data **/

	public static String mapJson(List<UserLanguageCount> userLang) {

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		for (UserLanguageCount u : userLang) {
			node.put(u.getLang(), u.getCount());
		}
		return node.toString();
	}

	public static String createResponseJson(int returnCode, String returnMessage) {

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("code", returnCode);
		node.put("message", returnMessage);
		return node.toString();
	}

}
