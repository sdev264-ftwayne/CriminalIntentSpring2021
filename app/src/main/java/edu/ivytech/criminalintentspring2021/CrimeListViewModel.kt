package edu.ivytech.criminalintentspring2021

import androidx.lifecycle.ViewModel
import java.util.*

class CrimeListViewModel : ViewModel() {
    val crimes = mutableListOf<Crime>()

    init {
        for (i in 0 until 100) {
            val crime = Crime()
            crime.title = "Crime #$i"
            crime.isSolved = i % 2 == 0
            crimes += crime
        }
    }
    fun getCrime(crimeid: UUID): Crime {
        for(crime in crimes) {
          if(crime.id == crimeid)
              return crime;
        }
        val crime = Crime()
        crimes += crime
        return crime
    }
}