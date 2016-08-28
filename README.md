Android-RealtimeUpdate-CleanArchitecture 

=========================
This is a sample project that demostrate Android-CleanArchitecture with realtime update datasource (Base on this https://github.com/android10/Android-CleanArchitecture project)
Wethin this project I am using Firebase to desmostrate the realtime update data source (maybe in real project it could be socket connection, database ...).


What I have done
-----------------
- Connect to datasource and listent to the data changed.
- Download image, cached to files and Memory cache.
 
Concerning
-----------------
- Firebase already running in background thread and post data to main thread, but the app still calling Firebase from another background thread.
I just want to demostrate the real time update from a background thread thought.
- Could be use a better approach to download the image.
- I am more than welcome to recieve your comment.
- haven't covered by unit test, just a few sample.



Reseach about Chat app.
-----------------
As I researched, the best approach for Chat app in Android should be listening data change in a Service (maybe the service should run in other process) and write data to Database then update the UI base on the database changed.


Compile 
-----------------
Please create your own google-services.json file.