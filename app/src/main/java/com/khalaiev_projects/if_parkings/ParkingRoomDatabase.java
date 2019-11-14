package com.khalaiev_projects.if_parkings;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Parking.class}, version = 1)
public abstract class ParkingRoomDatabase extends RoomDatabase {

    public abstract ParkingDao parkingDao();

    private static ParkingRoomDatabase INSTANCE;

    static ParkingRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ParkingRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ParkingRoomDatabase.class, "parking_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more words, just add them.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ParkingDao mDao;
        Parking[] parkings = {new Parking(48.915553d, 24.713686d, "вул. Мельника, 10")
                , new Parking(48.918400d, 24.711063d, "вул. Євгена Коновальця, 13")
                , new Parking(48.917354d, 24.730247d, "вул. Незалежності, 126")};

        PopulateDbAsync(ParkingRoomDatabase db) {
            mDao = db.parkingDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            if (INSTANCE.parkingDao().getCount() == 0) {
                mDao.deleteAllParkings();

                for (int i = 0; i <= parkings.length - 1; i++) {
                    Parking parking = parkings[i];
                    mDao.insert(parking);
                }
            }
            return null;
        }
    }
}
