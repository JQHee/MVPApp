package com.example.testmvpapp.component.net.file;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartBodyCreator {

    /**
     * File对象转化成MultipartBody
     */
    public static MultipartBody filesToMultipartBody(FileParentBody fileParentBody) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        if ((fileParentBody == null || fileParentBody.getBodys() == null)) {
            MultipartBody multipartBody = builder.build();
            return multipartBody;
        }
        for (FileBody body : fileParentBody.getBodys()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse(fileParentBody.getMediaType()), body.getFile());
            builder.addFormDataPart(fileParentBody.getKey(), body.getFile().getName(), requestBody);
        }


        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    /**
     * File转化成MultipartBody.Part
     */
    public static List<MultipartBody.Part> filesToMultipartBodyParts(FileParentBody fileParentBody) {
        List<MultipartBody.Part> parts = new ArrayList<>((fileParentBody == null || fileParentBody.getBodys() == null) ? 0 : fileParentBody.getBodys().size());
        if ((fileParentBody == null || fileParentBody.getBodys() == null)) {
            return parts;
        }
        for (FileBody body : fileParentBody.getBodys()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse(fileParentBody.getMediaType()), body.getFile());
            MultipartBody.Part part = MultipartBody.Part.createFormData(fileParentBody.getKey(), body.getFile().getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    /*
    * 参数和文件通过表单拼接
    */
    public static MultipartBody paramsAndFilesToMultipartBody(WeakHashMap<String, Object>params, FileParentBody fileParentBody) {
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        //遍历map中所有参数到builder
        if (params != null){
            for (String key : params.keySet()) {
                multipartBodyBuilder.addFormDataPart(key, params.get(key).toString());
            }
        }
        // 遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (fileParentBody != null && fileParentBody.getBodys() != null){
            for (FileBody body : fileParentBody.getBodys() ) {
                multipartBodyBuilder.addFormDataPart(fileParentBody.getKey(), body.getFile().getName(), RequestBody.create(MediaType.parse(fileParentBody.getMediaType()), body.getFile()));
            }
        }
        // 构建请求体
        MultipartBody requestBody = multipartBodyBuilder.build();
        return requestBody;
    }

}


