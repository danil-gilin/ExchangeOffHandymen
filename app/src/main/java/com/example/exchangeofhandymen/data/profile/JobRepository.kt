package com.example.exchangeofhandymen.data.profile

import android.util.Log
import androidx.core.net.toUri
import com.example.exchangeofhandymen.entity.GeoPosition
import com.example.exchangeofhandymen.entity.job.Job
import com.example.exchangeofhandymen.entity.job.JobWithId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class JobRepository @Inject constructor() {
    private val dbFirestoreJob: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage.reference

    suspend fun saveJob(job: Job) {
        var imgList = arrayListOf<String>()
        val indexJob = dbFirestoreJob.collection("Job").whereEqualTo("employer", job.employer).get()
            .await().documents.size
        val imgReference =
            Firebase.storage.reference.child("Job/${job.employer}/Job_$indexJob").listAll().await()
        imgReference.items.forEach {
            it.delete().await()
        }
        job.img.forEachIndexed { index, it ->
            val storageLocal =
                storage.child("Job").child("${job.employer}").child("Job_$indexJob/$index")
            storageLocal.putFile(it.toUri()).await()
            val uri = storageLocal.downloadUrl.await()
            imgList.add(uri.toString())
        }
        val jobtoDb = Job(
            job.employer,
            job.workers,
            job.geopoint,
            imgList,
            job.money,
            job.countWorkers,
            job.info,
            job.name,
            job.phone,
            job.workerAccept
        )
        dbFirestoreJob.collection("Job").add(jobtoDb).await()
    }

    suspend fun getJobEmployer(uid: String, workerUid: String? = null): List<JobWithId> {
        val listJob =
            dbFirestoreJob.collection("Job").whereEqualTo("employer", uid).get().await().documents
        val rezult = arrayListOf<JobWithId>()
        listJob.forEach {
            val id = it.id
            val employer = it.get("employer") as String
            var workers = listOf<String>()
            if ((it.get("workers") as List<*>).isNotEmpty()) {
                workers = it.get("workers") as List<String>
            }
            val geopositionWorker = it.getGeoPoint("geopoint")
            var mygeo: GeoPosition? = null
            if (geopositionWorker != null) {
                mygeo = GeoPosition(geopositionWorker!!.latitude, geopositionWorker!!.longitude)
            }
            var img = listOf<String>()
            if ((it.get("img") as List<*>).isNotEmpty()) {
                img = it.get("img") as List<String>
            }
            val money: String = it.get("money") as String
            val countWorkers: String = it.get("countWorkers") as String
            val info: String = it.get("info") as String
            val name: String = it.get("name") as String
            val phone: String = it.get("phone") as String
            var workersAccept = listOf<String>()
            if ((it.get("workerAccept") as List<*>).isNotEmpty()) {
                workersAccept = it.get("workerAccept") as List<String>
            }
            val job = JobWithId(
                id,
                employer,
                workers,
                mygeo,
                img,
                money,
                countWorkers,
                info,
                name,
                phone,
                workersAccept
            )
            if (workerUid != null) {
                if (workerUid !in workersAccept && workerUid !in workers) {
                    if (workersAccept.size != countWorkers.toInt()) {
                        rezult.add(job)
                    }
                }
            } else {
                rezult.add(job)
            }
        }
        return rezult
    }


    suspend fun deleteJob(uidJob: String, imgList: List<String>) {
        imgList.forEach {
            val imgReference = Firebase.storage.getReferenceFromUrl(it.toUri().toString())
            Log.d("deleteJob", imgReference.parent.toString())
            imgReference.delete().await()
        }
        dbFirestoreJob.collection("Job").document(uidJob).delete()
    }

    suspend fun inviteWorker(idJob: String, workerUid: String) {
        val document = dbFirestoreJob.collection("Job").document(idJob).get().await().get("workers")
        var workers = listOf<String>()
        if ((document as List<*>).isNotEmpty()) {
            workers = document as List<String>
        }
        if (workerUid !in workers) {
            workers = workers.plus(workerUid)
        }
        dbFirestoreJob.collection("Job").document(idJob).update("workers", workers)
    }

    suspend fun jobForWorker(uidJob: String): List<JobWithId> {
        val listJob = dbFirestoreJob.collection("Job").whereArrayContains("workers", uidJob).get()
            .await().documents
        Log.d("jobForWorker", listJob.toString())

        val rezult = arrayListOf<JobWithId>()
        listJob.forEach {
            val id = it.id
            val employer = it.get("employer") as String
            var workers = listOf<String>()
            if ((it.get("workers") as List<*>).isNotEmpty()) {
                workers = it.get("workers") as List<String>
            }

            val geopositionWorker = it.getGeoPoint("geopoint")
            var mygeo: GeoPosition? = null
            if (geopositionWorker != null) {
                mygeo = GeoPosition(geopositionWorker!!.latitude, geopositionWorker!!.longitude)
            }

            var img = listOf<String>()
            if ((it.get("img") as List<*>).isNotEmpty()) {
                img = it.get("img") as List<String>
            }
            val money: String = it.get("money") as String
            val countWorkers: String = it.get("countWorkers") as String
            val info: String = it.get("info") as String
            val name: String = it.get("name") as String
            val phone: String = it.get("phone") as String

            var workersAccept = listOf<String>()
            if ((it.get("workerAccept") as List<*>).isNotEmpty()) {
                workersAccept = it.get("workerAccept") as List<String>
            }

            val job = JobWithId(
                id,
                employer,
                workers,
                mygeo,
                img,
                money,
                countWorkers,
                info,
                name,
                phone,
                workersAccept
            )

            if (workersAccept.size != countWorkers.toInt()) {
                rezult.add(job)
            }
        }
        return rezult
    }


    suspend fun workerSelectJob(uidJob: String, uidWorker: String) {
        var workersAccept = listOf<String>()
        val workerAcceptJob = dbFirestoreJob.collection("Job").document(uidJob).get().await()
            .get("workerAccept") as List<*>
        if (workerAcceptJob.isNotEmpty()) {
            workersAccept = workerAcceptJob as List<String>
        }
        workerDeleteJob(uidJob, uidWorker)
        workersAccept = workersAccept.plus(uidWorker)
        dbFirestoreJob.collection("Job").document(uidJob).update("workerAccept", workersAccept)
    }

    suspend fun workerDeleteJob(uidJob: String, uidWorker: String) {
        var workers = listOf<String>()
        val workerJob = dbFirestoreJob.collection("Job").document(uidJob).get().await()
            .get("workers") as List<*>
        if (workerJob.isNotEmpty()) {
            workers = workerJob as List<String>
        }
        workers = workers.minus(uidWorker)
        dbFirestoreJob.collection("Job").document(uidJob).update("workers", workers)
    }

}