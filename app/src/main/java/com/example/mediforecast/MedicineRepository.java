package com.example.mediforecast;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MedicineRepository {
    private MedicineDao medicineDao;

    public MedicineRepository(Application application) {
        MedicineDatabase database = MedicineDatabase.getInstance(application);
        medicineDao = database.medicineDao();
    }

    public void insert(Medicine medicine) {
        new InsertMedicineAsyncTask(medicineDao).execute(medicine);
    }

    public LiveData<List<Medicine>> getAllMedicines() {
        return medicineDao.getAllMedicines();
    }

    private static class InsertMedicineAsyncTask extends AsyncTask<Medicine, Void, Void> {
        private MedicineDao medicineDao;

        private InsertMedicineAsyncTask(MedicineDao medicineDao) {
            this.medicineDao = medicineDao;
        }

        @Override
        protected Void doInBackground(Medicine... medicines) {
            medicineDao.insert(medicines[0]);
            return null;
        }
    }
    public void delete(Medicine medicine) {
        new DeleteMedicineAsyncTask(medicineDao).execute(medicine);
    }

    private static class DeleteMedicineAsyncTask extends AsyncTask<Medicine, Void, Void> {
        private MedicineDao medicineDao;

        private DeleteMedicineAsyncTask(MedicineDao medicineDao) {
            this.medicineDao = medicineDao;
        }

        @Override
        protected Void doInBackground(Medicine... medicines) {
            medicineDao.delete(medicines[0]);
            return null;
        }
    }

    public void update(Medicine medicine) {
        new UpdateMedicineAsyncTask(medicineDao).execute(medicine);
    }
    private static class UpdateMedicineAsyncTask extends AsyncTask<Medicine, Void, Void> {
        private MedicineDao medicineDao;

        public UpdateMedicineAsyncTask(MedicineDao medicineDao) {
        this.medicineDao = medicineDao;
        }

        @Override
        protected Void doInBackground(Medicine... medicines) {
            medicineDao.update(medicines[0]);
            return null;
        }
    }
}