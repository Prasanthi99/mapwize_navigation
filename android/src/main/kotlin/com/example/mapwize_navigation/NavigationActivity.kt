package com.example.mapwize_navigation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mapwize_navigation.R
import io.indoorlocation.core.IndoorLocation
import io.indoorlocation.manual.ManualIndoorLocationProvider
import io.mapwize.mapwizesdk.api.*
import io.mapwize.mapwizesdk.map.MapOptions
import io.mapwize.mapwizesdk.map.MapwizeMap
import io.mapwize.mapwizeui.MapwizeFragmentUISettings
import io.mapwize.mapwizeui.events.Channel
import io.mapwize.mapwizeui.events.EventManager
import io.mapwize.mapwizeui.events.OnEventListener
import io.mapwize.mapwizeui.MapwizeFragment
import io.mapwize.mapwizeui.MapwizeUIView
import kotlinx.android.synthetic.main.activity_main.*

class NavigationActivity : AppCompatActivity(), MapwizeUIView.OnViewInteractionListener, OnEventListener {
    override fun onMenuButtonClick() {
        Toast.makeText(applicationContext, "Menu click", Toast.LENGTH_LONG).show()
    }

    private var mapwizeFragment: MapwizeFragment? = null
    private var mapwizeMap: MapwizeMap? = null
    private var provider: ManualIndoorLocationProvider? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var floor: Double?  =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        latitude = intent.getDoubleExtra("lat", 0.0)
        longitude = intent.getDoubleExtra("long", 0.0)
        floor = intent.getDoubleExtra("floor", 0.0)

        // Uncomment and fill place holder to test MapwizeUI on your venue
        var opts = MapOptions.Builder()
                //.restrictContentToOrganization("YOUR_ORGANIZATION_ID")
               // .restrictContentToVenue("YOUR_VENUE_ID")
                //.centerOnVenue("5ee8eef0042588001617f5eb")
                // .centerOnVenue("56b20714c3fa800b00d8f0b5")
                .centerOnPlace("5d08d8a4efe1d20012809ee5")
                .build()
        if (latitude != 0.0 && longitude != 0.0) {
            opts = MapOptions.Builder()
                //.restrictContentToOrganization("YOUR_ORGANIZATION_ID")
                //.
                .centerOnVenue("5ee8eef0042588001617f5eb")
                //.restrictContentToVenueId("5ee8eef0042588001617f5eb")
                // .centerOnVenue("56b20714c3fa800b00d8f0b5")
               // .centerOnPlace("5d08d8a4efe1d20012809ee5")
                .build()
        }

        // Uncomment and change value to test different settings configuration
        val uiSettings = MapwizeFragmentUISettings.Builder()
                /*.menuButtonHidden(true)
                .followUserButtonHidden(true)
                .floorControllerHidden(true)
                .compassHidden(true)*/
                .build()
        mapwizeFragment = MapwizeFragment.newInstance(opts, uiSettings)
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(fragmentContainer.id, mapwizeFragment!!)
        ft.commit()

        EventManager.configure(this)

    }

    override fun onFollowUserButtonClickWithoutLocation() {
        Toast.makeText(this, "onFollowUserButtonClickWithoutLocation", Toast.LENGTH_LONG).show()
    }

    override fun onFragmentReady(mapwizeMap: MapwizeMap?) {
        this.mapwizeMap = mapwizeMap
        this.provider = ManualIndoorLocationProvider()
        this.mapwizeMap!!.setIndoorLocationProvider(this.provider!!)
        this.mapwizeMap!!.addOnClickListener {
            val il = IndoorLocation("manual", it.latLngFloor.latitude, it.latLngFloor.longitude, it.latLngFloor.floor, System.currentTimeMillis())
            provider!!.setIndoorLocation(il)
        }
        if(latitude!=0.0 && longitude != 0.0) {
            val il = IndoorLocation("manual", 17.43642289789757, 78.39690203324847, floor, System.currentTimeMillis())
            provider!!.setIndoorLocation(il)
        }
         Toast.makeText(this, "latitude $latitude longitude $longitude floor $floor", Toast.LENGTH_LONG).show()
       
    }

    override fun onInformationButtonClick(mapwizeObject: MapwizeObject?) {
        Toast.makeText(this, "onInformationButtonClick", Toast.LENGTH_LONG).show()
    }

    override fun shouldDisplayFloorController(floors: MutableList<Floor>?): Boolean {
        return true
    }

    override fun shouldDisplayInformationButton(mapwizeObject: MapwizeObject?): Boolean {
        return true
    }

    override fun onContentSelect(place: Place,
                                 currentUniverse: Universe,
                                 searchResultUniverse: Universe,
                                 channel: Channel,
                                 searchQuery: String?) {
        Log.i("Debug", "" + place.name + " " + currentUniverse.name +  " " + channel + " " + searchQuery)
    }

    override fun onDirectionStart(venue: Venue, universe: Universe?, from: DirectionPoint?, to: DirectionPoint?, mode: String?, isNavigation: Boolean) {
        Log.i("Debug", "" + venue.name + " " + universe?.name +  " " + from + " " + to + " " + mode + " " + isNavigation)
    }

    override fun onContentSelect(placelist: Placelist, currentUniverse: Universe, searchResultUniverse: Universe, channel: Channel, searchQuery: String?) {
        Log.i("Debug", "" + placelist.name + " " + currentUniverse.name +  " " + channel + " " + searchQuery)
    }

}