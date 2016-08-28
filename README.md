Android-RealtimeUpdate-CleanArchitecture 

=========================
This is a sample project that demostrate Android-CleanArchitecture with realtime update datasource (Base on this https://github.com/android10/Android-CleanArchitecture project)
Within this project I am using Firebase to desmostrate the realtime update data source (maybe in real project it could be socket connection, database ...).


What I have done
-----------------
- Connect to datasource and listent to the data changed.
- Download image, cached to files and Memory.
 
Concerning
-----------------
- Firebase already running in background thread and post data to main thread, but the app still calling Firebase from another background thread.
I just want to demostrate the real time update from a background thread thought.
- Haven't cached data from Firebase yet
- Could be use a better approach to download the image.
- haven't covered all by unit test, just a few sample.
- I am more than welcome to recieve your comment.


Compile 
-----------------
Follow this tutorial "https://firebase.google.com/docs/android/setup" to create your own google-services.json file.
And just import this project by Android Studio.

