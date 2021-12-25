package com.example.news.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.UserSpace.MyAppreciationActivity;
import com.example.news.UserSpace.MyCollectActivity;
import com.example.news.UserSpace.MyHistoryToNewsActivity;
import com.example.news.UserSpace.MyInfoActivity;
import com.example.news.UserSpace.MyIssueActivity;
import com.example.news.bean.CommentsBean;
import com.example.news.bean.MyInfoBean;
import com.example.news.bean.PostPhotoBean;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class MyOwnPage extends Fragment implements View.OnClickListener {
    private Button button_name;private Button sighout;
    public static final int TAKE_PHOTO = 1;
    private ImageView imageview_myself;
    private Uri imageUri;
    public static final int CHOOSE_PHOTO = 2;
    private PopupWindow pop;
    private RxPermissions rxPermissions;
    private boolean hasPermissions = false;
    private Button pickphoto;
    private LinearLayout my_issue;private LinearLayout my_interest;private LinearLayout my_info;
    private LinearLayout my_fans;private LinearLayout my_history;
    private LinearLayout my_download;private LinearLayout my_collect;private LinearLayout my_appreciation;
    SharedPreferences preferences;
    private String token;
    private Button button_change;

    private static final String TAG = "MainActivity";
    private final int IMAGE_RESULT_CODE = 2;
    private final int PICK = 1;
    private String url = "";//此处为上传图片地址，我就不写了
    private String imgString = "";
    private Intent intent;
    private Uri uri;
    private String imagePath;
    private File outputImage;
    private int img_id;
    private String path;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myownpage, container, false);
        preferences = getActivity().getSharedPreferences("LoginData", MODE_PRIVATE);
        token=preferences.getString("token","null");
        get_info();
        my_appreciation = view.findViewById(R.id.my_appreciation);
        sighout = view.findViewById(R.id.sighout);
        imageview_myself = view.findViewById(R.id.imageview_myself);
        button_change = view.findViewById(R.id.btn_change);
        my_collect = view.findViewById(R.id.my_collect);
//        my_download = view.findViewById(R.id.my_download);
//        my_fans = view.findViewById(R.id.my_fans);
        my_info = view.findViewById(R.id.my_info);
//        my_interest = view.findViewById(R.id.my_interest);
        my_issue = view.findViewById(R.id.my_issue);
        my_history = view.findViewById(R.id.my_history);
        button_name = view.findViewById(R.id.name_change);
        Init();
        String username=preferences.getString("username","Null");
        button_name.setText(username);
        Log.d("preferences",preferences.toString());
        return view;
    }

    private void Init(){
        my_appreciation.setOnClickListener(this);
        my_collect.setOnClickListener(this);
        button_change.setOnClickListener(this);
//        my_download.setOnClickListener(this);
//        my_fans.setOnClickListener(this);
        my_info.setOnClickListener(this);
//        my_interest.setOnClickListener(this);
        my_issue.setOnClickListener(this);
        my_history.setOnClickListener(this);
        sighout.setOnClickListener(this);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_change:
                showPop();
                break;
            case R.id.my_appreciation://我的点赞
                Intent intent = new Intent(getActivity(), MyAppreciationActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.my_collect://我的收藏
                Intent intent_collect = new Intent(getActivity(), MyCollectActivity.class);
                getActivity().startActivity(intent_collect);
                break;
            case R.id.my_info://我的信息
                Intent intent_info = new Intent(getActivity(), MyInfoActivity.class);
                getActivity().startActivity(intent_info);
                break;
//            case R.id.my_interest://我的关注
//                Intent intent_interest = new Intent(getActivity(), MyInterestActivity.class);
//                getActivity().startActivity(intent_interest);
//                break;
            case R.id.my_issue://我的发布
                Bundle bundle_issue = new Bundle();
                bundle_issue.putString("token",token);
                Intent intent_issue = new Intent(getActivity(), MyIssueActivity.class);
                intent_issue.putExtras(bundle_issue);
                getActivity().startActivity(intent_issue);
                break;
//            case R.id.my_download://我的下载
//                Intent intent_download = new Intent(getActivity(), MyDownloadActivity.class);
//                getActivity().startActivity(intent_download);
//                break;
//            case R.id.my_fans://我的粉丝
//                Intent intent_fans = new Intent(getActivity(), MyFansActivity.class);
//                getActivity().startActivity(intent_fans);
//                break;
            case R.id.my_history://我的历史
                Bundle bundle = new Bundle();
                bundle.putString("token",token);
                Log.d("token",token);
                Intent intent_history = new Intent(getActivity(), MyHistoryToNewsActivity.class);
                intent_history.putExtras(bundle);
                getActivity().startActivity(intent_history);
                break;
            case R.id.sighout:
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().commit();
                Log.d("preference",preferences.getString("token","?"));
                getActivity().finish();
                break;
        }
    }
    public void commit(JSONObject jsonObject){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/self/avatar-upload")
                        .method("POST", body)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .addHeader("Content-Type", "application/json")
                        .build();
                Log.d("request", String.valueOf(request));
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("onFailure",e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        Log.d("id", result);
                        CommentsBean commentsBean = gson.fromJson(result,CommentsBean.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(commentsBean.getCode()==1000)
                                {
                                    Toast.makeText(getActivity(), "发表成功！", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(getActivity(), "发表失败！", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
            }
        });
        thread.start(); try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
    }
    private void get_info()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/self/info")
                        .method("GET", null)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("onFailure",e.getMessage());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("result",result);
                        Gson gson = new Gson();
                        MyInfoBean myInfoBean = gson.fromJson(result,MyInfoBean.class);
                        if(!myInfoBean.equals(""))
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(myInfoBean != null && myInfoBean.getData() != null)
                                    {
                                        Log.d("avatar",myInfoBean.getData().getAvatar());
                                        Glide.with(getActivity()).load(myInfoBean.getData().getAvatar()).into(imageview_myself);
                                    }
                                }

                            });

                        }

                    }

                });

            }

        });
        thread.start();
        try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    //上传图片文件的操作
    public void post_photo(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("img", String.valueOf(path),
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        new File(String.valueOf(outputImage))))
                        .addFormDataPart("type", String.valueOf(1))
                        .build();
                Request request = new Request.Builder()
                        .url("http://39.106.195.109/itnews/api/img-upload")
                        .method("POST", body)
                        .addHeader("Authorization", token)
                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("onFailure",e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        Log.d("id", result);
                        PostPhotoBean postPhotoBean = gson.fromJson(result,PostPhotoBean.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(postPhotoBean.getCode()==1000)
                                {
                                    img_id = postPhotoBean.getData().getImg_id();
                                    Log.d("image", String.valueOf(img_id));
                                    Toast.makeText(getActivity(), "发表成功！", Toast.LENGTH_SHORT).show();
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("img_id",img_id);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    commit(jsonObject);

                                }
                                else
                                    Toast.makeText(getActivity(), "发表失败！", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
            }
        });
        thread.start(); try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
    }
    private void showPop() {
        View bottomView = View.inflate(getActivity(), R.layout.dialog_bottom, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        outputImage = new File(getActivity().getExternalCacheDir(),"output_image.jpg");
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                            //相册中的照片都是存储在SD卡上的，需要申请运行时权限，WRITE_EXTERNAL_STORAGE是危险权限，表示同时授予程序对SD卡的读和写的能力
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }else {
                            openAlbum();

                        }
                        break;
                    case R.id.tv_camera:
                        //拍照
                        outputImage = new File(getActivity().getExternalCacheDir(),"output_image.jpg");
                        Log.d("image", String.valueOf(outputImage));
                        try{
                            if(outputImage.exists())
                                outputImage.delete();
                            outputImage.createNewFile();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        if(Build.VERSION.SDK_INT >=24){
                            imageUri = FileProvider.getUriForFile(getActivity(),
                                    "com.example.cameraalbumtest.fileprovider",outputImage);
                        }else{
                            imageUri = Uri.fromFile(outputImage);
                        }
                        path = imageUri.getPath();
                        Log.d("image",path);
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,TAKE_PHOTO);

                        break;
                    case R.id.tv_cancel:
                        //取消
                        closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };
        mCamera.setOnClickListener(clickListener);
        mAlbum.setOnClickListener(clickListener);

        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        imageview_myself.setImageBitmap(bitmap);
                        post_photo();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK) {
                    //因为sdk19以后返回的数据不同，所以要根据手机系统版本进行不同的操作
                    //判断手机系统版本
                    if(Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKiKai(data);
                        post_photo();
                    }else {
                        handleImageBeforeKiKai(data);
                        post_photo();
                    }
                }
                break;
            default:
                break;
        }
    }
    //>=19的操作
    @TargetApi(19)
    private void handleImageOnKiKai(Intent data) {
        path = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(getActivity(), uri)) {
            //如果是Document类型的Uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(contentUri, null);
            }else if("content".equalsIgnoreCase(uri.getScheme())) {
                //不是document类型的Uri，普通方法处理
                path = getImagePath(uri, null);
            }
            displayImage(path);

        }
    }

    //<19的操作
    private void handleImageBeforeKiKai(Intent data) {
        Uri uri = data.getData();
        String path = getImagePath(uri, null);
        displayImage(path);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri 和selection获取真正的图片路径
        Cursor cursor = getActivity().getContentResolver().query(
                uri, null, selection, null, null);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                path = cursor.getString(
                        cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String path) {
        if(path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageview_myself.setImageBitmap(bitmap);
        }else {
            Toast.makeText(getActivity(), "Load Failed", Toast.LENGTH_LONG).show();
        }
    }
}
