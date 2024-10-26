
# Political Preparedness

This application provides comprehensive voting and election information, helping users access
polling locations, election dates, and political resources. With it, users can explore details about
their federal, state, and local representatives, empowering informed civic engagement at all levels
of government.

## Getting Started

This voter preparedness app provides users with easy access to election and representative information through three main screens:

1.	**Launcher Screen:** The app’s landing page, where users can choose to explore Election Info or Find Representatives for quick navigation.
2.	**Election and Voter Info:** In this section, users can browse a list of upcoming elections. Each election item is clickable, revealing comprehensive details like the election authority’s address, election name, and links to state election resources, polling locations, and sample ballots (when available). Users can also choose to follow or unfollow elections, with their selections reflected in the main election list for easy tracking.
3.	**Find Representatives:** This screen allows users to access a list of their federal, state, and local representatives. Based on the user’s location (auto-detected or manually entered), the app displays representatives’ details, including name, position, party affiliation, and social media or website links.

Overall, this app makes it easy for users to stay informed on upcoming elections and engage with their government representatives at all levels.



### Dependencies
1. Retrofit 
   ```
   "com.github.bumptech.glide:glide:4.12.0
   ```
2. Moshi 
   ```
   "com.squareup.moshi:moshi:$version_moshi"
   "com.squareup.moshi:moshi-kotlin:$version_moshi"
   "com.squareup.moshi:moshi-adapters:$version_moshi"
   "com.squareup.okhttp3:okhttp:5.0.0-alpha.14"
   "com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14"
   ```
3. Glide
   ```
   "com.github.bumptech.glide:glide:4.12.0"
   ```
4. RecyclerView
   ```
   "androidx.room:room-runtime:$version_room"
   "androidx.room:room-compiler:$version_room"
   "androidx.room:room-ktx:2.6.1"
   ```
5. Room
   ```
    "androidx.room:room-runtime:$version_room"
    "androidx.room:room-compiler:$version_room"
    "androidx.room:room-ktx:2.6.1"
   ``` 

### Installation
1. [Android Studio](https://developer.android.com/studio)
2. Install [JDK 21](https://sdkman.io/install/) and set `JAVA_HOME` environment variable
3. Clone the project
    ```
   git clone https://github.com/diego5456/udacity-political-preparedness.git
   ```
4. Checkout `Master` branch
   ```
   git checkout master
   ```
5. Import project to Android Studio, sync the project in gradle. Once synced click `Run` on a device
   * Minimum SDK is 22 (Android Lollipop)
   * Target SDK is 33 (Android 13)

[//]: # (## Testing)

[//]: # ()
[//]: # (Explain the steps needed to run any automated tests)

[//]: # ()
[//]: # (### Break Down Tests)

[//]: # ()
[//]: # (Explain what each test does and why)

[//]: # ()
[//]: # (```)

[//]: # ()
[//]: # (Examples here)

[//]: # ()
[//]: # (```)

## Project Instructions

### Android UI/UX
* Navigable interface consisting of multiple screens of functionality and data.
* Animate UI components to better utilize screen real estate and create engaging content.
* Construct interfaces that adhere to Android standards and display appropriately on screens of different size and resolution.

### Local and Network data
* Connect to and consume data from a remote data source such as a RESTful API.
* Load network resources, such as Bitmap Images, dynamically and on-demand.
* Store data locally on the device for use between application sessions and/or offline use.

### Android system and hardware integration
* Architect application functionality using MVVM.
* Implement logic to handle and respond to hardware and system events that impact the Android Lifecycle.
* Utilize system hardware to provide the user with advanced functionality and features.



## Built With

* [Android Studio](https://developer.android.com/studio)
* [Kotlin](https://github.com/diego5456/udacity-loading-app-project#:~:text=build%20android%20apps-,Kotlin,-%2D%20Default%20language%20used_)
*  [JDK 21](https://sdkman.io/install/)

Include all items used to build project.

## License
