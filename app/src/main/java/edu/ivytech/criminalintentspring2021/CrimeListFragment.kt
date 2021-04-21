package edu.ivytech.criminalintentspring2021


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
    private var adapter:CrimeAdapter? = CrimeAdapter(emptyList())


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
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
        binding.crimeRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimesLisLiveData.observe(viewLifecycleOwner,
                {crimes ->crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }})
    }

    private fun updateUI(crimes:List<Crime>) {
        adapter = CrimeAdapter(crimes)
        binding.crimeRecyclerView.adapter = adapter
    }

    private inner class CrimeHolder(itemBinding: ListItemCrimeBinding) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {
        private lateinit var crime: Crime
        val titleTextView : TextView = itemBinding.crimeDisplayTitle
        val dateTextView : TextView = itemBinding.crimeDisplayDate
        val image :ImageView = itemBinding.crimeSolved
        init {
            itemBinding.root.setOnClickListener(this)
        }
        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = crime.title
            dateTextView.text = crime.date.toString()
            if(crime.isSolved)
                image.visibility = View.VISIBLE
            else
                image.visibility = View.GONE
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