package com.mygdx.gama6map.utils;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mygdx.gama6map.model.Transaction;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiRequests {

    public static List<Transaction> fetchTransactionsFromDB() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/transactions/getAll")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();

            // Parse the JSON response to a List of Transaction objects
            return new Gson().fromJson(responseData, new TypeToken<List<Transaction>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
