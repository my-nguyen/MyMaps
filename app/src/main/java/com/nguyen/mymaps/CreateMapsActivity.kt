package com.nguyen.mymaps

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.nguyen.mymaps.databinding.ActivityCreateMapsBinding
import com.nguyen.mymaps.databinding.DialogCreatePlaceBinding
import com.nguyen.mymaps.models.Place
import com.nguyen.mymaps.models.UserMap

private const val TAG = "CreateMapsActivity"

class CreateMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityCreateMapsBinding
    private val markers = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = intent.getStringExtra(EXTRA_MAP_TITLE)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapFragment.view?.let {
            Snackbar.make(it, "Long press to add a marker!", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", {})
                .setActionTextColor(ContextCompat.getColor(this, android.R.color.white))
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create_map, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save) {
            Log.i(TAG, "Tapped on SAVE")
            if (markers.isEmpty()) {
                Toast.makeText(this, "There must be at least one marker", Toast.LENGTH_LONG).show()
                return true
            }
            val title = intent.getStringExtra(EXTRA_MAP_TITLE)!!
            val places = markers.map { Place(it.title!!, it.snippet!!, it.position.latitude, it.position.longitude) }
            val userMap = UserMap(title, places)
            val data = Intent()
            data.putExtra(EXTRA_USER_MAP, userMap)
            setResult(Activity.RESULT_OK, data)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnInfoWindowClickListener {
            Log.i(TAG, "setOnInfoWindowClickListener - delete this marker")
            markers.remove(it)
            it.remove()
        }

        mMap.setOnMapLongClickListener {
            Log.i(TAG, "setOnMapLongClickListener")
            showAlertDialog(it)

        }
        // Add a marker in Silicon Valley and move the camera
        val siliconValley = LatLng(37.4, -122.1)
        // Zoom levels: 1=World; 5=Landmass/continent; 10=City; 15=Streets; 20=Buildings
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(siliconValley, 10f))
    }

    private fun showAlertDialog(latlng: LatLng) {
        val binding = DialogCreatePlaceBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this)
            .setTitle("Create a marker")
            .setView(binding.root)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK", null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val title = binding.title.text.toString().trim()
            val description = binding.description.text.toString().trim()
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Place must have non-empty title and description", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            mMap.addMarker(MarkerOptions().position(latlng).title(title).snippet(description))?.let {
                markers.add(it)
            }
            dialog.dismiss()
        }
    }
}