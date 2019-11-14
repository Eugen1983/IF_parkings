package com.khalaiev_projects.if_parkings;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ParkingViewModel extends AndroidViewModel {

    private ParkingRepository mRepository;

    private LiveData<List<Parking>> mAllParkings;

    public ParkingViewModel (Application application) {
        super(application);
        mRepository = new ParkingRepository(application);
        mAllParkings = mRepository.getAllParkings();
    }

    LiveData<List<Parking>> getAllParkings() { return mAllParkings; }

    public void insert(Parking parking) { mRepository.insert(parking); }

    public void deleteParking(int id) {mRepository.deleteParking(id);}

    public void deleteAllParkings() {
        mRepository.deleteAllParkings();
    }

}
