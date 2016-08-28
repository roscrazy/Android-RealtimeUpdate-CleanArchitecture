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

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by MinhNguyen on 8/24/16.
 */


public class FirebaseImpl implements Firebase {

    DatabaseReference database;

    public FirebaseImpl(DatabaseReference database) {
        this.database = database;
    }


    @Override
    public Observable<FeedEntity> feedEntityByIndex(final int index) {
        return null;
    }

    @Override
    public Observable<FeedChangedInfoEntity> registerFeedChangedEvent() {

        return Observable.create(new Observable.OnSubscribe<FeedChangedInfoEntity>() {
            @Override
            public void call(Subscriber<? super FeedChangedInfoEntity> subscriber) {
                registerFeedChangedEvent(subscriber);

            }
        }).onBackpressureBuffer();


    }


    void registerFeedChangedEvent(final Subscriber<? super FeedChangedInfoEntity> subscriber){
        final Query query = database.child(Firebase.QUERY_FEEDS);

        final ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FeedEntity feedEntity = dataSnapshot.getValue(FeedEntity.class);
                feedEntity.setKey(dataSnapshot.getKey());
                FeedChangedInfoEntity feedChangedInfoEntity = new FeedChangedInfoEntity(EventType.Added, s, dataSnapshot.getKey(), feedEntity);
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
        };

        query.addChildEventListener(listener);


        // remove listener when unsubscribed
        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                query.removeEventListener(listener);
            }
        }));
    }


    @Override
    public Observable<WrittenEntity> writeFeed(final FeedEntity entity){
        return Observable.create(new Observable.OnSubscribe<WrittenEntity>() {
            @Override
            public void call(final Subscriber<? super WrittenEntity> subscriber) {
                final String key = database.child(Firebase.QUERY_FEEDS).push().getKey();
                Map<String, Object> feedValues = entity.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(String.format("/%s/%s", Firebase.QUERY_FEEDS, key), feedValues);

                database.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError == null) {
                            WrittenEntity writtenEntity = new WrittenEntity();
                            writtenEntity.setKey(key);
                            subscriber.onNext(writtenEntity);
                            subscriber.onCompleted();
                        }else{
                            subscriber.onError(databaseError.toException());
                        }

                    }
                });


            }
        });
    }

    @Override
    public Observable<DeletedEntity> deleteFeed(final String key) {
        return Observable.create(new Observable.OnSubscribe<DeletedEntity>() {
            @Override
            public void call(final Subscriber<? super DeletedEntity> subscriber) {
                database.child(Firebase.QUERY_FEEDS).child(key).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError == null) {
                            DeletedEntity deletedEntity = new DeletedEntity();
                            subscriber.onNext(deletedEntity);
                        }else{
                            subscriber.onError(databaseError.toException());
                        }
                    }
                });
            }
        });

    }
}
