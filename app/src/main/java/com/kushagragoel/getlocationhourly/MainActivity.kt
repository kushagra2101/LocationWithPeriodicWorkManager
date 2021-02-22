package com.kushagragoel.getlocationhourly

import android.Manifest.permission
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.kushagragoel.getlocationhourly.databinding.ActivityMainBinding
import com.kushagragoel.getlocationhourly.ui.GlhViewModelFactory
import com.kushagragoel.getlocationhourly.ui.LocationDataRecyclerViewAdapter
import com.kushagragoel.getlocationhourly.ui.LocationFetchViewModel
import com.kushagragoel.getlocationhourly.work.FetchLocationWorker
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val PERMISSION_REQUEST_CODE = 200
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        val application = requireNotNull(this.application)
        val viewModelFactory = GlhViewModelFactory(application)
        val viewModel: LocationFetchViewModel = ViewModelProvider(this,viewModelFactory).get(
            LocationFetchViewModel::class.java)
        val adapter = LocationDataRecyclerViewAdapter()
        binding.locationDataRecyclerView.adapter = adapter

        viewModel.allLocations.observe(this, { t -> adapter.submitList(t) })

        try {
            if (isWorkScheduled(WorkManager.getInstance().
                getWorkInfosByTag(FetchLocationWorker.WORK_NAME).get())) {
                binding.button.text = getString(R.string.button_text_stop)
            } else {
                binding.button.text = getString(R.string.button_text_start)
            }
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        binding.button.setOnClickListener {
            if (binding.button.text.toString()
                    .contentEquals(getString(R.string.button_text_start))
            ) {
                // START Worker
                val periodicWork =
                    PeriodicWorkRequest.Builder(
                        FetchLocationWorker::class.java,
                        15,
                        TimeUnit.MINUTES
                    )
                        .addTag(FetchLocationWorker.WORK_NAME)
                        .build()
                WorkManager.getInstance().enqueueUniquePeriodicWork(
                    FetchLocationWorker.WORK_NAME,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    periodicWork
                )
                Toast.makeText(
                    this@MainActivity,
                    "Location Worker Started : " + periodicWork.id,
                    Toast.LENGTH_SHORT
                ).show()
                binding.button.text = getString(R.string.button_text_stop)
            } else {
                WorkManager.getInstance().cancelAllWorkByTag(FetchLocationWorker.WORK_NAME)
                binding.button.text = getString(R.string.button_text_start)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun isWorkScheduled(workInfos: List<WorkInfo>?): Boolean {
        var running = false
        if (workInfos.isNullOrEmpty())
            return false
        for (workStatus in workInfos) {
            running =
                workStatus.state == WorkInfo.State.RUNNING || workStatus.state == WorkInfo.State.ENQUEUED
        }
        return running
    }

    /**
     * All about permission
     */
    private fun checkLocationPermission(): Boolean {
        val result3 = ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
        val result4 = ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
        return result3 == PackageManager.PERMISSION_GRANTED &&
                result4 == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                val coarseLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val fineLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (coarseLocation && fineLocation) Toast.makeText(
                    this,
                    "Permission Granted",
                    Toast.LENGTH_SHORT
                ).show() else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}