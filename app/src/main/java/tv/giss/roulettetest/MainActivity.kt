package tv.giss.roulettetest

/**
 * Created by chevil@giss.tv on 22/01/21.
 */

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentActivity

import android.os.Bundle 
import android.os.Handler 
import android.os.Looper 
import android.content.Context
import android.util.Log
import android.widget.Toast
import android.view.View 
import android.graphics.Color
import android.graphics.BitmapFactory

// access views with their ids
import kotlinx.android.synthetic.main.main_layout.*

import tv.giss.roulette.RouletteWheel 
import tv.giss.roulette.RouletteView 
import tv.giss.roulette.RouletteSection 

import kotlin.collections.List
import kotlin.collections.ArrayList

import java.security.SecureRandom

class MainActivity : FragmentActivity() {

    // static data
    companion object {
      lateinit var rouletteSections : ArrayList<RouletteSection>;
    }

    // Creating the main UI
    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.main_layout)

       loadSections()

       // adding roulette sections ( 6 )
       wheel.addRouletteSections( rouletteSections )

       // action for shoot button
       shoot.setOnClickListener {
          wheel.rotate( (0..(rouletteSections.size-1)).random(), true) { res -> 
                               Log.v( this::class.simpleName, "Wheel Positon : " + res)
                               if ( res == 4 )  { 
                                  ape.setVisibility( View.VISIBLE )
                                  Handler(getMainLooper()).postDelayed( { ape.setVisibility( View.GONE ) }, 5000 )
                               }
                            }
       }
    }

    override fun onResume() {
       super.onResume()
    }

    private fun loadSections() {
        rouletteSections = ArrayList<RouletteSection>();
        rouletteSections.add(RouletteSection(Color.parseColor("#333333"), BitmapFactory.decodeResource(getResources(), R.drawable.loaded)))
        rouletteSections.add(RouletteSection(Color.parseColor("#555555"), BitmapFactory.decodeResource(getResources(), R.drawable.empty)))
        rouletteSections.add(RouletteSection(Color.parseColor("#777777"), BitmapFactory.decodeResource(getResources(), R.drawable.empty)))
        rouletteSections.add(RouletteSection(Color.parseColor("#999999"), BitmapFactory.decodeResource(getResources(), R.drawable.empty)))
        rouletteSections.add(RouletteSection(Color.parseColor("#bbbbbb"), BitmapFactory.decodeResource(getResources(), R.drawable.empty)))
        rouletteSections.add(RouletteSection(Color.parseColor("#dddddd"), BitmapFactory.decodeResource(getResources(), R.drawable.empty)))
    }

}
