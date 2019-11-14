package com.khalaiev_projects.if_parkings;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ParkingRepository {

    private ParkingDao mParkingDao;

    private LiveData<List<Parking>> mAllParkings;

    ParkingRepository(Application application) {
        ParkingRoomDatabase db = ParkingRoomDatabase.getDatabase(application);
        mParkingDao = db.parkingDao();
        mAllParkings = mParkingDao.getAllParkings();
    }

    LiveData<List<Parking>> getAllParkings() {
        return mAllParkings;
    }

    public void insert (Parking parking) {
        new insertAsyncTask(mParkingDao).execute(parking);
    }

    public void deleteParking (int id) {
        new deleteParkingAsyncTask(mParkingDao).execute(id);
    }

    public void deleteAllParkings() {
        new deleteAllParkingsAsyncTask(mParkingDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<Parking, Void, Void> {

        private ParkingDao mAsyncTaskDao;

        insertAsyncTask(ParkingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Parking... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteParkingAsyncTask extends AsyncTask<Integer, Void, Void> {

        private ParkingDao mAsyncTaskDao;
        deleteParkingAsyncTask(ParkingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteParking(params[0]);
            return null;
        }
    }

    private static class deleteAllParkingsAsyncTask extends AsyncTask<Void, Void, Void> {

        private ParkingDao mAsyncTaskDao;
        deleteAllParkingsAsyncTask(ParkingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAllParkings();
            return null;
        }
    }
}