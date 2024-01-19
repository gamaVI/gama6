package com.mygdx.gama6map.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mygdx.gama6map.model.Transaction;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiRequests {

    public static List<Transaction> fetchTransactionsFromDB() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:3000/api/transactions/getAll")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseData = response.body().string();
            System.out.println(responseData);

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Transaction>>(){}.getType();
            return gson.fromJson(responseData, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

