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
