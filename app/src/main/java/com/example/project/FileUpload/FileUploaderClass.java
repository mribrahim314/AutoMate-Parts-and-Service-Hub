package com.example.project.FileUpload;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploaderClass {

    public interface onSuccessfulTask {
        void onSuccess();
        void onFailed(String error);
    }

    public static void uploadFile(String filePath, String folder_name, int user_id, onSuccessfulTask task) {
        try {
            AppFilesService apiInterface = RetrofitApiClient.getClient().create(AppFilesService.class);
            File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
            MultipartBody.Part folder = MultipartBody.Part.createFormData("folder", folder_name);
            MultipartBody.Part userId = MultipartBody.Part.createFormData("user_id", String.valueOf(user_id));

            Call<String> call = apiInterface.UploadFile(body, folder, userId);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    String responseBody = response.body();
                    Log.d("test",response.message());
                    if (responseBody != null) {
                        if ("ok".equals(responseBody)) {
                            task.onSuccess();
                            Log.d("test",response.message());
                        } else {
                            task.onFailed(responseBody);
                            Log.d("test",response.message());
                        }
                    } else {
                        task.onFailed(null);
                        Log.d("test",response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    task.onFailed(t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("FileUploader", "Error uploading file", e);
            task.onFailed(e.getMessage());
        }
    }
}
