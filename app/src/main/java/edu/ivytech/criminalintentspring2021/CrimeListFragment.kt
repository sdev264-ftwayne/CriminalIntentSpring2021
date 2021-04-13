package edu.ivytech.criminalintentspring2021


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ivytech.criminalintentspring2021.databinding.FragmentCrimeBinding
import edu.ivytech.criminalintentspring2021.databinding.FragmentCrimeListBinding
import edu.ivytech.criminalintentspring2021.databinding.ListItemCrimeBinding
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null
    private val crimeListViewModel by lazy {
        ViewModelProvider(requireActivity()).get(CrimeListViewModel::class.java)
    }
    private var _binding: FragmentCrimeListBinding? = null
    private val binding get() = _binding!!
    private var adapter:CrimeAdapter? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        //binding.crimeRecyclerView.layoutManager = GridLayoutManager(context,2)
        updateUI()
        return view
    }

    private fun updateUI() {
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        binding.crimeRecyclerView.adapter = adapter
    }

    private inner class CrimeHolder(itemBinding: ListItemCrimeBinding) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {
        private lateinit var crime: Crime
        val titleTextView : TextView = itemBinding.crimeDisplayTitle
        val dateTextView : TextView = itemBinding.crimeDisplayDate
        init {
            itemBinding.root.setOnClickListener(this)
        }
        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = crime.title
            dateTextView.text = crime.date.toString()
        }

        override fun onClick(v: View?) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
            callbacks?.onCrimeSelected(crime.id)
        }
    }

    private inner class CrimeAdapter(var crimes:List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val itemBinding = ListItemCrimeBinding.inflate(LayoutInflater.from(parent.context),parent, false)
            return CrimeHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemCount(): Int {
            return crimes.size
        }

    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

}