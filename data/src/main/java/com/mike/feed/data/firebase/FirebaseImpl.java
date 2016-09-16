package com.mike.feed.data.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.mike.feed.data.entity.DeletedEntity;
import com.mike.feed.data.entity.EventType;
import com.mike.feed.data.entity.FeedChangedInfoEntity;
import com.mike.feed.data.entity.FeedEntity;
import com.mike.feed.data.entity.WrittenEntity;

import java.util.HashMap;
import java.util.Map;

import rx.AsyncEmitter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * Created by MinhNguyen on 8/24/16.
 */


public class FirebaseImpl implements Firebase {

    DatabaseReference database;

    public FirebaseImpl(DatabaseReference database) {
        this.database = database;
    }


    @Override
    public Observable<FeedChangedInfoEntity> registerFeedChangedEvent() {

        return Observable.fromAsync(new Action1<AsyncEmitter<FeedChangedInfoEntity>>() {
            @Override
            public void call(AsyncEmitter<FeedChangedInfoEntity> feedChangedInfoEntityAsyncEmitter) {

                Timber.v(String.format("fromAsync create : %s", Thread.currentThread().getName()));

                // those of registered method will be called in a background thread
                final Query query = database.child(Firebase.QUERY_FEEDS);
                final ChildEventListener listener = new FeedChangeListener(feedChangedInfoEntityAsyncEmitter);

                query.addChildEventListener(listener);

                feedChangedInfoEntityAsyncEmitter.setCancellation(new AsyncEmitter.Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        database.removeEventListener(listener);
                    }
                });
            }
        }, AsyncEmitter.BackpressureMode.BUFFER);


    }



    @Override
    public Observable<WrittenEntity> writeFeed(final FeedEntity entity) {

        return Observable.fromAsync(new Action1<AsyncEmitter<WrittenEntity>>() {
            @Override
            public void call(final AsyncEmitter<WrittenEntity> writtenEntityAsyncEmitter) {
                final String key = database.child(Firebase.QUERY_FEEDS).push().getKey();
                Map<String, Object> feedValues = entity.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(String.format("/%s/%s", Firebase.QUERY_FEEDS, key), feedValues);


                database.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        if (databaseError == null) {
                            WrittenEntity writtenEntity = new WrittenEntity();
                            writtenEntity.setKey(key);
                            writtenEntityAsyncEmitter.onNext(writtenEntity);
                            writtenEntityAsyncEmitter.onCompleted();
                        } else {
                            writtenEntityAsyncEmitter.onError(databaseError.toException());
                        }
                    }

                });
            }
        }, AsyncEmitter.BackpressureMode.NONE);


    }

    @Override
    public Observable<DeletedEntity> deleteFeed(final String key) {

        return Observable.fromAsync(new Action1<AsyncEmitter<DeletedEntity>>() {
            @Override
            public void call(final AsyncEmitter<DeletedEntity> deletedEntityAsyncEmitter) {
                database.child(Firebase.QUERY_FEEDS).child(key).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                DeletedEntity deletedEntity = new DeletedEntity();
                                deletedEntityAsyncEmitter.onNext(deletedEntity);
                                deletedEntityAsyncEmitter.onCompleted();
                            } else {
                                deletedEntityAsyncEmitter.onError(databaseError.toException());
                            }
                    }
                });
            }
        }, AsyncEmitter.BackpressureMode.NONE);


    }


    // please note that all of the callback method here will be called from main thread
    static class FeedChangeListener implements ChildEventListener {
        AsyncEmitter<FeedChangedInfoEntity> subscriber;

        public FeedChangeListener(AsyncEmitter<FeedChangedInfoEntity> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            FeedEntity feedEntity = dataSnapshot.getValue(FeedEntity.class);
            feedEntity.setKey(dataSnapshot.getKey());
            final FeedChangedInfoEntity feedChangedInfoEntity = new FeedChangedInfoEntity(EventType.Added, s, dataSnapshot.getKey(), feedEntity);
            subscriber.onNext(feedChangedInfoEntity);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            FeedEntity feedEntity = dataSnapshot.getValue(FeedEntity.class);
            feedEntity.setKey(dataSnapshot.getKey());
            FeedChangedInfoEntity feedChangedInfoEntity = new FeedChangedInfoEntity(EventType.Changed, s, dataSnapshot.getKey(), feedEntity);
            subscriber.onNext(feedChangedInfoEntity);

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            FeedEntity feedEntity = dataSnapshot.getValue(FeedEntity.class);
            feedEntity.setKey(dataSnapshot.getKey());
            FeedChangedInfoEntity feedChangedInfoEntity = new FeedChangedInfoEntity(EventType.Removed, null, dataSnapshot.getKey(), feedEntity);
            subscriber.onNext(feedChangedInfoEntity);

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            FeedEntity feedEntity = dataSnapshot.getValue(FeedEntity.class);
            feedEntity.setKey(dataSnapshot.getKey());
            FeedChangedInfoEntity feedChangedInfoEntity = new FeedChangedInfoEntity(EventType.Moved, s, dataSnapshot.getKey(), feedEntity);
            subscriber.onNext(feedChangedInfoEntity);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            subscriber.onError(new RuntimeException("Data base error"));
        }
    }
}
