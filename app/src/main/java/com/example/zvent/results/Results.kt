package com.example.zvent.results

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.example.zvent.R
import com.example.zvent.results.ResultsArgs
import com.example.zvent.results.ResultsDirections
import com.example.zvent.databinding.FragmentResultsBinding

/**
 * A simple [Fragment] subclass.
 */
class Results : Fragment() {

    private lateinit var viewModel: ResultsViewModel
    @SuppressLint("SetTextI18n")
    /**
     * Builds the fragment
     * @param inflater reads XML file
     * @param container that contains the fragment
     * @param savedInstanceState saved instances of the fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Changes title to its appropriate name
        (activity as AppCompatActivity).supportActionBar?.title = "Resultado"

        // Data binding
        val binding = DataBindingUtil.inflate<FragmentResultsBinding>(inflater,
            R.layout.fragment_results, container, false)
        viewModel = ViewModelProvider(this).get(ResultsViewModel::class.java)

        // Arguments from the guest registration
        val args =
            ResultsArgs.fromBundle(arguments!!)

        // Navigates to guest registration if clicked
        binding.restart.setOnClickListener{
            view!!.findNavController().navigate(ResultsDirections.actionResultsToNavRegister())
        }

        // Arguments are stored in the ViewModel
        viewModel.guestString = args.guestList
        viewModel.numberOfGuests = args.invitedGuests
        viewModel.registered = args.registeredGuests

        // Shows the state of guests if clicked
        binding.viewGuests.setOnClickListener{
            Toast.makeText(view!!.context, viewModel.guestString, Toast.LENGTH_SHORT).show()
        }


        // Updates the invited and registered guests on screen
        binding.Invitedguests.text = "Invitados: " + viewModel.numberOfGuests
        binding.registeredGuests.text = "Registrados: " + viewModel.registered

        // Share menu
        setHasOptionsMenu(true)

        return binding.root
    }

    /**
     * Generates the menu
     * @param menu
     * @param inflater reads XML
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share, menu)

        // If the intent doesn't resolve, it makes the share button invisible
        if(null == getShareIntent().resolveActivity(activity!!.packageManager)) {
            menu.findItem(R.id.share)?.setVisible(false)
        }
    }

    /**
     * Adds functionality to the different menu options
     * @param item from the menu
     * @return Boolean
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareSuccess()    // When share is clicked
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Gets the elements that are going to be exported
     * @return Intent with elements
     */
    private fun getShareIntent() : Intent {
        val args =
            ResultsArgs.fromBundle(arguments!!)  // Arguments from the guest registration
        val shareIntent = Intent(Intent.ACTION_SEND)    // To be exported

        // Sets the content of export
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT,
            getString(R.string.successful_share, viewModel.numberOfGuests, viewModel.registered))

        return shareIntent
    }

    /**
     * Shares the items prepared for export
     */
    private fun shareSuccess() {
        startActivity(getShareIntent())
    }
}
