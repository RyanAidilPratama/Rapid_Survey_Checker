# RAPID SURVEY CHECKER

Rapid Checker is an Android application used to track the progress of rapid survey mapping in the research of Batch 59 Politeknik Statistika STIS.

## Screenshots
#### - Home Activity
![home_activity](http://i.ibb.co/TTr7tMp/home.png)

#### - Detail Building Activity/Halaman Lihat Bangunan
![detail_building_activity](http://i.ibb.co/s23WWDQ/halaman-bangunan.png)

#### - Show Mapping Path
![show_mapping_path](http://i.ibb.co/17MzZVx/lihat-jalur-mapping.png)

#### - Show All Markers
![show_all_markers](http://i.ibb.co/hdTWM6z/lihat-semua-titik-mapping.png)

#### - Show Only BC (Bangunan Campuran) Marker
![show_bc_marker](http://i.ibb.co/xg7JJLJ/lihat-bangunan-campuran.png)

#### - Show Only BTT (Bangunan Tempat Tinggal) Marker
![show_btt_marker](http://i.ibb.co/Zz8w9hz/lihat-bangunan-tempat-tinggal.png)

## - Usage
Clone this repository into your local machine using git bash or directly from Android Studio. 
This application need several libraries :
``` gradle
    //ViewModel and LiveData
    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'

    //AsyncHttp
    implementation 'com.loopj.android:android-async-http:1.4.10'

    //Glide
    implementation 'com.github.bumptech.glide:glide:4.11.0'

    //Material Design
    implementation 'com.google.android.material:material:1.1.0'
    
    //Google Maps Services
    implementation 'com.google.android.gms:play-services-maps:17.0.0'

    //RXJava
    implementation 'io.reactivex.rxjava2:rxjava:2.2.18'
    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
```

Don't forget to change some of this line :
1. #### java/data/helper/StaticFinal.java
``` java
public class StaticFinal {
    public static final String BASE_URL = "Your Api URL goes here";
}
```

2. #### res/values/strings.xml
```` xml
    <string name="API_KEY" translatable="false">Your Api Key</string>
    <string name="base_url_image" translatable="false">https://baseurlimage.com/%s.jpg</string>

```` 

You can get your Google Service API KEY from [here](https://console.cloud.google.com)

## - Future
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.


## License
[MIT](https://choosealicense.com/licenses/mit/)