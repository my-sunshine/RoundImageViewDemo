# RoundImageViewDemo

## Demo 图片
![](https://github.com/my-sunshine/RoundImageViewDemo/raw/master/app/img/img1.png)  

##使用gradle 依赖:
```java
   compile 'com.liyi:circleimageview:0.1.1'
```

##使用说明：
```java
       <com.liyi.circleimageview.CircleImageView
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/profile_image12"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:paddingLeft="10dp"
        android:paddingRight="10dp"
        android:layout_marginTop="20dp"
        android:layout_gravity="center_horizontal"
        android:src="@drawable/test"
        app:round_border_width="2dp"
        app:round_border_color="#b44242"
        app:round_type="round"
        app:round_radius="10dp"

        />


    <com.liyi.circleimageview.CircleImageView
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/profile_imag1e12"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:paddingLeft="10dp"
        android:paddingRight="10dp"
        android:layout_gravity="center_horizontal"
        android:src="@drawable/test"
        app:round_border_width="2dp"
        app:round_border_color="#b44242"

        />
```

##参考文件：

[hdodenhof/CircleImageView](https://github.com/hdodenhof/CircleImageView "hdodenhof/CircleImageView")  

[Android BitmapShader 实战 实现圆形、圆角图片](http://blog.csdn.net/lmj623565791/article/details/41967509/ "Android BitmapShader 实战 实现圆形、圆角图片示")  
