package nextradio.nranalytics.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkondati on 10/31/2017.
 */

class NrUGsonConverter {
    private static final String TAG = "NrUGsonConverter";

    private static NrUGsonConverter instance;

    static NrUGsonConverter getInstance() {
        if (instance == null) {
            instance = new NrUGsonConverter();
        }
        return instance;
    }

    <T> String convertObjectToString(String savedValues, T actionPayload) {
        List<T> actionList = convertStringToArrayList(savedValues);
        actionList.add(actionPayload);
        return getGson().toJson(actionList, getType());
    }

    private <T> List<T> convertStringToArrayList(String savedValues) {
        if (!savedValues.isEmpty()) {
            return getGson().fromJson(savedValues, getType());
        } else {
            return new ArrayList<>();
        }
    }

    <T> List<T> getSavedData(String savedData) {
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(savedData, listType);
    }


    String createJsonObjectToString(String savedValues, JSONObject newDataJson) {
        try {
            JSONArray jsonArray;
            if (savedValues.isEmpty()) {
                jsonArray = new JSONArray();
            } else {
                jsonArray = new JSONArray(savedValues);
            }
            jsonArray.put(newDataJson);
            return jsonArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    String updateJsonObject(String savedValues, JSONObject newDataJson) {
        try {
            JSONArray jsonArray = new JSONArray(savedValues);
            int length = jsonArray.length();
            jsonArray.put(length - 1, newDataJson);
            return jsonArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    JSONObject updateSessionEndTime(String savedValues, String endTime) {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(savedValues);
            JSONObject jsonObject = new JSONObject(jsonArray.get(jsonArray.length() - 1).toString());
            jsonObject.put("endTime", endTime);

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    JSONObject giveMeJsonObject(String savedValues) {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(savedValues);
            return new JSONObject(jsonArray.get(jsonArray.length() - 1).toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T> List<T> addDataToList(List<T> actionList, T newValueToSave) {
        actionList.add(newValueToSave);
        return actionList;
    }

    private Gson getGson() {
        return new Gson();
    }

    private <T> Type getType() {
        return new TypeToken<List<T>>() {
        }.getType();
    }

    <T> String serializeToJson(T myClass) {
        Gson gson = new Gson();
        return gson.toJson(myClass);
    }

    // Deserialize to single object.
    <T> T deserializeFromJson(String jsonString, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, classOfT);
    }
}
