Android-RealtimeUpdate-CleanArchitecture 

=========================
This is a sample project that demonstrate Clean Architecture approach with realtime update datasource. 
This project is based on this sample "https://github.com/android10/Android-CleanArchitecture" which instroduced by android10.

Within this project I am using Firebase as the datasource (maybe in real project it could be socket connection, database ...).


What I have done
-----------------
- Connect to datasource and listen to the data changed.
- Download image, cached to files and Memory.
 
Concerning
-----------------
- Firebase already running in background thread and post data to main thread, but the app still calling Firebase from another background thread.
I just want to demostrate the real time update from a background thread thought.
- Could use a better approach to download the image.
- Haven't covered all by unit test, just a few sample.
- I am more than welcome to recieve your comment.


Compile 
-----------------
Follow this tutorial "https://firebase.google.com/docs/android/setup" to create your own google-services.json file.
After that, you have to remove authentication by follow this instruction : https://firebase.google.com/docs/database/security/quickstart.
Update the rule to :

{
  "rules": {
    ".read": "true",
    ".write": "true"
  }
}

And just import this project by Android Studio.

