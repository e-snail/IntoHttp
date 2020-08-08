package into.http;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            imageView.setImageBitmap((Bitmap) msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);

        testHttps(this);
    }

    void testHttps(final Context context) {

        String urlHttps = "https://img01.sogoucdn.com/app/a/200953/bisheng_1266fa01590d4cabb33637cbd3388daa_5Py9BP";

        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .get()
                .url(urlHttps)
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //将响应数据转化为输入流数据
                InputStream inputStream=response.body().byteStream();
                //将输入流数据转化为Bitmap位图数据
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                File file=new File(context.getApplicationContext().getFilesDir() + "/" + "1.png");
                file.createNewFile();
                //创建文件输出流对象用来向文件中写入数据
                FileOutputStream out=new FileOutputStream(file);
                //将bitmap存储为jpg格式的图片
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                //刷新文件流
                out.flush();
                out.close();

                Message msg=Message.obtain();
                msg.obj=bitmap;
                handler.sendMessage(msg);
            }
        });
    }


}
